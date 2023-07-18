package de.lojaw.module;

import net.minecraft.client.option.KeyBinding;

public interface Module {
    String getName();
    Category getCategory();
    boolean isEnabled();
    void setEnabled(boolean isEnabled);
    void onEnable();
    void onDisable();
    void setKey(int key);
    int getKey();
    void setKeyBinding(KeyBinding keyBinding);
    KeyBinding getKeyBinding();
    void setMode(String mode);
    String getMode();
    void handleKeyInput();
}
