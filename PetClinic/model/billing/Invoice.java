package PetClinic.model.billing;

import model.scheduling.Appointment;

public class Invoice {
    private Appointment appointment;
    private Service service;
    private boolean paid;

    public Invoice(Appointment appointment, Service service) {
        this.appointment = appointment;
        this.service = service;
        this.paid = false;
    }

    public Appointment getAppointment() {
        return appointment;
    }

    public Service getService() {
        return service;
    }

    public double getTotalAmount() {
        return service.getPrice();
    }

    public boolean isPaid() {
        return paid;
    }

    public void markAsPaid() {
        this.paid = true;
    }

    @Override
    public String toString() {
        return "Invoice[service=" + service.getName()
                + ", amount=" + service.getPrice()
                + ", paid=" + paid + "]";
    }
}