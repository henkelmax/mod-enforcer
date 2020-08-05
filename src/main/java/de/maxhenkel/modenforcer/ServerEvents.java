package de.maxhenkel.modenforcer;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ServerEvents {

    private static ServerEvents instance;

    private Map<UUID, Long> players;

    private ServerEvents() {
        players = new HashMap<>();
    }

    @SubscribeEvent
    public void onLogIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (!(event.getPlayer() instanceof ServerPlayerEntity)) {
            return;
        }
        ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();

        players.put(player.getUniqueID(), System.currentTimeMillis());
    }

    @SubscribeEvent
    public void onLogOut(PlayerEvent.PlayerLoggedOutEvent event) {
        if (!(event.getPlayer() instanceof ServerPlayerEntity)) {
            return;
        }
        ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();

        players.remove(player.getUniqueID());
    }

    @SubscribeEvent
    public void onTick(TickEvent.PlayerTickEvent event) {
        if (!event.phase.equals(TickEvent.Phase.END)) {
            return;
        }
        if (!(event.player instanceof ServerPlayerEntity)) {
            return;
        }
        ServerPlayerEntity player = (ServerPlayerEntity) event.player;

        Long timestamp = players.get(player.getUniqueID());
        if (timestamp == null) {
            return;
        }

        if (System.currentTimeMillis() - timestamp > Main.SERVER_CONFIG.playerTimeout.get().longValue()) {
            player.connection.disconnect(new StringTextComponent("Missing mods"));
        }
    }

    public void setPlayerAuthorized(PlayerEntity playerEntity) {
        players.remove(playerEntity.getUniqueID());
    }

    public static ServerEvents instance() {
        if (instance == null) {
            instance = new ServerEvents();
        }
        return instance;
    }

}
