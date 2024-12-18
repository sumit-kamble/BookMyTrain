package myjava.myprojects.entities;

import java.util.List;

public class TrainStation {
    private List<String> stations;

    public TrainStation() {

    }

    public TrainStation(List<String> stations) {
        this.stations = stations;
    }

    public List<String> getStation() {
        return stations;
    }

    public void setStation(List<String> stations) {
        this.stations = stations;
    }
}
