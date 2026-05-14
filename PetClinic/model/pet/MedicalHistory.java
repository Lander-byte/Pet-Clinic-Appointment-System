package PetClinic.model.pet;

import PetClinic.model.clinical.TreatmentPlan;

import java.util.ArrayList;
import java.util.List;

public class MedicalHistory {
    private final Pet pet;
    private final List<String> notes;
    private final List<TreatmentPlan> treatmentPlans;

    public MedicalHistory(Pet pet) {
        this.pet = pet;
        this.notes = new ArrayList<>();
        this.treatmentPlans = new ArrayList<>();
    }

    public Pet getPet() {
        return pet;
    }

    public void addNote(String note) {
        notes.add(note);
    }

    public List<String> getNotes() {
        return notes;
    }

    public void addTreatmentPlan(TreatmentPlan treatmentPlan) {
        treatmentPlans.add(treatmentPlan);
    }

    public List<TreatmentPlan> getTreatmentPlans() {
        return treatmentPlans;
    }

    @Override
    public String toString() {
        return "MedicalHistory[pet=" + pet.getName()
                + ", notes=" + notes.size()
                + ", treatmentPlans=" + treatmentPlans.size() + "]";
    }
}
