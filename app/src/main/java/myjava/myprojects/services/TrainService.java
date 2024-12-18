package myjava.myprojects.services;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import myjava.myprojects.entities.Coach;
import myjava.myprojects.entities.Ticket;
import myjava.myprojects.entities.Train;
import myjava.myprojects.entities.User;

public class TrainService {
    private Train train;
    private List<Train> trainList;
    private ObjectMapper objectMapper = new ObjectMapper();
    private static final String TRAINS_PATH = "C:/Users/sumit/Desktop/lang/train-ticket-booking-app/app/src/main/java/myjava/myprojects/localDb/trains.json";
    private static final String USERS_PATH = "C:/Users/sumit/Desktop/lang/train-ticket-booking-app/app/src/main/java/myjava/myprojects/localDb/users.json";

    public TrainService() throws Exception {
        loadTrains();
    }

    public TrainService(Train train) throws IOException {
        this.train = train;
        loadTrains();
    }

    public List<Train> loadTrains() throws IOException {
        File trains = new File(TRAINS_PATH);
        return trainList = objectMapper.readValue(trains, new TypeReference<List<Train>>() {
        });
    }

    public void searchTrain(String sourceLocation, String destinationLocation) {
        List<Train> matchingTrains = new ArrayList<>();
        for (Train train : trainList) {
            List<String> stations = train.getStation();
            if (stations.contains(sourceLocation) && stations.contains(destinationLocation)) {
                int sourceIndex = stations.indexOf(sourceLocation);
                int destinationIndex = stations.indexOf(destinationLocation);
                if (sourceIndex < destinationIndex) {
                    matchingTrains.add(train);
                }
            }
        }

        if (matchingTrains.isEmpty()) {
            System.out.println("No Trains found.");
        } else {
            for (Train train : matchingTrains) {
                this.train = train;
                train.getTrain(train);
            }
        }
    }

    public Train searchTrainById(String trainIdOrTrainNo) {
        Train matchingTrain = null;
        for (Train train : trainList) {
            if (train.getTrainId().equals(trainIdOrTrainNo) || train.getTrainNo().equals(trainIdOrTrainNo)) {
                matchingTrain = train;
                return matchingTrain;
            }
        }
        return matchingTrain;
    }

    private void displayCoachSeats(Coach coach) {
        System.out.println("Coach ID: " + coach.getCoachId() + " (" + coach.getCoachType() + ")");
        List<List<Integer>> seats = coach.getSeats();
        int seatIndex = 1; // Seat numbering starts from 1
        int rowCount = 0;

        for (List<Integer> row : seats) {
            rowCount++;
            for (int seat : row) {
                String position;
                switch (rowCount % 4) { // Define positions in cycles
                    case 1:
                        position = "Lower Berth";
                        break;
                    case 2:
                        position = "Middle Berth";
                        break;
                    case 3:
                        position = "Upper Berth";
                        break;
                    default:
                        position = "Side Berth";
                        break;
                }

                if (seat == 0) { // Seat is available
                    System.out.printf("Seat #%d (%s): Available%n", seatIndex, position);
                }
                seatIndex++;
            }
        }
    }

    public void showAvailableSeats(Train train, String coachId) {
        if (train.getCoaches().isEmpty()) {
            System.out.println("This train doesn't have any coach. It may not be ready yet.");
            return;
        }

        System.out.println("Total Coaches: " + train.getCoaches().size());
        System.out.println("Current status of seat availability:");
        if (coachId == null) {

            for (Coach coach : train.getCoaches()) {
                displayCoachSeats(coach);
            }
        } else {
            // Show specific coach
            Coach selectedCoach = train.getCoaches().stream()
                    .filter(coach -> coach.getCoachId().equalsIgnoreCase(coachId))
                    .findFirst()
                    .orElse(null);

            if (selectedCoach != null) {
                displayCoachSeats(selectedCoach);
            } else {
                System.out.println("Coach ID " + coachId + " not found in this train.");
            }
        }
    }

    private void saveUserAndTrainsToFile(User user, Train train) {
        ObjectMapper objectMapper = new ObjectMapper();
        File usersFile = new File(USERS_PATH);
        File trainsFile = new File(TRAINS_PATH);

        try {
            List<User> users = new ArrayList<>();
            List<Train> trains = new ArrayList<>();
            if (usersFile.exists() && trainsFile.exists()) {
                // Read the existing users
                users = objectMapper.readValue(usersFile, new TypeReference<List<User>>() {
                });
                trains = objectMapper.readValue(
                        Paths.get(TRAINS_PATH).toFile(),
                        new TypeReference<List<Train>>() {
                        });
            } else {
                System.out.println("Failed  to read user data or trains data");
            }

            // Update or add the current user
            users = users.stream()
                    .filter(u -> !u.getUserId().equals(user.getUserId()))
                    .collect(Collectors.toList());
            users.add(user);

            trains = trains.stream().map(existingTrain -> {
                if (existingTrain.getTrainId().equals(train.getTrainId())) {
                    return train; // Update the current train with new seat data
                }
                return existingTrain;
            }).collect(Collectors.toList());

            // Write the updated users back to the file
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(usersFile, users);
            objectMapper.writeValue(Paths.get(TRAINS_PATH).toFile(), trains);
        } catch (IOException e) {
            System.out.println("Failed . . .");
            System.out.println("Error saving user data: " + e.getMessage());
        }

    }

    public void bookSeatForUser(User user, Train train, String coachId, List<Integer> seatNumbers,
            String boardingStation, String alightingStation) {
        Coach selectedCoach = train.getCoaches().stream()
                .filter(coach -> coach.getCoachId().equalsIgnoreCase(coachId))
                .findFirst()
                .orElse(null);

        if (selectedCoach == null) {
            System.out.println("Coach ID " + coachId + " not found in this train.");
            return;
        }

        int boardingStationIndex = train.getStation().indexOf(boardingStation);
        int alightingStationIndex = train.getStation().indexOf(alightingStation);

        if (boardingStationIndex < 0 || boardingStationIndex >= train.getStation().size() ||
                alightingStationIndex <= boardingStationIndex || alightingStationIndex >= train.getStation().size()) {
            System.out.println("Invalid station selection. Please try again.");
            return;
        }
        List<List<Integer>> seats = selectedCoach.getSeats();
        // int totalSeats = 0;

        List<Integer> bookedSeats = new ArrayList<>();
        for (int seatNumber : seatNumbers) {
            int seatNumberToBook = seatNumber;

            int row = (seatNumberToBook - 1) / seats.get(0).size();
            int column = (seatNumberToBook - 1) % seats.get(0).size();

            if (row < 0 || row >= seats.size() || column < 0 || column >= seats.get(row).size()) {
                System.out.println("Invalid seat number.");
                return;
            }

            if (seats.get(row).get(column) == 1) {
                System.out.println("Seat #" + seatNumberToBook + " is already occupied.");
                return;
            }

            seats.get(row).set(column, 1);
            System.out.println("Seat #" + seatNumberToBook + " has been successfully booked!");

            bookedSeats.add(seatNumber);
        }

        // Create a ticket and add to the user's booked tickets
        Ticket newTicket = new Ticket(
                UUID.randomUUID().toString(),
                user.getUserId(),
                train.getStation().get(
                        boardingStationIndex),
                train.getStation().get(
                        alightingStationIndex),
                new Date(),
                selectedCoach.getCoachId(),
                bookedSeats,
                train.getTrainId(),
                train.getTrainNo());

        try {
            user.getTicketsBooked().add(newTicket);
            saveUserAndTrainsToFile(user, train);
            System.out.println("Ticket successfully added to your bookings:");
            System.out.println(newTicket);
            // pending work
            // trainService.saveTrainsListToFile();
        } catch (Exception ex) {
            System.out.println("Booking failed . . . ");
            ex.printStackTrace();

        }

    }

}
