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
 * Servlet implementation class Guardar_Permisos
 */
@WebServlet("/dashboard/usuario/access")
public class Guardar_Permisos extends HttpServlet {
	
	Validacion val = new Validacion();
	
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Guardar_Permisos() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
					query = "SELECT UpdateAcceso FROM Accesos WHERE IdUsuario = '" + request.getSession().getAttribute("id") + "'";
					ArrayList<String> accesos = val.getRow(query);
					if(accesos.size() == 0) {
						accesos.add(0, "0");
					}
					if(accesos.get(0).equals("0")) {
						response.sendRedirect("../");
					}
					else {
						String id = request.getParameter("id");
						String iduser = request.getSession().getAttribute("id").toString();
						if(iduser.equals(id)) {
							response.sendRedirect("./vista");
						}
						else {
							query = "SELECT Id FROM Usuarios WHERE Id = '" + id + "' ORDER BY Id DESC LIMIT 1";
							ArrayList<String> existuser = val.getRow(query);
							if(existuser.size() > 0) {
								if (id != null && id != "") {
									request.getSession().setAttribute("idtab", id);
								} else {
									request.getSession().setAttribute("idtab", null);
								}
								request.getRequestDispatcher("permisos.jsp").forward(request, response);
							}
							else {
								request.getSession().setAttribute("mensacces", "El Usuario seleccionado no existe.");
								response.sendRedirect("./vista");
							}
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

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
					String id = request.getParameter("id");
					String iduser = request.getSession().getAttribute("id").toString();
					if(iduser.equals(id)) {
						response.sendRedirect("./vista");
					}
					else {
						query = "SELECT UpdateAcceso FROM Accesos WHERE IdUsuario = '" + request.getSession().getAttribute("id") + "'";
						ArrayList<String> accesos = val.getRow(query);
						if(accesos.size() == 0) {
							accesos.add(0, "0");
						}
						if(accesos.get(0).equals("0")) {
							response.sendRedirect("../");
						}
						else {
							query = "SELECT Id FROM Usuarios WHERE Id = '" + id + "' ORDER BY Id DESC LIMIT 1";
							ArrayList<String> existuser = val.getRow(query);
							if(existuser.size() > 0) {
								String RP = request.getParameter("chkConsAcc") != null && (request.getParameter("chkConsAcc").equals("on") || request.getParameter("chkConsAcc").equals("true")) ? "1" : "0";
								String AP = request.getParameter("chkAddAcc") != null && (request.getParameter("chkAddAcc").equals("on") || request.getParameter("chkAddAcc").equals("true")) ? "1" : "0";
								String DP = request.getParameter("chkDevAcc") != null && (request.getParameter("chkDevAcc").equals("on") || request.getParameter("chkDevAcc").equals("true")) ? "1" : "0";
								String CP = request.getParameter("chkIngAcc") != null && (request.getParameter("chkIngAcc").equals("on") || request.getParameter("chkIngAcc").equals("true")) ? "1" : "0";
								String UP = request.getParameter("chkModAcc") != null && (request.getParameter("chkModAcc").equals("on") || request.getParameter("chkModAcc").equals("true")) ? "1" : "0";
								String RetP = request.getParameter("chkRetAcc") != null && (request.getParameter("chkRetAcc").equals("on") || request.getParameter("chkRetAcc").equals("true")) ? "1" : "0";
								String RT = request.getParameter("chkConsCla") != null && (request.getParameter("chkConsCla").equals("on") || request.getParameter("chkConsCla").equals("true")) ? "1" : "0";
								String CT = request.getParameter("chkInsCla") != null && (request.getParameter("chkInsCla").equals("on") || request.getParameter("chkInsCla").equals("true")) ? "1" : "0";
								String DT = request.getParameter("chkDelCla") != null && (request.getParameter("chkDelCla").equals("on") || request.getParameter("chkDelCla").equals("true")) ? "1" : "0";
								String UT = request.getParameter("chModCla") != null && (request.getParameter("chModCla").equals("on") || request.getParameter("chModCla").equals("true")) ? "1" : "0";
								String RB = request.getParameter("chkConBit") != null && (request.getParameter("chkConBit").equals("on") || request.getParameter("chkConBit").equals("true")) ? "1" : "0";
								String CR = request.getParameter("chkRepBit") != null && (request.getParameter("chkRepBit").equals("on") || request.getParameter("chkRepBit").equals("true")) ? "1" : "0";
								String RU = request.getParameter("chkConsUse") != null && (request.getParameter("chkConsUse").equals("on") || request.getParameter("chkConsUse").equals("true")) ? "1" : "0";
								String CU = request.getParameter("chkInsUse") != null && (request.getParameter("chkInsUse").equals("on") || request.getParameter("chkInsUse").equals("true")) ? "1" : "0";
								String DU = request.getParameter("chkDelUse") != null && (request.getParameter("chkDelUse").equals("on") || request.getParameter("chkDelUse").equals("true")) ? "1" : "0";
								String UU = request.getParameter("chModUse") != null && (request.getParameter("chModUse").equals("on") || request.getParameter("chModUse").equals("true")) ? "1" : "0";
								String UA = request.getParameter("chAccUse") != null && (request.getParameter("chAccUse").equals("on") || request.getParameter("chAccUse").equals("true")) ? "1" : "0";
								//Verificamos que siempre seleccione el consultar en cualquier segmento
								//Accesorios
								if(RP.equals("0") && (AP.equals("1") || DP.equals("1") || CP.equals("1") || UP.equals("1") || RetP.equals("1"))) {
									request.getSession().setAttribute("mensupdacces", "No puede asignar esos permisos en el manejo de los ACCESORIOS/DISPOSITIVOS sin el \"Consultar\".");
									response.sendRedirect("./access");
								}
								else {
									//Clasificaciones
									if(RT.equals("0") && (CT.equals("1") || DT.equals("1") || UT.equals("1"))) {
										request.getSession().setAttribute("mensupdacces", "No puede asignar esos permisos en el manejo de las CLASIFICACIONES sin el \"Consultar\".");
										response.sendRedirect("./access");
									}
									else {
										//Bitacora
										if(RB.equals("0") && CR.equals("1")) {
											request.getSession().setAttribute("mensupdacces", "No puede asignar esos permisos en el manejo de las BITACORAS sin el \"Consultar\".");
											response.sendRedirect("./access");
										}
										else {
											//Usuarios
											if(RU.equals("0") && (CU.equals("1") || DU.equals("1") || UU.equals("1") || UA.equals("1"))) {
												request.getSession().setAttribute("mensupdacces", "No puede asignar esos permisos en el manejo de los USUARIOS sin el \"Consultar\".");
												response.sendRedirect("./access");
											}
											else {
												query = "SELECT U.Id FROM Usuarios AS U INNER JOIN Accesos AS A ON U.Id = A.IdUsuario "
														+ "WHERE UpdateAcceso = '1' ORDER BY U.Id";
												ArrayList<ArrayList<String>> onlyaccess = val.getRows(query);
												if(onlyaccess.size() == 1 && onlyaccess.get(0).get(0).equals(id) && UA == "0") {
													request.getSession().setAttribute("mensupdacces", "No se puede quitar el acceso a Accesos/Permisos debido a que no existe otro Usuario con acceso a esta área.");
													response.sendRedirect("./access");
												}
												else {
													query = "UPDATE Accesos SET "
															+ "ReadProducto = '" + RP + "', AgregarProducto = '" + AP + "', DevolverProducto = '" + DP + "', CreateProducto = '" + CP + "', UpdateProducto = '" + UP + "', RetirarProducto = '" + RetP + "', "
															+ "ReadTipoProducto = '" + RT + "', CreateTipoProducto = '" + CT + "', DeleteTipoProducto = '" + DT + "', UpdateTipoProducto = '" + UT + "', "
															+ "ReadBitacora = '" + RB +"', CreateReporte = '" + CR + "', "
															+ "ReadUsuario = '" + RU + "', CreateUsuario = '" + CU + "', DeleteUsuario = '" + DU + "', UpdateUsuario = '" + UU + "', UpdateAcceso = '" + UA + "' "
															+ "WHERE IdUsuario = '" + id + "'";
													val.executeQuery(query);
													request.getSession().setAttribute("succViewU", "Success");
													response.sendRedirect("./vista");
												}
											}
										}
									}
								}
							}
							else {
								request.getSession().setAttribute("mensacces", "El Usuario seleccionado no existe.");
								response.sendRedirect("./vista");
							}
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

}
