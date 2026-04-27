public class Procedure {
    private String name;
    private String description;

    public Procedure(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() { return name; }
    public String getDescription() { return description; }

    @Override
    public String toString() {
        return "Procedure: " + name + " (" + description + ")";
    }
}
