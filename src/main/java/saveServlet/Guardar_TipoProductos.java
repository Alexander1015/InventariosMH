package saveServlet;

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
 * Servlet implementation class Guardar_TipoProductos
 */
@WebServlet("/dashboard/tipo_producto/guardar")
public class Guardar_TipoProductos extends HttpServlet {

	Validacion val = new Validacion();

	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Guardar_TipoProductos() {
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
					request.getSession().setAttribute("returntipo", null);
					String id = request.getParameter("id");
					query = "SELECT CreateTipoProducto, UpdateTipoProducto FROM Accesos WHERE IdUsuario = '" + request.getSession().getAttribute("id") + "'";
					ArrayList<String> accesos = val.getRow(query);
					if(accesos.size() == 0) {
						accesos.add(0, "0");
						accesos.add(1, "0");
					}
					if(accesos.get(0).equals("0") && (id == null || id.trim().equals(""))) {
						response.sendRedirect("../tipo_producto");
					}
					else if(accesos.get(1).equals("0") && id != null && !id.trim().equals("")) {
						response.sendRedirect("../tipo_producto");
					}
					else {
						String retrn = request.getParameter("return");
						if (id != null && !id.trim().equals("")) {
							request.getSession().setAttribute("idtab", id);
						} else {
							request.getSession().setAttribute("idtab", null);
						}
						if (retrn != null && !retrn.trim().equals("")) {
							request.getSession().setAttribute("returntipo", retrn);
						}
						request.getRequestDispatcher("save.jsp").forward(request, response);
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
		String usuario = "", link = "./guardar";
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
					String nombre = request.getParameter("txtTipo");
					String simbolo = request.getParameter("txtSimbolo");
					String id = (String) request.getSession().getAttribute("idtab");
					String retrn = (String) request.getSession().getAttribute("returntipo");
					if (id == null || id.trim().equals("")) {
						request.getSession().setAttribute("rettipnombre", nombre);
						request.getSession().setAttribute("rettipsimb", simbolo);
					}
					query = "SELECT CreateTipoProducto, UpdateTipoProducto FROM Accesos WHERE IdUsuario = '" + request.getSession().getAttribute("id") + "'";
					ArrayList<String> accesos = val.getRow(query);
					if(accesos.size() == 0) {
						accesos.add(0, "0");
						accesos.add(1, "0");
					}
					if(accesos.get(0).equals("0") && (id == null || id.trim().equals(""))) {
						response.sendRedirect("../tipo_producto");
					}
					else if(accesos.get(1).equals("0") && id != null && !id.trim().equals("")) {
						response.sendRedirect("../tipo_producto");
					}
					else {
						query = "SELECT Id FROM Tipo_Productos WHERE Id = '" + id + "' ORDER BY Id ASC LIMIT 1";
						ArrayList<String> exist = val.getRow(query);
						if(id != null && !id.trim().equals("") && exist.size() == 0) {
							request.getSession().setAttribute("mensupdatipo",
									"El registro seleccionado no existe.");
							response.sendRedirect("./vista");
						}
						else if (nombre != null && !nombre.trim().equals("")) {
							if (simbolo != null && !simbolo.trim().equals("")) {
								int cantnom = nombre.length();
								int cantsim = simbolo.length();
								if (cantnom > 1 && cantnom <= 100 && cantsim >= 2 && cantsim <= 3) {
									if (!val.existNumeros(nombre) && !val.existNumeros(simbolo)) {
										if (id != null && !id.trim().equals("")) {
											query = "SELECT Nombre, Simbolo FROM Tipo_Productos WHERE (Simbolo = '" + simbolo
												+ "' OR Nombre = '" + nombre + "') AND Id <> '" + id + "' ORDER BY Simbolo ASC LIMIT 1";
										}
										else {
											query = "SELECT Nombre, Simbolo FROM Tipo_Productos WHERE (Simbolo = '" + simbolo
													+ "' OR Nombre = '" + nombre + "') ORDER BY Simbolo ASC LIMIT 1";
										}
										try {
											datosobt = val.getRow(query);
											request.getSession().setAttribute("idtab", null);
											if (datosobt.size() == 0 || !datosobt.get(0).equals(nombre)) {
												if (datosobt.size() == 0 || !datosobt.get(1).equals(simbolo)) {
													simbolo = simbolo.toUpperCase();
													if (id != null && !id.trim().equals("")) {
														query = "UPDATE Tipo_Productos SET Nombre = '" + nombre
																+ "', Simbolo = '" + simbolo + "' WHERE Id = '" + id + "'";
													} else {
														query = "INSERT INTO Tipo_Productos(Nombre, Simbolo) VALUES ('" + nombre
																+ "', '" + simbolo + "')";
													}
													val.executeQuery(query);
													request.getSession().setAttribute("rettipnombre", null);
													request.getSession().setAttribute("rettipsimb", null);
													if (id != null && !id.trim().equals("")) {
														request.getSession().setAttribute("succViewT", "Success");
														response.sendRedirect("./vista");
													}
													else if (request.getParameter("otro") != null && !request.getParameter("otro").trim().equals("")) {
														request.getSession().setAttribute("succSaveT", "Success");
														if (retrn != null && retrn != "") {
															response.sendRedirect(link + "?return=1");
														}
														else response.sendRedirect(link);
													} else {
														if (retrn != null && retrn != "") {
															request.getSession().setAttribute("succInsertP", "Success");
															request.getSession().setAttribute("returntipo", null);
															response.sendRedirect("../inventario/insertar");
														} else {
															request.getSession().setAttribute("succIndexT", "Success");
															response.sendRedirect("./");
														}
													}
												} else {
													request.getSession().setAttribute("menssavetipo",
															"El símbolo de la clasificación ya existe, ingrese una diferente.");
													if ((retrn != null && !retrn.trim().equals("")) || (id != null && !id.trim().equals(""))) {
														link += "?";
														if(id != null) link += "id=" + id;
														if(retrn != null) {
															if(id != null) link += "&";
															link += "return=1";
														}
													}
													response.sendRedirect(link);
												}
											}
											else {
												request.getSession().setAttribute("menssavetipo",
														"La clasificación ya existe, ingrese una diferente.");
												if ((retrn != null && !retrn.trim().equals("")) || (id != null && !id.trim().equals(""))) {
													link += "?";
													if(id != null) link += "id=" + id;
													if(retrn != null) {
														if(id != null) link += "&";
														link += "return=1";
													}
												}
												response.sendRedirect(link);
											}
										} catch (SQLException e) {
											request.getSession().setAttribute("menssavetipo",
													"Ha ocurrido un error al ejecutar el proceso.");
											if ((retrn != null && !retrn.trim().equals("")) || (id != null && !id.trim().equals(""))) {
												link += "?";
												if(id != null) link += "id=" + id;
												if(retrn != null) {
													if(id != null) link += "&";
													link += "return=1";
												}
											}
											response.sendRedirect(link);
										}
									} else {
										request.getSession().setAttribute("menssavetipo",
												"Uno o varios campos poseen dígitos.");
										if ((retrn != null && !retrn.trim().equals("")) || (id != null && !id.trim().equals(""))) {
											link += "?";
											if(id != null) link += "id=" + id;
											if(retrn != null) {
												if(id != null) link += "&";
												link += "return=1";
											}
										}
										response.sendRedirect(link);
									}
								} else {
									request.getSession().setAttribute("menssavetipo",
											"Uno o varios campos no cumplen el formato correcto.");
									if ((retrn != null && !retrn.trim().equals("")) || (id != null && !id.trim().equals(""))) {
										link += "?";
										if(id != null) link += "id=" + id;
										if(retrn != null) {
											if(id != null) link += "&";
											link += "return=1";
										}
									}
									response.sendRedirect(link);
								}
							} else {
								request.getSession().setAttribute("menssavetipo",
										"No ha ingresado el simbolo de la clasificación.");
								if ((retrn != null && !retrn.trim().equals("")) || (id != null && !id.trim().equals(""))) {
									link += "?";
									if(id != null) link += "id=" + id;
									if(retrn != null) {
										if(id != null) link += "&";
										link += "return=1";
									}
								}
								response.sendRedirect(link);
							}
						} else {
							request.getSession().setAttribute("menssavetipo", "No ha ingresado el nombre.");
							if ((retrn != null && !retrn.trim().equals("")) || (id != null && !id.trim().equals(""))) {
								link += "?";
								if(id != null) link += "id=" + id;
								if(retrn != null) {
									if(id != null) link += "&";
									link += "return=1";
								}
							}
							response.sendRedirect(link);
						}
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
