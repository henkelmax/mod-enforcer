package de.maxhenkel.modenforcer;

import de.maxhenkel.modenforcer.net.MessageModList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;

@OnlyIn(Dist.CLIENT)
public class ClientEvents {

    @SubscribeEvent
    public void joinEvent(EntityJoinWorldEvent event) {
        if (event.getEntity() != Minecraft.getInstance().player) {
            return;
        }
        ServerData serverData = Minecraft.getInstance().getCurrentServerData();
        if (serverData != null) {
            Main.SIMPLE_CHANNEL.sendToServer(new MessageModList(ModList.get().getMods()));
        }
    }

}
