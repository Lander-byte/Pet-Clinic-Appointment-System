package model.user;

public class Veterinarian extends User {
    private String specialization;

    public Veterinarian(String name, String specialization) {
        super(name);
        this.specialization = specialization;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    @Override
    public String toString() {
        return "Veterinarian[name=" + name + ", specialization=" + specialization + "]";
    }
}
