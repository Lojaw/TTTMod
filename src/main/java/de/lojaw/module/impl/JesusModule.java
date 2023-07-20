package de.lojaw.module.impl;

import de.lojaw.module.Category;
import de.lojaw.module.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class JesusModule implements Module {

    private String name = "Jesus";
    private Category category = Category.MOVEMENT;
    private boolean isEnabled = false;
    private int key = GLFW.GLFW_KEY_H;
    private String mode = "toggle";
    private KeyBinding keyBinding;

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
        if (player != null) {
            player.sendAbilitiesUpdate();
            player.sendMessage(Text.of("[TTTMod] Das Jesus Modul wurde aktiviert"));
        }
    }

    @Override
    public void onDisable() {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (player != null) {
            player.sendAbilitiesUpdate();
            player.sendMessage(Text.of("[TTTMod] Das Jesus Modul wurde deaktiviert"));
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
