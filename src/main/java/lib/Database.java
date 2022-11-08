package lib;

//import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Database {
	protected Connection cn;
	protected Statement st;
	protected ResultSet rs;
	protected PreparedStatement prs;
	protected ResultSetMetaData md;

	protected String url = "C:\\InventariosMH\\InventariosMH.db";
	
	public Connection Conexion() {
		cn = null;
		//verifConexion();
		try {
			Class.forName("org.sqlite.JDBC");
			cn = DriverManager.getConnection("jdbc:sqlite:" + url);
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
			System.out.print(e.getClass() + ": " + e.getMessage());
		}
		return cn;
	}

	public ArrayList<ArrayList<String>> getRows(String query) throws SQLException {
		ArrayList<ArrayList<String>> datos = new ArrayList<ArrayList<String>>();
		try {
			prs = Conexion().prepareStatement(query);
			rs = prs.executeQuery();
			md = rs.getMetaData();
			int columnas = md.getColumnCount();
			while (rs.next()) {
				ArrayList<String> dato = new ArrayList<String>();
				for (int i = 1; i <= columnas; i++) {
					dato.add(rs.getString(i));
				}
				datos.add(dato);
			}
			cerrarConexion();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("\nError -> " + e.getClass() + ": " + e.getMessage() + "\n");
		}
		return datos;
	}

	public ArrayList<String> getRow(String query) throws SQLException {
		rs = null;
		ArrayList<String> datosobt = new ArrayList<String>();
		try {
			st = Conexion().createStatement();
			rs = st.executeQuery(query);
			while (rs.next()) {
				md = rs.getMetaData();
				int columnas = md.getColumnCount();
				for (int i = 1; i <= columnas; i++) {
					datosobt.add(rs.getString(i));
				}
			}
			cerrarConexion();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.print(e.getClass() + ": " + e.getMessage());
		}
		return datosobt;
	}

	public void executeQuery(String query) {
		try {
			st = Conexion().createStatement();
			st.executeUpdate(query);
			st.close();

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("\nError -> " + e.getClass() + ": " + e.getMessage() + "\n");
		}
	}

	public void cerrarConexion() {
		try {
			cn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.print(e.getClass() + ": " + e.getMessage());
		}
	}
}
