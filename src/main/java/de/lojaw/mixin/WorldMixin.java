package de.lojaw.mixin;

import de.lojaw.module.ModuleManager;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(World.class)
public class WorldMixin {

    @Inject(at = @At("HEAD"), method = "getBlockState", cancellable = true)
    private void getBlockState(BlockPos pos, CallbackInfoReturnable<BlockState> cir) {
        // Überprüfen Sie, ob das Modul "Jesus" aktiv ist
        if (ModuleManager.getInstance().getModule("Jesus").isEnabled()) {

            // Bestimmen Sie die Position unter dem Spieler
            Vec3d posVec = MinecraftClient.getInstance().player.getPos();
            BlockPos underPlayerPos = new BlockPos((int) posVec.x, (int) (posVec.y - 1), (int) posVec.z);

            // Überprüfen Sie, ob das Block unter dem Spieler Wasser ist
            if (pos.equals(underPlayerPos) && cir.getReturnValue().getFluidState().isIn(FluidTags.WATER)) {
                // Geben Sie stattdessen einen Grasblock zurück
                cir.setReturnValue(Blocks.GRASS.getDefaultState());
            }
        }
    }
}