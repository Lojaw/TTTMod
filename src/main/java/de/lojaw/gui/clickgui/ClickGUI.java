package de.lojaw.gui.clickgui;

import com.mojang.blaze3d.systems.RenderSystem;
import de.lojaw.module.Category;
import de.lojaw.module.Module;
import de.lojaw.module.ModuleManager;
import de.lojaw.module.impl.ClickGUIModule;
import de.lojaw.util.GuiUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import org.lwjgl.glfw.GLFW;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClickGUI extends Screen {
    private static ClickGUI instance;


    private boolean isOpen = false;
    private final Map<Category, Boolean> categoryStates = new HashMap<>();

    // Schritt 1: Felder für die Positionen der Kategorien
    private final Map<Category, Point> categoryPositions = new HashMap<>();
    private final Map<Module, Boolean> moduleStates = new HashMap<>();

    // Schritt 2: Felder für das Ziehen der Kategorien
    private Category draggingCategory = null;
    private Point clickOffset = null;

    private ClickGUI() {
        super(Text.of("Click GUI")); /* Titel der GUI, normalerweise ein Text Objekt */

        // Initialisiere die Positionen der Kategorien
        int x = 15;
        int y = 10;
       // Setzt alle Kategorien beim Start auf geschlossen
        for (Category category : Category.values()) {
            categoryStates.put(category, false); // Jetzt sind alle Kategorien standardmäßig geschlossen
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
        Map<Category, List<Module>> modulesByCategory = ModuleManager.getInstance().getModulesByCategory();
        for (Category category : modulesByCategory.keySet()) {
            Point pos = categoryPositions.get(category);  // Hole die Position der Kategorie

            int x = pos.x;  // Setze die anfängliche x-Position basierend auf der Position der Kategorie
            int y = pos.y;  // Setze die anfängliche y-Position basierend auf der Position der Kategorie

            // Zeichne Rahmen um die Kategorie
            drawRect(matrices, x - 5, y - 5, 150, 20, 0x80808080);

            // Zeichne Kategoriename hervorgehoben
            drawStringWithShadow(matrices, this.textRenderer, category.toString(), x, y, 0xFFFFFF, 1.5f);
            y += 20;

            if (!isCategoryOpen(category)) continue;

            for (Module module : modulesByCategory.get(category)) {
                // Überprüfe den Status des Moduls und passe das Rendern entsprechend an
                boolean moduleOpen = moduleStates.getOrDefault(module, true);
                if (moduleOpen) {
                    drawStringWithShadow(matrices, this.textRenderer, module.getName(), x, y, 0xFFFFFF, 1.0f);
                    y += 15;
                }
            }
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


    private Module clickedModule = null;

    private int clickOffsetX = 0;
    private int clickOffsetY = 0;

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        Category categoryClicked = getClickedCategory(mouseX, mouseY);
        System.out.println("Clicked category: " + categoryClicked);  // Debugging-Ausgabe

        // Berechnet die Position des "Reset Positions"-Buttons
        int resetButtonX = this.width - 150;  // 150 ist die Breite des Buttons
        int resetButtonY = this.height - 20;  // 20 ist die Höhe des Buttons

        // Überprüft, ob der Mausklick innerhalb der Grenzen des "Reset Positions"-Buttons liegt
        if (mouseX >= resetButtonX && mouseY >= resetButtonY && mouseX <= resetButtonX + 150 && mouseY <= resetButtonY + 20) {
            // Wenn ja, führe den Reset der Positionen durch
            resetPositions();
            return true;
        }

        if (categoryClicked != null) {
            if ((button == GLFW.GLFW_MOUSE_BUTTON_LEFT || button == GLFW.GLFW_MOUSE_BUTTON_RIGHT) && mouseY - categoryPositions.get(categoryClicked).getY() < 20) {
                if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
                    draggingCategory = categoryClicked;
                    clickOffset = new Point((int)mouseX - categoryPositions.get(categoryClicked).x, (int)mouseY - categoryPositions.get(categoryClicked).y);
                } else {
                    categoryStates.put(categoryClicked, !categoryStates.getOrDefault(categoryClicked, false));
                    System.out.println("Right click detected. New state: " + categoryStates.get(categoryClicked));  // Debugging-Ausgabe
                }
                return true;
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (draggingCategory != null) {
            int newX = (int)mouseX - clickOffset.x;
            int newY = (int)mouseY - clickOffset.y;

            int categoryWidth = textRenderer.getWidth(draggingCategory.toString()) + 10; // Breite der Kategorie
            int categoryHeight = 20; // Höhe der Kategorie

            // Beschränkung der X-Position, um sicherzustellen, dass die Kategorie nicht außerhalb des Fensters verschoben wird
            GuiUtils.ensureClientNotNull(this.client);

            int windowWidth = this.client.getWindow().getScaledWidth();
            if (newX < 0) {
                newX = 0;
            } else if (newX + categoryWidth > windowWidth) {
                newX = windowWidth - categoryWidth;
            }

            // Beschränkung der Y-Position, um sicherzustellen, dass die Kategorie nicht außerhalb des Fensters verschoben wird
            int windowHeight = this.client.getWindow().getScaledHeight();
            if (newY < 0) {
                newY = 0;
            } else if (newY + categoryHeight > windowHeight) {
                newY = windowHeight - categoryHeight;
            }

            // Aktualisieren Sie die Position der Kategorie auf die berechneten Werte
            categoryPositions.put(draggingCategory, new Point(newX, newY));

            return true;
        }

        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }


    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        draggingCategory = null;
        clickOffset = null;
        return super.mouseReleased(mouseX, mouseY, button);
    }

    private Category getClickedCategory(double mouseX, double mouseY) {
        for (Category category : categoryPositions.keySet()) {
            Point categoryPos = categoryPositions.get(category);
            int width = textRenderer.getWidth(category.toString()) + 10;
            int height = 20;
            if (mouseX >= categoryPos.x && mouseY >= categoryPos.y && mouseX <= categoryPos.x + width && mouseY <= categoryPos.y + height) {
                return category;
            }
        }
        return null;
    }




    private void resetPositions() {
        int x = 15;
        int y = 10;
        for (Category category : Category.values()) {
            categoryStates.put(category, false);
            categoryPositions.put(category, new Point(x, y));
            y += 35;  // Ändern Sie dies, um den Abstand zwischen den Kategorien anzupassen
        }
    }



    //@Override
    /*    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
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
    }*/

    //@Override
    /*public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
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
    }*/

    // Methoden um den Zustand einer Kategorie zu prüfen und zu ändern
    public boolean isCategoryOpen(Category category) {
        return categoryStates.get(category);
    }

    public void setCategoryOpen(Category category, boolean open) {
        categoryStates.put(category, open);
    }
}
