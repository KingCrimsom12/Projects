package com.example.hibernate.Controllers.Botones;

import com.example.hibernate.Database.Categoria;
import com.example.hibernate.HibernateUtils.HibernateUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import org.hibernate.Session;

public class AgregarCategoria {

    @FXML
    private Button btnCategoria;

    @FXML
    private TextField labelCat;

    @FXML
    private TextField labelNombre;

    @FXML
    private void initialize() {
        btnCategoria.setText("Agregar");
    }


    @FXML
    void OnClickedButton(MouseEvent event) {
        String nombreTexto = labelNombre.getText();
        String nombreCat = labelCat.getText();

        Integer nombre = validarEntero(nombreTexto);
        if (nombre == null) {
            mostrarAlerta("Error de validación", "El campo 'Nombre' debe ser un número entero.");
            return;
        }

        // Validar longitud de la descripción
        if (nombreCat != null && nombreCat.length() > 100) {
            mostrarAlerta("Error de validación", "El campo 'Descripción' no puede tener más de 100 caracteres.");
            return;
        }

        guardarCategoriaEnBaseDeDatos(nombre, nombreCat);

        ((Button) event.getSource()).getScene().getWindow().hide();
    }

    private Integer validarEntero(String texto) {
        try {
            return Integer.parseInt(texto);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private void guardarCategoriaEnBaseDeDatos(Integer nombre, String descripcion) {
        Session session = HibernateUtils.getSessionFactory().openSession();
        try {
            Categoria categoria = new Categoria();
            categoria.setId(nombre);
            categoria.setNombrecat(descripcion);

            session.beginTransaction();

            session.save(categoria);

            session.getTransaction().commit();

            mostrarAlerta("Éxito", "Categoría agregada correctamente.");

        } catch (Exception e) {
            if (session.getTransaction() != null) {
                session.getTransaction().rollback();
            }
            mostrarAlerta("Error", "Hubo un error al guardar la categoría.");
            e.printStackTrace();
        } finally {
            session.close();
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