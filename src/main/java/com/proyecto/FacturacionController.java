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
import java.util.logging.Logger;

public class FacturacionController {

    private static final Logger logger = Logger.getLogger(FacturacionController.class.getName());

    // Constantes para mensajes y estados
    private static final String MENSAJE_ERROR = "Error";
    private static final String ESTADO_PAGADA = "Pagada";
    private static final String ESTADO_PENDIENTE = "Pendiente";

    @FXML private TableView<FacturaTabla> tablaFacturas;
    @FXML private TableView<ReservaTabla> tablaReservasParaFacturar;
    @FXML private DatePicker dpFechaInicio;
    @FXML private DatePicker dpFechaFin;
    @FXML private Label lblTotalIngresos;
    @FXML private Label lblFacturasGeneradas;
    @FXML private Label lblOcupacionPromedio;

    @FXML private TableColumn<FacturaTabla, String> colFacturaId;
    @FXML private TableColumn<FacturaTabla, String> colReservaId;
    @FXML private TableColumn<FacturaTabla, String> colFacturaTotal;
    @FXML private TableColumn<FacturaTabla, String> colFacturaFecha;
    @FXML private TableColumn<FacturaTabla, String> colFacturaEstado;

    @FXML private TableColumn<ReservaTabla, String> colReservaId2;
    @FXML private TableColumn<ReservaTabla, String> colReservaCliente;
    @FXML private TableColumn<ReservaTabla, String> colReservaHabitacion;
    @FXML private TableColumn<ReservaTabla, String> colReservaTotal;

    private ObservableList<FacturaTabla> facturasData = FXCollections.observableArrayList();
    private ObservableList<ReservaTabla> reservasData = FXCollections.observableArrayList();
    private int contadorFacturas = 3;

    public static class FacturaTabla {
        private final SimpleStringProperty id;
        private final SimpleStringProperty idReserva;
        private final SimpleStringProperty total;
        private final SimpleStringProperty fecha;
        private final SimpleStringProperty estado;

        public FacturaTabla(String id, String idReserva, String total, String fecha, String estado) {
            this.id = new SimpleStringProperty(id);
            this.idReserva = new SimpleStringProperty(idReserva);
            this.total = new SimpleStringProperty(total);
            this.fecha = new SimpleStringProperty(fecha);
            this.estado = new SimpleStringProperty(estado);
        }

        public String getId() { return id.get(); }
        public String getIdReserva() { return idReserva.get(); }
        public String getTotal() { return total.get(); }
        public String getFecha() { return fecha.get(); }
        public String getEstado() { return estado.get(); }
    }

    public static class ReservaTabla {
        private final SimpleStringProperty id;
        private final SimpleStringProperty cliente;
        private final SimpleStringProperty habitacion;
        private final SimpleStringProperty total;

        public ReservaTabla(String id, String cliente, String habitacion, String total) {
            this.id = new SimpleStringProperty(id);
            this.cliente = new SimpleStringProperty(cliente);
            this.habitacion = new SimpleStringProperty(habitacion);
            this.total = new SimpleStringProperty(total);
        }

        public String getId() { return id.get(); }
        public String getCliente() { return cliente.get(); }
        public String getHabitacion() { return habitacion.get(); }
        public String getTotal() { return total.get(); }
    }

    @FXML
    public void initialize() {
        logger.info("Controlador de Facturación inicializado");

        dpFechaInicio.setValue(LocalDate.now().minusDays(30));
        dpFechaFin.setValue(LocalDate.now());

        configurarTablas();
        cargarDatosEjemplo();
        calcularReportes();
    }

    private void configurarTablas() {
        if (tablaFacturas != null && colFacturaId != null) {
            colFacturaId.setCellValueFactory(new PropertyValueFactory<>("id"));
            colReservaId.setCellValueFactory(new PropertyValueFactory<>("idReserva"));
            colFacturaTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
            colFacturaFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
            colFacturaEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
            tablaFacturas.setItems(facturasData);
        }

        if (tablaReservasParaFacturar != null && colReservaId2 != null) {
            colReservaId2.setCellValueFactory(new PropertyValueFactory<>("id"));
            colReservaCliente.setCellValueFactory(new PropertyValueFactory<>("cliente"));
            colReservaHabitacion.setCellValueFactory(new PropertyValueFactory<>("habitacion"));
            colReservaTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
            tablaReservasParaFacturar.setItems(reservasData);
        }
    }

    private void cargarDatosEjemplo() {
        logger.info("Cargando datos de ejemplo para facturación");

        reservasData.add(new ReservaTabla("1", "Bairon Reyes", "267", "$210.00"));
        reservasData.add(new ReservaTabla("2", "Xiomara Arriaga", "280", "$330.00"));
        reservasData.add(new ReservaTabla("3", "Ricardo Montoya", "300", "$675.00"));

        facturasData.add(new FacturaTabla("1", "1", "$150.00", LocalDate.now().minusDays(10).toString(), ESTADO_PAGADA));
        facturasData.add(new FacturaTabla("2", "2", "$225.00", LocalDate.now().minusDays(5).toString(), ESTADO_PAGADA));
    }

    @FXML
    private void generarFactura() {
        logger.info("Generando nueva factura");

        ReservaTabla reservaSeleccionada = tablaReservasParaFacturar.getSelectionModel().getSelectedItem();
        if (reservaSeleccionada == null) {
            mostrarAlerta(MENSAJE_ERROR, "Seleccione una reserva para facturar");
            return;
        }

        String idFactura = String.valueOf(contadorFacturas++);
        facturasData.add(new FacturaTabla(
                idFactura,
                reservaSeleccionada.getId(),
                reservaSeleccionada.getTotal(),
                LocalDate.now().toString(),
                ESTADO_PENDIENTE
        ));

        reservasData.remove(reservaSeleccionada);

        calcularReportes();

        mostrarAlerta("Factura Generada",
                "Factura generada exitosamente:\n" +
                        "ID Factura: " + idFactura + "\n" +
                        "ID Reserva: " + reservaSeleccionada.getId() + "\n" +
                        "Cliente: " + reservaSeleccionada.getCliente() + "\n" +
                        "Total: " + reservaSeleccionada.getTotal() + "\n" +
                        "Estado: " + ESTADO_PENDIENTE);
    }

    @FXML
    private void generarReporteIngresos() {
        logger.info("Generando reporte de ingresos");

        if (dpFechaInicio.getValue() == null || dpFechaFin.getValue() == null) {
            mostrarAlerta(MENSAJE_ERROR, "Seleccione fechas para el reporte");
            return;
        }

        calcularReportes();

        String reporte = "Reporte de Ingresos\n" +
                "Período: " + dpFechaInicio.getValue() + " a " + dpFechaFin.getValue() + "\n" +
                "Total ingresos: " + lblTotalIngresos.getText() + "\n" +
                "Facturas generadas: " + lblFacturasGeneradas.getText() + "\n" +
                "Ocupación promedio: " + lblOcupacionPromedio.getText();

        mostrarAlerta("Reporte de Ingresos", reporte);
    }

    @FXML
    private void marcarComoPagado() {
        logger.info("Marcando factura como pagada");

        FacturaTabla facturaSeleccionada = tablaFacturas.getSelectionModel().getSelectedItem();
        if (facturaSeleccionada == null) {
            mostrarAlerta(MENSAJE_ERROR, "Seleccione una factura para marcar como pagada");
            return;
        }

        facturasData.remove(facturaSeleccionada);
        facturasData.add(new FacturaTabla(
                facturaSeleccionada.getId(),
                facturaSeleccionada.getIdReserva(),
                facturaSeleccionada.getTotal(),
                facturaSeleccionada.getFecha(),
                ESTADO_PAGADA
        ));

        calcularReportes();

        mostrarAlerta("Factura Actualizada",
                "Factura ID: " + facturaSeleccionada.getId() + " marcada como " + ESTADO_PAGADA);
    }

    @FXML
    private void volverAlDashboard(ActionEvent event) throws IOException {
        logger.info("Volviendo al Dashboard");

        Parent root = FXMLLoader.load(getClass().getResource("Dashboard.fxml"));
        Scene scene = new Scene(root, 800, 600);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        stage.setScene(scene);
        stage.setTitle("Sistema de Hotel");
    }

    private void calcularReportes() {
        double totalIngresos = 0;
        int facturasEnPeriodo = 0;

        LocalDate fechaInicio = dpFechaInicio.getValue();
        LocalDate fechaFin = dpFechaFin.getValue();

        for (FacturaTabla factura : facturasData) {
            try {
                LocalDate fechaFactura = LocalDate.parse(factura.getFecha());
                if ((fechaInicio == null || !fechaFactura.isBefore(fechaInicio)) &&
                        (fechaFin == null || !fechaFactura.isAfter(fechaFin))) {

                    double totalFactura = procesarTotalFactura(factura.getTotal());
                    if (totalFactura > 0) {
                        totalIngresos += totalFactura;
                        facturasEnPeriodo++;
                    }
                }
            } catch (Exception _) {
                logger.warning("Error al hacer fecha de factura: " + factura.getFecha());
            }
        }

        double ocupacionPromedio = Math.min(100, (facturasEnPeriodo * 25.0));

        lblTotalIngresos.setText(String.format("$%.2f", totalIngresos));
        lblFacturasGeneradas.setText(String.valueOf(facturasEnPeriodo));
        lblOcupacionPromedio.setText(String.format("%.0f%%", ocupacionPromedio));

        logger.info("Reportes calculados - Ingresos: $" + totalIngresos + ", Facturas: " + facturasEnPeriodo);
    }

    private double procesarTotalFactura(String totalStr) {
        try {
            String totalLimpio = totalStr.replace("$", "").replace(",", "");
            return Double.parseDouble(totalLimpio);
        } catch (NumberFormatException _) {
            logger.warning("Error al hacer el total de factura: " + totalStr);
            return 0.0;
        }
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