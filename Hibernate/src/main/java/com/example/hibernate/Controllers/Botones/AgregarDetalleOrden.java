package com.example.hibernate.Controllers.Botones;

import com.example.hibernate.Database.DetalleOrdene;
import com.example.hibernate.Database.DetalleOrdeneId;
import com.example.hibernate.HibernateUtils.HibernateUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import org.hibernate.Session;

public class AgregarDetalleOrden {

    @FXML
    private Button btnCategoria;

    @FXML
    private TextField labelCantidad;

    @FXML
    private TextField labelID;

    @FXML
    private TextField labelOrdenId;

    @FXML
    private TextField labelProductoId;

    @FXML
    private void initialize() {
        btnCategoria.setText("Agregar");
    }

    @FXML
    void OnClickedButton(MouseEvent event) {
        if (labelID.getText().isEmpty() || labelOrdenId.getText().isEmpty() ||
                labelProductoId.getText().isEmpty() || labelCantidad.getText().isEmpty()) {
            mostrarAlerta("Error", "Todos los campos son obligatorios.");
            return;
        }

        try {
            DetalleOrdeneId detalleOrdeneId = new DetalleOrdeneId();
            detalleOrdeneId.setOrdenid(Integer.parseInt(labelOrdenId.getText()));
            detalleOrdeneId.setDetalleid(Integer.parseInt(labelProductoId.getText()));

            Session session = HibernateUtils.getSessionFactory().openSession();
            DetalleOrdene detalleExistente = session.get(DetalleOrdene.class, detalleOrdeneId);

            if (detalleExistente != null) {
                mostrarAlerta("Error", "El Detalle de Orden con este ID ya existe.");
                session.close();
                return;
            }

            com.example.hibernate.Database.Ordene ordene = session.get(com.example.hibernate.Database.Ordene.class, detalleOrdeneId.getOrdenid());
            com.example.hibernate.Database.Producto producto = session.get(com.example.hibernate.Database.Producto.class, detalleOrdeneId.getDetalleid());

            if (ordene == null) {
                mostrarAlerta("Error", "El Orden ID no existe.");
                session.close();
                return;
            }

            if (producto == null) {
                mostrarAlerta("Error", "El Producto ID no existe.");
                session.close();
                return;
            }

            DetalleOrdene nuevoDetalle = new DetalleOrdene();
            nuevoDetalle.setId(detalleOrdeneId);
            nuevoDetalle.setOrdenid(ordene);
            nuevoDetalle.setProductoid(producto);
            nuevoDetalle.setCantidad(Integer.parseInt(labelCantidad.getText()));

            session.beginTransaction();
            session.save(nuevoDetalle);
            session.getTransaction().commit();
            session.close();

            mostrarAlerta("Éxito", "El Detalle de Orden se ha guardado correctamente.");

            ((Button) event.getSource()).getScene().getWindow().hide();
        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "Los campos Orden ID, Producto ID y Cantidad deben ser números válidos.");
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error", "Hubo un problema al guardar el Detalle de Orden.");
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
