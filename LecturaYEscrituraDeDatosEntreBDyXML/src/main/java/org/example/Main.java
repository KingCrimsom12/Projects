package org.example;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.sql.*;
import java.util.Scanner;

public class Main {

    private final static int OK = 1;
    private final static int NO_OK = 0;

    private final static String URL_CONEXION = "jdbc:mysql://localhost:3306/empleados_departamentos";
    private final static String DB_USER = "root";
    private final static String DB_PASSWORD = "root";

    private final static String CREAR_EMPLEADO = "INSERT INTO empleados "
            + "(nDIEmp, nomEmp, sexEmp, fecNac, fecIncorporacion, salEmp, comisionE, "
            + " cargoE, jefeID, codDepto) "
            + "VALUES (?, ?, ?, ?, sysdate(), ?, ?, ?, ?, ?)";

    private final static String CREAR_DEPARTAMENTO = "INSERT INTO departamentos "
            + "(codDepto, nombreDpto, ciudad, codDirector) "
            + "VALUES (?, ?, ?, ?)";

    private static final int NUMERO_MAXIMO_MENU = 3;
    private static final int NUMERO_SALIDA = 0;

    private static Scanner sc;

    private static void initialize()
    {
        sc = new Scanner(System.in);
    }

    public static void main(String[] args)
    {
        initialize();

        int numUsuario;

        do{
            numUsuario = pedirNumero();
            GestionarNumero(numUsuario);
        }
        while(numUsuario != NUMERO_SALIDA);
    }



    private static void GestionarNumero(int numeroUsuario) {
        switch (numeroUsuario)
        {
            case 1 -> LecturaXMLEscrituraBBDD();
            case 2 -> ExportacionBBDDAXML();
            case 3 -> exportarTablaAXML();
            case 0 -> System.out.println("Adios");
            default -> System.out.println("Opcion no valida");
        }
    }

    private static void exportarTablaAXML()
    {
        System.out.print("Ingrese el nombre de la tabla que desea exportar: ");
        String nombreTabla = sc.next();
        String archivoSalida = "src/main/resources/XML/tabla.xml";

        try {
            // Consulta todos los datos de la tabla
            String query = "SELECT * FROM " + nombreTabla;
            try (PreparedStatement preparedStatement = getDbConection().prepareStatement(query);
                 ResultSet resultSet = preparedStatement.executeQuery()) {

                // Crear un documento XML
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document doc = dBuilder.newDocument();

                // Nodo raíz con el nombre de la tabla
                Element rootElement = doc.createElement(nombreTabla);
                doc.appendChild(rootElement);

                // Metadatos de las columnas
                ResultSetMetaData metaData = resultSet.getMetaData();
                int columnCount = metaData.getColumnCount();

                // Recorrer las filas de la tabla
                while (resultSet.next()) {
                    // Crear un nodo para cada fila
                    Element rowElement = doc.createElement("row");
                    rootElement.appendChild(rowElement);

                    // Añadir cada columna como un subnodo
                    for (int i = 1; i <= columnCount; i++) {
                        String columnName = metaData.getColumnLabel(i);
                        String columnValue = resultSet.getString(i);

                        Element columnElement = doc.createElement(columnName);
                        columnElement.appendChild(doc.createTextNode(columnValue != null ? columnValue : "NULL"));
                        rowElement.appendChild(columnElement);
                    }
                }

                // Escribir el documento XML a un archivo
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                DOMSource source = new DOMSource(doc);
                StreamResult result = new StreamResult(new File(archivoSalida));

                transformer.transform(source, result);
                System.out.println("Datos exportados de la tabla '" + nombreTabla + "' a " + archivoSalida);

            }
        } catch (Exception e) {
            System.out.println("Error al exportar la tabla " + nombreTabla + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void ExportacionBBDDAXML() {
        String archivoSalida = "src/main/resources/XML/exportacion.xml";

        try (Connection connection = getDbConection()) {
            if (connection == null) {
                System.out.println("No se pudo establecer la conexión a la base de datos.");
                return;
            }

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.newDocument();

            // Nodo raíz
            Element rootElement = doc.createElement("empleados_departamentos");
            doc.appendChild(rootElement);

            // Exportar departamentos
            try (Statement stmt = connection.createStatement();
                 ResultSet rsDepartamentos = stmt.executeQuery("SELECT * FROM departamentos")) {

                while (rsDepartamentos.next()) {
                    Element departamento = doc.createElement("departamento");
                    departamento.setAttribute("codDepto", rsDepartamentos.getString("codDepto"));

                    crearElementoHijo(doc, departamento, "nombreDpto", rsDepartamentos.getString("nombreDpto"));
                    crearElementoHijo(doc, departamento, "ciudad", rsDepartamentos.getString("ciudad"));
                    crearElementoHijo(doc, departamento, "codDirector", rsDepartamentos.getString("codDirector"));

                    rootElement.appendChild(departamento);
                }
            }

            // Exportar empleados
            try (Statement stmt = connection.createStatement();
                 ResultSet rsEmpleados = stmt.executeQuery("SELECT * FROM empleados")) {

                while (rsEmpleados.next()) {
                    Element empleado = doc.createElement("empleado");
                    empleado.setAttribute("nDIEmp", rsEmpleados.getString("nDIEmp"));

                    crearElementoHijo(doc, empleado, "nomEmp", rsEmpleados.getString("nomEmp"));
                    crearElementoHijo(doc, empleado, "sexEmp", rsEmpleados.getString("sexEmp"));
                    crearElementoHijo(doc, empleado, "fecNac", rsEmpleados.getString("fecNac"));
                    crearElementoHijo(doc, empleado, "fecIncorporacion", rsEmpleados.getString("fecIncorporacion"));
                    crearElementoHijo(doc, empleado, "salEmp", String.valueOf(rsEmpleados.getInt("salEmp")));
                    crearElementoHijo(doc, empleado, "comisionE", String.valueOf(rsEmpleados.getInt("comisionE")));
                    crearElementoHijo(doc, empleado, "cargoE", rsEmpleados.getString("cargoE"));
                    crearElementoHijo(doc, empleado, "jefeID", rsEmpleados.getString("jefeID"));
                    crearElementoHijo(doc, empleado, "codDepto", rsEmpleados.getString("codDepto"));

                    rootElement.appendChild(empleado);
                }
            }

            // Escribir el documento XML a un archivo
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(archivoSalida));

            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            transformer.transform(source, result);

            System.out.println("Exportacion completada. Archivo XML generado: " + archivoSalida);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void crearElementoHijo(Document doc, Element padre, String nombre, String valor) {
        Element hijo = doc.createElement(nombre);
        hijo.appendChild(doc.createTextNode(valor != null ? valor : ""));
        padre.appendChild(hijo);
    }

    private static Connection getDbConection() {
        Connection connection = null;

        // Registramos el driver de MySQL
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            System.out.println("Error al registrar el driver de PostgreSQL: " + ex);
        }

        // Conectamos con la base de datos
        try {
            connection = DriverManager.getConnection(URL_CONEXION, DB_USER, DB_PASSWORD);
            connection.setAutoCommit(false);

            //Comprobamos is la conexion es valida
            boolean valid = connection.isValid(50000);
            System.out.println(valid ? "CONNECTION OK" : "CONNECTION NO OK");
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return connection;
    }

    private static void LecturaXMLEscrituraBBDD() {
        File fichero = new File("src/main/resources/XML/datos_correcto.xml");

        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fichero);
            doc.getDocumentElement().normalize();

            Connection connection = getDbConection();
            if (connection == null) {
                System.out.println("No se pudo establecer la conexión a la base de datos.");
                return;
            }

            try {
                // Procesar los departamentos
                NodeList departamentos = doc.getElementsByTagName("departamento");
                for (int i = 0; i < departamentos.getLength(); i++) {
                    Element departamento = (Element) departamentos.item(i);
                    insertarDepartamento(connection, departamento);
                }

                // Procesar los empleados
                NodeList empleados = doc.getElementsByTagName("empleado");
                for (int i = 0; i < empleados.getLength(); i++) {
                    Element empleado = (Element) empleados.item(i);
                    insertarEmpleado(connection, empleado);
                }

                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
                if (!connection.isClosed()) {
                    connection.rollback();
                    connection.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void insertarEmpleado(Connection connection, Element empleado) {
        try {
            // Validar si el empleado ya existe
            String empleadoID = empleado.getAttribute("nDIEmp");
            boolean existe = false;
            try (PreparedStatement checkStatement = connection.prepareStatement(
                    "SELECT COUNT(*) FROM empleados WHERE nDIEmp = ?")) {
                checkStatement.setString(1, empleadoID);
                try (ResultSet resultSet = checkStatement.executeQuery()) {
                    if (resultSet.next() && resultSet.getInt(1) > 0) {
                        existe = true;
                    }
                }
            }

            if (existe) {
                System.out.println("El empleado con ID " + empleadoID + " ya existe. Se omite la inserción.");
                return;
            }

            String jefeID = getTextContent(empleado, "jefeID");
            if (jefeID != null && !jefeID.isEmpty()) {
                boolean jefeExiste = false;
                try (PreparedStatement jefeCheckStatement = connection.prepareStatement(
                        "SELECT COUNT(*) FROM empleados WHERE nDIEmp = ?")) {
                    jefeCheckStatement.setString(1, jefeID);
                    try (ResultSet resultSet = jefeCheckStatement.executeQuery()) {
                        if (resultSet.next() && resultSet.getInt(1) > 0) {
                            jefeExiste = true;
                        }
                    }
                }

                if (!jefeExiste) {
                    System.out.println("El jefe con ID " + jefeID + " no existe. Se omite la inserción del empleado con ID " + empleadoID + ".");
                    return;
                }
            }

            // Insertar el empleado si no existe
            try (PreparedStatement preparedStatement = connection.prepareStatement(CREAR_EMPLEADO)) {
                preparedStatement.setString(1, empleadoID);
                preparedStatement.setString(2, getTextContent(empleado, "nomEmp"));
                preparedStatement.setString(3, getTextContent(empleado, "sexEmp"));
                preparedStatement.setString(4, getTextContent(empleado, "fecNac"));
                preparedStatement.setInt(5, Integer.parseInt(getTextContent(empleado, "salEmp")));
                preparedStatement.setInt(6, Integer.parseInt(getTextContent(empleado, "comisionE")));
                preparedStatement.setString(7, getTextContent(empleado, "cargoE"));
                preparedStatement.setString(8, getTextContent(empleado, "jefeID"));
                preparedStatement.setString(9, getTextContent(empleado, "codDepto"));

                int filasInsertadas = preparedStatement.executeUpdate();
                if (filasInsertadas == 1) {
                    connection.commit();
                    System.out.println("Empleado con ID " + empleadoID + " insertado correctamente.");
                } else {
                    connection.rollback();
                    System.out.println("Error al insertar el empleado con ID " + empleadoID);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al insertar el empleado", e);
        }
    }

    private static void insertarDepartamento(Connection connection, Element departamento)
    {
        String codDepto = departamento.getAttribute("codDepto");

        // Verificar si el departamento ya existe
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT COUNT(*) FROM departamentos WHERE codDepto = ?")) {
            preparedStatement.setString(1, codDepto);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next() && resultSet.getInt(1) == 0) {
                    // Si no existe, insertamos el nuevo departamento
                    try (PreparedStatement insertStatement = connection.prepareStatement(CREAR_DEPARTAMENTO)) {
                        insertStatement.setString(1, codDepto);
                        insertStatement.setString(2, getTextContent(departamento, "nombreDpto"));
                        insertStatement.setString(3, getTextContent(departamento, "ciudad"));
                        String codDirector = getTextContent(departamento, "codDirector");

                        if (codDirector == null || codDirector.isEmpty()) {
                            insertStatement.setNull(4, Types.VARCHAR);
                        } else {
                            insertStatement.setString(4, codDirector);
                        }

                        int filasInsertadas = insertStatement.executeUpdate();
                        if (filasInsertadas == 1) {
                            connection.commit();
                        } else {
                            connection.rollback();
                        }
                    }
                } else {
                    System.out.println("El departamento con codDepto " + codDepto + " ya existe.");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    private static String getTextContent(Element element, String tagName) {
       return element.getElementsByTagName(tagName).item(0).getTextContent();
    }

    private static int pedirNumero() {
        int numero = 0;
        boolean esNumero;

        do {
            try {
                System.out.println("Seleccione una opción del menú:");
                System.out.println("1 - Lectura desde XML y escritura en BBDD");
                System.out.println("2 - Exportación de toda la BBDD a XML");
                System.out.println("3 - Exportación de una tabla a XML");
                System.out.println("0 - Salir");
                System.out.print("Introduzca un numero de este menu: ");
                numero = sc.nextInt();
                esNumero = true;
            }
            catch (Exception ex)
            {
                System.out.println("Eso no es un numero");
                esNumero = false;
                sc.next();
            }
        } while(!esNumero);

        return numero;
    }
}