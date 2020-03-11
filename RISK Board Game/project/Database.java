package project;

import java.io.*;
import java.sql.*;
import java.util.*;

public class Database {
	
	  private Connection connection;
	  
	  public void setConnection(String fn) 
	  {
		  	Properties properties = new Properties();
			try {
				FileInputStream fileInput = new FileInputStream(fn);
				properties.load(fileInput);
			} catch (FileNotFoundException e0) {
				e0.printStackTrace();
				System.out.println("File " + fn + " not found.");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
			String user = properties.getProperty("user");
			String password = properties.getProperty("password");
			String url = properties.getProperty("url");

			try {
				connection = DriverManager.getConnection(url,user,password);
			} catch (SQLException e2) {
				e2.printStackTrace();
				System.out.println("Error occured when attempting to login to database with credentials");
			}
	  }
	
	  public Connection getConnection()
	  {
		  	return connection;
	  }
	  
	  public ArrayList<String> query(String query) 
	  {	
			try {
			  	ArrayList<String> result = new ArrayList<>();
				Statement statement;
				statement = connection.createStatement();
				ResultSet set = statement.executeQuery(query);
				ResultSetMetaData metaData = set.getMetaData();
				while (set.next()) {
					String temp = "";
					int i = 0;
					for (i = 0; i < metaData.getColumnCount() -1 ; i++)
						temp += set.getString(1) + ",";
					temp += set.getString(++i);
					result.add(temp);
				}
				statement.close();
				return result;
				} 
			catch (SQLException e) {
				e.printStackTrace();
				return null;
			}
	  }
	  
	  public void executeDML(String dml) throws SQLException
	  {
		  	Statement statement = connection.createStatement();
		  	statement.execute(dml);
	  }
}