package com.fanya.lazyshuffle.client.gui.widgets;

import com.fanya.lazyshuffle.client.gui.AlertScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class UpdateSkinsButton extends ButtonWidget {
    public static final Logger LOGGER = LoggerFactory.getLogger("LazySkinShuffle");

    public UpdateSkinsButton(int x, int y, int width, int height) {
        super(x, y, width, height,
                Text.translatable("lazyshuffle.button.update"),
                (btn) -> {
                    AlertScreen.currentScreen = MinecraftClient.getInstance().currentScreen;
                    MinecraftClient.getInstance().setScreen(new AlertScreen(MinecraftClient.getInstance().currentScreen));
                },textSupplier -> Text.translatable("lazyshuffle.button.tooltip"));
    }


}