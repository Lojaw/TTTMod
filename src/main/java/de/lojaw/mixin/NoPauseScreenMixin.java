package de.lojaw.mixin;

import de.lojaw.gui.clickgui.ClickGUI;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Screen.class)
public abstract class NoPauseScreenMixin {
    @Inject(at = @At("HEAD"), method = "shouldPause", cancellable = true)
    private void shouldPause(CallbackInfoReturnable<Boolean> info) {
        if ((Object) this instanceof ClickGUI) {
            info.setReturnValue(false);
        }
    }
}

