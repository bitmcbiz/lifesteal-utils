package dev.candycup.lifestealutils.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.candycup.lifestealutils.Config;
import dev.candycup.lifestealutils.ItemClusterRenderStateDuck;
//? if > 1.21.8
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.ItemEntityRenderer;
import net.minecraft.client.renderer.entity.state.ItemEntityRenderState;
//? if > 1.21.8
import net.minecraft.client.renderer.state.CameraRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntityRenderer.class)
public class ItemRendererMixin {
   //? if > 1.21.8 {

   @Inject(method = "submit(Lnet/minecraft/client/renderer/entity/state/ItemEntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/CameraRenderState;)V", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;mulPose(Lorg/joml/Quaternionfc;)V"))
   private void scaleItems(ItemEntityRenderState state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState cameraState, CallbackInfo ci) {
      if (!((ItemClusterRenderStateDuck) state).lifestealutils$isRare()) return;

      float scale = Config.getRareItemScaling();
      poseStack.scale(scale, scale, scale);
   }
   //?}
}
