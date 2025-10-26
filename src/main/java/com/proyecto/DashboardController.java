package com.proyecto;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.event.ActionEvent;
import java.io.IOException;
import java.util.logging.Logger;

public class DashboardController {

    private static final Logger logger = Logger.getLogger(DashboardController.class.getName());

    @FXML
    private void abrirGestionClientes(ActionEvent event) throws IOException {
        logger.info(() -> "Navegando a Gestión de Clientes");

        Parent root = FXMLLoader.load(getClass().getResource("GestionClientes.fxml"));
        Scene scene = new Scene(root, 900, 700);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        stage.setScene(scene);
        stage.setTitle("Gestión de Clientes");
    }

    @FXML
    private void abrirGestionHabitaciones(ActionEvent event) throws IOException {
        logger.info(() -> "Navegando a Gestión de Habitaciones");

        Parent root = FXMLLoader.load(getClass().getResource("GestionHabitaciones.fxml"));
        Scene scene = new Scene(root, 800, 600);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        stage.setScene(scene);
        stage.setTitle("Gestión de Habitaciones");
    }

    @FXML
    private void abrirSistemaReservas(ActionEvent event) throws IOException {
        logger.info(() -> "Navegando a Sistema de Reservas");

        Parent root = FXMLLoader.load(getClass().getResource("Reservas.fxml"));
        Scene scene = new Scene(root, 1000, 700); // Más grande para la tabla
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        stage.setScene(scene);
        stage.setTitle("Sistema de Reservas");

    }

    @FXML
    private void abrirFacturacion(ActionEvent event) {
        logger.info(() -> "Solicitado módulo de Facturación (en desarrollo)");
        mostrarProximamente("Facturación");
    }

    private void mostrarProximamente(String modulo) {
        logger.info(() -> "Mostrando alerta de 'Próximamente' para: " + modulo);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Próximamente");
        alert.setHeaderText(null);
        alert.setContentText("Módulo de " + modulo + " aun programandose");
        alert.showAndWait();
    }
}