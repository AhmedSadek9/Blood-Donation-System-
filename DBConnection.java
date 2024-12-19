
package com.project.ds;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
  

    private static final String URL = "jdbc:mysql://localhost:3306/blood_donation";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "01023058859";


    public static Connection getConnection() throws SQLException {
        Connection con = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (ClassNotFoundException e) {
 
            throw new SQLException("MySQL JDBC Driver not found.", e);
        }
        return con;
    }
}

