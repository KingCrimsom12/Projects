package com.example.hibernate.Controllers.Botones;

import com.example.hibernate.Database.Categoria;
import com.example.hibernate.HibernateUtils.HibernateUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class ModificarCategoria {

    @FXML
    private Button btnCategoria;

    @FXML
    private TextField labelCat;

    @FXML
    private TextField labelNombre;

    private Categoria categoria;

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    @FXML
    private void initialize() {
        btnCategoria.setText("Modificar");
        labelCat.setText(categoria.getId().toString());
        labelNombre.setText(categoria.getNombrecat());
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

        if (nombreCat != null && nombreCat.length() > 100) {
            mostrarAlerta("Error de validación", "El campo 'Descripción' no puede tener más de 100 caracteres.");
            return;
        }

        actualizarCategoriaEnBaseDeDatos(nombre, nombreCat);

        ((Button) event.getSource()).getScene().getWindow().hide();
    }

    private Integer validarEntero(String texto) {
        try {
            return Integer.parseInt(texto);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private void actualizarCategoriaEnBaseDeDatos(Integer id, String nombreCat) {
        Session session = HibernateUtils.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            session.beginTransaction();
            Categoria categoriaExistente = session.get(Categoria.class, id);

            if (categoriaExistente != null) {
                categoriaExistente.setNombrecat(nombreCat);

                session.update(categoriaExistente);
                session.getTransaction().commit();
                mostrarAlerta("Éxito", "Categoría modificada correctamente.");
            } else {
                mostrarAlerta("Error", "No se encontró la categoría con el ID proporcionado.");
            }
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            mostrarAlerta("Error", "Hubo un error al actualizar la categoría.");
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