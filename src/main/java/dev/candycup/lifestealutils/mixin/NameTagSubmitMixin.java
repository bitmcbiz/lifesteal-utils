package dev.candycup.lifestealutils.mixin;


import dev.candycup.lifestealutils.Config;
import dev.candycup.lifestealutils.features.alliances.Alliances;
import dev.candycup.lifestealutils.interapi.MessagingUtils;
import net.kyori.adventure.platform.modcommon.MinecraftClientAudiences;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;import org.spongepowered.asm.mixin.injection.invoke.arg.Args;
//? if 1.21.8
//import net.minecraft.client.renderer.entity.player.PlayerRenderer;

//? if >1.21.8 {
@Pseudo
@Mixin(targets = "net.minecraft.client.renderer.SubmitNodeStorage$NameTagSubmit")
public class NameTagSubmitMixin {

   @Inject(method = "text", at = @At("RETURN"), cancellable = true)
   private void textReturn(CallbackInfoReturnable<Component> cir) {
      if (!Config.getEnableAlliances()) return;
      Component original = cir.getReturnValue();
      if (original == null) return;

      String serialized = MiniMessage.miniMessage().serialize(MinecraftClientAudiences.of().asAdventure(original));
      String lastWord = Alliances.getLastVisibleWord(serialized);
      if (lastWord == null || lastWord.isBlank()) return;
      if (!Alliances.isAlliedName(lastWord)) return;

      cir.setReturnValue(Alliances.colorizeNameTag(original));
   }
}
//?} else {
/*@Mixin(PlayerRenderer.class)
public class NameTagSubmitMixin {
   @ModifyArg(
           method = "renderNameTag(Lnet/minecraft/client/renderer/entity/state/PlayerRenderState;Lnet/minecraft/network/chat/Component;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
           at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/LivingEntityRenderer;renderNameTag(Lnet/minecraft/client/renderer/entity/state/EntityRenderState;Lnet/minecraft/network/chat/Component;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V"),
           index = 1
   )
   private Component renderNameTag(Component par2) {
      if (!Config.getEnableAlliances()) return par2;
      Component original = par2;
      if (original == null) return null;

      String serialized = MiniMessage.miniMessage().serialize(MinecraftClientAudiences.of().asAdventure(original));
      String lastWord = Alliances.getLastVisibleWord(serialized);
      if (lastWord == null || lastWord.isBlank()) return par2;
      if (!Alliances.isAlliedName(lastWord)) return par2;

      return Alliances.colorizeNameTag(original);
   }
}
*///?}
