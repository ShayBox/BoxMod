package com.shaybox.boxmod.mixin;

import com.shaybox.boxmod.client.BoxModClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.GameStateChangeS2CPacket;
import net.minecraft.network.packet.s2c.play.WorldBorderInitializeS2CPacket;
import net.minecraft.network.packet.s2c.play.WorldTimeUpdateS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {

    @Inject(method = "onGameStateChange", at = @At("HEAD"), cancellable = true)
    public void onGameStateChange(GameStateChangeS2CPacket packet, CallbackInfo ci) {
        GameStateChangeS2CPacket.Reason reason = packet.getReason();

        if (reason.equals(GameStateChangeS2CPacket.DEMO_MESSAGE_SHOWN)) {
            ci.cancel();
        }
    }

    @Inject(method = "onWorldBorderInitialize", at = @At("HEAD"), cancellable = true)
    public void onWorldBorderInitialize(WorldBorderInitializeS2CPacket packet, CallbackInfo ci) {
        ci.cancel();
    }

    @Inject(method = "onWorldTimeUpdate", at = @At("HEAD"))
    public void onWorldTimeUpdate(WorldTimeUpdateS2CPacket packet, CallbackInfo ci) {
        BoxModClient.TRS.onWorldTimeUpdate();
    }

}
