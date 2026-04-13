package com.example.hibernate.Controllers;

import com.example.hibernate.Controllers.Botones.AgregarProducto;
import com.example.hibernate.Controllers.Botones.AgregarProveedor;
import com.example.hibernate.Controllers.Botones.ModificarProveedor;
import com.example.hibernate.Database.Categoria;
import com.example.hibernate.Database.Empleado;
import com.example.hibernate.Database.Producto;
import com.example.hibernate.Database.Proveedore;
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

public class TablaProveedorController {

    @FXML
    private TableColumn<Proveedore, String> contactoColumn;

    @FXML
    private TableColumn<Proveedore, String> fijoColumn;

    @FXML
    private TableColumn<Proveedore, String> movilColumn;

    @FXML
    private TableColumn<Proveedore, String> nombreProveedorColumn;

    @FXML
    private TableColumn<Proveedore, Integer> proveedorIdColumn;

    @FXML
    private TableView<Proveedore> tablaProveedor;

    private ObservableList<Proveedore> categoriaData = FXCollections.observableArrayList();

    @FXML
    private void initialize() {

        proveedorIdColumn.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        contactoColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getContacto()));
        fijoColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFijoprov()));
        movilColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCeluprov()));
        nombreProveedorColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNombreprov()));

        cargarDatosDeBaseDeDatos();

        tablaProveedor.setItems(categoriaData);
    }

    private void cargarDatosDeBaseDeDatos() {
        Session session = HibernateUtils.getSessionFactory().openSession();
        try {
            session.beginTransaction();

            Query<Proveedore> query = session.createQuery("from Proveedore", Proveedore.class);
            List<Proveedore> categorias = query.getResultList();
            categoriaData.clear(); // Clear existing data
            categoriaData.addAll(categorias); // Add new data
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    @FXML
    void OnAgregarClicked(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("generic-proveedore-view.fxml"));

        AgregarProveedor agregarProveedor = new AgregarProveedor();
        loader.setController(agregarProveedor);

        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setTitle("Agregar Proveedor");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
        actualizarTabla();
    }

    @FXML
    void OnEliminarClick(MouseEvent event) {
        Proveedore proveedor = tablaProveedor.getSelectionModel().getSelectedItem();

        if (proveedor != null) {
            ServicioBorrar.eliminarCategria(proveedor, categoriaData);
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
        if (tablaProveedor.getSelectionModel().getSelectedItem() == null) {
            // Si no hay una fila seleccionada, muestra un mensaje de alerta
            mostrarAlerta("Error", "Debe seleccionar una fila antes de continuar.");
            return;
        }

        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("generic-proveedore-view.fxml"));
        Proveedore proveedore = tablaProveedor.getSelectionModel().getSelectedItem();

        ModificarProveedor modificarProveedor = new ModificarProveedor();
        modificarProveedor.setProveedor(proveedore);
        loader.setController(modificarProveedor);

        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setTitle("Modificar Proveedor");
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

        tablaProveedor.setItems(categoriaData);
    }

}
