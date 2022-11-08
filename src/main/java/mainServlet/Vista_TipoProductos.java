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
 * Servlet implementation class Vista_TipoProductos
 */
@WebServlet("/dashboard/tipo_producto/vista")
public class Vista_TipoProductos extends HttpServlet {

	Validacion val = new Validacion();

	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Vista_TipoProductos() {
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
					try {
						query = "SELECT ReadTipoProducto FROM Accesos WHERE IdUsuario = '" + request.getSession().getAttribute("id") + "'";
						ArrayList<String> accesos = val.getRow(query);
						if(accesos.get(0).equals("0")) {
							response.sendRedirect("../tipo_producto");
						}
						else {
							String search = (String) request.getSession().getAttribute("stipo");
							String tilde1 = "replace(replace(replace(replace(replace(";
							String tilde2 = ", 'á', 'a'), 'é', 'e'), 'í', 'i'), 'ó', 'o'), 'ú', 'u')";
							if (search != null && search != "") {
								query = "SELECT Id, Simbolo, Nombre FROM Tipo_Productos ";
								if (search.equals("@all"))
									query += "ORDER BY Nombre ASC";
								else
									query += "WHERE " + tilde1 + "Simbolo" + tilde2 + " LIKE '%" + search + "%' OR " + tilde1 + "Nombre" + tilde2 + " LIKE '%" + search
										+ "%' ORDER BY Nombre ASC";
								request.getSession().setAttribute("tablatipo", val.getRows(query));
								request.getSession().setAttribute("existablatipo", "1");
							}
							request.getRequestDispatcher("view.jsp").forward(request, response);
						}
					} catch (SQLException e) {
						request.getSession().setAttribute("mensviewtipo", "No se pueden mostrar los datos solicitados.");
						response.sendRedirect("./");
					}
				}
			} else {
				response.sendRedirect("../desconectar");
			}
		} catch (SQLException | IOException e) {
			response.sendRedirect("../desconectar");
		}
		/* Fin de verificar sesión */
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
					try {
						query = "SELECT ReadTipoProducto FROM Accesos WHERE IdUsuario = '" + request.getSession().getAttribute("id") + "'";
						ArrayList<String> accesos = val.getRow(query);
						if(accesos.get(0).equals("0")) {
							response.sendRedirect("../tipo_producto");
						}
						else {
							String search = request.getParameter("txtSearch").replace("á", "a").replace("é", "e").replace("í", "i").replace("ó", "o").replace("ú", "u");
							String tilde1 = "replace(replace(replace(replace(replace(";
							String tilde2 = ", 'á', 'a'), 'é', 'e'), 'í', 'i'), 'ó', 'o'), 'ú', 'u')";
							if (search != null && search != "") {
								query = "SELECT Id, Simbolo, Nombre FROM Tipo_Productos ";
								if (search.equals("@all"))
									query += "ORDER BY Nombre ASC";
								else
									query += "WHERE " + tilde1 + "Simbolo" + tilde2 + " LIKE '%" + search + "%' OR " + tilde1 + "Nombre" + tilde2 + " LIKE '%" + search
											+ "%' ORDER BY Nombre ASC";
								request.getSession().setAttribute("tablatipo", val.getRows(query));
								request.getSession().setAttribute("existablatipo", "1");
								request.getSession().setAttribute("stipo", search);
								request.getSession().setAttribute("sinputtipo", search);
							} else {
								request.getSession().setAttribute("tablatipo", null);
								request.getSession().setAttribute("existablatipo", null);
								request.getSession().setAttribute("stipo", null);
								request.getSession().setAttribute("sinputtipo", null);
							}
							request.getRequestDispatcher("view.jsp").forward(request, response);
						}
					} catch (SQLException e) {
						request.getSession().setAttribute("mensviewtipo", "No se pueden mostrar los datos solicitados.");
						response.sendRedirect("./");
					}
				}
			} else {
				response.sendRedirect("../desconectar");
			}
		} catch (SQLException | IOException e) {
			response.sendRedirect("../desconectar");
		}
		/* Fin de verificar sesión */
	}
}
