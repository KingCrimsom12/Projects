package com.example.hibernate.Controllers;

import com.example.hibernate.Controllers.Botones.AgregarCategoria;
import com.example.hibernate.Controllers.Botones.ModificarCategoria;
import com.example.hibernate.Database.Categoria;
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

public class TablaCategoriaController {  // Correct class name
    @FXML
    private TableView<Categoria> TablaCategoria;

    @FXML
    private TableColumn<Categoria, Integer> CategoriaId;

    @FXML
    private TableColumn<Categoria, String> NombreCategoria;

    private ObservableList<Categoria> categoriaData = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        // Configurar las columnas
        CategoriaId.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        NombreCategoria.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNombrecat()));

        // Cargar los datos desde la base de datos
        cargarDatosDeBaseDeDatos();

        // Establecer los datos en la tabla
        TablaCategoria.setItems(categoriaData);
    }

    private void cargarDatosDeBaseDeDatos() {
        Session session = HibernateUtils.getSessionFactory().openSession();
        try {
            session.beginTransaction();
            Query<Categoria> query = session.createQuery("from Categoria", Categoria.class);
            List<Categoria> categorias = query.getResultList();
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
        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("generic-categoria-view.fxml"));

        AgregarCategoria agregarCategoria = new AgregarCategoria();
        loader.setController(agregarCategoria);

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
        Categoria detalleSeleccionado = TablaCategoria.getSelectionModel().getSelectedItem();

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
        if (TablaCategoria.getSelectionModel().getSelectedItem() == null) {
            // Si no hay una fila seleccionada, muestra un mensaje de alerta
            mostrarAlerta("Error", "Debe seleccionar una fila antes de continuar.");
            return;
        }
        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("generic-categoria-view.fxml"));
        Categoria categoriaSeleccionada = TablaCategoria.getSelectionModel().getSelectedItem();

        ModificarCategoria modificarCategoria = new ModificarCategoria();

        // Pasar la categoría seleccionada al controlador de la ventana de modificación
        modificarCategoria.setCategoria(categoriaSeleccionada);


        loader.setController(modificarCategoria);

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

        TablaCategoria.setItems(categoriaData);
    }
}