package de.lojaw.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.Field;

@Mixin(PlayerEntityRenderer.class)
public class PlayerEntityRendererMixin {

    private static final Identifier DEV_CAPE = new Identifier("tttmod", "devcape.png");
    private static final Identifier USER_CAPE = new Identifier("tttmod", "usercape.png");

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/LivingEntityRenderer;render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V"))
    private void renderCape(AbstractClientPlayerEntity entity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci) {
        try {
            if (DEV_CAPE != null && USER_CAPE != null) {
                Identifier capeTexture = entity.getName().getString().startsWith("Player") ? DEV_CAPE : USER_CAPE;

                MinecraftClient.getInstance().getTextureManager().bindTexture(capeTexture);

                PlayerEntityModel<AbstractClientPlayerEntity> playerModel = ((PlayerEntityRenderer) (Object) this).getModel();

                Field cloakField = PlayerEntityModel.class.getDeclaredField("cloak");
                cloakField.setAccessible(true);
                ModelPart cloak = (ModelPart) cloakField.get(playerModel);

                cloak.visible = true;

                cloak.setPivot(0F, 22.5F, 0F);

                double yaw = entity.prevYaw * ((float)Math.PI / 180F);
                double pitch = entity.prevPitch * ((float)Math.PI / 180F);
                double roll = 0.0F;  // Je nachdem, wie Sie Ihren Umhang rotieren möchten, möchten Sie vielleicht auch diese Variable anpassen

                // Setzt die Rotation des Umhangs auf die gleiche Rotation wie die des Spielers
                cloak.setAngles((float) yaw, 0.0F, (float) roll);

                // Beginn des hinzugefügten Render-Codes
                VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getEntitySolid(capeTexture));
                cloak.render(matrixStack, vertexConsumer, i, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
                // Ende des hinzugefügten Render-Codes


            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}