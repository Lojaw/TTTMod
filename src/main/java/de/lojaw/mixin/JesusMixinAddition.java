package de.lojaw.mixin;

import de.lojaw.module.ModuleManager;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.stat.Stats;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class JesusMixinAddition extends LivingEntity {

    @Shadow @Final private PlayerAbilities abilities;

    @Shadow public abstract void increaseStat(Identifier stat, int amount);
    @Shadow public abstract void addExhaustion(float exhaustion);

    protected JesusMixinAddition() {
        super(null, null);
    }

    @Inject(at = @At("HEAD"), method = "increaseTravelMotionStats", cancellable = true)
    private void onHandleincreaseTravelMotionStats(double dx, double dy, double dz, CallbackInfo ci) {

        if (this.hasVehicle()) {
            return;
        }
        if (this.isSwimming()) {
            int i = Math.round((float)Math.sqrt(dx * dx + dy * dy + dz * dz) * 100.0f);
            if (i > 0) {
                this.increaseStat(Stats.SWIM_ONE_CM, i);
                this.addExhaustion(0.01f * (float)i * 0.01f);
            }
        } else if (this.isSubmergedIn(FluidTags.WATER)) {
            int i = Math.round((float)Math.sqrt(dx * dx + dy * dy + dz * dz) * 100.0f);
            if (i > 0) {
                this.increaseStat(Stats.WALK_UNDER_WATER_ONE_CM, i);
                this.addExhaustion(0.01f * (float)i * 0.01f);
            }
        } else if (this.isTouchingWater()) {

            // Eigene Logik, wir tuen so, als wäre das Spieler onGround
            if (ModuleManager.getInstance().getModule("Jesus").isEnabled()) {

                int i = Math.round((float)Math.sqrt(dx * dx + dz * dz) * 100.0f);
                if (i > 0) {
                    if (this.isSprinting()) {
                        this.increaseStat(Stats.SPRINT_ONE_CM, i);
                        this.addExhaustion(0.1f * (float)i * 0.01f);
                    } else if (this.isInSneakingPose()) {
                        this.increaseStat(Stats.CROUCH_ONE_CM, i);
                        this.addExhaustion(0.0f * (float)i * 0.01f);
                    } else {
                        this.increaseStat(Stats.WALK_ONE_CM, i);
                        this.addExhaustion(0.0f * (float)i * 0.01f);
                    }
                }

            } else {
                int i = Math.round((float)Math.sqrt(dx * dx + dz * dz) * 100.0f);
                if (i > 0) {
                    this.increaseStat(Stats.WALK_ON_WATER_ONE_CM, i);
                    this.addExhaustion(0.01f * (float)i * 0.01f);
                }
            }
            //

        } else if (this.isClimbing()) {
            if (dy > 0.0) {
                this.increaseStat(Stats.CLIMB_ONE_CM, (int)Math.round(dy * 100.0));
            }
        } else if (this.onGround) {
            int i = Math.round((float)Math.sqrt(dx * dx + dz * dz) * 100.0f);
            if (i > 0) {
                if (this.isSprinting()) {
                    this.increaseStat(Stats.SPRINT_ONE_CM, i);
                    this.addExhaustion(0.1f * (float)i * 0.01f);
                } else if (this.isInSneakingPose()) {
                    this.increaseStat(Stats.CROUCH_ONE_CM, i);
                    this.addExhaustion(0.0f * (float)i * 0.01f);
                } else {
                    this.increaseStat(Stats.WALK_ONE_CM, i);
                    this.addExhaustion(0.0f * (float)i * 0.01f);
                }
            }
        } else if (this.isFallFlying()) {
            int i = Math.round((float)Math.sqrt(dx * dx + dy * dy + dz * dz) * 100.0f);
            this.increaseStat(Stats.AVIATE_ONE_CM, i);
        } else {
            int i = Math.round((float)Math.sqrt(dx * dx + dz * dz) * 100.0f);
            if (i > 25) {
                this.increaseStat(Stats.FLY_ONE_CM, i);
            }
        }

        ci.cancel();

    }
}