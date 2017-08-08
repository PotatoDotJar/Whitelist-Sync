package com.potatosaucevfx.mod.utils;

import com.potatosaucevfx.mod.core.Core;
import net.minecraftforge.common.config.Configuration;

import static net.minecraftforge.common.config.Configuration.CATEGORY_GENERAL;

public class ConfigHandler {

    public static String databasePath = "/";

    public static void readConfig() {
        Configuration cfg = Core.config;
        try {
            cfg.load();
            initGeneralConfig(cfg);
        } catch (Exception e1) {
            Log.logln("Problem loading config file!");
        } finally {
            if (cfg.hasChanged()) {
                cfg.save();
            }
        }
    }

    private static void initGeneralConfig(Configuration cfg) {
        cfg.addCustomCategoryComment(CATEGORY_GENERAL, "General configuration");
        // cfg.getBoolean() will get the value in the config if it is already specified there. If not it will create the value.
        databasePath = cfg.getString("goodTutorial", CATEGORY_GENERAL, databasePath, "Insert System Path for your main database file. " +
                "This will be the same for all your servers you want to sync!");
    }

}
