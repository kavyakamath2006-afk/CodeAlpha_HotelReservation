import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Reservation {

    private int reservationId;
    private Customer customer;
    private Room room;
    private int numberOfNights;
    private double totalAmount;

    private boolean paid;
    private String paymentMethod;
    private String status;
    private double refundAmount;

    private LocalDateTime checkInDateTime;

    public Reservation(int reservationId, Customer customer, Room room,
                       int numberOfNights, String checkInDateTime) {

        this.reservationId = reservationId;
        this.customer = customer;
        this.room = room;
        this.numberOfNights = numberOfNights;
        this.totalAmount = room.getPrice() * numberOfNights;

        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

        this.checkInDateTime =
                LocalDateTime.parse(checkInDateTime, formatter);

        this.paid = false;
        this.paymentMethod = "Not Paid";
        this.status = "Active";
        this.refundAmount = 0;
    }

    public int getReservationId() {
        return reservationId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Room getRoom() {
        return room;
    }

    public int getNumberOfNights() {
        return numberOfNights;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public boolean isPaid() {
        return paid;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public String getStatus() {
        return status;
    }

    public double getRefundAmount() {
        return refundAmount;
    }

    public LocalDateTime getCheckInDateTime() {
        return checkInDateTime;
    }

    public void makePayment(String paymentMethod) {
        paid = true;
        this.paymentMethod = paymentMethod;
    }

    public void cancelReservation(double refundAmount) {
        status = "Cancelled";
        this.refundAmount = refundAmount;
    }

    public void checkOut() {
        status = "Checked Out";
    }

    public void displayReservation() {

        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

        System.out.println("\n===== RESERVATION DETAILS =====");
        System.out.println("Reservation ID : " + reservationId);
        customer.displayCustomer();

        System.out.println("Room Number    : "
                + room.getRoomNumber());

        System.out.println("Room Type      : "
                + room.getRoomType());

        System.out.println("Price/Night    : Rs."
                + room.getPrice());

        System.out.println("Nights         : "
                + numberOfNights);

        System.out.println("Check-in Date  : "
                + checkInDateTime.format(formatter));

        System.out.println("Total Amount   : Rs."
                + totalAmount);

        System.out.println("Payment Method : "
                + paymentMethod);

        System.out.println("Payment Status : "
                + (paid ? "Paid" : "Not Paid"));

        System.out.println("Reservation    : "
                + status);

        System.out.println("Refund Amount  : Rs."
                + refundAmount);
    }
}