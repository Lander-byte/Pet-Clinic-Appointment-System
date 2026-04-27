/*import model.user.Owner;
import model.user.Veterinarian;
import model.user.Staff;
import model.pet.Pet;
import model.pet.Species;
import model.pet.MedicalHistory;
import model.scheduling.Schedule;
import model.scheduling.Timeslot;
import model.scheduling.Appointment;
import model.clinical.PetClinic.model.clinical.Diagnosis;
import model.clinical.PetClinic.model.clinical.Medication;
import model.clinical.PetClinic.model.clinical.Procedure;
import model.clinical.PetClinic.model.clinical.TreatmentPlan;*/
import PetClinic.model.billing.Service;
import PetClinic.model.billing.Invoice;
import PetClinic.model.billing.Payment;

public class Main {
    public static void main(String[] args) {

        // ── STEVEN: Create a pet ──────────────────────────────
        Owner owner = new Owner("Maria", "0917-555-1234");
        Pet pet = new Pet("Doggo", Species.DOG, owner);
        MedicalHistory history = new MedicalHistory(pet);


        // ── ROJ: Create clinic staff ───────────────────────────
        Veterinarian vet = new Veterinarian("Dr. Smith", "smith@clinic.com");
        Staff staff = new Staff("Jane", "jane@clinic.com");

        // ── SAIRA: Set up schedule & book appointment ──────────


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
