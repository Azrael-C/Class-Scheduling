package ui;

import Database.DBConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.io.File;

public class ClassDetailsFrame extends JFrame {

    private JPanel contentPane;
    private JTable studentsTable;
    private DefaultTableModel studentsTableModel;
    private JButton btnBack;
    private int classId;
    private Font poppinsRegular, poppinsMedium, poppinsBold;

    public ClassDetailsFrame(int classId) {
        this.classId = classId;
        
        
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setTitle("Class Details");

        
        contentPane = new JPanel();
        contentPane.setBackground(new Color(255, 255, 255)); 
        contentPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        
        try {
            poppinsRegular = Font.createFont(Font.TRUETYPE_FONT, new File("src/fonts/Poppins-Regular.ttf")).deriveFont(22f);
            poppinsMedium = Font.createFont(Font.TRUETYPE_FONT, new File("src/fonts/Poppins-Medium.ttf")).deriveFont(26f);
            poppinsBold = Font.createFont(Font.TRUETYPE_FONT, new File("src/fonts/Poppins-Bold.ttf")).deriveFont(40f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(poppinsRegular);
            ge.registerFont(poppinsMedium);
            ge.registerFont(poppinsBold);
        } catch (Exception e) {
            poppinsRegular = new Font("SansSerif", Font.PLAIN, 22);
            poppinsMedium = new Font("SansSerif", Font.BOLD, 26);
            poppinsBold = new Font("SansSerif", Font.BOLD, 40);
        }

        
        JLabel lblTitle = new JLabel("Class Details");
        lblTitle.setFont(poppinsBold.deriveFont(40f));
        lblTitle.setBounds(30, 20, 400, 50);
        lblTitle.setForeground(new Color(26, 35, 126));
        contentPane.add(lblTitle);

        
        loadClassDetails();

        
        btnBack = new JButton("Back");
        btnBack.setFont(poppinsMedium.deriveFont(24f));
        btnBack.setBounds(1350, 680, 150, 49);
        btnBack.setBackground(new Color(26, 35, 126));
        btnBack.setForeground(Color.WHITE);
        contentPane.add(btnBack);

        
        btnBack.addActionListener(e -> {
            this.dispose(); 
        });
    }

    private void loadClassDetails() {
        try (Connection conn = DBConnection.getConnection()) {
            
            String classDetailsQuery = """
                SELECT 
                    c.class_id,
                    sub.subject_code,
                    sub.subject_name,
                    sub.units,
                    sub.category,
                    t.full_name as teacher_name,
                    t.department as teacher_department,
                    t.specialization,
                    r.room_code,
                    r.building,
                    r.capacity,
                    c.schedule_day,
                    c.start_time,
                    c.end_time,
                    c.duration,
                    c.status,
                    sem.semester_name,
                    sem.academic_year
                FROM classes c
                JOIN subjects sub ON c.subject_id = sub.subject_id
                JOIN teachers t ON c.teacher_id = t.teacher_id
                JOIN classrooms r ON c.room_id = r.room_id
                JOIN semesters sem ON c.semester_id = sem.semester_id
                WHERE c.class_id = ?
            """;

            try (PreparedStatement stmt = conn.prepareStatement(classDetailsQuery)) {
                stmt.setInt(1, classId);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    createClassInfoPanel(rs);
                } else {
                    JOptionPane.showMessageDialog(this, "Class not found.", "Error", JOptionPane.ERROR_MESSAGE);
                    this.dispose();
                    return;
                }
            }

            
            loadEnrolledStudents();

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading class details: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void createClassInfoPanel(ResultSet rs) throws SQLException {
        
        JPanel classInfoPanel = new JPanel();
        classInfoPanel.setBounds(30, 90, 600, 400);
        classInfoPanel.setLayout(new GridLayout(12, 2, 10, 15));
        classInfoPanel.setBackground(new Color(211, 211, 211));
        classInfoPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(26, 35, 126), 2), 
            "Class Information", 
            0, 0, 
            poppinsMedium.deriveFont(16f), 
            new Color(26, 35, 126)
        ));
        contentPane.add(classInfoPanel);

        
        addInfoField(classInfoPanel, "Class ID:", String.valueOf(rs.getInt("class_id")));
        addInfoField(classInfoPanel, "Subject Code:", rs.getString("subject_code"));
        addInfoField(classInfoPanel, "Subject Name:", rs.getString("subject_name"));
        addInfoField(classInfoPanel, "Units:", String.valueOf(rs.getInt("units")));
        addInfoField(classInfoPanel, "Category:", rs.getString("category"));
        addInfoField(classInfoPanel, "Teacher:", rs.getString("teacher_name"));
        addInfoField(classInfoPanel, "Department:", rs.getString("teacher_department"));
        addInfoField(classInfoPanel, "Specialization:", rs.getString("specialization"));
        addInfoField(classInfoPanel, "Room:", rs.getString("room_code") + " (" + rs.getString("building") + ")");
        addInfoField(classInfoPanel, "Capacity:", String.valueOf(rs.getInt("capacity")));
        addInfoField(classInfoPanel, "Schedule:", rs.getString("schedule_day") + ", " 
            + rs.getString("start_time") + " - " + rs.getString("end_time") 
            + " (" + rs.getInt("duration") + " min)");
        addInfoField(classInfoPanel, "Status:", rs.getString("status").toUpperCase());
    }

    private void addInfoField(JPanel panel, String label, String value) {
        JLabel lblField = new JLabel(label);
        lblField.setFont(poppinsMedium.deriveFont(14f));
        lblField.setForeground(new Color(26, 35, 126));
        panel.add(lblField);

        JLabel lblValue = new JLabel(value);
        lblValue.setFont(poppinsRegular.deriveFont(14f));
        panel.add(lblValue);
    }

    private void loadEnrolledStudents() {
        try (Connection conn = DBConnection.getConnection()) {
            
            String studentsQuery = """
                SELECT 
                    s.student_number,
                    s.full_name,
                    p.program_name,
                    s.year_level,
                    s.section,
                    e.status as enrollment_status,
                    e.date_enrolled
                FROM enrollments e
                JOIN students s ON e.student_id = s.student_id
                JOIN programs p ON s.program_id = p.program_id
                WHERE e.class_id = ?
                ORDER BY s.student_number
            """;

            
            studentsTableModel = new DefaultTableModel(
                new Object[][]{},
                new String[]{"Student Number", "Full Name", "Program", "Year", "Section", "Status", "Date Enrolled"}
            ) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false; 
                }
            };

            studentsTable = new JTable(studentsTableModel);
            studentsTable.setFont(poppinsRegular.deriveFont(12f));
            studentsTable.getTableHeader().setFont(poppinsMedium.deriveFont(12f));
            studentsTable.setRowHeight(34); 
            
            javax.swing.table.DefaultTableCellRenderer centerRenderer = new javax.swing.table.DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(javax.swing.JLabel.CENTER);
            for (int i = 0; i < studentsTable.getColumnCount(); i++) {
                studentsTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            }
            studentsTable.getTableHeader().setBackground(new Color(26, 35, 126));
            studentsTable.getTableHeader().setForeground(Color.WHITE);

            JScrollPane scrollPane = new JScrollPane(studentsTable);
            scrollPane.setBounds(650, 90, 900, 500);
            scrollPane.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(26, 35, 126), 2), 
                "Enrolled Students", 
                0, 0, 
                poppinsMedium.deriveFont(16f), 
                new Color(26, 35, 126)
            ));
            contentPane.add(scrollPane);

            
            try (PreparedStatement stmt = conn.prepareStatement(studentsQuery)) {
                stmt.setInt(1, classId);
                ResultSet rs = stmt.executeQuery();

                int studentCount = 0;
                while (rs.next()) {
                    Object[] row = {
                        rs.getString("student_number"),
                        rs.getString("full_name"),
                        rs.getString("program_name"),
                        rs.getString("year_level"),
                        rs.getString("section"),
                        rs.getString("enrollment_status").toUpperCase(),
                        rs.getTimestamp("date_enrolled") != null ? 
                            rs.getTimestamp("date_enrolled").toString().split(" ")[0] : "N/A"
                    };
                    studentsTableModel.addRow(row);
                    studentCount++;
                }

                
                JLabel lblStudentCount = new JLabel("Total Enrolled: " + studentCount + " students");
                lblStudentCount.setFont(poppinsMedium.deriveFont(16f));
                lblStudentCount.setForeground(new Color(26, 35, 126));
                lblStudentCount.setBounds(650, 600, 300, 30);
                contentPane.add(lblStudentCount);

                if (studentCount == 0) {
                    JLabel lblNoStudents = new JLabel("No students enrolled in this class yet.");
                    lblNoStudents.setFont(poppinsRegular.deriveFont(Font.ITALIC, 14f));
                    lblNoStudents.setForeground(Color.GRAY);
                    lblNoStudents.setBounds(650, 620, 300, 30);
                    contentPane.add(lblNoStudents);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading enrolled students: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
