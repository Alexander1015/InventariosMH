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
 * Servlet implementation class Vista_Bitacora_Min
 */
@WebServlet("/dashboard/inventario/bitacora_min")
public class Vista_Bitacora_Min extends HttpServlet {

	Validacion val = new Validacion();

	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Vista_Bitacora_Min() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		/* Inicio verificar sesi?n */
		String query = "SELECT Nombres, Apellidos, Defecto FROM Usuarios WHERE Id = '" + request.getSession().getAttribute("id")
				+ "' ORDER BY Id DESC LIMIT 1";
		String usuario = "";
		try {
			ArrayList<String> datosobt = val.getRow(query);
			if (!datosobt.isEmpty()) {
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
					query = "SELECT DevolverProducto FROM Accesos WHERE IdUsuario = '" + request.getSession().getAttribute("id") + "'";
					ArrayList<String> accesos = val.getRow(query);
					if(accesos.size() == 0) {
						accesos.add(0, "0");
					}
					if(accesos.get(0).equals("0")) {
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
		/* Inicio verificar sesi?n */
		String query = "SELECT Nombres, Apellidos, Defecto FROM Usuarios WHERE Id = '" + request.getSession().getAttribute("id")
				+ "' ORDER BY Id DESC LIMIT 1";
		String usuario = "";
		try {
			ArrayList<String> datosobt = val.getRow(query);
			if (!datosobt.isEmpty()) {
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
						/* Fin de verificar sesi?n */
						query = "SELECT DevolverProducto FROM Accesos WHERE IdUsuario = '" + request.getSession().getAttribute("id") + "'";
						ArrayList<String> accesos = val.getRow(query);
						if(accesos.size() == 0) {
							accesos.add(0, "0");
						}
						if(accesos.get(0).equals("0")) {
							response.sendRedirect("../inventario");
						}
						else {
							String fechaInicio = request.getParameter("dtInicio"), fcInit = ""; // 2021-07-07
							String fechaFin = request.getParameter("dtFin"), fcFin = "";
							int dtinicio = 0, dtfin = 0;
							String tilde1 = "replace(replace(replace(replace(replace(";
							String tilde2 = ", '?', 'a'), '?', 'e'), '?', 'i'), '?', 'o'), '?', 'u')";
							String saccesorio = request.getParameter("txtSearchAcc").replace("?", "a").replace("?", "e").replace("?", "i").replace("?", "o").replace("?", "u");
							String susuario = request.getParameter("txtSearchUser").replace("?", "a").replace("?", "e").replace("?", "i").replace("?", "o").replace("?", "u");
							request.getSession().setAttribute("retdevsearch", request.getParameter("txtSearchUser"));
							request.getSession().setAttribute("retdevsearchacc", request.getParameter("txtSearchAcc"));
							request.getSession().setAttribute("retdevinicio", fechaInicio);
							request.getSession().setAttribute("retdevfin", fechaFin);
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
							query = "SELECT I.Id, I.UsuarioExterno, I.UbicacionExterna, I.Accion, I.Fecha, I.Hora, U.Nombres, U.Apellidos, (U.Nombres || ' ' || U.Apellidos) AS Completo, I.Caso, I.Referencia, I.Comentario FROM Inventarios AS I "
									+ "INNER JOIN Usuarios AS U ON I.IdUsuario = U.Id INNER JOIN Acciones AS A ON A.IdInventario = I.Id INNER JOIN Productos AS P ON P.Id = A.IdProducto INNER JOIN Tipo_Productos AS T ON P.Tipo = T.Id ";
							if ((susuario != null && susuario != "") || (saccesorio != null && saccesorio != "")) {						//Para concatenar en SQLite es ||
								if (susuario.equals("@all") || saccesorio.equals("@all"))
									query += "WHERE I.Estado = '1' AND I.Accion = '3' ORDER BY I.Fecha DESC, I.Hora DESC";
								else {
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
									if (susuario == null || susuario == "") {
										query += "(" + tilde1 + "P.Nombre" + tilde2 + " LIKE '%" + saccesorio + "%'"
												+ " OR " + tilde1 + "P.Marca" + tilde2 + "LIKE '%" + saccesorio + "%'"
												+ " OR " + tilde1 + "P.Modelo" + tilde2 + "LIKE '%" + saccesorio + "%'"
												+ " OR " + tilde1 + "P.Serie" + tilde2 + "LIKE '%" + saccesorio + "%'"
												+ " OR " + tilde1 + "P.ActivoFijo" + tilde2 + "LIKE '%" + saccesorio + "%'"
												+ " OR " + tilde1 + "P.Codigo" + tilde2 + "LIKE '%" + saccesorio + "%'"
												+ " OR " + tilde1 + "T.Nombre" + tilde2 + "LIKE '%" + saccesorio + "%'"
												+ ") AND I.Estado = '1' AND I.Accion = '3' ORDER BY I.Fecha DESC, I.Hora DESC";
									} else {
										query += "(" + tilde1 + "I.UsuarioExterno" + tilde2 + " LIKE '%" + susuario + "%' OR " + tilde1 + "I.UbicacionExterna" + tilde2
												+ " LIKE '%" + susuario + "%' OR " + tilde1 + "U.Nombres" + tilde2 + " LIKE '%" + susuario + "%' OR " + tilde1 + "U.Apellidos"
												+ tilde2 + " LIKE '%" + susuario + "%' OR " + tilde1 +  "Completo" + tilde2 + " LIKE '%" + susuario + "%' OR " + tilde1 + "I.Caso" + tilde2 + "LIKE '%" + susuario + "%'"
												+ "OR " + tilde1 + "I.Referencia" + tilde2 + "LIKE '%" + susuario + "%'";
										if (saccesorio != null && !saccesorio.trim().equals("")) {
											query += " OR " + tilde1 + "P.Nombre" + tilde2 + " LIKE '%" + saccesorio + "%'"
													+ " OR " + tilde1 + "P.Marca" + tilde2 + "LIKE '%" + saccesorio + "%'"
													+ " OR " + tilde1 + "P.Modelo" + tilde2 + "LIKE '%" + saccesorio + "%'"
													+ " OR " + tilde1 + "P.Serie" + tilde2 + "LIKE '%" + saccesorio + "%'"
													+ " OR " + tilde1 + "P.ActivoFijo" + tilde2 + "LIKE '%" + saccesorio + "%'"
													+ " OR " + tilde1 + "P.Codigo" + tilde2 + "LIKE '%" + saccesorio + "%'"
													+ " OR " + tilde1 + "T.Nombre" + tilde2 + "LIKE '%" + saccesorio + "%'";
										}
										query += ") AND I.Estado = '1' AND I.Accion = '3' ORDER BY I.Fecha DESC, I.Hora DESC";
									}
								}
								request.getSession().setAttribute("tabla_bit_min", val.getRows(query));
								request.getSession().setAttribute("existablabitmin", "1");
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
								query += "AND I.Estado = '1' AND I.Accion = '3' ORDER BY I.Fecha DESC, I.Hora DESC";
								request.getSession().setAttribute("tabla_bit_min", val.getRows(query));
								request.getSession().setAttribute("existablabitmin", "1");
							} else {
								request.getSession().setAttribute("tabla_bit_min", null);
								request.getSession().setAttribute("existablabitmin", null);
							}
							response.sendRedirect("./devolucion.jsp");
						}
					} catch (SQLException e) {
						request.getSession().setAttribute("mensbitnventario", "No se pueden mostrar los datos solicitados.");
						response.sendRedirect("./devolucion");
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
