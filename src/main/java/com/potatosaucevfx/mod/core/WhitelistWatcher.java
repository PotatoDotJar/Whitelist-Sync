package com.potatosaucevfx.mod.core;

import com.potatosaucevfx.mod.utils.ConfigHandler;
import com.potatosaucevfx.mod.utils.Log;
import net.minecraft.server.MinecraftServer;

import java.io.IOException;
import java.nio.file.*;

import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;

public class WhitelistWatcher implements Runnable {

  private FileSystem fileSystem;
  private WatchService watcher;
  private MinecraftServer server;

  public WhitelistWatcher(MinecraftServer server) {
    this.server = server;
  }

  @Override
  public void run() {

    switch(ConfigHandler.mode) {
      // Interval Update
      case 0:
        while(server.isServerRunning()) {
          try {
            long timeMillis = (ConfigHandler.serverSyncTimer)*1000;
            Database.updateLocalWithDatabase(server);

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

          Path dataBasePath = fileSystem.getPath(ConfigHandler.databasePath);
          dataBasePath.register(watcher, ENTRY_MODIFY);

        } catch (IOException e) {
          e.printStackTrace();
        }

        while(server.isServerRunning()) {
          WatchKey key;
          try {
            key = watcher.take();
          } catch (InterruptedException x) {
            return;
          }

          for (WatchEvent<?> event : key.pollEvents()) {
            WatchEvent.Kind<?> kind = event.kind();

            // Test if whitelist is changed
            if(event.context().toString().equalsIgnoreCase(ConfigHandler.databasePath)) {
              Log.logln("Database Updated... Syncing...");
              Database.updateLocalWithDatabase(server);
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
            Thread.sleep(ConfigHandler.serverListenerTimer*1000);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }

        }
        break;

    }

  }

}
