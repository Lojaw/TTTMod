package de.lojaw;

import de.lojaw.module.Module;
import de.lojaw.module.ModuleManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.message.v1.ClientSendMessageEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class TTTModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {

        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            if (!client.isInSingleplayer()) {

                // Sende eine Nachricht an den Spieler
                client.execute(() -> {
                    assert MinecraftClient.getInstance().player != null;
                    MinecraftClient.getInstance().player.sendMessage(Text.of("[TTTMod] Die Mod wurde geladen"), false);
                });
            }
        });

        ClientSendMessageEvents.ALLOW_CHAT.register((message) -> {
            // Überprüfung, ob die Nachricht dem mod Präfix entspricht
            if (!MinecraftClient.getInstance().isInSingleplayer() && message.startsWith(".tttmod")) {
                String[] args = message.split(" ");
                PlayerEntity player = MinecraftClient.getInstance().player;

                if (args.length > 1) {
                    if (args[1].equalsIgnoreCase("enable")) {
                        // Stelle sicher, dass args[2] existiert, bevor darauf zugegriffen wird
                        if (args.length > 2) {
                            String moduleName = args[2];
                            Module module = ModuleManager.getInstance().getModule(moduleName);

                            if (module != null) {
                                ModuleManager.getInstance().enableModule(moduleName);

                                if (player != null) {
                                    Text messageText = Text.of("[TTTMod] Das Modul " + moduleName + " wurde aktiviert.");
                                    Style style = messageText.getStyle().withColor(Formatting.GREEN);
                                    messageText = messageText.copy().setStyle(style);

                                    player.sendMessage(messageText, false);
                                }
                            } else {
                                if (player != null) {
                                    player.sendMessage(Text.of("[TTTMod] Das Modul " + moduleName + " existiert nicht."), false);
                                }
                            }

                            return false; // verhindert das Senden der Nachricht an den Server
                        } else {
                            if (player != null) {
                                player.sendMessage(Text.of("[TTTMod] Kein Modulname angegeben."), false);
                            }

                            return false; // verhindert das Senden der Nachricht an den Server
                        }
                    } else {
                        if (player != null) {
                            player.sendMessage(Text.of("[TTTMod] Unbekannter Befehl."), false);
                        }

                        return false; // verhindert das Senden der Nachricht an den Server
                    }
                } else {
                    // Liste von Befehlen senden
                    if (player != null) {
                        player.sendMessage(Text.of("[TTTMod] Liste der verfügbaren Befehle:"), false);
                        // TODO: Liste der verfügbaren Befehle
                    }

                    return false; // verhindert das Senden der Nachricht an den Server
                }
            }

            // Wenn die Nachricht nicht mit .tttmod beginnt, wird sie normal gesendet
            return true;
        });
    }
}
