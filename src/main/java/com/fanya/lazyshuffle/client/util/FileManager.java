package com.fanya.lazyshuffle.client.util;

import com.fanya.lazyshuffle.client.LazyshuffleClient;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.*;
import org.slf4j.Logger;

import static com.fanya.lazyshuffle.client.LazyshuffleClient.CONFIG_DIR;

public class FileManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(LazyshuffleClient.MOD_ID);

    public static void initializeDirectories(Path configDir) {
        try {
            Files.createDirectories(getLazyShuffleDir(configDir));
            Files.createDirectories(getBackupDir(configDir));
            LOGGER.info("Directories initialized.");
        } catch (IOException e) {
            LOGGER.error("Failed to initialize directories: {}", e.getMessage());
            e.printStackTrace();
        }
    }

    public static Path getLazyShuffleDir(Path configDir) {
        return configDir.resolve("skins").resolve("lazyshuffle");
    }

    public static Path getBackupDir(Path configDir) {
        return configDir.resolve("backups");
    }

    public static Path getPresetsFile(Path configDir) {
        return configDir.resolve("presets.json");
    }

    public static void createBackup(Path presetsFile, Path backupDir) throws IOException {
        if (!Files.exists(presetsFile)) {
            LOGGER.info("presets.json not found, skipping backup.");
            return;
        }
        String backupName = "presets_backup_" + System.currentTimeMillis() + ".json";
        Path backupPath = backupDir.resolve(backupName);
        Files.copy(presetsFile, backupPath, StandardCopyOption.REPLACE_EXISTING);
        LOGGER.info("Backup created at: {}", backupPath);
    }
}