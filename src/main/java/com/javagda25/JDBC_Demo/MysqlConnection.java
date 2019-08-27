package com.javagda25.JDBC_Demo;

import com.mysql.cj.jdbc.MysqlDataSource;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class MysqlConnection {
    //    łączenie się z bazą danych

    private MysqlConnectionParameters parameters;
    private MysqlDataSource dataSource;

    //    Ta metoda ma wywołać się tylko raz :
//    jest to konstrukt, co oznacza, że wykona się bez wywołania (zabezpieczenie)
    public MysqlConnection() throws IOException {
        parameters = new MysqlConnectionParameters();
        initialize();
    }

    private void initialize() {
        dataSource = new MysqlDataSource();
//        dataSource - zbiór poleceń do bazy danycyh

        dataSource.setPort(Integer.parseInt(parameters.getDbPort()));
        dataSource.setUser(parameters.getDbUsername());
        dataSource.setServerName(parameters.getDbHost());
        dataSource.setPassword(parameters.getDbPassword());
        dataSource.setDatabaseName(parameters.getDbName());

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
