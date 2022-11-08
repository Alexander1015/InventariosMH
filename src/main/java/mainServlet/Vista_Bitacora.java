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
 * Servlet implementation class Vista_Bitacora
 */
@WebServlet("/dashboard/bitacora/vista")
public class Vista_Bitacora extends HttpServlet {

	Validacion val = new Validacion();

	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Vista_Bitacora() {
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
						/* Fin de verificar sesión */
						query = "SELECT ReadBitacora FROM Accesos WHERE IdUsuario = '" + request.getSession().getAttribute("id") + "'";
						ArrayList<String> accesos = val.getRow(query);
						if(accesos.size() == 0) {
							accesos.add(0, "0");
						}
						if(accesos.get(0).equals("0")) {
							response.sendRedirect("../");
						}
						else {
							String search = (String) request.getSession().getAttribute("sbita");
							if (search != null && !search.equals("")) {
								search = search.replace("á", "a").replace("é", "e").replace("í", "i").replace("ó", "o").replace("ú", "u");
							}
							String saccesorio = (String) request.getSession().getAttribute("sbitacce");
							if (saccesorio != null && !search.equals("")) {
								saccesorio = saccesorio.replace("á", "a").replace("é", "e").replace("í", "i").replace("ó", "o").replace("ú", "u");
							}
							String accion = (String) request.getSession().getAttribute("sbitaccion");
							String fechaInicio = (String) request.getSession().getAttribute("sbitantes"), fcInit = ""; // 2021-07-07
							String fechaFin = (String) request.getSession().getAttribute("sbitadespues"), fcFin = "";
							int dtinicio = 0, dtfin = 0;
							if((search == null || search.equals("")) && (saccesorio == null || saccesorio.equals("")) && (accion == null || accion.equals("")) && (fechaInicio == null || fechaInicio.equals("")) && (fechaFin == null || fechaFin.equals(""))) {
								//Si esta vacio mostrara los ultimos 10 procesos
								query = "SELECT I.Id, U.Nombres, U.Apellidos, I.Accion, I.Fecha, I.Hora, (U.Nombres || ' ' || U.Apellidos) AS Completo, I.UsuarioExterno, I.UbicacionExterna, I.Caso, U.Id, I.Comentario, I.Autorizacion, I.Referencia "
										+ "FROM Inventarios AS I INNER JOIN Usuarios AS U ON U.Id = I.IdUsuario WHERE I.Estado = '1' AND I.Accion <> '0' ORDER BY I.Fecha DESC, I.Hora DESC LIMIT 10";
								request.getSession().setAttribute("tablabita", val.getRows(query));
								request.getSession().setAttribute("existablabita", "1");
								request.getSession().setAttribute("sbita", null);
								request.getSession().setAttribute("sbitacce", null);
								request.getSession().setAttribute("sbitantes", null);
								request.getSession().setAttribute("sbitadespues", null);
								request.getSession().setAttribute("sbitaccion", null);
							}
							else {
								String tilde1 = "replace(replace(replace(replace(replace(";
								String tilde2 = ", 'á', 'a'), 'é', 'e'), 'í', 'i'), 'ó', 'o'), 'ú', 'u')";
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
								query = "SELECT I.Id, U.Nombres, U.Apellidos, I.Accion, I.Fecha, I.Hora, (U.Nombres || ' ' || U.Apellidos) AS Completo, I.UsuarioExterno, I.UbicacionExterna, I.Caso, U.Id, I.Comentario, I.Autorizacion, I.Referencia  FROM Inventarios AS I INNER JOIN Usuarios AS U ON U.Id = I.IdUsuario INNER JOIN Acciones AS A ON A.IdInventario = I.Id ";
								if ((search != null && search != "") || (saccesorio != null && saccesorio != "")) {
									if (search.equals("@all") || saccesorio.equals("@all"))
										query += "WHERE I.Estado = '1' ORDER BY I.Fecha DESC, I.Hora DESC";
									else {
										if (saccesorio != null && !saccesorio.trim().equals("")) {
											query += "INNER JOIN Productos AS P ON P.Id = A.IdProducto INNER JOIN Tipo_Productos AS T ON P.Tipo = T.Id ";
										}
										query += "WHERE ";
										if (dtinicio != 0 && dtfin != 0) {
											if (dtinicio == dtfin)
												query += "(I.Fecha = '" + dtinicio + "' OR A.FechaDescripcion = '" + dtinicio + "') ";
											else
												query += "(I.Fecha BETWEEN  '" + dtinicio + "' AND '" + dtfin + "' OR A.FechaDescripcion BETWEEN  '" + dtinicio + "' AND '" + dtfin + "') ";
										} else if (dtinicio != 0)
											query += "(I.Fecha = '" + dtinicio + "' OR A.FechaDescripcion = '" + dtinicio + "') ";
										else if (dtfin != 0)
											query += "(I.Fecha = '" + dtfin + "' OR A.FechaDescripcion = '" + dtfin + "') ";
										if (dtinicio != 0 || dtfin != 0)
											query += "AND ";
										if (accion != null && !accion.trim().equals(""))
											query += "I.Accion = '" + accion + "' AND";
										if (search == null || search == "") {
											query += "(" + tilde1 + "P.Nombre" + tilde2 + " LIKE '%" + saccesorio + "%'"
													+ " OR " + tilde1 + "P.Marca" + tilde2 + "LIKE '%" + saccesorio + "%'"
													+ " OR " + tilde1 + "P.Modelo" + tilde2 + "LIKE '%" + saccesorio + "%'"
													+ " OR " + tilde1 + "P.Serie" + tilde2 + "LIKE '%" + saccesorio + "%'"
													+ " OR " + tilde1 + "P.ActivoFijo" + tilde2 + "LIKE '%" + saccesorio + "%'"
													+ " OR " + tilde1 + "P.Codigo" + tilde2 + "LIKE '%" + saccesorio + "%'"
													+ " OR " + tilde1 + "T.Nombre" + tilde2 + "LIKE '%" + saccesorio + "%'"
													+ ")";
										} else {
											query += "(" + tilde1 + "U.Nombres" + tilde2 + " LIKE '%" + search + "%' OR " + tilde1 + "U.Apellidos" + tilde2 + " LIKE '%" + search
													+ "%' OR " + tilde1 + "Completo" + tilde2 + " LIKE '%" + search + "%' OR " + tilde1 + "I.UsuarioExterno" + tilde2 + " LIKE '%" + search 
													+ "%' OR " + tilde1 + "I.UbicacionExterna" + tilde2 + " LIKE '%" + search + "%' OR " + tilde1 + "I.Caso" + tilde2 + " LIKE '%" + search
													+ "%' OR " + tilde1 + "I.Referencia" + tilde2 + " LIKE '%" + search + "%' OR " + tilde1 + "I.Autorizacion" + tilde2 + " LIKE '%" + search + "%'";
											if (saccesorio != null && !saccesorio.trim().equals("")) {
												query += " OR " + tilde1 + "P.Nombre" + tilde2 + " LIKE '%" + saccesorio + "%'"
														+ " OR " + tilde1 + "P.Marca" + tilde2 + "LIKE '%" + saccesorio + "%'"
														+ " OR " + tilde1 + "P.Modelo" + tilde2 + "LIKE '%" + saccesorio + "%'"
														+ " OR " + tilde1 + "P.Serie" + tilde2 + "LIKE '%" + saccesorio + "%'"
														+ " OR " + tilde1 + "P.ActivoFijo" + tilde2 + "LIKE '%" + saccesorio + "%'"
														+ " OR " + tilde1 + "P.Codigo" + tilde2 + "LIKE '%" + saccesorio + "%'"
														+ " OR " + tilde1 + "T.Nombre" + tilde2 + "LIKE '%" + saccesorio + "%'";
											}
											query += ") AND I.Estado = '1' ORDER BY I.Fecha DESC, I.Hora DESC";
										}
									}
									request.getSession().setAttribute("tablabita", val.getRows(query));
									request.getSession().setAttribute("existablabita", "1");
									if (saccesorio != null) request.getSession().setAttribute("sbita", search);
									else request.getSession().setAttribute("sbita", null);
									if (saccesorio != null) request.getSession().setAttribute("sbitacce", saccesorio);
									else request.getSession().setAttribute("sbitacce", null);
									request.getSession().setAttribute("sbitantes", fechaInicio);
									request.getSession().setAttribute("sbitadespues", fechaFin);
									request.getSession().setAttribute("sbitaccion", accion);
								} else if (dtinicio != 0 || dtfin != 0) {
									query += "WHERE ";
									if (dtinicio != 0 && dtfin != 0) {
										if (dtinicio == dtfin)
											query += "(I.Fecha = '" + dtinicio + "' OR A.FechaDescripcion = '" + dtinicio + "') ";
										else
											query += "(I.Fecha BETWEEN  '" + dtinicio + "' AND '" + dtfin + "' OR A.FechaDescripcion BETWEEN  '" + dtinicio + "' AND '" + dtfin + "') ";
									} else if (dtinicio != 0)
										query += "(I.Fecha = '" + dtinicio + "' OR A.FechaDescripcion = '" + dtinicio + "') ";
									else if (dtfin != 0)
										query += "(I.Fecha = '" + dtfin + "' OR A.FechaDescripcion = '" + dtfin + "') ";
									if (accion != null && !accion.trim().equals("") && !accion.equals("6"))
										query += "AND I.Accion = '" + accion + "' ";
									query += "AND I.Estado = '1' ORDER BY I.Fecha DESC, I.Hora DESC";
									request.getSession().setAttribute("tablabita", val.getRows(query));
									request.getSession().setAttribute("existablabita", "1");
									if (saccesorio != null) request.getSession().setAttribute("sbita", search);
									else request.getSession().setAttribute("sbita", null);
									if (saccesorio != null) request.getSession().setAttribute("sbitacce", saccesorio);
									else request.getSession().setAttribute("sbitacce", null);
									request.getSession().setAttribute("sbitantes", fechaInicio);
									request.getSession().setAttribute("sbitadespues", fechaFin);
									request.getSession().setAttribute("sbitaccion", accion);
								} else if (accion != null && !accion.trim().equals("") && !accion.equals("6")) {
									query += "WHERE I.Accion='" + accion
											+ "' AND I.Estado = '1' ORDER BY I.Fecha DESC, I.Hora DESC";
									request.getSession().setAttribute("tablabita", val.getRows(query));
									request.getSession().setAttribute("existablabita", "1");
									if (saccesorio != null) request.getSession().setAttribute("sbita", search);
									else request.getSession().setAttribute("sbita", null);
									if (saccesorio != null) request.getSession().setAttribute("sbitacce", saccesorio);
									else request.getSession().setAttribute("sbitacce", null);
									request.getSession().setAttribute("sbitantes", fechaInicio);
									request.getSession().setAttribute("sbitadespues", fechaFin);
									request.getSession().setAttribute("sbitaccion", accion);
								} else {
									request.getSession().setAttribute("tablabita", null);
									request.getSession().setAttribute("existablabita", null);
									request.getSession().setAttribute("sbita", null);
									request.getSession().setAttribute("sbitacce", null);
									request.getSession().setAttribute("sbitantes", null);
									request.getSession().setAttribute("sbitadespues", null);
									request.getSession().setAttribute("sbitaccion", null);
								}
							}
							request.getRequestDispatcher("view.jsp").forward(request, response);
						}
					} catch (SQLException e) {
						request.getSession().setAttribute("mensbitacora", "No se pueden mostrar los datos solicitados.");
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
		String query = "SELECT Nombres, Apellidos, Defecto FROM Usuarios WHERE Id = '" + request.getSession().getAttribute("id")
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
						/* Fin de verificar sesión */
						query = "SELECT ReadBitacora FROM Accesos WHERE IdUsuario = '" + request.getSession().getAttribute("id") + "'";
						ArrayList<String> accesos = val.getRow(query);
						if(accesos.size() == 0) {
							accesos.add(0, "0");
						}
						if(accesos.get(0).equals("0")) {
							response.sendRedirect("../");
						}
						else {
							String search = request.getParameter("txtSearch");
							if (search != null && !search.equals("")) {
								search = search.replace("á", "a").replace("é", "e").replace("í", "i").replace("ó", "o").replace("ú", "u");
							}
							String saccesorio = request.getParameter("txtSearchAcc");
							if (saccesorio != null && !search.equals("")) {
								saccesorio = saccesorio.replace("á", "a").replace("é", "e").replace("í", "i").replace("ó", "o").replace("ú", "u");
							}
							String accion = "";
							if (request.getParameter("cmbAccion") != null && !request.getParameter("cmbAccion").trim().equals("")) {
								accion = request.getParameter("cmbAccion");
								if (accion.equals("6"))
									accion = "";
							}
							String fechaInicio = request.getParameter("dtInicio"), fcInit = ""; // 2021-07-07
							String fechaFin = request.getParameter("dtFin"), fcFin = "";
							if((search == null || search.equals("")) && (saccesorio == null || saccesorio.equals("")) && (accion == null || accion.equals("")) && (fechaInicio == null || fechaInicio.equals("")) && (fechaFin == null || fechaFin.equals(""))) {
								//Si esta vacio mostrara los ultimos 10 procesos
								query = "SELECT I.Id, U.Nombres, U.Apellidos, I.Accion, I.Fecha, I.Hora, (U.Nombres || ' ' || U.Apellidos) AS Completo, I.UsuarioExterno, I.UbicacionExterna, I.Caso, U.Id, I.Comentario, I.Autorizacion, I.Referencia "
										+ "FROM Inventarios AS I INNER JOIN Usuarios AS U ON U.Id = I.IdUsuario WHERE I.Estado = '1' AND I.Accion <> '0' ORDER BY I.Fecha DESC, I.Hora DESC LIMIT 10";
								request.getSession().setAttribute("tablabita", val.getRows(query));
								request.getSession().setAttribute("existablabita", "1");
								request.getSession().setAttribute("sbita", null);
								request.getSession().setAttribute("sbitacce", null);
								request.getSession().setAttribute("sbitantes", null);
								request.getSession().setAttribute("sbitadespues", null);
								request.getSession().setAttribute("sbitaccion", null);
							}
							else {
								int dtinicio = 0, dtfin = 0;
								String tilde1 = "replace(replace(replace(replace(replace(";
								String tilde2 = ", 'á', 'a'), 'é', 'e'), 'í', 'i'), 'ó', 'o'), 'ú', 'u')";
								for (int i = 0; i <= 9; i++) {
									if (fechaInicio != null && !fechaInicio.trim().equals("") && fechaInicio.charAt(i) != "-".charAt(0))
										fcInit += fechaInicio.charAt(i) + ""; // 20210707
									if (fechaFin != null && !fechaFin.trim().equals("") && fechaFin.charAt(i) != "-".charAt(0))
										fcFin += fechaFin.charAt(i) + "";
								}
								if (fechaInicio != null && !fechaInicio.trim().equals(""))
									dtinicio = Integer.parseInt(fcInit);
								if (fechaFin != null && !fechaFin.trim().equals(""))
									dtfin = Integer.parseInt(fcFin);
								query = "SELECT I.Id, U.Nombres, U.Apellidos, I.Accion, I.Fecha, I.Hora, (U.Nombres || ' ' || U.Apellidos) AS Completo, I.UsuarioExterno, I.UbicacionExterna, I.Caso, U.Id, I.Comentario, I.Autorizacion, I.Referencia FROM Inventarios AS I INNER JOIN Usuarios AS U ON U.Id = I.IdUsuario INNER JOIN Acciones AS A ON A.IdInventario = I.Id ";
								if ((search != null && search != "") || (saccesorio != null && saccesorio != "")) {
									if (search.equals("@all") || saccesorio.equals("@all"))
										query += "WHERE I.Estado = '1' ORDER BY I.Fecha DESC, I.Hora DESC";
									else {
										if (saccesorio != null && !saccesorio.trim().equals("")) {
											query += "INNER JOIN Productos AS P ON P.Id = A.IdProducto INNER JOIN Tipo_Productos AS T ON P.Tipo = T.Id ";
										}
										query += "WHERE ";
										if (dtinicio != 0 && dtfin != 0) {
											if (dtinicio == dtfin)
												query += "(I.Fecha = '" + dtinicio + "' OR A.FechaDescripcion = '" + dtinicio + "') ";
											else
												query += "(I.Fecha BETWEEN  '" + dtinicio + "' AND '" + dtfin + "' OR A.FechaDescripcion BETWEEN  '" + dtinicio + "' AND '" + dtfin + "') ";
										} else if (dtinicio != 0)
											query += "(I.Fecha = '" + dtinicio + "' OR A.FechaDescripcion = '" + dtinicio + "') ";
										else if (dtfin != 0)
											query += "(I.Fecha = '" + dtfin + "' OR A.FechaDescripcion = '" + dtfin + "') ";
										if (dtinicio != 0 || dtfin != 0)
											query += "AND ";
										if (accion != null && !accion.trim().equals(""))
											query += "I.Accion = '" + accion + "' AND";
										if (search == null || search == "") {
											query += "(" + tilde1 + "P.Nombre" + tilde2 + " LIKE '%" + saccesorio + "%'"
													+ " OR " + tilde1 + "P.Marca" + tilde2 + "LIKE '%" + saccesorio + "%'"
													+ " OR " + tilde1 + "P.Modelo" + tilde2 + "LIKE '%" + saccesorio + "%'"
													+ " OR " + tilde1 + "P.Serie" + tilde2 + "LIKE '%" + saccesorio + "%'"
													+ " OR " + tilde1 + "P.ActivoFijo" + tilde2 + "LIKE '%" + saccesorio + "%'"
													+ " OR " + tilde1 + "P.Codigo" + tilde2 + "LIKE '%" + saccesorio + "%'"
													+ " OR " + tilde1 + "T.Nombre" + tilde2 + "LIKE '%" + saccesorio + "%'"
													+ ") AND I.Estado = '1' ORDER BY I.Fecha DESC, I.Hora DESC";
										} else {
											query += "(" + tilde1 + "U.Nombres" + tilde2 + " LIKE '%" + search + "%' OR " + tilde1 + "U.Apellidos" + tilde2 + " LIKE '%" + search
													+ "%' OR " + tilde1 + "Completo" + tilde2 + " LIKE '%" + search + "%' OR " + tilde1 + "I.UsuarioExterno" + tilde2 + " LIKE '%" + search 
													+ "%' OR " + tilde1 + "I.UbicacionExterna" + tilde2 + " LIKE '%" + search + "%' OR " + tilde1 + "I.Caso" + tilde2 + " LIKE '%" + search
													+ "%' OR " + tilde1 + "I.Referencia" + tilde2 + " LIKE '%" + search + "%' OR " + tilde1 + "I.Autorizacion" + tilde2 + " LIKE '%" + search + "%'";
											if (saccesorio != null && !saccesorio.trim().equals("")) {
												query += " OR " + tilde1 + "P.Nombre" + tilde2 + " LIKE '%" + saccesorio + "%'"
														+ " OR " + tilde1 + "P.Marca" + tilde2 + "LIKE '%" + saccesorio + "%'"
														+ " OR " + tilde1 + "P.Modelo" + tilde2 + "LIKE '%" + saccesorio + "%'"
														+ " OR " + tilde1 + "P.Serie" + tilde2 + "LIKE '%" + saccesorio + "%'"
														+ " OR " + tilde1 + "P.ActivoFijo" + tilde2 + "LIKE '%" + saccesorio + "%'"
														+ " OR " + tilde1 + "P.Codigo" + tilde2 + "LIKE '%" + saccesorio + "%'"
														+ " OR " + tilde1 + "T.Nombre" + tilde2 + "LIKE '%" + saccesorio + "%'";
											}
											query += ") AND I.Estado = '1' ORDER BY I.Fecha DESC, I.Hora DESC";
										}
									}
									request.getSession().setAttribute("tablabita", val.getRows(query));
									request.getSession().setAttribute("existablabita", "1");
									if (saccesorio != null) request.getSession().setAttribute("sbita", search);
									else request.getSession().setAttribute("sbita", null);
									if (saccesorio != null) request.getSession().setAttribute("sbitacce", saccesorio);
									else request.getSession().setAttribute("sbitacce", null);
									request.getSession().setAttribute("sbitantes", fechaInicio);
									request.getSession().setAttribute("sbitadespues", fechaFin);
									request.getSession().setAttribute("sbitaccion", accion);
								} else if (dtinicio != 0 || dtfin != 0) {
									query += "WHERE ";
									if (dtinicio != 0 && dtfin != 0) {
										if (dtinicio == dtfin)
											query += "(I.Fecha = '" + dtinicio + "' OR A.FechaDescripcion = '" + dtinicio + "') ";
										else
											query += "(I.Fecha BETWEEN  '" + dtinicio + "' AND '" + dtfin + "' OR A.FechaDescripcion BETWEEN  '" + dtinicio + "' AND '" + dtfin + "') ";
									} else if (dtinicio != 0)
										query += "(I.Fecha = '" + dtinicio + "' OR A.FechaDescripcion = '" + dtinicio + "') ";
									else if (dtfin != 0)
										query += "(I.Fecha = '" + dtfin + "' OR A.FechaDescripcion = '" + dtfin + "') ";
									if (accion != null && !accion.trim().equals(""))
										query += "AND I.Accion = '" + accion + "' ";
									query += "AND I.Estado = '1' ORDER BY I.Fecha DESC, I.Hora DESC";
									request.getSession().setAttribute("tablabita", val.getRows(query));
									request.getSession().setAttribute("existablabita", "1");
									if (saccesorio != null) request.getSession().setAttribute("sbita", search);
									else request.getSession().setAttribute("sbita", null);
									if (saccesorio != null) request.getSession().setAttribute("sbitacce", saccesorio);
									else request.getSession().setAttribute("sbitacce", null);
									request.getSession().setAttribute("sbitantes", fechaInicio);
									request.getSession().setAttribute("sbitadespues", fechaFin);
									request.getSession().setAttribute("sbitaccion", accion);
								} else if (accion != null && !accion.trim().equals("")) {
									query += "WHERE I.Accion='" + accion + "' AND I.Estado = '1' ORDER BY I.Fecha DESC, I.Hora DESC";
									request.getSession().setAttribute("tablabita", val.getRows(query));
									request.getSession().setAttribute("existablabita", "1");
									if (saccesorio != null) request.getSession().setAttribute("sbita", search);
									else request.getSession().setAttribute("sbita", null);
									if (saccesorio != null) request.getSession().setAttribute("sbitacce", saccesorio);
									else request.getSession().setAttribute("sbitacce", null);
									request.getSession().setAttribute("sbitantes", fechaInicio);
									request.getSession().setAttribute("sbitadespues", fechaFin);
									request.getSession().setAttribute("sbitaccion", accion);
								} else {
									request.getSession().setAttribute("tablabita", null);
									request.getSession().setAttribute("existablabita", null);
									request.getSession().setAttribute("sbita", null);
									request.getSession().setAttribute("sbitacce", null);
									request.getSession().setAttribute("sbitantes", null);
									request.getSession().setAttribute("sbitadespues", null);
									request.getSession().setAttribute("sbitaccion", null);
								}
							}
						}
					} catch (SQLException e) {
						request.getSession().setAttribute("mensbitacora", "No se pueden mostrar los datos solicitados.");
					}
					request.getSession().setAttribute("sinputbita", request.getSession().getAttribute("sbita") == null ? null : request.getSession().getAttribute("sbita"));
					request.getSession().setAttribute("sinputbitacce", request.getSession().getAttribute("sbitacce") == null ? null : request.getSession().getAttribute("sbitacce"));
					request.getSession().setAttribute("sinputbitantes", request.getSession().getAttribute("sbitantes") == null ? null : request.getSession().getAttribute("sbitantes"));
					request.getSession().setAttribute("sinputbitadespues", request.getSession().getAttribute("sbitadespues") == null ? null : request.getSession().getAttribute("sbitadespues"));
					request.getSession().setAttribute("sinputbitaccion", request.getSession().getAttribute("sbitaccion") == null ? null : request.getSession().getAttribute("sbitaccion"));
					request.getRequestDispatcher("view.jsp").forward(request, response);
				}
			} else {
				response.sendRedirect("../desconectar");
			}
		} catch (SQLException | IOException e) {
			e.printStackTrace();
		}
	}
}
