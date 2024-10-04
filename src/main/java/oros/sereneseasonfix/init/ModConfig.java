package oros.sereneseasonfix.init;

import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import oros.sereneseasonfix.config.ServerConfig;
import oros.sereneseasonfix.core.Sereneseasonfix;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ModConfig {
    public static void init()
    {
        Path configPath = FMLPaths.CONFIGDIR.get();
        Path modConfigPath = Paths.get(configPath.toAbsolutePath().toString(), "sereneseasonfix");

        try
        {
            Files.createDirectory(modConfigPath);
        }
        catch (FileAlreadyExistsException e) {
            Sereneseasonfix.LOGGER.debug("Using existing file sereneseasonfix config directory");
        }
        catch (IOException e)
        {
            Sereneseasonfix.LOGGER.error("Failed to create sereneseasonfix config directory", e);
        }

        ModLoadingContext.get().registerConfig(net.minecraftforge.fml.config.ModConfig.Type.SERVER, ServerConfig.SPEC, "sereneseasonfix-server.toml");
    }
}
