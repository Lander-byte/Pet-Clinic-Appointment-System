package PetClinic.model.scheduling;

public class Timeslot {
    private String startTime;
    private String endTime;
    private boolean available;

    public Timeslot(String startTime, String endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.available = true;
    }

    public String getStartTime() { return startTime; }
    public String getEndTime()   { return endTime; }
    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }

    @Override
    public String toString() {
        return startTime + " → " + endTime;
    }
}
