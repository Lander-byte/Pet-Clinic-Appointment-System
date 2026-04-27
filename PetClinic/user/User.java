package model.user;

public abstract class User {
    private static int idCounter = 1000;

    private final int userId;
    private String name;
    private String email;
    private String phone;
    private String address;

    public User(String name, String email, String phone, String address) {
        this.userId  = idCounter++;
        this.name    = name;
        this.email   = email;
        this.phone   = phone;
        this.address = address;
    }

    public abstract String getRole();

    public void displayInfo() {
        System.out.println("-----------------------------");
        System.out.println("Role    : " + getRole());
        System.out.println("ID      : " + userId);
        System.out.println("Name    : " + name);
        System.out.println("Email   : " + email);
        System.out.println("Phone   : " + phone);
        System.out.println("Address : " + address);
    }

    public int getUserId()           { return userId; }
    public String getName()          { return name; }
    public String getEmail()         { return email; }
    public String getPhone()         { return phone; }
    public String getAddress()       { return address; }
    public void setName(String n)    { this.name = n; }
    public void setEmail(String e)   { this.email = e; }
    public void setPhone(String p)   { this.phone = p; }
    public void setAddress(String a) { this.address = a; }

}
