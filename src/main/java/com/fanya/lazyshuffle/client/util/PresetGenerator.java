package com.fanya.lazyshuffle.client.util;

import com.fanya.lazyshuffle.client.LazyshuffleClient;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.GsonBuilder;
import org.jetbrains.annotations.NotNull;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.*;

public class PresetGenerator {
    private static final Logger LOGGER = LoggerFactory.getLogger(LazyshuffleClient.MOD_ID);

    public static void generatePresets(Path lazyShuffleDir, Path presetsFile) throws IOException {
        JsonObject root = new JsonObject();
        root.addProperty("chosenPreset", 0);
        root.addProperty("apiPreset", 0);

        JsonArray loadedPresets = new JsonArray();

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(lazyShuffleDir)) {
            for (Path file : stream) {
                if (Files.isRegularFile(file) && file.toString().endsWith(".png")) {
                    String fileName = file.getFileName().toString();
                    String skinName = fileName.replaceFirst("[.][^.]+$", "");
                    JsonObject preset = getJsonObject(skinName);

                    loadedPresets.add(preset);
                    LOGGER.info("Added skin: {}", skinName);
                }
            }
        }

        root.add("loadedPresets", loadedPresets);

        Files.write(presetsFile, new GsonBuilder().setPrettyPrinting().create().toJson(root).getBytes());
        LOGGER.info("New presets.json written to: {}", presetsFile);
    }

    private static @NotNull JsonObject getJsonObject(String skinName) {
        String model = skinName.toLowerCase().contains("_slim") ? "slim" : "default";

        JsonObject skinObject = new JsonObject();
        skinObject.addProperty("skin_name", "lazyshuffle/" + skinName);
        skinObject.addProperty("model", model);
        skinObject.addProperty("type", "skinshuffle:config");

        JsonObject preset = new JsonObject();
        preset.add("skin", skinObject);
        preset.addProperty("name", skinName);
        return preset;
    }
}
