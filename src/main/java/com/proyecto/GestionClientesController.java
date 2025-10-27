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
import java.util.logging.Logger;

public class GestionClientesController {

    private static final Logger logger = Logger.getLogger(GestionClientesController.class.getName());

    @FXML private TextField txtNombre;
    @FXML private TextField txtApellido;
    @FXML private TextField txtEmail;
    @FXML private TextField txtTelefono;
    @FXML private TextField txtIdentificacion;
    @FXML private ComboBox<String> cmbTipoIdentificacion;

    @FXML private TableView<ClienteTabla> tablaClientes;
    @FXML private TableColumn<ClienteTabla, String> colId;
    @FXML private TableColumn<ClienteTabla, String> colNombre;
    @FXML private TableColumn<ClienteTabla, String> colApellido;
    @FXML private TableColumn<ClienteTabla, String> colEmail;
    @FXML private TableColumn<ClienteTabla, String> colTelefono;
    @FXML private TableColumn<ClienteTabla, String> colIdentificacion;
    @FXML private TableColumn<ClienteTabla, String> colTipoIdentificacion;

    private ObservableList<ClienteTabla> clientesData = FXCollections.observableArrayList();
    private int contadorClientes = 4; // Empezar después de los 3 de ejemplo

    // Clase interna para manejar los datos de la tabla
    public static class ClienteTabla {
        private final SimpleStringProperty id;
        private final SimpleStringProperty nombre;
        private final SimpleStringProperty apellido;
        private final SimpleStringProperty email;
        private final SimpleStringProperty telefono;
        private final SimpleStringProperty identificacion;
        private final SimpleStringProperty tipoIdentificacion;

        public ClienteTabla(String id, String nombre, String apellido, String email,
                            String telefono, String identificacion, String tipoIdentificacion) {
            this.id = new SimpleStringProperty(id);
            this.nombre = new SimpleStringProperty(nombre);
            this.apellido = new SimpleStringProperty(apellido);
            this.email = new SimpleStringProperty(email);
            this.telefono = new SimpleStringProperty(telefono);
            this.identificacion = new SimpleStringProperty(identificacion);
            this.tipoIdentificacion = new SimpleStringProperty(tipoIdentificacion);
        }

        public String getId() { return id.get(); }
        public String getNombre() { return nombre.get(); }
        public String getApellido() { return apellido.get(); }
        public String getEmail() { return email.get(); }
        public String getTelefono() { return telefono.get(); }
        public String getIdentificacion() { return identificacion.get(); }
        public String getTipoIdentificacion() { return tipoIdentificacion.get(); }
    }

    @FXML
    public void initialize() {
        // Llenar combobox con opciones de la BD
        cmbTipoIdentificacion.getItems().addAll("DUI", "ID", "Pasaporte", "Licencia");
        logger.info("Controlador de Gestión de Clientes inicializado");

        // Configurar tabla de clientes
        configurarTablaClientes();

        // Cargar clientes de ejemplo
        cargarClientesEjemplo();
    }

    private void configurarTablaClientes() {
        if (tablaClientes != null && colId != null) {
            // Configurar las columnas
            colId.setCellValueFactory(new PropertyValueFactory<>("id"));
            colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
            colApellido.setCellValueFactory(new PropertyValueFactory<>("apellido"));
            colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
            colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
            colIdentificacion.setCellValueFactory(new PropertyValueFactory<>("identificacion"));
            colTipoIdentificacion.setCellValueFactory(new PropertyValueFactory<>("tipoIdentificacion"));

            // poner los datos a la tabla
            tablaClientes.setItems(clientesData);
        }
    }

    private void cargarClientesEjemplo() {
        // Agregar los 3 clientes de ejemplo en reservas
        clientesData.add(new ClienteTabla("1", "Bairon", "Reyes", "bairon@email.com", "1234-5678", "00123456", "DUI"));
        clientesData.add(new ClienteTabla("2", "Xiomara", "Arriaga", "xiomara@email.com", "2345-6789", "00234567", "ID"));
        clientesData.add(new ClienteTabla("3", "Ricardo", "Montoya", "ricardo@email.com", "3456-7890", "00345678", "Pasaporte"));

        logger.info("Clientes de ejemplo: " + clientesData.size());
    }

    @FXML
    private void agregarCliente() {
        logger.info("Intentando agregar nuevo cliente");

        if (txtNombre.getText().isEmpty() || txtApellido.getText().isEmpty() ||
                txtEmail.getText().isEmpty() || txtIdentificacion.getText().isEmpty() ||
                cmbTipoIdentificacion.getValue() == null) {
            mostrarAlerta("Error", "Complete todos los campos obligatorios");
            return;
        }

        // Agregar nuevo cliente a la tabla
        String nuevoId = String.valueOf(contadorClientes++);
        clientesData.add(new ClienteTabla(
                nuevoId,
                txtNombre.getText(),
                txtApellido.getText(),
                txtEmail.getText(),
                txtTelefono.getText(),
                txtIdentificacion.getText(),
                cmbTipoIdentificacion.getValue()
        ));

        mostrarAlerta("Nuevo Cliente",
                "Cliente agregado :\n" +
                        "Nombre: " + txtNombre.getText() + " " + txtApellido.getText() + "\n" +
                        "Email: " + txtEmail.getText() + "\n" +
                        "ID: " + txtIdentificacion.getText() + "\n\n");

        limpiarFormulario();
    }

    @FXML
    private void volverAlDashboard(ActionEvent event) throws IOException {
        logger.info("Volviendo al Dashboard desde Gestión de Clientes");

        Parent root = FXMLLoader.load(getClass().getResource("Dashboard.fxml"));
        Scene scene = new Scene(root, 800, 600);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        stage.setScene(scene);
        stage.setTitle("Sistema de Hotel");
    }

    private void limpiarFormulario() {
        txtNombre.clear();
        txtApellido.clear();
        txtEmail.clear();
        txtTelefono.clear();
        txtIdentificacion.clear();
        cmbTipoIdentificacion.setValue(null);
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