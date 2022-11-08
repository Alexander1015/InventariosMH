package deleteServlet;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lib.Validacion;

/**
 * Servlet implementation class Guardar_Retiros
 */
@WebServlet("/dashboard/inventario/guardaretiros")
public class Guardar_Retiros extends HttpServlet {

	Validacion val = new Validacion();

	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Guardar_Retiros() {
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
					query = "SELECT RetirarProducto FROM Accesos WHERE IdUsuario = '" + request.getSession().getAttribute("id") + "'";
					ArrayList<String> accesos = val.getRow(query);
					if(accesos.size() == 0) {
						accesos.add(0, "0");
					}
					if(accesos.get(0).equals("0")) {
						response.sendRedirect("../inventario");
					}
					else {
						response.sendRedirect("./retiro.jsp");
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
						query = "SELECT RetirarProducto FROM Accesos WHERE IdUsuario = '" + request.getSession().getAttribute("id") + "'";
						ArrayList<String> accesos = val.getRow(query);
						if(accesos.size() == 0) {
							accesos.add(0, "0");
						}
						if(accesos.get(0).equals("0")) {
							response.sendRedirect("../inventario");
						}
						else {
							String personal = request.getParameter("txtPersonal");
							String yo = request.getParameter("chkyo") != null && (request.getParameter("chkyo").equals("on") || request.getParameter("chkyo").equals("true")) ? "1" : "0";
							String ubicacion = request.getParameter("txtUbicacion");
							String[] productos = request.getParameterValues("txtCantidad");
							String caso = request.getParameter("txtCaso");
							String RF = request.getParameter("txtRF");
							String ref1 = request.getParameter("txtReferencia1");
							String ref2 = request.getParameter("txtReferencia2");
							String ref3 = request.getParameter("txtReferencia3");
							String autoriza = request.getParameter("txtAutorizacion");
							String auto = request.getParameter("chkauto") != null && (request.getParameter("chkauto").equals("on") || request.getParameter("chkauto").equals("true")) ? "1" : "0";
							String comentario = "";
							if (request.getParameter("txtComentario") != null
									&& !request.getParameter("txtComentario").trim().equals("")) {
								comentario = request.getParameter("txtComentario");
							}
							request.getSession().setAttribute("retretpersonal", personal);
							request.getSession().setAttribute("retretubicacion", ubicacion);
							request.getSession().setAttribute("retretautor", autoriza);
							request.getSession().setAttribute("retretcaso", caso);
							request.getSession().setAttribute("retretRF", RF);
							request.getSession().setAttribute("retretref1", ref1);
							request.getSession().setAttribute("retretref2", ref2);
							request.getSession().setAttribute("retretref3", ref3);
							request.getSession().setAttribute("retretcomenting", comentario);
							request.getSession().setAttribute("retretautoyo", auto);
							request.getSession().setAttribute("retretpersoyo", yo);
							if(yo == "1" || (personal != null && !personal.trim().equals(""))) {
								if(ubicacion != null && !ubicacion.trim().equals("")) {
									int cantperson = 0;
									if(yo == "0") cantperson = personal.length();
									else cantperson = 10;
									int cantubic = ubicacion.length();
									if(yo == "1" || (cantperson > 1 && cantperson <= 100 && !val.existNumeros(personal))) {
										if(cantubic > 1 && cantubic <= 100) {
											if(auto == "1" || (autoriza != null && !autoriza.trim().equals(""))) {
												int cantautor = 0;
												if(auto == "0") cantautor = autoriza.length();
												else cantperson = 10;
												if(auto == "1" || (cantautor > 1 && cantautor <= 100 && !val.existNumeros(autoriza))) {
													if(caso != null && !caso.trim().equals("") && val.isNumero(caso) && RF != null && !RF.trim().equals("") && !val.existNumeros(RF) && ref1 != null && !ref1.trim().equals("") && val.isNumero(ref1) && ref2 != null && !ref2.trim().equals("") && val.isNumero(ref2) && ref3 != null && !ref3.trim().equals("") && val.isNumero(ref3) && caso.trim().equals(ref3.trim())) {
														int cantcaso = caso.length();
														int cantRF = RF.length();
														int cantref1 = ref1.length();
														int cantref2 = ref2.length();
														int cantref3 = ref3.length();
														if(cantcaso >= 1 && cantcaso <= 8 && cantRF >= 1 && cantRF <= 6 && cantref1 >= 1 && cantref1 <= 5 && cantref2 >= 1 && cantref2 <= 5 && cantref3 >= 1 && cantref3 <= 8) {
															String referencia =  RF.trim().toUpperCase() + "-" + ref1.trim() + "-" + ref2.trim() + "-" + ref3.trim();
															int cantidad = productos.length;
															if (cantidad > 0) {
																boolean cero = false;
																for(int i = 0; i < cantidad; i++) {
																	if (val.isNumero(productos[i])) {
																		int num = Integer.parseInt(productos[i]);
																		if(num <= 0) cero = true;
																	}
																}
																if(!cero) {
																	query = "SELECT Id FROM Inventarios WHERE IdUsuario = '"
																			+ request.getSession().getAttribute("id")
																			+ "' AND Estado = '0' AND Accion = '3' ORDER BY Id DESC LIMIT 1";
																	ArrayList<String> idInventario = val.getRow(query);
																	int cant = idInventario.size();
																	if (cant > 0) {
																		query = "SELECT A.IdProducto, P.Cantidad FROM Acciones AS A INNER JOIN Productos AS P ON A.IdProducto = P.Id WHERE A.IdInventario = '"
																				+ idInventario.get(0) + "' AND Estado = '1'";
																		ArrayList<ArrayList<String>> productosbd = val.getRows(query);
																		int cantprod = productosbd.size();
																		if (cantprod > 0) {
																			if (cantidad == cantprod) {
																				Date dNow = new Date();
																				SimpleDateFormat Fsdf = new SimpleDateFormat("yyyy-MM-dd");
																				String FHoy = Fsdf.format(dNow);
																				String Fecha = "";
																				for (int i = 0; i <= 9; i++) {
																					if (FHoy.charAt(i) != "-".charAt(0))
																						Fecha += FHoy.charAt(i) + ""; // 20210707
																				}
																				// Verificamos que todas las cantidades resultantes son mayores o iguales a 0
																				boolean verif = false;
																				for (int i = 0; i < cantprod; i++) {
																					if (val.isNumero(productos[i])) {
																						int num = Integer.parseInt(productos[i]);
																						if (num <= 0) {
																							verif = true;
																						}
																						int cantidadbd = Integer.parseInt(productosbd.get(i).get(1));
																						int total = cantidadbd - num;
																						if (total < 0) {
																							verif = true;
																						}
																					} else {
																						verif = true;
																					}
																				}
																				int error = 0;
																				if (!verif) {
																					for (int i = 0; i < cantprod; i++) {
																						int cantidadbd = Integer.parseInt(productosbd.get(i).get(1));
																						int total = cantidadbd - Integer.parseInt(productos[i]);
																						if(error == 0) {
																							if (total >= 0) {
																								query = "UPDATE Productos SET Cantidad = '" + total + "', Actualizacion = '"
																										+ Fecha + "' WHERE Id = '" + productosbd.get(i).get(0)
																										+ "' AND Estado = '1'";
																								val.executeQuery(query);
																								query = "UPDATE Acciones SET Cantidad = '" + cantidadbd
																										+ "', Retirado = '" + productos[i]
																										+ "' WHERE IdInventario = '" + idInventario.get(0)
																										+ "' AND IdProducto = '" + productosbd.get(i).get(0) + "'";
																								val.executeQuery(query);
																								String externo = "";
																								String autori = "";
																								if(yo == "1") {
																									externo = "";
																								}
																								else externo = ", UsuarioExterno = '" + personal + "'";
																								if(auto == "1") {
																									autori = "";
																								}
																								else autori = ", Autorizacion = '" + autoriza + "'";
																								DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
																								String horact = dtf.format(LocalDateTime.now());
																								query = "UPDATE Inventarios SET Estado = '1', Fecha = '" + Fecha+ "', FechaReporte ='" + FHoy + "', Hora = '" + horact + "'"
																										+ externo + ", UbicacionExterna = '" + ubicacion
																										+ "', Caso = '" + caso + "', Referencia = '" + referencia + "', Comentario = '" + comentario + "'" + autori
																										+ " WHERE Id = '" + idInventario.get(0)
																										+ "' AND Estado = '0'";
																								val.executeQuery(query);
																							} else error++;
																						}
																					}
																					if(error == 0) {
																						request.getSession().setAttribute("retretpersonal", null);
																						request.getSession().setAttribute("retretubicacion", null);
																						request.getSession().setAttribute("retretautor", null);
																						request.getSession().setAttribute("retretcaso", null);
																						request.getSession().setAttribute("retretRF", null);
																						request.getSession().setAttribute("retretref1", null);
																						request.getSession().setAttribute("retretref2", null);
																						request.getSession().setAttribute("retretref3", null);
																						request.getSession().setAttribute("retretcomenting", null);
																						request.getSession().setAttribute("retretautoyo", null);
																						request.getSession().setAttribute("retretpersoyo", null);
																						request.getSession().setAttribute("succIndexP", "Success");
																						request.getSession().setAttribute("idreport", idInventario.get(0));
																						request.getSession().setAttribute("tipo", "3");
																						response.sendRedirect("./");
																					}
																					else {
																						request.getSession().setAttribute("mensretinventario",
																								"Uno ó varios datos a guardar no son válidos.");
																						response.sendRedirect("./retiro.jsp");
																					}
																				} else {
																					for (int i = 0; i < cantprod; i++) {
																						int cantidadbd = Integer.parseInt(productosbd.get(i).get(1));
																						query = "UPDATE Acciones SET Cantidad = '" + cantidadbd
																								+ "' WHERE IdInventario = '" + idInventario.get(0)
																								+ "' AND IdProducto = '" + productosbd.get(i).get(0) + "'";
																						val.executeQuery(query);
																					}
																					request.getSession().setAttribute("mensretinventario",
																							"Revise la cantidad a retirar.");
																					response.sendRedirect("./retiro.jsp");
																				}
																			} else {
																				request.getSession().setAttribute("mensretinventario",
																						"Debe rellenar todos los campos de las cantidades a retirar.");
																				response.sendRedirect("./retiro.jsp");
																			}
																		} else {
																			request.getSession().setAttribute("mensretinventario",
																					"Ha ocurrido un error al ejecutar el proceso.");
																			response.sendRedirect("./retiro.jsp");
																		}
																	} else {
																		request.getSession().setAttribute("mensretinventario",
																				"Ha ocurrido un error al ejecutar el proceso.");
																		response.sendRedirect("./retiro.jsp");
																	}
																}
																else {
																	request.getSession().setAttribute("mensretinventario",
																			"No puede retirar solamente accesorios/dispositivos en valor '0'.");
																	response.sendRedirect("./retiro.jsp");
																}
															} else {
																request.getSession().setAttribute("mensretinventario", "Los datos a guardar no son validos.");
																response.sendRedirect("./retiro.jsp");
															}
														}
														else {
															request.getSession().setAttribute("mensretinventario", "Revise la longitud de los datos del caso o de la referencia de este.");
															response.sendRedirect("./retiro.jsp");
														}
													}
													else {
														request.getSession().setAttribute("mensretinventario", "No ha ingresado los datos del caso o referencia de este.");
														response.sendRedirect("./retiro.jsp");
													}
												}
												else {
													request.getSession().setAttribute("mensretinventario", "No ha ingresado el que autorizó correctamente.");
													response.sendRedirect("./retiro.jsp");
												}
											}
											else {
												request.getSession().setAttribute("mensretinventario", "No ha ingresado el que autorizó este movimiento.");
												response.sendRedirect("./retiro.jsp");
											}
										}
										else {
											request.getSession().setAttribute("mensretinventario", "Uno o varios campos no cumplen el formato correcto.");
											response.sendRedirect("./retiro.jsp");
										}
										
									}
									else {
										request.getSession().setAttribute("mensretinventario", "Uno o varios campos no cumplen el formato correcto.");
										response.sendRedirect("./retiro.jsp");
									}
								}
								else {
									request.getSession().setAttribute("mensretinventario", "No ha ingresado la ubicación.");
									response.sendRedirect("./retiro.jsp");
								}
							}
							else {
								request.getSession().setAttribute("mensretinventario", "No ha ingresado el empleado.");
								response.sendRedirect("./retiro.jsp");
							}
						}
					} catch (SQLException e) {
						request.getSession().setAttribute("mensretinventario", "Ha ocurrido un error al ejecutar el proceso.");
						response.sendRedirect("./retiro.jsp");
					}
				}
			} else {
				response.sendRedirect("../desconectar");
			}
		} catch (SQLException | IOException e) {
			e.printStackTrace();
		}
	}

}
