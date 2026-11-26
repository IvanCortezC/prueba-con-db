package com.duoc.adoptame.repository;

import com.duoc.adoptame.dao.AnimalDao;
import com.duoc.adoptame.dao.AnimalDaoImpl;
import com.duoc.adoptame.model.Animal;
import com.duoc.adoptame.model.Gato;
import com.duoc.adoptame.model.Perro;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AnimalRepositoryImpl implements AnimalRepository {
    private final AnimalDao dao;
    private final List<Animal> animales = new ArrayList<>();

    public AnimalRepositoryImpl() {
        this(new AnimalDaoImpl());
    }

    public AnimalRepositoryImpl(AnimalDao dao) {
        this.dao = dao;
    }

    @Override
    public void cargar() throws RepositoryException {
        try {
            List<Animal> cargados = new ArrayList<>();
            cargados.addAll(dao.leerPerro());
            cargados.addAll(dao.leerGato());

            animales.clear();
            animales.addAll(cargados);
        } catch (SQLException | IllegalArgumentException e) {
            throw new RepositoryException("Error al leer la base de datos: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Animal> listar() {
        return new ArrayList<>(animales);
    }

    @Override
    public void registrar(Animal animal) throws RepositoryException {
        List<Animal> actualizados = new ArrayList<>(animales);
        actualizados.add(animal);
        guardar(actualizados);

        animales.clear();
        animales.addAll(actualizados);
    }

    @Override
    public void eliminar(Animal animal) throws RepositoryException {
        List<Animal> actualizados = new ArrayList<>(animales);
        if (!actualizados.remove(animal)) {
            return;
        }
        guardar(actualizados);

        animales.clear();
        animales.addAll(actualizados);
    }

    private void guardar(List<Animal> lista) throws RepositoryException {
        List<Perro> perros = new ArrayList<>();
        List<Gato> gatos = new ArrayList<>();

        for (Animal animal : lista) {
            if (animal instanceof Perro perro) {
                perros.add(perro);
            } else if (animal instanceof Gato gato) {
                gatos.add(gato);
            }
        }
        try {
            dao.guardarTodo(perros, gatos);
        } catch (SQLException e) {
            throw new RepositoryException("Error al guardar en la base de datos: " + e.getMessage(), e);
        }
    }
}
