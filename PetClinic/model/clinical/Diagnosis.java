package PetClinic.model.clinical;

public class Diagnosis {
    private String condition;
    private String notes;

    public Diagnosis(String condition, String notes) {
        this.condition = condition;
        this.notes = notes;
    }

    public String getCondition() { return condition; }
    public String getNotes() { return notes; }

    @Override
    public String toString() {
        return "PetClinic.model.clinical.Diagnosis: " + condition + " (" + notes + ")";
    }
}
