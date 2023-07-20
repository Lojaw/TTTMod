package de.lojaw.mixin;

import de.lojaw.module.ModuleManager;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class NoFallMixin extends LivingEntity {
    @Shadow @Final private PlayerAbilities abilities;

    @Shadow public abstract void increaseStat(Identifier stat, int amount);

    protected NoFallMixin() {
        super(null, null);
    }

    @Inject(at = @At("HEAD"), method = "handleFallDamage", cancellable = true)
    private void onHandleFallDamage(float fallDistance, float damageMultiplier, DamageSource damageSource, CallbackInfoReturnable<Boolean> cir) {
        if (this.abilities.allowFlying) {
            cir.setReturnValue(false);
            cir.cancel();
            return;
        }
        if (fallDistance >= 2.0f) {
            this.increaseStat(Stats.FALL_ONE_CM, (int)Math.round((double)fallDistance * 100.0));
        }

        // eigene Logik
        if (ModuleManager.getInstance().getModule("NoFall").isEnabled()) {
            cir.setReturnValue(false);
            cir.cancel();
            return;
        }
        //

        cir.setReturnValue(super.handleFallDamage(fallDistance, damageMultiplier, damageSource));
        cir.cancel();
    }
}





