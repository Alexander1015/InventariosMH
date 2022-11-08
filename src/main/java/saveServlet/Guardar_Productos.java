package saveServlet;

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
 * Servlet implementation class Guardar_Productos
 */
@WebServlet("/dashboard/inventario/guardar")
public class Guardar_Productos extends HttpServlet {

	Validacion val = new Validacion();

	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Guardar_Productos() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		/* Inicio verificar sesi�n */
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

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		/* Inicio verificar sesi�n */
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
							String idinventario = request.getSession().getAttribute("idInventariodev").toString();
							String autoriza = request.getParameter("txtAutorizacion");
							String auto = request.getParameter("chkauto") != null && (request.getParameter("chkauto").equals("on") || request.getParameter("chkauto").equals("true")) ? "1" : "0";
							String caso = request.getParameter("txtCaso");
							String RF = request.getParameter("txtRF");
							String ref1 = request.getParameter("txtReferencia1");
							String ref2 = request.getParameter("txtReferencia2");
							String ref3 = request.getParameter("txtReferencia3");
							String comentario = "";
							if (request.getParameter("txtComentario") != null
									&& !request.getParameter("txtComentario").trim().equals("")) {
								comentario = request.getParameter("txtComentario");
							}
							request.getSession().setAttribute("retinsautor", autoriza);
							request.getSession().setAttribute("retinscaso", caso);
							request.getSession().setAttribute("retinsRF", RF);
							request.getSession().setAttribute("retinsref1", ref1);
							request.getSession().setAttribute("retinsref2", ref2);
							request.getSession().setAttribute("retinsref3", ref3);
							request.getSession().setAttribute("retinscomenting", comentario);
							request.getSession().setAttribute("retinsyo", auto);
							if(auto == "1" || (autoriza != null && !autoriza.trim().equals(""))) {
								int cantautor = 0;
								if(auto == "0") cantautor = autoriza.length();
								else cantautor = 10;
								if(auto == "1" || (cantautor > 1 && cantautor <= 100 && !val.existNumeros(autoriza))) {
									if(caso != null && !caso.trim().equals("") && val.isNumero(caso) && RF != null && !RF.trim().equals("") && !val.existNumeros(RF) && ref1 != null && !ref1.trim().equals("") && val.isNumero(ref1) && ref2 != null && !ref2.trim().equals("") && val.isNumero(ref2) && ref3 != null && !ref3.trim().equals("") && val.isNumero(ref3) && caso.trim().equals(ref3.trim())) {
										int cantcaso = caso.length();
										int cantRF = RF.length();
										int cantref1 = ref1.length();
										int cantref2 = ref2.length();
										int cantref3 = ref3.length();
										if(cantcaso >= 1 && cantcaso <= 8 && cantRF >= 1 && cantRF <= 6 && cantref1 >= 1 && cantref1 <= 5 && cantref2 >= 1 && cantref2 <= 5 && cantref3 >= 1 && cantref3 <= 8) {
											String referencia =  RF.trim().toUpperCase() + "-" + ref1.trim() + "-" + ref2.trim() + "-" + ref3.trim();
											query = "SELECT A.IdProducto FROM Acciones AS A INNER JOIN Inventarios AS I ON A.IdInventario = I.Id WHERE A.IdInventario = '"
													+ idinventario + "' AND I.Estado = '0' ORDER BY A.IdProducto ASC";
											ArrayList<ArrayList<String>> productos = val.getRows(query);
											int cantidad = productos.size();
											Date dNow = new Date();
											SimpleDateFormat Fsdf = new SimpleDateFormat("yyyy-MM-dd");
											String FHoy = Fsdf.format(dNow);
											String Fecha = "";
											for (int i = 0; i <= 9; i++) {
												if (FHoy.charAt(i) != "-".charAt(0))
													Fecha += FHoy.charAt(i) + ""; // 20210707
											}
											if (cantidad > 0) {
												for (int i = 0; i < cantidad; i++) {
													query = "UPDATE Productos SET Estado = '1' WHERE Id = '" + productos.get(i).get(0)
															+ "' AND Estado = '0'";
													val.executeQuery(query);
												}
												String autori = "";
												if(auto == "1") {
													autori = "";
												}
												else autori = ", Autorizacion = '" + autoriza + "'";
												DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
												String horact = dtf.format(LocalDateTime.now());
												query = "UPDATE Inventarios SET Estado = '1', Fecha = '" + Fecha + "', FechaReporte ='" + FHoy + "', Hora = '" + horact + "'"
														+ ", Caso = '" + caso + "', Referencia = '" + referencia + "', Comentario = '" + comentario + "'" + autori
														+ " WHERE Estado = '0' AND Id ='" + idinventario + "'";
												val.executeQuery(query);
												request.getSession().setAttribute("retinsautor", null);
												request.getSession().setAttribute("retinscaso", null);
												request.getSession().setAttribute("retinsRF", null);
												request.getSession().setAttribute("retinsref1", null);
												request.getSession().setAttribute("retinsref2", null);
												request.getSession().setAttribute("retinsref3", null);
												request.getSession().setAttribute("retinscomenting", null);
												request.getSession().setAttribute("retinsyo", null);
												request.setAttribute("idInventariodev", null);
												request.getSession().setAttribute("succIndexP", "Success");
												request.getSession().setAttribute("idreport", idinventario);
												request.getSession().setAttribute("tipo", "1");
												response.sendRedirect("./");
											} else {
												request.getSession().setAttribute("mensinsmodinventario",
														"Ha ocurrido un error al ejecutar el proceso.");
												response.sendRedirect("./insertar");
											}
										}
										else {
											request.getSession().setAttribute("mensinsmodinventario", "Revise la longitud de los datos del caso o de la referencia de este.");
											response.sendRedirect("./insertar");
										}
									}
									else {
										request.getSession().setAttribute("mensinsmodinventario", "No ha ingresado los datos del caso o referencia de este.");
										response.sendRedirect("./insertar");
									}
								}
								else {
									request.getSession().setAttribute("mensinsmodinventario", "No ha ingresado el que autoriz� correctamente.");
									response.sendRedirect("./insertar");
								}
							}
							else {
								request.getSession().setAttribute("mensinsmodinventario", "No ha ingresado el que autoriz� este movimiento.");
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
