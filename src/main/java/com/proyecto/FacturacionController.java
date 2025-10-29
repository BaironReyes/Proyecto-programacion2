package com.proyecto;

import Modelo.Reserva;
import Modelo.Factura;
import dao.FacturacionDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

public class FacturacionController {

    @FXML private TableView<Reserva> tablaReservas;
    @FXML private TableColumn<Reserva, Integer> colIdReserva;
    @FXML private TableColumn<Reserva, Integer> colIdCliente;
    @FXML private TableColumn<Reserva, Integer> colIdHabitacion;
    @FXML private TableColumn<Reserva, String> colCheckIn;
    @FXML private TableColumn<Reserva, String> colCheckOut;
    @FXML private TableColumn<Reserva, String> colEstado;

    @FXML private TableView<Factura> tablaFacturas;
    @FXML private TableColumn<Factura, Integer> colFacturaId;
    @FXML private TableColumn<Factura, Integer> colFacturaReserva;
    @FXML private TableColumn<Factura, Double> colTotal;
    @FXML private TableColumn<Factura, String> colFecha;
    @FXML private TableColumn<Factura, Boolean> colPagado;

    @FXML private Button btnGenerarFactura;
    @FXML private Button btnMarcarPagado;
    @FXML private Button btnReporteIngresos;

    private FacturacionDAO facturacionDAO;

    @FXML
    public void initialize() throws SQLException {
        facturacionDAO = new FacturacionDAO();
        configurarTablas();
        cargarReservas();
        cargarFacturas();
    }

    private void configurarTablas() {
        colIdReserva.setCellValueFactory(new PropertyValueFactory<>("id"));
        colIdCliente.setCellValueFactory(new PropertyValueFactory<>("idCliente"));
        colIdHabitacion.setCellValueFactory(new PropertyValueFactory<>("idHabitacion"));
        colCheckIn.setCellValueFactory(new PropertyValueFactory<>("checkIn"));
        colCheckOut.setCellValueFactory(new PropertyValueFactory<>("checkOut"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));

        colFacturaId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colFacturaReserva.setCellValueFactory(new PropertyValueFactory<>("idReserva"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        colPagado.setCellValueFactory(new PropertyValueFactory<>("pagado"));
    }

    private void cargarReservas() {
        List<Reserva> reservas = facturacionDAO.obtenerReservasPendientes();
        tablaReservas.setItems(FXCollections.observableArrayList(reservas));
    }

    private void cargarFacturas() {
        List<Factura> facturas = facturacionDAO.obtenerFacturas();
        tablaFacturas.setItems(FXCollections.observableArrayList(facturas));
    }

    @FXML
    private void generarFactura(ActionEvent event) {
        Reserva reservaSeleccionada = tablaReservas.getSelectionModel().getSelectedItem();
        if (reservaSeleccionada == null) {
            mostrarAlerta("Selecciona una reserva para generar factura.");
            return;
        }

        double total = calcularTotalReserva(reservaSeleccionada);
        String fecha = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        boolean exito = facturacionDAO.generarFactura(reservaSeleccionada.getId(), total, fecha);

        if (exito) {
            mostrarAlerta("Factura generada correctamente.");
            cargarReservas();
            cargarFacturas();
        } else {
            mostrarAlerta("Error al generar la factura.");
        }
    }

    @FXML
    private void marcarComoPagada(ActionEvent event) {
        Factura facturaSeleccionada = tablaFacturas.getSelectionModel().getSelectedItem();
        if (facturaSeleccionada == null) {
            mostrarAlerta("Selecciona una factura para marcar como pagada.");
            return;
        }

        boolean exito = facturacionDAO.marcarFacturaPagada(facturaSeleccionada.getId());
        if (exito) {
            mostrarAlerta("Factura marcada como pagada.");
            cargarFacturas();
        } else {
            mostrarAlerta("Error al actualizar el estado de la factura.");
        }
    }

    @FXML
    private void generarReporteIngresos(ActionEvent event) {
        List<Factura> facturas = facturacionDAO.obtenerFacturas();
        double total = facturas.stream().filter(Factura::isPagado).mapToDouble(Factura::getTotal).sum();
        mostrarAlerta("💰 Ingresos totales: $" + total);
    }

    private double calcularTotalReserva(Reserva r) {
        long dias = ChronoUnit.DAYS.between(r.getCheckIn(), r.getCheckOut());
        if (dias < 1) dias = 1;
        double precio = 50.00; // valor temporal o de prueba
        return dias * precio;
    }

    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
