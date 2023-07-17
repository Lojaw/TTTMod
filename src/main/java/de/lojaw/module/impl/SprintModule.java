package de.lojaw.module.impl;

import de.lojaw.module.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;

public class SprintModule implements Module {

    private String name;
    private boolean isEnabled = false;
    private int toggleKey = -1; // -1 bedeutet nicht gesetzt
    private int holdKey = -1; // -1 bedeutet nicht gesetzt

    @Override
    public String getName() {
        return this.name;
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

    @Override
    public void setToggleKey(int keybind) {
        this.toggleKey = keybind;
    }

    @Override
    public int getToggleKey() {
        return toggleKey;
    }

    @Override
    public void setHoldKey(int keybind) {
        this.holdKey = keybind;
    }

    @Override
    public int getHoldKey() {
        return holdKey;
    }
}

