package de.maxhenkel.modenforcer;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class Config {

    public static final ServerConfig SERVER;
    public static final ForgeConfigSpec SERVER_SPEC;

    public static ForgeConfigSpec.IntValue PLAYER_TIMEOUT;

    static {
        Pair<ServerConfig, ForgeConfigSpec> specPairServer = new ForgeConfigSpec.Builder().configure(ServerConfig::new);
        SERVER_SPEC = specPairServer.getRight();
        SERVER = specPairServer.getLeft();
    }

    public static class ServerConfig {

        public ServerConfig(ForgeConfigSpec.Builder builder) {
            PLAYER_TIMEOUT = builder.comment("The timeout for players to send their mod list on login").comment("The player gets kicked if the timer runs out").defineInRange("player_timeout", 5000, 100, Integer.MAX_VALUE);
        }
    }
}
