package model.pet;

public enum Species {
    DOG,
    CAT,
    AQUATIC,    // fish, turtles, frogs, etc.
    MAMMAL,     // rabbits, hamsters, guinea pigs, etc.
    BIRD,       // parrots, ducks, chickens, etc.
    FARM_ANIMAL; // goats, sheep, horses, pigs, etc.

    @Override
    public String toString() {
        String name = name().replace("_", " ");
        return name.charAt(0) + name.substring(1).toLowerCase();
    }
}
