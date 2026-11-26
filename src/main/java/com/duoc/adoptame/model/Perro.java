package com.duoc.adoptame.model;

/**
 * Perro registrado en el centro de adopción.
 * Cuota base: $25.000. Si tiene vacunas al día, se incrementa en un 20%.
 */
public class Perro implements Animal {

    private static final double CUOTA_BASE = 25000;
    private static final double RECARGO_VACUNAS_AL_DIA = 0.20;

    private String nombre;
    private int anio;
    private int edad;
    private String raza;
    private boolean vacunasAlDia;
    private boolean aptoParaNinos;

    /**
     * Constructor sin parámetros, requerido para reconstruir objetos.
     */
    public Perro() {
    }

    public Perro(String nombre, int anio, int edad, String raza,
                 boolean vacunasAlDia, boolean aptoParaNinos) {
        setNombre(nombre);
        setAnio(anio);
        setEdad(edad);
        setRaza(raza);
        this.vacunasAlDia = vacunasAlDia;
        this.aptoParaNinos = aptoParaNinos;
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

    public String getRaza() {
        return raza;
    }

    public void setRaza(String raza) {
        if (raza == null || raza.trim().isEmpty()) {
            throw new IllegalArgumentException("La raza no puede ser nula ni vacía.");
        }
        this.raza = raza.trim();
    }

    public boolean isVacunasAlDia() {
        return vacunasAlDia;
    }

    public void setVacunasAlDia(boolean vacunasAlDia) {
        this.vacunasAlDia = vacunasAlDia;
    }

    public boolean isAptoParaNinos() {
        return aptoParaNinos;
    }

    public void setAptoParaNinos(boolean aptoParaNinos) {
        this.aptoParaNinos = aptoParaNinos;
    }

    @Override
    public double calcularCuotaAdopcion() {
        if (vacunasAlDia) {
            return CUOTA_BASE + CUOTA_BASE * RECARGO_VACUNAS_AL_DIA;
        }
        return CUOTA_BASE;
    }

    @Override
    public String getTipo() {
        return "Perro";
    }

    @Override
    public String toString() {
        return "Nombre: " + nombre + " | Año: " + anio + " | Edad: " + edad;
    }
}
