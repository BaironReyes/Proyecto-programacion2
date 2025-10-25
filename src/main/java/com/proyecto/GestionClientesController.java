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

public class GestionClientesController {

    private static final Logger logger = Logger.getLogger(GestionClientesController.class.getName());

    @FXML private TextField txtNombre;
    @FXML private TextField txtApellido;
    @FXML private TextField txtEmail;
    @FXML private TextField txtTelefono;
    @FXML private TextField txtIdentificacion;
    @FXML private ComboBox<String> cmbTipoIdentificacion;
    @FXML private TableView<?> tablaClientes;

    @FXML
    public void initialize() {
        // Llenar combobox con opciones de la BD
        cmbTipoIdentificacion.getItems().addAll("DUI", "ID", "Pasaporte", "Licencia");
        logger.info("Controlador de Gestión de Clientes inicializado");
    }

    @FXML
    private void agregarCliente() {
        logger.info(() -> "Intentando agregar nuevo cliente");
        mostrarAlerta("Cliente agregado (próximamente con BD)");
    }

    @FXML
    private void volverAlDashboard(ActionEvent event) throws IOException {
        logger.info(() -> "Volviendo al Dashboard desde Gestión de Clientes");

        Parent root = FXMLLoader.load(getClass().getResource("Dashboard.fxml"));
        Scene scene = new Scene(root, 800, 600);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        stage.setScene(scene);
        stage.setTitle("Sistema de Hotel");
    }

    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Gestión de Clientes");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();

        logger.info(() -> "Alerta mostrada: " + mensaje);
    }
}