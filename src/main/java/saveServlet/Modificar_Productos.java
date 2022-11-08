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
 * Servlet implementation class Modificar_Productos
 */
@WebServlet("/dashboard/inventario/modificar")
public class Modificar_Productos extends HttpServlet {

	Validacion val = new Validacion();

	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Modificar_Productos() {
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
					query = "SELECT UpdateProducto FROM Accesos WHERE IdUsuario = '" + request.getSession().getAttribute("id") + "'";
					ArrayList<String> accesos = val.getRow(query);
					if(accesos.size() == 0) {
						accesos.add(0, "0");
					}
					if(accesos.get(0).equals("0")) {
						response.sendRedirect("../inventario");
					}
					else {
						String id = request.getParameter("id");
						if (id != null && !id.trim().equals("")) {
							request.getSession().setAttribute("idtab", id);
						} else {
							request.getSession().setAttribute("idtab", null);
						}
						request.getRequestDispatcher("modify.jsp").forward(request, response);
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
					String vista = request.getParameter("vista");
					query = "SELECT CreateProducto, UpdateProducto FROM Accesos WHERE IdUsuario = '" + request.getSession().getAttribute("id") + "'";
					ArrayList<String> accesos = val.getRow(query);
					if(accesos.size() == 0) {
						accesos.add(0, "0");
						accesos.add(1, "0");
					}
					boolean exisacces = true;
					if(accesos.get(0).equals("0") && (vista == null || vista.trim().equals(""))) exisacces = false; //Area de modificar en ingresos
					else if(accesos.get(1).equals("0") && vista != null && !vista.trim().equals("")) exisacces = false; //Area de modificar luego del view
					if (!exisacces) { 
						response.sendRedirect("../inventario");
					}
					else {
						String idproduct = request.getParameter("id");
						try {
							// Variables
							String codigo = request.getParameter("txtCodigo");
							String nombre = request.getParameter("txtNombre");
							String tipo = "";
							if (request.getParameter("cmbTipos") != null && !request.getParameter("cmbTipos").trim().equals("")) {
								tipo = request.getParameter("cmbTipos");
							}
							String ubicacion = request.getParameter("txtUbicacion");
							String cantidad = request.getParameter("txtCantidad");
							String comentario = "";
							String registro = "";
							if (request.getParameter("txtComentario") != null
									&& !request.getParameter("txtComentario").trim().equals("")) {
								comentario = request.getParameter("txtComentario");
							}
							String marca = "-";
							if (request.getParameter("txtMarca") != null
									&& !request.getParameter("txtMarca").trim().equals("")) {
								marca = request.getParameter("txtMarca");
							}
							String modelo = "-";
							if (request.getParameter("txtModelo") != null
									&& !request.getParameter("txtModelo").trim().equals("")) {
								modelo = request.getParameter("txtModelo");
							}
							String serie = "-";
							if (request.getParameter("txtSerie") != null
									&& !request.getParameter("txtSerie").trim().equals("")) {
								serie = request.getParameter("txtSerie");
							}
							String activofijo = "-";
							if (request.getParameter("txtActivoFijo") != null
									&& !request.getParameter("txtActivoFijo").trim().equals("")) {
								activofijo = request.getParameter("txtActivoFijo");
							}
							if (codigo != null && !codigo.equals("")) {
								if (marca.equals("-") || (marca.length() >= 1 && marca.length() <= 100)) {
									if (modelo.equals("-") || (modelo.length() >= 1 && modelo.length() <= 100)) {
										if (serie.equals("-") || (serie.length() >= 1 && serie.length() <= 100)) {
											if (activofijo.equals("-") || (val.isNumero(activofijo) && activofijo.length() >= 14 && activofijo.length() <= 20)) {
												if (nombre != null && !nombre.trim().equals("")) {
													if (ubicacion != null && !ubicacion.trim().equals("")) {
														int cantnom = nombre.length();
														int cantubi = ubicacion.length();
														int cantcant = cantidad.length();
														int cantcod = codigo.length();
														if (cantnom > 1 && cantnom <= 150 && cantubi > 1 && cantubi <= 100 && cantcant > 0
																&& cantcant <= 5 && cantcod >= 8 && cantcod <= 9) {
															if (val.isNumero(cantidad)) {
																int numCantidad = Integer.parseInt(cantidad);
																if (numCantidad > 0) {
																	int cantcoment = comentario.length();
																	if (comentario.trim().equals("") || (cantcoment > 0 && cantcoment <= 500)) {
																		query = "SELECT id FROM Tipo_Productos WHERE Simbolo = '" + tipo
																				+ "' ORDER BY Simbolo DESC LIMIT 1";
																		ArrayList<String> datosobttipo = val.getRow(query);
																		int cantTipos = datosobttipo.size();
																		query = "SELECT Id FROM Productos WHERE Codigo = '" + codigo
																				+ "' ORDER BY Id DESC LIMIT 1";
																		ArrayList<String> verifid = val.getRow(query);
																		int cantverid = verifid.size();
																		if ( cantverid == 0 || (cantverid > 0 && verifid.get(0).equals(idproduct))) {
																			if (cantTipos > 0) {
																				Date dNow = new Date();
																				tipo = datosobttipo.get(0);
																				SimpleDateFormat Fsdf = new SimpleDateFormat("yyyy-MM-dd");
																				String FHoy = Fsdf.format(dNow);
																				String Fecha = "";
																				for (int i = 0; i <= 9; i++) {
																					if (FHoy.charAt(i) != "-".charAt(0))
																						Fecha += FHoy.charAt(i) + ""; // 20210707
																				}
																				if(vista != null && !vista.trim().equals("")) {
																					registro = imprRegistro(idproduct ,codigo, tipo, nombre, cantidad, ubicacion, comentario);
																				}
																				query = "UPDATE Productos SET Codigo = '" + codigo
																						+ "', Tipo = '" + tipo + "', Nombre = '" + nombre
																						+ "', Cantidad = '" + cantidad + "', Ubicacion = '"
																						+ ubicacion + "', Comentario = '" + comentario
																						+ "', Actualizacion = '" + Fecha + "', "
																						+ "Marca = '" + marca + "', Modelo = '" + modelo + "', Serie = '" + serie + "', ActivoFijo = '" + activofijo + "'"
																						+ " WHERE Id = '" + idproduct + "'";
																				val.executeQuery(query);
																				if(vista != null && !vista.trim().equals("")) {
																					//Registro a guardar
																					query = "INSERT INTO Inventarios (IdUsuario, Accion, Estado, Fecha) VALUES ('"
																							+ request.getSession().getAttribute("id")
																							+ "', '5', '1', '" + Fecha + "')";
																					val.executeQuery(query);
																					// Volvemos a comprobar
																					query = "SELECT Id FROM Inventarios WHERE IdUsuario = '"
																							+ request.getSession().getAttribute("id")
																							+ "' AND Estado = '1' AND Accion = '5' ORDER BY Id DESC LIMIT 1";
																					ArrayList<String>  idInventario = val.getRow(query);
																					query = "INSERT INTO Acciones (IdInventario, Descripcion) VALUES ('" + idInventario.get(0)
																					+ "', '" + registro + "')";
																					val.executeQuery(query);
																					request.getSession().setAttribute("retmodcodigo", "");
																					request.getSession().setAttribute("retmodnombre", "");
																					request.getSession().setAttribute("retmodcantidad", "");
																					request.getSession().setAttribute("retmodubicacion", "");
																					request.getSession().setAttribute("retmodcoment", "");
																					request.getSession().setAttribute("retmodmarca", "");
																					request.getSession().setAttribute("retmodmodelo", "");
																					request.getSession().setAttribute("retmodserie", "");
																					request.getSession().setAttribute("retmodactivo", "");
																					request.getSession().setAttribute("succViewP", "Success");
																					response.sendRedirect("./vista");
																				}
																				else {
																					query = "SELECT Id FROM Inventarios WHERE IdUsuario = '"
																							+ request.getSession().getAttribute("id")
																							+ "' AND Estado = '0' ORDER BY Id DESC LIMIT 1";
																					ArrayList<String> idInventario = val.getRow(query);
																					query = "UPDATE Acciones SET Cantidad = '" + cantidad
																							+ "' WHERE IdProducto = '" + idproduct
																							+ "' AND IdInventario = '" + idInventario.get(0) + "'";
																					val.executeQuery(query);
																					query = "UPDATE Inventarios SET Fecha = '" + Fecha
																							+ "' WHERE Id = '" + idInventario.get(0) + "'";
																					val.executeQuery(query);
																					request.getSession().setAttribute("retmodcodigo", null);
																					request.getSession().setAttribute("retmodnombre", null);
																					request.getSession().setAttribute("retmodcantidad", null);
																					request.getSession().setAttribute("retmodubicacion", null);
																					request.getSession().setAttribute("retmodcoment", null);
																					request.getSession().setAttribute("retmodmarca", null);
																					request.getSession().setAttribute("retmodmodelo", null);
																					request.getSession().setAttribute("retmodserie", null);
																					request.getSession().setAttribute("retmodactivo", null);
																					request.getSession().setAttribute("succInsertP", "Success");
																					response.sendRedirect("./insertar");
																				}
																			} else {
																				if (cantTipos == 0)
																					request.getSession().setAttribute("mensinsmodinventario",
																							"Debe seleccionar una clasificación para el accesorio/dispositivo.");
																				else
																					request.getSession().setAttribute("mensinsmodinventario",
																							"La clasificación seleccionada no existe.");
																				if(vista != null && !vista.trim().equals("")) response.sendRedirect("./modificar?id=" + idproduct);
																				else response.sendRedirect("./insertar");
																			}
																		}
																		else {
																			request.getSession().setAttribute("mensinsmodinventario", "El código ingresado ya posee un accesorio/dispositivo atribuido.");
																			if(vista != null && !vista.trim().equals("")) response.sendRedirect("./modificar?id=" + idproduct);
																			else response.sendRedirect("./insertar");
																		}
																	} else {
																		request.getSession().setAttribute("mensinsmodinventario",
																				"Uno o varios campos no cumplen el formato correcto.");
																		if(vista != null && !vista.trim().equals("")) response.sendRedirect("./modificar?id=" + idproduct);
																		else response.sendRedirect("./insertar");
																	}
																} else {
																	request.getSession().setAttribute("mensinsmodinventario",
																			"El valor de cantidad debe ser mayor a 0.");
																	if(vista != null && !vista.trim().equals("")) response.sendRedirect("./modificar?id=" + idproduct);
																	else response.sendRedirect("./insertar");
																}
															} else {
																request.getSession().setAttribute("mensinsmodinventario",
																		"El valor de cantidad debe ser numérico.");
																if(vista != null && !vista.trim().equals("")) response.sendRedirect("./modificar?id=" + idproduct);
																else response.sendRedirect("./insertar");
															}
														} else {
															request.getSession().setAttribute("mensinsmodinventario",
																	"Uno o varios campos no cumplen el formato correcto.");
															if(vista != null && !vista.trim().equals("")) response.sendRedirect("./modificar?id=" + idproduct);
															else response.sendRedirect("./insertar");
														}
													} else {
														request.getSession().setAttribute("mensinsmodinventario",
																"No ha ingresado la ubicación del accesorio/dispositivo.");
														if(vista != null && !vista.trim().equals("")) response.sendRedirect("./modificar?id=" + idproduct);
														else response.sendRedirect("./insertar");
													}
												} else {
													request.getSession().setAttribute("mensinsmodinventario", "No ha ingresado el nombre.");
													if(vista != null && !vista.trim().equals("")) response.sendRedirect("./modificar?id=" + idproduct);
													else response.sendRedirect("./insertar");
												}
											}
											else {
												request.getSession().setAttribute("mensinsmodinventario", "El Activo Fijo ingresado debe ser numerico de entre 14 y 20 caracteres.");
												if(vista != null && !vista.trim().equals("")) response.sendRedirect("./modificar?id=" + idproduct);
												else response.sendRedirect("./insertar");
											}
										}
										else {
											request.getSession().setAttribute("mensinsmodinventario", "La Serie ingresada debe ser numerica de entre 1 y 100 caracteres.");
											if(vista != null && !vista.trim().equals("")) response.sendRedirect("./modificar?id=" + idproduct);
											else response.sendRedirect("./insertar");
										}
									}
									else {
										request.getSession().setAttribute("mensinsmodinventario", "El Modelo ingresado debe ser numerico de entre 1 y 100 caracteres.");
										if(vista != null && !vista.trim().equals("")) response.sendRedirect("./modificar?id=" + idproduct);
										else response.sendRedirect("./insertar");
									}
								}
								else {
									request.getSession().setAttribute("mensinsmodinventario", "La Marca ingresada debe ser numerico de entre 1 y 100 caracteres.");
									if(vista != null && !vista.trim().equals("")) response.sendRedirect("./modificar?id=" + idproduct);
									else response.sendRedirect("./insertar");
								}
							}
							else {
								request.getSession().setAttribute("mensinsmodinventario", "No ha ingresado el código del accesorio/dispositivo.");
								if(vista != null && !vista.trim().equals("")) response.sendRedirect("./modificar?id=" + idproduct);
								else response.sendRedirect("./insertar");
							}
						} catch (SQLException e) {
							request.getSession().setAttribute("mensinsmodinventario", "Ha ocurrido un error al ejecutar el proceso.");
							if(vista != null && !vista.trim().equals("")) response.sendRedirect("./modificar?id=" + idproduct);
							else response.sendRedirect("./insertar");
						}
					}
				}
			} else {
				response.sendRedirect("../desconectar");
			}
		} catch (SQLException | IOException e) {
			response.sendRedirect("../desconectar");
		}
	}
	
	public String imprRegistro(String id, String codigo, String tipo, String nombre, String cantidad, String ubicacion, String comentario) throws SQLException {
		String query = "SELECT Codigo, Tipo, Nombre, Cantidad, Ubicacion, Comentario FROM Productos WHERE Id = '" + id + "' ORDER BY Id LIMIT 1";
		ArrayList<String> datosobt = val.getRow(query);
		String codigobd = datosobt.get(0);
		String tipobd = datosobt.get(1);
		String nombrebd = datosobt.get(2);
		String cantidadbd = datosobt.get(3);
		String ubicacionbd = datosobt.get(4);
		String comentariobd = datosobt.get(5);
		String registro = "El usuario ha modificado la información del siguiente producto:";
		registro += "<ul>";
		if(!codigobd.equals(codigo)) registro += "<li><b>Código:</b><ul><li><b>De -></b> " + codigobd + "</li><li><b>A  -></b> " + codigo + "</li></ul></li>";
		if(!tipobd.equals(tipo)) registro += "<li><b>Clasificación:</b><ul><li><b>De -></b> " + tipobd + "</li><li><b>A  -></b> " + tipo + "</li></ul></li>";
		if(!nombrebd.equals(nombre)) registro += "<li><b>Nombre:</b><ul><li><b>De -></b> " + nombrebd + "</li><li><b>A  -></b> " + nombre + "</li></ul></li>";
		if(!cantidadbd.equals(cantidad)) registro += "<li><b>Cantidad:</b><ul><li><b>De -></b> " + cantidadbd + "</li><li><b>A  -></b> " + cantidad + "</li></ul></li>";
		if(!ubicacionbd.equals(ubicacion)) registro += "<li><b>Ubicación:</b><ul><li><b>De -></b> " + ubicacionbd + "</li><li><b>A  -></b> " + ubicacion + "</li></ul></li>";
		if(!comentariobd.equals(comentario)) registro += "<li><b>Comentario:</b><ul><li><b>De -></b> " + comentariobd + "</li><li><b>A  -></b> " + comentario + "</li></ul></li>";
		registro += "</ul>";
		return registro;
	}
}
