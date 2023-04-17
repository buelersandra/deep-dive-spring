package com.sika.contact;

import lombok.Generated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.*;

@SpringBootApplication
public class ContactApplication {

    @Generated
    public static void main(String[] args) {
        createDb();
        SpringApplication.run(ContactApplication.class, args);
    }

    @Generated
    private static  void createDb(){
        Logger logger = LoggerFactory.getLogger(ContactApplication.class);
        Connection connection = null;
        Statement statement = null;

        try {
            logger.debug("Creating database if not exist...");
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/", "postgres", "hello");
            statement = connection.createStatement();
            statement.executeQuery("SELECT count(*) FROM pg_database WHERE datname = 'sika_db'");
            ResultSet resultSet = statement.getResultSet();
            resultSet.next();
            int count = resultSet.getInt(1);

            if (count <= 0) {
                statement.executeUpdate("CREATE DATABASE sika_db");
                logger.debug("Database created.");
            } else {
                logger.debug("Database already exist.");
            }
        } catch (SQLException e) {
            logger.error(e.toString());
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                    logger.debug("Closed Statement.");
                }
                if (connection != null) {
                    logger.debug("Closed Connection.");
                    connection.close();
                }
            } catch (SQLException e) {
                logger.error(e.toString());
            }
        }
    }
}
