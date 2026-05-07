package model.pet;

import model.clinical.TreatmentPlan;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MedicalHistory {

    private Pet pet;
    private List<String> vaccinations;
    private List<String> allergies;
    private List<TreatmentPlan> treatmentPlans;
    private Date lastVisit;
    private String notes;

    public MedicalHistory(Pet pet) {
        this.pet            = pet;
        this.vaccinations   = new ArrayList<>();
        this.allergies      = new ArrayList<>();
        this.treatmentPlans = new ArrayList<>();
        this.lastVisit      = null;
        this.notes          = "";
    }

    public Pet getPet()                             { return pet; }
    public List<String> getVaccinations()           { return vaccinations; }
    public List<String> getAllergies()              { return allergies; }
    public List<TreatmentPlan> getTreatmentPlans() { return treatmentPlans; }
    public Date getLastVisit()                      { return lastVisit; }
    public String getNotes()                        { return notes; }

    public void setLastVisit(Date lastVisit) { this.lastVisit = lastVisit; }
    public void setNotes(String notes)       { this.notes = notes; }

    public void addVaccination(String vaccination) { this.vaccinations.add(vaccination); }
    public void addAllergy(String allergy)         { this.allergies.add(allergy); }

    public void addTreatmentPlan(TreatmentPlan plan) {
        this.treatmentPlans.add(plan);
        this.lastVisit = new Date();
    }

    public void display() {
        System.out.println("===== Medical History: " + pet.getName() + " =====");
        System.out.println("Vaccinations : " + (vaccinations.isEmpty() ? "None" : vaccinations));
        System.out.println("Allergies    : " + (allergies.isEmpty()    ? "None" : allergies));
        System.out.println("Last Visit   : " + (lastVisit == null      ? "N/A"  : lastVisit));
        System.out.println("Notes        : " + (notes.isEmpty()        ? "None" : notes));
        if (!treatmentPlans.isEmpty()) {
            System.out.println("Treatment Plans:");
            for (TreatmentPlan tp : treatmentPlans) {
                System.out.println("  - " + tp);
            }
        }
    }

    @Override
    public String toString() {
        return "MedicalHistory[pet=" + pet.getName()
                + ", plans=" + treatmentPlans.size()
                + ", lastVisit=" + lastVisit + "]";
    }
}
