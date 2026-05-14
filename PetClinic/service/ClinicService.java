package PetClinic.service;

import PetClinic.model.billing.Invoice;
import PetClinic.model.billing.Payment;
import PetClinic.model.billing.Service;
import PetClinic.model.clinical.Diagnosis;
import PetClinic.model.clinical.Medication;
import PetClinic.model.clinical.Procedure;
import PetClinic.model.clinical.TreatmentPlan;
import PetClinic.model.pet.Pet;
import PetClinic.model.pet.Species;
import PetClinic.model.scheduling.Appointment;
import PetClinic.model.scheduling.Timeslot;
import PetClinic.model.user.Owner;
import PetClinic.model.user.User;
import model.user.UserAccountStore;
import PetClinic.model.user.Veterinarian;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ClinicService {
    private final UserAccountStore accountStore = new UserAccountStore();
    private final List<Pet> pets = new ArrayList<>();
    private final List<Appointment> appointments = new ArrayList<>();
    private final List<Service> services = new ArrayList<>();
    private final List<Invoice> invoices = new ArrayList<>();
    private final Veterinarian defaultVeterinarian = new Veterinarian("Clinic Veterinarian", "General Practice", "vet", "vet123");

    public ClinicService() {
        services.add(new Service("Consultation", 500));
        services.add(new Service("Vaccination", 850));
        services.add(new Service("Grooming", 650));
        services.add(new Service("Laboratory Test", 1200));
    }

    public UserAccountStore getAccountStore() {
        return accountStore;
    }

    public List<Pet> getPetsForOwner(Owner owner) {
        List<Pet> result = new ArrayList<>();
        for (Pet pet : pets) {
            if (pet.getOwner() == owner) result.add(pet);
        }
        return result;
    }

    public Pet registerPet(Owner owner, String name, Species species) {
        requireOwner(owner);
        String cleanName = clean(name);
        if (cleanName.isEmpty()) throw new IllegalArgumentException("Pet name is required.");
        Pet existing = findPet(owner, cleanName);
        if (existing != null) return existing;
        Pet pet = new Pet(cleanName, species, owner);
        pets.add(pet);
        return pet;
    }

    public Appointment requestAppointment(Owner owner, String petName, Species species, String date, String time, String reason) {
        requireOwner(owner);
        String cleanDate = clean(date);
        String cleanTime = clean(time);
        validateDate(cleanDate);
        if (cleanTime.isEmpty()) throw new IllegalArgumentException("Preferred time is required.");
        if (isTimeslotTaken(cleanDate, cleanTime)) {
            throw new IllegalArgumentException("That date and time is already booked. Please choose another timeslot.");
        }

        Pet pet = registerPet(owner, petName, species);
        Appointment appointment = new Appointment(
                pet,
                defaultVeterinarian,
                new Timeslot(cleanTime, ""),
                cleanDate,
                clean(reason).isEmpty() ? "General" : clean(reason)
        );
        appointments.add(appointment);
        return appointment;
    }

    public List<Appointment> getAppointments() {
        return Collections.unmodifiableList(appointments);
    }

    public List<Appointment> getAppointmentsForOwner(Owner owner) {
        List<Appointment> result = new ArrayList<>();
        for (Appointment appointment : appointments) {
            if (appointment.getPet().getOwner() == owner) result.add(appointment);
        }
        return result;
    }

    public void updateAppointmentStatus(Appointment appointment, String status) {
        if (appointment == null) return;
        appointment.setStatus(status);
    }

    public void deleteAppointment(Appointment appointment) {
        appointments.remove(appointment);
    }

    public TreatmentPlan createTreatmentPlan(Appointment appointment, String condition, String notes, String medication, String dosage, int durationDays, String procedure, String procedureDescription) {
        if (appointment == null) throw new IllegalArgumentException("Select an appointment first.");
        TreatmentPlan plan = new TreatmentPlan(new Diagnosis(clean(condition), clean(notes)));
        if (!clean(medication).isEmpty()) {
            plan.addMedication(new Medication(clean(medication), clean(dosage), Math.max(durationDays, 1)));
        }
        if (!clean(procedure).isEmpty()) {
            plan.addProcedure(new Procedure(clean(procedure), clean(procedureDescription)));
        }
        appointment.setTreatmentPlan(plan);
        appointment.setStatus("Completed");
        return plan;
    }

    public List<Service> getServices() {
        return Collections.unmodifiableList(services);
    }

    public void addService(String name, double price) {
        String cleanName = clean(name);
        if (cleanName.isEmpty()) throw new IllegalArgumentException("Service name is required.");
        if (price <= 0) throw new IllegalArgumentException("Service price must be greater than zero.");
        services.add(new Service(cleanName, price));
    }

    public Invoice generateInvoice(Appointment appointment, Service service) {
        if (appointment == null) throw new IllegalArgumentException("Select an appointment first.");
        if (service == null) throw new IllegalArgumentException("Select a service first.");
        Invoice invoice = new Invoice(appointment, service);
        invoices.add(invoice);
        return invoice;
    }

    public List<Invoice> getInvoices() {
        return Collections.unmodifiableList(invoices);
    }

    public List<Invoice> getInvoicesForOwner(Owner owner) {
        List<Invoice> result = new ArrayList<>();
        for (Invoice invoice : invoices) {
            if (invoice.getAppointment().getPet().getOwner() == owner) result.add(invoice);
        }
        return result;
    }

    public void payInvoice(Invoice invoice, String method) {
        if (invoice == null) throw new IllegalArgumentException("Select an invoice first.");
        new Payment(invoice, clean(method).isEmpty() ? "Cash" : clean(method)).process();
    }

    public boolean isVeterinarian(User user) {
        return user != null && "Veterinarian".equalsIgnoreCase(user.getRole());
    }

    public boolean isStaff(User user) {
        return user != null && "Staff".equalsIgnoreCase(user.getRole());
    }

    private Pet findPet(Owner owner, String petName) {
        for (Pet pet : pets) {
            if (pet.getOwner() == owner && pet.getName().equalsIgnoreCase(petName)) return pet;
        }
        return null;
    }

    private boolean isTimeslotTaken(String date, String time) {
        for (Appointment appointment : appointments) {
            if (appointment.getDate().equals(date)
                    && appointment.getTimeslot().getStartTime().equalsIgnoreCase(time)
                    && !"Cancelled".equals(appointment.getStatus())) {
                return true;
            }
        }
        return false;
    }

    private void validateDate(String date) {
        try {
            LocalDate.parse(date);
        } catch (DateTimeParseException ex) {
            throw new IllegalArgumentException("Date must use YYYY-MM-DD format.");
        }
    }

    private void requireOwner(Owner owner) {
        if (owner == null) throw new IllegalArgumentException("A customer account is required.");
    }

    private String clean(String value) {
        return value == null ? "" : value.trim();
    }
}
