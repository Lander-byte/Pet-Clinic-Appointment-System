package service;

import model.pet.Pet;
import model.scheduling.Appointment;
import java.util.ArrayList;
import java.util.List;

public class PetClinic {
    private String clinicName;
    private List<Pet> registeredPets;
    private List<Appointment> appointments;

    public PetClinic(String clinicName) {
        this.clinicName = clinicName;
        this.registeredPets = new ArrayList<>();
        this.appointments = new ArrayList<>();
    }

    public void registerPet(Pet pet) {
        registeredPets.add(pet);
    }

    public void addAppointment(Appointment appointment) {
        appointments.add(appointment);
    }

    public List<Pet> getRegisteredPets() {
        return registeredPets;
    }

    public List<Appointment> getAppointments() {
        return appointments;
    }

    public String getClinicName() {
        return clinicName;
    }

    @Override
    public String toString() {
        return "PetClinic[name=" + clinicName
                + ", pets=" + registeredPets.size()
                + ", appointments=" + appointments.size() + "]";
    }
}