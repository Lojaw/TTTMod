package de.lojaw.mixin;

import de.lojaw.gui.clickgui.ClickGUI;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin {
    @Shadow @Final private PlayerAbilities abilities;

    @Shadow public abstract void increaseStat(Identifier stat, int amount);

    @Inject(method = "tickMovement", at = @At("HEAD"))
    private void tickMovement(CallbackInfo ci) {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (player != null) {
            if (ClickGUI.getInstance().isOpen()) {
                KeyBinding forwardKey = MinecraftClient.getInstance().options.forwardKey;
                KeyBinding backKey = MinecraftClient.getInstance().options.backKey;
                KeyBinding leftKey = MinecraftClient.getInstance().options.leftKey;
                KeyBinding rightKey = MinecraftClient.getInstance().options.rightKey;
                KeyBinding jumpKey = MinecraftClient.getInstance().options.jumpKey;

                double forwardInput = forwardKey.isPressed() ? 1.0 : (backKey.isPressed() ? -1.0 : 0.0);
                double sidewaysInput = rightKey.isPressed() ? 1.0 : (leftKey.isPressed() ? -1.0 : 0.0);

                Vec3d movementInput = new Vec3d(forwardInput, jumpKey.isPressed() ? 1.0 : 0.0, sidewaysInput);

                player.travel(movementInput);
            }
        }
    }
}

