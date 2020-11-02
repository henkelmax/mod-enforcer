package de.maxhenkel.modenforcer.net;

import de.maxhenkel.corelib.net.Message;
import de.maxhenkel.modenforcer.ServerEvents;
import de.maxhenkel.modenforcer.types.BasicModInfo;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.moddiscovery.ModInfo;
import net.minecraftforge.fml.network.NetworkEvent;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MessageModList implements Message<MessageModList> {

    private List<BasicModInfo> mods;

    public MessageModList() {

    }

    public MessageModList(List<ModInfo> mods) {
        this.mods = mods.stream().map(BasicModInfo::new).collect(Collectors.toList());
    }

    @Override
    public Dist getExecutingSide() {
        return Dist.DEDICATED_SERVER;
    }

    @Override
    public void executeServerSide(NetworkEvent.Context context) {
        List<Pair<BasicModInfo, BasicModInfo>> mismatches = new ArrayList<>();
        for (ModInfo i : ModList.get().getMods()) {
            BasicModInfo basicModInfo = new BasicModInfo(i);
            BasicModInfo matchedInfo = mods.stream().filter(info -> info.getModid().equals(basicModInfo.getModid())).findFirst().orElse(null);
            if (matchedInfo == null) {
                mismatches.add(new ImmutablePair<>(basicModInfo, null));
            } else {
                if (!matchedInfo.getVersion().equals(basicModInfo.getVersion())) {
                    mismatches.add(new ImmutablePair<>(basicModInfo, matchedInfo));
                }
            }

        }

        if (mismatches.isEmpty()) {
            ServerEvents.instance().setPlayerAuthorized(context.getSender());
            return;
        }

        IFormattableTextComponent text = new TranslationTextComponent("message.modenforcer.mismatching_mods");
        text.appendString("\n");

        for (Pair<BasicModInfo, BasicModInfo> pair : mismatches) {
            text.appendString("\n");
            ITextComponent comp;
            if (pair.getRight() == null) {
                comp = new TranslationTextComponent("message.modenforcer.missing_mod").mergeStyle(TextFormatting.RED);
            } else {
                comp = new TranslationTextComponent("message.modenforcer.version_mismatch", new StringTextComponent(pair.getRight().getVersion()).mergeStyle(TextFormatting.RED), new StringTextComponent(pair.getLeft().getVersion()).mergeStyle(TextFormatting.GREEN)).mergeStyle(TextFormatting.GRAY);
            }

            text.append(new TranslationTextComponent("message.modenforcer.mismatched_mod", new StringTextComponent(pair.getLeft().getName()).mergeStyle(TextFormatting.WHITE), comp).mergeStyle(TextFormatting.GRAY));
        }

        context.getSender().connection.disconnect(text);
    }

    @Override
    public MessageModList fromBytes(PacketBuffer buf) {
        int count = buf.readInt();
        mods = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            mods.add(new BasicModInfo(buf.readString(256), buf.readString(256), buf.readString(256)));
        }
        return this;
    }

    @Override
    public void toBytes(PacketBuffer buf) {
        buf.writeInt(mods.size());

        for (BasicModInfo modInfo : mods) {
            buf.writeString(modInfo.getName());
            buf.writeString(modInfo.getModid());
            buf.writeString(modInfo.getVersion());
        }
    }

}
