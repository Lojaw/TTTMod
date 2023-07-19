package de.lojaw;

import de.lojaw.discordintegration.PythonRunner;
import de.lojaw.module.Module;
import de.lojaw.module.ModuleManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.message.v1.ClientSendMessageEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.lwjgl.glfw.GLFW;

public class TTTModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ModuleManager.getInstance().registerModulesAndKeybindings();

        PythonRunner.runPythonScript();

        // ShutdownHook hinzufügen
        Runtime.getRuntime().addShutdownHook(new Thread(PythonRunner::stopPythonProcess));

        // Starten Sie die Methode zum Senden von "alive"-Signalen
        PythonRunner.sendAliveSignal();

        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            //if (!client.isInSingleplayer()) {
                // Sende eine Nachricht an den Spieler
                client.execute(() -> {
                    assert MinecraftClient.getInstance().player != null;
                    MinecraftClient.getInstance().player.sendMessage(Text.of("[TTTMod] Die Mod wurde geladen"), false);
                });
            //}
        });

        ClientSendMessageEvents.ALLOW_CHAT.register((message) -> {
            //if (!MinecraftClient.getInstance().isInSingleplayer() && message.startsWith(".tttmod")) {
                String[] args = message.split(" ");
                PlayerEntity player = MinecraftClient.getInstance().player;

                if (args.length > 1) {
                    String command = args[1].toLowerCase();

                    switch (command) {
                        case "enable":
                            handleEnableCommand(args, player);
                            return false;
                        case "disable":
                            handleDisableCommand(args, player);
                            return false;
                        default:
                            if (player != null) {
                                player.sendMessage(Text.of("[TTTMod] Unbekannter Befehl."), false);
                            }
                            return false;
                    }
                } else {
                    // Liste von Befehlen senden
                    if (player != null) {
                        player.sendMessage(Text.of("[TTTMod] Liste der verfügbaren Befehle:"), false);
                        // TODO: Liste der verfügbaren Befehle
                    }
                    return false;
                }
            //}
            // Wenn die Nachricht nicht mit .tttmod beginnt, wird sie normal gesendet
        });
    }

    private void handleEnableCommand(String[] args, PlayerEntity player) {
        if (args.length < 3) {
            sendMessageToPlayer(player, "[TTTMod] Kein Modulname angegeben.", Formatting.RED);
            return;
        }

        String moduleName = args[2];
        Module module = ModuleManager.getInstance().getModule(moduleName);

        if (module == null) {
            sendMessageToPlayer(player, "[TTTMod] Das Modul " + moduleName + " existiert nicht.", Formatting.RED);
            return;
        }

        if (args.length > 3) {
            handleEnableCommandWithKey(args, player, moduleName, module);
        } else {
            ModuleManager.getInstance().enableModule(moduleName);
            sendMessageToPlayer(player, "[TTTMod] Das Modul " + moduleName + " wurde aktiviert.", Formatting.GREEN);
        }
    }

    private void handleEnableCommandWithKey(String[] args, PlayerEntity player, String moduleName, Module module) {
        if (args.length < 5) {
            sendMessageToPlayer(player, "[TTTMod] Keine Taste angegeben.", Formatting.RED);
            return;
        }

        String mode = args[3].toLowerCase();
        String letter = args[4];
        int key = getKeycodeFromLetter(letter);

        if (key == GLFW.GLFW_KEY_UNKNOWN) {
            sendMessageToPlayer(player, "[TTTMod] Ungültige Taste angegeben. Bitte geben Sie einen Schlüssel zwischen A-Z oder 0-9 ein.", Formatting.RED);
            return;
        }

        switch (mode) {
            case "toggle":
            case "hold":
                ModuleManager.getInstance().updateModuleKey(moduleName, letter);
                module.setMode(mode);
                break;
            default:
                sendMessageToPlayer(player, "[TTTMod] Unbekannter Modus " + mode + ". Verfügbare Modi sind 'toggle' und 'hold'.", Formatting.RED);
                return;
        }

        ModuleManager.getInstance().enableModule(moduleName);
        sendMessageToPlayer(player, "[TTTMod] Das Modul " + moduleName + " wurde aktiviert mit " + mode + " Mode.", Formatting.GREEN);
    }

    private int getKeycodeFromLetter(String letter) {
        if (letter.length() != 1) {
            return GLFW.GLFW_KEY_UNKNOWN;
        }
        char c = Character.toUpperCase(letter.charAt(0));
        return org.lwjgl.glfw.GLFW.glfwGetKeyScancode(c);
    }

    private void sendMessageToPlayer(PlayerEntity player, String message, Formatting color) {
        if (player != null) {
            Text messageText = Text.of(message);
            Style style = messageText.getStyle().withColor(color);
            messageText = messageText.copy().setStyle(style);
            player.sendMessage(messageText, false);
        }
    }

    private void handleDisableCommand(String[] args, PlayerEntity player) {
        if (args.length < 3) {
            if (player != null) {
                player.sendMessage(Text.of("[TTTMod] Kein Modulname angegeben."), false);
            }
        } else {
            String moduleName = args[2];
            ModuleManager.getInstance().disableModule(moduleName);
            if (player != null) {
                player.sendMessage(Text.of("[TTTMod] Das Modul " + moduleName + " wurde deaktiviert."), false);
            }
        }
    }
}