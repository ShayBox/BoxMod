package com.shaybox.boxmod.helper;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.shaybox.boxmod.client.BoxModClient;
import com.shaybox.boxmod.mixin.MultiplayerScreenAccessor;
import net.fabricmc.fabric.api.client.screen.v1.Screens;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.client.option.ServerList;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class Hud {

    public static void onRender(MatrixStack matrixStack, float ignoredTickDelta) {
        MinecraftClient mc = MinecraftClient.getInstance();

        String[] lines = {
                Formatting.RED + "BoxMod v4.2.0-69" + Formatting.WHITE + " by " + Formatting.AQUA + "Shayne Hartford (ShayBox)",
                Formatting.RED + "FPS: " + getFPS(),
                Formatting.RED + "TPS: " + getTPS(),
        };

        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            int y = mc.textRenderer.fontHeight * i;
            mc.textRenderer.drawWithShadow(matrixStack, line, 2, y + 2, 0);
        }
    }

    private static String getFPS() {
        MinecraftClient mc = MinecraftClient.getInstance();
        int fps = mc.getCurrentFps();

        Formatting format = Formatting.GREEN;
        if (fps < 120) format = Formatting.YELLOW;
        if (fps < 60) format = Formatting.GOLD;
        if (fps < 30) format = Formatting.RED;

        return format + String.valueOf(fps);
    }

    private static String getTPS() {
        double tps = BoxModClient.TRS.getTickrate();

        Formatting format = Formatting.GREEN;
        if (tps < 18) format = Formatting.YELLOW;
        if (tps < 14) format = Formatting.GOLD;
        if (tps < 10) format = Formatting.RED;

        return format + String.format("%.2f", tps);
    }

    public static void afterInit(MinecraftClient client, Screen screen, int scaledWidth, int scaledHeight) {
        if (screen instanceof MultiplayerScreen) {
            ButtonWidget buttonWidget = buildSigiloButtonWidget(screen);
            Screens.getButtons(screen).add(buttonWidget);
        }
    }

    private static ButtonWidget buildSigiloButtonWidget(Screen screen) {
        return ButtonWidget.builder(Text.literal("Sig"), (button) -> {
            try {
                URL url = new URL("https://api.shaybox.com/sigilo/random");
                URLConnection connection = url.openConnection();
                connection.connect();

                Object content = connection.getContent();
                InputStreamReader inputReader = new InputStreamReader((InputStream) content);
                JsonObject json = JsonParser.parseReader(inputReader).getAsJsonObject();

                String host = json.get("host").getAsString();
                String port = json.get("port").getAsString();
                String address = host + ":" + port;

                MultiplayerScreen mpScreen = (MultiplayerScreen) screen;
                ServerList serverList = mpScreen.getServerList();
                ServerInfo serverInfo = new ServerInfo(address, address, false);
                serverList.add(serverInfo, false);
                serverList.saveFile();

                MultiplayerScreenAccessor mpScreenAccessor = (MultiplayerScreenAccessor) screen;
                mpScreenAccessor.setServerList(serverList);
                Screen parent = mpScreenAccessor.getParent();

                MinecraftClient mc = MinecraftClient.getInstance();
                mc.setScreen(new MultiplayerScreen(parent));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).dimensions(screen.width / 2 + 155, screen.height - 28, 20, 20).build();
    }

}