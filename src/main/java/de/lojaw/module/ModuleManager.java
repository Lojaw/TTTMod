package de.lojaw.module;

import de.lojaw.event.KeyInputHandler;
import de.lojaw.gui.clickgui.ClickGUI;
import de.lojaw.module.impl.ClickGUIModule;
import de.lojaw.module.impl.FlyModule;
import de.lojaw.module.impl.FullbrightModule;
import de.lojaw.module.impl.SprintModule;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ModuleManager {
    private static ModuleManager instance;

    private final Map<String, Module> modules = new HashMap<>();

    // Privater Konstruktor, verhindert die Erstellung von Instanzen von außerhalb dieser Klasse
    private ModuleManager() {
        // Do nothing here
    }

    public void registerModulesAndKeybindings() {
        // Register modules
        SprintModule sprintModule = new SprintModule();
        modules.put("Sprint", sprintModule);

        ClickGUIModule clickGUIModule = ClickGUIModule.getInstance();
        modules.put("Click GUI", clickGUIModule);

        FlyModule flyModule = new FlyModule();
        modules.put("Fly", flyModule);

        FullbrightModule fullbrightModuleModule = new FullbrightModule();
        modules.put("Fullbright", fullbrightModuleModule);

        // Register keybindings
        if (sprintModule.getKey() != -1) {
            String keyId = "key.tttmod." + sprintModule.getName();
            KeyBinding keyBinding = new KeyBinding(
                    keyId,
                    InputUtil.Type.KEYSYM,
                    sprintModule.getKey(),
                    KeyInputHandler.KEY_CATEGORY_TTTMOD
            );
            KeyBindingHelper.registerKeyBinding(keyBinding);
            sprintModule.setKeyBinding(keyBinding);
        }

        if (clickGUIModule.getKey() != -1) {
            String keyId = "key.tttmod." + clickGUIModule.getName();
            KeyBinding keyBinding = new KeyBinding(
                    keyId,
                    InputUtil.Type.KEYSYM,
                    clickGUIModule.getKey(),
                    KeyInputHandler.KEY_CATEGORY_TTTMOD
            );
            KeyBindingHelper.registerKeyBinding(keyBinding);
            clickGUIModule.setKeyBinding(keyBinding);
        }

        if (clickGUIModule.getKey() != -1) {
            String keyId = "key.tttmod." + flyModule.getName();
            KeyBinding keyBinding = new KeyBinding(
                    keyId,
                    InputUtil.Type.KEYSYM,
                    flyModule.getKey(),
                    KeyInputHandler.KEY_CATEGORY_TTTMOD
            );
            KeyBindingHelper.registerKeyBinding(keyBinding);
            flyModule.setKeyBinding(keyBinding);
        }

        if (fullbrightModuleModule.getKey() != -1) {
            String keyId = "key.tttmod." + fullbrightModuleModule.getName();
            KeyBinding keyBinding = new KeyBinding(
                    keyId,
                    InputUtil.Type.KEYSYM,
                    fullbrightModuleModule.getKey(),
                    KeyInputHandler.KEY_CATEGORY_TTTMOD
            );
            KeyBindingHelper.registerKeyBinding(keyBinding);
            fullbrightModuleModule.setKeyBinding(keyBinding);
        }

        // Register event handlers
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.world != null) {
                for (Module module : modules.values()) {
                    KeyBinding keyBinding = module.getKeyBinding();
                    if (keyBinding != null) {
                        if (keyBinding.wasPressed()) {
                            module.handleKeyInput();
                        } else if (module.getMode().equals("hold") && !keyBinding.isPressed()) {
                            module.setEnabled(false);
                        }
                    }
                }
            }
        });



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
            ClientPlayerEntity player = MinecraftClient.getInstance().player;
            if (player != null) {
                MinecraftClient.getInstance().player.sendMessage(Text.of("[TTTMod] Das Module " + module.getName() + " wurde aktiviert"), false);
            }
        }
    }

    public void disableModule(String name) {
        Module module = getModule(name);
        if (module != null) {
            module.setEnabled(false);
            ClientPlayerEntity player = MinecraftClient.getInstance().player;
            if (player != null) {
                MinecraftClient.getInstance().player.sendMessage(Text.of("[TTTMod] Das Module " + module.getName() + " wurde deaktiviert"), false);
            }
        }
    }

    public void setModuleMode(String name, String mode) {
        Module module = getModule(name);
        if (module != null) {
            module.setMode(mode);
        }
    }

    public void updateModuleKey(String moduleName, String newKeyLetter) {
        Module module = getModule(moduleName);
        if (module != null) {
            InputUtil.Key key = InputUtil.fromTranslationKey("key.keyboard." + newKeyLetter);
            module.getKeyBinding().setBoundKey(key);
            KeyBinding.updateKeysByCode(); // Stellt sicher, dass die Änderung angewendet wird
        }
    }


    public Module getModule(String name) {
        return modules.get(name);
    }

    public Collection<Module> getAllModules() {
        return modules.values();
    }
}