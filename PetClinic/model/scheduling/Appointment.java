package PetClinic.model.scheduling;

import PetClinic.model.clinical.TreatmentPlan;
import PetClinic.model.pet.Pet;
import PetClinic.model.user.Veterinarian;

public class Appointment {
    private Pet pet;
    private Veterinarian veterinarian;
    private Timeslot timeslot;
    private String date;
    private String reason;
    private String status;
    private TreatmentPlan treatmentPlan;

    public Appointment(Pet pet, Veterinarian veterinarian, Timeslot timeslot, String reason) {
        this(pet, veterinarian, timeslot, "", reason);
    }

    public Appointment(Pet pet, Veterinarian veterinarian, Timeslot timeslot, String date, String reason) {
        this.pet = pet;
        this.veterinarian = veterinarian;
        this.timeslot = timeslot;
        this.date = date;
        this.reason = reason;
        this.status = "Pending";
        this.timeslot.setAvailable(false);
    }

    public Pet getPet() {
        return pet;
    }

    public Veterinarian getVeterinarian() {
        return veterinarian;
    }

    public Timeslot getTimeslot() {
        return timeslot;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getReason() {
        return reason;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public TreatmentPlan getTreatmentPlan() {
        return treatmentPlan;
    }

    public void setTreatmentPlan(TreatmentPlan treatmentPlan) {
        this.treatmentPlan = treatmentPlan;
        pet.getMedicalHistory().addTreatmentPlan(treatmentPlan);
    }

    @Override
    public String toString() {
        return "Appointment[pet=" + pet.getName()
                + ", veterinarian=" + veterinarian.getName()
                + ", date=" + date
                + ", timeslot=" + timeslot
                + ", status=" + status + "]";
    }
}
