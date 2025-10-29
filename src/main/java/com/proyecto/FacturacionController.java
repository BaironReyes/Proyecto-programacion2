package com.proyecto;

import Modelo.Factura;
import dao.FacturacionDAO;
import dao.Conexion;

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
import java.math.BigDecimal;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.List;
import java.util.logging.Logger;

public class FacturacionController {

    private static final Logger logger = Logger.getLogger(FacturacionController.class.getName());

    private final FacturacionDAO facturacionDAO = new FacturacionDAO();

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

    // ==========================================================
    // === CLASES DE TABLA
    // ==========================================================
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

    // ==========================================================
    // === MÉTODOS PRINCIPALES
    // ==========================================================
    @FXML
    public void initialize() {
        logger.info("Inicializando módulo de Facturación con base de datos...");

        dpFechaInicio.setValue(LocalDate.now().minusDays(30));
        dpFechaFin.setValue(LocalDate.now());

        configurarTablas();
        cargarFacturasDesdeBD();
        cargarReservasDesdeBD();
        calcularReportes();
    }

    private void configurarTablas() {
        // Facturas
        colFacturaId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colReservaId.setCellValueFactory(new PropertyValueFactory<>("idReserva"));
        colFacturaTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        colFacturaFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        colFacturaEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        tablaFacturas.setItems(facturasData);

        // Reservas para facturar (esta parte la llenas desde tu DAO de Reservas)
        colReservaId2.setCellValueFactory(new PropertyValueFactory<>("id"));
        colReservaCliente.setCellValueFactory(new PropertyValueFactory<>("cliente"));
        colReservaHabitacion.setCellValueFactory(new PropertyValueFactory<>("habitacion"));
        colReservaTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        tablaReservasParaFacturar.setItems(reservasData);
    }

    // ==========================================================
    // === CARGA DE DATOS
    // ==========================================================
    private void cargarFacturasDesdeBD() {
        facturasData.clear();
        List<Factura> facturas = facturacionDAO.obtenerTodasFacturas();
        for (Factura f : facturas) {
            facturasData.add(new FacturaTabla(
                    String.valueOf(f.getId()),
                    String.valueOf(f.getIdReserva()),
                    "$" + f.getTotal(),
                    f.getFecha() != null ? f.getFecha().toString() : "",
                    f.isPagado() ? ESTADO_PAGADA : ESTADO_PENDIENTE
            ));
        }
    }

    private void cargarReservasDesdeBD() {
        // 🚧 Aquí debes conectar con tu ReservaDAO real
        // por ahora, dejamos algunos ejemplos visuales
        reservasData.clear();
        reservasData.add(new ReservaTabla("1", "Bairon Reyes", "267", "$210.00"));
        reservasData.add(new ReservaTabla("2", "Xiomara Arriaga", "280", "$330.00"));
        reservasData.add(new ReservaTabla("3", "Ricardo Montoya", "300", "$675.00"));
    }

    // ==========================================================
    // === ACCIONES DE USUARIO
    // ==========================================================
    @FXML
    private void generarFactura() {
        ReservaTabla reservaSeleccionada = tablaReservasParaFacturar.getSelectionModel().getSelectedItem();
        if (reservaSeleccionada == null) {
            mostrarAlerta(MENSAJE_ERROR, "Seleccione una reserva para facturar");
            return;
        }

        try {
            int idReserva = Integer.parseInt(reservaSeleccionada.getId());
            BigDecimal total = new BigDecimal(reservaSeleccionada.getTotal().replace("$", "").replace(",", ""));

            if (facturacionDAO.crearFactura(idReserva, total)) {
                mostrarAlerta("Éxito", "Factura creada correctamente");
                cargarFacturasDesdeBD();
                reservasData.remove(reservaSeleccionada);
                calcularReportes();
            } else {
                mostrarAlerta(MENSAJE_ERROR, "No se pudo crear la factura");
            }
        } catch (Exception e) {
            mostrarAlerta(MENSAJE_ERROR, "Error al generar factura: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void marcarComoPagado() {
        FacturaTabla facturaSeleccionada = tablaFacturas.getSelectionModel().getSelectedItem();
        if (facturaSeleccionada == null) {
            mostrarAlerta(MENSAJE_ERROR, "Seleccione una factura para marcar como pagada");
            return;
        }

        try {
            int idFactura = Integer.parseInt(facturaSeleccionada.getId());
            if (facturacionDAO.marcarComoPagada(idFactura)) {
                mostrarAlerta("Éxito", "Factura marcada como pagada");
                cargarFacturasDesdeBD();
                calcularReportes();
            } else {
                mostrarAlerta(MENSAJE_ERROR, "No se pudo actualizar el estado de la factura");
            }
        } catch (Exception e) {
            mostrarAlerta(MENSAJE_ERROR, "Error al actualizar factura: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void generarReporteIngresos() {
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
    private void volverAlDashboard(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Dashboard.fxml"));
        Scene scene = new Scene(root, 800, 600);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Sistema de Hotel");
    }

    // ==========================================================
    // === REPORTES Y UTILIDADES
    // ==========================================================
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
                logger.warning("Error al procesar fecha de factura: " + factura.getFecha());
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
            String totalLimpio = totalStr.replace("$", "").replace(",", "").trim();
            return Double.parseDouble(totalLimpio);
        } catch (NumberFormatException _) {
            logger.warning("Error al procesar total de factura: " + totalStr);
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
