package ui;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.JButton;

import Database.DBConnection;

public class AddRoom extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField textField; 
    private JTextField textField_1; 
    private JTextField textField_2; 
    private JTextField textField_3; 
    private JTextField textField_4; 
    private Font poppinsRegular, poppinsMedium, poppinsBold;

    
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    AddRoom frame = new AddRoom();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    
    public AddRoom() {
        
        try {
            poppinsRegular = Font.createFont(Font.TRUETYPE_FONT, new File("src/fonts/Poppins-Regular.ttf")).deriveFont(22f);
            poppinsMedium = Font.createFont(Font.TRUETYPE_FONT, new File("src/fonts/Poppins-Medium.ttf")).deriveFont(26f);
            poppinsBold = Font.createFont(Font.TRUETYPE_FONT, new File("src/fonts/Poppins-Bold.ttf")).deriveFont(60f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(poppinsRegular);
            ge.registerFont(poppinsMedium);
            ge.registerFont(poppinsBold);
        } catch (Exception e) {
            System.out.println("Font loading failed, using default fonts.");
            poppinsRegular = new Font("SansSerif", Font.PLAIN, 22);
            poppinsMedium = new Font("SansSerif", Font.BOLD, 26);
            poppinsBold = new Font("SansSerif", Font.BOLD, 60);
        }

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        contentPane = new JPanel(null);
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);

        
        JPanel panel = new JPanel(null);
        panel.setBackground(new Color(26, 35, 126));
        panel.setBounds(0, 0, 1920, 1080);
        contentPane.add(panel);

        
        int panelWidth = (int) (1920 * 0.6);  
        int panelHeight = (int) (1080 * 0.8); 
        int panelX = (1920 - panelWidth) / 2;
        int panelY = (1080 - panelHeight) / 2;

        JPanel panel_1 = new JPanel(null);
        panel_1.setBackground(Color.WHITE);
        panel_1.setBounds(panelX, panelY, panelWidth, panelHeight);
        panel.add(panel_1);

        
        int titleWidth = 600;
        int titleX = (panelWidth - titleWidth) / 2;
        JLabel lblAddRoom = new JLabel("Add Room");
        lblAddRoom.setHorizontalAlignment(SwingConstants.CENTER);
        lblAddRoom.setForeground(new Color(26, 35, 126));
        lblAddRoom.setFont(poppinsBold.deriveFont(50f));
        lblAddRoom.setBounds(titleX, 50, titleWidth, 70);
        panel_1.add(lblAddRoom);

        
        int labelX = 150;
        int fieldX = 400;
        int fieldWidth = 600;
        int fieldHeight = 40;
        int gap = 60;  
        int startY = 150;

        
        JLabel lblRoomNumber = new JLabel("Room Number");
        lblRoomNumber.setHorizontalAlignment(SwingConstants.LEFT);
        lblRoomNumber.setForeground(new Color(26, 35, 126));
        lblRoomNumber.setFont(poppinsRegular.deriveFont(25f));
        lblRoomNumber.setBounds(labelX, startY, 200, 40);
        panel_1.add(lblRoomNumber);

        textField = new JTextField();
        textField.setHorizontalAlignment(SwingConstants.LEFT);
        textField.setForeground(new Color(26, 35, 126));
        textField.setFont(poppinsRegular.deriveFont(20f));
        textField.setBounds(fieldX, startY, fieldWidth, fieldHeight);
        panel_1.add(textField);
        textField.setColumns(10);

        
        JLabel lblLocation = new JLabel("Location");
        lblLocation.setHorizontalAlignment(SwingConstants.LEFT);
        lblLocation.setForeground(new Color(26, 35, 126));
        lblLocation.setFont(poppinsRegular.deriveFont(25f));
        lblLocation.setBounds(labelX, startY + gap, 200, 40);
        panel_1.add(lblLocation);

        textField_1 = new JTextField();
        textField_1.setHorizontalAlignment(SwingConstants.LEFT);
        textField_1.setForeground(new Color(26, 35, 126));
        textField_1.setFont(poppinsRegular.deriveFont(20f));
        textField_1.setBounds(fieldX, startY + gap, fieldWidth, fieldHeight);
        panel_1.add(textField_1);
        textField_1.setColumns(10);

        
        JLabel lblFloor = new JLabel("Floor");
        lblFloor.setHorizontalAlignment(SwingConstants.LEFT);
        lblFloor.setForeground(new Color(26, 35, 126));
        lblFloor.setFont(poppinsRegular.deriveFont(25f));
        lblFloor.setBounds(labelX, startY + 2 * gap, 140, 35);
        panel_1.add(lblFloor);

        textField_2 = new JTextField();
        textField_2.setHorizontalAlignment(SwingConstants.LEFT);
        textField_2.setForeground(new Color(26, 35, 126));
        textField_2.setFont(poppinsRegular.deriveFont(20f));
        textField_2.setBounds(fieldX, startY + 2 * gap, fieldWidth, fieldHeight);
        panel_1.add(textField_2);
        textField_2.setColumns(10);

        
        JLabel lblCapacity = new JLabel("Capacity");
        lblCapacity.setHorizontalAlignment(SwingConstants.LEFT);
        lblCapacity.setForeground(new Color(26, 35, 126));
        lblCapacity.setFont(poppinsRegular.deriveFont(25f));
        lblCapacity.setBounds(labelX, startY + 3 * gap, 140, 35);
        panel_1.add(lblCapacity);

        textField_3 = new JTextField();
        textField_3.setHorizontalAlignment(SwingConstants.LEFT);
        textField_3.setForeground(new Color(26, 35, 126));
        textField_3.setFont(poppinsRegular.deriveFont(20f));
        textField_3.setBounds(fieldX, startY + 3 * gap, fieldWidth, fieldHeight);
        panel_1.add(textField_3);
        textField_3.setColumns(10);

        
        JLabel lblCollege = new JLabel("College");
        lblCollege.setHorizontalAlignment(SwingConstants.LEFT);
        lblCollege.setForeground(new Color(26, 35, 126));
        lblCollege.setFont(poppinsRegular.deriveFont(25f));
        lblCollege.setBounds(labelX, startY + 4 * gap, 140, 35);
        panel_1.add(lblCollege);

        textField_4 = new JTextField();
        textField_4.setHorizontalAlignment(SwingConstants.LEFT);
        textField_4.setForeground(new Color(26, 35, 126));
        textField_4.setFont(poppinsRegular.deriveFont(20f));
        textField_4.setBounds(fieldX, startY + 4 * gap, fieldWidth, fieldHeight);
        panel_1.add(textField_4);
        textField_4.setColumns(10);

        
        int btnWidth = 200;
        int btnHeight = 45;
        int btnY = panelHeight - 100;  
        int btnGap = 50;
        int btnStartX = (panelWidth - (btnWidth * 2 + btnGap)) / 2;

        JButton btnBack = new JButton("Back");
        btnBack.setForeground(Color.WHITE);
        btnBack.setFont(poppinsBold.deriveFont(22f));
        btnBack.setBackground(new Color(26, 35, 126));
        btnBack.setBounds(btnStartX, btnY, btnWidth, btnHeight);
        btnBack.setFocusPainted(false);
        btnBack.setBorderPainted(false);
        panel_1.add(btnBack);
        btnBack.addActionListener(e -> {
            dispose();
            new classroomManagement().setVisible(true);
        });

        JButton btnSubmit = new JButton("Submit");
        btnSubmit.setForeground(Color.WHITE);
        btnSubmit.setFont(poppinsBold.deriveFont(22f));
        btnSubmit.setBackground(new Color(26, 35, 126));
        btnSubmit.setBounds(btnStartX + btnWidth + btnGap, btnY, btnWidth, btnHeight);
        btnSubmit.setFocusPainted(false);
        btnSubmit.setBorderPainted(false);
        panel_1.add(btnSubmit);
        btnSubmit.addActionListener(e -> {
        	addRoom();
        	dispose();
            new classroomManagement().setVisible(true);
        });

        
        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                int width = getWidth();
                int height = getHeight();
                panel.setBounds(0, 0, width, height);
                int panelWidth_new = (int) (width * 0.8);
                int panelHeight_new = (int) (height * 0.85);
                int panelX_new = (width - panelWidth_new) / 2;
                int panelY_new = (height - panelHeight_new) / 2;
                panel_1.setBounds(panelX_new, panelY_new, panelWidth_new, panelHeight_new);

                
                int titleX_new = (panelWidth_new - titleWidth) / 2;
                lblAddRoom.setBounds(titleX_new, 50, titleWidth, 70);

                
                int fieldX_new = (panelWidth_new - fieldWidth) / 2 + 100; 
                textField.setBounds(fieldX_new, startY, fieldWidth, fieldHeight);
                textField_1.setBounds(fieldX_new, startY + gap, fieldWidth, fieldHeight);
                textField_2.setBounds(fieldX_new, startY + 2 * gap, fieldWidth, fieldHeight);
                textField_3.setBounds(fieldX_new, startY + 3 * gap, fieldWidth, fieldHeight);
                textField_4.setBounds(fieldX_new, startY + 4 * gap, fieldWidth, fieldHeight);

                
                int btnStartX_new = (panelWidth_new - (btnWidth * 2 + btnGap)) / 2;
                btnBack.setBounds(btnStartX_new, panelHeight_new - 100, btnWidth, btnHeight);
                btnSubmit.setBounds(btnStartX_new + btnWidth + btnGap, panelHeight_new - 100, btnWidth, btnHeight);
            }
        });
    }

    private void addRoom() {
        String roomNumber = textField.getText().trim();
        String location = textField_1.getText().trim();
        String floor = textField_2.getText().trim();
        String capacityStr = textField_3.getText().trim();
        String college = textField_4.getText().trim();

        if (roomNumber.isEmpty() || capacityStr.isEmpty() || location.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Room Number, Capacity, and Location are required!");
            return;
        }

        int capacity;
        try {
            capacity = Integer.parseInt(capacityStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Capacity must be a number!");
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {
            
            String sql = "INSERT INTO classrooms (room_code, capacity, building, floor, college) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, roomNumber);
            ps.setInt(2, capacity);
            ps.setString(3, location); 
            ps.setString(4, floor); 
            ps.setString(5, college); 
            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "Room added successfully!");
            clearFields();

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error adding room: " + e.getMessage());
        }
    }

    private void clearFields() {
        textField.setText("");
        textField_1.setText("");
        textField_2.setText("");
        textField_3.setText("");
        textField_4.setText("");
    }
}