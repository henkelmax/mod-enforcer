package de.maxhenkel.modenforcer;

import de.maxhenkel.corelib.config.ConfigBase;
import net.minecraftforge.common.ForgeConfigSpec;

public class ServerConfig extends ConfigBase {

    public final ForgeConfigSpec.IntValue playerTimeout;

    public ServerConfig(ForgeConfigSpec.Builder builder) {
        super(builder);
        playerTimeout = builder
                .comment("The timeout for players to send their mod list on login")
                .comment("The player gets kicked if the timer runs out")
                .defineInRange("player_timeout", 5000, 100, Integer.MAX_VALUE);
    }

}
