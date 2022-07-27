package oros.sereneseasonfix;

import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("sereneseasonfix")
public class Sereneseasonfix {

    // Directly reference a log4j logger.
    public static final Logger LOGGER = LogManager.getLogger("sereneseasonfix");

    // Idk if I can just remove this class altogether, so imma just keep the bare minimum.
    public Sereneseasonfix() {
    }
}
