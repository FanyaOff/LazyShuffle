package com.fanya.lazyshuffle.client;

import com.fanya.lazyshuffle.client.util.FileManager;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.MinecraftClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;

public class  LazyshuffleClient implements ClientModInitializer {
    public static final String MOD_ID = "lazyshuffle";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final Path CONFIG_DIR = MinecraftClient.getInstance().runDirectory.toPath().resolve("config/skinshuffle");
    @Override
    public void onInitializeClient() {
        LOGGER.info("Initializing LazyShuffleMod...");
        FileManager.initializeDirectories(CONFIG_DIR);
    }
}
