package dev.candycup.lifestealutils.mixin;

import dev.candycup.lifestealutils.LifestealServerDetector;
import dev.candycup.lifestealutils.event.EventBus;
import dev.candycup.lifestealutils.event.events.ChatMessageReceivedEvent;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.network.chat.Component;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChatComponent.class)
public class MessageReceiver {
   @Shadow
   @Final
   private static Logger LOGGER;

   @Unique
   private static final ThreadLocal<Boolean> lifestealutils$reentrant =
         ThreadLocal.withInitial(() -> false);

   @Inject(at = @At("HEAD"), method = "addMessage(Lnet/minecraft/network/chat/Component;)V", cancellable = true)
   private void addMessage(Component component, CallbackInfo ci) {
      if (lifestealutils$reentrant.get()) {
         return;
      }

      if (!LifestealServerDetector.isOnLifestealServer()) {
         return;
      }

      // post chat message received event
      ChatMessageReceivedEvent event = new ChatMessageReceivedEvent(component);
      EventBus.getInstance().post(event);

      // if any feature cancelled the event, prevent the original message
      if (event.isCancelled()) {
         ci.cancel();
         return;
      }

      // if the message was modified, show the modified version instead
      Component modified = event.getModifiedMessage();
      if (modified != null && modified != component) {
         ci.cancel();
         lifestealutils$reentrant.set(true);
         try {
            ((ChatComponent) (Object) this).addMessage(modified);
         } finally {
            lifestealutils$reentrant.set(false);
         }
      }
   }
}