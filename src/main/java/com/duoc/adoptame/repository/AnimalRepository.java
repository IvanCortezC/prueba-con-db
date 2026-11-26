package com.duoc.adoptame.repository;

import com.duoc.adoptame.model.Animal;

import java.util.List;

public interface AnimalRepository {
    void cargar() throws RepositoryException;

    List<Animal> listar();

    void registrar(Animal animal) throws RepositoryException;

    void eliminar(Animal animal) throws RepositoryException;
}
