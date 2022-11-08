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
 * Servlet implementation class Insertar_Productos
 */
@WebServlet("/dashboard/inventario/insertar")
public class Insertar_Productos extends HttpServlet {

	Validacion val = new Validacion();

	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Insertar_Productos() {
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
					query = "SELECT CreateProducto FROM Accesos WHERE IdUsuario = '" + request.getSession().getAttribute("id") + "'";
					ArrayList<String> accesos = val.getRow(query);
					if(accesos.size() == 0) {
						accesos.add(0, "0");
					}
					if(accesos.get(0).equals("0")) {
						response.sendRedirect("../inventario");
					}
					else {
						request.getRequestDispatcher("insert.jsp").forward(request, response);
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
						query = "SELECT CreateProducto FROM Accesos WHERE IdUsuario = '" + request.getSession().getAttribute("id") + "'";
						ArrayList<String> accesos = val.getRow(query);
						if(accesos.size() == 0) {
							accesos.add(0, "0");
						}
						if(accesos.get(0).equals("0")) {
							response.sendRedirect("../inventario");
						}
						else {
							// Variables
							String nombre = request.getParameter("txtNombre");
							String tipo = "";
							if (request.getParameter("cmbTipos") != null && !request.getParameter("cmbTipos").trim().equals("")) {
								tipo = request.getParameter("cmbTipos");
							}
							String ubicacion = request.getParameter("txtUbicacion");
							String cantidad = request.getParameter("txtCantidad");
							String comentario = "";
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
							request.getSession().setAttribute("retinsnombre", nombre);
							request.getSession().setAttribute("retinscantidad", cantidad);
							request.getSession().setAttribute("retinsubicacion", ubicacion);
							request.getSession().setAttribute("retinscoment", comentario.equals("-") ? "" : comentario);
							request.getSession().setAttribute("retinsmarca", marca.equals("-") ? "" : marca);
							request.getSession().setAttribute("retinsmodelo", modelo.equals("-") ? "" : modelo);
							request.getSession().setAttribute("retinsserie", serie.equals("-") ? "" : serie);
							request.getSession().setAttribute("retinsactivo", activofijo.equals("-") ? "" : activofijo);
							request.getSession().setAttribute("retinstipo", tipo);
							if (activofijo.equals("-") || (val.isNumero(activofijo) && activofijo.length() >= 14 && activofijo.length() <= 20)) {
								if (nombre != null && !nombre.trim().equals("")) {
									if (ubicacion != null && !ubicacion.trim().equals("")) {
										int cantnom = nombre.length();
										int cantubi = ubicacion.length();
										int cantcant = cantidad.length();
										if (cantnom > 1 && cantnom <= 150 && cantubi > 1 && cantubi <= 100 && cantcant > 0
												&& cantcant <= 5) {
											if (val.isNumero(cantidad)) {
												int numCantidad = Integer.parseInt(cantidad);
												if (numCantidad > 0) {
													int cantcoment = comentario.length();
													if (comentario.trim().equals("") || cantcoment > 0 || cantcoment <= 500) {
														query = "SELECT id FROM Tipo_Productos WHERE Simbolo = '" + tipo
																+ "' ORDER BY Simbolo DESC LIMIT 1";
														ArrayList<String> datosobttipo = val.getRow(query);
														int cantTipos = datosobttipo.size();
														if (cantTipos > 0) {
															// Para arreglar la fecha
															// Inicio para crear el ID
															Date dNow = new Date();
															SimpleDateFormat anioid = new SimpleDateFormat("yy");
															String idProd = tipo + anioid.format(dNow); // Agregamos la
																										// fecha, Ej: AA21
															query = "SELECT Codigo FROM Productos WHERE Codigo LIKE '"
																	+ idProd + "%' ORDER BY Codigo DESC LIMIT 1";
															datosobt = val.getRow(query);
															int existid = datosobt.size();
															if (existid > 0) {
																String nwID = datosobt.get(0);
																nwID = nwID.substring(nwID.length() - 4, nwID.length()); // Obtenemos los ultimos 4 digitos
																int num = Integer.parseInt(nwID);
																num++;
																String conj = String.valueOf(num);
																int digitos = conj.length();
																String conj0 = "";
																for (int i = digitos; i < 4; i++) {
																	conj0 += "0";
																}
																conj = conj0 + conj;
																idProd += conj;
		
															} else {
																idProd += "0001";
															}
															tipo = datosobttipo.get(0);
															// Fin para crear el ID
															SimpleDateFormat Fsdf = new SimpleDateFormat("yyyy-MM-dd");
															String FHoy = Fsdf.format(dNow);
															String Fecha = "";
															for (int i = 0; i <= 9; i++) {
																if (FHoy.charAt(i) != "-".charAt(0))
																	Fecha += FHoy.charAt(i) + ""; // 20210707
															}
															// Verificamos el inventario
															query = "SELECT Id FROM Inventarios WHERE IdUsuario = '"
																	+ request.getSession().getAttribute("id")
																	+ "' AND Estado = '0' AND Accion = '1' ORDER BY Id DESC LIMIT 1";
															ArrayList<String> idInventario = val.getRow(query);
															int existId = idInventario.size();
															if (existId == 0) {
																query = "INSERT INTO Inventarios (IdUsuario, Accion, Estado, Fecha) VALUES ('"
																		+ request.getSession().getAttribute("id")
																		+ "', '1', '0', '" + Fecha + "')";
																val.executeQuery(query);
																// Volvemos a comprobar
																query = "SELECT Id FROM Inventarios WHERE IdUsuario = '"
																		+ request.getSession().getAttribute("id")
																		+ "' AND Estado = '0' AND Accion = '1' ORDER BY Id DESC LIMIT 1";
																idInventario = val.getRow(query);
															} else {
																query = "UPDATE Inventarios SET Fecha = '" + Fecha
																		+ "' WHERE Id = '" + idInventario.get(0) + "'";
																val.executeQuery(query);
															}
															// Guardamos en productos
															query = "INSERT INTO Productos (Codigo, Tipo, Nombre, Ubicacion, Cantidad, Fecha, Comentario, Estado, Marca, Modelo, Serie, ActivoFijo) VALUES ('"
																	+ idProd + "', '" + tipo + "', '" + nombre + "', '"
																	+ ubicacion + "', '" + cantidad + "', '" + Fecha
																	+ "', '" + comentario + "', '0'"
																	+ ", '" + marca + "', '" + modelo + "', '" + serie + "', '" + activofijo + "')";
															val.executeQuery(query);
															// Guardamos en acciones
															query = "SELECT Id FROM Productos WHERE Codigo = '" + idProd
																	+ "' AND Estado = '0'";
															ArrayList<String> idProducto = val.getRow(query);
															query = "INSERT INTO Acciones (IdInventario, IdProducto, Cantidad) VALUES ('"
																	+ idInventario.get(0) + "', '" + idProducto.get(0)
																	+ "', '" + cantidad + "')";
															val.executeQuery(query);
															//
															request.getSession().setAttribute("retinsnombre", null);
															request.getSession().setAttribute("retinscantidad", null);
															request.getSession().setAttribute("retinsubicacion", null);
															request.getSession().setAttribute("retinscoment", null);
															request.getSession().setAttribute("retinsmarca", null);
															request.getSession().setAttribute("retinsmodelo", null);
															request.getSession().setAttribute("retinsserie", null);
															request.getSession().setAttribute("retinsactivo", null);
															request.getSession().setAttribute("retinstipo", null);
															//
															request.getSession().setAttribute("succInsertP", "Success");
															response.sendRedirect("./insertar");
														} else {
															if (cantTipos == 0)
																request.getSession().setAttribute("mensinsmodinventario",
																		"Debe seleccionar una clasificación para el accesorio/dispositivo.");
															else
																request.getSession().setAttribute("mensinsmodinventario",
																		"La clasificación seleccionada no existe.");
															response.sendRedirect("./insertar");
														}
													} else {
														request.getSession().setAttribute("mensinsmodinventario",
																"El comentario debe de tener un máximo de 500 carácteres.");
														response.sendRedirect("./insertar");
													}
												} else {
													request.getSession().setAttribute("mensinsmodinventario",
															"El valor de cantidad debe ser mayor a 0.");
													response.sendRedirect("./insertar");
												}
											} else {
												request.getSession().setAttribute("mensinsmodinventario",
														"El valor de cantidad debe ser numérico.");
												response.sendRedirect("./insertar");
											}
										} else {
											if (cantnom <= 1 || cantnom > 150) 
												request.getSession().setAttribute("mensinsmodinventario",
														"El nombre debe tener mínimo 2 y máximo 150 caracteres.");
											else if (cantubi <= 1 || cantubi > 100) {
												request.getSession().setAttribute("mensinsmodinventario",
														"La ubicación debe tener mínimo 2 y máximo 100 caracteres.");
											}
											else if (cantcant <= 0 || cantcant > 6) {
												request.getSession().setAttribute("mensinsmodinventario",
														"El dígito ingresado no es válido");
											}
											else {
												request.getSession().setAttribute("mensinsmodinventario",
														"Uno o varios campos no cumplen el formato correcto.");
											}
											response.sendRedirect("./insertar");
										}
									} else {
										request.getSession().setAttribute("mensinsmodinventario",
												"No ha ingresado la ubicación del accesorio/dispositivo.");
										response.sendRedirect("./insertar");
									}
								} else {
									request.getSession().setAttribute("mensinsmodinventario", "No ha ingresado el nombre.");
									response.sendRedirect("./insertar");
								}
							}
							else {
								request.getSession().setAttribute("mensinsmodinventario", "El Activo Fijo ingresado debe ser númerico de entre 14 y 20 caracteres.");
								response.sendRedirect("./insertar");
							}
						}
					} catch (SQLException e) {
						request.getSession().setAttribute("mensinsmodinventario", "Ha ocurrido un error al ejecutar el proceso.");
						response.sendRedirect("./insertar");
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
