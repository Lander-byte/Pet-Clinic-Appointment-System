package model.user;

public class Veterinarian extends User {
    private String specialization;

    public Veterinarian(String name, String specialization) {
        super(name);
        this.specialization = specialization;
    }

    public Veterinarian(String name, String specialization, String username, String password) {
        super(name, "", "", "", username, password);
        this.specialization = specialization;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    @Override
    public String getRole() {
        return "Veterinarian";
    }

    @Override
    public String toString() {
        return "Veterinarian[name=" + getName() + ", specialization=" + specialization + "]";
    }
}
