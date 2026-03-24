	package Database;
	
	import java.sql.*;
	
	public class DBConnection {
	
	    private static final String URL = "jdbc:mysql://localhost:3306/class_scheduling_system";
	    private static final String USER = "root";   // your MySQL username
	    private static final String PASSWORD = "root"; // your MySQL password
	
		static {
			try {
				setupDatabase();
			} catch (SQLException e) {
				System.err.println("❌ Error during database setup: " + e.getMessage());
			}
		}

		public static Connection getConnection() {
			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
				Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
				//System.out.println("✅ Connected to class_scheduling_system database.");
				return conn;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}

		// Legacy no-op close helper. Individual connections must be closed by callers.
		public static void closeConnection() {
			// no-op
		}
	
	    private static void setupDatabase() throws SQLException {
	        String rootUrl = "jdbc:mysql://localhost:3306/?serverTimezone=UTC";
	        try (Connection conn = DriverManager.getConnection(rootUrl, USER, PASSWORD);
	             Statement stmt = conn.createStatement()) {
	
	            // Create database if not exists
	            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS class_scheduling_system");
	            stmt.executeUpdate("USE class_scheduling_system");
	
	            // === ACCOUNTS TABLE ===
	            stmt.executeUpdate("""
	                CREATE TABLE IF NOT EXISTS accounts (
	                    account_id INT AUTO_INCREMENT PRIMARY KEY,
	                    username VARCHAR(100) NOT NULL UNIQUE,
	                    full_email VARCHAR(150) NOT NULL UNIQUE,
	                    password VARCHAR(100) NOT NULL,
	                    role ENUM('student', 'teacher', 'admin') NOT NULL,
	                    keyword VARCHAR(100),
	                    date_created TIMESTAMP DEFAULT CURRENT_TIMESTAMP
	                )
	            """);
	
	            // === STUDENTS TABLE ===
	            stmt.executeUpdate("""
	                CREATE TABLE IF NOT EXISTS students (
	                    student_id INT AUTO_INCREMENT PRIMARY KEY,
	                    account_id INT,
	                    student_number VARCHAR(50) UNIQUE,
	                    full_name VARCHAR(150),
	                    program_id INT,
	                    year_level VARCHAR(50),
	                    section VARCHAR(50),
	                    FOREIGN KEY (account_id) REFERENCES accounts(account_id) ON DELETE CASCADE
	                )
	            """);
	
	            // === TEACHERS TABLE ===
	            stmt.executeUpdate("""
	                CREATE TABLE IF NOT EXISTS teachers (
	                    teacher_id INT AUTO_INCREMENT PRIMARY KEY,
	                    account_id INT,
	                    full_name VARCHAR(150),
	                    department VARCHAR(100),
	                    specialization VARCHAR(100),
	                    college VARCHAR(100),
	                    FOREIGN KEY (account_id) REFERENCES accounts(account_id) ON DELETE CASCADE
	                )
	            """);
	
	            // === PROGRAMS / DEGREES TABLE ===
	            stmt.executeUpdate("""
	                CREATE TABLE IF NOT EXISTS programs (
	                    program_id INT AUTO_INCREMENT PRIMARY KEY,
	                    program_code VARCHAR(50) UNIQUE,
	                    program_name VARCHAR(150),
	                    program_duration INT(1),
	                    total_units INT(5),
	                    department VARCHAR(100)
	                )
	            """);
	
	            // === SUBJECTS TABLE ===
	            stmt.executeUpdate("""
	                CREATE TABLE IF NOT EXISTS subjects (
	            		subject_id INT AUTO_INCREMENT PRIMARY KEY,
	            		subject_code VARCHAR(50) UNIQUE,
	            		subject_name VARCHAR(150),
	            		units INT DEFAULT 2,  -- Adding units
	            		prerequisite VARCHAR(150),  -- Adding prerequisite field
	            		category VARCHAR(50) DEFAULT "General Subject",  -- Adding category field
	            		program_id INT,
	            		FOREIGN KEY (program_id) REFERENCES programs(program_id) ON DELETE SET NULL
	            		);
	            """);
	
	            // === CLASSROOMS TABLE ===
	            stmt.executeUpdate("""
	                CREATE TABLE IF NOT EXISTS classrooms (
	            		room_id INT AUTO_INCREMENT PRIMARY KEY,
	            		room_code VARCHAR(50) UNIQUE NOT NULL,
	            		capacity INT NOT NULL,
	            		building VARCHAR(100) NOT NULL,
	            		floor VARCHAR(50) NOT NULL,
	            		college VARCHAR(100) NOT NULL
	            		)
	            """);
	
	            // === SEMESTERS TABLE (NEW: For academic periods and recurring schedules) ===
	            stmt.executeUpdate("""
	                CREATE TABLE IF NOT EXISTS semesters (
	                    semester_id INT AUTO_INCREMENT PRIMARY KEY,
	                    semester_name VARCHAR(100) NOT NULL UNIQUE,  -- e.g., 'Fall 2023'
	                    start_date DATE NOT NULL,
	                    end_date DATE NOT NULL,
	                    academic_year VARCHAR(50)  -- e.g., '2023-2024'
	                )
	            """);
	
	            // === CLASSES TABLE (UPDATED: Added time fields, recurring, status, and semester link) ===
	            stmt.executeUpdate("""
	            	CREATE TABLE IF NOT EXISTS classes (
	            		class_id INT AUTO_INCREMENT PRIMARY KEY,
	            		subject_id INT,
	            		teacher_id INT,
	            		room_id INT,
	            		semester_id INT,  -- Link to semester for grouping
	            		schedule_day VARCHAR(50),  -- Schedule day as a string
	            		schedule_time VARCHAR(50),  -- Schedule time as a string (you can adjust this field based on your needs)
	            		start_time VARCHAR(5),  -- Start time stored as VARCHAR (HH:mm)
	            		end_time VARCHAR(5),  -- End time stored as VARCHAR (HH:mm)
	            		duration INT,  -- Duration in minutes (calculated or stored)
	            		is_recurring BOOLEAN DEFAULT FALSE,  -- For recurring classes
	            		status ENUM('active', 'cancelled', 'postponed') DEFAULT 'active',  -- Class status
	            		FOREIGN KEY (subject_id) REFERENCES subjects(subject_id) ON DELETE CASCADE,
	            		FOREIGN KEY (teacher_id) REFERENCES teachers(teacher_id) ON DELETE SET NULL,
	            		FOREIGN KEY (room_id) REFERENCES classrooms(room_id) ON DELETE SET NULL,
	            		FOREIGN KEY (semester_id) REFERENCES semesters(semester_id) ON DELETE SET NULL
	            	)
	            """);
	
	            // === ENROLLMENTS TABLE (UPDATED: Added status and grade) ===
	            stmt.executeUpdate("""
	                CREATE TABLE IF NOT EXISTS enrollments (
	                    enrollment_id INT AUTO_INCREMENT PRIMARY KEY,
	                    student_id INT,
	                    class_id INT,
	                    date_enrolled TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	                    status ENUM('enrolled', 'dropped', 'completed') DEFAULT 'enrolled',  -- NEW: Enrollment status
	                    grade VARCHAR(10),  -- NEW: e.g., 'A', 'B+', or NULL
	                    FOREIGN KEY (student_id) REFERENCES students(student_id) ON DELETE CASCADE,
	                    FOREIGN KEY (class_id) REFERENCES classes(class_id) ON DELETE CASCADE
	                )
	            """);
	
            // === ADMIN ACCOUNT DEFAULT ===
            stmt.executeUpdate("""
                INSERT INTO accounts (username, full_email, password, role, keyword)
                SELECT 'admin', 'admin@classsystem.com', 'admin123', 'admin', 'adminkey'
                WHERE NOT EXISTS (SELECT 1 FROM accounts WHERE username = 'admin')
            """);
            
	        }
	    }
	}