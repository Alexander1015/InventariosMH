package saveServlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lib.Cifrado;
import lib.Validacion;

/**
 * Servlet implementation class Guardar_Usuarios
 */
@WebServlet("/dashboard/usuario/guardar")
public class Guardar_Usuarios extends HttpServlet {

	Validacion val = new Validacion();

	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Guardar_Usuarios() {
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
					request.getSession().setAttribute("returnuser", null);
					String id = request.getParameter("id");
					String iduser = request.getSession().getAttribute("id").toString();
					if(iduser.equals(id)) {
						response.sendRedirect("./vista");
					}
					else {
						String retrn = request.getParameter("return");
						if (id != null && id != "") {
							request.getSession().setAttribute("idtab", id);
						} else {
							request.getSession().setAttribute("idtab", null);
						}
						if (retrn != null && !retrn.trim().equals("")) {
							request.getSession().setAttribute("returnuser", retrn);
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
					String nombre = request.getParameter("txtNombre");
					String apellido = request.getParameter("txtApellido");
					String user = request.getParameter("txtUsuario");
					String password = request.getParameter("txtContra");
					String passwordrepeat = request.getParameter("txtContraRep");
					String id = (String) request.getSession().getAttribute("idtab");
					String retrn = (String) request.getSession().getAttribute("returnuser");
					String iduser = request.getSession().getAttribute("id").toString();
					if (id == null || id.trim().equals("")) {
						request.getSession().setAttribute("retusrnombre", nombre);
						request.getSession().setAttribute("retusrapellido", apellido);
						request.getSession().setAttribute("retusrusuario", user);
					}
					if(id != null && iduser.equals(id)) {
						response.sendRedirect("./vista");
					}
					else {
						if (nombre != null && !nombre.trim().equals("")) {
							if (apellido != null && !apellido.trim().equals("")) {
								if (user != null && !user.trim().equals("")) {
									int cantnom = nombre.length();
									int cantapel = apellido.length();
									int cantuser = user.length();
									if (cantnom > 1 && cantnom <= 250 && cantapel > 1 && cantapel <= 250 && cantuser >= 5 && cantuser <= 100) {
										boolean verif = false;
										if (id != null && !id.trim().equals("")) {	//Modificar
											query = "SELECT Id FROM Usuarios WHERE Usuario = '" + user + "' ORDER BY Id DESC";
											ArrayList<ArrayList<String>> existe = val.getRows(query);
											int cantidad = existe.size();
											boolean existuser = false;
											for(int i = 0; i < cantidad; i++) {
												if(!existe.get(i).get(0).equals(id)) existuser = true;
											}
											if(!existuser) {
												if (password != null && !password.trim().equals("")) { //Actualiza la contraseña
													if(val.existNumeros(password) && val.existLetras(password)) {
														if (passwordrepeat != null && !passwordrepeat.trim().equals("") && val.existNumeros(passwordrepeat) && val.existLetras(passwordrepeat)) {
															int cantpass = password.length();
															if(cantpass >= 14 && cantpass <= 50) {
																if(password.equals(passwordrepeat)) {
																	verif = true;
																}
																else {
																	request.getSession().setAttribute("menssaveuser",
																			"Las contraseñas no coinciden.");
																	if (retrn != null && retrn != "" && id != null && id != "") {
																		response.sendRedirect("./guardar?id=" + id + "&return=1");
																	}
																	else response.sendRedirect("./guardar");
																}
															}
															else {
																request.getSession().setAttribute("menssaveuser",
																		"La contraseña debe tener una longitud entre 14 y 50 carácteres.");
																if (retrn != null && retrn != "" && id != null && id != "") {
																	response.sendRedirect("./guardar?id=" + id + "&return=1");
																}
																else response.sendRedirect("./guardar");
															}
														}
														else {
															request.getSession().setAttribute("menssaveuser", "Debe repetir la contraseña.");
															if (retrn != null && retrn != "" && id != null && id != "") {
																response.sendRedirect("./guardar?id=" + id + "&return=1");
															}
															else response.sendRedirect("./guardar");
														}
													}
													else {
														if (passwordrepeat != null && !passwordrepeat.trim().equals("")) {
															request.getSession().setAttribute("menssaveuser", "La contraseña debe contener Letras y Números, además tener una longitud entre 14 y 50 carácteres.");
															if (retrn != null && retrn != "" && id != null && id != "") {
																response.sendRedirect("./guardar?id=" + id + "&return=1");
															}
															else response.sendRedirect("./guardar");
														}
														else verif = true;
													}
												}
												else {//No actualiza la contraseña
													if (passwordrepeat != null && !passwordrepeat.trim().equals("")) {
														request.getSession().setAttribute("menssaveuser", "No ha ingresado la contraseña.");
														if (retrn != null && retrn != "" && id != null && id != "") {
															response.sendRedirect("./guardar?id=" + id + "&return=1");
														}
														else response.sendRedirect("./guardar");
													}
													else verif = true;
												}
											}
											else {
												request.getSession().setAttribute("menssaveuser", "El usuario ingresado ya esta en uso.");
												if (retrn != null && retrn != "" && id != null && id != "") {
													response.sendRedirect("./guardar?id=" + id + "&return=1");
												}
												else response.sendRedirect("./guardar");
											}
										}
										else { //Guardar
											query = "SELECT Id FROM Usuarios WHERE Usuario = '" + user + "' ORDER BY Id DESC LIMIT 1";
											ArrayList<String> existe = val.getRow(query);
											int cantidad = existe.size();
											if(cantidad == 0) {
												if (password != null && !password.trim().equals("") && val.existNumeros(password) && val.existLetras(password)) { //Actualiza la contraseña
													if (passwordrepeat != null && !passwordrepeat.trim().equals("") && val.existNumeros(passwordrepeat) && val.existLetras(passwordrepeat)) {
														int cantpass = password.length();
														if(cantpass >= 14 && cantpass <= 50) {
															if(password.equals(passwordrepeat)) {
																verif = true;
															}
															else {
																request.getSession().setAttribute("menssaveuser",
																		"Las contraseñas no coinciden.");
																if (retrn != null && retrn != "" && id != null && id != "") {
																	response.sendRedirect("./guardar?id=" + id + "&return=1");
																}
																else response.sendRedirect("./guardar");
															}
														}
														else {
															request.getSession().setAttribute("menssaveuser",
																	"La contraseña debe tener una longitud entre 14 y 50 caracteres.");
															if (retrn != null && retrn != "" && id != null && id != "") {
																response.sendRedirect("./guardar?id=" + id + "&return=1");
															}
															else response.sendRedirect("./guardar");
														}
													}
													else {
														request.getSession().setAttribute("menssaveuser", "Debe repetir la contraseña.");
														if (retrn != null && retrn != "" && id != null && id != "") {
															response.sendRedirect("./guardar?id=" + id + "&return=1");
														}
														else response.sendRedirect("./guardar");
													}
												}
												else {
													request.getSession().setAttribute("menssaveuser", "Debe ingresar una contraseña con el formato valido.");
													if (retrn != null && retrn != "" && id != null && id != "") {
														response.sendRedirect("./guardar?id=" + id + "&return=1");
													}
													else response.sendRedirect("./guardar");
												}
											}
											else {
												request.getSession().setAttribute("menssaveuser", "El usuario ingresado ya existe.");
												if (retrn != null && retrn != "" && id != null && id != "") {
													response.sendRedirect("./guardar?id=" + id + "&return=1");
												}
												else response.sendRedirect("./guardar");
											}
										}
										if(verif) {
											request.getSession().setAttribute("idtab", null);
											String llave = "";
											String secure = "";
											if(password != null && !password.trim().equals("")) {
											    // Genera la llave.
											    llave = Cifrado.getSalt(30);
											    // Proteje la contraseña del usuario
											    secure = Cifrado.generateSecurePassword(password, llave);
											}
											if (id != null && !id.trim().equals("")) {
												query = "UPDATE Usuarios SET Nombres = '" + nombre + "', Apellidos = '" + apellido
														+ "', Usuario = '" + user + "'";
												if(password != null && !password.trim().equals("")) {
													query += ", Password = '" + secure + "', Key = '" + llave + "', Defecto = '0'";
												}
												query += " WHERE Id = '" + id + "'";
											} else {
												query = "INSERT INTO Usuarios (Nombres, Apellidos, Usuario, Password, Key, Defecto) VALUES ('" + nombre
														+ "', '" + apellido + "', '" + user + "', '" + secure + "', '" + llave + "', '0')";
											}
											val.executeQuery(query);
											if (id == null || id.trim().equals("")) {
												query = "SELECT Id FROM Usuarios WHERE Nombres = '" + nombre+ "' AND Apellidos = '" + apellido + "' AND Usuario = '" + user + "' AND Defecto = '0' ORDER BY Id LIMIT 1";
												ArrayList<String> userbd = val.getRow(query);
												query = "INSERT INTO Accesos (IdUsuario) VALUES ('" + userbd.get(0) + "')";
												val.executeQuery(query);
											}
											request.getSession().setAttribute("retusrnombre", null);
											request.getSession().setAttribute("retusrapellido", null);
											request.getSession().setAttribute("retusrusuario", null);
											if (request.getParameter("otro") != null && !request.getParameter("otro").trim().equals("") && (id == null || id == "")) {
												request.getSession().setAttribute("succSaveU", "Success");
												response.sendRedirect("./guardar");
											} else {
												if (retrn != null && retrn != "") {
													request.getSession().setAttribute("returnuser", null);
													request.getSession().setAttribute("succViewU", "Success");
													response.sendRedirect("../usuario/vista");
												} else {
													request.getSession().setAttribute("succIndexU", "Success");
													response.sendRedirect("./");
												}
											}
										}
									} else {
										request.getSession().setAttribute("menssaveuser",
												"Revise la cantidad de caracteres del Nombre, Apellido ó Usuario.");
										if (retrn != null && retrn != "" && id != null && id != "") {
											response.sendRedirect("./guardar?id=" + id + "&return=1");
										}
										else response.sendRedirect("./guardar");
									}
								}
								else {
									request.getSession().setAttribute("menssaveuser", "No ha ingresado el usuario del empleado.");
									if (retrn != null && retrn != "" && id != null && id != "") {
										response.sendRedirect("./guardar?id=" + id + "&return=1");
									}
									else response.sendRedirect("./guardar");
								}
							}
							else {
								request.getSession().setAttribute("menssaveuser", "No ha ingresado los apellidos del empleado.");
								if (retrn != null && retrn != "" && id != null && id != "") {
									response.sendRedirect("./guardar?id=" + id + "&return=1");
								}
								else response.sendRedirect("./guardar");
							}
						}
						else {
							request.getSession().setAttribute("menssaveuser", "No ha ingresado los nombres del empleado.");
							if (retrn != null && retrn != "" && id != null && id != "") {
								response.sendRedirect("./guardar?id=" + id + "&return=1");
							}
							else response.sendRedirect("./guardar");
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