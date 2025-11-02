/*
 * JDBC Project: Student Management System
 * Fully ready-to-submit single Java file
 * Author: Your Name
 * Instructions:
 * 1. Make sure MySQL is installed and running.
 * 2. Create a database called "school":
 *      CREATE DATABASE school;
 * 3. Add mysql-connector-j-9.5.0.jar to your project classpath.
 * 4. Replace "YOUR_PASSWORD_HERE" below with your MySQL root password.
 * 5. Compile: javac -cp ".;mysql-connector-j-9.5.0.jar" StudentManagement.java
 * 6. Run: java -cp ".;mysql-connector-j-9.5.0.jar" StudentManagement
 */

import java.sql.*;
import java.util.Scanner;

public class StudentManagement {
    
    // Database connection variables
    private static final String DB_URL = "jdbc:mysql://localhost:3306/school";
    private static final String DB_USER = "root";      
    private static final String DB_PASSWORD = "2612005ar"; // <- REPLACE THIS
    
    private Connection con;
    private Scanner sc;

    // Constructor: connects to the database
    public StudentManagement() {
        try {
            con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            sc = new Scanner(System.in);
            createTableIfNotExists();
        } catch (SQLException e) {
            System.out.println("Database connection failed: " + e.getMessage());
            System.exit(0);
        }
    }

    // Create Students table if it doesn't exist
    private void createTableIfNotExists() {
        String sql = "CREATE TABLE IF NOT EXISTS Students (" +
                     "id INT PRIMARY KEY AUTO_INCREMENT," +
                     "name VARCHAR(50) NOT NULL," +
                     "age INT NOT NULL," +
                     "grade VARCHAR(5) NOT NULL)";
        try (Statement stmt = con.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println("Error creating table: " + e.getMessage());
        }
    }

    // Add a new student
    private void addStudent() {
        try {
            System.out.print("Enter name: ");
            String name = sc.nextLine();
            System.out.print("Enter age: ");
            int age = Integer.parseInt(sc.nextLine());
            System.out.print("Enter grade: ");
            String grade = sc.nextLine();
            
            String sql = "INSERT INTO Students (name, age, grade) VALUES (?, ?, ?)";
            try (PreparedStatement pst = con.prepareStatement(sql)) {
                pst.setString(1, name);
                pst.setInt(2, age);
                pst.setString(3, grade);
                pst.executeUpdate();
                System.out.println("Student added successfully!");
            }
        } catch (SQLException e) {
            System.out.println("Error adding student: " + e.getMessage());
        }
    }

    // View all students
    private void viewStudents() {
        String sql = "SELECT * FROM Students";
        try (Statement stmt = con.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            System.out.println("\nID | Name | Age | Grade");
            System.out.println("-------------------------");
            while (rs.next()) {
                System.out.printf("%d | %s | %d | %s\n",
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("age"),
                        rs.getString("grade"));
            }
        } catch (SQLException e) {
            System.out.println("Error fetching students: " + e.getMessage());
        }
    }

    // Update student info
    private void updateStudent() {
        try {
            System.out.print("Enter student ID to update: ");
            int id = Integer.parseInt(sc.nextLine());
            System.out.print("Enter new name: ");
            String name = sc.nextLine();
            System.out.print("Enter new age: ");
            int age = Integer.parseInt(sc.nextLine());
            System.out.print("Enter new grade: ");
            String grade = sc.nextLine();
            
            String sql = "UPDATE Students SET name=?, age=?, grade=? WHERE id=?";
            try (PreparedStatement pst = con.prepareStatement(sql)) {
                pst.setString(1, name);
                pst.setInt(2, age);
                pst.setString(3, grade);
                pst.setInt(4, id);
                int rows = pst.executeUpdate();
                if (rows > 0)
                    System.out.println("Student updated successfully!");
                else
                    System.out.println("Student ID not found.");
            }
        } catch (SQLException e) {
            System.out.println("Error updating student: " + e.getMessage());
        }
    }

    // Delete a student
    private void deleteStudent() {
        try {
            System.out.print("Enter student ID to delete: ");
            int id = Integer.parseInt(sc.nextLine());
            String sql = "DELETE FROM Students WHERE id=?";
            try (PreparedStatement pst = con.prepareStatement(sql)) {
                pst.setInt(1, id);
                int rows = pst.executeUpdate();
                if (rows > 0)
                    System.out.println("Student deleted successfully!");
                else
                    System.out.println("Student ID not found.");
            }
        } catch (SQLException e) {
            System.out.println("Error deleting student: " + e.getMessage());
        }
    }

    // Menu for user
    private void menu() {
        while (true) {
            System.out.println("\n===== Student Management Menu =====");
            System.out.println("1. Add Student");
            System.out.println("2. View Students");
            System.out.println("3. Update Student");
            System.out.println("4. Delete Student");
            System.out.println("5. Exit");
            System.out.print("Enter choice: ");
            String choice = sc.nextLine();
            
            switch (choice) {
                case "1": addStudent(); break;
                case "2": viewStudents(); break;
                case "3": updateStudent(); break;
                case "4": deleteStudent(); break;
                case "5": System.out.println("Exiting..."); close(); return;
                default: System.out.println("Invalid choice. Try again.");
            }
        }
    }

    // Close database connection
    private void close() {
        try {
            if (con != null) con.close();
            if (sc != null) sc.close();
        } catch (SQLException e) {
            System.out.println("Error closing connection: " + e.getMessage());
        }
    }

    // Main method
    public static void main(String[] args) {
        StudentManagement sm = new StudentManagement();
        sm.menu();
    }
}
