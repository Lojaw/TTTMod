package de.lojaw.gui.clickgui;

import de.lojaw.module.Module;
import de.lojaw.module.ModuleManager;
import de.lojaw.module.impl.ClickGUIModule;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;

public class ClickGUI extends Screen {
    private static ClickGUI instance;


    private boolean isOpen = false;

    private ClickGUI() {
        super(Text.of("Click GUI")); /* Titel der GUI, normalerweise ein Text Objekt */
    }

    public static ClickGUI getInstance() {
        if (instance == null) {
            instance = new ClickGUI();
        }
        return instance;
    }

    public void open() {
        this.isOpen = true;
        // Code, um die GUI tatsächlich in Minecraft zu öffnen
        MinecraftClient.getInstance().setScreen(this); // öffnet das GUI in Minecraft
    }

    public void close() {
        this.isOpen = false;
        // Code, um die GUI tatsächlich in Minecraft zu schließen
        MinecraftClient.getInstance().setScreen(null); // schließt das aktuelle GUI in Minecraft
    }

    public boolean isOpen() {
        return this.isOpen;
    }

    @Override
    public boolean shouldPause() {
        // Verwenden Sie die Eigenschaft aus ClickGUIModule
        return ClickGUIModule.getInstance().getShouldPauseGame();
    }


    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        // Zeichne den Hintergrund
        this.renderBackground(matrices);

        // Zeichne die Modulkategorien und Module
        int y = 10;  // Setze die anfängliche y-Position
        for (Module module : ModuleManager.getInstance().getAllModules()) {
            drawStringWithShadow(matrices, this.textRenderer, module.getName(), 10, y, 0xFFFFFF);
            y += 10;
        }

        super.render(matrices, mouseX, mouseY, delta);
    }

    public void drawStringWithShadow(MatrixStack matrices, TextRenderer textRenderer, String text, int x, int y, int color) {
        Text messageText = Text.of(text);
        Style style = messageText.getStyle().withColor(TextColor.fromRgb(color));
        messageText = messageText.copy().setStyle(style);
        textRenderer.draw(matrices, messageText, x, y, color);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (!this.shouldPause()) {
            assert this.client != null;
            if (this.client.player != null) {
                KeyBinding forwardKey = MinecraftClient.getInstance().options.forwardKey;
                KeyBinding backKey = MinecraftClient.getInstance().options.backKey;
                KeyBinding leftKey = MinecraftClient.getInstance().options.leftKey;
                KeyBinding rightKey = MinecraftClient.getInstance().options.rightKey;
                KeyBinding jumpKey = MinecraftClient.getInstance().options.jumpKey;

                if (forwardKey.matchesKey(keyCode, scanCode)) {
                    forwardKey.setPressed(true);
                } else if (backKey.matchesKey(keyCode, scanCode)) {
                    backKey.setPressed(true);
                } else if (leftKey.matchesKey(keyCode, scanCode)) {
                    leftKey.setPressed(true);
                } else if (rightKey.matchesKey(keyCode, scanCode)) {
                    rightKey.setPressed(true);
                } else if (jumpKey.matchesKey(keyCode, scanCode)) {
                    jumpKey.setPressed(true);
                    jumpKey.setPressed(false);
                }
            }
        }

        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        if (!this.shouldPause()) {
            assert this.client != null;
            if (this.client.player != null) {
                KeyBinding forwardKey = MinecraftClient.getInstance().options.forwardKey;
                KeyBinding backKey = MinecraftClient.getInstance().options.backKey;
                KeyBinding leftKey = MinecraftClient.getInstance().options.leftKey;
                KeyBinding rightKey = MinecraftClient.getInstance().options.rightKey;

                if (forwardKey.matchesKey(keyCode, scanCode) || backKey.matchesKey(keyCode, scanCode)) {
                    forwardKey.setPressed(false);
                    backKey.setPressed(false);
                } else if (leftKey.matchesKey(keyCode, scanCode) || rightKey.matchesKey(keyCode, scanCode)) {
                    leftKey.setPressed(false);
                    rightKey.setPressed(false);
                }
            }
        }

        return super.keyReleased(keyCode, scanCode, modifiers);
    }

}
