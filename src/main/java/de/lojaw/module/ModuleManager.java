package de.lojaw.module;

import de.lojaw.gui.tabgui.TabGUI;
import de.lojaw.module.impl.*;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;

import java.util.*;

public class ModuleManager {
    public static final String KEY_CATEGORY_TTTMOD = "key.category.tttmod.modules";

    private static ModuleManager instance;

    private final Map<String, Module> modules = new HashMap<>();
    private TabGUI tabGUI;

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

        NoFallModule noFallModule = new NoFallModule();
        modules.put("NoFall", noFallModule);

        ModuleListModule moduleListModule = new ModuleListModule();
        modules.put("ModuleList", moduleListModule);

        TabGUIModule tabGUIModule = new TabGUIModule();
        modules.put("TabGUI", tabGUIModule);

        JesusModule jesusModule = new JesusModule();
        modules.put("Jesus", jesusModule);

        // Register keybindings
        if (sprintModule.getKey() != -1) {
            String keyId = "key.tttmod." + sprintModule.getName();
            KeyBinding keyBinding = new KeyBinding(
                    keyId,
                    InputUtil.Type.KEYSYM,
                    sprintModule.getKey(),
                    KEY_CATEGORY_TTTMOD
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
                    KEY_CATEGORY_TTTMOD
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
                    KEY_CATEGORY_TTTMOD
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
                    KEY_CATEGORY_TTTMOD
            );
            KeyBindingHelper.registerKeyBinding(keyBinding);
            fullbrightModuleModule.setKeyBinding(keyBinding);
        }

        if (noFallModule.getKey() != -1) {
            String keyId = "key.tttmod." + noFallModule.getName();
            KeyBinding keyBinding = new KeyBinding(
                    keyId,
                    InputUtil.Type.KEYSYM,
                    noFallModule.getKey(),
                    KEY_CATEGORY_TTTMOD
            );
            KeyBindingHelper.registerKeyBinding(keyBinding);
            noFallModule.setKeyBinding(keyBinding);
        }

        if (moduleListModule.getKey() != -1) {
            String keyId = "key.tttmod." + moduleListModule.getName();
            KeyBinding keyBinding = new KeyBinding(
                    keyId,
                    InputUtil.Type.KEYSYM,
                    moduleListModule.getKey(),
                    KEY_CATEGORY_TTTMOD
            );
            KeyBindingHelper.registerKeyBinding(keyBinding);
            moduleListModule.setKeyBinding(keyBinding);
        }

        if (tabGUIModule.getKey() != -1) {
            String keyId = "key.tttmod." + tabGUIModule.getName();
            KeyBinding keyBinding = new KeyBinding(
                    keyId,
                    InputUtil.Type.KEYSYM,
                    tabGUIModule.getKey(),
                    KEY_CATEGORY_TTTMOD
            );
            KeyBindingHelper.registerKeyBinding(keyBinding);
            tabGUIModule.setKeyBinding(keyBinding);
        }

        if (jesusModule.getKey() != -1) {
            String keyId = "key.tttmod." + jesusModule.getName();
            KeyBinding keyBinding = new KeyBinding(
                    keyId,
                    InputUtil.Type.KEYSYM,
                    jesusModule.getKey(),
                    KEY_CATEGORY_TTTMOD
            );
            KeyBindingHelper.registerKeyBinding(keyBinding);
            jesusModule.setKeyBinding(keyBinding);
        }

        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        this.tabGUI = new TabGUI(textRenderer);

        // Register event handlers
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            long windowHandle = MinecraftClient.getInstance().getWindow().getHandle();

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

                // Check if the TabGUI module is enabled
                if (tabGUIModule.isEnabled()) {
                    if (InputUtil.isKeyPressed(windowHandle, GLFW.GLFW_KEY_UP)) {
                        this.tabGUI.onKeyPressed(GLFW.GLFW_KEY_UP);  // handle key up
                    } else if (InputUtil.isKeyPressed(windowHandle, GLFW.GLFW_KEY_DOWN)) {
                        this.tabGUI.onKeyPressed(GLFW.GLFW_KEY_DOWN);  // handle key down
                    } else if (InputUtil.isKeyPressed(windowHandle, GLFW.GLFW_KEY_RIGHT)) {
                        this.tabGUI.onKeyPressed(GLFW.GLFW_KEY_RIGHT);  // handle key right
                    } else if (InputUtil.isKeyPressed(windowHandle, GLFW.GLFW_KEY_LEFT)) {
                        this.tabGUI.onKeyPressed(GLFW.GLFW_KEY_LEFT);  // handle key left
                    } else if(InputUtil.isKeyPressed(windowHandle, GLFW.GLFW_KEY_ENTER)) {
                        this.tabGUI.onKeyPressed(GLFW.GLFW_KEY_ENTER);  // handle key enter
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

    public Map<Category, List<Module>> getModulesByCategory() {
        Map<Category, List<Module>> map = new HashMap<>();

        for (Module module : getAllModules()) {
            if (!map.containsKey(module.getCategory())) {
                map.put(module.getCategory(), new ArrayList<>());
            }

            map.get(module.getCategory()).add(module);
        }

        return map;
    }

    public Collection<Module> getActiveModules() {
        List<Module> activeModules = new ArrayList<>();
        for (Module module : modules.values()) {
            if (module.isEnabled()) {
                activeModules.add(module);
            }
        }
        return activeModules;
    }


    public Collection<Module> getAllModules() {
        return modules.values();
    }
}