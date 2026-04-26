package service;

import model.scheduling.Appointment;
import java.util.ArrayList;
import java.util.List;

public class AppointmentManager {
    private List<Appointment> appointments;

    public AppointmentManager() {
        this.appointments = new ArrayList<>();
    }

    public void book(Appointment appointment) {
        appointments.add(appointment);
        System.out.println("Appointment booked: " + appointment);
    }

    public void cancel(Appointment appointment) {
        if (appointments.remove(appointment)) {
            appointment.getTimeslot().setAvailable(true);
            System.out.println("Appointment cancelled: " + appointment);
        }
    }

    public List<Appointment> getAllAppointments() {
        return appointments;
    }
}