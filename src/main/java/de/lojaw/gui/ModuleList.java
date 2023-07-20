package de.lojaw.gui;

import de.lojaw.module.Module;
import de.lojaw.module.ModuleManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

public class ModuleList extends DrawableHelper {

    private final TextRenderer textRenderer;
    private final ModuleManager moduleManager;

    public ModuleList(TextRenderer textRenderer, ModuleManager moduleManager) {
        this.textRenderer = textRenderer;
        this.moduleManager = moduleManager;
    }

    public void render(MatrixStack matrices) {
        // Ändere Collection<Module> zu List<Module> und verwende getActiveModules() anstatt getAllModules()
        List<Module> modules = new ArrayList<>(moduleManager.getActiveModules());

        // Sortiere Module nach Länge des Namens
        modules.sort(Comparator.comparingInt((Module m) -> m.getName().length()).reversed());

        // Aktuelle Bildschirmbreite ermitteln
        int screenWidth = MinecraftClient.getInstance().getWindow().getScaledWidth();

        // Zeichne jedes Modul auf den Bildschirm
        int y = 2; // Beginne etwas von der Oberkante entfernt
        for (Module module : modules) {
            int color = 0xFFFFFF; // Weiß
            // Zeichne an der rechten Seite des Bildschirms mit einem Rand von 10 Pixeln
            drawStringWithShadow(matrices, this.textRenderer, module.getName(), screenWidth - 2 - this.textRenderer.getWidth(module.getName()), y, color, 1.0f);
            y += 15; // Erhöhe y für das nächste Modul
        }
    }

    public void drawStringWithShadow(MatrixStack matrices, TextRenderer textRenderer, String text, int x, int y, int color, float scale) {
        Text messageText = Text.of(text);
        Style style = messageText.getStyle().withColor(TextColor.fromRgb(color));
        messageText = messageText.copy().setStyle(style);

        matrices.push();
        matrices.scale(scale, scale, 1.0f);

        textRenderer.draw(matrices, messageText, x / scale, y / scale, color);

        matrices.pop();
    }

}


