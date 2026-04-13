package com.example.hibernate.Controllers;

import com.example.hibernate.Controllers.Botones.AgregarEmpleado;
import com.example.hibernate.Controllers.Botones.AgregarOrden;
import com.example.hibernate.Controllers.Botones.ModificarOrden;
import com.example.hibernate.Database.Cliente;
import com.example.hibernate.Database.DetalleOrdene;
import com.example.hibernate.Database.Empleado;
import com.example.hibernate.Database.Ordene;
import com.example.hibernate.HelloApplication;
import com.example.hibernate.HibernateUtils.HibernateUtils;
import com.example.hibernate.Service.ServicioBorrar;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class TablaOrdenController {

    @FXML
    private TableColumn<Ordene, Integer> clienteIdColumn;

    @FXML
    private TableColumn<Ordene, Integer> descuentoColumn;

    @FXML
    private TableColumn<Ordene, Integer> empleadoIdColumn;

    @FXML
    private TableColumn<Ordene, LocalDate> fechaOrdenColumn;

    @FXML
    private TableColumn<Ordene, Integer> ordenIdColumn;

    @FXML
    private TableView<Ordene> tablaOrden;

    private ObservableList<Ordene> categoriaData = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        descuentoColumn.setCellValueFactory(cellData -> {
            Integer descuento = cellData.getValue().getDescuento();
            return new SimpleIntegerProperty(descuento != null ? descuento : 0).asObject();
        });
        fechaOrdenColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getFechaorden()));
        ordenIdColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        clienteIdColumn.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(cellData.getValue().getClienteid().getId()).asObject());
        empleadoIdColumn.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(cellData.getValue().getEmpleadoid().getId()).asObject());

        cargarDatosDeBaseDeDatos();

        tablaOrden.setItems(categoriaData);
    }

    private void cargarDatosDeBaseDeDatos() {
        Session session = HibernateUtils.getSessionFactory().openSession();
        try {
            session.beginTransaction();
            String hql = "SELECT p FROM Ordene p " +
                    "JOIN FETCH p.clienteid " +
                    "JOIN FETCH p.empleadoid";
            Query<Ordene> query = session.createQuery(hql, Ordene.class);
            List<Ordene> categorias = query.getResultList();
            categoriaData.clear();
            categoriaData.addAll(categorias);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    @FXML
    void OnAgregarClicked(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("generic-orden-view.fxml"));

        AgregarOrden agregarOrden = new AgregarOrden();
        loader.setController(agregarOrden);

        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setTitle("Agregar Categoria");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
        actualizarTabla();
    }

    @FXML
    void OnEliminarClick(MouseEvent event) {
        Ordene orden = tablaOrden.getSelectionModel().getSelectedItem();

        if (orden != null) {
            ServicioBorrar.eliminarCategria(orden, categoriaData);
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
        if (tablaOrden.getSelectionModel().getSelectedItem() == null) {
            // Si no hay una fila seleccionada, muestra un mensaje de alerta
            mostrarAlerta("Error", "Debe seleccionar una fila antes de continuar.");
            return;
        }
        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("generic-orden-view.fxml"));
        Ordene ordene = tablaOrden.getSelectionModel().getSelectedItem();

        ModificarOrden modificarOrden = new ModificarOrden();
        modificarOrden.setOrden(ordene);
        loader.setController(modificarOrden);

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

        tablaOrden.setItems(categoriaData);
    }

}
