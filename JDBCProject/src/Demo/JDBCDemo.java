package Demo;

import java.sql.*;

public class JDBCDemo {
	
	private static final String user = "root";
	private static final String password = "1234";
	
	private static final String url = "jdbc:mysql://localhost:3306/myDatabase";

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		Class.forName("com.mysql.cj.jdbc.Driver");
		Connection conn = DriverManager.getConnection(url, user, password);
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT * FROM article FOR UPDATE");
		while(rs.next()) {
			System.out.println("id: " + rs.getInt(1) + ", title: " + rs.getString(2));
		}
		stmt.close();
		conn.close();
	}

}
