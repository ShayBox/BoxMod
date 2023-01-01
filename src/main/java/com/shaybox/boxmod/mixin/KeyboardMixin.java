package com.shaybox.boxmod.mixin;

import net.minecraft.client.Keyboard;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.GameModeSelectionScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Keyboard.class)
public class KeyboardMixin {

    @Inject(method = "processF3", at = @At("HEAD"), cancellable = true)
    public void processF3(int key, CallbackInfoReturnable<Boolean> cir) {
        if (key == 293) {
            MinecraftClient.getInstance().setScreen(new GameModeSelectionScreen());
            cir.setReturnValue(true);
        }
    }

}
