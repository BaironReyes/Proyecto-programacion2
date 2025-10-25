package com.proyecto;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.event.ActionEvent;
import java.io.IOException;
import java.util.logging.Logger;

public class GestionHabitacionesController {

    private static final Logger logger = Logger.getLogger(GestionHabitacionesController.class.getName());

    @FXML private TextField txtNumero;
    @FXML private ComboBox<String> cmbTipo;
    @FXML private TextField txtPrecio;
    @FXML private ComboBox<String> cmbEstado;
    @FXML private TableView<?> tablaHabitaciones;

    @FXML
    public void initialize() {
        cmbTipo.getItems().addAll("Individual", "Doble", "Suite", "Familiar");
        cmbEstado.getItems().addAll("Disponible", "Mantenimiento", "Reservado");

        logger.info("Controlador de Gestión de Habitaciones inicializado");
    }

    @FXML
    private void agregarHabitacion() {
        logger.info(() -> "Intentando agregar nueva habitación");
        mostrarAlerta("Habitación agregada (próximamente con BD)");
    }

    @FXML
    private void volverAlDashboard(ActionEvent event) throws IOException {
        logger.info(() -> "Volviendo al Dashboard desde Gestión de Habitaciones");

        Parent root = FXMLLoader.load(getClass().getResource("Dashboard.fxml"));
        Scene scene = new Scene(root, 800, 600);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        stage.setScene(scene);
        stage.setTitle("Sistema de Hotel");
    }

    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Gestión de Habitaciones");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();

        logger.info(() -> "Alerta mostrada: " + mensaje);
    }
}