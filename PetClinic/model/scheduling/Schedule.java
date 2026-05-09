package PetClinic.model.scheduling;

import PetClinic.model.user.Veterinarian;
import java.util.ArrayList;
import java.util.List;

public class Schedule {
    private Veterinarian vet;
    private List<Appointment> appointments;

    public Schedule(Veterinarian vet) {
        this.vet = vet;
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