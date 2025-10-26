package com.proyecto;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.event.ActionEvent;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.logging.Logger;

public class ReservasController {

    private static final Logger logger = Logger.getLogger(ReservasController.class.getName());

    private static final String MSJ_SELECCIONAR_CLIENTE_HAB = "Debe seleccionar un cliente y una habitación";
    private static final String MSJ_SELECCIONAR_FECHAS = "Debe seleccionar fechas de check-in y check-out";
    private static final String MSJ_FECHA_CHECKIN_INVALIDA = "La fecha de check-in no puede ser después del check-out";
    private static final String TITULO_ERROR = "Error";
    private static final String TITULO_RESERVA_CREADA = "Reserva creada";
    private static final String TITULO_DISPONIBILIDAD = "Disponibilidad";
    private static final String MSJ_VERIFICANDO_DISPONIBILIDAD = " Habitaciones disponibles";

    @FXML private ComboBox<String> cmbClientes;
    @FXML private ComboBox<String> cmbHabitaciones;
    @FXML private DatePicker dpCheckIn;
    @FXML private DatePicker dpCheckOut;
    @FXML private Label lblNoches;
    @FXML private Label lblPrecioNoche;
    @FXML private Label lblTotal;

    @FXML
    public void initialize() {
        logger.info("Controlador de Reservas inicializado");

        System.out.println("ReservasController inicializado");
        if (cmbClientes == null) System.out.println("cmbClientes es NULL - revisa fx:id en FXML");
        if (cmbHabitaciones == null) System.out.println("cmbHabitaciones es NULL - revisa fx:id en FXML");

        ObservableList<String> clientes = FXCollections.observableArrayList(
                "Bairon Reyes (ID: 1)",
                "Xiomara Arriaga (ID: 2)",
                "Ricardo Montoya (ID: 3)"
        );
        cmbClientes.setItems(clientes);
        cmbClientes.setPromptText("Seleccionar Cliente");

        ObservableList<String> habitaciones = FXCollections.observableArrayList(
                "267 - Individual - $70",
                "280 - Doble - $110",
                "300 - Suite - $225",
                "202 - Familiar - $150"
        );
        cmbHabitaciones.setItems(habitaciones);
        cmbHabitaciones.setPromptText("Seleccionar Habitación");

        System.out.println("ComboBoxes cargados - Clientes: " + clientes.size() + ", Habitaciones: " + habitaciones.size());

        dpCheckIn.valueProperty().addListener((obs, oldDate, newDate) -> calcularNochesYTotal());
        dpCheckOut.valueProperty().addListener((obs, oldDate, newDate) -> calcularNochesYTotal());
        cmbHabitaciones.valueProperty().addListener((obs, oldVal, newVal) -> calcularNochesYTotal());

        dpCheckIn.setValue(LocalDate.now());
        dpCheckOut.setValue(LocalDate.now().plusDays(1));

        lblPrecioNoche.setText("0$");
        lblTotal.setText("0$");
        lblNoches.setText("Noches: 0");
    }

    @FXML
    private void crearReserva() {
        logger.info("Intentando crear nueva reserva");

        if (cmbClientes.getValue() != null) {
            logger.info("Cliente seleccionado: " + cmbClientes.getValue());
        }
        if (cmbHabitaciones.getValue() != null) {
            logger.info("Habitación seleccionada: " + cmbHabitaciones.getValue());
        }

        if (cmbClientes.getValue() == null || cmbHabitaciones.getValue() == null) {
            mostrarAlerta(TITULO_ERROR, MSJ_SELECCIONAR_CLIENTE_HAB);
            return;
        }

        if (dpCheckIn.getValue() == null || dpCheckOut.getValue() == null) {
            mostrarAlerta(TITULO_ERROR, MSJ_SELECCIONAR_FECHAS);
            return;
        }

        if (dpCheckIn.getValue().isAfter(dpCheckOut.getValue())) {
            mostrarAlerta(TITULO_ERROR, MSJ_FECHA_CHECKIN_INVALIDA);
            return;
        }

        String mensaje = "Reserva creada:\n" +
                "Cliente: " + cmbClientes.getValue() + "\n" +
                "Habitación: " + cmbHabitaciones.getValue() + "\n" +
                "Check-in: " + dpCheckIn.getValue() + "\n" +
                "Check-out: " + dpCheckOut.getValue() + "\n" +
                "Total: " + lblTotal.getText();

        mostrarAlerta(TITULO_RESERVA_CREADA, mensaje);

        limpiarFormulario();
    }

    @FXML
    private void verDisponibilidad() {
        logger.info("Disponibilidad de habitaciones");
        mostrarAlerta(TITULO_DISPONIBILIDAD, MSJ_VERIFICANDO_DISPONIBILIDAD);
    }

    @FXML
    private void volverAlDashboard(ActionEvent event) throws IOException {
        logger.info("Volvi al Dashboard desde Reservas");

        Parent root = FXMLLoader.load(getClass().getResource("Dashboard.fxml"));
        Scene scene = new Scene(root, 800, 600);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        stage.setScene(scene);
        stage.setTitle("Sistema de Hotel");
    }

    private void calcularNochesYTotal() {
        if (dpCheckIn.getValue() != null && dpCheckOut.getValue() != null) {
            long noches = ChronoUnit.DAYS.between(dpCheckIn.getValue(), dpCheckOut.getValue());
            noches = Math.max(0, noches);

            lblNoches.setText("Noches: " + noches);

            double precioPorNoche = obtenerPrecioPorNoche();
            double total = noches * precioPorNoche;

            lblPrecioNoche.setText(precioPorNoche + "$");
            lblTotal.setText(total + "$");
        }
    }

    private double obtenerPrecioPorNoche() {
        if (cmbHabitaciones.getValue() != null) {
            String habitacionSeleccionada = cmbHabitaciones.getValue();

            if (habitacionSeleccionada.contains("267")) return 70.0;
            if (habitacionSeleccionada.contains("280")) return 110.0;
            if (habitacionSeleccionada.contains("300")) return 225.0;
            if (habitacionSeleccionada.contains("202")) return 150.0;
        }

        return 85.0;
    }

    private void limpiarFormulario() {
        cmbClientes.setValue(null);
        cmbHabitaciones.setValue(null);
        dpCheckIn.setValue(LocalDate.now());
        dpCheckOut.setValue(LocalDate.now().plusDays(1));
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();

        logger.info("Alerta mostrada: " + titulo + " - " + mensaje);
    }
}