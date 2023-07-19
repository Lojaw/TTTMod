package de.lojaw.util;

import net.minecraft.client.MinecraftClient;

public class GuiUtils {

    public static void ensureClientNotNull(MinecraftClient client) {
        if (client == null) {
            throw new RuntimeException("Client sollte nicht null sein");
        }
    }
}

