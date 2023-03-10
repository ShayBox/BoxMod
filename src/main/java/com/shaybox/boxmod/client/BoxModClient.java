package com.shaybox.boxmod.client;

import com.shaybox.boxmod.helper.Hud;
import com.shaybox.boxmod.helper.KeyBindings;
import com.shaybox.boxmod.helper.TickRateService;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;

@Environment(EnvType.CLIENT)
public class BoxModClient implements ClientModInitializer {

    public static TickRateService TRS = new TickRateService();

    @Override
    public void onInitializeClient() {
        KeyBindings.register();
        HudRenderCallback.EVENT.register(Hud::onRender);
        ScreenEvents.AFTER_INIT.register(Hud::afterInit);
        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> TRS.onPlayDisconnect());
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> TRS.onPlayReady());
        ClientTickEvents.END_CLIENT_TICK.register(KeyBindings::tick);
    }

}
