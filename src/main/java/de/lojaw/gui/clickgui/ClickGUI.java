package de.lojaw.gui.clickgui;

import de.lojaw.module.Module;
import de.lojaw.module.ModuleManager;
import de.lojaw.module.impl.ClickGUIModule;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.Screen;
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
}
