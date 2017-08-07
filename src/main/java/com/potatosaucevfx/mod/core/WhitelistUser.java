package com.potatosaucevfx.mod.core;

public class WhitelistUser {

  private final String uuid;
  private final String name;

  public WhitelistUser(String uuid, String name) {
    this.uuid = uuid;
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public String getUuid() {
    return uuid;
  }

  public String toString() {
    return this.uuid + ":" + this.name;
  }

}
