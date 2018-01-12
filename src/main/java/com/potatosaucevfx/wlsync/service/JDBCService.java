/**
 *
 * @author PotatoSauceVFX <rj@potatosaucevfx.com>
 */
package com.potatosaucevfx.wlsync.service;

import com.potatosaucevfx.wlsync.utils.ConfigHandler;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class JDBCService {

    Connection conn = null;

    public JDBCService() {

        try {

            conn = DriverManager.getConnection("jdbc:mysql://"
                    + ConfigHandler.mySQL_IP + ":" + ConfigHandler.mySQL_PORT
                    + "?user=" + ConfigHandler.mySQL_Username
                    + "&password=" + ConfigHandler.mySQL_Password);

            System.out.println("mySQL connected!");

        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }

    }

    String SQL;
    Statement statement;

    public void setupDatabase() {

        // Create database
        try {
            SQL = "CREATE DATABASE IF NOT EXISTS whitelist;";
            statement = conn.createStatement();
            statement.execute(SQL);
        } catch (SQLException ex) {
            System.out.println("Error creating database.");
            System.err.println(ex.getMessage());
        }

        SQL = "CREATE TABLE IF NOT EXISTS whitelist ("
                + "uuid text NOT NULL PRIMARY KEY,"
                + "name text,"
                + "whitelisted integer NOT NULL"
                + ");";
        // Execute

    }
}
