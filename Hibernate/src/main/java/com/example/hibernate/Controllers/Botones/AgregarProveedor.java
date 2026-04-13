package com.example.hibernate.Controllers.Botones;

import com.example.hibernate.Database.Proveedore;
import com.example.hibernate.HibernateUtils.HibernateUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import org.hibernate.Session;

public class AgregarProveedor {

    @FXML
    private Button btnCategoria;

    @FXML
    private TextField labelContacto;

    @FXML
    private TextField labelFijo;

    @FXML
    private TextField labelMovil;

    @FXML
    private TextField labelNombre;

    @FXML
    private TextField labelProveedorId;

    @FXML
    private void initialize() {
        btnCategoria.setText("Agregar");
    }

    @FXML
    void OnClickedButton(MouseEvent event) {
        if (labelProveedorId.getText().isEmpty() || labelNombre.getText().isEmpty() ||
                labelContacto.getText().isEmpty()) {
            mostrarAlerta("Error", "Todos los campos obligatorios deben ser completados.");
            return;
        }

        try {
            Integer proveedorId = Integer.parseInt(labelProveedorId.getText());
            String nombre = labelNombre.getText();
            String contacto = labelContacto.getText();
            String movil = labelMovil.getText();
            String fijo = labelFijo.getText();

            Session session = HibernateUtils.getSessionFactory().openSession();
            try {
                session.beginTransaction();

                Proveedore proveedorExistente = session.get(Proveedore.class, proveedorId);
                if (proveedorExistente != null) {
                    mostrarAlerta("Error", "Ya existe un proveedor con el ID especificado.");
                    session.close();
                    return;
                }

                Proveedore nuevoProveedor = new Proveedore();
                nuevoProveedor.setId(proveedorId);
                nuevoProveedor.setNombreprov(nombre);
                nuevoProveedor.setContacto(contacto);
                nuevoProveedor.setCeluprov(movil);
                nuevoProveedor.setFijoprov(fijo);

                session.save(nuevoProveedor);
                session.getTransaction().commit();
                mostrarAlerta("Éxito", "El proveedor se ha guardado correctamente.");
            } catch (Exception e) {
                e.printStackTrace();
                mostrarAlerta("Error", "Ocurrió un problema al guardar el proveedor.");
            } finally {
                session.close();
            }

            ((Button) event.getSource()).getScene().getWindow().hide();

        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "El ID del proveedor debe ser un valor numérico.");
        }
    }

    private void mostrarAlerta(String titulo, String contenido) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setContentText(contenido);
        alerta.showAndWait();
    }

}
