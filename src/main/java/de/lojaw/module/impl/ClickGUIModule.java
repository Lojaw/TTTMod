package de.lojaw.module.impl;

import de.lojaw.gui.clickgui.ClickGUI;
import de.lojaw.module.Category;
import de.lojaw.module.Module;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class ClickGUIModule implements Module {

    private String name = "Click GUI";
    private Category category = Category.RENDER;
    private boolean isEnabled = false;
    private int key = GLFW.GLFW_KEY_B;
    private String mode = "toggle";
    private KeyBinding keyBinding;

    private boolean shouldPauseGame = false; // Standardmäßig auf false setzen

    private static ClickGUIModule instance;

    // Privater Konstruktor, um zu verhindern, dass von außerhalb dieser Klasse neue Instanzen erstellt werden
    private ClickGUIModule() {
        // Initialisieren Sie Ihre Instanzvariablen hier, wenn Sie welche haben
    }

    // Öffentliche Methode, um auf die einzige Instanz dieser Klasse zuzugreifen
    public static ClickGUIModule getInstance() {
        // Erstellen Sie die Instanz nur, wenn sie noch nicht existiert
        if (instance == null) {
            instance = new ClickGUIModule();
        }
        // Rückgabe der einzigen Instanz dieser Klasse
        return instance;
    }

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
        return ClickGUI.getInstance().isOpen();
    }

    @Override
    public void setEnabled(boolean isEnabled) {
        if (isEnabled) {
            ClickGUI.getInstance().open();
        } else {
            ClickGUI.getInstance().close();
        }
    }

    @Override
    public void onEnable() {
        // nothing, currently happens in setsetEnabled(boolean isEnabled)
    }

    @Override
    public void onDisable() {
        // nothing, currently happens in setsetEnabled(boolean isEnabled)
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
        if (ClickGUI.getInstance().isOpen()) {
            this.setEnabled(false);
        } else {
            this.setEnabled(true);
        }
    }

    public void setShouldPauseGame(boolean shouldPauseGame) {
        this.shouldPauseGame = shouldPauseGame;
    }

    public boolean getShouldPauseGame() {
        return this.shouldPauseGame;
    }

}
