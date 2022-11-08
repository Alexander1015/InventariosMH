package saveServlet;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lib.Validacion;

/**
 * Servlet implementation class Devolucion_Productos
 */
@WebServlet("/dashboard/inventario/devolver")
public class Devolucion_Productos extends HttpServlet {

	Validacion val = new Validacion();

	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Devolucion_Productos() {
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
						query = "SELECT DevolverProducto FROM Accesos WHERE IdUsuario = '" + request.getSession().getAttribute("id") + "'";
						ArrayList<String> accesos = val.getRow(query);
						if(accesos.size() == 0) {
							accesos.add(0, "0");
						}
						if(accesos.get(0).equals("0")) {
							response.sendRedirect("../inventario");
						}
						else {
							String idinventario = request.getParameter("id");
							if (idinventario != null && !idinventario.trim().equals("")) {
								query = "SELECT P.Id, P.Nombre, A.Retirado, A.Devuelto, P.Marca, P.Modelo, P.Serie, P.ActivoFijo, P.Comentario FROM Productos AS P INNER JOIN Acciones AS A ON A.IdProducto = P.Id INNER JOIN Inventarios AS I ON A.IdInventario = I.Id WHERE I.Id = '"
										+ idinventario + "' AND I.Accion = '3' AND I.Estado = '1' ORDER BY P.Nombre";
								ArrayList<ArrayList<String>> productos = val.getRows(query);
								int cantidad = productos.size();
								if (cantidad > 0) {
									request.getSession().setAttribute("tabla_productos", productos);
									request.getSession().setAttribute("id_inventario", idinventario);
									request.getSession().setAttribute("succDevP", "Proceso realizado correctamente.");
								} else {
									request.getSession().setAttribute("mensdevinventario",
											"El accesorio/dispositivo a quitar no es el correcto.");
								}
							} else {
								request.getSession().setAttribute("mensdevinventario", "El accesorio/dispositivo a quitar no es el correcto.");
							}
							response.sendRedirect("./devolucion.jsp");
						}
					} catch (SQLException e) {
						request.getSession().setAttribute("mensdevinventario", "Ha ocurrido un error al ejecutar el proceso.");
						response.sendRedirect("./devolucion.jsp");
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
					try {
						query = "SELECT DevolverProducto FROM Accesos WHERE IdUsuario = '" + request.getSession().getAttribute("id") + "'";
						ArrayList<String> accesos = val.getRow(query);
						if(accesos.size() == 0) {
							accesos.add(0, "0");
						}
						if(accesos.get(0).equals("0")) {
							response.sendRedirect("../inventario");
						}
						else {
							String[] productos = request.getParameterValues("checks");
							if (productos != null) {
								String Fecha = "";
								Date dNow = new Date();
								SimpleDateFormat Fsdf = new SimpleDateFormat("yyyy-MM-dd");
								String FHoy = Fsdf.format(dNow);
								for (int i = 0; i <= 9; i++) {
									if (FHoy.charAt(i) != "-".charAt(0))
										Fecha += FHoy.charAt(i) + ""; // 20210707
								}
								// Verificamos el inventario
								query = "SELECT Id FROM Inventarios WHERE IdUsuario = '"
										+ request.getSession().getAttribute("id")
										+ "' AND Accion = '4' AND Estado = '0' ORDER BY Id DESC LIMIT 1";
								ArrayList<String> idInventario = val.getRow(query);
								int existId = idInventario.size();
								if (existId == 0) {
									query = "INSERT INTO Inventarios (IdUsuario, Accion, Estado, Fecha) VALUES ('"
											+ request.getSession().getAttribute("id") + "', '4', '0', '" + Fecha + "')";
									val.executeQuery(query);
									// Volvemos a comprobar
									query = "SELECT Id FROM Inventarios WHERE IdUsuario = '"
											+ request.getSession().getAttribute("id")
											+ "' AND Accion = '4' AND Estado = '0' ORDER BY Id DESC LIMIT 1";
									idInventario = val.getRow(query);
								} else {
									query = "UPDATE Inventarios SET Fecha = '" + Fecha + "' WHERE Id = '"
											+ idInventario.get(0) + "'";
									val.executeQuery(query);
								}
								int error = 0;
								for (int i = 0; i < productos.length; i++) {
									// Guardamos en acciones
									query = "SELECT Id, Cantidad FROM Productos WHERE Id = '" + productos[i]
											+ "' AND Estado = '1'";
									ArrayList<String> idProducto = val.getRow(query);
									int cantidad = idProducto.size();
									if (cantidad > 0) {
										query = "INSERT INTO Acciones (IdInventario, IdProducto, Cantidad) VALUES ('"
												+ idInventario.get(0) + "', '" + idProducto.get(0) + "', '"
												+ idProducto.get(1) + "')";
										val.executeQuery(query);
									} else {
										error++;
										request.getSession().setAttribute("mensbitnventario",
												"El accesorio/dispositivo ingresado no es el correcto.");
									}
									//
								}
								if(error == 0) request.getSession().setAttribute("succDevP", "Proceso realizado correctamente.");
								else request.getSession().setAttribute("succDevP", "Es posible que solo algunos procesos se hayan realizado correctamente.");
								response.sendRedirect("./devolucion.jsp");
							} else {
								request.getSession().setAttribute("mensbitnventario", "No ha seleccionado ningun elemento.");
								response.sendRedirect("./devolucion.jsp");
							}
						}
					} catch (SQLException e) {
						request.getSession().setAttribute("mensbitnventario", "Ha ocurrido un error al ejecutar el proceso.");
						response.sendRedirect("./devolucion.jsp");
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
