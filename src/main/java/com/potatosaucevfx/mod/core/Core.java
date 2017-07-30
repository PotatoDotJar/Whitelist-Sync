package com.potatosaucevfx.mod.core;

import com.potatosaucevfx.mod.commands.CommandDisplayMessage;
import com.potatosaucevfx.mod.utils.Log;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

/**
 * Created by PotatoSauceVFX on 7/27/2017.
 */

@Mod(modid = Core.MODID, version = Core.VERSION, acceptableRemoteVersions = "*", serverSideOnly = true)
public class Core {

    public static final String MODID = "wlsync";
    public static final String VERSION = "0.1";
    public static final String LOG_PREFIX = "[Whitelist Sync " + VERSION + "] ";

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {

        Log.logln("[Whitelist Sync] Hello Minecraft!!!");
    }

    @Mod.EventHandler
    public void serverLoad(FMLServerStartingEvent event) {
        Log.logln("[Whitelist Sync] Loading Commands");
        event.registerServerCommand(new CommandDisplayMessage());
    }
}
