package src;
import java.sql.*;
import java.util.Scanner;

public class AirlineReservationSystem {

    
    static final String DB_URL = "jdbc:mysql://localhost:3306/airline_reservation";
    static final String DB_USER = "root";
    static final String DB_PASSWORD ="Nivi@123";  

    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n====== Airline Reservation System ======");
            System.out.println("1. View Flights");
            System.out.println("2. Book Flight");
            System.out.println("3. Cancel Booking");
            System.out.println("4. Exit");
            System.out.print("Enter choice: ");
            int choice = sc.nextInt();

            switch (choice) {
                case 1 -> viewFlights();
                case 2 -> bookFlight();
                case 3 -> cancelBooking();
                case 4 -> {
                    System.out.println("Exiting... Thank you!");
                    System.exit(0);
                }
                default -> System.out.println("Invalid choice!");
            }
        }
    }

    static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    static void viewFlights() {
        try (Connection con = getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM flights")) {

            System.out.println("\nAvailable Flights:");
            System.out.println("---------------------------------------------------------------");
            while (rs.next()) {
                System.out.printf("ID: %d | Airline: %s | From: %s | To: %s | Date: %s | Time: %s | Price: ₹%.2f | Seats: %d\n",
                        rs.getInt("flight_id"),
                        rs.getString("airline_name"),
                        rs.getString("source"),
                        rs.getString("destination"),
                        rs.getDate("flight_date"),
                        rs.getTime("flight_time"),
                        rs.getDouble("price"),
                        rs.getInt("seats_available"));
            }
            System.out.println("---------------------------------------------------------------");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void bookFlight() {
        try (Connection con = getConnection()) {
            System.out.print("Enter Flight ID: ");
            int flightId = sc.nextInt();
            sc.nextLine();  // Consume newline
            System.out.print("Enter your name: ");
            String name = sc.nextLine();
            System.out.print("Enter number of passengers: ");
            int passengers = sc.nextInt();

            // Check flight
            PreparedStatement check = con.prepareStatement("SELECT * FROM flights WHERE flight_id=?");
            check.setInt(1, flightId);
            ResultSet rs = check.executeQuery();

            if (rs.next()) {
                int available = rs.getInt("seats_available");
                double price = rs.getDouble("price");

                if (available >= passengers) {
                    double total = price * passengers;

                    PreparedStatement book = con.prepareStatement(
                            "INSERT INTO bookings (user_name, flight_id, num_passengers, total_price, booking_date) " +
                            "VALUES (?, ?, ?, ?, CURDATE())");
                    book.setString(1, name);
                    book.setInt(2, flightId);
                    book.setInt(3, passengers);
                    book.setDouble(4, total);
                    book.executeUpdate();

                    PreparedStatement updateSeats = con.prepareStatement(
                            "UPDATE flights SET seats_available = seats_available - ? WHERE flight_id = ?");
                    updateSeats.setInt(1, passengers);
                    updateSeats.setInt(2, flightId);
                    updateSeats.executeUpdate();

                    System.out.println("✅ Booking confirmed! Total price: ₹" + total);
                } else {
                    System.out.println("❌ Not enough seats available.");
                }
            } else {
                System.out.println("❌ Flight not found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void cancelBooking() {
        try (Connection con = getConnection()) {
            System.out.print("Enter Booking ID to cancel: ");
            int bookingId = sc.nextInt();

            PreparedStatement get = con.prepareStatement("SELECT * FROM bookings WHERE booking_id=?");
            get.setInt(1, bookingId);
            ResultSet rs = get.executeQuery();

            if (rs.next()) {
                int passengers = rs.getInt("num_passengers");
                int flightId = rs.getInt("flight_id");

                PreparedStatement cancel = con.prepareStatement("DELETE FROM bookings WHERE booking_id=?");
                cancel.setInt(1, bookingId);
                cancel.executeUpdate();

                PreparedStatement restoreSeats = con.prepareStatement(
                        "UPDATE flights SET seats_available = seats_available + ? WHERE flight_id=?");
                restoreSeats.setInt(1, passengers);
                restoreSeats.setInt(2, flightId);
                restoreSeats.executeUpdate();

                System.out.println("✅ Booking cancelled successfully.");
            } else {
                System.out.println("❌ Booking not found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


