package myjava.myprojects.entities;

import java.util.List;
import java.util.Map;

public class Train {
    private String trainId;
    private String trainNo;
    private Map<String, String> stationTimes;
    private List<String> station;
    private List<String> dates;
    private List<Coach> coaches;

    public Train() {
    }

    public Train(String trainId, String trainNo, Map<String, String> stationTimes,
            List<String> station, List<String> dates, List<Coach> coaches) {
        this.trainId = trainId;
        this.trainNo = trainNo;
        this.stationTimes = stationTimes;
        this.station = station;
        this.dates = dates;
        this.coaches = coaches;
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

    public Map<String, String> getStationTimes() {
        return stationTimes;
    }

    public void setStationTimes(Map<String, String> stationTimes) {
        this.stationTimes = stationTimes;
    }

    public List<String> getStation() {
        return station;
    }

    public void setStation(List<String> station) {
        this.station = station;
    }

    public List<String> getDates() {
        return dates;
    }

    public void setDates(List<String> dates) {
        this.dates = dates;
    }

    public List<Coach> getCoaches() {
        return coaches;
    }

    public void setCoaches(List<Coach> coaches) {
        this.coaches = coaches;
    }

    public void getTrain(Train train) {
        System.out.println("Train Id: " + train.trainId + " Train No: " + train.trainNo);
        System.out
                .println("Source: " + train.station.get(0) + " Destionation: " + train.station.get(station.size() - 1));
        System.out.println("Runs on:");
        for (String days : train.dates) {
            System.out.print(days + " ");
        }
        System.out.println();
        System.out.println("Check if seats available in Booking ticket service below");
    }

    @Override
    public String toString() {

        return String.format(
                "Train ID: %s\nTrain No: %s\nStation Times: %s\nStations: %s",
                trainId, trainNo, stationTimes, station);
    }
}
