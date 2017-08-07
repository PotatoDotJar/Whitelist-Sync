package com.potatosaucevfx.mod.core;

import java.io.IOException;
import java.nio.file.*;

import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;

public class WhitelistWatcher implements Runnable {

  private FileSystem fileSystem;
  private WatchService watcher;

  public WhitelistWatcher() {
    try {
      this.fileSystem = FileSystems.getDefault();
      this.watcher = fileSystem.newWatchService();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void run() {
    Path whitelist = fileSystem.getPath("./");
    try {
      whitelist.register(watcher, ENTRY_MODIFY);
    } catch (IOException e) {
      e.printStackTrace();
    }

    for (;;) {
      WatchKey key;
      try {
        key = watcher.take();
      } catch (InterruptedException x) {
        return;
      }

      for (WatchEvent<?> event : key.pollEvents()) {
        WatchEvent.Kind<?> kind = event.kind();
        System.out.println(kind.toString());
        System.out.println(event.context().toString());
      }

      // Reset the key -- this step is critical if you want to
      // receive further watch events.  If the key is no longer valid,
      // the directory is inaccessible so exit the loop.
      boolean valid = key.reset();
      if (!valid) {
        break;
      }
    }
  }

}
