package model.user;

public class Owner extends User {
    private String contactNumber;

    public Owner(String name, String contactNumber) {
        super(name);
        this.contactNumber = contactNumber;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    @Override
    public String toString() {
        return "Owner[name=" + name + ", contact=" + contactNumber + "]";
    }
}
