package com.potatosaucevfx.mod.core;

import com.potatosaucevfx.mod.utils.Log;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;

public class Test {

    public static void main(String[] args) {

        /*
        Connection conn = null;
        // TODO: Rename DB File
        String dataBasePath = "C:/sqlite/db/myDatabase.db";
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

            sql = "SELECT * FROM whitelist;";

            ResultSet rs = stmt.executeQuery(sql);

            while(rs.next()) {
                System.out.println(rs.next());
            }

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.err.println(ex.getMessage());
            }
        }
        */
    }

    public static void createNewDatabase(String dataBasePath) {
        String url = "jdbc:sqlite:" + dataBasePath;

        try {
            Connection conn = DriverManager.getConnection(url);
            if(conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                Log.logln("The driver name is " + meta.getDriverName());
                Log.logln("A new database \"" + dataBasePath +"\" has been created.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }




}
