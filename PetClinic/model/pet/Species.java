package PetClinic.model.pet;

public enum Species {
    DOG,
    CAT,
    BIRD,
    RABBIT,
    HAMSTER,
    REPTILE,
    FISH,
    OTHER;

    @Override
    public String toString() {
        return name().charAt(0) + name().substring(1).toLowerCase();
    }
}
