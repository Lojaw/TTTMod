package de.lojaw.mixin;

import de.lojaw.module.Module;
import de.lojaw.module.ModuleManager;
import net.minecraft.block.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractBlock.AbstractBlockState.class)
public class AbstractBlockStateMixin {

    //getCollisionShape ist eine überladene Methode, deswegen muss der genaue Pfad angegeben werden,
    // sonst crasht das Spiel direkt
    @Inject(at = @At("HEAD"), method = "getCollisionShape(Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/ShapeContext;)Lnet/minecraft/util/shape/VoxelShape;", cancellable = true)
    public void getCollisionShape(BlockView world, BlockPos pos, ShapeContext context, CallbackInfoReturnable<VoxelShape> cir) {
        Module jesusModule = ModuleManager.getInstance().getModule("Jesus");
        if (jesusModule != null && jesusModule.isEnabled()) {
            BlockState state = world.getBlockState(pos);
            if (state.getBlock() == Blocks.WATER) {
                cir.setReturnValue(VoxelShapes.fullCube());
            }
        }
    }
}



