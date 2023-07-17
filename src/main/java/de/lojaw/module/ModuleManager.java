package de.lojaw.module;

import de.lojaw.module.impl.SprintModule;

import java.util.HashMap;
import java.util.Map;

public class ModuleManager {
    private static ModuleManager instance;

    private final Map<String, Module> modules = new HashMap<>();

    // Privater Konstruktor, verhindert die Erstellung von Instanzen von außerhalb dieser Klasse
    private ModuleManager() {
        // Registrieren der Module
        modules.put("Sprint", new SprintModule());
    }

    public static ModuleManager getInstance() {
        if (instance == null) {
            instance = new ModuleManager();
        }
        return instance;
    }

    public void enableModule(String name) {
        Module module = getModule(name);
        if (module != null) {
            module.setEnabled(true);
        }
    }

    public void disableModule(String name) {
        Module module = getModule(name);
        if (module != null) {
            module.setEnabled(false);
        }
    }

    public void toggleModule(String name) {
        Module module = getModule(name);
        if (module != null) {
            module.toggle();
        }
    }

    public Module getModule(String name) {
        return modules.get(name);
    }

}
