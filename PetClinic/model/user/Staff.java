package model.user;

public class Staff extends User {
    private String role;

    public Staff(String name, String role) {
        super(name);
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "Staff[name=" + name + ", role=" + role + "]";
    }
}
