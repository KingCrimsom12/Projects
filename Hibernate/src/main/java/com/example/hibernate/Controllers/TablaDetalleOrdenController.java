package com.example.hibernate.Controllers;

import com.example.hibernate.Controllers.Botones.AgregarCliente;
import com.example.hibernate.Controllers.Botones.AgregarDetalleOrden;
import com.example.hibernate.Controllers.Botones.ModificarDetalleOrden;
import com.example.hibernate.Database.*;
import com.example.hibernate.HelloApplication;
import com.example.hibernate.HibernateUtils.HibernateUtils;
import com.example.hibernate.Service.ServicioBorrar;
import javafx.beans.property.SimpleIntegerProperty;
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
import java.util.List;

public class TablaDetalleOrdenController {

    @FXML
    private TableColumn<DetalleOrdene, Integer> cantidadColumn;

    @FXML
    private TableColumn<DetalleOrdene, Integer> detalleIDColumn;

    @FXML
    private TableColumn<DetalleOrdene, Integer> ordenIdColumn;

    @FXML
    private TableColumn<DetalleOrdene, Integer> productoIDColumn;

    @FXML
    private TableView<DetalleOrdene> tablaDetalleOrden;

    private ObservableList<DetalleOrdene> categoriaData = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        cantidadColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getCantidad()).asObject());
        detalleIDColumn.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(cellData.getValue().getId().getDetalleid()).asObject()
        );
        ordenIdColumn.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(cellData.getValue().getOrdenid().getId()).asObject());
        productoIDColumn.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(cellData.getValue().getProductoid().getId()).asObject());

        cargarDatosDeBaseDeDatos();

        tablaDetalleOrden.setItems(categoriaData);
    }

    private void cargarDatosDeBaseDeDatos() {
        Session session = HibernateUtils.getSessionFactory().openSession();
        try {
            session.beginTransaction();
            String hql = "SELECT p FROM DetalleOrdene p " +
                    "JOIN FETCH p.productoid " + // Hacer el join con la tabla Proveedore
                    "JOIN FETCH p.ordenid";
            Query<DetalleOrdene> query = session.createQuery(hql, DetalleOrdene.class);
            List<DetalleOrdene> categorias = query.getResultList();
            categoriaData.clear(); // Clear existing data
            categoriaData.addAll(categorias); // Add new data
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            // Log and handle exception
        } finally {
            session.close();
        }
    }

    @FXML
    void OnAgregarClicked(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("generic-detalle-orden-view.fxml"));

        AgregarDetalleOrden agregarDetalleOrden = new AgregarDetalleOrden();
        loader.setController(agregarDetalleOrden);

        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setTitle("Agregar Detalle Orden");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
        actualizarTabla();
    }

    @FXML
    void OnEliminarClick(MouseEvent event) {
        DetalleOrdene detalleSeleccionado = tablaDetalleOrden.getSelectionModel().getSelectedItem();

        if (detalleSeleccionado != null) {
            ServicioBorrar.eliminarCategria(detalleSeleccionado, categoriaData);
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
        if (tablaDetalleOrden.getSelectionModel().getSelectedItem() == null) {
            // Si no hay una fila seleccionada, muestra un mensaje de alerta
            mostrarAlerta("Error", "Debe seleccionar una fila antes de continuar.");
            return;
        }
        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("generic-detalle-orden-view.fxml"));
        DetalleOrdene detalleOrdene = tablaDetalleOrden.getSelectionModel().getSelectedItem();

        ModificarDetalleOrden modificarDetalleOrden = new ModificarDetalleOrden();
        modificarDetalleOrden.setDetalleOrdene(detalleOrdene);
        loader.setController(modificarDetalleOrden);

        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setTitle("Modificar Detalle Orden");
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

        tablaDetalleOrden.setItems(categoriaData);
    }

}
