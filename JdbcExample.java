package src;
import java.util.Scanner;
import java.sql.*;
public class JdbcExample {
	private static final String URL = "jdbc:mysql://localhost:3306/java";
	private static final String USER ="root";
	private static final String PASSWORD = "";
	
	public static void main(String[] args) {
		Connection connection =  null;
		Statement statement = null;
		try {
			connection = DriverManager.getConnection(URL,USER,PASSWORD);
			System.out.println("connected to database successfully!");
			
			statement = connection.createStatement();
			
			String createTableSQL = "CREATE TABLE IF NOT EXISTS Employeedetails ("
					+ "id INT AUTO_INCREMENT PRIMARY KEY,"
					+ "name VARCHAR(100), "
					+ "email VARCHAR(100)";
			statement.executeUpdate(createTableSQL);
			System.out.println("Table created successfully");
		}
		finally
	}
}
