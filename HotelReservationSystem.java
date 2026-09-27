import java.util.ArrayList;
import java.util.Scanner;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HotelReservationSystem {

    static ArrayList<Room> rooms = new ArrayList<>();
    static ArrayList<Reservation> reservations = new ArrayList<>();

    static Scanner scanner = new Scanner(System.in);

    static int nextReservationId = 1001;

    public static void main(String[] args) {

        addRooms();

        int choice;

        do {
            System.out.println("\n=================================");
            System.out.println("     HOTEL RESERVATION SYSTEM");
            System.out.println("=================================");
            System.out.println("1. View Available Rooms");
            System.out.println("2. Book a Room");
            System.out.println("3. View Reservation");
            System.out.println("4. Make Payment");
            System.out.println("5. Cancel Reservation");
            System.out.println("6. Check Out");
            System.out.println("7. Exit");
            System.out.println("---------------------------------");
            System.out.print("Enter your choice: ");

            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    viewAvailableRooms();
                    break;

                case 2:
                    bookRoom();
                    break;

                case 3:
                    viewReservation();
                    break;

                case 4:
                    makePayment();
                    break;

                case 5:
                    cancelReservation();
                    break;

                case 6:
                    checkOut();
                    break;

                case 7:
                    System.out.println("\nThank you for using the Hotel Reservation System!");
                    break;

                default:
                    System.out.println("\nInvalid choice. Please try again.");
            }

        } while (choice != 7);

        scanner.close();
    }

    // Add rooms to the hotel
    static void addRooms() {

        rooms.add(new Room(101, "Standard", 1500));
        rooms.add(new Room(102, "Standard", 1500));
        rooms.add(new Room(201, "Deluxe", 2500));
        rooms.add(new Room(202, "Deluxe", 2500));
        rooms.add(new Room(301, "Suite", 4000));
        rooms.add(new Room(302, "Suite", 4000));
    }

    // Display available rooms
    static void viewAvailableRooms() {

        System.out.println("\n========== AVAILABLE ROOMS ==========");

        boolean found = false;

        for (Room room : rooms) {

            if (room.isAvailable()) {
                room.displayRoom();
                found = true;
            }
        }

        if (!found) {
            System.out.println("No rooms are currently available.");
        }
    }

    // Book a room
    static void bookRoom() {

        System.out.println("\n========== ROOM BOOKING ==========");

        viewAvailableRooms();

        System.out.print("\nEnter room number: ");
        int roomNumber = scanner.nextInt();
        scanner.nextLine();

        Room selectedRoom = null;

        for (Room room : rooms) {

            if (room.getRoomNumber() == roomNumber && room.isAvailable()) {
                selectedRoom = room;
                break;
            }
        }

        if (selectedRoom == null) {
            System.out.println("Room is not available or does not exist.");
            return;
        }

        System.out.print("Enter customer name: ");
        String name = scanner.nextLine();

        System.out.print("Enter phone number: ");
        String phone = scanner.nextLine();

        System.out.print("Enter email: ");
        String email = scanner.nextLine();

        System.out.print("Enter number of nights: ");
        int nights = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter check-in date and time (dd-MM-yyyy HH:mm): ");
        String checkInDate = scanner.nextLine();

        if (nights <= 0) {
            System.out.println("Number of nights must be greater than zero.");
            return;
        }

        Customer customer = new Customer(name, phone, email);

        Reservation reservation =
                new Reservation(nextReservationId, customer, selectedRoom, nights,checkInDate);

        reservations.add(reservation);

        selectedRoom.setAvailable(false);
        FileManager.saveAllReservations(reservations);

        System.out.println("\nRoom booked successfully!");
        System.out.println("Your Reservation ID is: " + nextReservationId);

        nextReservationId++;
    }

    // View reservation
    static void viewReservation() {

        System.out.println("\n========== VIEW RESERVATION ==========");

        System.out.print("Enter Reservation ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        for (Reservation reservation : reservations) {

            if (reservation.getReservationId() == id) {
                reservation.displayReservation();
                return;
            }
        }

        System.out.println("Reservation not found.");
    }

    // Make payment
    static void makePayment() {

    System.out.println("\n========== PAYMENT ==========");

    System.out.print("Enter Reservation ID: ");
    int id = scanner.nextInt();
    scanner.nextLine();

    for (Reservation reservation : reservations) {

        if (reservation.getReservationId() == id) {

            if (reservation.isPaid()) {
                System.out.println("Payment has already been completed.");
                return;
            }

            System.out.println("Amount to Pay: Rs."
                    + reservation.getTotalAmount());

            System.out.println("\nSelect Payment Method:");
            System.out.println("1. UPI");
            System.out.println("2. Card");
            System.out.println("3. Cash");

            System.out.print("Enter choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            String paymentMethod;

            switch (choice) {
                case 1:
                    paymentMethod = "UPI";
                    break;

                case 2:
                    paymentMethod = "Card";
                    break;

                case 3:
                    paymentMethod = "Cash";
                    break;

                default:
                    System.out.println("Invalid payment method.");
                    return;
            }

            System.out.print("Confirm payment? (yes/no): ");
            String answer = scanner.nextLine();

            if (answer.equalsIgnoreCase("yes")) {

                reservation.makePayment(paymentMethod);

                FileManager.saveAllReservations(reservations);

                System.out.println("\nPayment successful!");
                System.out.println("Payment Method: " + paymentMethod);
                System.out.println("Amount Paid: Rs."
                        + reservation.getTotalAmount());

            } else {
                System.out.println("Payment cancelled.");
            }

            return;
        }
    }

    System.out.println("Reservation not found.");
}


    // Cancel reservation
   static void cancelReservation() {

    System.out.println("\n========== CANCEL RESERVATION ==========");
    System.out.print("Enter Reservation ID: ");

    int id = scanner.nextInt();
    scanner.nextLine();

    for (Reservation reservation : reservations) {

        if (reservation.getReservationId() == id) {

            if (reservation.getStatus().equals("Cancelled")) {
                System.out.println("This reservation is already cancelled.");
                return;
            }

            if (reservation.getStatus().equals("Checked Out")) {
                System.out.println("This reservation is already completed.");
                return;
            }

            double refund = 0;

            if (reservation.isPaid()) {

                LocalDateTime now = LocalDateTime.now();

                long hoursUntilCheckIn =
                        java.time.Duration.between(
                                now,
                                reservation.getCheckInDateTime()
                        ).toHours();

                if (hoursUntilCheckIn >= 24) {

                    refund = reservation.getTotalAmount();

                    System.out.println("Cancellation is more than 24 hours before check-in.");
                    System.out.println("Refund Policy: 100%");

                } else if (hoursUntilCheckIn > 0) {

                    refund = reservation.getTotalAmount() * 0.80;

                    System.out.println("Cancellation is within 24 hours of check-in.");
                    System.out.println("Refund Policy: 80%");

                } else {

                    refund = 0;

                    System.out.println("Check-in time has already passed.");
                    System.out.println("Refund Policy: No Refund");
                }

            } else {

                System.out.println("No payment was made.");
                refund = 0;
            }

            reservation.cancelReservation(refund);

            // Make the room available again
            reservation.getRoom().setAvailable(true);

            FileManager.saveAllReservations(reservations);

            System.out.println("\nReservation cancelled successfully.");
            System.out.println("Refund Amount: Rs." + refund);

            return;
        }
    }

    System.out.println("Reservation not found.");
}

static void checkOut() {

    System.out.println("\n========== CHECK OUT ==========");
    System.out.print("Enter Reservation ID: ");

    int id = scanner.nextInt();
    scanner.nextLine();

    for (Reservation reservation : reservations) {

        if (reservation.getReservationId() == id) {

            if (reservation.getStatus().equals("Cancelled")) {
                System.out.println("This reservation was cancelled.");
                return;
            }

            if (reservation.getStatus().equals("Checked Out")) {
                System.out.println("This reservation is already checked out.");
                return;
            }

            reservation.checkOut();

            // Make room available again
            reservation.getRoom().setAvailable(true);

            FileManager.saveAllReservations(reservations);

            System.out.println("\nCheck-out successful!");
            System.out.println("Room "
                    + reservation.getRoom().getRoomNumber()
                    + " is now available.");

            System.out.println("Reservation Status: Checked Out");

            return;
        }
    }

    System.out.println("Reservation not found.");
}

}
