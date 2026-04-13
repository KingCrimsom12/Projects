package com.example.hibernate.Controllers;

import com.example.hibernate.Controllers.Botones.AgregarDetalleOrden;
import com.example.hibernate.Controllers.Botones.AgregarEmpleado;
import com.example.hibernate.Controllers.Botones.ModificarCategoria;
import com.example.hibernate.Controllers.Botones.ModificarEmpleado;
import com.example.hibernate.Database.Cliente;
import com.example.hibernate.Database.DetalleOrdene;
import com.example.hibernate.Database.Empleado;
import com.example.hibernate.Database.Producto;
import com.example.hibernate.HelloApplication;
import com.example.hibernate.HibernateUtils.HibernateUtils;
import com.example.hibernate.Service.ServicioBorrar;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TablaEmpleadoController {

    @FXML
    private TableColumn<Empleado, String> apellidoColumn;

    @FXML
    private TableColumn<Empleado, Integer> empleadoIdColumn;

    @FXML
    private TableColumn<Empleado, Integer> extensionColumn;

    @FXML
    private TableColumn<Empleado, String> fechaNacColumn;

    @FXML
    private TableColumn<Empleado, String > nombreColumn;

    @FXML
    private TableColumn<Empleado, Integer> reportaColumn;

    @FXML
    private TableView<Empleado> tablaEmpleados;

    private ObservableList<Empleado> categoriaData = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        apellidoColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getApellido()));
        empleadoIdColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        nombreColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNombre()));
        extensionColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getExtension()).asObject());
        fechaNacColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getFechaNac().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))));
        reportaColumn.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(cellData.getValue().getReportaA().getId()).asObject());


        cargarDatosDeBaseDeDatos();

        tablaEmpleados.setItems(categoriaData);
    }

    private void cargarDatosDeBaseDeDatos() {
        Session session = HibernateUtils.getSessionFactory().openSession();
        try {
            session.beginTransaction();

            // Realizar la consulta con JOIN FETCH para evitar LazyInitializationException
            String hql = "SELECT p FROM Empleado p " +
                    "JOIN FETCH p.reportaA ";
            Query<Empleado> query = session.createQuery(hql, Empleado.class);

            // Obtener los resultados de la consulta
            List<Empleado> productos = query.getResultList();

            // Limpiar la lista actual y agregar los nuevos resultados
            categoriaData.clear();
            categoriaData.addAll(productos);

            // Confirmar la transacción
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    @FXML
    void OnAgregarClicked(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("generic-empleado-view.fxml"));

        AgregarEmpleado agregarempleado = new AgregarEmpleado();
        loader.setController(agregarempleado);

        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setTitle("Agregar Empleado");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
        actualizarTabla();
    }

    @FXML
    void OnEliminarClick(MouseEvent event) {
        Empleado empleado = tablaEmpleados.getSelectionModel().getSelectedItem();

        if (empleado != null) {
            ServicioBorrar.eliminarCategria(empleado, categoriaData);
        } else {
            // Mostrar un mensaje de advertencia si no se seleccionó ningún elemento
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Advertencia");
            alert.setHeaderText(null);
            alert.setContentText("Por favor, seleccione un elemento para eliminar.");
            alert.showAndWait();
        }
        actualizarTabla();
    }

    @FXML
    void OnModificarClick(MouseEvent event) throws IOException {
        if (tablaEmpleados.getSelectionModel().getSelectedItem() == null) {
            // Si no hay una fila seleccionada, muestra un mensaje de alerta
            mostrarAlerta("Error", "Debe seleccionar una fila antes de continuar.");
            return;
        }
        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("generic-empleado-view.fxml"));
        Empleado empleado = tablaEmpleados.getSelectionModel().getSelectedItem();

        ModificarEmpleado modificarEmpleado = new ModificarEmpleado();
        modificarEmpleado.setEmpleado(empleado);
        loader.setController(modificarEmpleado);

        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setTitle("Modificar Categoria");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
        actualizarTabla();
    }

    private void mostrarAlerta(String titulo, String contenido) {
        Alert alerta = new Alert(Alert.AlertType.WARNING);
        alerta.setTitle(titulo);
        alerta.setContentText(contenido);
        alerta.showAndWait();
    }

    private void actualizarTabla() {
        cargarDatosDeBaseDeDatos();

        tablaEmpleados.setItems(categoriaData);
    }

}
