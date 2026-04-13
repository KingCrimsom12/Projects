package com.example.hibernate.Controllers;

import com.example.hibernate.Service.CambiarViewService;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

public class HelloController {

    @FXML
    void OnClickCategoria(MouseEvent event) throws IOException {
        CambiarViewService.cambiarView("tabla-categoria-view.fxml", "Categorias");
    }

    @FXML
    void OnClickCliente(MouseEvent event) throws IOException {
        CambiarViewService.cambiarView("tabla-cliente-view.fxml", "Cliente");
    }

    @FXML
    void OnClickDetalleOrden(MouseEvent event) throws IOException {
        CambiarViewService.cambiarView("tabla-detalle-orden-view.fxml", "Detalle Orden");
    }

    @FXML
    void OnClickEmpleado(MouseEvent event) throws IOException {
        CambiarViewService.cambiarView("tabla-empleado-view.fxml", "Empleado");
    }

    @FXML
    void OnClickOrden(MouseEvent event) throws IOException {
        CambiarViewService.cambiarView("tabla-orden-view.fxml", "Orden");
    }

    @FXML
    void OnClickProducto(MouseEvent event) throws IOException {
        CambiarViewService.cambiarView("tabla-producto-view.fxml", "Producto");
    }

    @FXML
    void OnClickProveedores(MouseEvent event) throws IOException {
        CambiarViewService.cambiarView("tabla-proveedor-view.fxml", "Proveedores");
    }

}
