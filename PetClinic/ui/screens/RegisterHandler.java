package PetClinic.ui.screens;

import PetClinic.ui.components.FloatingInput;

@FunctionalInterface
public interface RegisterHandler {
    void register(FloatingInput username, FloatingInput email, FloatingInput password, FloatingInput confirm);
}
