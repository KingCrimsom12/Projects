package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args)
    {
        String url = "jdbc:mysql://localhost:3306/empleados_departamentos";
        String user = "root";
        String password = "123456789";
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            System.out.println("La conexión fue exitosa");
        } catch (SQLException e) {
            System.out.println("No funca");
        }
    }
}

