package de.lojaw.mixin;

import de.lojaw.gui.ModuleList;
import de.lojaw.module.Module;
import de.lojaw.module.ModuleManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHudMixin {
    private ModuleList moduleList;
    private Module moduleListModule;

    @Inject(at = @At("RETURN"), method = "render")
    public void render(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
        if (moduleList == null) {
            MinecraftClient client = MinecraftClient.getInstance();
            int screenWidth = client.getWindow().getScaledWidth();
            this.moduleList = new ModuleList(client.textRenderer, ModuleManager.getInstance());
            this.moduleListModule = ModuleManager.getInstance().getModule("ModuleList");
        }
        if (moduleListModule != null && moduleListModule.isEnabled()) {
            moduleList.render(matrices);
        }
    }
}



