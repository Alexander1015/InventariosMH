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
 * Servlet implementation class Guardar_Devoluciones
 */
@WebServlet("/dashboard/inventario/guardardevolucion")
public class Guardar_Devoluciones extends HttpServlet {

	Validacion val = new Validacion();

	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Guardar_Devoluciones() {
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
					query = "SELECT DevolverProducto FROM Accesos WHERE IdUsuario = '" + request.getSession().getAttribute("id") + "'";
					ArrayList<String> accesos = val.getRow(query);
					if(accesos.size() == 0) {
						accesos.add(0, "0");
					}
					if(accesos.get(0).equals("0")) {
						response.sendRedirect("../inventario");
					}
					else {
						request.getSession().setAttribute("tabla_productos", null);
						request.getSession().setAttribute("retdevcomenting", null);
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
							String[] productos = request.getParameterValues("txtCantidad");
							String comentario = "";
							if (request.getParameter("txtComentario") != null
									&& !request.getParameter("txtComentario").trim().equals("")) {
								comentario = request.getParameter("txtComentario");
							}
							request.getSession().setAttribute("retdevcomenting", comentario);
							String antigInventario = request.getSession().getAttribute("id_inventario").toString();
							query = "SELECT I.Id, I.UsuarioExterno, I.IdUsuario, (U.Nombres || ' ' || U.Apellidos) AS Usuario FROM Inventarios AS I "
									+ "INNER JOIN Usuarios AS U ON U.Id = I.IdUsuario WHERE I.Id = '" + antigInventario
									+ "' AND I.Estado = '1' AND I.Accion = '3' ORDER BY I.Id DESC LIMIT 1";
							ArrayList<String> idInventario = val.getRow(query);
							int cantidad = productos.length;
							if (cantidad > 0) {
								int cero = 0;
								for(int i = 0; i < cantidad; i++) {
									query = "SELECT A.Retirado, A.Devuelto FROM Acciones AS A INNER JOIN Productos AS P ON A.IdProducto = P.Id WHERE A.IdInventario = '"
											+ idInventario.get(0) + "' AND P.Estado = '1' ORDER BY P.Nombre";
									ArrayList<ArrayList<String>> verificant = val.getRows(query);
									int rest = Integer.parseInt(verificant.get(i).get(0)) - Integer.parseInt(verificant.get(i).get(1));
									if(rest > 0) {
										if (val.isNumero(productos[i])) {
											int num = Integer.parseInt(productos[i]);
											if(num <= 0) cero++;
										}
									}
									else {
										productos[i] = "N";
									}
								}
								if(cero < cantidad) {
									int cant = idInventario.size();
									if (cant > 0) {
										query = "SELECT A.IdProducto, P.Cantidad, A.Retirado, A.Devuelto FROM Acciones AS A INNER JOIN Productos AS P ON A.IdProducto = P.Id WHERE A.IdInventario = '"
												+ idInventario.get(0) + "' AND P.Estado = '1'  ORDER BY P.Nombre";
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
													if(productos[i] != "N") {
														if (val.isNumero(productos[i])) {
															int num = Integer.parseInt(productos[i]);
															if (num < 0) {
																verif = true;
															}
															int cantidadbd = 0;
															if (productosbd.get(i).get(3) != null
																	&& !productosbd.get(i).get(3).trim().equals("")
																	&& !productosbd.get(i).get(3).equals("0")) {
																cantidadbd = Integer.parseInt(productosbd.get(i).get(2)) - Integer.parseInt(productosbd.get(i).get(3));
															} else
																cantidadbd = Integer.parseInt(productosbd.get(i).get(2));
															int total = cantidadbd - num;
															if (total < 0) {
																verif = true;
															}
														} else {
															verif = true;
														}
													}
												}
												int error = 0;
												int valcero = 0;
												if (!verif) {
													ArrayList<String> nwIdInventario = val.getRow(query);
													for (int i = 0; i < cantprod; i++) {
														if(productos[i] != "N") {
															if (Integer.parseInt(productos[i]) > 0) {
																int cantidadbd = Integer.parseInt(productosbd.get(i).get(1));
																int total = cantidadbd + Integer.parseInt(productos[i]);
																if(error == 0) {
																	if (total > 0) {
																		query = "UPDATE Productos SET Cantidad = '" + total
																				+ "', Actualizacion = '" + Fecha + "' WHERE Id = '"
																				+ productosbd.get(i).get(0) + "' AND Estado = '1'";
																		val.executeQuery(query);
																		if(idInventario.get(1) != null && !idInventario.get(1).trim().equals("")) {
																			query = "INSERT INTO Inventarios (IdUsuario, UsuarioExterno, Accion, Estado, Fecha, Comentario) VALUES ('"
																					+ request.getSession().getAttribute("id") + "', '" + idInventario.get(1)
																					+ "', '4', '1', '" + Fecha + "', '" + comentario + "')";
																		}
																		else if(idInventario.get(2).equals(request.getSession().getAttribute("id"))) {
																			query = "INSERT INTO Inventarios (IdUsuario, Accion, Estado, Fecha, Comentario) VALUES ('"
																					+ request.getSession().getAttribute("id") + "', "
																					+ "'4', '1', '" + Fecha + "', '" + comentario + "')";
																		}
																		else {
																			query = "INSERT INTO Inventarios (IdUsuario, UsuarioExterno, Accion, Estado, Fecha, Comentario) VALUES ('"
																					+ request.getSession().getAttribute("id") + "', '" + idInventario.get(3)
																					+ "', '4', '1', '" + Fecha + "', '" + comentario + "')";
																		}
																		val.executeQuery(query);
																		// Verificamos el inventario que acabamos de ingresar
																		query = "SELECT Id FROM Inventarios WHERE IdUsuario = '"
																				+ request.getSession().getAttribute("id")
																				+ "' AND Accion = '4' AND Estado = '1' ORDER BY Id DESC LIMIT 1";
																		nwIdInventario = val.getRow(query);
																		query = "INSERT INTO Acciones (IdInventario, IdProducto, Cantidad, Devuelto) VALUES ('"
																				+ nwIdInventario.get(0) + "', '"
																				+ productosbd.get(i).get(0) + "', '"
																				+ productosbd.get(i).get(1) + "', '" + productos[i]
																				+ "')";
																		val.executeQuery(query);
																		// Dejamos claro que se han devuelto de que lugar
																		query = "SELECT Devuelto FROM Acciones WHERE IdInventario = '"
																				+ idInventario.get(0) + "' AND IdProducto = '"
																				+ productosbd.get(i).get(0) + "' LIMIT 1";
																		ArrayList<String> anteriordev = val.getRow(query);
																		int totdevuelto = Integer.parseInt(productos[i])
																				+ Integer.parseInt(anteriordev.get(0));
																		query = "UPDATE Acciones SET Devuelto = '" + totdevuelto
																				+ "' WHERE IdInventario = '" + idInventario.get(0)
																				+ "' AND IdProducto = '" + productosbd.get(i).get(0)
																				+ "'";
																		val.executeQuery(query);
																		request.getSession().setAttribute("tabla_productos", null);
																		request.getSession().setAttribute("id_inventario", null);
																	} else error++;
																}
															}
														}
														else {
															valcero++;
														}
													}
													if(error == 0 && valcero < cantprod) {
														request.getSession().setAttribute("retdevcomenting", null);
														request.getSession().setAttribute("succIndexP", "Success");
														request.getSession().setAttribute("idreport", nwIdInventario.get(0));
														request.getSession().setAttribute("tipo", "4");
														response.sendRedirect("./");
													}
													else {
														request.getSession().setAttribute("mensdevinventario",
																"Uno ó varios datos a guardar no son válidos.");
														response.sendRedirect("./devolucion.jsp");
													}
												} else {
													request.getSession().setAttribute("mensdevinventario",
															"La cantidad a devolver no concuerda con los datos retirados.");
													response.sendRedirect("./devolucion.jsp");
												}
											} else {
												request.getSession().setAttribute("mensdevinventario",
														"Debe rellenar todos los campos de las cantidades a devolver.");
												response.sendRedirect("./devolucion.jsp");
											}
										} else {
											request.getSession().setAttribute("mensdevinventario",
													"Ha ocurrido un error al ejecutar el proceso.");
											response.sendRedirect("./devolucion.jsp");
										}
									} else {
										request.getSession().setAttribute("mensdevinventario",
												"Ha ocurrido un error al ejecutar el proceso.");
										response.sendRedirect("./devolucion.jsp");
									}
								}
								else {
									request.getSession().setAttribute("mensdevinventario",
											"No puede devolver solamente accesorios/dispositivos en valor '0'.");
									response.sendRedirect("./devolucion.jsp");
								}
							} else {
								request.getSession().setAttribute("mensdevinventario", "Los datos a guardar no son validos.");
								response.sendRedirect("./devolucion.jsp");
							}
						}
					} catch (SQLException e) {
						e.printStackTrace();
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
