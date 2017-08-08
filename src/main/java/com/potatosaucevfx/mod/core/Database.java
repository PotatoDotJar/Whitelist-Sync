package com.potatosaucevfx.mod.core;

import com.mojang.authlib.GameProfile;
import com.potatosaucevfx.mod.utils.ConfigHandler;
import com.potatosaucevfx.mod.utils.Log;
import com.potatosaucevfx.mod.utils.WhitelistRead;
import net.minecraft.server.MinecraftServer;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.UUID;

public class Database {

    public static void setupDatabase() {
        Connection conn = null;
        File database = new File(ConfigHandler.databasePath);

        if(!database.exists()) {
            createNewDatabase();
        }
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:" + ConfigHandler.databasePath);
            System.out.println("Connected to Database successfully!");

            // SQL statement for creating a new table
            String sql = "CREATE TABLE IF NOT EXISTS whitelist (\n"
                    + "	uuid text NOT NULL PRIMARY KEY,\n"
                    + "	name text,\n"
                    + " whitelisted integer NOT NULL\n"
                    + ");";
            Statement stmt = conn.createStatement();
            stmt.execute(sql);

        } catch (SQLException e) {
            Log.logln(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                Log.logln(ex.getMessage());
            }
        }
    }


    // Writes data from local whitelist to database.
    public static void pushLocalToDatabase(MinecraftServer server) {
        ArrayList<String> uuids = WhitelistRead.getWhitelistUUIDs();
        ArrayList<String> names = WhitelistRead.getWhitelistNames();

        new Thread(new Runnable() {
            @Override
            public void run() {
                int records = 0;
                try {
                    Connection conn = DriverManager.getConnection("jdbc:sqlite:" + ConfigHandler.databasePath);
                    Statement stmt = conn.createStatement();
                    long startTime = System.currentTimeMillis();

                    for(int i = 0; i < uuids.size() || i < names.size(); i++) {
                        if((uuids.get(i) != null) && (names.get(i) != null)) {
                            String sql = "INSERT OR REPLACE INTO whitelist(uuid, name, whitelisted) VALUES (\'" + uuids.get(i) + "\', \'" + names.get(i) + "\', 1);";
                            stmt.execute(sql);
                            records++;
                        }
                    }
                    long timeTaken = System.currentTimeMillis() - startTime;

                    Log.logln("Database Updated | Took " + timeTaken + "ms | Wrote " + records + " records.");


                    stmt.close();
                    conn.close();
                } catch (SQLException e) {
                    Log.logln(e.getMessage());

                }
            }
        }).start();
    }
    public static ArrayList<String> pullUuidsFromDatabase(MinecraftServer server) {
        ArrayList<String> uuids = new ArrayList<String>();

        try {
            int records = 0;
            Connection conn = DriverManager.getConnection("jdbc:sqlite:" + ConfigHandler.databasePath);
            Statement stmt = conn.createStatement();
            String sql = "SELECT uuid, whitelisted FROM whitelist;";

            long startTime = System.currentTimeMillis();

            stmt.execute(sql);
            ResultSet rs = stmt.executeQuery(sql);

            while(rs.next()) {
                if(rs.getInt("whitelisted") == 1) {
                    uuids.add(rs.getString("uuid"));
                }
                records++;
            }
            long timeTaken = System.currentTimeMillis() - startTime;
            Log.logln("Database Pulled | Took " + timeTaken + "ms | Read " + records + " records.");

            rs = null;
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            Log.logln(e.getMessage());
        }
        return uuids;

    }

    public static ArrayList<String> pullNamesFromDatabase(MinecraftServer server) {
        ArrayList<String> names = new ArrayList<String>();
        try {
            int records = 0;
            Connection conn = DriverManager.getConnection("jdbc:sqlite:" + ConfigHandler.databasePath);
            Statement stmt = conn.createStatement();
            String sql = "SELECT name, whitelisted FROM whitelist;";

            long startTime = System.currentTimeMillis();

            stmt.execute(sql);
            ResultSet rs = stmt.executeQuery(sql);

            while(rs.next()) {

                if(rs.getInt("whitelisted") == 1) {
                    names.add(rs.getString("name"));
                }

                records++;
            }

            long timeTaken = System.currentTimeMillis() - startTime;
            Log.logln("Database Pulled | Took " + timeTaken + "ms | Read " + records + " records.");

            rs = null;
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            Log.logln(e.getMessage());
        }
        return names;

    }


    public static void addPlayertoDataBase(GameProfile player) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Connection conn = DriverManager.getConnection("jdbc:sqlite:" + ConfigHandler.databasePath);
                    Statement stmt = conn.createStatement();
                    String sql = "INSERT OR REPLACE INTO whitelist(uuid, name, whitelisted) VALUES (\'" + player.getId() + "\', \'" + player.getName() + "\', 1);";

                    long startTime = System.currentTimeMillis();

                    stmt.execute(sql);

                    long timeTaken = System.currentTimeMillis() - startTime;
                    Log.logln("Database Added " + player.getName() + " | Took " + timeTaken + "ms");

                    stmt.close();
                    conn.close();
                } catch (SQLException e) {
                    Log.logln(e.getMessage());
                }
            }
        }).start();
    }

    public static void removePlayerFromDataBase(GameProfile player) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Connection conn = DriverManager.getConnection("jdbc:sqlite:" + ConfigHandler.databasePath);
                    Statement stmt = conn.createStatement();
                    String sql = "INSERT OR REPLACE INTO whitelist(uuid, name, whitelisted) VALUES (\'" + player.getId() + "\', \'" + player.getName() + "\', 0);";

                    long startTime = System.currentTimeMillis();

                    stmt.execute(sql);

                    long timeTaken = System.currentTimeMillis() - startTime;
                    Log.logln("Database Removed " + player.getName() + " | Took " + timeTaken + "ms");

                    stmt.close();
                    conn.close();
                } catch (SQLException e) {
                    Log.logln(e.getMessage());
                }
            }
        }).start();
    }

    public static void updateLocalWithDatabase(MinecraftServer server) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    int records = 0;
                    Connection conn = DriverManager.getConnection("jdbc:sqlite:" + ConfigHandler.databasePath);
                    Statement stmt = conn.createStatement();
                    String sql = "SELECT name, uuid, whitelisted FROM whitelist;";

                    long startTime = System.currentTimeMillis();

                    stmt.execute(sql);
                    ResultSet rs = stmt.executeQuery(sql);

                    ArrayList<String> localUuids = WhitelistRead.getWhitelistUUIDs();
                    while (rs.next()) {
                        GameProfile player = new GameProfile(UUID.fromString(rs.getString("uuid")), rs.getString("name"));

                        if (rs.getInt("whitelisted") == 1) {
                            if (!localUuids.contains(rs.getString("uuid"))) {
                                try {
                                    server.getPlayerList().addWhitelistedPlayer(player);

                                } catch (NullPointerException e) {
                                    e.printStackTrace();
                                }
                            }
                        } else {
                            Log.logln(rs.getString("uuid") + " is NOT whitelisted.");
                            if (localUuids.contains(rs.getString("uuid"))) {
                                server.getPlayerList().removePlayerFromWhitelist(player);

                                Log.logln("Removed player " + rs.getString("name"));
                            }
                        }

                        records++;
                    }
                    long timeTaken = System.currentTimeMillis() - startTime;
                    Log.logln("Database Pulled | Took " + timeTaken + "ms | Wrote " + records + " records.");
                    Log.logln("Local whitelist.json up to date!");

                    rs = null;
                    stmt.close();
                    conn.close();
                } catch(SQLException e){
                    Log.logln(e.getMessage());
                }
            }
        }).start();



    }


    private static void createNewDatabase() {
        String url = "jdbc:sqlite:" + ConfigHandler.databasePath;
        try {
            Connection conn = DriverManager.getConnection(url);
            if(conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                Log.logln("A new database \"" + ConfigHandler.databasePath +"\" has been created.");
            }
        } catch (SQLException e) {
            Log.logln(e.getMessage());
        }
    }
}