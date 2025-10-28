package com.proyecto;

import dao.HabitacionDAO;
import Modelo.Habitacion;
import java.util.List;
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
import java.util.logging.Logger;

public class GestionHabitacionesController {

    private HabitacionDAO habitacionDAO = new HabitacionDAO();

    private static final Logger logger = Logger.getLogger(GestionHabitacionesController.class.getName());

    // Constantes para los estados (como pide Cube)
    private static final String ESTADO_DISPONIBLE = "Disponible";
    private static final String ESTADO_MANTENIMIENTO = "Mantenimiento";
    private static final String ESTADO_RESERVADO = "Reservado";

    // Constantes para los tipos de habitación
    private static final String TIPO_INDIVIDUAL = "Individual";
    private static final String TIPO_DOBLE = "Doble";
    private static final String TIPO_SUITE = "Suite";
    private static final String TIPO_FAMILIAR = "Familiar";

    @FXML private TextField txtNumero;
    @FXML private ComboBox<String> cmbTipo;
    @FXML private TextField txtPrecio;
    @FXML private ComboBox<String> cmbEstado;

    @FXML private TableView<HabitacionTabla> tablaHabitaciones;
    @FXML private TableColumn<HabitacionTabla, String> colId;
    @FXML private TableColumn<HabitacionTabla, String> colNumero;
    @FXML private TableColumn<HabitacionTabla, String> colTipo;
    @FXML private TableColumn<HabitacionTabla, String> colPrecio;
    @FXML private TableColumn<HabitacionTabla, String> colEstado;

    private ObservableList<HabitacionTabla> habitacionesData = FXCollections.observableArrayList();
    private int contadorHabitaciones = 5;

    public static class HabitacionTabla {
        private final SimpleStringProperty id;
        private final SimpleStringProperty numero;
        private final SimpleStringProperty tipo;
        private final SimpleStringProperty precio;
        private final SimpleStringProperty estado;

        public HabitacionTabla(String id, String numero, String tipo, String precio, String estado) {
            this.id = new SimpleStringProperty(id);
            this.numero = new SimpleStringProperty(numero);
            this.tipo = new SimpleStringProperty(tipo);
            this.precio = new SimpleStringProperty(precio);
            this.estado = new SimpleStringProperty(estado);
        }

        public String getId() { return id.get(); }
        public String getNumero() { return numero.get(); }
        public String getTipo() { return tipo.get(); }
        public String getPrecio() { return precio.get(); }
        public String getEstado() { return estado.get(); }
    }

    @FXML
    public void initialize() {
        // Usar constantes en lugar de strings literales
        cmbTipo.getItems().addAll(TIPO_INDIVIDUAL, TIPO_DOBLE, TIPO_SUITE, TIPO_FAMILIAR);
        cmbEstado.getItems().addAll(ESTADO_DISPONIBLE, ESTADO_MANTENIMIENTO, ESTADO_RESERVADO);

        logger.info("Entrando a gestion de habitaciones");

        configurarTablaHabitaciones();
        cargarHabitacionesDesdeBD();
    }

    private void configurarTablaHabitaciones() {
        if (tablaHabitaciones != null && colId != null) {
            colId.setCellValueFactory(new PropertyValueFactory<>("id"));
            colNumero.setCellValueFactory(new PropertyValueFactory<>("numero"));
            colTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
            colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
            colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));

            tablaHabitaciones.setItems(habitacionesData);
        }
    }

    private void cargarHabitacionesDesdeBD() {
        habitacionesData.clear();
        List<Habitacion> lista = habitacionDAO.listarHabitaciones();
        if (lista != null) {
            for (Habitacion h : lista) {
                String id = String.valueOf(h.getId());
                habitacionesData.add(new HabitacionTabla(
                        id,
                        h.getNumeroHabitacion(),
                        h.getTipoHabitacion(),
                        String.valueOf(h.getPrecio()),
                        h.getEstado()
                ));
            }
        } else {
            System.out.println("No se pudieron obtener las habitaciones desde la BD.");
        }
        tablaHabitaciones.setItems(habitacionesData);
    }


    @FXML
    private void agregarHabitacion() {
        logger.info("Intentando agregar nueva habitación");

        if (txtNumero.getText().isEmpty() || cmbTipo.getValue() == null || txtPrecio.getText().isEmpty()) {
            mostrarAlerta("Error", "Complete todos los campos obligatorios");
            return;
        }

        boolean ok = habitacionDAO.insertarHabitacion(
                txtNumero.getText(),
                cmbTipo.getValue(),
                Double.parseDouble(txtPrecio.getText()),
                cmbEstado.getValue()
        );

        if (ok) {
            mostrarAlerta("Éxito", "Habitación guardada en la base de datos.");
            cargarHabitacionesDesdeBD(); // refresca la tabla
            txtNumero.clear();
            txtPrecio.clear();
            cmbTipo.setValue(null);
        } else {
            mostrarAlerta("Error", "No se pudo guardar la habitación.");
        }
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

    private void limpiarFormulario() {
        txtNumero.clear();
        cmbTipo.setValue(null);
        txtPrecio.clear();
        cmbEstado.setValue(null);
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();

        logger.info("Alerta mostrada: " + titulo + " " + mensaje);
    }
}