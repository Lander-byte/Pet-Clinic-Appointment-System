package model.user;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UserAccountStore {
    private final List<Owner> customers = new ArrayList<>();
    private final List<User> admins = new ArrayList<>();

    public UserAccountStore() {
        admins.add(new Staff("Front Desk Staff", "Staff", "staff", "staff123"));
        admins.add(new Veterinarian("Clinic Veterinarian", "General Practice", "vet", "vet123"));
    }

    public Owner registerCustomer(String username, String email, String password) {
        String cleanUsername = clean(username);
        String cleanEmail = clean(email);
        String cleanPassword = password == null ? "" : password;

        if (cleanUsername.isEmpty()) {
            throw new IllegalArgumentException("Username is required.");
        }
        if (cleanEmail.isEmpty()) {
            throw new IllegalArgumentException("Email is required.");
        }
        if (cleanPassword.isEmpty()) {
            throw new IllegalArgumentException("Password is required.");
        }
        if (findByUsername(customers, cleanUsername) != null) {
            throw new IllegalArgumentException("Username is already registered.");
        }

        Owner owner = new Owner(cleanUsername, cleanEmail, "", "", cleanUsername, cleanPassword);
        customers.add(owner);
        return owner;
    }

    public Owner authenticateCustomer(String username, String password) {
        User user = findByUsername(customers, clean(username));
        if (user instanceof Owner && user.passwordMatches(password)) {
            return (Owner) user;
        }
        return null;
    }

    public User authenticateAdmin(String username, String password) {
        User user = findByUsername(admins, clean(username));
        if (user != null && user.passwordMatches(password)) {
            return user;
        }
        return null;
    }

    public List<Owner> getCustomers() {
        return Collections.unmodifiableList(customers);
    }

    public List<User> getAdmins() {
        return Collections.unmodifiableList(admins);
    }

    private User findByUsername(List<? extends User> users, String username) {
        for (User user : users) {
            if (user.hasUsername(username)) {
                return user;
            }
        }
        return null;
    }

    private String clean(String value) {
        return value == null ? "" : value.trim();
    }
}
