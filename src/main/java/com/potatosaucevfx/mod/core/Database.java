package com.potatosaucevfx.mod.core;

import com.mojang.authlib.GameProfile;
import com.potatosaucevfx.mod.utils.Log;
import net.minecraft.server.management.UserListWhitelistEntry;

import java.io.File;
import java.sql.*;

public class Database {

    public static String dataBasePath = "whitelist.db";

    public static void setupDatabase() {
        Connection conn = null;
        // TODO: Rename DB File
        File database = new File(dataBasePath);

        if(!database.exists()) {
            createNewDatabase(dataBasePath);
        }
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:" + dataBasePath);
            System.out.println("Connected to Database successful!");

            // SQL statement for creating a new table
            String sql = "CREATE TABLE IF NOT EXISTS whitelist (\n"
                    + "	uuid text NOT NULL PRIMARY KEY,\n"
                    + "	name text,\n"
                    + " whitelisted integer NOT NULL\n"
                    + ");";

            Statement stmt = conn.createStatement();
            stmt.execute(sql);
            /*
            sql = "SELECT * FROM whitelist;";

            ResultSet rs = stmt.executeQuery(sql);

            while(rs.next()) {
                System.out.println(rs.next());
            }
            */

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

    public static void addPlayertoDataBase(GameProfile player) {

    }

    private static void createNewDatabase(String dataBasePath) {
        String url = "jdbc:sqlite:" + dataBasePath;
        try {
            Connection conn = DriverManager.getConnection(url);
            if(conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                Log.logln("A new database \"" + dataBasePath +"\" has been created.");
            }
        } catch (SQLException e) {
            Log.logln(e.getMessage());
        }
    }
}
