
package org.Ejemplos;

import java.sql.*;

public class EjemploResultsetmetadata {

	// URL DE CONEXION A LA BASE DE DATOS
	private final static String URL_CONEXION = "jdbc:mysql://localhost:3306/empleados_departamentos";
	private final static String DB_USER = "root";
	private final static String DB_PASSWORD = "root";
			
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
	
	
	public static void main(String[] args) {
		String sql = "SELECT * FROM departamentos";
		
		try (Connection connection = getDbConection();
			 Statement sentencia = connection.createStatement();		   
			 ResultSet rs = sentencia.executeQuery(sql)) { 
			
			ResultSetMetaData rsmd = rs.getMetaData();
			
			int nColumnas = rsmd.getColumnCount();
			String nula;
			System.out.printf("Numero de columnas recuperadas: %d%n", nColumnas);

			for (int i = 1; i <= nColumnas; i++) {
				System.out.printf("Columna %d: %n ", i);
				System.out.printf("  Nombre: %s %n   Tipo: %s %n ",
				rsmd.getColumnName(i),  rsmd.getColumnTypeName(i));
				if (rsmd.isNullable(i) == 0)
					nula = "NO";
				else
					nula = "SI";
				
				System.out.printf("  Puede ser nula?: %s %n ", nula);			
				System.out.printf("  Maximo ancho de la columna: %d %n",
				rsmd.getColumnDisplaySize(i));
			}// for

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}// fin de main
}