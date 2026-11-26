package com.duoc.adoptame.dao;

import com.duoc.adoptame.model.Gato;
import com.duoc.adoptame.model.Perro;

import java.sql.SQLException;
import java.util.List;

public interface AnimalDao {
    List<Perro> leerPerro() throws SQLException;

    List<Gato> leerGato() throws SQLException;

    void guardarPerro(List<Perro> animales) throws SQLException;

    void guardarGato(List<Gato> animales) throws SQLException;

    /** Guarda perros y gatos. Las implementaciones pueden hacerlo de forma atómica. */
    default void guardarTodo(List<Perro> perros, List<Gato> gatos) throws SQLException {
        guardarPerro(perros);
        guardarGato(gatos);
    }
}
