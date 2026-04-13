package org.example;

import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import org.bson.Document;

import java.util.Scanner;

public class Main {
    private static final String CONEXION_STRING = "mongodb+srv://kingcrimsomau:!Febrero2005@cluster0.l1g49.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0";
    private static final String NOMBRE_DATABASE = "testDB";
    private static final String NOMBRE_COLECCION = "testCollection";
    private static MongoCollection<Document> collection;
    private static Scanner sc;

    private static void initialize()
    {
        sc = new Scanner(System.in);
    }

    public static void main(String[] args) {
        try (MongoClient mongoClient = MongoClients.create(CONEXION_STRING)) {
            MongoDatabase database = mongoClient.getDatabase(NOMBRE_DATABASE);
            collection = database.getCollection(NOMBRE_COLECCION);
            initialize();
            int opcion;
            do {
                try {
                    mostrarMenu();
                    opcion = Integer.parseInt(sc.nextLine());
                    gestionarOpcion(opcion);
                } catch (NumberFormatException e) {
                    System.out.println("Error: Debe ingresar un número válido.");
                    opcion = 0;
                }
            } while (opcion != 5);
        } catch (Exception e) {
            System.out.println("Error al conectar con la base de datos: " + e.getMessage());
        }
    }

    private static void gestionarOpcion(int option) {
        switch (option) {
            case 1 -> alta();
            case 2 -> modificacion();
            case 3 -> baja();
            case 4 -> consulta();
            case 5 -> System.out.println("Saliendo...");
            default -> System.out.println("Opción inválida");
        }
    }

    private static void mostrarMenu() {
        System.out.println("\nMENU CRUD:");
        System.out.println("1. Alta");
        System.out.println("2. Modificación");
        System.out.println("3. Baja");
        System.out.println("4. Consulta");
        System.out.println("5. Salir");
        System.out.print("Seleccione una opción: ");
    }


    private static void alta() {
        try {
            Document document = new Document();
            while (true) {
                String clave = pedirTexto("Ingrese clave (Enter para finalizar): ");
                if (clave.isEmpty()) break;
                String valor = pedirTexto("Ingrese valor: ");
                document.append(clave, valor);
            }
            collection.insertOne(document);
            System.out.println("Documento insertado con éxito.");
        } catch (Exception e) {
            System.out.println("Error al insertar documento: " + e.getMessage());
        }
    }

    private static String pedirTexto(String s) {
        System.out.print(s);
        return sc.nextLine();
    }

    private static void modificacion() {
        try {
            String clave = pedirTexto("Ingrese clave del campo a modificar: ");
            String valorActual = pedirTexto("Ingrese valor actual para filtrar: ");
            String nuevoValor = pedirTexto("Ingrese nuevo valor: ");

            collection.updateMany(Filters.and(Filters.eq(clave, valorActual)), new Document("$set", new Document(clave, nuevoValor)));
            System.out.println("Documentos actualizados.");
        } catch (Exception e) {
            System.out.println("Error al modificar documentos: " + e.getMessage());
        }
    }

    private static void baja() {
        try {
            String clave = pedirTexto("Ingrese clave para eliminar documentos: ");
            String valor = pedirTexto("Ingrese valor de la clave: ");

            collection.deleteMany(Filters.and(Filters.eq(clave, valor)));
            System.out.println("Documentos eliminados.");
        } catch (Exception e) {
            System.out.println("Error al eliminar documentos: " + e.getMessage());
        }
    }

    private static void consulta() {
        try {
            String clave = pedirTexto("Ingrese clave para consultar documentos: ");
            String valor = pedirTexto("Ingrese valor de la clave: ");

            FindIterable<Document> documents = collection.find(Filters.and(Filters.eq(clave, valor)));
            for (Document doc : documents) {
                System.out.println(clave + ": " + doc.get(clave));
            }
        } catch (Exception e) {
            System.out.println("Error al consultar documentos: " + e.getMessage());
        }
    }
}
