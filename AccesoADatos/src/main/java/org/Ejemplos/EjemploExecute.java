package org.Ejemplos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class EjemploExecute {   

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
		
	
	
		public static void main(String[] args) throws ClassNotFoundException, SQLException {
			ResultSet rs = null;
			
			try (Connection connection = getDbConection()) {
	 
	            //DatabaseMetaData dbmd = connection.getMetaData();//Creamos 	  
       		   
	   
	            String sql="SELECT * FROM departamentos";
	            //sql = "UPDATE DEPARTAMENTOS SET DNOMBRE = LOWER(DNOMBRE)";
	            Statement sentencia = connection.createStatement();		   
	            boolean valor = sentencia.execute(sql);  
	   		   
	            if(valor){
	            	rs = sentencia.getResultSet();
	            	while (rs.next())
	            		System.out.printf("%d, %s, %s %n",
	            				rs.getInt(1), rs.getString(2), rs.getString(3));	
	            } else {
	            	int f = sentencia.getUpdateCount();
	            	System.out.printf("Filas afectadas:%d %n", f);
	            }
	           
	       rs.close();
           sentencia.close();
           connection.close();
           
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}//main
}//	
