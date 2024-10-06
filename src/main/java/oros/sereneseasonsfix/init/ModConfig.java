package oros.sereneseasonsfix.init;

import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import oros.sereneseasonsfix.config.ServerConfig;
import oros.sereneseasonsfix.core.Sereneseasonsfix;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class ModConfig {
    public static void init() {
        Path configPath = FMLPaths.CONFIGDIR.get();
        Path modConfigPath = Paths.get(configPath.toAbsolutePath().toString(), "sereneseasonsfix");

        try {
            Files.createDirectory(modConfigPath);
        } catch (FileAlreadyExistsException e) {
            Sereneseasonsfix.LOGGER.debug("Using existing file sereneseasonsfix config directory");
        } catch (IOException e) {
            Sereneseasonsfix.LOGGER.error("Failed to create sereneseasonsfix config directory", e);
        }

        ModLoadingContext.get().registerConfig(net.minecraftforge.fml.config.ModConfig.Type.SERVER, ServerConfig.SPEC, "sereneseasonsfix-server.toml");
    }
}
