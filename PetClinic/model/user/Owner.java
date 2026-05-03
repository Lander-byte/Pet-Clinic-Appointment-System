package model.user;

public class Owner extends User {
    private String contactNumber;

    public Owner(String name, String contactNumber) {
        super(name);
        this.contactNumber = contactNumber;
    }

    public Owner(String name, String email, String phone, String address, String username, String password) {
        super(name, email, phone, address, username, password);
        this.contactNumber = phone;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    @Override
    public String getRole() {
        return "Owner";
    }

    @Override
    public String toString() {
        return "Owner[name=" + getName() + ", contact=" + contactNumber + "]";
    }
}
