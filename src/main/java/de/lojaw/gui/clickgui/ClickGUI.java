package de.lojaw.gui.clickgui;

import com.mojang.blaze3d.systems.RenderSystem;
import de.lojaw.module.Category;
import de.lojaw.module.Module;
import de.lojaw.module.ModuleManager;
import de.lojaw.module.impl.ClickGUIModule;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.util.Identifier;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClickGUI extends Screen {
    private static ClickGUI instance;


    private boolean isOpen = false;
    private final Map<Category, Boolean> categoryStates = new HashMap<>();

    // Schritt 1: Felder für die Positionen der Kategorien
    private final Map<Category, Point> categoryPositions = new HashMap<>();

    // Schritt 2: Felder für das Ziehen der Kategorien
    private Category draggingCategory = null;
    private Point clickOffset = null;

    private ClickGUI() {
        super(Text.of("Click GUI")); /* Titel der GUI, normalerweise ein Text Objekt */


        // Initialisiere die Positionen der Kategorien
        int x = 15;
        int y = 10;
        // Setzt alle Kategorien beim Start auf geöffnet
        for (Category category : Category.values()) {
            categoryStates.put(category, true);
            categoryPositions.put(category, new Point(x, y));
            y += 35;  // Ändern Sie dies, um den Abstand zwischen den Kategorien anzupassen
        }

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
        int x = 15;  // Setze die anfängliche x-Position
        int y = 10;  // Setze die anfängliche y-Position

        Map<Category, List<Module>> modulesByCategory = ModuleManager.getInstance().getModulesByCategory();
        for (Category category : modulesByCategory.keySet()) {
            Point pos = categoryPositions.get(category);  // Hole die Position der Kategorie

            // Zeichne Rahmen um die Kategorie
            drawRect(matrices, x - 5, y - 5, 150, 20, 0x80808080);

            // Zeichne Kategoriename hervorgehoben
            drawStringWithShadow(matrices, this.textRenderer, category.toString(), x, y, 0xFFFFFF, 1.5f);
            y += 20;

            if (!isCategoryOpen(category)) continue;

            for (Module module : modulesByCategory.get(category)) {
                drawStringWithShadow(matrices, this.textRenderer, module.getName(), x, y, 0xFFFFFF, 1.0f);
                y += 15;
            }

            y += 15;  // Füge zusätzlichen Abstand zwischen den Kategorien hinzu
        }

        // Zeichne den "Reset Positions"-Button
        int resetButtonX = this.width - 150;  // 150 ist die Breite des Buttons
        int resetButtonY = this.height - 20;  // 20 ist die Höhe des Buttons
        drawStringWithShadow(matrices, this.textRenderer, "Reset Positions", resetButtonX, resetButtonY, 0xFFFFFF, 1.0f);

        super.render(matrices, mouseX, mouseY, delta);
    }

    public void drawRect(MatrixStack matrices, int x, int y, int width, int height, int color) {
        // Zeichne ein Rechteck mit der angegebenen Position, Größe und Farbe
        RenderSystem.setShader(GameRenderer::getPositionColorProgram); // Diese Zeile wurde geändert
        Tessellator tesselator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tesselator.getBuffer();
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        bufferBuilder.vertex(x, y + height, 0.0D).color(color, color, color, 255).next();
        bufferBuilder.vertex(x + width, y + height, 0.0D).color(color, color, color, 255).next();
        bufferBuilder.vertex(x + width, y, 0.0D).color(color, color, color, 255).next();
        bufferBuilder.vertex(x, y, 0.0D).color(color, color, color, 255).next();
        tesselator.draw();
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


    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int x = 15;  // Anfangsposition
        int y = 10;  // Anfangsposition

        Map<Category, List<Module>> modulesByCategory = ModuleManager.getInstance().getModulesByCategory();
        for (Category category : modulesByCategory.keySet()) {
            Point pos = categoryPositions.get(category);

            // Prüft, ob der Klick innerhalb des Bereichs der Kategorie liegt
            if (mouseX >= x - 5 && mouseX <= x + 150 && mouseY >= y - 5 && mouseY <= y + 15) {
                draggingCategory = category;
                clickOffset = new Point((int)mouseX - pos.x, (int)mouseY - pos.y);
                setCategoryOpen(category, !isCategoryOpen(category));
                return true;
            }
            y += 20;
            if (isCategoryOpen(category)) {
                y += 15 * modulesByCategory.get(category).size() + 15;
            }
        }

        // Prüfen, ob der Klick innerhalb des Bereichs des "Reset Positions"-Buttons liegt
        int resetButtonX = this.width - 150;
        int resetButtonY = this.height - 20;
        int resetButtonWidth = 150;
        int resetButtonHeight = 20;
        if (mouseX >= resetButtonX && mouseX <= resetButtonX + resetButtonWidth && mouseY >= resetButtonY && mouseY <= resetButtonY + resetButtonHeight) {
            resetPositions();
            return true;
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        draggingCategory = null;
        return super.mouseReleased(mouseX, mouseY, button);
    }

    private void resetPositions() {
        int x = 15;
        int y = 10;
        for (Category category : categoryPositions.keySet()) {
            Point pos = categoryPositions.get(category);
            pos.x = x;
            pos.y = y;
            y += 35;  // Ändern Sie dies, um den Abstand zwischen den Kategorien anzupassen
        }
    }


    private int delayCounter;

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

    // Methoden um den Zustand einer Kategorie zu prüfen und zu ändern
    public boolean isCategoryOpen(Category category) {
        return categoryStates.get(category);
    }

    public void setCategoryOpen(Category category, boolean open) {
        categoryStates.put(category, open);
    }

}
