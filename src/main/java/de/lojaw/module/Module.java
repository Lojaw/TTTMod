package de.lojaw.module;

public interface Module {
    String getName();
    boolean isEnabled();
    void setEnabled(boolean isEnabled);
    void onEnable();
    void onDisable();
    void setToggleKey(int keybind); // Umschalten (ein/aus)
    int getToggleKey();
    void setHoldKey(int keybind); // Nur solange aktiv, wenn die Taste gedrückt wird
    int getHoldKey();
}
