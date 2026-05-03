package model.user;

public class Staff extends User {
    private String role;

    public Staff(String name, String role) {
        super(name);
        this.role = role;
    }

    public Staff(String name, String role, String username, String password) {
        super(name, "", "", "", username, password);
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
        return "Staff[name=" + getName() + ", role=" + role + "]";
    }
}
