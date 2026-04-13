package com.example.hibernate.Controllers.Botones;

import com.example.hibernate.Database.Cliente;
import com.example.hibernate.Database.Empleado;
import com.example.hibernate.Database.Ordene;
import com.example.hibernate.HibernateUtils.HibernateUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import org.hibernate.Session;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class AgregarOrden {

    @FXML
    private Button btnCategoria;

    @FXML
    private TextField labelClienteID;

    @FXML
    private TextField labelDescuento;

    @FXML
    private TextField labelEmpleadoId;

    @FXML
    private TextField labelFecOrden;

    @FXML
    private TextField labelID;

    @FXML
    private void initialize() {
        btnCategoria.setText("Agregar");
    }


    @FXML
    void OnClickedButton(MouseEvent event) {
        if (labelID.getText().isEmpty() || labelClienteID.getText().isEmpty() ||
                labelEmpleadoId.getText().isEmpty() || labelFecOrden.getText().isEmpty()) {
            mostrarAlerta("Error", "Todos los campos obligatorios deben ser completados.");
            return;
        }

        try {
            Integer id = Integer.parseInt(labelID.getText());
            Integer clienteId = Integer.parseInt(labelClienteID.getText());
            Integer empleadoId = Integer.parseInt(labelEmpleadoId.getText());
            LocalDate fechaOrden = LocalDate.parse(labelFecOrden.getText());
            Integer descuento = labelDescuento.getText().isEmpty() ? null : Integer.parseInt(labelDescuento.getText());

            Session session = HibernateUtils.getSessionFactory().openSession();
            try {
                session.beginTransaction();

                Ordene ordenExistente = session.get(Ordene.class, id);
                if (ordenExistente != null) {
                    mostrarAlerta("Error", "Ya existe una orden con el ID especificado.");
                    session.close();
                    return;
                }

                Cliente cliente = session.get(Cliente.class, clienteId);
                if (cliente == null) {
                    mostrarAlerta("Error", "El cliente especificado no existe.");
                    session.close();
                    return;
                }

                Empleado empleado = session.get(Empleado.class, empleadoId);
                if (empleado == null) {
                    mostrarAlerta("Error", "El empleado especificado no existe.");
                    session.close();
                    return;
                }

                Ordene nuevaOrden = new Ordene();
                nuevaOrden.setId(id);
                nuevaOrden.setClienteid(cliente);
                nuevaOrden.setEmpleadoid(empleado);
                nuevaOrden.setFechaorden(fechaOrden);
                nuevaOrden.setDescuento(descuento);

                session.save(nuevaOrden);
                session.getTransaction().commit();
                mostrarAlerta("Éxito", "La orden se ha guardado correctamente.");
            } catch (Exception e) {
                e.printStackTrace();
                mostrarAlerta("Error", "Ocurrió un problema al guardar la orden.");
            } finally {
                session.close();
            }

            ((Button) event.getSource()).getScene().getWindow().hide();

        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "ID, ClienteID, EmpleadoID y Descuento deben ser valores numéricos.");
        } catch (DateTimeParseException e) {
            mostrarAlerta("Error", "La fecha de orden debe tener el formato yyyy-MM-dd.");
        }
    }

    private void mostrarAlerta(String titulo, String contenido) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setContentText(contenido);
        alerta.showAndWait();
    }

}
