package mainServlet;

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
 * Servlet implementation class Vista_Productos
 */
@WebServlet("/dashboard/inventario/vista")
public class Vista_Productos extends HttpServlet {

	Validacion val = new Validacion();

	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Vista_Productos() {
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
					query = "SELECT ReadProducto FROM Accesos WHERE IdUsuario = '" + request.getSession().getAttribute("id") + "'";
					ArrayList<String> accesos = val.getRow(query);
					if(accesos.size() == 0) {
						accesos.add(0, "0");
					}
					if(accesos.get(0).equals("0")) {
						response.sendRedirect("../inventario");
					}
					else {
						request.getRequestDispatcher("view.jsp").forward(request, response);
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
						query = "SELECT ReadProducto FROM Accesos WHERE IdUsuario = '" + request.getSession().getAttribute("id") + "'";
						ArrayList<String> accesos = val.getRow(query);
						if(accesos.size() == 0) {
							accesos.add(0, "0");
						}
						if(accesos.get(0).equals("0")) {
							response.sendRedirect("../inventario");
						}
						else {
							String reportjsr = "", datarep = "";
							String search = request.getParameter("txtSearch").replace("á", "a").replace("é", "e").replace("í", "i").replace("ó", "o").replace("ú", "u");
							String searchcant = request.getParameter("txtSearchCant"), newcant = "";
							if (searchcant != null && !searchcant.equals("")) {
								int cant = searchcant.length(); 
								for (int i = 0; i < cant; i++) {
									char caracter = searchcant.charAt(i);
									if (i < 6) newcant += caracter;
								}
								searchcant = newcant;
							}
							String fechaInicio = request.getParameter("dtInicio"), fcInit = ""; // 2021-07-07
							String fechaFin = request.getParameter("dtFin"), fcFin = "";
							String tilde1 = "replace(replace(replace(replace(replace(";
							String tilde2 = ", 'á', 'a'), 'é', 'e'), 'í', 'i'), 'ó', 'o'), 'ú', 'u')";
							request.getSession().setAttribute("sinvent", search);
							request.getSession().setAttribute("sinventcant", searchcant);
							request.getSession().setAttribute("sinventantes", fechaInicio);
							request.getSession().setAttribute("sinventdespues", fechaFin);
							//Verificamos el dato que manejaremos como cantidad 
							if (searchcant == null || searchcant.trim().equals("") || !val.isNumero(searchcant) || Integer.parseInt(searchcant) < 0) {
								searchcant = "0";
							}
							int dtinicio = 0, dtfin = 0;
							for (int i = 0; i <= 9; i++) {
								if (fechaInicio != null && !fechaInicio.trim().equals("")
										&& fechaInicio.charAt(i) != "-".charAt(0))
									fcInit += fechaInicio.charAt(i) + ""; // 20210707
								if (fechaFin != null && !fechaFin.trim().equals("") && fechaFin.charAt(i) != "-".charAt(0))
									fcFin += fechaFin.charAt(i) + "";
							}
							if (fechaInicio != null && !fechaInicio.trim().equals(""))
								dtinicio = Integer.parseInt(fcInit);
							if (fechaFin != null && !fechaFin.trim().equals(""))
								dtfin = Integer.parseInt(fcFin);
							if ((search != null && search != "") ||  (searchcant != null && searchcant != "" && val.isNumero(searchcant) && Integer.parseInt(searchcant) >= 0)) {
								query = "SELECT P.Id, P.Nombre, T.Nombre, P.Ubicacion, P.Cantidad, P.Fecha, P.Comentario, P.Codigo, P.Actualizacion, P.Marca, P.Modelo, P.Serie, P.ActivoFijo "
										+ "FROM Productos AS P INNER JOIN Tipo_Productos AS T ON T.Id = P.Tipo ";
								if (search.equals("@all")) {
									datarep += "Estado = '1' ORDER BY P.Nombre ASC";
									reportjsr += "WHERE " + datarep;
								}
								else if (search.trim().equals("") && !searchcant.trim().equals("")) {
									datarep += "Cantidad >= '" + searchcant + "' AND Estado = '1' ORDER BY P.Nombre ASC";
									reportjsr += "WHERE " + datarep;
								}
								else {
									if (dtinicio != 0 && dtfin != 0) {
										if (dtinicio == dtfin) {
											datarep += "P.Fecha = '" + dtinicio + "' OR P.Actualizacion = '" + dtinicio + "' AND Estado = '1' ";
											reportjsr += "WHERE " + datarep;
										}
										else {
											datarep += "P.Fecha BETWEEN  '" + dtinicio + "' AND '" + dtfin
													+ "' OR P.Actualizacion BETWEEN '" + dtinicio + "' AND '" + dtfin + "' AND Estado = '1' ";;
											reportjsr += "WHERE " + datarep;
										}
									} else if (dtinicio != 0) {
										datarep += "P.Fecha = '" + dtinicio + "' OR P.Actualizacion = '" + dtinicio + "' AND Estado = '1' ";
										reportjsr += "WHERE " + datarep;
									}
									else if (dtfin != 0) {
										datarep += "P.Fecha = '" + dtfin + "' OR P.Actualizacion = '" + dtfin + "' AND Estado = '1' ";
										reportjsr += "WHERE " + datarep;
									}
									if (dtinicio != 0 || dtfin != 0) {
										datarep += "AND ";
										reportjsr += datarep;
									}
									else {
										reportjsr += "WHERE ";
									}
									datarep += "(" + tilde1 + "P.Codigo" + tilde2 + " LIKE '%" + search + "%' OR " + tilde1 + "P.Nombre" + tilde2 + " LIKE '%" + search
											+ "%' OR " + tilde1 + "T.Nombre" + tilde2 + " LIKE '%" + search + "%' OR " + tilde1 + "P.Ubicacion" + tilde2 + " LIKE '%" + search
											+ "%' OR " + tilde1 + "P.Cantidad" + tilde2 + " LIKE '%" + search + "%'"
											+  "OR " + tilde1 + "P.Marca" + tilde2 + " LIKE '%" + search + "%'"
											+  "OR " + tilde1 + "P.Modelo" + tilde2 + " LIKE '%" + search + "%'"
											+  "OR " + tilde1 + "P.Serie" + tilde2 + " LIKE '%" + search + "%'"
											+  "OR " + tilde1 + "P.ActivoFijo" + tilde2 + " LIKE '%" + search + "%'"
											+ ") AND P.Cantidad >= '" + searchcant + "' AND P.Estado = '1' ORDER BY P.Nombre ASC";
									reportjsr += datarep;
								}
								query += reportjsr;
								request.getSession().setAttribute("tablaprod", val.getRows(query));
								request.getSession().setAttribute("existablaprod", "1");
							} else if (dtinicio != 0 || dtfin != 0) {
								query = "SELECT P.Codigo, P.Nombre, T.Nombre, P.Ubicacion, P.Cantidad, P.Fecha, P.Comentario, P.Codigo, P.Actualizacion, P.Marca, P.Modelo, P.Serie, P.ActivoFijo FROM Productos AS P INNER JOIN Tipo_Productos AS T ON T.Id = P.Tipo ";
								if (dtinicio != 0 && dtfin != 0) {
									if (dtinicio == dtfin) {
										datarep += "P.Fecha = '" + dtinicio + "' OR P.Actualizacion = '" + dtinicio + "' AND Estado = '1' ";
										reportjsr += "WHERE " + datarep;
									}
									else {
										datarep += "P.Fecha BETWEEN '" + dtinicio + "' AND '" + dtfin
												+ "' OR P.Actualizacion BETWEEN '" + dtinicio + "' AND '" + dtfin + "' AND Estado = '1' ";
										reportjsr += "WHERE " + datarep;
									}
								} else if (dtinicio != 0) {
									datarep += "P.Fecha = '" + dtinicio + "' OR P.Actualizacion = '" + dtinicio + "' AND Estado = '1' ";
									reportjsr += "WHERE " + datarep;
								}
								else if (dtfin != 0) {
									datarep += "P.Fecha = '" + dtfin + "' OR P.Actualizacion = '" + dtfin + "' AND Estado = '1' ";
									reportjsr += "WHERE " + datarep;
								}
								datarep += "ORDER BY P.Nombre ASC";
								reportjsr += datarep;
								query += reportjsr;
								request.getSession().setAttribute("tablaprod", val.getRows(query));
								request.getSession().setAttribute("existablaprod", "1");
							} else {
								request.getSession().setAttribute("tablaprod", null);
								request.getSession().setAttribute("existablaprod", null);
								request.getSession().setAttribute("sinvent", null);
								request.getSession().setAttribute("sinventcant", null);
								request.getSession().setAttribute("sinventantes", null);
								request.getSession().setAttribute("sinventdespues", null);
							}
							request.getSession().setAttribute("reportebit", datarep);
							String registro = imprRegistro(search, searchcant, dtinicio, dtfin);
							if ((search != null && search != "") || (searchcant != null && searchcant != "") || dtinicio != 0 || dtfin != 0) {
								Date dNow = new Date();
								SimpleDateFormat Fsdf = new SimpleDateFormat("yyyy-MM-dd");
								String FHoy = Fsdf.format(dNow);
								String Fecha = "";
								for (int i = 0; i <= 9; i++) {
									if (FHoy.charAt(i) != "-".charAt(0))
										Fecha += FHoy.charAt(i) + ""; // 20210707
								}
								query = "SELECT Id FROM Inventarios WHERE IdUsuario = '"
										+ request.getSession().getAttribute("id") + "' AND Accion = '0'"
										+ " AND Estado = '1' ORDER BY Id DESC LIMIT 1";
								ArrayList<String> inventario = val.getRow(query);
								int cantinvent = inventario.size();
								if (cantinvent > 0) {
									query = "INSERT INTO Acciones (IdInventario, Descripcion, FechaDescripcion) VALUES ('" + inventario.get(0)
											+ "', '" + registro + "', '" + Fecha + "')";
									val.executeQuery(query);
								} else {
									query = "INSERT INTO Inventarios (IdUsuario, Accion, Estado, Fecha) VALUES ('"
											+ request.getSession().getAttribute("id") + "', '0', '1', '" + Fecha + "')";
									val.executeQuery(query);
									query = "SELECT Id FROM Inventarios WHERE IdUsuario = '"
											+ request.getSession().getAttribute("id")
											+ "' AND Accion = '0' ORDER BY Id DESC LIMIT 1";
									inventario = val.getRow(query);
									query = "INSERT INTO Acciones (IdInventario, Descripcion, FechaDescripcion) VALUES ('" + inventario.get(0)
											+ "', '" + registro + "', '" + Fecha + "')";
									val.executeQuery(query);
								}
							}
							request.getRequestDispatcher("view.jsp").forward(request, response);
						}
					} catch (SQLException e) {
						request.getSession().setAttribute("mensviewinventario", "No se pueden mostrar los datos solicitados.");
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

	public String imprRegistro(String search, String searchcant, int dtinicio, int dtfin) {
		String registro = "", fechaInit = "", fechaFin = "";
		if (dtinicio != 0) {
			fechaInit = dtinicio + "";
			fechaInit = fechaInit.charAt(6) + "" + fechaInit.charAt(7) + "-" + fechaInit.charAt(4) + ""
					+ fechaInit.charAt(5) + "-" + fechaInit.charAt(0) + "" + fechaInit.charAt(1) + ""
					+ fechaInit.charAt(2) + "" + fechaInit.charAt(3);
		}
		if (dtfin != 0) {
			fechaFin = dtfin + "";
			fechaFin = fechaFin.charAt(6) + "" + fechaFin.charAt(7) + "-" + fechaFin.charAt(4) + "" + fechaFin.charAt(5)
					+ "-" + fechaFin.charAt(0) + "" + fechaFin.charAt(1) + "" + fechaFin.charAt(2) + ""
					+ fechaFin.charAt(3);
		}
		if ((search != null && search != "") && (searchcant != null && searchcant != "") && dtinicio != 0 && dtfin != 0)
			registro = "Ha realizado una busqueda de: \"" + search + "\", desde la cantidad de: \"" + searchcant + "\" existencia/s para las fechas entre: el \"" + fechaInit
					+ "\" y el \"" + fechaFin + "\"";
		else if ((search != null && search != "") && (searchcant != null && searchcant != "") && dtinicio != 0 && dtfin != 0)
			registro = "Ha realizado una busqueda de: \"" + search + "\" , desde la cantidad de: \"" + searchcant + "\" existencia/s";
		else if ((search != null && search != "") && (searchcant == null || searchcant == "") && dtinicio != 0 && dtfin != 0)
			registro = "Ha realizado una busqueda de: \"" + search + "\"";
		else if ((search == null || search == "") && (searchcant != null && searchcant != "") && dtinicio != 0 && dtfin != 0)
			registro = "Ha realizado una busqueda desde la cantidad de: \"" + searchcant + "\" existencia/s para las fechas entre: el \"" + fechaInit + "\" y el \"" + fechaFin
					+ "\"";
		else if ((search == null || search == "") && (searchcant == null || searchcant == "") && dtinicio != 0 && dtfin != 0)
			registro = "Ha realizado una busqueda para las fechas entre: el \"" + fechaInit + "\" y el \"" + fechaFin
					+ "\"";
		else if ((search != null && search != "") && (searchcant != null && searchcant != "") && dtinicio != 0)
			registro = "Ha realizado una busqueda de: \"" + search + "\" , desde la cantidad de: \"" + searchcant + "\" existencia/s para la fecha: \"" + fechaInit + "\"";
		else if ((search != null && search != "") && (searchcant == null || searchcant == "") && dtinicio != 0)
			registro = "Ha realizado una busqueda de: \"" + search + "\" para la fecha: \"" + fechaInit + "\"";
		else if ((search != null && search != "") && (searchcant != null && searchcant != "") && dtfin != 0)
			registro = "Ha realizado una busqueda de: \"" + search + "\" , desde la cantidad de: \"" + searchcant + "\" existencia/s para la fecha: \"" + fechaFin + "\"";
		else if ((search != null && search != "") && dtfin != 0)
			registro = "Ha realizado una busqueda de: \"" + search + "\" para la fecha: \"" + fechaFin + "\"";
		else if (search != null && search != "")
			registro = "Ha realizado una busqueda de: \"" + search + "\"";
		else if (searchcant != null && searchcant != "")
			registro = "Ha realizado una busqueda desde la cantidad de: \"" + searchcant + "\" existencia/s";
		else if (dtinicio != 0)
			registro = "Ha realizado una busqueda para la fecha: \"" + fechaInit + "\"";
		else if (dtfin != 0)
			registro = "Ha realizado una busqueda para la fecha: \"" + fechaFin + "\"";
		return registro;
	}

}
