package com.duoc.adoptame.model;

/**
 * Contrato que cumplen los animales registrados por el centro de adopción.
 * Define los datos comunes, la cuota estimada y el tipo de cada animal.
 */
public interface Animal {

    int ANIO_MINIMO = 2010;
    int ANIO_MAXIMO = 2026;

    String getNombre();

    void setNombre(String nombre);

    int getAnio();

    void setAnio(int anio);

    int getEdad();

    void setEdad(int edad);

    /**
     * Calcula la cuota de adopción estimada según las reglas de cada especie.
     *
     * @return cuota estimada en pesos
     */
    double calcularCuotaAdopcion();

    /**
     * Retorna el nombre del tipo de animal ("Perro" o "Gato").
     *
     * @return tipo de animal
     */
    String getTipo();

    /** Valida y normaliza el nombre. */
    static String validarNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede ser nulo ni vacío.");
        }
        return nombre.trim();
    }

    /** Valida el año de ingreso o nacimiento estimado. */
    static int validarAnio(int anio) {
        if (anio < ANIO_MINIMO || anio > ANIO_MAXIMO) {
            throw new IllegalArgumentException("El año debe estar entre " + ANIO_MINIMO + " y " + ANIO_MAXIMO + ".");
        }
        return anio;
    }

    /** Valida la edad en años. */
    static int validarEdad(int edad) {
        if (edad <= 0) {
            throw new IllegalArgumentException("La edad en años debe ser mayor que cero.");
        }
        return edad;
    }
}
