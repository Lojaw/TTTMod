package de.lojaw.mixin;

import de.lojaw.module.ModuleManager;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class JesusMixin {

    @Inject(method = "isOnGround", at = @At("HEAD"), cancellable = true)
    public void onHandleOnGround(CallbackInfoReturnable<Boolean> cir) {
        PlayerEntity player = null;

        try {
            player = (PlayerEntity) (Object) this;
        } catch (ClassCastException e) {
            // Das Entity ist keine PlayerEntity, machen Sie nichts.
        }

        if (player != null && ModuleManager.getInstance().getModule("Jesus").isEnabled()) {

            //BlockPos entityPos = new BlockPos((int)player.getX(), (int)player.getY(), (int)player.getZ());
            //BlockPos blockUnderPos = entityPos.down(); // moves the position one block down
            World world = player.getEntityWorld();
            //BlockState blockUnderState = world.getBlockState(blockUnderPos);
            //Block blockUnder = blockUnderState.getBlock();

            BlockPos entityPos = new BlockPos((int) Math.floor(player.getX()), (int) Math.floor(player.getY() - 0.5), (int) Math.floor(player.getZ()));
            BlockState blockState = world.getBlockState(entityPos);
            Block block = blockState.getBlock();


            if (blockState.getFluidState().isIn(FluidTags.WATER)) {
                player.setPosition(player.getPos().getX(), player.getPos().getY() + 1, player.getPos().getZ());
                cir.setReturnValue(true);
            }
        }
    }

    @Inject(method = "isTouchingWater", at = @At("HEAD"), cancellable = true)
    public void onHandleIsTouchingWater(CallbackInfoReturnable<Boolean> cir) {
        PlayerEntity player = null;

        try {
            player = (PlayerEntity) (Object) this;
        } catch (ClassCastException e) {
            // Das Entity ist keine PlayerEntity, machen Sie nichts.
        }

        if (player != null && ModuleManager.getInstance().getModule("Jesus").isEnabled()) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "updateWaterState", at = @At("HEAD"), cancellable = true)
    public void onUpdateWaterState(CallbackInfoReturnable<Boolean> cir) {
        PlayerEntity player = null;

        try {
            player = (PlayerEntity) (Object) this;
        } catch (ClassCastException e) {
            // Das Entity ist keine PlayerEntity, machen Sie nichts.
        }

        if (player != null && ModuleManager.getInstance().getModule("Jesus").isEnabled()) {
            cir.setReturnValue(false);
        }
    }


    @Redirect(method = "move", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;isTouchingWater()Z"))
    public boolean redirectIsTouchingWater(Entity entity) {
        // Überprüfen Sie, ob Ihr Modul aktiviert ist
        if (ModuleManager.getInstance().getModule("Jesus").isEnabled()) {
            // Wenn ja, geben Sie 'false' zurück, um zu "simulieren", dass die Entity nicht im Wasser ist
            return false;
        } else {
            // Wenn nicht, rufen Sie die ursprüngliche Methode auf
            return entity.isTouchingWater();
        }
    }
}