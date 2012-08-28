package org.handsomestone.ConnectionHelper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionHelper
{

	//private String url;
	private String url = "jdbc:mysql://localhost/languagedb";
	private static String username = "root";
	private static String password = "";
	private static ConnectionHelper instance;
	
	//private static Connection con = null;
	public String getUrl()
	{
		return url;
	}

	private ConnectionHelper()
	{
		try
		{
			/*Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();
			String str = URLDecoder.decode(getClass().getClassLoader().getResource("services").toString(),"UTF-8");
			str= str.substring(0, str.indexOf("classes/services")); 
			if ( str.startsWith("file:/C:",0)){
				str=str.substring(6);
			}
			else{
				str=str.substring(5);
			}
			url = "jdbc:derby:" + str + "database/testdrive";
			System.out.println("Database url "+url);
			//url="jdbc:derby:testdrive";*/
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("ok");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static ConnectionHelper getInstance()
	{
		if (instance == null)
			instance = new ConnectionHelper();
		return instance;
	}

	public static Connection getConnection() throws java.sql.SQLException
	{
		return DriverManager.getConnection(getInstance().getUrl(), username, password);
		//return con;
	}

	public static void closeConnection(Connection c)
	{
		try
		{
			if (c != null)
			{
			    c.close();
            }
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
	/*
	public static void initConnection() throws SQLException {
		con = DriverManager.getConnection(getInstance().getUrl(), username, password);
		
	}
	public static void closeConnection()
	{
		try
		{
			if (con != null)
			{
				System.out.println("close");
			    con.close();
            }
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
	*/
}
