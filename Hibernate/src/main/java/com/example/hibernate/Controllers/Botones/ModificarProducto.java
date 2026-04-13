package com.example.hibernate.Controllers.Botones;

import com.example.hibernate.Database.Categoria;
import com.example.hibernate.Database.Producto;
import com.example.hibernate.Database.Proveedore;
import com.example.hibernate.HibernateUtils.HibernateUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import org.hibernate.Session;

public class ModificarProducto {

    @FXML
    private Button btnCategoria;

    @FXML
    private TextField labelCategoriaId;

    @FXML
    private TextField labelDescripcion;

    @FXML
    private TextField labelExistencias;

    @FXML
    private TextField labelPrecioUnit;

    @FXML
    private TextField labelProductoId;

    @FXML
    private TextField labelProveedorId;

    private Producto producto;

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    @FXML
    private void initialize() {
        btnCategoria.setText("Modificar");
        if (producto != null) {
            labelProductoId.setText(producto.getId().toString());
            labelProveedorId.setText(producto.getProveedorid().getId().toString());
            labelCategoriaId.setText(producto.getCategoriaid().getId().toString());
            labelDescripcion.setText(producto.getDescripcion());
            labelPrecioUnit.setText(producto.getPreciounit().toString());
            labelExistencias.setText(producto.getExistencia().toString());
        }
    }

    @FXML
    void OnClickedButton(MouseEvent event) {
        if (labelProductoId.getText().isEmpty() || labelProveedorId.getText().isEmpty() ||
                labelCategoriaId.getText().isEmpty() || labelPrecioUnit.getText().isEmpty() ||
                labelExistencias.getText().isEmpty()) {
            mostrarAlerta("Error", "Todos los campos obligatorios deben ser completados.");
            return;
        }

        try {
            Integer productoId = Integer.parseInt(labelProductoId.getText());
            Integer proveedorId = Integer.parseInt(labelProveedorId.getText());
            Integer categoriaId = Integer.parseInt(labelCategoriaId.getText());
            Double precioUnit = Double.parseDouble(labelPrecioUnit.getText());
            Integer existencias = Integer.parseInt(labelExistencias.getText());
            String descripcion = labelDescripcion.getText();

            Session session = HibernateUtils.getSessionFactory().openSession();
            try {
                session.beginTransaction();

                Producto productoExistente = session.get(Producto.class, productoId);
                if (productoExistente == null) {
                    mostrarAlerta("Error", "No se encuentra un producto con este ID.");
                    session.close();
                    return;
                }

                Proveedore proveedor = session.get(Proveedore.class, proveedorId);
                if (proveedor == null) {
                    mostrarAlerta("Error", "El proveedor especificado no existe.");
                    session.close();
                    return;
                }

                Categoria categoria = session.get(Categoria.class, categoriaId);
                if (categoria == null) {
                    mostrarAlerta("Error", "La categoría especificada no existe.");
                    session.close();
                    return;
                }

                productoExistente.setProveedorid(proveedor);
                productoExistente.setCategoriaid(categoria);
                productoExistente.setDescripcion(descripcion);
                productoExistente.setPreciounit(precioUnit);
                productoExistente.setExistencia(existencias);

                session.update(productoExistente);
                session.getTransaction().commit();
                mostrarAlerta("Éxito", "El producto ha sido actualizado correctamente.");
            } catch (Exception e) {
                e.printStackTrace();
                mostrarAlerta("Error", "Ocurrió un problema al actualizar el producto.");
            } finally {
                session.close();
            }

            ((Button) event.getSource()).getScene().getWindow().hide();

        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "Los campos ProductoID, ProveedorID, CategoriaID, Precio Unitario y Existencias deben ser valores numéricos.");
        }
    }

    // Método para mostrar alertas
    private void mostrarAlerta(String titulo, String contenido) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setContentText(contenido);
        alerta.showAndWait();
    }

}
