package de.lojaw.module.impl;

import de.lojaw.module.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;

public class SprintModule implements Module {
    private final String name = "Sprint";
    private boolean enabled = false;
    private int keybind;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        if (enabled) {
            onEnable();
        } else {
            onDisable();
        }
    }

    @Override
    public void toggle() {
        enabled = !enabled;
    }

    @Override
    public void setKeybind(int keybind) {
        this.keybind = keybind;
    }

    @Override
    public int getKeybind() {
        return keybind;
    }

    @Override
    public void onEnable() {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (player != null) {
            player.setSprinting(true);
        }
    }

    @Override
    public void onDisable() {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (player != null) {
            player.setSprinting(false);
        }
    }
}

