package myjava.myprojects;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

import myjava.myprojects.entities.Ticket;
import myjava.myprojects.entities.Train;
import myjava.myprojects.entities.User;
import myjava.myprojects.services.TrainService;
import myjava.myprojects.services.UserBookingService;
import myjava.myprojects.utils.UserServiceUtil;

public class App {

    public static void main(String[] args) {
        System.out.println("✅ Running Train Booking System.....");
        System.out.println();

        // =============================== Constants
        // ===================================================
        final String RESET = "\u001B[0m";
        final String RED = "\u001B[31m";
        final String GREEN = "\u001B[32m";
        final String YELLOW = "\u001B[33m";
        final String BLUE = "\u001B[34m";
        final String CYAN = "\u001B[36m";

        System.out.println(CYAN + "********************************************************");
        System.out.println(CYAN + "*      Welcome to the Train Ticket Booking System      *");
        System.out.println(CYAN + "********************************************************");
        System.out.println(GREEN + "_________________________________________________");
        System.out.println(GREEN + "|        Get ready for your next adventure!      |");
        System.out.println(GREEN + "|         Choose a destination and enjoy         |");
        System.out.println(GREEN + "|             the ride in comfort!               |");
        System.out.println(GREEN + "|________________________________________________|");

        UserBookingService userBookingService;
        TrainService trainService;

        try {
            userBookingService = new UserBookingService();
            trainService = new TrainService();
            System.out.println(RESET + "System initialized successfully!");
        } catch (Exception ex) {
            System.out.println(RED + "Error initializing the Train Booking System. Please check the configuration.");
            ex.printStackTrace();
            System.out.println(RED + "The system will now exit. Please try again later.");
            // scanner.close();
            return;
        }

        Scanner scanner = new Scanner(System.in);

        int option = 0;
        User user = null;
        while (option != 7) {
            System.out.println(GREEN + "Services:");
            System.out.println();
            System.out.println(YELLOW + "1. Sign Up");
            System.out.println(YELLOW + "2. Login");
            System.out.println(YELLOW + "3. Fetch Bookings");
            System.out.println(YELLOW + "4. Search Trains");
            System.out.println(YELLOW + "5. Book a seat");
            System.out.println(YELLOW + "6. Cancel my Booking");
            System.out.println(YELLOW + "7. Exit the App");
            System.out.println();

            System.out.println(BLUE + "Enter the Service No.:");
            System.out.println(RESET);
            if (scanner.hasNextInt()) {
                option = scanner.nextInt();
                scanner.nextLine();
                switch (option) {
                    case 1:
                        System.out.println(RED + "Enter the username to sign up");
                        System.out.println(RESET);
                        String nameToSignUp = scanner.nextLine();
                        System.out.println(RED + "Enter the password to signup");
                        System.out.println(RESET);
                        String passwordToSignUp = scanner.nextLine();
                        User userToSignUp = new User(nameToSignUp, passwordToSignUp,
                                UserServiceUtil.hashedPassword(passwordToSignUp), new ArrayList<>(),
                                UUID.randomUUID().toString());
                        boolean signUpSuccess = userBookingService.signUp(userToSignUp);
                        if (signUpSuccess) {
                            System.out.println(GREEN + "Sign-up successful!");
                        } else {
                            System.out.println(RED + "Already Signed Up.");
                        }
                        System.out.println();
                        break;

                    case 2:
                        System.out.println(RED + "Enter the username to Login");
                        System.out.println(RESET);
                        String userName = scanner.nextLine();
                        System.out.println(RED + "Enter the password to Login");
                        System.out.println(RESET);
                        String userPassword = scanner.nextLine();

                        try {
                            User loggedInUser = userBookingService.loginUser(userName, userPassword);
                            user = loggedInUser;
                            System.out.println(GREEN + "Login successful! Welcome " + loggedInUser.getName());
                        } catch (Exception ex) {
                            System.out.println(RED + "An error occurred during login. Please try again.");
                            ex.printStackTrace();
                        }
                        System.out.println();
                        break;

                    case 3:
                        System.out.println(BLUE + "Fetching your bookings");
                        if (user != null) {
                            System.out.println(RESET + "Fetching bookings for: " + user.getName());
                            userBookingService.fetchBooking(user);
                        } else {
                            System.out.println(RED + "No user is logged in.");
                        }
                        System.out.println();
                        break;

                    case 4:
                        System.out.println(RED + "Enter your source location: ");
                        System.out.println(RESET);
                        String sourceLocation = scanner.nextLine();
                        System.out.println(RED + "Enter your Destination location: ");
                        System.out.println(RESET);
                        String destinationLocation = scanner.nextLine();
                        System.out.println(RESET + "Showing trains from " + GREEN + sourceLocation + RESET + " to "
                                + GREEN + destinationLocation);
                        System.out.println(RESET);
                        trainService.searchTrain(sourceLocation, destinationLocation);
                        System.out.println();
                        break;
                    case 5:
                        if (user == null) {
                            System.out.println("Please Login to Book ticket. Choose service No. 2");
                            System.out.println();
                            break;
                        }
                        System.out.println(BLUE + "Enter Train ID OR Train No: ");
                        System.out.println(RESET);
                        String trainIdOrTrainNo = scanner.nextLine();

                        Train train = trainService.searchTrainById(trainIdOrTrainNo);
                        if (train == null) {
                            System.out.println(RED + "Train not found. Please enter valid details.");
                        } else {
                            System.out.println();
                            System.out.println(RESET + "Train Details as below:");
                            System.out.println("Source: " + GREEN + train.getStation().get(0) + RESET + " Destination: "
                                    + GREEN + train.getStation().get(train.getStation().size() - 1));
                        }

                        System.out.println(BLUE + "Do you want to check all coaches or a specific coach?" + RESET);
                        System.out.println(
                                "Enter 'all' to view all coaches or specify the coach ID to view a single coach:");
                        System.out.println(RESET);
                        String coachId = scanner.nextLine();

                        if (coachId.equalsIgnoreCase("all")) {
                            trainService.showAvailableSeats(train, null);
                            System.out.println("Enter Coach ID to book seat: ");
                            coachId = scanner.nextLine();
                        } else {
                            trainService.showAvailableSeats(train, coachId); // Passing specific coach ID
                        }
                        System.out.println(BLUE + "How many seat numbers you want to book: ");
                        List<Integer> seatNumbers = new ArrayList<>();
                        System.out.println(RESET);
                        int numberOfSeatsToBook = scanner.nextInt();
                        for (int i = 0; i < numberOfSeatsToBook; i++) {
                            System.out.println("Enter Seat #" + (i + 1) + " Number to book: ");
                            seatNumbers.add(scanner.nextInt());
                        }

                        scanner.nextLine();
                        System.out.println(BLUE + "What's your boarding station:");
                        System.out.println(RESET);
                        String boardingStation = scanner.nextLine();
                        System.out.println(BLUE + "Enter the number corresponding to your alighting station:");
                        System.out.println(RESET);
                        String alightingStation = scanner.nextLine();

                        trainService.bookSeatForUser(user, train, coachId, seatNumbers, boardingStation,
                                alightingStation);
                        System.out.println();
                        break;
                    case 6:
                        if (user == null) {
                            System.out.println("Please Login to Cancel ticket. Choose service No. 2");
                            System.out.println();
                            break;
                        }
                        if (user.getTicketsBooked().isEmpty()) {
                            System.out.println("You don't have any Train Ticket bookings to cancel.");
                            System.out.println();
                            break;
                        }
                        int numberOfTicketsBooked = user.getTicketsBooked().size();
                        System.out.println("Total tickets booked: " + numberOfTicketsBooked);
                        List<Ticket> bookedTickets = user.getTicketsBooked();
                        System.out.println("Here are some details: ");
                        System.out.println();
                        for (int i = 0; i < numberOfTicketsBooked; i++) {
                            System.out.println("Ticket ID: " + bookedTickets.get(i).getTicketId());
                            System.out.println("Train ID: " + bookedTickets.get(i).getTrainId());
                            System.out.println("Date of Travel: " + bookedTickets.get(i).getDateOfTravel());
                            System.out.println(
                                    "Boarding Station: " + bookedTickets.get(i).getSource() + "  Alighting Station: "
                                            + bookedTickets.get(i).getDestination());
                            System.out.println("Seats Booked" + bookedTickets.get(i).getSeatNumbers());
                            System.out.println();
                        }

                        System.out.println("Enter Ticket ID to cancel Ticket: ");
                        String ticketIdToCancel = scanner.nextLine();
                        System.out.println("Enter number of Seats you want to Cancel:");
                        int numberOfSeatsToCancel = scanner.nextInt();
                        List<Integer> seatNumbersToCancel = new ArrayList<>();
                        for (int i = 0; i < numberOfSeatsToCancel; i++) {
                            System.out.println("Enter #" + (i + 1) + " Seat No. to cancel");
                            seatNumbersToCancel.add(scanner.nextInt());
                        }
                        userBookingService.cancelTicketBooking(user, ticketIdToCancel, seatNumbersToCancel);

                        System.out.println();
                        break;

                    case 7:
                        System.out.println(GREEN + "Thank you for using the Train Ticket Booking System.");
                        System.out.println(RESET + "Exiting the application...");
                        System.out.println();
                        break;

                    default:
                        System.out.println(RED + "Invalid choice.");
                        System.out.println();
                        break;
                }
            } else {
                System.out.println(RESET + "Invalid input. Please enter a valid integer.");
                System.out.println();
                scanner.next();
            }
        }
        System.out.println(RESET);
        scanner.close();
    }
}
