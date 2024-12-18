package myjava.myprojects.entities;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class Ticket {
    private String ticketId;
    private String userId;
    private String source;
    private String destination;
    private Date dateOfTravel;
    private String coachId;
    private List<Integer> seatNumbers;
    private String trainId;
    private String trainNo;

    public Ticket() {
    }

    public Ticket(String ticketId, String userId, String source, String destination, Date dateOfTravel,
            String coachId, List<Integer> seatNumbers, String trainId, String trainNo) {
        this.ticketId = ticketId;
        this.userId = userId;
        this.source = source;
        this.destination = destination;
        this.dateOfTravel = dateOfTravel;
        this.coachId = coachId;
        this.seatNumbers = seatNumbers;
        this.trainId = trainId;
        this.trainNo = trainNo;
    }

    public String getTicketId() {
        return ticketId;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Date getDateOfTravel() {
        return dateOfTravel;
    }

    public void setDateOfTravel(Date dateOfTravel) {
        this.dateOfTravel = dateOfTravel;
    }

    public String getCoachId() {
        return coachId;
    }

    public void setCoachId(String coachId) {
        this.coachId = coachId;
    }

    public List<Integer> getSeatNumbers() {
        return seatNumbers;
    }

    public void setSeatNumbers(List<Integer> seatNumbers) {
        this.seatNumbers = seatNumbers;
    }

    public String getTrainId() {
        return trainId;
    }

    public void setTrainId(String trainId) {
        this.trainId = trainId;
    }

    public String getTrainNo() {
        return trainNo;
    }

    public void setTrainNo(String trainNo) {
        this.trainNo = trainNo;
    }

    @Override
    public String toString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        String formattedDate = (dateOfTravel != null) ? dateFormat.format(dateOfTravel) : "N/A";

        String seatNumbersFormatted = (seatNumbers != null && !seatNumbers.isEmpty()) ? seatNumbers.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(", "))
                : "N/A";

        return String.format(
                "Ticket ID: %s\nUser ID: %s\nSource: %s\nDestination: %s\nDate of Travel: %s\nCoach ID: %s\nSeat Numbers: %s\nTrain ID: %s\nTrain NO: %s\n",
                ticketId != null ? ticketId : "N/A",
                userId != null ? userId : "N/A",
                source != null ? source : "N/A",
                destination != null ? destination : "N/A",
                formattedDate,
                coachId != null ? coachId : "N/A",
                seatNumbersFormatted,
                trainId != null ? trainId : "N/A",
                trainNo != null ? trainNo : "N/A");
    }
}
