package myjava.myprojects.entities;

import java.util.List;

public class Coach {
    private String coachType;
    private String coachId;
    private List<List<Integer>> seats;

    public Coach() {

    }

    public Coach(String coachType, String coachId, List<List<Integer>> seats) {
        this.coachType = coachType;
        this.coachId = coachId;
        this.seats = seats;
    }

    public String getCoachType() {
        return coachType;
    }

    public void setCoachType(String coachType) {
        this.coachType = coachType;
    }

    public String getCoachId() {
        return coachId;
    }

    public void setCoachId(String coachId) {
        this.coachId = coachId;
    }

    public List<List<Integer>> getSeats() {
        return seats;
    }

    public void setSeats(List<List<Integer>> seats) {
        this.seats = seats;
    }
}
