package de.lojaw.gui.tabgui;

import de.lojaw.module.Category;
import de.lojaw.module.Module;
import de.lojaw.module.ModuleManager;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

public class TabGUI extends DrawableHelper {
    private static final int SELECTED_COLOR = 0xFFFF00;  // Gelb
    private static final int UNSELECTED_COLOR = 0xFFFFFF;  // Weiß
    private final TextRenderer textRenderer;
    private final List<Category> categories;
    private static int selectedCategoryIndex;
    private static int selectedModuleIndex;
    private static boolean isInModuleSelection;

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

        // Wenn der Benutzer sich im Modulauswahlfenster befindet, zeichne die Modulliste
        if (isInModuleSelection) {
            List<Module> modules = ModuleManager.getInstance().getModulesByCategory().get(categories.get(selectedCategoryIndex));

            // Zeichne ein Rechteck für die Modulliste
            int moduleRectX = rectX + rectWidth + 10;  // Positioniert das Modulmenü 10 Pixel rechts vom Kategoriemenü
            fill(matrices, moduleRectX, 10, moduleRectX + rectWidth, modules.size() * 15 + 20, 0x60000000);  // Füllfarbe ist dezenteres Schwarz

            // Holt die aktiven Module
            Collection<Module> activeModules = ModuleManager.getInstance().getActiveModules();

            // Zeichne jedes Modul in das Rechteck
            y = 20;
            for (int i = 0; i < modules.size(); i++) {
                Module module = modules.get(i);

                // Wenn das Modul ausgewählt ist, zeichne einen grauen Hintergrund
                if (i == selectedModuleIndex) {
                    //fill(matrices, moduleRectX, y - 2, moduleRectX + rectWidth, y + 13, 0x40000000);  // 0x40 für eine dezente Grautönung

                    // Verändere die Farbe zu #7f7e82 (grau) mit voller Undurchsichtigkeit
                    //fill(matrices, moduleRectX, y - 2, moduleRectX + rectWidth, y + 13, 0xFF7f7e82);

                    // Verändere die Farbe zu #7f7e82 mit halber Transparenz
                    //#434345
                    fill(matrices, moduleRectX, y - 2, moduleRectX + rectWidth, y + 13, 0x80434345);

                }

                // Überprüft, ob das Modul aktiv ist, um seine Farbe zu bestimmen
                int color = activeModules.contains(module) ? 0x00FF00 : UNSELECTED_COLOR;  // Grün, wenn aktiviert, sonst Weiß

                drawCenteredTextWithShadow(matrices, this.textRenderer, Text.of(module.getName()), moduleRectX + rectWidth / 2, y, color);
                y += 15;
            }
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
        if (isInModuleSelection) {
            // Wenn der Benutzer sich im Modulauswahlfenster befindet
            if (keyCode == GLFW.GLFW_KEY_UP) {
                // Bewege die Auswahl im Modulmenü nach oben, bleibe aber innerhalb der Grenzen
                if (selectedModuleIndex > 0) {
                    selectedModuleIndex--;
                }
            } else if (keyCode == GLFW.GLFW_KEY_DOWN) {
                // Bewege die Auswahl im Modulmenü nach unten, bleibe aber innerhalb der Grenzen
                if (selectedModuleIndex < ModuleManager.getInstance().getModulesByCategory().size() - 1) {
                    selectedModuleIndex++;
                }
            } else if (keyCode == GLFW.GLFW_KEY_LEFT) {
                // Der Benutzer drückt die linke Pfeiltaste, also schließen wir das Modulauswahlfenster
                isInModuleSelection = false;
            } else if (keyCode == GLFW.GLFW_KEY_ENTER) {
                // Der Benutzer hat die Eingabetaste gedrückt
                List<Module> modules = ModuleManager.getInstance().getModulesByCategory().get(categories.get(selectedCategoryIndex));
                Module selectedModule = modules.get(selectedModuleIndex);

                if (ModuleManager.getInstance().getActiveModules().contains(selectedModule)) {
                    // Wenn das Modul aktiviert ist, deaktivieren wir es
                    ModuleManager.getInstance().disableModule(selectedModule.getName());
                } else {
                    // Wenn das Modul deaktiviert ist, aktivieren wir es
                    ModuleManager.getInstance().enableModule(selectedModule.getName());
                }
            }
        } else {
            // Wenn der Benutzer sich im Kategoriemenü befindet
            if (keyCode == GLFW.GLFW_KEY_UP) {
                // Bewege die Auswahl nach oben, bleibe aber innerhalb der Grenzen
                if (selectedCategoryIndex > 0) {
                    selectedCategoryIndex--;
                }
            } else if (keyCode == GLFW.GLFW_KEY_DOWN) {
                // Bewege die Auswahl nach unten, bleibe aber innerhalb der Grenzen
                if (selectedCategoryIndex < categories.size() - 1) {
                    selectedCategoryIndex++;
                }
            } else if (keyCode == GLFW.GLFW_KEY_RIGHT) {
                // Der Benutzer drückt die rechte Pfeiltaste, also öffnen wir das Modulauswahlfenster
                List<Module> modules = ModuleManager.getInstance().getModulesByCategory().get(categories.get(selectedCategoryIndex));
                if (modules != null && !modules.isEmpty()) {
                    isInModuleSelection = true;
                    selectedModuleIndex = 0;  // Standardmäßig ist das oberste Modul ausgewählt
                } else {
                    PlayerEntity player = MinecraftClient.getInstance().player;
                    if (player != null) {
                        player.sendMessage(Text.of("[TTTMod] Für diese Kategorie gibt es noch keine Module"));
                    }
                }
            }
        }
    }
}
