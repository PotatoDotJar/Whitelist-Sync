package com.potatosaucevfx.mod.utils;

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

    List<String> uuids = new ArrayList<String>();
    List<String> names = new ArrayList<String>();

    public WhitelistRead() {
        JSONParser jsonParser = new JSONParser();
        try {
            FileReader reader = new FileReader("whitelist.json");
            Object obj = jsonParser.parse(reader);
            JSONArray players = (JSONArray) obj;
            players.forEach(emp -> parsePlayers((JSONObject) emp));



        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static void parsePlayers(JSONObject whitelist)
    {
        //Get uuid
        String uuid = (String) whitelist.get("uuid");
        System.out.println(uuid);
        //Get name
        String name = (String) whitelist.get("name");
        System.out.println(name + "\n");

    }
}
