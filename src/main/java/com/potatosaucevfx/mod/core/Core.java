package com.potatosaucevfx.mod.core;

import com.potatosaucevfx.mod.commands.CommandWhitelist;
import com.potatosaucevfx.mod.utils.Log;
import com.potatosaucevfx.mod.utils.WhitelistRead;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

import java.util.List;

/**
 * Created by PotatoSauceVFX on 7/27/2017.
 */

@Mod(modid = Core.MODID, version = Core.VERSION, acceptableRemoteVersions = "*", serverSideOnly = true)
public class Core {

    public static final String MODID = "wlsync";
    public static final String VERSION = "0.1";
    public static final String LOG_PREFIX = "[Whitelist Sync " + VERSION + "] ";
    public static String SERVER_FILEPATH = "";

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {

        Log.logln("[Whitelist Sync] Hello Minecraft!!!");
        Database.setupDatabase();

    }

    @Mod.EventHandler
    public void serverLoad(FMLServerStartingEvent event) {
        SERVER_FILEPATH = event.getServer().getDataDirectory().getAbsolutePath();
        Log.logln("Loading Commands");
        event.registerServerCommand(new CommandWhitelist());

        // Print current whitelist
        // read the whitelist into a list of POJOs
        WhitelistRead.getWhitelistUsers().forEach(user -> Log.logln(user.toString()));

        // start a file watcher for the whitelist file. Possible use to automate whitelist changes.
        Thread t = new Thread(new WhitelistWatcher());
        t.start();
    }
}
