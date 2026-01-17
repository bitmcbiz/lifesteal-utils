package dev.candycup.lifestealutils.mixin;

import net.minecraft.client.gui.components.SplashRenderer;
import net.minecraft.client.resources.SplashManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static dev.candycup.lifestealutils.Config.getCustomSplashes;
import static dev.candycup.lifestealutils.features.titlescreen.CustomSplashTexts.SPLASH_TEXTS;

@Mixin(SplashManager.class)
public class SplashManagerMixin {
   @Inject(method = "getSplash", at = @At("HEAD"), cancellable = true)
   private void getSplashHead(CallbackInfoReturnable<SplashRenderer> cir) {
      if (getCustomSplashes()) {
         cir.setReturnValue(new SplashRenderer(
                 //? if > 1.21.10 {
                 dev.candycup.lifestealutils.interapi.MessagingUtils.miniMessage(
                         "<yellow>" + SPLASH_TEXTS.get((int) (Math.random() * SPLASH_TEXTS.size())) + "</yellow>"
                 )
                 //? } else {
                 /*SPLASH_TEXTS.get((int) (Math.random() * SPLASH_TEXTS.size()))
                  *///? }
         ));
      }
   }
}
