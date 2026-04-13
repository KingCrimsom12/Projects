package com.example.hibernate.HibernateUtils;

import com.example.hibernate.Database.*;
import javafx.scene.control.Alert;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtils {

    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            // Cambia aquí la configuración si el archivo XML u otros ajustes son incorrectos
            Configuration configuration = new Configuration();
            return configuration.configure("hibernate.cfg.xml")
                    .addAnnotatedClass(Categoria.class)
                    .addAnnotatedClass(Empleado.class)
                    .addAnnotatedClass(Producto.class)
                    .addAnnotatedClass(Cliente.class)
                    .addAnnotatedClass(DetalleOrdene.class)
                    .addAnnotatedClass(DetalleOrdeneId.class)
                    .addAnnotatedClass(Ordene.class)
                    .addAnnotatedClass(Proveedore.class)
                    .buildSessionFactory();
        } catch (HibernateException e) {
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setTitle("Error de Conexión");
            alerta.setHeaderText("No se pudo conectar a la base de datos");
            alerta.setContentText("Verifique las credenciales de la base de datos (usuario/contraseña).");
            alerta.showAndWait();
            e.printStackTrace();
            throw new RuntimeException("Error de conexión a la base de datos", e);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void close() {
        getSessionFactory().close();
    }
}
