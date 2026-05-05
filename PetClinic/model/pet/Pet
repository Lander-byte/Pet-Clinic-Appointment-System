package model.pet;

import model.user.Owner;

public class Pet {

    private static int idCounter = 1;

    private int petId;
    private String name;
    private Species species;
    private String breed;
    private int age;
    private double weight;
    private String gender;
    private Owner owner;


    public Pet(String name, Species species, Owner owner) {
        this.petId   = idCounter++;
        this.name    = name;
        this.species = species;
        this.owner   = owner;
        this.breed   = "Unknown";
        this.age     = 0;
        this.weight  = 0.0;
        this.gender  = "Unknown";
    }

    // Full constructor
    public Pet(String name, Species species, Owner owner,
               String breed, int age, double weight, String gender) {
        this.petId   = idCounter++;
        this.name    = name;
        this.species = species;
        this.owner   = owner;
        this.breed   = breed;
        this.age     = age;
        this.weight  = weight;
        this.gender  = gender;
    }

    // Getters
    public int getPetId()        { return petId; }
    public String getName()      { return name; }
    public Species getSpecies()  { return species; }
    public String getBreed()     { return breed; }
    public int getAge()          { return age; }
    public double getWeight()    { return weight; }
    public String getGender()    { return gender; }
    public Owner getOwner()      { return owner; }

    // Setters
    public void setName(String name)         { this.name = name; }
    public void setSpecies(Species species)  { this.species = species; }
    public void setBreed(String breed)       { this.breed = breed; }
    public void setAge(int age)              { this.age = age; }
    public void setWeight(double weight)     { this.weight = weight; }
    public void setGender(String gender)     { this.gender = gender; }
    public void setOwner(Owner owner)        { this.owner = owner; }

    public void displayInfo() {
        System.out.println("-----------------------------");
        System.out.println("Pet ID   : " + petId);
        System.out.println("Name     : " + name);
        System.out.println("Species  : " + species);
        System.out.println("Breed    : " + breed);
        System.out.println("Age      : " + age + " yr(s)");
        System.out.println("Weight   : " + weight + " kg");
        System.out.println("Gender   : " + gender);
        System.out.println("Owner    : " + (owner != null ? owner.getName() : "N/A"));
    }

    @Override
    public String toString() {
        return "Pet[id=" + petId + ", name=" + name + ", species=" + species
                + ", owner=" + (owner != null ? owner.getName() : "N/A") + "]";
    }
}
