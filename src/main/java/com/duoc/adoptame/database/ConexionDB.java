package com.duoc.adoptame.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Entrega conexiones a la base de datos Oracle (contenedor Docker del docker-compose.yml).
 */
public class ConexionDB {
    private static final String URL = "jdbc:oracle:thin:@//localhost:1521/FREEPDB1";
    private static final String USER = "system";
    private static final String PASSWORD = "TuPassword123";

    private ConexionDB() {
    }

    public static Connection obtenerConexion() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
