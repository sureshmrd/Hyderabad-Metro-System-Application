package com.metro.utils;

import java.sql.Connection;

public class TestDBConnection 
{
	public static void main(String[] args)
	{
		Connection conn=DBConnection.getConnection();
		if(conn!=null)
		{
			System.out.println("JDBC Connection Successfull");
		}else {
			System.out.println("Failed to Connect JDBC");
		}
	}
}
