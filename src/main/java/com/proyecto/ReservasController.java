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
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.cell.PropertyValueFactory;
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
    private static final String MSJ_VERIFICANDO_DISPONIBILIDAD = "Habitaciones disponibles";

    @FXML private ComboBox<ClienteItem> cmbClientes;
    @FXML private ComboBox<HabitacionItem> cmbHabitaciones;
    @FXML private DatePicker dpCheckIn;
    @FXML private DatePicker dpCheckOut;
    @FXML private Label lblNoches;
    @FXML private Label lblPrecioNoche;
    @FXML private Label lblTotal;

    @FXML private TableView<ReservaTabla> tablaReservas;
    @FXML private TableColumn<ReservaTabla, String> colId;
    @FXML private TableColumn<ReservaTabla, String> colIdCliente;
    @FXML private TableColumn<ReservaTabla, String> colIdHabitacion;
    @FXML private TableColumn<ReservaTabla, String> colCheckIn;
    @FXML private TableColumn<ReservaTabla, String> colCheckOut;
    @FXML private TableColumn<ReservaTabla, String> colEstado;

    private ObservableList<ReservaTabla> reservasData = FXCollections.observableArrayList();
    private int contadorReservas = 1;

    // Clases para mantener ID numéricos (como tu BD)
    public static class ClienteItem {
        private int id;
        private String nombreCompleto;

        public ClienteItem(int id, String nombreCompleto) {
            this.id = id;
            this.nombreCompleto = nombreCompleto;
        }

        public int getId() { return id; }
        public String getNombreCompleto() { return nombreCompleto; }

        @Override
        public String toString() {
            return nombreCompleto + " (ID: " + id + ")";
        }
    }

    public static class HabitacionItem {
        private int id;
        private String numero;
        private String tipo;
        private double precio;

        public HabitacionItem(int id, String numero, String tipo, double precio) {
            this.id = id;
            this.numero = numero;
            this.tipo = tipo;
            this.precio = precio;
        }

        public int getId() { return id; }
        public String getNumero() { return numero; }
        public String getTipo() { return tipo; }
        public double getPrecio() { return precio; }

        @Override
        public String toString() {
            return numero + " - " + tipo + " - $" + precio;
        }
    }

    // Clase que calza PERFECTAMENTE con tu BD y clase Reserva
    public static class ReservaTabla {
        private final SimpleStringProperty id;
        private final SimpleStringProperty idCliente;
        private final SimpleStringProperty idHabitacion;
        private final SimpleStringProperty checkIn;
        private final SimpleStringProperty checkOut;
        private final SimpleStringProperty estado;

        public ReservaTabla(String id, String idCliente, String idHabitacion,
                            String checkIn, String checkOut, String estado) {
            this.id = new SimpleStringProperty(id);
            this.idCliente = new SimpleStringProperty(idCliente);
            this.idHabitacion = new SimpleStringProperty(idHabitacion);
            this.checkIn = new SimpleStringProperty(checkIn);
            this.checkOut = new SimpleStringProperty(checkOut);
            this.estado = new SimpleStringProperty(estado);
        }

        // Getters que calzan con tu clase Reserva.java
        public String getId() { return id.get(); }
        public String getIdCliente() { return idCliente.get(); }
        public String getIdHabitacion() { return idHabitacion.get(); }
        public String getCheckIn() { return checkIn.get(); }
        public String getCheckOut() { return checkOut.get(); }
        public String getEstado() { return estado.get(); }
    }

    @FXML
    public void initialize() {
        logger.info("Controlador de Reservas inicializado");

        if (cmbClientes == null || cmbHabitaciones == null) {
            logger.severe("Error: ComboBox no inicializados correctamente en FXML");
            return;
        }

        // Configurar tabla de reservas
        configurarTablaReservas();

        // Cargar clientes con ID numéricos (como tu BD)
        ObservableList<ClienteItem> clientes = FXCollections.observableArrayList(
                new ClienteItem(1, "Bairon Reyes"),
                new ClienteItem(2, "Xiomara Arriaga"),
                new ClienteItem(3, "Ricardo Montoya")
        );
        cmbClientes.setItems(clientes);
        cmbClientes.setPromptText("Seleccionar Cliente");

        // Cargar habitaciones con ID numéricos (como tu BD)
        ObservableList<HabitacionItem> habitaciones = FXCollections.observableArrayList(
                new HabitacionItem(1, "267", "Individual", 70.0),
                new HabitacionItem(2, "280", "Doble", 110.0),
                new HabitacionItem(3, "300", "Suite", 225.0),
                new HabitacionItem(4, "202", "Familiar", 150.0)
        );
        cmbHabitaciones.setItems(habitaciones);
        cmbHabitaciones.setPromptText("Seleccionar Habitación");

        logger.info("ComboBoxes Clientes: " + clientes.size() + ", Habitaciones: " + habitaciones.size());

        dpCheckIn.valueProperty().addListener((obs, oldDate, newDate) -> calcularNochesYTotal());
        dpCheckOut.valueProperty().addListener((obs, oldDate, newDate) -> calcularNochesYTotal());
        cmbHabitaciones.valueProperty().addListener((obs, oldVal, newVal) -> calcularNochesYTotal());

        dpCheckIn.setValue(LocalDate.now());
        dpCheckOut.setValue(LocalDate.now().plusDays(1));

        lblPrecioNoche.setText("0$");
        lblTotal.setText("0$");
        lblNoches.setText("Noches: 0");
    }

    private void configurarTablaReservas() {
        if (tablaReservas != null && colId != null) {
            // Configurar columnas que calzan con tu BD
            colId.setCellValueFactory(new PropertyValueFactory<>("id"));
            colIdCliente.setCellValueFactory(new PropertyValueFactory<>("idCliente"));
            colIdHabitacion.setCellValueFactory(new PropertyValueFactory<>("idHabitacion"));
            colCheckIn.setCellValueFactory(new PropertyValueFactory<>("checkIn"));
            colCheckOut.setCellValueFactory(new PropertyValueFactory<>("checkOut"));
            colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));

            tablaReservas.setItems(reservasData);
        }
    }

    @FXML
    private void crearReserva() {
        logger.info("Intentando crear nueva reserva");

        ClienteItem clienteSeleccionado = cmbClientes.getValue();
        HabitacionItem habitacionSeleccionada = cmbHabitaciones.getValue();

        if (clienteSeleccionado == null || habitacionSeleccionada == null) {
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

        // Crear reserva con ID numéricos (como tu BD)
        String idReserva = String.valueOf(contadorReservas++);
        reservasData.add(new ReservaTabla(
                idReserva,
                String.valueOf(clienteSeleccionado.getId()),      // id_cliente (INT)
                String.valueOf(habitacionSeleccionada.getId()),   // id_habitacion (INT)
                dpCheckIn.getValue().toString(),                  // check_in (DATE)
                dpCheckOut.getValue().toString(),                 // check_out (DATE)
                "Confirmada"                                      // estado (ENUM)
        ));

        String mensaje = "Reserva creada (calza con BD):\n" +
                "ID Reserva: " + idReserva + "\n" +
                "ID Cliente: " + clienteSeleccionado.getId() + "\n" +
                "ID Habitación: " + habitacionSeleccionada.getId() + "\n" +
                "Cliente: " + clienteSeleccionado.getNombreCompleto() + "\n" +
                "Habitación: " + habitacionSeleccionada.getNumero() + "\n" +
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
        logger.info("Volviendo al dashboard");

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
        HabitacionItem habitacionSeleccionada = cmbHabitaciones.getValue();
        if (habitacionSeleccionada != null) {
            return habitacionSeleccionada.getPrecio();
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