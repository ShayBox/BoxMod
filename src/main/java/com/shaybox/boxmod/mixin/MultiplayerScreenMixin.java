package com.shaybox.boxmod.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerServerListWidget;
import net.minecraft.client.option.ServerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MultiplayerScreen.class)
public class MultiplayerScreenMixin {

    @Shadow
    protected MultiplayerServerListWidget serverListWidget;

    @Shadow
    private ServerList serverList;

    @Inject(method = "init", at = @At("RETURN"))
    private void init(CallbackInfo ci) {
        new Thread(this::run).start();
    }

    private void run() {
        MinecraftClient mc = MinecraftClient.getInstance();

        while (mc.currentScreen instanceof MultiplayerScreen) {
            try {
                this.serverList.loadFile();
                this.serverListWidget.setServers(serverList);
                Thread.sleep(30 * 1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

}