package lib;

import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.ArrayList;

public class Plantilla extends Database {
	
	Validacion val = new Validacion();
	
	public String header(String titulo, int ubi) {
		String direc = "";
		if (ubi > 0)
			direc += "../";
		for (int i = 1; i < ubi; i++) {
			direc += "../";
		}
		String text = "<meta charset=\"utf-8\">"
				+ "<title>Control Inventarios, El Salvador</title>"
				+ "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">"
				+ "<link rel=\"icon\" type=\"image/ico\" href=\"" + direc + "resources/img/logo.ico\">"
				+ "<link rel=\"stylesheet\" type=\"text/css\"  href=\"" + direc + "css/bootstrap.min.css\">"
				+ "<link rel=\"stylesheet\" type=\"text/css\"  href=\"" + direc + "resources/iconos/bootstrap-icons.css\">"
				+ "<link rel=\"stylesheet\" type=\"text/css\" href=\"" + direc + "css/toastr.min.css\">"
				+ "<link rel=\"stylesheet\" type=\"text/css\" href=\"" + direc + "css/datatables.css\">"
				+ "<link rel=\"stylesheet\" type=\"text/css\" href=\"" + direc + "css/select2.min.css\">"
				+ "<link rel=\"stylesheet\" type=\"text/css\" href=\"" + direc + "css/jquery.flexdatalist.css\">"
				+ "<link rel=\"stylesheet\" type=\"text/css\" href=\"" + direc + "css/main.css\">";
		return text;
	}

	public String navbar(String txtubi, String id, String token, int ubi) throws UnknownHostException, SQLException {
		String text = "";
		String direc = "", alt = "", usuario = "";
		for (int i = 1; i < ubi; i++) {
			direc += "../";
		}
		if(ubi == 3 && txtubi.equals("Login")) {
			alt = direc + "../";
		}
		if (!txtubi.equals("Login")) {
			alt = direc + "../";
			direc += "../dashboard";
		}
		String query = "SELECT Nombres, Apellidos FROM Usuarios WHERE Id = '" + id + "' ORDER BY Id DESC LIMIT 1";
		ArrayList<String> datosobt = getRow(query);
		if (datosobt.size() > 0) {
			usuario = datosobt.get(0) + " " + datosobt.get(1);
		}
		if (!txtubi.equals("Login") && (usuario != null && usuario.trim().equals(""))) {
			text = "<script type=\"text/javascript\">"
					+ "window.location.href = \"" + direc + "/desconectar\";"
					+ "</script>";
		} else {
			boolean verif = true;
			if (id != null && !id.trim().equals("")) {
				verif = val.verificarsesion(id, usuario, token);
			}
			if (verif) {
				query = "SELECT ReadProducto, ReadBitacora, ReadUsuario FROM Accesos WHERE IdUsuario = '" + id + "'";
				ArrayList<String> accesos = val.getRow(query);
				if(accesos.size() == 0) {
					accesos.add(0, "0");
					accesos.add(1, "0");
					accesos.add(2, "0");
				}
				//
				text = 	"<div class=\"container-fluid wbody\">"
						+ "		<nav class=\"navbar navbar-expand-lg\">"
						+ (!txtubi.equals("Login")
								? "	<button class=\"navbar-toggler\" type=\"button\" data-bs-toggle=\"collapse\" data-bs-target=\"#menuRetra\" aria-controls=\"menuRetra\" aria-expanded=\"false\" aria-label=\"Toggle navigation\">"
							+ "				<span class=\"navbar-toggler-icon iconmen\"><i class=\"bi bi-list-ul\"></i> Men�</span>"
							+ "			</button>"
								: "")
						+ "			<div class=\"container-fluid" + (!txtubi.equals("Login") ? " menActive" : "")
						+ "\">"
						+ (!txtubi.equals("Login") ? "<div class=\"collapse navbar-collapse\" id=\"menuRetra\">" : "")
						+ "				<a class=\"logo-s\" href=\"" + direc + "\">"
						+ "					<img src=\"" + alt
						+ "resources/img/logo.png\" class=\"imglogo\"/>CONTROL DE INVENTARIOS"
						+ "					</a>"
						+ (!txtubi.equals("Login") ? "	<ul class=\"navbar-nav me-auto mb-2 mb-lg-0\">"
								+ (accesos.get(0).equals("1") && accesos.get(1).equals("1") ? "<li class=\"nav-item opcAcc\">"
								+ "								<a class=\"nav-link"
								+ (txtubi.equals("Dashboard") ? " active" : "") + "\" aria-current=\"page\" href=\""
								+ direc
								+ "/\"><i class=\"bi bi-house-door\"></i> DASHBOARD</a>"
								+ "							</li>" : "")
								+ (accesos.get(0).equals("1") ? "<li class=\"nav-item opcAcc\">"
								+ "								<a class=\"nav-link"
								+ (txtubi.equals("Inventario") ? " active" : "") + "\" aria-current=\"page\" href=\""
								+ direc
								+ "/inventario\"><i class=\"bi bi-archive\"></i> INVENTARIO DE ACCESORIOS/DISPOSITIVOS</a>"
								+ "							</li>" : "")
								+ (accesos.get(1).equals("1") ? "<li class=\"nav-item opcAcc\">"
								+ "								<a class=\"nav-link"
								+ (txtubi.equals("Bitacora") ? " active" : "") + "\" aria-current=\"page\" href=\""
								+ direc
								+ "/bitacora\"><i class=\"bi bi-file-earmark-bar-graph\"></i> BITACORA GENERAL DE PROCESOS</a>"
								+ "							</li>" : "")
								+ (accesos.get(2).equals("1") ? "<li class=\"nav-item opcAcc\">"
								+ "								<a class=\"nav-link"
								+ (txtubi.equals("Usuarios") ? " active" : "") + "\" aria-current=\"page\" href=\""
								+ direc + "/usuario\"><i class=\"bi bi-people\"></i> MANEJO DE USUARIO</a>"
								+ "							</li>": "")
								+ "							<li class=\"nav-item dropdown\">"
								+ "						         <a class=\"nav-link dropdown-toggle\" href=\"#\" id=\"menPerfil\" role=\"button\" data-bs-toggle=\"dropdown\" aria-expanded=\"false\">"
								+ "<i class=\"iconmenu bi bi-list-ul\"></i> "
								+ (usuario != null ? " " + usuario.toUpperCase() : " Usuario no detectado")
								+ "						         </a>"
								+ "						         <ul class=\"dropdown-menu dropdown-menu-lg-end opcPerfil\" aria-labelledby=\"menPerfil\">"
								+ (accesos.get(0).equals("1") && accesos.get(1).equals("1") ? "<li class=\"opcAccDrop\"><a class=\"dropdown-item"
								+ (txtubi.equals("Dashboard") ? " active" : "") + "\" href=\"" + direc
								+ "/\"><i class=\"bi bi-house-door\"></i> DASHBOARD</a></li>" : "")
								+ (accesos.get(0).equals("1") ? "<li class=\"opcAccDrop\"><a class=\"dropdown-item"
								+ (txtubi.equals("Inventario") ? " active" : "") + "\" href=\"" + direc
								+ "/inventario\"><i class=\"bi bi-archive\"></i> INVENTARIO DE ACCESORIOS/DISPOSITIVOS</a></li>" : "")
								+ (accesos.get(1).equals("1") ? "<li class=\"opcAccDrop\"><a class=\"dropdown-item "
								+ (txtubi.equals("Bitacora") ? " active" : "") + "\" href=\"" + direc
								+ "/bitacora\"><i class=\"bi bi-file-earmark-bar-graph\"></i> BITACORA GENERAL DE PROCESOS</a></li>" : "")
								+ (accesos.get(2).equals("1") ? "<li class=\"opcAccDrop\"><a class=\"dropdown-item "
								+ (txtubi.equals("Usuarios") ? " active" : "") + "\" href=\"" + direc
								+ "/usuario\"><i class=\"bi bi-people\"></i> MANEJO DE USUARIOS</a></li>" : "")
								+ "						           <li><a class=\"dropdown-item "
								+ (txtubi.equals("Historial") ? " active" : "") + "\" href=\"" + direc
								+ "/perfil/historial\"><i class=\"bi bi-clock-history\"></i> MI HISTORIAL DE PROCESOS</a></li>\r\n"
								+ "						           <li><a class=\"dropdown-item "
								+ (txtubi.equals("Perfil") ? " active" : "") + "\" href=\"" + direc
								+ "/perfil\"><i class=\"bi bi-person-circle\"></i> MI PERFIL</a></li>\r\n"
								+ "						           <li><a class=\"dropdown-item\" href=\"" + direc
								+ "/desconectar\"><i class=\"bi bi-box-arrow-left\"></i> CERRAR SESI�N</a></li>"
								+ "						         </ul>"
								+ "						      </li>"
								+ "						</ul>\r\n"
								+ "					</div>" : "")
						+ "				</div>\r\n"
						+ "			</nav>";
			} else {
				text = "<script type=\"text/javascript\">"
						+ "		window.location.href = \"" + direc + "/desconectar\";"
						+ "</script>";
			}
		}
		return text;
	}

	public String footer(int ubi) {
		String direc = "";
		if (ubi > 0)
			direc += "../";
		for (int i = 1; i < ubi; i++) {
			direc += "../";
		}
		String text = "	</div>"
				+ "		<footer>"
				+ "			<p>DESARROLLADO EL 2022</p>"
				+ "		</footer>"
				+ "<script type=\"text/javascript\" src=\"" + direc + "js/jquery-3.6.0.min.js\"></script>"
				+ "<script type=\"text/javascript\" src=\"" + direc + "js/popper.min.js\"></script>"
				+ "<script type=\"text/javascript\" src=\"" + direc + "js/bootstrap.min.js\"></script>"
				+ "<script type=\"text/javascript\" src=\"" + direc + "js/toastr.min.js\"></script>"
				+ "<script type=\"text/javascript\" src=\"" + direc + "js/datatables.js\"></script>"
				+ "<script type=\"text/javascript\" src=\"" + direc + "js/select2.min.js\"></script>"
				+ "<script type=\"text/javascript\" src=\"" + direc + "js/chart.min.js\"></script>"
				+ "<script type=\"text/javascript\" src=\"" + direc + "js/bootbox.min.js\"></script>"
				+ "<script type=\"text/javascript\" src=\"" + direc + "js/confirm.js\"></script>"
				+ "<script type=\"text/javascript\" src=\"" + direc + "js/jquery.flexdatalist.js\"></script>"
				+ "<script type=\"text/javascript\" src=\"" + direc + "js/main.js\"></script>";
		return text;
	}
}
