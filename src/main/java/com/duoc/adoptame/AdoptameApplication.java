package com.duoc.adoptame;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

/**
 * Aplicación JavaFX. Carga la vista principal y la muestra en la ventana.
 * <p>
 * La vista principal se encuentra en:
 * src/main/resources/com/duoc/adoptame/adoptame-view.fxml
 */
public class AdoptameApplication extends Application {

    /** Ubicación de la vista principal dentro de src/main/resources. */
    private static final String VISTA_PRINCIPAL = "/com/duoc/adoptame/adoptame-view.fxml";

    @Override
    public void start(Stage stage) throws Exception {
        URL vista = AdoptameApplication.class.getResource(VISTA_PRINCIPAL);
        if (vista == null) {
            throw new IllegalStateException("No se encontró la vista principal: " + VISTA_PRINCIPAL);
        }
        Parent root = FXMLLoader.load(vista);
        stage.setTitle("Huella Segura - Centro de Adopción");
        stage.setScene(new Scene(root, 1024, 720));
        stage.show();
    }
}
