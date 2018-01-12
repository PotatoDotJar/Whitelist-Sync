package com.potatosaucevfx.wlsync.utils;

import com.potatosaucevfx.wlsync.core.Core;
import net.minecraftforge.common.config.Configuration;

import static net.minecraftforge.common.config.Configuration.CATEGORY_GENERAL;

/**
 *
 * @author PotatoSauceVFX <rj@potatosaucevfx.com>
 */
public class ConfigHandler {

    // Custom Categories
    public final static String MYSQL_CATEGORY = "mySQL";
    public final static String SQLITE_CATEGORY = "sqlite";

    // General settings
    public static String whitelistMode = "SQLITE";

    // sqlite config
    public static String databasePath = "./whitelist.db";
    public static int serverSyncTimer = 60;
    public static int mode = 0;
    public static int serverListenerTimer = 10;

    // mySQL config
    public static String mySQL_IP = "localhost";
    public static String mySQL_PORT = "3306";
    public static String mySQL_Username = "root";
    public static String mySQL_Password = "password";

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
        whitelistMode = cfg.getString("Whitelist Sync Mode", CATEGORY_GENERAL, whitelistMode,
                "Mode for the database. Options are [MYSQL or SQLITE]. Default is SQLITE!");

        databasePath = cfg.getString("Database Path", SQLITE_CATEGORY,
                databasePath, "Insert System Path for your main database file. "
                + "This will be the same for all your servers you want to sync!");

        serverSyncTimer = cfg.getInt("Sync Timer", SQLITE_CATEGORY,
                serverSyncTimer, 5, 1000, "Time Interval in seconds for when the server polls "
                + "the whitelist changes from the database. (Only used in Interval Sync Mode!)");

        mode = cfg.getInt("Sqlite Sync Mode", SQLITE_CATEGORY, mode, 0, 1,
                "Mode for how the database updates."
                + " 0 = Interval, 1 = Database Update Listener (Beta).");

        serverListenerTimer = cfg.getInt("Server Listener Sync Time",
                SQLITE_CATEGORY, serverListenerTimer, 1, 1000,
                "Time Interval in seconds for when the server checks for"
                + " database changes (Only used in Database Update "
                + "Listener Mode!)");

        cfg.addCustomCategoryComment(MYSQL_CATEGORY, "mySQL configuration (To enable "
                + "mySQL, refer to the mode setting in the general configuration).");

        mySQL_IP = cfg.getString("mySQL IP", MYSQL_CATEGORY, mySQL_IP,
                "IP for your mySQL server (Example: localhost) Note: Do not add schema.");

        mySQL_PORT = cfg.getString("mySQL Port", MYSQL_CATEGORY, mySQL_PORT, "Port for your mySQL server.");

        mySQL_Username = cfg.getString("mySQL Username", MYSQL_CATEGORY, mySQL_Username, "Username for your mySQL server.");

        mySQL_Password = cfg.getString("mySQL Password", MYSQL_CATEGORY, mySQL_Password, "Password for your mySQL server.");
    }

}
