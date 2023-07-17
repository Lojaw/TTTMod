package de.lojaw.module;

public interface Module {
    String getName();
    boolean isEnabled();
    void setEnabled(boolean enabled);
    void toggle();
    void setKeybind(int keybind);
    int getKeybind();
    void onEnable();
    void onDisable();
}
