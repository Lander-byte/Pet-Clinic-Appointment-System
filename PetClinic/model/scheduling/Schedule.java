package PetClinic.model.scheduling;


import java.util.ArrayList;
import java.util.List;

public class Schedule {
    private List<Appointment> appointments;

    public Schedule() {
        this.appointments = new ArrayList<>();
    }

    public void addAppointment(Appointment appointment) {
        appointments.add(appointment);
        System.out.println("Booked: " + appointment);
    }

    public List<Appointment> getAppointments() {
        return appointments;
    }

    public void printSchedule() {
        System.out.println("=== Schedule ===");
        for (Appointment a : appointments) {
            System.out.println(a);
        }
    }
}