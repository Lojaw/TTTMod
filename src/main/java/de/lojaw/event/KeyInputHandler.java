package de.lojaw.event;

import de.lojaw.module.Module;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;

import java.util.HashMap;
import java.util.Map;

public class KeyInputHandler {
    public static final String KEY_CATEGORY_TUTORIAL = "key.tttmod.ttt";

    private static final Map<KeyBinding, Module> keyBindings = new HashMap<>();

    public static void registerKeyInputs() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            for (Map.Entry<KeyBinding, Module> entry : keyBindings.entrySet()) {
                KeyBinding keyBinding = entry.getKey();
                Module module = entry.getValue();

                if(keyBinding.wasPressed()) {
                    module.setEnabled(!module.isEnabled());
                }

                if(module.getHoldKey() == keyBinding.getDefaultKey().getCode() && !keyBinding.isPressed()) {
                    module.setEnabled(false);
                }
            }
        });
    }

    public static void register(Module module, int key) {
        KeyBinding keyBinding = new KeyBinding(
                "key.tttmod." + key,  // Du könntest hier eine bessere Beschreibung verwenden
                InputUtil.Type.KEYSYM,
                key,
                KEY_CATEGORY_TUTORIAL
        );
        KeyBindingHelper.registerKeyBinding(keyBinding);

        keyBindings.put(keyBinding, module);
    }
}