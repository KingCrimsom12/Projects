package com.example.hibernate.Controllers;

import com.example.hibernate.Controllers.Botones.AgregarCliente;
import com.example.hibernate.Controllers.Botones.ModificarCategoria;
import com.example.hibernate.Controllers.Botones.ModificarCliente;
import com.example.hibernate.Database.Categoria;
import com.example.hibernate.Database.Cliente;
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
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.io.IOException;
import java.util.List;

public class TablaClienteController {

    @FXML
    private TableColumn<Cliente, String> cedulaColumn;

    @FXML
    private TableColumn<Cliente, String> direccionColumn;

    @FXML
    private TableColumn<Cliente, String> emailColumn;

    @FXML
    private TableColumn<Cliente, String> faxColumn;

    @FXML
    private TableColumn<Cliente, String> fijoColumn;

    @FXML
    private TableColumn<Cliente, Integer> idColumn;

    @FXML
    private TableColumn<Cliente, String> movilColumn;

    @FXML
    private TableColumn<Cliente, String> nombreColumn;

    @FXML
    private TableColumn<Cliente, String> nombreContactoColumn;

    @FXML
    private TableView<Cliente> tablaCliente;

    private ObservableList<Cliente> categoriaData = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        idColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        cedulaColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCedulaRuc()));
        direccionColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDireccioncli()));
        fijoColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFijo()));
        movilColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCelular()));
        nombreColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNombrecontacto()));
        nombreContactoColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNombrecontacto()));
        emailColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmail()));
        faxColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFax()));

        cargarDatosDeBaseDeDatos();

        tablaCliente.setItems(categoriaData);
    }

    private void cargarDatosDeBaseDeDatos() {
        Session session = HibernateUtils.getSessionFactory().openSession();
        try {
            session.beginTransaction();
            Query<Cliente> query = session.createQuery("from Cliente", Cliente.class);
            List<Cliente> categorias = query.getResultList();
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
        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("generic-cliente-view.fxml"));

        AgregarCliente agregarCliente = new AgregarCliente();
        loader.setController(agregarCliente);

        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setTitle("Agregar Cliente");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
        actualizarTabla();
    }

    @FXML
    void OnEliminarClick(MouseEvent event) {
        Cliente detalleSeleccionado = tablaCliente.getSelectionModel().getSelectedItem();

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
        if (tablaCliente.getSelectionModel().getSelectedItem() == null) {
            // Si no hay una fila seleccionada, muestra un mensaje de alerta
            mostrarAlerta("Error", "Debe seleccionar una fila antes de continuar.");
            return;
        }
        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("generic-cliente-view.fxml"));
        Cliente cliente = tablaCliente.getSelectionModel().getSelectedItem();

        ModificarCliente modificarCliente = new ModificarCliente();

        modificarCliente.setCliente(cliente);
        loader.setController(modificarCliente);

        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setTitle("Modificar Cliente");
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

        tablaCliente.setItems(categoriaData);
    }

}

