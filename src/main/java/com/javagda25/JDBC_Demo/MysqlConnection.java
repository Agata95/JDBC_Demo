package com.javagda25.JDBC_Demo;

import com.mysql.cj.jdbc.MysqlDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class MysqlConnection {
    //    łączenie się z bazą danych

    private static final String DB_HOST = "localhost"; // 127.0.0.1
    private static final String DB_PORT = "3306";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "root";
    private static final String DB_NAME = "jdbc_students";

    private MysqlDataSource dataSource;

    //    Ta metoda ma wywołać się tylko raz :
//    jest to konstrukt, co oznacza, że wykona się bez wywołania (zabezpieczenie)
    public MysqlConnection() {
        initialize();
    }

    private void initialize() {
        dataSource = new MysqlDataSource();
//        dataSource - zbiór poleceń do bazy danycyh

        dataSource.setPort(Integer.parseInt(DB_PORT));
        dataSource.setUser(DB_USERNAME);
        dataSource.setServerName(DB_HOST);
        dataSource.setPassword(DB_PASSWORD);
        dataSource.setDatabaseName(DB_NAME);

        try {
            dataSource.setServerTimezone("Europe/Warsaw");
            dataSource.setUseSSL(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
