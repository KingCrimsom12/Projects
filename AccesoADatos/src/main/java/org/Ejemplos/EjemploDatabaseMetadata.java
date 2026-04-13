
package org.Ejemplos;

import java.sql.*;
public class EjemploDatabaseMetadata {
	
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
		ResultSet rs = null;
		
		try (Connection connection = getDbConection()) {
 
            DatabaseMetaData dbmd = connection.getMetaData();//Creamos 
         
            String nombre  = dbmd.getDatabaseProductName();
            String driver  = dbmd.getDriverName();
            String url     = dbmd.getURL(); 
            String usuario = dbmd.getUserName() ;
		 		 
            System.out.println("INFORMACION SOBRE LA BASE DE DATOS:");
            System.out.println("===================================");
            System.out.printf("Nombre : %s %n", nombre );
            System.out.printf("Driver : %s %n", driver );
            System.out.printf("URL    : %s %n", url );
            System.out.printf("Usuario: %s %n %n", usuario );
                 
            System.out.println("INFORMACION SOBRE LAS TABLAS:");
            System.out.println("===================================");

            //Obtener informacion de las tablas y vistas que hay 		       
            rs = dbmd.getTables(null, "ejemplo", null, null);		 
            while (rs.next()) {			   
			     String catalogo = rs.getString(1);//columna 1 
			     String esquema = rs.getString(2); //columna 2
			     String tabla = rs.getString(3);   //columna 3
			     String tipo = rs.getString(4);	  //columna 4 en MySQL te dice si es una tabla o una vista
  			     System.out.printf("%s - Catalogo: %s, Esquema: %s, Nombre: %s %n", 
  					   tipo, catalogo, esquema, tabla);
            }  
            rs.close();
		  	   
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}	//fin de main
}//fin de la clase