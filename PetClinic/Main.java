package PetClinic;

import PetClinic.model.user.Owner;
import PetClinic.model.user.Veterinarian;
import PetClinic.model.user.Staff;
import PetClinic.model.pet.Pet;
import PetClinic.model.pet.Species;
import PetClinic.model.pet.MedicalHistory;
import PetClinic.model.scheduling.Schedule;
import PetClinic.model.scheduling.Timeslot;
import PetClinic.model.scheduling.Appointment;
import PetClinic.model.clinical.Diagnosis;
import PetClinic.model.clinical.Medication;
import PetClinic.model.clinical.Procedure;
import PetClinic.model.clinical.TreatmentPlan;
import PetClinic.model.billing.Service;
import PetClinic.model.billing.Invoice;
import PetClinic.model.billing.Payment;

public class  Main {
    public static void main(String[] args) {

        // ── STEVEN: Create a pet ──────────────────────────────
        Owner owner = new Owner("Maria", "0917-555-1234");
        Pet pet = new Pet("Doggo", Species.MAMMAL, owner);
        MedicalHistory history = new MedicalHistory(pet);


        // ── ROJ: Create clinic staff ───────────────────────────
        Veterinarian vet = new Veterinarian("Dr. Smith", "smith@clinic.com");
        Staff staff = new Staff("Jane", "jane@clinic.com");

        // ── SAIRA: Set up schedule & book appointment ──────────
        Schedule schedule = new Schedule(vet);
        Timeslot slot = new Timeslot("2025-06-01", "10:00 AM");
        Appointment appointment = new Appointment(pet, vet, slot, "pending");
        schedule.addAppointment(appointment);


        // ── BRANDON: Clinical outcome ──────────────────────────
        Diagnosis diagnosis = new Diagnosis("Ear Infection", "Mild redness and irritation");
        TreatmentPlan treatmentPlan = new TreatmentPlan(diagnosis);

        Medication med = new Medication("Amoxicillin", "250mg twice daily", 7);
        Procedure procedure = new Procedure("Ear Cleaning", "Performed with antiseptic solution");

        treatmentPlan.addMedication(med);
        treatmentPlan.addProcedure(procedure);

        appointment.setTreatmentPlan(treatmentPlan);
        // ── LANHCE: Billing ────────────────────────────────────
        Service service = new Service("Ear Cleaning", 500.00);
        Invoice invoice = new Invoice(appointment, service);
        Payment payment = new Payment(invoice, "GCash");
        payment.process();

        System.out.println("Appointment complete for: " + pet.getName());
        System.out.println("Treatment Plan:\n" + treatmentPlan);
    }
}
