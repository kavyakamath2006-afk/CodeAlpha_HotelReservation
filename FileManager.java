import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class FileManager {

    public static void saveAllReservations(
            ArrayList<Reservation> reservations) {

        try {
            FileWriter writer = new FileWriter("reservations.txt");

            for (Reservation reservation : reservations) {

                writer.write("Reservation ID: "
                        + reservation.getReservationId() + "\n");

                writer.write("Customer: "
                        + reservation.getCustomer().getName() + "\n");

                writer.write("Phone: "
                        + reservation.getCustomer().getPhone() + "\n");

                writer.write("Email: "
                        + reservation.getCustomer().getEmail() + "\n");

                writer.write("Room Number: "
                        + reservation.getRoom().getRoomNumber() + "\n");

                writer.write("Room Type: "
                        + reservation.getRoom().getRoomType() + "\n");

                writer.write("Total Amount: Rs."
                        + reservation.getTotalAmount() + "\n");

                writer.write("Payment Method: "
                        + reservation.getPaymentMethod() + "\n");

                writer.write("Payment Status: "
                        + (reservation.isPaid() ? "Paid" : "Not Paid") + "\n");

                writer.write("Reservation Status: "
                        + reservation.getStatus() + "\n");

                writer.write("Refund Amount: Rs."
                        + reservation.getRefundAmount() + "\n");

                writer.write("----------------------------------\n");
            }

            writer.close();

        } catch (IOException e) {
            System.out.println("Error saving reservations.");
        }
    }
}



