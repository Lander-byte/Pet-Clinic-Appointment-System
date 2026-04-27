public class Medication {
    private String name;
    private String dosage;   
    private int durationDays;

    public Medication(String name, String dosage, int durationDays) {
        this.name = name;
        this.dosage = dosage;
        this.durationDays = durationDays;
    }

    public String getName() { return name; }
    public String getDosage() { return dosage; }
    public int getDurationDays() { return durationDays; }

    @Override
    public String toString() {
        return "Medication: " + name + " | Dosage: " + dosage +
               " | Duration: " + durationDays + " days";
    }
}
