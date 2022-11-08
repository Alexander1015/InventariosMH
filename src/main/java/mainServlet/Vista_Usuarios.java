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
 * Servlet implementation class Vista_Usuarios
 */
@WebServlet("/dashboard/usuario/vista")
public class Vista_Usuarios extends HttpServlet {
	
	Validacion val = new Validacion();
	
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Vista_Usuarios() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
						query = "SELECT ReadUsuario FROM Accesos WHERE IdUsuario = '" + request.getSession().getAttribute("id") + "'";
						ArrayList<String> accesos = val.getRow(query);
						if(accesos.get(0).equals("0")) {
							response.sendRedirect("../usuario");
						}
						else {
							String search = (String) request.getSession().getAttribute("suser");
							if (search != null && !search.equals("")) {
								search = search.replace("á", "a").replace("é", "e").replace("í", "i").replace("ó", "o").replace("ú", "u");
							}
							String tilde1 = "replace(replace(replace(replace(replace(";
							String tilde2 = ", 'á', 'a'), 'é', 'e'), 'í', 'i'), 'ó', 'o'), 'ú', 'u')";
							if (search != null && search != "") {
								query = "SELECT Id, Nombres, Apellidos, Usuario FROM Usuarios";
								if (search.equals("@all"))
									query += " ORDER BY Nombres ASC";
								else
									query += " WHERE " + tilde1 + "Nombres" + tilde2 + " LIKE '%" + search + "%' OR " + tilde1 + "Apellidos" + tilde2 + " LIKE '%" + search
											+ "%' OR " + tilde1 + "Usuario" + tilde2 + " LIKE '%" + search + "%' ORDER BY Nombres ASC";
								request.getSession().setAttribute("tablauser", val.getRows(query));
								request.getSession().setAttribute("existablauser", "1");
								request.getSession().setAttribute("suser", search);
							} else {
								request.getSession().setAttribute("tablauser", null);
								request.getSession().setAttribute("existablauser", null);
								request.getSession().setAttribute("suser", null);
							}
							request.getRequestDispatcher("view.jsp").forward(request, response);
						}
					} catch (SQLException e) {
						request.getSession().setAttribute("mensviewuser", "No se pueden mostrar los datos solicitados.");
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
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
						query = "SELECT ReadUsuario FROM Accesos WHERE IdUsuario = '" + request.getSession().getAttribute("id") + "'";
						ArrayList<String> accesos = val.getRow(query);
						if(accesos.get(0).equals("0")) {
							response.sendRedirect("../usuario");
						}
						else {
							String search = request.getParameter("txtSearch");
							if (search != null && !search.equals("")) {
								search = search.replace("á", "a").replace("é", "e").replace("í", "i").replace("ó", "o").replace("ú", "u");
							}
							String tilde1 = "replace(replace(replace(replace(replace(";
							String tilde2 = ", 'á', 'a'), 'é', 'e'), 'í', 'i'), 'ó', 'o'), 'ú', 'u')";
							if (search != null && search != "") {
								query = "SELECT Id, Nombres, Apellidos, Usuario FROM Usuarios";
								if (search.equals("@all"))
									query += " ORDER BY Nombres ASC";
								else
									query += " WHERE " + tilde1 + "Nombres" + tilde2 + " LIKE '%" + search + "%' OR " + tilde1 + "Apellidos" + tilde2 + " LIKE '%" + search
									+ "%' OR " + tilde1 + "Usuario" + tilde2 + " LIKE '%" + search + "%' ORDER BY Nombres ASC";
								request.getSession().setAttribute("tablauser", val.getRows(query));
								request.getSession().setAttribute("existablauser", "1");
								request.getSession().setAttribute("suser", search);
								request.getSession().setAttribute("sinputuser", search);
							} else {
								request.getSession().setAttribute("tablauser", null);
								request.getSession().setAttribute("existablauser", null);
								request.getSession().setAttribute("suser", null);
								request.getSession().setAttribute("sinputuser", null);
							}
							request.getRequestDispatcher("view.jsp").forward(request, response);
						}
					} catch (SQLException e) {
						request.getSession().setAttribute("mensviewuser", "No se pueden mostrar los datos solicitados.");
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
