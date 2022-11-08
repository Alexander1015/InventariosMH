package deleteServlet;

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
 * Servlet implementation class Eliminar_Usuarios
 */
@WebServlet("/dashboard/usuario/eliminar")
public class Eliminar_Usuarios extends HttpServlet {

	Validacion val = new Validacion();

	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Eliminar_Usuarios() {
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
					query = "SELECT DeleteUsuario FROM Accesos WHERE IdUsuario = '" + request.getSession().getAttribute("id") + "'";
					ArrayList<String> accesos = val.getRow(query);
					if(accesos.size() == 0) {
						accesos.add(0, "0");
					}
					if(accesos.get(0).equals("1")) {
						String id = request.getParameter("id");
						query = "SELECT U.Id FROM Usuarios AS U INNER JOIN Accesos AS A ON U.Id = A.IdUsuario "
								+ "WHERE A.UpdateAcceso = '1' ORDER BY U.Id";
						ArrayList<ArrayList<String>> onlyaccess = val.getRows(query);
						if(onlyaccess.size() == 1 && onlyaccess.get(1).get(0).equals(id)) {
							request.getSession().setAttribute("mensviewuser", "No se puede eliminar el Usuario seleccionado debido a que no existe otro que tenga acceso a Accesos/Permisos.");
						}
						else {
							query = "SELECT Id FROM Usuarios WHERE Id = '" + id + "'";
							ArrayList<String> idbd = val.getRow(query);
							int cant = idbd.size();
							if(cant > 0) {
								query = "SELECT Id FROM Inventarios WHERE IdUsuario = '" + id + "'";
								ArrayList<String> evitar = val.getRow(query);
								if(evitar.size() == 0) {
									query = "DELETE FROM Usuarios WHERE Id = '" + idbd.get(0) + "'";
									val.executeQuery(query);
									query = "DELETE FROM Accesos WHERE IdUsuario = '" + idbd.get(0) + "'";
									val.executeQuery(query);
									request.getSession().setAttribute("succViewU", "Success");
								}
								else {
									request.getSession().setAttribute("mensviewuser",
											"No se puede eliminar el usuario, ya que posee registros atribuidos a él.");
								}
							}
							else {
								request.getSession().setAttribute("mensviewuser",
										"El registro seleccionado no existe.");
							}
						}
						response.sendRedirect("./vista");
					}
					else {
						response.sendRedirect("../usuario");
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
					query = "SELECT DeleteUsuario FROM Accesos WHERE IdUsuario = '" + request.getSession().getAttribute("id") + "'";
					ArrayList<String> accesos = val.getRow(query);
					if(accesos.size() == 0) {
						accesos.add(0, "0");
					}
					if(accesos.get(0).equals("1")) {
						response.sendRedirect("./vista");
					}
					else {
						response.sendRedirect("../usuario");
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
