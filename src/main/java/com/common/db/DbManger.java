
package com.common.db;



import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.common.conf.Configuration;


public class DbManger {
	
	static{
		
		try {
			Class.forName(Configuration.getString("class_name"));
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
	}
			
	public static Connection getConection() throws SQLException{
		return DriverManager.getConnection(Configuration.getString("db_url"),
				Configuration.getString("db_user"),
				Configuration.getString("db_password"));
	}

}
