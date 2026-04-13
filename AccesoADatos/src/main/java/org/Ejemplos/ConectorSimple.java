package org.Ejemplos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ConectorSimple {

	// URL DE CONEXION A LA BASE DE DATOS
	private final static String URL_CONEXION = "jdbc:mysql://localhost:3306/empleados_departamentos";
	private final static String DB_USER = "root";
	private final static String DB_PASSWORD = "123456789";
	
	private final static String CONSULTAR_EMPLEADOS = "SELECT * "
			   									    + "FROM empleados ";

	// CREAR CONEXION
	private static Connection getDbConection() {
		Connection connection = null;
		
    	// Registramos el driver de MySQL
    	try { 
    		  Class.forName("com.mysql.cj.jdbc.Driver");
    	} catch (ClassNotFoundException ex) {
    	    System.out.println("Error al registrar el driver de MySQL: " + ex);
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
	
	
	// CONSULTAS
	private static void consultarEmpleados() {
		try (Connection connection = getDbConection();
			 Statement stmt = connection.createStatement()) {
			

			// no lo puedo poner en el try para que se cierre auto, porque le pasamos un parámetro a la query
			// ResultSet es un HASHMAP
	        ResultSet rs = stmt.executeQuery(CONSULTAR_EMPLEADOS);
			
	        // se imprime para todos los elementos devueltos en la consulta
	        while (rs.next()) {
	        	System.out.println("======= INICIO DATOS USUARIO ======");
	      
	            System.out.println("ID: " + rs.getString("nDIEmp"));
	            System.out.println("Nombre: " + rs.getString("nomEmp"));
	            System.out.println("Sexo: " + rs.getString("sexEmp"));
	            System.out.println("Fecha nacimiento: " + rs.getString("fecNac"));
	            System.out.println("Fecha incorporación: " + rs.getDate("fecIncorporacion"));
	            System.out.println("Salario: " + rs.getInt("salEmp"));
	            System.out.println("Comisión: " + rs.getString("comisionE"));
	      
	            System.out.println("======= FIN DATOS USUARIO ======");
	        }
	        
	        rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	// MAIN
    public static void main(String args[]) {
    	consultarEmpleados();   	
    }
}
