package com.potatosaucevfx.mod.utils;

import com.potatosaucevfx.mod.core.Core;
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

    public static List getWhitelistUUIDs() {
        List<String> uuids = new ArrayList<String>();

        JSONParser jsonParser = new JSONParser();
        try {
            FileReader reader = new FileReader(Core.SERVER_FILEPATH  + "/whitelist.json");
            Object obj = jsonParser.parse(reader);
            JSONArray players = (JSONArray) obj;
            players.forEach(emp -> parseToList((JSONObject) emp, uuids, "uuid"));



        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return uuids;
    }

    public static List getWhitelistNames() {
        List<String> names = new ArrayList<String>();

        JSONParser jsonParser = new JSONParser();
        try {
            FileReader reader = new FileReader(Core.SERVER_FILEPATH  + "/whitelist.json");
            Object obj = jsonParser.parse(reader);
            JSONArray players = (JSONArray) obj;
            players.forEach(emp -> parseToList((JSONObject) emp, names, "name"));



        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return names;
    }

    private static void parseToList(JSONObject whitelist, List list, String key) {
        list.add(whitelist.get(key));
    }
}
