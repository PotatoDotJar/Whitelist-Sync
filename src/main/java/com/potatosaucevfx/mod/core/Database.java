package com.potatosaucevfx.mod.core;

import com.potatosaucevfx.mod.utils.Log;

import java.io.File;
import java.sql.*;

public class Database {

    public static void setupDatabase() {
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
                Log.logln(ex.getMessage());
            }
        }
    }
}
