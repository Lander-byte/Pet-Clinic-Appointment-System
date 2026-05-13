package model.user;

public class Owner extends User {
    public Owner(String name, String email, String phone, String address, String username, String password) {
        super(name, email, phone, address, username, password);
    }

    @Override
    public String getRole() {
        return "Owner";
    }

    @Override
    public String toString() {
        return "Owner[name=" + getName() + ", contact=" + getPhone() + "]";
    }
}
