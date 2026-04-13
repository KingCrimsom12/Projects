package com.example.hibernate.Controllers.Botones;

import com.example.hibernate.Database.Cliente;
import com.example.hibernate.HibernateUtils.HibernateUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.Random;

public class AgregarCliente {

    @FXML
    private Button btnCliente;

    @FXML
    private TextField labelContacto;

    @FXML
    private TextField labelD;

    @FXML
    private TextField labelDireccion;

    @FXML
    private TextField labelEmail;

    @FXML
    private TextField labelFax;

    @FXML
    private TextField labelFijo;

    @FXML
    private TextField labelNombre;

    @FXML
    private TextField labelcedula;

    @FXML
    private TextField labelmovil;

    @FXML
    private void initialize() {
        btnCliente.setText("Agregar");
    }

    @FXML
    void OnClickedButton(MouseEvent event) {
        if (labelcedula.getText().isEmpty() || labelNombre.getText().isEmpty() || labelContacto.getText().isEmpty() ||
                labelDireccion.getText().isEmpty()) {
            mostrarAlerta("Error", "Todos los campos requeridos deben estar llenos.");
            return;
        }

        try {
            Integer id = Integer.parseInt(labelD.getText());

            Session session = HibernateUtils.getSessionFactory().openSession();
            Cliente clienteExistente = session.get(Cliente.class, id);

            if (clienteExistente != null) {
                mostrarAlerta("Error", "El ID ya existe en la base de datos.");
                session.close();
                return;
            }

            Cliente cliente = new Cliente();
            cliente.setId(id);
            cliente.setCedulaRuc(labelcedula.getText());
            cliente.setNombrecia(labelNombre.getText());
            cliente.setNombrecontacto(labelContacto.getText());
            cliente.setDireccioncli(labelDireccion.getText());
            cliente.setFax(labelFax.getText());
            cliente.setEmail(labelEmail.getText());
            cliente.setCelular(labelmovil.getText());
            cliente.setFijo(labelFijo.getText());

            // Guardar cliente en la base de datos
            session.beginTransaction();
            session.save(cliente);
            session.getTransaction().commit();
            session.close();

            mostrarAlerta("Éxito", "El cliente se ha guardado correctamente.");

            ((Button) event.getSource()).getScene().getWindow().hide();
        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "El ID debe ser un número entero válido.");
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error", "Hubo un problema al guardar el cliente.");
        }
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
