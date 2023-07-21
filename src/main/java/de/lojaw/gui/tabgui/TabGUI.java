package de.lojaw.gui.tabgui;

import de.lojaw.module.Category;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class TabGUI extends DrawableHelper {
    private static final int SELECTED_COLOR = 0xFFFF00;  // Gelb
    private static final int UNSELECTED_COLOR = 0xFFFFFF;  // Weiß
    private final TextRenderer textRenderer;
    private final List<Category> categories;
    private static int selectedCategoryIndex;

    public TabGUI(TextRenderer textRenderer) {
        System.out.println("Creating a new TabGUI instance at " + System.currentTimeMillis());
        this.textRenderer = textRenderer;
        this.categories = Arrays.asList(Category.values());
        this.categories.sort(Comparator.comparing(Enum::name)); // Sortiert Kategorien nach Namen
        //this.selectedCategoryIndex = 0;  // Standardmäßig ist die oberste Kategorie ausgewählt
    }

    public void render(MatrixStack matrices) {
        //System.out.println("Rendering with selectedCategoryIndex: " + selectedCategoryIndex);

        // Ändern Sie diese Werte nach Ihren Wünschen
        int rectWidth = 100;
        int rectX = 10;  // Positioniert das GUI 10 Pixel von der linken Seite des Bildschirms entfernt

        // Zeichne ein Rechteck an der linken Seite des Bildschirms
        fill(matrices, rectX, 10, rectX + rectWidth, categories.size() * 15 + 20, 0x60000000); // Füllfarbe ist dezenteres Schwarz

        // Zeichne jede Kategorie in das Rechteck
        int y = 20;
        for (int i = 0; i < categories.size(); i++) {
            Category category = categories.get(i);
            int color = (i == selectedCategoryIndex) ? SELECTED_COLOR : UNSELECTED_COLOR;
            drawCenteredTextWithShadow(matrices, this.textRenderer, Text.of(category.name()), rectX + rectWidth / 2, y, color);
            y += 15;
        }
    }

    private long lastKeyPressTime = 0;

    public void onKeyPressed(int keyCode) {
        long now = System.currentTimeMillis();
        if (now - lastKeyPressTime < 200) {
            // Weniger als eine Sekunde seit dem letzten Tastendruck vergangen, also nichts tun
            return;
        }
        lastKeyPressTime = now;

        // Rest des Codes...
        if (keyCode == GLFW.GLFW_KEY_UP) {
            System.out.println("Key up pressed!");
            // Bewege die Auswahl nach oben, bleibe aber innerhalb der Grenzen
            if (selectedCategoryIndex > 0) {
                selectedCategoryIndex--;
                System.out.println("Updated selectedCategoryIndex: " + selectedCategoryIndex);
            }
        } else if (keyCode == GLFW.GLFW_KEY_DOWN) {
            System.out.println("Key down pressed!");
            // Bewege die Auswahl nach unten, bleibe aber innerhalb der Grenzen
            if (selectedCategoryIndex < categories.size() - 1) {
                selectedCategoryIndex++;
                System.out.println("Updated selectedCategoryIndex: " + selectedCategoryIndex);
            }
        }
    }
}
