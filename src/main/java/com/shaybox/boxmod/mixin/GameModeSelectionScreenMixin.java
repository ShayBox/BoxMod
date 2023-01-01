package com.shaybox.boxmod.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.GameModeSelectionScreen;
import net.minecraft.client.gui.screen.GameModeSelectionScreen.GameModeSelection;
import net.minecraft.world.GameMode;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(GameModeSelectionScreen.class)
public class GameModeSelectionScreenMixin {

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    @Inject(method = "apply(Lnet/minecraft/client/MinecraftClient;Ljava/util/Optional;)V", at = @At("HEAD"), cancellable = true)
    private static void apply(MinecraftClient client, Optional<GameModeSelection> gameModeSelection, CallbackInfo ci) {
        if (client.interactionManager != null && client.player != null && gameModeSelection.isPresent()) {
            if (!client.player.hasPermissionLevel(2)) {
                GameMode gameMode = of(gameModeSelection.get());
                client.interactionManager.setGameMode(gameMode);
                ci.cancel();
            }
        }
    }

    private static GameMode of(GameModeSelection gameModeSelection) {
        return switch (gameModeSelection) {
            case SPECTATOR -> GameMode.SPECTATOR;
            case SURVIVAL -> GameMode.SURVIVAL;
            case CREATIVE -> GameMode.CREATIVE;
            case ADVENTURE -> GameMode.ADVENTURE;
        };
    }

}
