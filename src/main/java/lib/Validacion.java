package lib;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

public class Validacion extends Database {
	public String obtenerToken () {
		StringBuffer buffer = new StringBuffer();
		//Creamos un token que diferencia la sesión
		char [] chars = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
		int charsLength = chars.length;
		Random random = new Random();
		for (int i = 1; i <= 20; i++){
			buffer.append(chars[random.nextInt(charsLength)]);
		}
		return buffer.toString();
	}
	
	public boolean verificarsesion(String id, String user, String actToken) throws UnknownHostException, SQLException {
		// El usuario es para saber si esta logueado
		boolean verif = true;
		// Verificar en la bd el Token
		String query = "SELECT Token FROM Usuarios WHERE Id = '" + id + "'";
		ArrayList<String> datosobt = getRow(query);
		String bdToken = "";
		if (!datosobt.isEmpty())
			bdToken = datosobt.get(0);
		// Verificar si la sesón es local o externas
		if (bdToken == null || bdToken.trim().equals("")) {
			if (user == null)
				verif = true;
			else
				verif = false;
		} else {
			if (bdToken.equals(actToken))
				verif = true;
			else {
				if (user == null)
					verif = true;
				else
					verif = false;
			}
		}
		return verif;
	}
	
	public String mostrarAlert (int tipo, String mensaje) { //0 = danger, 1 = success, 2 = warning
		String text = "";
		String alert = "";
		if (tipo == 0) alert = "danger";
		else if (tipo == 1) alert = "success";
		else if (tipo == 2) alert = "warning";
		else alert = "primary";
		if(mensaje != null && !mensaje.trim().equals("")) {
			text = "<div class=\"alert alert-" + alert + " mensaje col col-sm-12\" role=\"alert\">"
					+ mensaje
					+ "</div>";
		}
		return text;
	}
	
	public String mostrarToastr (String titulo, String mensaje) {
		String text = "<script>"
				+ "toastr[\"success\"](\"" + mensaje + "\", \"" + titulo + "\");"
				+ "</script>";
		return text;
	}
	
	public boolean isNumero(String cadena) {
		boolean number = cadena.matches("[+]?\\d*(\\.\\d+)?");
		return number;
	}
	
	public boolean existNumeros(String cadena) {
		int elementos = cadena.length();
		boolean veri = false;
		for (int i = 0; i < elementos; i++) {
			char caracter = cadena.charAt(i);
			if (Character.isDigit(caracter)) {
				veri = true;
			}
		}
		return veri;
	}
	
	public boolean existLetras(String cadena) {
		int elementos = cadena.length();
		boolean veri = false;
		for (int i = 0; i < elementos; i++) {
			char caracter = cadena.charAt(i);
			if (Character.isLetter(caracter)) {
				veri = true;
			}
		}
		return veri;
	}
	
	public ArrayList<String> obtenerUsers() {
		ArrayList<String> autorizar = new ArrayList<String>();
		String query = "";
		try {
			//Usuarios del sistema
			query = "SELECT (Nombres || ' ' || Apellidos) FROM Usuarios WHERE Nombres <> '' AND Apellidos <> ''";
			ArrayList<String> usuarios = getRow(query);
			if (usuarios.size() > 0) {
				for (int i = 0; i < usuarios.size(); i++) {
					if (autorizar.size() == 0) {
						autorizar.add(usuarios.get(i));
					}
					else if (autorizar.indexOf(usuarios.get(i)) < 0) {
						autorizar.add(usuarios.get(i));
					}
				}
			}
			//Personal de Autorización
			query = "SELECT Autorizacion FROM Inventarios WHERE Autorizacion <> ''";
			ArrayList<String> totautor = getRow(query);
			if (totautor.size() > 0) {
				for (int i = 0; i < totautor.size(); i++) {
					if (autorizar.size() == 0) {
						autorizar.add(totautor.get(i));
					}
					else if (autorizar.indexOf(totautor.get(i)) < 0) {
							autorizar.add(totautor.get(i));
					}
				}
			}
			//Personal de Autorización
			query = "SELECT UsuarioExterno FROM Inventarios WHERE UsuarioExterno <> ''";
			ArrayList<String> totext = getRow(query);
			if (totext.size() > 0) {
				for (int i = 0; i < totext.size(); i++) {
					if (autorizar.size() == 0) {
						autorizar.add(totext.get(i));
					}
					else if (autorizar.indexOf(totext.get(i)) < 0) {
							autorizar.add(totext.get(i));
					}
				}
			}
		} catch (SQLException e) {
			System.out.print("");
		}
		return autorizar;
	}
	
	public ArrayList<String> obtenerLocate() {
		ArrayList<String> ubicacion = new ArrayList<String>();
		String query = "";
		try {
			//Ubicación externa
			query = "SELECT UbicacionExterna FROM Inventarios WHERE UbicacionExterna <> ''";
			ArrayList<String> totubi = getRow(query);
			if (totubi.size() > 0) {
				for (int i = 0; i < totubi.size(); i++) {
					if (ubicacion.size() == 0) {
						ubicacion.add(totubi.get(i));
					}
					else if (ubicacion.indexOf(totubi.get(i)) < 0) {
						ubicacion.add(totubi.get(i));
					}
				}
			}
			//Ubicación de accesorios
			query = "SELECT Ubicacion FROM Productos WHERE Ubicacion <> ''";
			ArrayList<String> totubiacc = getRow(query);
			if (totubiacc.size() > 0) {
				for (int i = 0; i < totubiacc.size(); i++) {
					if (ubicacion.size() == 0) {
						ubicacion.add(totubiacc.get(i));
					}
					else if (ubicacion.indexOf(totubiacc.get(i)) < 0) {
						ubicacion.add(totubiacc.get(i));
					}
				}
			}
		} catch (SQLException e) {
			System.out.print("");
		}
		return ubicacion;
	}
	
	public ArrayList<String> obtenerMarca() {
		ArrayList<String> marcas = new ArrayList<String>();
		String query = "";
		try {
			//Ubicación de accesorios
			query = "SELECT Marca FROM Productos WHERE Marca <> ''";
			ArrayList<String> totmarc = getRow(query);
			if (totmarc.size() > 0) {
				for (int i = 0; i < totmarc.size(); i++) {
					if (marcas.size() == 0) {
						marcas.add(totmarc.get(i));
					}
					else if (marcas.indexOf(totmarc.get(i)) < 0) {
						marcas.add(totmarc.get(i));
					}
				}
			}
		} catch (SQLException e) {
			System.out.print("");
		}
		return marcas;
	}
	
	public ArrayList<String> obtenerProducto() {
		ArrayList<String> accesorios = new ArrayList<String>();
		String query = "";
		try {
			//Ubicación de accesorios
			query = "SELECT Nombre FROM Productos WHERE Nombre <> ''";
			ArrayList<String> totacc = getRow(query);
			if (totacc.size() > 0) {
				for (int i = 0; i < totacc.size(); i++) {
					if (accesorios.size() == 0) {
						accesorios.add(totacc.get(i));
					}
					else if (accesorios.indexOf(totacc.get(i)) < 0) {
						accesorios.add(totacc.get(i));
					}
				}
			}
		} catch (SQLException e) {
			System.out.print("");
		}
		return accesorios;
	}
}
