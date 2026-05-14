package PetClinic.model.billing;

import PetClinic.model.scheduling.Appointment;

public class Invoice {
    private Appointment appointment;
    private Service service;
    private boolean paid;
    private String paymentMethod;

    public Invoice(Appointment appointment, Service service) {
        this.appointment = appointment;
        this.service = service;
        this.paid = false;
        this.paymentMethod = "";
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
        markAsPaid("Cash");
    }

    public void markAsPaid(String paymentMethod) {
        this.paid = true;
        this.paymentMethod = paymentMethod == null ? "" : paymentMethod.trim();
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    @Override
    public String toString() {
        return "Invoice[service=" + service.getName()
                + ", amount=" + service.getPrice()
                + ", paid=" + paid + "]";
    }
}
