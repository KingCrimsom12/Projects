package com.example.hibernate.Controllers.Botones;

import com.example.hibernate.Database.DetalleOrdene;
import com.example.hibernate.Database.DetalleOrdeneId;
import com.example.hibernate.Database.Ordene;
import com.example.hibernate.Database.Producto;
import com.example.hibernate.HibernateUtils.HibernateUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class ModificarDetalleOrden {

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

    private DetalleOrdene detalleOrdene;

    public void setDetalleOrdene(DetalleOrdene detalleOrdene) {
        this.detalleOrdene = detalleOrdene;
    }

    @FXML
    private void initialize() {
        btnCategoria.setText("Modificar");
        labelID.setText(detalleOrdene.getId().getDetalleid().toString());
        labelOrdenId.setText(detalleOrdene.getId().getOrdenid().toString());
        labelProductoId.setText(detalleOrdene.getProductoid().getId().toString());
        labelCantidad.setText(detalleOrdene.getCantidad().toString());
    }

    @FXML
    void OnClickedButton(MouseEvent event) {
        if (labelOrdenId.getText().isEmpty() || labelProductoId.getText().isEmpty() || labelCantidad.getText().isEmpty()) {
            mostrarAlerta("Error", "Todos los campos son obligatorios.");
            return;
        }

        try {
            DetalleOrdeneId detalleOrdeneId = new DetalleOrdeneId();
            detalleOrdeneId.setOrdenid(Integer.parseInt(labelOrdenId.getText()));
            detalleOrdeneId.setDetalleid(Integer.parseInt(labelProductoId.getText()));

            Session session = HibernateUtils.getSessionFactory().openSession();

            DetalleOrdene detalleExistente = session.get(DetalleOrdene.class, detalleOrdeneId);

            if (detalleExistente == null) {
                mostrarAlerta("Error", "El Detalle de Orden con este ID no existe.");
                session.close();
                return;
            }

            Ordene ordene = session.get(Ordene.class, detalleOrdeneId.getOrdenid());
            Producto producto = session.get(Producto.class, detalleOrdeneId.getDetalleid());

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

            detalleExistente.setOrdenid(ordene);
            detalleExistente.setProductoid(producto);
            detalleExistente.setCantidad(Integer.parseInt(labelCantidad.getText()));

            Transaction transaction = session.beginTransaction();
            session.update(detalleExistente);
            transaction.commit();
            session.close();

            mostrarAlerta("Éxito", "El Detalle de Orden se ha modificado correctamente.");

            ((Button) event.getSource()).getScene().getWindow().hide();
        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "Los campos Orden ID, Producto ID y Cantidad deben ser números válidos.");
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error", "Hubo un problema al modificar el Detalle de Orden.");
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
