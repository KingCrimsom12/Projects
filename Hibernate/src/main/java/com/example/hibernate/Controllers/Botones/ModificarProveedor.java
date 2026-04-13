package com.example.hibernate.Controllers.Botones;

import com.example.hibernate.Database.Proveedore;
import com.example.hibernate.HibernateUtils.HibernateUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import org.hibernate.Session;

public class ModificarProveedor {

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


    private Proveedore proveedor;

    public void setProveedor(Proveedore proveedor) {
        this.proveedor = proveedor;
    }

    @FXML
    private void initialize() {
        btnCategoria.setText("Modificar");
        if (proveedor != null) {
            labelProveedorId.setText(proveedor.getId().toString());
            labelNombre.setText(proveedor.getNombreprov());
            labelContacto.setText(proveedor.getContacto());
            labelMovil.setText(proveedor.getCeluprov());
            labelFijo.setText(proveedor.getFijoprov());
        }
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

                // Verificar si el proveedor existe en la base de datos
                Proveedore proveedorExistente = session.get(Proveedore.class, proveedorId);
                if (proveedorExistente == null) {
                    mostrarAlerta("Error", "No se encuentra un proveedor con este ID.");
                    session.close();
                    return;
                }

                // Actualizar los datos del proveedor
                proveedorExistente.setNombreprov(nombre);
                proveedorExistente.setContacto(contacto);
                proveedorExistente.setCeluprov(movil);
                proveedorExistente.setFijoprov(fijo);

                session.update(proveedorExistente);
                session.getTransaction().commit();
                mostrarAlerta("Éxito", "El proveedor ha sido actualizado correctamente.");
            } catch (Exception e) {
                e.printStackTrace();
                mostrarAlerta("Error", "Ocurrió un problema al actualizar el proveedor.");
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
