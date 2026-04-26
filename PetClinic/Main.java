/*import model.user.Owner;
import model.user.Veterinarian;
import model.user.Staff;
import model.pet.Pet;
import model.pet.Species;
import model.pet.MedicalHistory;
import model.scheduling.Schedule;
import model.scheduling.Timeslot;
import model.scheduling.Appointment;
import model.clinical.Diagnosis;
import model.clinical.Medication;
import model.clinical.Procedure;
import model.clinical.TreatmentPlan;*/
import PetClinic.model.billing.Service;
import PetClinic.model.billing.Invoice;
import PetClinic.model.billing.Payment;

public class Main {
    public static void main(String[] args) {

        // ── STEVEN: Create a pet ──────────────────────────────


        // ── ROJ: Create clinic staff ───────────────────────────


        // ── SAIRA: Set up schedule & book appointment ──────────


        // ── BRANDON: Clinical outcome ──────────────────────────


        // ── LANHCE: Billing ────────────────────────────────────
        Service service = new Service("Ear Cleaning", 500.00);
        Invoice invoice = new Invoice(appointment, service);
        Payment payment = new Payment(invoice, "GCash");
        payment.process();

        System.out.println("Appointment complete for: " + pet.getName());
    }
}