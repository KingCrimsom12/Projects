package com.example.hibernate.Service;

import com.example.hibernate.HelloApplication;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class CambiarViewService
{
    public static void cambiarView(String view, String titulo) throws IOException {
        URL resource = HelloApplication.class.getResource(view);
        if (resource == null) {
            throw new IOException("No se pudo encontrar el recurso FXML: " + view);
        }
        FXMLLoader loader = new FXMLLoader(resource);
        Parent root = loader.load();

        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setTitle(titulo);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.show();
    }
}
