package com.potatosaucevfx.wlsync.utils;

import com.potatosaucevfx.wlsync.core.Core;
import net.minecraftforge.common.config.Configuration;

import static net.minecraftforge.common.config.Configuration.CATEGORY_GENERAL;

public class ConfigHandler {

    public static String databasePath = "./whitelist.db";
    public static int serverSyncTimer = 60;
    public static int mode = 0;
    public static int serverListenerTimer = 10;

    public static void readConfig() {
        Configuration cfg = Core.config;
        try {
            cfg.load();
            initGeneralConfig(cfg);
        } catch (Exception e1) {
            Core.logger.error("Problem loading config file!");
        } finally {
            if (cfg.hasChanged()) {
                cfg.save();
            }
        }
    }

    private static void initGeneralConfig(Configuration cfg) {
        cfg.addCustomCategoryComment(CATEGORY_GENERAL, "General configuration");
        // cfg.getBoolean() will get the value in the config if it is already specified there. If not it will create the value.
        databasePath = cfg.getString("Database Path", CATEGORY_GENERAL, databasePath, "Insert System Path for your main database file. " +
                "This will be the same for all your servers you want to sync!");
        serverSyncTimer = cfg.getInt("Sync Timer", CATEGORY_GENERAL, serverSyncTimer, 5,  1000, "Time Interval in seconds for when the server polls " +
                "the whitelist changes from the database. (Only used in Interval Sync Mode!)");
        mode = cfg.getInt("Sync Mode", CATEGORY_GENERAL, mode, 0, 1, "Mode for how the database updates." +
                " 0 = Interval, 1 = Database Update Listener (Beta).");
        serverListenerTimer = cfg.getInt("Server Listener Sync Time", CATEGORY_GENERAL, serverListenerTimer, 1, 1000, "Time Interval in seconds for when the server checks for database changes (Only used in Database Update Listener Mode!)");

    }

}
