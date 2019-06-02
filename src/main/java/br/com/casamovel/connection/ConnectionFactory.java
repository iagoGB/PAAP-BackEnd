package br.com.casamovel.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ConnectionFactory {
		 /*private final String DRIVER = "com.postgresql.jdbc.Driver";*/
		private static final String URL = "jdbc:postgresql://localhost:5432/casamovel";
		private static final String USER = "postgres";
		private static final String PASSWORD = "mydatabase";
		
		public static  Connection getConnection() 
		{
			try {
				return DriverManager.getConnection(URL, USER, PASSWORD);
			} catch (SQLException e) {
				throw new RuntimeException("Erro: ", e);
			}
		};
		
		public static void closeConnection(Connection con) {
				try 
				{
					if (con != null) 
					{
						con.close();
					}
				} catch (SQLException e) {
					throw new RuntimeException("Erro: ", e);
				}
			}
		
		public static void closeConnection(Connection con, PreparedStatement stmt) {
			closeConnection(con);
			try 
			{
				if (stmt != null) 
				{
					stmt.close();
				}
			} catch (SQLException e) {
				throw new RuntimeException("Erro: ", e);
			}
		}
		
		public static void closeConnection(Connection con, PreparedStatement stmt,ResultSet rs) {
			closeConnection(con,stmt);
			try 
			{
				if (rs != null) 
				{
					rs.close();
				}
			} catch (SQLException e) {
				throw new RuntimeException("Erro: ", e);
			}
		}

		 
	}

	


