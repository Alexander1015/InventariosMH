package mainServlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lib.Validacion;

/**
 * Servlet implementation class Vista_Productos_Min
 */
@WebServlet("/dashboard/inventario/vista_min")
public class Vista_Productos_Min extends HttpServlet {

	Validacion val = new Validacion();

	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Vista_Productos_Min() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		/* Inicio verificar sesión */
		String query = "SELECT Nombres, Apellidos, Defecto "
				+ "FROM Usuarios WHERE Id = '" + request.getSession().getAttribute("id")
				+ "' ORDER BY Id DESC LIMIT 1";
		String usuario = "";
		try {
			ArrayList<String> datosobt = val.getRow(query);
			if (datosobt.size() > 0) {
				usuario = datosobt.get(0) + " " + datosobt.get(1);
			}
			if (request.getSession().getAttribute("id") != null
					&& !request.getSession().getAttribute("id").equals("")) {
				if (!val.verificarsesion(request.getSession().getAttribute("id").toString(), usuario,
						request.getSession().getAttribute("token").toString())) {
					response.sendRedirect("../desconectar");
				} 
				else if(datosobt.get(2).equals("0")) {
					response.sendRedirect("../perfil");
				}
				else {
					query = "SELECT AgregarProducto, RetirarProducto FROM Accesos WHERE IdUsuario = '" + request.getSession().getAttribute("id") + "'";
					ArrayList<String> accesos = val.getRow(query);
					if(accesos.size() == 0) {
						accesos.add(0, "0");
						accesos.add(1, "0");
					}
					if(accesos.get(0).equals("0") && accesos.get(1).equals("0")) {
						response.sendRedirect("../inventario");
					}
					else {
						response.sendRedirect("./");
					}
				}
			} else {
				response.sendRedirect("../desconectar");
			}
		} catch (SQLException | IOException e) {
			response.sendRedirect("../desconectar");
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		/* Inicio verificar sesión */
		String query = "SELECT Nombres, Apellidos, Defecto "
				+ "FROM Usuarios WHERE Id = '" + request.getSession().getAttribute("id")
				+ "' ORDER BY Id DESC LIMIT 1";
		String usuario = "";
		try {
			ArrayList<String> datosobt = val.getRow(query);
			if (datosobt.size() > 0) {
				usuario = datosobt.get(0) + " " + datosobt.get(1);
			}
			if (request.getSession().getAttribute("id") != null
					&& !request.getSession().getAttribute("id").equals("")) {
				if (!val.verificarsesion(request.getSession().getAttribute("id").toString(), usuario,
						request.getSession().getAttribute("token").toString())) {
					response.sendRedirect("../desconectar");
				} 
				else if(datosobt.get(2).equals("0")) {
					response.sendRedirect("../perfil");
				}
				else {
					String lugar = request.getParameter("lugar");
					try {
						/* Fin de verificar sesión */
						query = "SELECT AgregarProducto, RetirarProducto FROM Accesos WHERE IdUsuario = '" + request.getSession().getAttribute("id") + "'";
						ArrayList<String> accesos = val.getRow(query);
						if(accesos.size() == 0) {
							accesos.add(0, "0");
							accesos.add(1, "0");
						}
						if(accesos.get(0).equals("0") && accesos.get(1).equals("0")) {
							response.sendRedirect("../inventario");
						}
						else {
							String search = request.getParameter("txtSearch").replace("á", "a").replace("é", "e").replace("í", "i").replace("ó", "o").replace("ú", "u");
							if (lugar.trim().equals("retiro")) {
								request.getSession().setAttribute("retretsearch", search);
							}
							else if (lugar.trim().equals("add")) {
								request.getSession().setAttribute("retaddsearch", search);
							}
							String tilde1 = "replace(replace(replace(replace(replace(";
							String tilde2 = ", 'á', 'a'), 'é', 'e'), 'í', 'i'), 'ó', 'o'), 'ú', 'u')";
							if (search != null && search != "") {
								query = "SELECT P.Id, P.Codigo, P.Nombre, P.Cantidad, P.Ubicacion, P.Marca, P.Modelo, P.Serie, P.ActivoFijo, P.Comentario FROM Productos AS P INNER JOIN Tipo_Productos AS T ON P.Tipo = T.Id ";
								if (search.equals("@all"))
									query += "WHERE P.Estado = '1' AND P.Cantidad >= '1' ORDER BY P.Nombre ASC";
								else
									query += "WHERE (" + tilde1 + "P.Nombre" + tilde2 + " LIKE '%" + search
											+ "%' OR " + tilde1 + "P.Ubicacion" + tilde2 + " LIKE '%" + search + "%'"
											+ " OR " + tilde1 + "P.Marca" + tilde2 + " LIKE '%" + search + "%'"
											+ " OR " + tilde1 + "P.Modelo" + tilde2 + " LIKE '%" + search + "%'"
											+ " OR " + tilde1 + "P.Serie" + tilde2 + " LIKE '%" + search + "%'"
											+ " OR " + tilde1 + "P.ActivoFijo" + tilde2 + " LIKE '%" + search + "%'"
											+ " OR " + tilde1 + "P.Codigo" + tilde2 + " LIKE '%" + search + "%'"
											+ " OR " + tilde1 + "T.Nombre" + tilde2 + " LIKE '%" + search + "%'"
											+ ") AND P.Estado = '1' AND P.Cantidad >= '1' ORDER BY P.Nombre ASC";
								request.getSession().setAttribute("tabla_min", val.getRows(query));
								request.getSession().setAttribute("existablamin", "1");
							} else {
								request.getSession().setAttribute("tabla_min", null);
								request.getSession().setAttribute("existablamin", null);
							}
							response.sendRedirect("./" + lugar + ".jsp");
						}
					} catch (SQLException e) {
						request.getSession().setAttribute("menssearchinventario", "No se pueden mostrar los datos solicitados.");
						response.sendRedirect("./" + lugar + ".jsp");
					}
				}
			} else {
				response.sendRedirect("../desconectar");
			}
		} catch (SQLException | IOException e) {
			response.sendRedirect("../desconectar");
		}
	}

}
