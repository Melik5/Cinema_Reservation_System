import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.LinkedList;

public class ReservationSystem extends JFrame {
    private JButton[][] seats;
    private boolean[][] seatStatus;
    
    private Map<String, Integer> seatAvailability;
    private Map<String, String> movieInfo;
    
    private Queue<ReservationTask> cancellationQueue;

    private JComboBox<String> movieComboBox; // ComboBox to select movies

    public ReservationSystem(int rows, int columns) {
        seats = new JButton[rows][columns];
        seatStatus = new boolean[rows][columns];
        seatAvailability = new HashMap<>();
        movieInfo = new HashMap<>();
        cancellationQueue = new LinkedList<>();

        // Initialize movie information and seat availability
        movieInfo.put("Spider-Man", "Action");
        movieInfo.put("Fast and Furious", "Adventure");
        movieInfo.put("Deadpool", "Comedy");
        movieInfo.put("Iron-Man", "Action");
        movieInfo.put("Avatar", "Science Fiction");

        seatAvailability.put("Spider-Man", 30);
        seatAvailability.put("Fast and Furious", 30);
        seatAvailability.put("Deadpool", 30);
        seatAvailability.put("Iron-Man", 30);
        seatAvailability.put("Avatar", 30);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Reservation System");
        setLayout(new BorderLayout());

        // Movie selection panel
        JPanel movieSelectionPanel = new JPanel();
        movieSelectionPanel.setLayout(new FlowLayout());
        JLabel movieLabel = new JLabel("Select a movie:");
        movieComboBox = new JComboBox<>(movieInfo.keySet().toArray(new String[0]));
        JButton selectButton = new JButton("Select");
        selectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedMovie = (String) movieComboBox.getSelectedItem();
                if (selectedMovie != null && movieInfo.containsKey(selectedMovie)) {
                    showSeatSelection(selectedMovie);
                } else {
                    JOptionPane.showMessageDialog(ReservationSystem.this, "Invalid movie selection.");
                }
            }
        });

        movieSelectionPanel.add(movieLabel);
        movieSelectionPanel.add(movieComboBox);
        movieSelectionPanel.add(selectButton);

        add(movieSelectionPanel, BorderLayout.NORTH);

        setSize(800, 600);
        setVisible(true);
    }

    private void showSeatSelection(String selectedMovie) {
        int rows = seats.length;
        int columns = seats[0].length;

        getContentPane().removeAll(); // Remove movie selection panel
        setTitle("Seat Selection - " + selectedMovie);
        setLayout(new GridLayout(rows, columns));

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                seats[i][j] = new JButton((i + 1) + "-" + (j + 1));
                seats[i][j].setBackground(Color.LIGHT_GRAY); // Set the initial color to blue for available seats
                final int row = i;
                final int column = j;
                seats[i][j].addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        makeReservation(row, column, selectedMovie);
                    }
                });
                seats[i][j].addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (SwingUtilities.isRightMouseButton(e)) {
                            cancellationQueue.offer(new ReservationTask(row, column, selectedMovie));
                            processNextCancellation();
                        }
                    }
                });
                add(seats[i][j]);
            }
        }

        revalidate();
        repaint();
    }

 //*The method begins by checking if the selected movie is valid by verifying its existence in the seatAvailability map.
 //This step ensures that the movie is available for reservation.

    private void makeReservation(int row, int column, String selectedMovie) {
    	
 //*If the movie is valid, the method proceeds to check the availability of seats for that particular movie.
        if (seatAvailability.containsKey(selectedMovie)) {
            int availableSeats = seatAvailability.get(selectedMovie);
            if (availableSeats > 0) {
            	
 //*The customer is prompted to enter their name using a input in box. This allows us to personalize their reservation experience.
                String customerName = JOptionPane.showInputDialog(this, "Enter customer name:");
                if (customerName != null && !customerName.isEmpty()) {
                	
 //*These presents the customer with payment options: "Credit/Debit Card" or "Cash." The customer's choice is obtained through a box.
                    Object[] paymentOptions = {"Credit/Debit Card", "Cash"};
                    int paymentChoice = JOptionPane.showOptionDialog(this, "Please select a payment method:", "Payment Method",
                            JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, paymentOptions, paymentOptions[0]);
                    if (paymentChoice == 0) {
                        String cardNumber = JOptionPane.showInputDialog(this, "Enter your card number:");
 //*After selecting the option the ticket price is set, and the payment process is initiated. The seat's status is updated to "reserved,"
 //*and the seat button's color is changed to red. And the availability of seats for the selected movie is also updated.
                        if (cardNumber != null && !cardNumber.isEmpty()) {
                            double ticketPrice = 8.0; // Set the ticket price for credit/debit card payment method
                            // Process payment with credit/debit card
                            
                            seatStatus[row][column] = true;
                            seats[row][column].setBackground(Color.RED); // Set the color to red for reserved seats
                            seats[row][column].setEnabled(false);

                            seatAvailability.put(selectedMovie, availableSeats - 1);
 //*A confirmation message is then displayed, providing details such as the movie, seat number, customer name, payment method and the ticket price.
 
                            JOptionPane.showMessageDialog(this, "Reservation successful!\nMovie: " + selectedMovie +
                                    "\nSeat: " + (row + 1) + "-" + (column + 1) +
                                    "\nCustomer: " + customerName +
                                    "\nPayment Method: Credit/Debit Card" +
                                    "\nCard Number: " + cardNumber +
                                    "\nTicket Price: $" + ticketPrice);
                        } else {
 //*This method also handles invalid inputs, such as an empty customer name or an empty card number.
 //*Appropriate error messages are displayed to the customer.
                            JOptionPane.showMessageDialog(this, "Invalid card number.");
                        }
                    } else if (paymentChoice == 1) {
                        double ticketPrice = 8.0; // Set the ticket price for cash payment method
                        // Process payment with cash
                        
                        seatStatus[row][column] = true;
                        seats[row][column].setBackground(Color.RED); // Set the color to red for reserved seats
                        seats[row][column].setEnabled(false);

                        seatAvailability.put(selectedMovie, availableSeats - 1);
                        
     

                        JOptionPane.showMessageDialog(this, "Reservation successful!\nMovie: " + selectedMovie +
                                "\nSeat: " + (row + 1) + "-" + (column + 1) +
                                "\nCustomer: " + customerName +
                                "\nPayment Method: Cash" +
                                "\nTicket Price: $" + ticketPrice);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid customer name.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Sorry, there are no more seats available for the selected movie.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Invalid movie name.");
        }
 //*Finally, the customer is asked to provide feedback on the reservation system, rating it from "Excellent" to "Poor."
        String[] evaluationOptions = {"Excellent", "Good", "Average", "Poor"};
        int evaluationChoice = JOptionPane.showOptionDialog(this,
                "Please rate our reservation system:",
                "System Evaluation",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                evaluationOptions,
                evaluationOptions[0]);

        if (evaluationChoice != JOptionPane.CLOSED_OPTION) {
            String evaluation = evaluationOptions[evaluationChoice];
            // Evaluation procedures
            System.out.println("Evaluation: " + evaluation);
        }
    }

 //*The processNextCancellation method checks if there are any pending cancellations in the cancellation queue.

    private void processNextCancellation() {
 //*If a cancellation is present, it retrieves the necessary details, such as the row, column, and selected movie.

        if (!cancellationQueue.isEmpty()) {
            ReservationTask task = cancellationQueue.poll();
            int row = task.getRow();
            int column = task.getColumn();
            String selectedMovie = task.getSelectedMovie();
 //*The seat status is updated to "unreserved," and the seat button's color is changed to light gray.
            seatStatus[row][column] = false;
            seats[row][column].setBackground(Color.LIGHT_GRAY); 
            seats[row][column].setEnabled(true);
 //*The seat is then enabled for further reservations.
            int availableSeats = seatAvailability.get(selectedMovie);
            seatAvailability.put(selectedMovie, availableSeats + 1);
 //*A confirmation message is displayed, notifying the customer about the successful cancellation.
            JOptionPane.showMessageDialog(this, "Reservation canceled!\nMovie: " + selectedMovie +
                    "\nSeat: " + (row + 1) + "-" + (column + 1));

            processNextCancellation(); // Process the next cancellation in the queue
        }
    }
    
  //*This class serves as a container to hold the details of a reservation task. 
  //*It encapsulates the row, column, and selected movie, providing a convenient way to pass and retrieve this information
    private class ReservationTask {
        private int row;
        private int column;
        private String selectedMovie;

        public ReservationTask(int row, int column, String selectedMovie) {
            this.row = row;
            this.column = column;
            this.selectedMovie = selectedMovie;
        }

        public int getRow() {
            return row;
        }

        public int getColumn() {
            return column;
        }

        public String getSelectedMovie() {
            return selectedMovie;
        }
    }
    
 //*This main method creates a user interface component using the Swing library,
 //*which is a user interface toolkit for Java. The code is specifically creating an interface for a reservation system.
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
 //The SwingUtilities.invokeLater() method is used to ensure that Swing user interface components are created and updated safely. 
 //It allows for the creation and updating of user interface components outside of the main thread
           @Override
            public void run() {
                int rows = 5; // Specifying the number of rows in the cinema
                int columns = 6; // Specifying the number of columns in the cinema
                new ReservationSystem(rows, columns);
            }
        });
    }
}


