package com.tranminhtri.test.geminimusicplayer.connection;

import android.os.StrictMode;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SqlConnection {

    private static final String IP = "172.21.122.35";
    private static final String URL = "jdbc:jtds:sqlserver://"+IP+":1433;databaseName=musicplayer";
    private static final String USERNAME = "sa";
    private static final String PASSWORD = "sapassword";
    private static Connection connection = null;

    public static Connection getConnection() throws Exception {
        if (connection == null)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            Class.forName("net.sourceforge.jtds.jdbc.Driver");

            connection = DriverManager.getConnection(URL,USERNAME,PASSWORD);

        }
        return connection;
    }
}
