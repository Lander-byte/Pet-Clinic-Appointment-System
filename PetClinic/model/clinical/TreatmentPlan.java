package PetClinic.model.clinical;

public class TreatmentPlan {
    private Diagnosis diagnosis;
    private List<Medication> medications;
    private List<Procedure> procedures;

    public TreatmentPlan(Diagnosis diagnosis) {
        this.diagnosis = diagnosis;
        this.medications = new ArrayList<>();
        this.procedures = new ArrayList<>();
    }

    public void addMedication(Medication medication) {
        medications.add(medication);
    }

    public void addProcedure(Procedure procedure) {
        procedures.add(procedure);
    }

    public Diagnosis getDiagnosis() { return diagnosis; }
    public List<Medication> getMedications() { return medications; }
    public List<Procedure> getProcedures() { return procedures; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(diagnosis).append("\n");
        sb.append("Medications:\n");
        for (Medication m : medications) {
            sb.append(" - ").append(m).append("\n");
        }
        sb.append("Procedures:\n");
        for (Procedure p : procedures) {
            sb.append(" - ").append(p).append("\n");
        }
        return sb.toString();
    }
}
