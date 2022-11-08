package mainServlet;

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
 * Servlet implementation class Perfil
 */
@WebServlet("/dashboard/perfil/modificar")
public class Perfil extends HttpServlet {
	Validacion val = new Validacion();
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Perfil() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		/* Inicio verificar sesión */
		String query = "SELECT Nombres, Apellidos FROM Usuarios WHERE Id = '" + request.getSession().getAttribute("id")
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
				} else {
					response.sendRedirect("./");
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
		String query = "SELECT Nombres, Apellidos FROM Usuarios WHERE Id = '" + request.getSession().getAttribute("id")
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
				else {
					String nombre = request.getParameter("txtNombre");
					String apellido = request.getParameter("txtApellido");
					String user = request.getParameter("txtUsuario");
					String passwordant = request.getParameter("txtContraAnt");
					String password = request.getParameter("txtContra");
					String passwordrepeat = request.getParameter("txtContraRep");
					String tipo = request.getParameter("tipo");
					if (tipo != null && !tipo.equals("")) {
						if (tipo.equals("1") || tipo.equals("2") || tipo.equals("3")) {
							if (nombre != null && !nombre.trim().equals("")) {
								if (apellido != null && !apellido.trim().equals("")) {
									if (user != null && !user.trim().equals("")) {
										int cantnom = nombre.length();
										int cantapel = apellido.length();
										int cantuser = user.length();
										if (cantnom > 1 && cantnom <= 250 && cantapel > 1 && cantapel <= 250 && cantuser >= 5 && cantuser <= 100) {
											boolean verif = false;
											query = "SELECT Id FROM Usuarios WHERE Usuario = '" + user + "' ORDER BY Id DESC";
											ArrayList<ArrayList<String>> existe = val.getRows(query);
											int cantidad = existe.size();
											boolean existuser = false;
											for(int i = 0; i < cantidad; i++) {
												if(!existe.get(i).get(0).equals(request.getSession().getAttribute("id"))) existuser = true;
											}
											if(!existuser) {
												if(passwordant != null && !passwordant.trim().equals("")) {
													query = "SELECT Id, Password, Key, Defecto FROM Usuarios WHERE Usuario = '" + user + "' ORDER BY Id DESC LIMIT 1";
													ArrayList<String> usuariobd = val.getRow(query);
												    boolean passwordMatch = Cifrado.verifyUserPassword(passwordant, usuariobd.get(1), usuariobd.get(2));
													if(passwordMatch) {
														if (password != null && !password.trim().equals("")) { //Actualiza la contraseña
															if(val.existNumeros(password) && val.existLetras(password)) {
																if (passwordrepeat != null && !passwordrepeat.trim().equals("") && val.existNumeros(passwordrepeat) && val.existLetras(passwordrepeat)) {
																	int cantpass = password.length();
																	if(cantpass >= 14 && cantpass <= 50) {
																		if(password.equals(passwordrepeat)) {
																			verif = true;
																		}
																		else {
																			request.getSession().setAttribute("mensperfil",
																					"Las nuevas contraseñas no coinciden.");
																			response.sendRedirect("../perfil");
																		}
																	}
																	else {
																		request.getSession().setAttribute("mensperfil",
																				"La contraseña debe tener una longitud entre 14 y 50 carácteres.");
																		response.sendRedirect("../perfil");
																	}
																}
																else {
																	request.getSession().setAttribute("mensperfil", "Debe repetir la contraseña correctamente.");
																	response.sendRedirect("../perfil");
																}
															}
															else {
																request.getSession().setAttribute("mensperfil", "La contraseña debe contener Letras y Números, además tener una longitud entre 14 y 50 carácteres..");
																response.sendRedirect("../perfil");
															}
														}
														else {
															request.getSession().setAttribute("mensperfil", "Debe ingresar la nueva contraseña.");
															response.sendRedirect("../perfil");
														}
													}
													else {
														request.getSession().setAttribute("mensperfil", "La contraseña actual es incorrecta.");
														response.sendRedirect("../perfil");
													}
												}
												else {
													if ((password != null && !password.trim().equals("")) || (passwordrepeat != null && !passwordrepeat.trim().equals(""))) { 
														request.getSession().setAttribute("mensperfil", "Debe ingresar la contraseña actual.");
														response.sendRedirect("../perfil");
													}
													else {
														query = "SELECT Defecto FROM Usuarios WHERE Usuario = '" + user + "' ORDER BY Id DESC LIMIT 1";
														ArrayList<String> usuariobd = val.getRow(query);
														if(usuariobd.size() > 0 && usuariobd.get(0).equals("0")) {
															request.getSession().setAttribute("mensperfil", "Debe actualizar la contraseña administrada.");
															response.sendRedirect("../perfil");
														}
														else verif = true;
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
													String id = (String) request.getSession().getAttribute("id");
													if (tipo.equals("1")) {
														query = "UPDATE Usuarios SET Nombres = '" + nombre + "', Apellidos = '" + apellido
																+ "', Usuario = '" + user + "' WHERE Id = '" + id + "'";
														val.executeQuery(query);
														request.getSession().setAttribute("succPassw", "Success");
														response.sendRedirect("../desconectar");
													}
													else {
														if (!secure.trim().equals("") && !llave.trim().equals("")) {
															if (tipo.equals("2")) {
																query = "UPDATE Usuarios SET Password = '" + secure + "', Key = '" + llave + "', Defecto = '1' WHERE Id = '" + id + "'";
																val.executeQuery(query);
																request.getSession().setAttribute("succPassw", "Success");
																response.sendRedirect("../desconectar");
															}
															else if (tipo.equals("3")) {
																query = "UPDATE Usuarios SET Nombres = '" + nombre + "', Apellidos = '" + apellido
																		+ "', Usuario = '" + user + "'";
																if(password != null && !password.trim().equals("")) {
																	query += ", Password = '" + secure + "', Key = '" + llave + "', Defecto = '1'";
																}
																query += " WHERE Id = '" + id + "'";
																val.executeQuery(query);
																request.getSession().setAttribute("succPassw", "Success");
																response.sendRedirect("../desconectar");
															}
															else {
																request.getSession().setAttribute("mensperfil", "No ha declarado la función con la que desea proceder.");
																response.sendRedirect("../perfil");
															}
														}
														else {
															request.getSession().setAttribute("mensperfil", "No todas las contraseñas solicitadas han sido ingresadas.");
															response.sendRedirect("../perfil");
														}
													}
												}
											}
											else {
												request.getSession().setAttribute("mensperfil", "El usuario ingresado ya esta en uso.");
												response.sendRedirect("../perfil");
											}
										}
										else {
											request.getSession().setAttribute("mensperfil", "Uno o varios campos ingresados no son validos.");
											response.sendRedirect("../perfil");
										}
									}
									else {
										request.getSession().setAttribute("mensperfil", "No ha ingresado el usuario.");
										response.sendRedirect("../perfil");
									}
								}
								else {
									request.getSession().setAttribute("mensperfil", "No ha ingresado los apellidos.");
									response.sendRedirect("../perfil");
								}
							}
							else {
								request.getSession().setAttribute("mensperfil", "No ha ingresado los nombres.");
								response.sendRedirect("../perfil");
							}
						}
						else {
							request.getSession().setAttribute("mensperfil", "No ha declarado correctamente la función con la que desea proceder.");
							response.sendRedirect("../perfil");
						}
					}
					else {
						request.getSession().setAttribute("mensperfil", "No ha declarado la función con la que desea proceder.");
						response.sendRedirect("../perfil");
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
