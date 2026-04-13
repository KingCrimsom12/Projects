package com.example.hibernate.Controllers;

import com.example.hibernate.Controllers.Botones.AgregarOrden;
import com.example.hibernate.Controllers.Botones.AgregarProducto;
import com.example.hibernate.Controllers.Botones.ModificarProducto;
import com.example.hibernate.Database.*;
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
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.io.IOException;
import java.util.List;

public class TablaProductoController {

    @FXML
    private TableColumn<Producto, Integer> categoriaIdColumn;

    @FXML
    private TableColumn<Producto, String> descripcionColumn;

    @FXML
    private TableColumn<Producto, Integer> existenciasColumn;

    @FXML
    private TableColumn<Producto, Double> precioUnidadColumn;

    @FXML
    private TableColumn<Producto, Integer> productoIdColumn;

    @FXML
    private TableColumn<Producto, Integer> proveedorIdColumn;

    @FXML
    private TableView<Producto> tablaProducto;

    private ObservableList<Producto> categoriaData = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        descripcionColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescripcion()));
        existenciasColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getExistencia()).asObject());
        productoIdColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        precioUnidadColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getPreciounit()));
        categoriaIdColumn.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(cellData.getValue().getCategoriaid().getId()).asObject());
        proveedorIdColumn.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(cellData.getValue().getProveedorid().getId()).asObject());


        cargarDatosDeBaseDeDatos();

        // Establecer los datos en la tabla
        tablaProducto.setItems(categoriaData);
    }

    private void cargarDatosDeBaseDeDatos() {
        Session session = HibernateUtils.getSessionFactory().openSession();
        try {
            session.beginTransaction();

            // Realizar la consulta con JOIN FETCH para evitar LazyInitializationException
            String hql = "SELECT p FROM Producto p " +
                    "JOIN FETCH p.proveedorid " + // Hacer el join con la tabla Proveedore
                    "JOIN FETCH p.categoriaid"; // Hacer el join con la tabla Categoria
            Query<Producto> query = session.createQuery(hql, Producto.class);

            // Obtener los resultados de la consulta
            List<Producto> productos = query.getResultList();

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
        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("generic-producto-view.fxml"));

        AgregarProducto agregarProducto = new AgregarProducto();
        loader.setController(agregarProducto);

        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setTitle("Agregar Producto");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
        actualizarTabla();
    }

    @FXML
    void OnEliminarClick(MouseEvent event) {
        Producto producto = tablaProducto.getSelectionModel().getSelectedItem();

        if (producto != null) {
            ServicioBorrar.eliminarCategria(producto, categoriaData);
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
        if (tablaProducto.getSelectionModel().getSelectedItem() == null) {
            // Si no hay una fila seleccionada, muestra un mensaje de alerta
            mostrarAlerta("Error", "Debe seleccionar una fila antes de continuar.");
            return;
        }
        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("generic-producto-view.fxml"));
        Producto producto = tablaProducto.getSelectionModel().getSelectedItem();

        ModificarProducto modificarProducto = new ModificarProducto();
        modificarProducto.setProducto(producto);
        loader.setController(modificarProducto);

        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setTitle("Modificar Producto");
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

        tablaProducto.setItems(categoriaData);
    }

}
