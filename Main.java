public class Main {
    public static void main(String[] args) {

        // ── STEVEN: Create a pet ──────────────────────────────
        Owner owner = new Owner("Maria", "0917-555-1234");
        Pet pet = new Pet("Doggo", Species.DOG, owner);
        MedicalHistory history = new MedicalHistory(pet);

        // ── ROJ: Create clinic staff ───────────────────────
        Veterinarian vet = new Veterinarian("Dr. Reyes", "Surgery");
        Staff staff = new Staff("Ana", "Receptionist");

        // ── SAIRA: Set up schedule & book appointment ────────
        Schedule schedule = new Schedule(vet);
        Timeslot slot = new Timeslot("2025-06-01", "10:00 AM");
        schedule.addTimeslot(slot);
        Appointment appointment = new Appointment(pet, vet, slot);

        // ── BRANDON: Clinical outcome ──────────────────────────
        Diagnosis diagnosis = new Diagnosis("Otitis Externa");
        Medication medication = new Medication("Ear drops", "2x daily");
        Procedure procedure = new Procedure("Ear cleaning");
        TreatmentPlan plan = new TreatmentPlan(appointment, diagnosis, medication, procedure);
        history.addTreatmentPlan(plan);

        // ── LANHCE: Billing ───────────────────────────────────
        Service service = new Service("Ear Cleaning", 500.00);
        Invoice invoice = new Invoice(appointment, service);
        Payment payment = new Payment(invoice, "GCash");
        payment.process();

        System.out.println("Appointment complete for: " + pet.getName());
    }
}
