package com.potatosaucevfx.wlsync.core;

import com.potatosaucevfx.wlsync.service.SQLiteService;
import com.potatosaucevfx.wlsync.utils.ConfigHandler;
import net.minecraft.server.MinecraftServer;

import java.io.IOException;
import java.nio.file.*;

import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

/**
 *
 * @author PotatoSauceVFX <rj@potatosaucevfx.com>
 */
public class WhitelistWatcher implements Runnable {

    private FileSystem fileSystem;
    private WatchService watcher;
    private MinecraftServer server;
    private Core core;

    public WhitelistWatcher(MinecraftServer server, Core core) {
        this.core = core;
        this.server = server;
    }

    @Override
    public void run() {
        if (core.sQliteService != null) {
            switch (ConfigHandler.mode) {
                // Interval Update
                case 0:
                    while (server.isServerRunning()) {
                        try {
                            long timeMillis = (ConfigHandler.serverSyncTimer) * 1000;
                            core.sQliteService.updateLocalWithDatabase(server);

                            Thread.sleep(timeMillis);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    break;

                // Listener Update
                case 1:
                    try {
                        this.fileSystem = FileSystems.getDefault();
                        this.watcher = fileSystem.newWatchService();

                        Path dataBasePath = fileSystem.getPath(ConfigHandler.databasePath.replace("whitelist.db", ""));
                        dataBasePath.register(watcher, ENTRY_MODIFY);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    while (server.isServerRunning()) {
                        WatchKey key;
                        try {
                            key = watcher.take();
                        } catch (InterruptedException x) {
                            return;
                        }

                        for (WatchEvent<?> event : key.pollEvents()) {
                            WatchEvent.Kind<?> kind = event.kind();

                            // Test if whitelist is changed
                            if (event.context().toString().equalsIgnoreCase("whitelist.db")) {
                                Core.logger.debug("Remote Database Updated... Syncing...");
                                core.sQliteService.updateLocalWithDatabase(server);
                            }
                        }

                        // Reset the key -- this step is critical if you want to
                        // receive further watch events.  If the key is no longer valid,
                        // the directory is inaccessible so exit the loop.
                        boolean valid = key.reset();
                        if (!valid) {
                            break;
                        }

                        try {
                            Thread.sleep(ConfigHandler.serverListenerTimer * 1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }
                    break;
            }
        }

    }

}
