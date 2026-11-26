package com.duoc.adoptame.model;

/**
 * Gato registrado en el centro de adopción.
 * Cuota base: $18.000. Si el gato tiene sociabilidad de 4,0 o más (perfil indoor),
 * la cuota se incrementa en un 15%.
 */
public class Gato implements Animal {

    private static final double CUOTA_BASE = 18000;
    private static final double RECARGO_INDOOR = 0.15;
    private static final double LIMITE_SOCIALIZACION_ALTA = 4.0;

    private String nombre;
    private int anio;
    private int edad;
    private double sociabilidad;

    /**
     * Constructor sin parámetros, requerido para reconstruir objetos.
     */
    public Gato() {
    }

    public Gato(String nombre, int anio, int edad, double sociabilidad) {
        setNombre(nombre);
        setAnio(anio);
        setEdad(edad);
        setSociabilidad(sociabilidad);
    }

    @Override
    public String getNombre() {
        return nombre;
    }

    @Override
    public void setNombre(String nombre) {
        this.nombre = Animal.validarNombre(nombre);
    }

    @Override
    public int getAnio() {
        return anio;
    }

    @Override
    public void setAnio(int anio) {
        this.anio = Animal.validarAnio(anio);
    }

    @Override
    public int getEdad() {
        return edad;
    }

    @Override
    public void setEdad(int edad) {
        this.edad = Animal.validarEdad(edad);
    }

    public double getSociabilidad() {
        return sociabilidad;
    }

    public void setSociabilidad(double sociabilidad) {
        if (sociabilidad <= 0) {
            throw new IllegalArgumentException("La sociabilidad debe ser mayor que cero.");
        }
        this.sociabilidad = sociabilidad;
    }

    /**
     * Indica si el gato tiene perfil indoor.
     *
     * @return true cuando la sociabilidad es 4,0 o más
     */
    public boolean isIndoor() {
        return sociabilidad >= LIMITE_SOCIALIZACION_ALTA;
    }

    @Override
    public double calcularCuotaAdopcion() {
        if (isIndoor()) {
            return CUOTA_BASE + CUOTA_BASE * RECARGO_INDOOR;
        }
        return CUOTA_BASE;
    }

    @Override
    public String getTipo() {
        return "Gato";
    }

    @Override
    public String toString() {
        return "Nombre: " + nombre + " | Año: " + anio + " | Edad: " + edad;
    }
}
