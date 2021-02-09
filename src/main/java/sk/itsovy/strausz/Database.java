package sk.itsovy.strausz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@SpringBootApplication
@RestController
public class Database {

    public static final String URL = "jdbc:mysql://localhost:3308/e-shop?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    public static final String ROOT = "root";
    public static final String PASSWORD = "";

    private static Connection connection;


    public static void main(String[] args) {
        SpringApplication.run(Database.class, args);
    }

    public  Connection getConnection() throws SQLException {

         connection = DriverManager.getConnection(
                Database.URL, Database.ROOT, Database.PASSWORD);

        return connection;
    }



}
