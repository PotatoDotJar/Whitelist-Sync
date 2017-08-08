package com.potatosaucevfx.mod.core;

import com.potatosaucevfx.mod.commands.CommandWhitelist;
import com.potatosaucevfx.mod.utils.ConfigHandler;
import com.potatosaucevfx.mod.utils.Log;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

import java.io.File;

/**
 * Created by PotatoSauceVFX on 7/27/2017.
 */

@Mod(modid = Core.MODID, version = Core.VERSION, acceptableRemoteVersions = "*", serverSideOnly = true)
public class Core {

    public static final String MODID = "wlsync";
    public static final String VERSION = "1.0";
    public static final String LOG_PREFIX = "[Whitelist Sync " + VERSION + "] ";
    public static String SERVER_FILEPATH = "";
    public static Configuration config;

    @EventHandler
    public void preInit(FMLPreInitializationEvent e) {
        File directory = e.getModConfigurationDirectory();
        config = new Configuration(new File(directory.getPath(), MODID + ".cfg"));
        ConfigHandler.readConfig();
    }



    @EventHandler
    public void init(FMLInitializationEvent event) {

        Log.logln("[Whitelist Sync] Hello Minecraft!!!");
        Database.setupDatabase();

    }

    @EventHandler
    public void serverLoad(FMLServerStartingEvent event) {
        SERVER_FILEPATH = event.getServer().getDataDirectory().getAbsolutePath();
        Log.logln("------------------------------------------------");
        Log.logln("------------------------------------------------");
        Log.logln("------------------------------------------------");
        Log.logln("Loading Commands");
        event.registerServerCommand(new CommandWhitelist());
        Database.updateLocalWithDatabase(event.getServer());

        if(!event.getServer().getPlayerList().isWhiteListEnabled()) {
            event.getServer().getPlayerList().setWhiteListEnabled(true);
            Log.logln("Whitelist not enabled, doing it for you! ;)");
        }

        // Thread to update local files with database.
        Thread t = new Thread(new WhitelistWatcher(event.getServer()));
        t.start();
        Log.logln("------------------------------------------------");
        Log.logln("------------------------------------------------");
        Log.logln("------------------------------------------------");
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent e) {
        if (config.hasChanged()) {
            config.save();
        }
    }
}
