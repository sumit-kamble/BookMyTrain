package myjava.myprojects.services;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import myjava.myprojects.entities.Coach;
import myjava.myprojects.entities.Ticket;
import myjava.myprojects.entities.Train;
import myjava.myprojects.entities.User;
import myjava.myprojects.utils.UserServiceUtil;

// ================================= User Booking Service =====================================================================

public class UserBookingService {
    private User user;
    private List<User> userList;
    private List<Train> trainList;
    private File users;
    private File trains;
    private ObjectMapper objectMapper = new ObjectMapper();

    private static final String USERS_PATH = "C:/Users/sumit/Desktop/lang/train-ticket-booking-app/app/src/main/java/myjava/myprojects/localDb/users.json";
    private static final String TRAINS_PATH = "C:/Users/sumit/Desktop/lang/train-ticket-booking-app/app/src/main/java/myjava/myprojects/localDb/trains.json";

    public UserBookingService(User user) throws Exception {
        this.user = user;
        loadUsers();
        loadTrains();
    }

    public UserBookingService() throws IOException {
        loadUsers();
        loadTrains();
    }

    public List<User> loadUsers() throws IOException {
        users = new File(USERS_PATH);
        return userList = objectMapper.readValue(users, new TypeReference<List<User>>() {
        });
    }

    public List<Train> loadTrains() throws IOException {
        trains = new File(TRAINS_PATH);
        return trainList = objectMapper.readValue(trains, new TypeReference<List<Train>>() {
        });
    }

    public User loginUser(String userName, String userPassword) {
        Optional<User> foundUser = userList.stream()
                .filter(user1 -> user1.getName().equals(userName)
                        && UserServiceUtil.checkPassword(userPassword, user1.getHashedPassword()))
                .findFirst();
        return foundUser.orElseThrow(() -> new RuntimeException("User not found or password incorrect"));
    }

    private void saveUsersListToFile() throws IOException {
        // File usersFile = new File(USERS_PATH);
        // objectMapper.writeValue(usersFile, userList);
        objectMapper.writeValue(Paths.get(USERS_PATH).toFile(), userList);
    }

    public Boolean signUp(User user1) {
        boolean userExists = userList.stream().anyMatch(user -> user.getName().equals(user1.getName()));
        if (userExists) {
            return false;
        }
        try {
            userList.add(user1);
            saveUsersListToFile();
            return true;
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public void fetchBooking(User user) {
        List<Ticket> bookedTickets = user.fetchBookedTickets();

        if (bookedTickets.isEmpty()) {
            System.out.println("No tickets booked yet.");
        } else {
            bookedTickets.forEach(ticket -> System.out.println(ticket));
        }
    }

    public String findUserIdFromTicketId(String ticketId) {
        for (User user : userList) {
            if (user.getTicketsBooked() != null) {
                for (Ticket ticket : user.getTicketsBooked()) {
                    if (ticket.getTicketId().equals(ticketId)) {
                        return user.getUserId();
                    }
                }
            }
        }
        return null;
    }

    public Boolean cancelBooking(String ticketId) {
        String userId = findUserIdFromTicketId(ticketId);

        if (userId == null) {
            System.out.println("userId" + userId + "doesn't exit in database");
            return false;
        }

        Optional<User> userOpt = userList.stream().filter(user -> user.getUserId().equals(userId)).findFirst();

        if (userOpt.isEmpty()) {
            return false;
        }

        User user = userOpt.get();

        Optional<Ticket> ticketOpt = user.getTicketsBooked().stream()
                .filter(ticket -> ticket.getTicketId().equals(ticketId)).findFirst();

        if (ticketOpt.isEmpty()) {
            return false;
        }

        Ticket ticket = ticketOpt.get();

        user.getTicketsBooked().remove(ticket);

        // implement seat to connect Ticket and Train classes
        // implement to unbook seat on train
        // Train train = ticket.getTrain();

        try {
            saveUsersListToFile();
            // pending work
            // trainService.saveTrainsListToFile();
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }

        return true;
    }

    public void cancelTicketBooking(User user, String ticketIdToCancel, List<Integer> seatNumbersToCancel) {
        Ticket ticketToCancel = user.getTicketsBooked().stream()
                .filter(ticket -> ticket.getTicketId().equals(ticketIdToCancel))
                .findFirst()
                .orElse(null);

        if (ticketToCancel == null) {
            System.out.println("Ticket with ID " + ticketIdToCancel + " not found.");
            return;
        }

        List<Integer> bookedSeats = ticketToCancel.getSeatNumbers();
        List<Integer> invalidSeats = seatNumbersToCancel.stream()
                .filter(seat -> !bookedSeats.contains(seat))
                .collect(Collectors.toList());

        if (!invalidSeats.isEmpty()) {
            System.out.println("Invalid seat numbers for cancellation: " + invalidSeats);
            return;
        }

        bookedSeats.removeAll(seatNumbersToCancel);

        if (bookedSeats.isEmpty()) {
            user.getTicketsBooked().remove(ticketToCancel);
            System.out.println("Ticket " + ticketIdToCancel + " has been fully canceled.");
        } else {
            System.out.println("Updated Ticket: " + ticketToCancel.getTicketId());
        }

        Train train = trainList.stream().filter(train1 -> train1.getTrainId().equals(ticketToCancel.getTrainId()))
                .findFirst().orElse(null);
        if (train == null) {
            System.out.println("Error: Train data is missing.");
            return;
        }
        System.out.println("Train ID: " + train.getTrainId());

        // System.out.println("Available coaches in the train:");
        // for (Coach coach : train.getCoaches()) {
        // System.out.println("Coach ID: " + coach.getCoachId());
        // }

        System.out.println("Looking for Coach ID: " + ticketToCancel.getCoachId());

        if (train.getCoaches() == null || train.getCoaches().isEmpty()) {
            if (train.getCoaches() == null) {
                System.out.println("The coaches list is null.");
                return;
            } else if (train.getCoaches().isEmpty()) {
                System.out.println("The coaches list is empty.");
                return;
            }
        }

        Coach ticketCoach = train.getCoaches().stream()
                .filter(coach -> coach.getCoachId().equals(ticketToCancel.getCoachId()))
                .findFirst()
                .orElse(null);

        if (ticketCoach == null) {
            System.out.println("Coach with ID " + ticketToCancel.getCoachId() + " not found in the train.");
            return;
        }

        System.out.println("Ticket Coach Id: " + ticketCoach.getCoachId());
        List<List<Integer>> seats = ticketCoach.getSeats();
        for (int seatNumber : seatNumbersToCancel) {
            int row = (seatNumber - 1) / seats.get(0).size();
            int column = (seatNumber - 1) % seats.get(0).size();

            if (row >= 0 && row < seats.size() && column >= 0 && column < seats.get(row).size()) {
                seats.get(row).set(column, 0);
            } else {
                System.out.println("Error: Seat #" + seatNumber + " is invalid in the seating arrangement.");
            }
        }

        try {
            userList = userList.stream().map(existingUser -> {
                if (existingUser.getUserId().equals(user.getUserId())) {
                    return user;
                }
                return existingUser;
            }).collect(Collectors.toList());

            objectMapper.writeValue(Paths.get(USERS_PATH).toFile(), userList);
        } catch (IOException e) {
            System.err.println("Error updating users data: " + e.getMessage());
        }

        try {
            trainList = trainList.stream().map(existingTrain -> {
                if (existingTrain.getTrainId().equals(train.getTrainId())) {
                    return train;
                }
                return existingTrain;
            }).collect(Collectors.toList());

            objectMapper.writeValue(Paths.get(TRAINS_PATH).toFile(), trainList);
        } catch (IOException e) {
            System.err.println("Error updating trains data " + e.getMessage());
        }

        System.out.println("Seat cancellation process completed successfully.");
    }

}
