package Demo;

import java.sql.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.io.*;

public class MultiThreadJDBC {
	
	private static final String USER = "root";
	private static final String PASSWORD = "1234";
	private static final String URL = "jdbc:mysql://localhost:3306/myDatabase";

	public static Connection getConn() {
		Connection conn = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection(URL, USER, PASSWORD);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Connection conn = null;
		
		//System.out Redirect
		PrintStream defaultOut = System.out;
		try {
			PrintStream out = new PrintStream(new FileOutputStream("/home/rootu/Java/MultiThreadMySQL.txt"));
			System.setOut(out);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		ExecutorService exec = Executors.newCachedThreadPool();
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection(URL, USER, PASSWORD);
		} catch(ClassNotFoundException e) {
			e.printStackTrace();
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		/*
		for(int i=0; i<100; i++) {
			exec.execute(new SqlRead(conn));
		}
		*/
		//exec.execute(new SqlUpdate(conn));
		
		for(int i=1; i<=100; i++) {
			exec.execute(new SqlInsert("ZK"+i));
		}
		
		
		
		//for(int i=0; i<20; i++) { exec.execute(new SqlReadUpdate()); }
		
		
		
		try {
			exec.shutdown();
			if(!exec.awaitTermination(60000*5, TimeUnit.SECONDS)) {
				exec.shutdownNow();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		try {
			if(conn != null) {
				conn.close();
				conn = null;
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
		System.setOut(defaultOut);
		System.out.println("END");
	}

}

class SqlInsert implements Runnable {
	private Connection conn;
	private PreparedStatement pstmt;
	private String title;
	
	public SqlInsert() {
		this("XK");
	}
	
	public SqlInsert(String title) {
		this.conn = MultiThreadJDBC.getConn();
		this.title = title;
	}
	
	@Override
	public void run() {
		String SQL = "INSERT INTO MTDemo (text) VALUES (?)";
		try {
			conn.setAutoCommit(false);
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, this.title);
			int rows = pstmt.executeUpdate();
			System.out.println("Insert Affected Rows: " + rows + ", Thread: " + Thread.currentThread().getId());
			conn.commit();
			conn.setAutoCommit(true);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		this.conn = null;
		try {
			if(pstmt != null) {
				this.pstmt.close();
				this.pstmt = null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}

class SqlUpdate implements Runnable {
	private Connection conn;
	private PreparedStatement pstmt;
	
	public SqlUpdate(Connection conn) {
		this.conn = conn;
	}
	
	public SqlUpdate() {
		this(null);
	}
	
	@Override
	public void run() {
		for(int id=1; id<=10000; id++) {
			String SQL = "UPDATE MTDemo SET text=? WHERE id=?";
			try {
				pstmt = conn.prepareStatement(SQL);
				if(pstmt != null) {
					pstmt.setString(1, "ZK"+id);
					pstmt.setInt(2, id);
					int rows = pstmt.executeUpdate();
					System.out.println("Update Affected Rows: " + rows);
				} else {
					System.out.println("No Statement Update");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		this.conn = null;
		try {
			if(this.pstmt != null) {
				this.pstmt.close();
				this.pstmt = null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}

class SqlRead implements Runnable {
	private Statement stmt;
	private Connection conn;
	private ResultSet rs;
	
	public SqlRead() {
		this(null);
	}
	
	public SqlRead(Connection conn) {
		this.conn = conn;
	}
	
	@Override
	public void run() {
		String SQL = "SELECT * FROM MTDemo FOR UPDATE";
		
		try {
			stmt = conn.createStatement();
			if(stmt != null) {
				rs = stmt.executeQuery(SQL);
			}
			if(rs != null) {
				while(rs.next()) {
					System.out.println("id: " + rs.getInt("id") + ", text: " + rs.getString("text")
					+ ", Thread: " + Thread.currentThread().getId());
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		this.conn = null;
		
		try {
			if(rs != null) {
				rs.close();
				rs = null;
			}
			if(stmt != null) {
				stmt.close();
				stmt = null;
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
	}
}

class SqlReadUpdate implements Runnable {
	private Statement stmt;
	private PreparedStatement pstmt;
	private Connection conn;
	private ResultSet rs;
	private int idd;
	
	private static int id=0;
	
	private static final String USER = "root";
	private static final String PASSWORD = "1234";
	private static final String URL = "jdbc:mysql://localhost:3306/myDatabase";

	
	public SqlReadUpdate() {
		id++;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			this.conn = DriverManager.getConnection(URL, USER, PASSWORD);
		} catch(ClassNotFoundException e) {
			e.printStackTrace();
		} catch(SQLException e) {
			e.printStackTrace();
		}
		idd = id;
	}
	
	@Override
	public void run() {
		int x;
		String text;
		try {
			conn.setAutoCommit(false);
			stmt = conn.createStatement();
			pstmt = conn.prepareStatement("UPDATE MTDemo SET text=? WHERE id=?");
			if(stmt != null) {
				rs = stmt.executeQuery("SELECT * FROM MTDemo FOR UPDATE");
			}
			if(rs != null) {
				while(rs.next()) {
					x = rs.getInt("id");
					text = rs.getString("text");
					pstmt.setString(1, text + "/UP" + idd);
					pstmt.setInt(2, x);
					pstmt.executeUpdate();
					System.out.println("id: " + x + ", text: " + text
					+ ", Thread: " + Thread.currentThread().getId());
				}
			}
			conn.commit();
			conn.setAutoCommit(true);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		this.conn = null;
		
		try {
			if(rs != null) {
				rs.close();
				rs = null;
			}
			if(stmt != null) {
				stmt.close();
				stmt = null;
			}
			if(pstmt != null) {
				pstmt.close();
				pstmt = null;
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
	}
}
