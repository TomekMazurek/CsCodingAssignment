package com.github.tomekmazurek.codingassignment.service;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbInitializer {

    private Connection connection;
    private String connectionString;

    public DbInitializer(String connectionString) {
        this.connectionString = connectionString;
    }

    public  void initializeDb() throws Exception {
        String createLogEntityTable = getCreateTableStatement();
        try {
            Class.forName("org.hsqldb.jdbc.JDBCDriver");
        } catch (ClassNotFoundException exception) {
            throw exception;
        }

        try {
            connection = DriverManager.getConnection(connectionString, "Sa", "");
            connection.createStatement().executeUpdate(createLogEntityTable);
        } catch (SQLException exception) {
            throw exception;
        } finally {
            connection.close();
        }
    }

    private  String getCreateTableStatement() throws IOException {
        return FileUtils.readFileToString(new File("src/main/resources/sql/data.sql"), "UTF-8");
    }
}
