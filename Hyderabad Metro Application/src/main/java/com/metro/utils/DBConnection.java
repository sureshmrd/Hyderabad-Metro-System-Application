package com.metro.utils;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection 
{
	private static final String URL = "jdbc:oracle:thin:@localhost:1521:ORCL";
    private static final String USER = "c##project";  
    private static final String PASSWORD = "project";  
    private static Connection conn = null;
    
    public static Connection getConnection() {
    	try {
    		if(conn == null || conn.isClosed()) {
    			Class.forName("oracle.jdbc.driver.OracleDriver");
    			conn=DriverManager.getConnection(URL, USER, PASSWORD);
    			System.out.println("Database Connected Sucessfully");
    		}
    	}catch(Exception e) {
    		e.printStackTrace();
    		System.err.println(e.getMessage());
    	}
    	return conn;
    }
}
