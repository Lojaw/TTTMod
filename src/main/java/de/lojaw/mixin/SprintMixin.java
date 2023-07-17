package de.lojaw.mixin;

import de.lojaw.module.ModuleManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public class SprintMixin {
    @Inject(at = @At("HEAD"), method = "tick")
    private void onTick(CallbackInfo info) {
        if (ModuleManager.getInstance().getModule("Sprint").isEnabled()) {
            ClientPlayerEntity player = (ClientPlayerEntity) (Object) this;
            if (MinecraftClient.getInstance().options.forwardKey.isPressed()) {
                player.setSprinting(true);
            }
        }
    }
}
