import java.sql.*;

public class Main {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/testdb"; // Change DB name if needed
        String user = "root"; // your MySQL username
        String password = ""; // your MySQL password (keep blank if none)

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, user, password);
            System.out.println("✅ Connected to Database Successfully!");

            Statement stmt = con.createStatement();

            // Create table if it doesn't exist
            String createTable = "CREATE TABLE IF NOT EXISTS students (id INT AUTO_INCREMENT PRIMARY KEY, name VARCHAR(50), marks INT)";
            stmt.executeUpdate(createTable);
            System.out.println("🧱 Table ready!");

            // Insert a record
            String insert = "INSERT INTO students (name, marks) VALUES ('Aashlesha', 90)";
            stmt.executeUpdate(insert);
            System.out.println("📥 Record inserted!");

            // Display all records
            ResultSet rs = stmt.executeQuery("SELECT * FROM students");
            System.out.println("\n📊 Student Records:");
            while (rs.next()) {
                System.out.println(rs.getInt("id") + " | " + rs.getString("name") + " | " + rs.getInt("marks"));
            }

            con.close();
            System.out.println("\n✅ Operation Completed Successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
