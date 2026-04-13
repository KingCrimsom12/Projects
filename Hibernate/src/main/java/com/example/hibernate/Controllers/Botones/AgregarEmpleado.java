package com.example.hibernate.Controllers.Botones;

import com.example.hibernate.Database.Empleado;
import com.example.hibernate.HibernateUtils.HibernateUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import org.hibernate.Session;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class AgregarEmpleado {

    @FXML
    private Button btnCategoria;

    @FXML
    private TextField labelApellido;

    @FXML
    private TextField labelExtension;

    @FXML
    private TextField labelFechaNac;

    @FXML
    private TextField labelID;

    @FXML
    private TextField labelNombre;

    @FXML
    private TextField labelReport;

    @FXML
    private void initialize() {
        btnCategoria.setText("Agregar");
    }

    @FXML
    void OnClickedButton(MouseEvent event) {
        if (labelID.getText().isEmpty() || labelNombre.getText().isEmpty() ||
                labelApellido.getText().isEmpty() || labelFechaNac.getText().isEmpty() ||
                labelExtension.getText().isEmpty()) {
            mostrarAlerta("Error", "Todos los campos obligatorios deben ser completados.");
            return;
        }

        try {
            Integer id = Integer.parseInt(labelID.getText());
            Integer extension = Integer.parseInt(labelExtension.getText());
            LocalDate fechaNac = LocalDate.parse(labelFechaNac.getText()); // Formato ISO (yyyy-MM-dd)

            Session session = HibernateUtils.getSessionFactory().openSession();
            try {
                session.beginTransaction();

                Empleado empleadoExistente = session.get(Empleado.class, id);
                if (empleadoExistente != null) {
                    mostrarAlerta("Error", "Ya existe un empleado con el ID especificado.");
                    session.close();
                    return;
                }

                Empleado nuevoEmpleado = new Empleado();
                nuevoEmpleado.setId(id);
                nuevoEmpleado.setNombre(labelNombre.getText());
                nuevoEmpleado.setApellido(labelApellido.getText());
                nuevoEmpleado.setFechaNac(fechaNac);
                nuevoEmpleado.setExtension(extension);

                if (!labelReport.getText().isEmpty()) {
                    Integer reportaAId = Integer.parseInt(labelReport.getText());
                    Empleado supervisor = session.get(Empleado.class, reportaAId);
                    if (supervisor == null) {
                        mostrarAlerta("Error", "El supervisor especificado no existe.");
                        session.close();
                        return;
                    }
                    nuevoEmpleado.setReportaA(supervisor);
                }

                session.save(nuevoEmpleado);
                session.getTransaction().commit();
                mostrarAlerta("Éxito", "El empleado se ha guardado correctamente.");
            } catch (Exception e) {
                e.printStackTrace();
                mostrarAlerta("Error", "Ocurrió un problema al guardar el empleado.");
            } finally {
                session.close();
            }

            ((Button) event.getSource()).getScene().getWindow().hide();

        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "ID, Extensión y Reporta A deben ser valores numéricos.");
        } catch (DateTimeParseException e) {
            mostrarAlerta("Error", "La fecha de nacimiento debe tener el formato yyyy-MM-dd.");
        }
    }

    private void mostrarAlerta(String titulo, String contenido) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setContentText(contenido);
        alerta.showAndWait();
    }
}
