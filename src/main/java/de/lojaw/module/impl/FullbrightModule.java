package de.lojaw.module.impl;

import de.lojaw.module.Category;
import de.lojaw.module.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class FullbrightModule implements Module {

    private String name = "Fullbright";
    private Category category = Category.RENDER;
    private boolean isEnabled = false;
    private int key = GLFW.GLFW_KEY_J;
    private String mode = "toggle";
    private KeyBinding keyBinding;
    private double oldGamma;

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Category getCategory() {
        return this.category;
    }

    @Override
    public boolean isEnabled() {
        return this.isEnabled;
    }

    @Override
    public void setEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
        if (isEnabled) {
            this.onEnable();
        } else {
            this.onDisable();
        }
    }

    @Override
    public void onEnable() {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        GameOptions gameOptions = MinecraftClient.getInstance().options;
        oldGamma = gameOptions.getGamma().getValue();
        if (player != null) {
            gameOptions.getGamma().setValue(10.0); // Setze die Gamma-Einstellung auf einen hohen Wert (10.0)
            player.sendMessage(Text.of("[TTTMod] Das Fullbright Modul wurde aktiviert"));
        }
    }

    @Override
    public void onDisable() {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        GameOptions gameOptions = MinecraftClient.getInstance().options;
        if (player != null) {
            gameOptions.getGamma().setValue(oldGamma); // Setze die Gamma-Einstellung auf einen hohen Wert (10.0)
            player.sendMessage(Text.of("[TTTMod] Das Fullbright Modul wurde deaktiviert"));
        }
    }

    @Override
    public void setKey(int key) {
        this.key = key;
        if (keyBinding != null) {
            keyBinding.setBoundKey(InputUtil.Type.KEYSYM.createFromCode(key));
            KeyBinding.updateKeysByCode(); // Stellt sicher, dass die Änderung angewendet wird
        }
    }

    @Override
    public int getKey() {
        return this.key;
    }

    @Override
    public void setKeyBinding(KeyBinding keyBinding) {
        this.keyBinding = keyBinding;
    }

    @Override
    public KeyBinding getKeyBinding() {
        return this.keyBinding;
    }

    @Override
    public void setMode(String mode) {
        this.mode = mode;
    }

    @Override
    public String getMode() {
        return this.mode;
    }

    @Override
    public void handleKeyInput() {
        switch (mode) {
            case "toggle":
                this.setEnabled(!this.isEnabled());
                break;
            case "hold":
                this.setEnabled(true);
                break;
        }
    }
}
