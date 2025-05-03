package src;
import java.sql.*;
import java.util.Scanner;

public class employee {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/java";
        String user = "root"; 
        String password = "Nivi@123"; 

        Scanner sc = new Scanner(System.in);

        try {
         
            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection conn = DriverManager.getConnection(url, user, password);

            String sql = "INSERT INTO employeedetails (empid, empname, dept, basicpay, DA, HRA, YOE) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            for (int i = 0; i < 5; i++) {
           
            System.out.print("Enter Employee ID: ");
            int empid = sc.nextInt();
            sc.nextLine();

            System.out.print("Enter Employee Name: ");
            String empname = sc.nextLine();

            System.out.print("Enter Department: ");
            String dept = sc.nextLine();

            System.out.print("Enter Basic Pay: ");
            double basicPay = sc.nextDouble();

            System.out.print("Enter DA: ");
            double DA = sc.nextDouble();

            System.out.print("Enter HRA: ");
            double HRA= sc.nextDouble();

            System.out.print("Enter Years of Experience: ");
            int YOE = sc.nextInt();

           
            pstmt.setInt(1, empid);
            pstmt.setString(2, empname);
            pstmt.setString(3, dept);
            pstmt.setDouble(4, basicPay);
            pstmt.setDouble(5, DA);
            pstmt.setDouble(6, HRA);
            pstmt.setInt(7, YOE);

            
            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Employee inserted successfully!");
            }

            }
            pstmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        sc.close();
    }
}



