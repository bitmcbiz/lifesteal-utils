package dev.candycup.lifestealutils.mixin;

import dev.candycup.lifestealutils.Config;
import dev.candycup.lifestealutils.LifestealServerDetector;
import dev.candycup.lifestealutils.interapi.MessagingUtils;
import net.kyori.adventure.platform.modcommon.MinecraftClientAudiences;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.util.regex.Pattern;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.PlayerTabOverlay;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.Scoreboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(PlayerTabOverlay.class)
public abstract class TabListMixin {
   @Shadow
   protected abstract List<PlayerInfo> getPlayerInfos();

   @Shadow
   public abstract Component getNameForDisplay(PlayerInfo playerInfo);

   @Inject(method = "render", at = @At("HEAD"))
   private void renderHead(GuiGraphics guiGraphics, int i, Scoreboard scoreboard, Objective objective, CallbackInfo ci) {
      List<PlayerInfo> list = getPlayerInfos();


      for (PlayerInfo playerInfo : list) {
         if (playerInfo == null) continue;
         Component component = getNameForDisplay(playerInfo);
      }
   }

   @Inject(method = "decorateName", at = @At("HEAD"), cancellable = true)
   private void decorateNameHead(PlayerInfo playerInfo, MutableComponent mutableComponent, CallbackInfoReturnable<Component> cir) {
      if (!Config.getRemoveUniquePlusColor()) return;
      if (!LifestealServerDetector.isOnLifestealServer()) return;

      String serialized = MiniMessage.miniMessage().serialize(MinecraftClientAudiences.of().asAdventure(mutableComponent));

      boolean hadPlus = serialized.contains("+");

      // Removes the original
      serialized = serialized.replace("+", "");

      // Re-add the + just before the first coloring block closes (</) if there was one originally
      if (hadPlus) {
         int index = serialized.indexOf("</");
         if (index != -1) {
            serialized = serialized.substring(0, index) + "+" + serialized.substring(index);
         }
      }

      cir.setReturnValue(MessagingUtils.miniMessage(serialized));
   }
}
