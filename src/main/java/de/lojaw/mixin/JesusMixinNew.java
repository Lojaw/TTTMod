package de.lojaw.mixin;

import de.lojaw.module.ModuleManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Entity.class)
public class JesusMixinNew {
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
