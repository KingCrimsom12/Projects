package com.example.hibernate.Controllers.Botones;

import com.example.hibernate.Database.Cliente;
import com.example.hibernate.HibernateUtils.HibernateUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class ModificarCliente {

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

    private Cliente cliente;

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    @FXML
    private void initialize() {
        btnCliente.setText("Modificar");
        labelcedula.setText(cliente.getCedulaRuc());
        labelNombre.setText(cliente.getNombrecia());
        labelContacto.setText(cliente.getNombrecontacto());
        labelDireccion.setText(cliente.getDireccioncli());
        labelFax.setText(cliente.getFax());
        labelEmail.setText(cliente.getEmail());
        labelmovil.setText(cliente.getCelular());
        labelFijo.setText(cliente.getFijo());
        labelD.setText(cliente.getId().toString());
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

            if (clienteExistente == null) {
                mostrarAlerta("Error", "El cliente con el ID proporcionado no existe en la base de datos.");
                session.close();
                return;
            }

            clienteExistente.setCedulaRuc(labelcedula.getText());
            clienteExistente.setNombrecia(labelNombre.getText());
            clienteExistente.setNombrecontacto(labelContacto.getText());
            clienteExistente.setDireccioncli(labelDireccion.getText());
            clienteExistente.setFax(labelFax.getText());
            clienteExistente.setEmail(labelEmail.getText());
            clienteExistente.setCelular(labelmovil.getText());
            clienteExistente.setFijo(labelFijo.getText());

            Transaction transaction = session.beginTransaction();
            session.update(clienteExistente);
            transaction.commit();
            session.close();

            mostrarAlerta("Éxito", "El cliente se ha modificado correctamente.");

            ((Button) event.getSource()).getScene().getWindow().hide();
        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "El ID debe ser un número entero válido.");
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error", "Hubo un problema al modificar el cliente.");
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
