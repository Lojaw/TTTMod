package de.lojaw.gui.clickgui;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import de.lojaw.module.Category;

import java.awt.*;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class ClickGUIStateManager {
    private static final String STATE_FILE = "click_gui_state.json";
    private static final Type TYPE = new TypeToken<Map<Category, Point>>(){}.getType();
    private static final Gson GSON = new Gson();

    public static void saveState(Map<Category, Point> categoryPositions, Map<Category, Boolean> categoryStates) {
        GUIState guiState = new GUIState(categoryPositions, categoryStates);
        String json = GSON.toJson(guiState);

        try (FileWriter file = new FileWriter(STATE_FILE)) {
            file.write(json);
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static GUIState loadState() {
        try (Reader reader = new FileReader(STATE_FILE)) {
            return GSON.fromJson(reader, GUIState.class);
        } catch (IOException e) {
            e.printStackTrace();
            // Erstelle ein neues GUIState-Objekt und initialisiere es mit Standardwerten
            GUIState newState = new GUIState();

            // Hier Initialisierung der Kategorie-Positionen und -Zustände
            Map<Category, Point> defaultPositions = new HashMap<>();
            Map<Category, Boolean> defaultStates = new HashMap<>();
            int x = 15;
            int y = 10;
            for (Category category : Category.values()) {
                defaultStates.put(category, false);
                defaultPositions.put(category, new Point(x, y));
                y += 35;
            }
            newState.categoryPositions = defaultPositions;
            newState.categoryStates = defaultStates;

            // Versuche, den neuen Zustand in die Datei zu schreiben
            try (FileWriter writer = new FileWriter(STATE_FILE)) {
                GSON.toJson(newState, writer);
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            // Gebe den neuen Zustand zurück
            return newState;
        }
    }

    public static class GUIState {
        public Map<Category, Point> categoryPositions;
        public Map<Category, Boolean> categoryStates;

        public GUIState() {
            // default constructor for Gson
        }

        public GUIState(Map<Category, Point> categoryPositions, Map<Category, Boolean> categoryStates) {
            this.categoryPositions = categoryPositions;
            this.categoryStates = categoryStates;
        }
    }
}
