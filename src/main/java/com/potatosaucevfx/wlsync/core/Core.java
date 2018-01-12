package com.potatosaucevfx.wlsync.core;

import com.potatosaucevfx.wlsync.service.SQLiteService;
import com.potatosaucevfx.wlsync.commands.CommandWhitelist;
import com.potatosaucevfx.wlsync.service.JDBCService;
import com.potatosaucevfx.wlsync.utils.ConfigHandler;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.sqlite.JDBC;

import java.io.File;
import java.sql.DriverManager;

/**
 *
 * @author PotatoSauceVFX <rj@potatosaucevfx.com>
 */
@Mod(modid = Core.MODID, version = Core.VERSION, acceptableRemoteVersions = "*", serverSideOnly = true)
public class Core {

    public static final String MODID = "wlsync";
    public static final String VERSION = "3.1";
    public static final String LOG_PREFIX = "[Whitelist Sync " + VERSION + "] ";
    public static String SERVER_FILEPATH = "";
    public static Configuration config;

    // Services
    public JDBCService jDBCService;
    public SQLiteService sQliteService;

    public static final Logger logger = LogManager.getLogger(MODID);

    @EventHandler
    public void preInit(FMLPreInitializationEvent e) {

        try {
            DriverManager.registerDriver(new JDBC());
        } catch (Exception ee) {
            ee.printStackTrace();
        }

        File directory = e.getModConfigurationDirectory();
        config = new Configuration(new File(directory.getPath(), MODID + ".cfg"));
        ConfigHandler.readConfig();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {

        logger.info("[Whitelist Sync] Hello Minecraft!!!");

        if (ConfigHandler.whitelistMode.equalsIgnoreCase("SQLITE")) {
            sQliteService = new SQLiteService();
            sQliteService.setupDatabase();
        } else if (ConfigHandler.whitelistMode.equalsIgnoreCase("MYSQL")) {
            jDBCService = new JDBCService();
            jDBCService.setupDatabase();
        }

    }

    @SideOnly(Side.SERVER)
    @EventHandler
    public void serverLoad(FMLServerStartingEvent event) {
        SERVER_FILEPATH = event.getServer().getDataDirectory().getAbsolutePath();
        logger.info("------------------------------------------------");
        logger.info("------------------------------------------------");
        logger.info("------------------------------------------------");
        logger.info("Loading Commands");
        event.registerServerCommand(new CommandWhitelist(this));

        if (ConfigHandler.whitelistMode.equalsIgnoreCase("SQLITE")) {
            sQliteService.updateLocalWithDatabase(event.getServer());

            // Thread to update local files with database.
            Thread t = new Thread(new WhitelistWatcher(event.getServer(), this));
            t.start();
        } else if (ConfigHandler.whitelistMode.equalsIgnoreCase("MYSQL")) {
            // TODO: MYSQL
        }

        if (!event.getServer().getPlayerList().isWhiteListEnabled()) {
            event.getServer().getPlayerList().setWhiteListEnabled(true);
            Core.logger.info("Whitelist not enabled, doing it for you! ;)");
        }

        logger.info("------------------------------------------------");
        logger.info("------------------------------------------------");
        logger.info("------------------------------------------------");
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent e) {
        if (config.hasChanged()) {
            config.save();
        }
    }
}
