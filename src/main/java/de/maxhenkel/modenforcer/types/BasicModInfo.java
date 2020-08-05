package de.maxhenkel.modenforcer.types;

import net.minecraftforge.fml.loading.moddiscovery.ModInfo;

public class BasicModInfo {

    private String name;
    private String modid;
    private String version;

    public BasicModInfo(ModInfo modInfo) {
        this.name = modInfo.getDisplayName();
        this.modid = modInfo.getModId();
        this.version = modInfo.getVersion().getQualifier();
    }

    public BasicModInfo(String name, String modid, String version) {
        this.name = name;
        this.modid = modid;
        this.version = version;
    }

    public String getName() {
        if (name == null) {
            return modid;
        }
        return name;
    }

    public String getModid() {
        return modid;
    }

    public String getVersion() {
        if (version == null) {
            return "";
        }
        return version;
    }

}
