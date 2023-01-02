package com.shaybox.boxmod.helper;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class KeyBindings {

    public static final KeyBinding hudKey = new KeyBinding("boxmod.hud", GLFW.GLFW_KEY_H, "BoxMod");
    public static final KeyBinding flightKey = new KeyBinding("boxmod.flight", GLFW.GLFW_KEY_G, "BoxMod");

    public static void register() {
        KeyBinding[] keyBindings = new KeyBinding[] {hudKey, flightKey};
        for (KeyBinding keyBinding : keyBindings) {
            KeyBindingHelper.registerKeyBinding(keyBinding);
        }
    }

    public static boolean hud = true;
    public static boolean flight = false;

    public static void tick(MinecraftClient mc) {
        if (mc.player == null) return;

        while (hudKey.wasPressed()) {
            hud = !hud;
            mc.player.sendMessage(Text.translatable("boxmod.hud." + flight), false);
        }

        while (flightKey.wasPressed()) {
            flight = !flight;
            mc.player.sendMessage(Text.translatable("boxmod.flight." + flight), false);
            mc.player.getAbilities().allowFlying = flight;
            mc.player.sendAbilitiesUpdate();
        }
    }

}
