package com.duoc.adoptame.dao;

import com.duoc.adoptame.database.ConexionDB;
import com.duoc.adoptame.database.TablaAnimal;
import com.duoc.adoptame.model.Gato;
import com.duoc.adoptame.model.Perro;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Guarda y lee los animales en Oracle (tablas perros y gatos).
 * Cada guardado reemplaza el contenido de la tabla dentro de una transacción:
 * si algo falla se hace rollback y la base queda como estaba.
 */
public class AnimalDaoImpl implements AnimalDao {

    private static final String LEER_PERROS =
            "SELECT nombre, anio, edad, raza, vacunas_al_dia, apto_ninos FROM perros ORDER BY id";
    private static final String LEER_GATOS =
            "SELECT nombre, anio, edad, sociabilidad FROM gatos ORDER BY id";

    private static final String BORRAR_PERROS = "DELETE FROM perros";
    private static final String BORRAR_GATOS = "DELETE FROM gatos";

    private static final String CREAR_PERRO =
            "INSERT INTO perros (nombre, anio, edad, raza, vacunas_al_dia, apto_ninos) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String CREAR_GATO =
            "INSERT INTO gatos (nombre, anio, edad, sociabilidad) VALUES (?, ?, ?, ?)";

    private boolean tablasListas = false;

    @FunctionalInterface
    private interface Operacion {
        void ejecutar(Connection conn) throws SQLException;
    }

    @Override
    public List<Perro> leerPerro() throws SQLException {
        asegurarTablas();
        List<Perro> listado = new ArrayList<>();
        try (
                Connection conn = ConexionDB.obtenerConexion();
                PreparedStatement ps = conn.prepareStatement(LEER_PERROS);
                ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                listado.add(new Perro(
                        rs.getString("nombre"),
                        rs.getInt("anio"),
                        rs.getInt("edad"),
                        rs.getString("raza"),
                        rs.getInt("vacunas_al_dia") == 1,
                        rs.getInt("apto_ninos") == 1));
            }
        }
        return listado;
    }

    @Override
    public List<Gato> leerGato() throws SQLException {
        asegurarTablas();
        List<Gato> listado = new ArrayList<>();
        try (
                Connection conn = ConexionDB.obtenerConexion();
                PreparedStatement ps = conn.prepareStatement(LEER_GATOS);
                ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                listado.add(new Gato(
                        rs.getString("nombre"),
                        rs.getInt("anio"),
                        rs.getInt("edad"),
                        rs.getDouble("sociabilidad")));
            }
        }
        return listado;
    }

    @Override
    public void guardarPerro(List<Perro> animales) throws SQLException {
        enTransaccion(conn -> reemplazarPerros(conn, animales));
    }

    @Override
    public void guardarGato(List<Gato> animales) throws SQLException {
        enTransaccion(conn -> reemplazarGatos(conn, animales));
    }

    /** Guarda perros y gatos en una sola transacción (o se guardan los dos o ninguno). */
    @Override
    public void guardarTodo(List<Perro> perros, List<Gato> gatos) throws SQLException {
        enTransaccion(conn -> {
            reemplazarPerros(conn, perros);
            reemplazarGatos(conn, gatos);
        });
    }

    private void reemplazarPerros(Connection conn, List<Perro> perros) throws SQLException {
        try (Statement borrar = conn.createStatement()) {
            borrar.executeUpdate(BORRAR_PERROS);
        }
        try (PreparedStatement ps = conn.prepareStatement(CREAR_PERRO)) {
            for (Perro perro : perros) {
                ps.setString(1, perro.getNombre());
                ps.setInt(2, perro.getAnio());
                ps.setInt(3, perro.getEdad());
                ps.setString(4, perro.getRaza());
                ps.setInt(5, perro.isVacunasAlDia() ? 1 : 0);
                ps.setInt(6, perro.isAptoParaNinos() ? 1 : 0);
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private void reemplazarGatos(Connection conn, List<Gato> gatos) throws SQLException {
        try (Statement borrar = conn.createStatement()) {
            borrar.executeUpdate(BORRAR_GATOS);
        }
        try (PreparedStatement ps = conn.prepareStatement(CREAR_GATO)) {
            for (Gato gato : gatos) {
                ps.setString(1, gato.getNombre());
                ps.setInt(2, gato.getAnio());
                ps.setInt(3, gato.getEdad());
                ps.setDouble(4, gato.getSociabilidad());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private void enTransaccion(Operacion operacion) throws SQLException {
        asegurarTablas();
        try (Connection conn = ConexionDB.obtenerConexion()) {
            conn.setAutoCommit(false);
            try {
                operacion.ejecutar(conn);
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        }
    }

    private void asegurarTablas() throws SQLException {
        if (!tablasListas) {
            TablaAnimal.inicializar();
            tablasListas = true;
        }
    }
}
