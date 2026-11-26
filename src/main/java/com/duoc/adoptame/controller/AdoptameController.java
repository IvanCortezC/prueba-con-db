package com.duoc.adoptame.controller;

import com.duoc.adoptame.model.Animal;
import com.duoc.adoptame.model.Gato;
import com.duoc.adoptame.model.Perro;
import com.duoc.adoptame.repository.AnimalRepository;
import com.duoc.adoptame.repository.AnimalRepositoryImpl;
import com.duoc.adoptame.repository.RepositoryException;
import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.util.Locale;

/**
 * Controlador de la ventana única de adopción.
 * <p>
 * Registra y elimina animales, mantiene la tabla sincronizada con la base de datos Oracle
 * (a través del repositorio) y muestra los resultados en el área de mensajes.
 */
public class AdoptameController {

    private final AnimalRepository repositorio = new AnimalRepositoryImpl();
    private final ObservableList<Animal> animalesTabla = FXCollections.observableArrayList();

    @FXML
    private ComboBox<String> tipoComboBox;
    @FXML
    private TextField nombreField;
    @FXML
    private TextField anioField;
    @FXML
    private TextField edadField;
    @FXML
    private VBox datosPerroBox;
    @FXML
    private TextField razaField;
    @FXML
    private CheckBox vacunasCheckBox;
    @FXML
    private CheckBox aptoNinosCheckBox;
    @FXML
    private VBox datosGatoBox;
    @FXML
    private TextField sociabilidadField;
    @FXML
    private Button registrarButton;
    @FXML
    private Button eliminarButton;
    @FXML
    private TableView<Animal> tablaAnimales;
    @FXML
    private TableColumn<Animal, String> colEspecie;
    @FXML
    private TableColumn<Animal, String> colNombre;
    @FXML
    private TableColumn<Animal, Number> colAnio;
    @FXML
    private TableColumn<Animal, Number> colEdad;
    @FXML
    private TableColumn<Animal, Number> colCuota;
    @FXML
    private Label mensajeLabel;

    @FXML
    private void initialize() {
        tipoComboBox.setItems(FXCollections.observableArrayList("Perro", "Gato"));

        tipoComboBox.getSelectionModel().selectFirst();

        colEspecie.setCellValueFactory(datos -> new ReadOnlyStringWrapper(datos.getValue().getTipo()));
        colNombre.setCellValueFactory(datos -> new ReadOnlyStringWrapper(datos.getValue().getNombre()));
        colAnio.setCellValueFactory(datos -> new ReadOnlyIntegerWrapper(datos.getValue().getAnio()));
        colEdad.setCellValueFactory(datos -> new ReadOnlyIntegerWrapper(datos.getValue().getEdad()));
        colCuota.setCellValueFactory(datos -> new ReadOnlyDoubleWrapper(datos.getValue().calcularCuotaAdopcion()));

        colCuota.setCellFactory(columna -> new TableCell<>() {
            @Override
            protected void updateItem(Number valor, boolean vacio) {
                super.updateItem(valor, vacio);
                if (vacio || valor == null) {
                    setText(null);
                } else {
                    setText(String.format(Locale.forLanguageTag("es-CL"), "$%,.0f", valor.doubleValue()));
                }
            }
        });

        tablaAnimales.setItems(animalesTabla);

        tipoComboBox.valueProperty().addListener((observable, anterior, actual) -> actualizarCamposPorTipo());
        actualizarCamposPorTipo();

        try {
            repositorio.cargar();
            actualizarTabla();
        } catch (RepositoryException e) {
            mostrarMensaje(e.getMessage());
        }
    }

    /** Habilita únicamente los campos propios del tipo seleccionado. */
    private void actualizarCamposPorTipo() {
        boolean perro = "Perro".equals(tipoComboBox.getValue());
        boolean gato = "Gato".equals(tipoComboBox.getValue());
        datosPerroBox.setDisable(!perro);
        datosGatoBox.setDisable(!gato);
    }

    @FXML
    private void onRegistrar() {
        try {
            String tipo = tipoComboBox.getValue();
            if (tipo == null) {
                throw new IllegalArgumentException("Debe seleccionar un tipo de animal.");
            }
            String nombre = nombreField.getText();
            if (nombre == null || nombre.trim().isEmpty()) {
                throw new IllegalArgumentException("El nombre no puede estar vacío ni contener solo espacios.");
            }
            int anio = leerEntero(anioField.getText(), "Año");
            if (anio < Animal.ANIO_MINIMO || anio > Animal.ANIO_MAXIMO) {
                throw new IllegalArgumentException("El año debe estar entre "
                        + Animal.ANIO_MINIMO + " y " + Animal.ANIO_MAXIMO + ".");
            }
            int edad = leerEntero(edadField.getText(), "Edad");
            if (edad <= 0) {
                throw new IllegalArgumentException("La edad debe ser mayor a cero.");
            }

            Animal animal;
            if ("Perro".equals(tipo)) {
                String raza = razaField.getText();
                if (raza == null || raza.trim().isEmpty()) {
                    throw new IllegalArgumentException("La raza no puede estar vacía ni contener solo espacios.");
                }
                animal = new Perro(nombre, anio, edad, raza,
                        vacunasCheckBox.isSelected(), aptoNinosCheckBox.isSelected());
            } else {
                double sociabilidad = leerDecimal(sociabilidadField.getText(), "Sociabilidad");
                if (sociabilidad <= 0) {
                    throw new IllegalArgumentException("La sociabilidad debe ser mayor que cero.");
                }
                animal = new Gato(nombre, anio, edad, sociabilidad);
            }

            repositorio.registrar(animal);
            actualizarTabla();
            mostrarMensaje(animal.getNombre() + " (" + animal.getTipo() + ") registrado correctamente.");
            limpiarFormulario();

        } catch (IllegalArgumentException e) {
            // Dato no válido: no se registra y se mantienen los valores ingresados.
            mostrarMensaje(e.getMessage());
        } catch (RepositoryException e) {
            // Error al guardar en la base de datos.
            mostrarMensaje(e.getMessage());
        }
    }

    private int leerEntero(String texto, String nombreCampo) {
        if (texto == null || texto.trim().isEmpty()) {
            throw new IllegalArgumentException("El campo " + nombreCampo + " es obligatorio.");
        }
        try {
            return Integer.parseInt(texto.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("El campo " + nombreCampo + " debe ser un número entero.");
        }
    }

    /** Lee un decimal aceptando coma o punto (por ejemplo 4,5 o 4.5). */
    private double leerDecimal(String texto, String nombreCampo) {
        if (texto == null || texto.trim().isEmpty()) {
            throw new IllegalArgumentException("El campo " + nombreCampo + " es obligatorio.");
        }
        try {
            double valor = Double.parseDouble(texto.trim().replace(',', '.'));
            if (Double.isNaN(valor) || Double.isInfinite(valor)) {
                throw new NumberFormatException();
            }
            return valor;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("El campo " + nombreCampo + " debe ser un número (ej.: 4,5).");
        }
    }

    @FXML
    private void onEliminar() {
        Animal seleccionado = tablaAnimales.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarMensaje("Debe seleccionar un animal de la tabla para eliminarlo.");
            return;
        }
        try {
            repositorio.eliminar(seleccionado);
            actualizarTabla();
            mostrarMensaje(seleccionado.getNombre() + " (" + seleccionado.getTipo() + ") eliminado correctamente.");
        } catch (RepositoryException e) {
            mostrarMensaje(e.getMessage());
        }
    }

    /** Refresca la tabla con lo que hay en el repositorio. */
    private void actualizarTabla() {
        animalesTabla.setAll(repositorio.listar());
    }

    /** Muestra un mensaje en la ventana; cada mensaje reemplaza al anterior. */
    private void mostrarMensaje(String mensaje) {
        mensajeLabel.setText(mensaje);
    }

    private void limpiarFormulario() {
        nombreField.clear();
        anioField.clear();
        edadField.clear();
        razaField.clear();
        vacunasCheckBox.setSelected(false);
        aptoNinosCheckBox.setSelected(false);
        sociabilidadField.clear();
    }
}
