package dev.candycup.lifestealutils.mixin;

import dev.candycup.lifestealutils.api.LifestealServerDetector;
import dev.candycup.lifestealutils.api.LifestealTablistAPI;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundTabListPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public class ClientPacketListenerMixin {
   @Inject(method = "handleTabListCustomisation", at = @At("RETURN"))
   private void onTabListUpdate(ClientboundTabListPacket packet, CallbackInfo ci) {
      if (LifestealServerDetector.isOnLifestealServer()) {
         LifestealTablistAPI.updateFromFooter(packet.footer());
      } else {
         LifestealTablistAPI.reset();
      }
   }
}
