package com.potatosaucevfx.mod.utils;

import com.potatosaucevfx.mod.core.Core;
import com.potatosaucevfx.mod.core.WhitelistUser;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WhitelistRead {

  private static JSONParser parser = new JSONParser();

  public static ArrayList getWhitelistUUIDs() {
    ArrayList<String> uuids = new ArrayList<String>();
    getWhitelistJson().forEach(emp -> parseToList((JSONObject) emp, uuids, "uuid"));
    return uuids;
  }

  public static ArrayList getWhitelistNames() {
    ArrayList<String> names = new ArrayList<String>();
    getWhitelistJson().forEach(emp -> parseToList((JSONObject) emp, names, "name"));
    return names;
  }

  public static ArrayList<WhitelistUser> getWhitelistUsers() {
    ArrayList<WhitelistUser> users = new ArrayList<WhitelistUser>();
    getWhitelistJson().forEach((user) -> {
      String uuid = ((JSONObject) user).get("uuid").toString();
      String name = ((JSONObject) user).get("name").toString();
      users.add(new WhitelistUser(uuid, name));
    });
    return users;
  }

  private static void parseToList(JSONObject whitelist, List list, String key) {
    list.add(whitelist.get(key));
  }

  private static JSONArray getWhitelistJson() {
    JSONArray whitelist = null;
    try {
      whitelist = (JSONArray) parser.parse(new FileReader(Core.SERVER_FILEPATH + "/whitelist.json"));
    } catch (FileNotFoundException e) {
        Core.logger.info("Whitelist.json is empty! :O");
    } catch (ParseException e) {
      //e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return whitelist;
  }

}
