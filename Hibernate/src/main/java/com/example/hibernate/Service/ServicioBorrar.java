package com.example.hibernate.Service;

import com.example.hibernate.HibernateUtils.HibernateUtils;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import org.hibernate.Session;

public class ServicioBorrar
{
    public static void eliminarCategria(Object categoria, ObservableList<?> categoriaData)
    {
        Session session = HibernateUtils.getSessionFactory().openSession();

        try {
            // Comenzar una transacción
            session.beginTransaction();

            // Eliminar el detalle de la base de datos
            session.delete(categoria);

            // Confirmar los cambios en la base de datos
            session.getTransaction().commit();

            // Eliminar el elemento de la lista observable (para actualizar la tabla)
            categoriaData.remove(categoria);

            // Mostrar un mensaje de éxito
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Éxito");
            alert.setHeaderText(null);
            alert.setContentText("Elemento eliminado exitosamente.");
            alert.showAndWait();
        } catch (Exception e) {
            // Si ocurre un error, hacer rollback de la transacción
            if (session.getTransaction() != null) {
                session.getTransaction().rollback();
            }

            // Mostrar un mensaje de error
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Hubo un error al intentar eliminar el elemento.");
            alert.showAndWait();

            e.printStackTrace();
        } finally {
            // Cerrar la sesión
            session.close();
        }
    }
}
