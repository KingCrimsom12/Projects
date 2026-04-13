/*
 * 1. Voy al directorio en donde quiero crear la base de datos -> /Users/mjmadridmontes/eclipse-workspace/Tema02/sqlite
 * 2, Para crear una base de datos -> sqlite3 nombre_base_de_datos.db
 * 3. .read ./create_tables.sql -> genera el archivo .db creado en 2 y crea las tablas
 * 4, .quit -> para salir de sqlite
 */
package org.Ejemplos;

import java.sql.*;

public class ConectorSQLLite {   
	
	// CREAR CONEXION
	private static Connection getDbConection() {
		Connection connection = null;
		
    	// Registramos el driver de MySQL
    	try { 
    		Class.forName("org.sqlite.JDBC");
    	} catch (ClassNotFoundException ex) {
    	    System.out.println("Error al registrar el driver de PostgreSQL: " + ex);
    	}  
    	
    	// Conectamos con la base de datos 
    	try { 
    		connection = DriverManager.getConnection("jdbc:sqlite:/Users/mjmadridmontes/eclipse-workspace/Tema02/sqlite/empleados.db");
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
	   		   
   	 		while (rs.next()) {
   	 			System.out.printf("%d, %s, %s %n", rs.getInt("codDepto"), rs.getString("nombreDpto"), rs.getString("ciudad"));
   	 		}
		} catch (SQLException e) {
			e.printStackTrace();
		}	    

        System.out.print("END");
	}//main
}//
