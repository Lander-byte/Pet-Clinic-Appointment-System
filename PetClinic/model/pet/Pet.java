package model.pet;

import model.user.Owner;

public class Pet {
    private String name;
    private Species species;
    private Owner owner;
    private MedicalHistory medicalHistory;

    public Pet(String name, Species species, Owner owner) {
        this.name = name;
        this.species = species;
        this.owner = owner;
        this.medicalHistory = new MedicalHistory(this);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Species getSpecies() {
        return species;
    }

    public void setSpecies(Species species) {
        this.species = species;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public MedicalHistory getMedicalHistory() {
        return medicalHistory;
    }

    @Override
    public String toString() {
        return "Pet[name=" + name + ", species=" + species + ", owner=" + owner.getName() + "]";
    }
}
