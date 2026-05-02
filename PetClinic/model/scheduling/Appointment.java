package PetClinic.model.scheduling;

import model.pet.Pet;
import model.user.Veterinarian;
import model.clinical.TreatmentPlan;

public class Appointment {
    private pet pet;
    private Veterinarian vet;
    private Timeslot timeslot;
    private String reason;
    private String status;
    private TreatmentPlan treatmentPlan;

    public Appointment(Pet pet, Veterinarian vet, Timeslot timeslot, String reason) {
        this.pet      = pet;
        this.vet      = vet;
        this.timeslot = timeslot;
        this.reason   = reason;
        this.status   = "SCHEDULED";
        timeslot.setAvailable(false);
    }

    public pet getPet()                        { return pet; }
    public Veterinarian getVet()               { return vet; }
    public Timeslot getTimeslot()              { return timeslot; }
    public String getReason()                  { return reason; }
    public String getStatus()                  { return status; }
    public TreatmentPlan getTreatmentPlan()    { return treatmentPlan; }

    public void setTreatmentPlan(TreatmentPlan treatmentPlan) {
        this.treatmentPlan = treatmentPlan;
        this.status = "COMPLETED";
    }

    @Override
    public String toString() {
        return "[Appointment] " + pet.getName()
                + " with " + vet.getName()
                + " @ " + timeslot
                + " | Reason: " + reason
                + " | Status: " + status;
    }
}