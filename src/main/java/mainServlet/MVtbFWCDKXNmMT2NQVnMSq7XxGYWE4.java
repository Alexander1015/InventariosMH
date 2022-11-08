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
 * Servlet implementation class Guardar_mVtbFWCDKXNmMT2NQVnMSq7XxGYWE4
 */
@WebServlet("/dashboard/usuario/pvmaGekrkAV7cHfm966xJPYZ8cJPjY/mVtbFWCDKXNmMT2NQVnMSq7XxGYWE4")
public class MVtbFWCDKXNmMT2NQVnMSq7XxGYWE4 extends HttpServlet {
	
	Validacion val = new Validacion();
	
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MVtbFWCDKXNmMT2NQVnMSq7XxGYWE4() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			if(request.getSession().getAttribute("id") == null) {
				String query = "SELECT COUNT(Id) AS Cantidad FROM Usuarios";
				ArrayList<String> cantidadUser;
				cantidadUser = val.getRow(query);
				if(cantidadUser.size() == 0 || !cantidadUser.get(0).equals("0")) {
					response.sendRedirect("../../../dashboard/desconectar");
				}
				else {
					response.sendRedirect("../../../dashboard/desconectar");
				}
			}
			else {
				response.sendRedirect("./WCERH3gPpUFUmm8y7CN6vXL4XxDc7F.jsp");
			}
		} catch (SQLException e) {
			response.sendRedirect("../../../dashboard/desconectar");
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			if(request.getSession().getAttribute("id") == null) {
				String query = "SELECT COUNT(Id) AS Cantidad FROM Usuarios";
				ArrayList<String> cantidadUser;
				cantidadUser = val.getRow(query);
				if(cantidadUser.size() == 0 || !cantidadUser.get(0).equals("0")) {
					response.sendRedirect("../../../dashboard/desconectar");
				}
				else {
					//
					String nombre = request.getParameter("txtNombre");
					String apellido = request.getParameter("txtApellido");
					String user = request.getParameter("txtUsuario");
					String password = request.getParameter("txtContra");
					String passwordrepeat = request.getParameter("txtContraRep");
					if (nombre != null && !nombre.trim().equals("")) {
						if (apellido != null && !apellido.trim().equals("")) {
							if (user != null && !user.trim().equals("")) {
								int cantnom = nombre.length();
								int cantapel = apellido.length();
								int cantuser = user.length();
								if (cantnom > 1 && cantnom <= 250 && cantapel > 1 && cantapel <= 250 && cantuser >= 5 && cantuser <= 100) {
									boolean verif = false;
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
														response.sendRedirect("./WCERH3gPpUFUmm8y7CN6vXL4XxDc7F.jsp");
													}
												}
												else {
													request.getSession().setAttribute("menssaveuser",
															"La contraseña debe tener una longitud entre 14 y 50 caracteres.");
													response.sendRedirect("./WCERH3gPpUFUmm8y7CN6vXL4XxDc7F.jsp");;
												}
											}
											else {
												request.getSession().setAttribute("menssaveuser", "Debe repetir la contraseña.");
												response.sendRedirect("./WCERH3gPpUFUmm8y7CN6vXL4XxDc7F.jsp");
											}
										}
										else {
											request.getSession().setAttribute("menssaveuser", "Debe ingresar una contraseña con el formato valido.");
											response.sendRedirect("./WCERH3gPpUFUmm8y7CN6vXL4XxDc7F.jsp");
										}
									}
									else {
										request.getSession().setAttribute("menssaveuser", "El usuario ingresado ya existe.");
										response.sendRedirect("./WCERH3gPpUFUmm8y7CN6vXL4XxDc7F.jsp");
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
										query = "INSERT INTO Usuarios (Nombres, Apellidos, Usuario, Password, Key, Defecto) VALUES ('" + nombre
													+ "', '" + apellido + "', '" + user + "', '" + secure + "', '" + llave + "', '1')";
										val.executeQuery(query);
										query = "SELECT Id FROM Usuarios WHERE Nombres = '" + nombre + "' AND Apellidos = '" + apellido + "' AND Usuario = '" + user + "' AND Defecto = '1' ORDER BY Id LIMIT 1";
										ArrayList<String> userbd = val.getRow(query);
										query = "INSERT INTO Accesos (IdUsuario, ReadProducto, AgregarProducto, DevolverProducto, CreateProducto, UpdateProducto, RetirarProducto, ReadTipoProducto, CreateTipoProducto, DeleteTipoProducto, UpdateTipoProducto, ReadBitacora, CreateReporte, ReadUsuario, CreateUsuario, DeleteUsuario, UpdateUsuario, UpdateAcceso) "
												+ "VALUES ('" + userbd.get(0) + "', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1')";
										val.executeQuery(query);
										response.sendRedirect("../../../dashboard/desconectar");
									}
								} else {
									request.getSession().setAttribute("menssaveuser",
											"Revise la cantidad de caracteres del Nombre, Apellido ó Usuario.");
									response.sendRedirect("./WCERH3gPpUFUmm8y7CN6vXL4XxDc7F.jsp");
								}
							}
							else {
								request.getSession().setAttribute("menssaveuser", "No ha ingresado el usuario del empleado.");
								response.sendRedirect("./WCERH3gPpUFUmm8y7CN6vXL4XxDc7F.jsp");
							}
						}
						else {
							request.getSession().setAttribute("menssaveuser", "No ha ingresado los apellidos del empleado.");
							response.sendRedirect("./WCERH3gPpUFUmm8y7CN6vXL4XxDc7F.jsp");
						}
					}
					else {
						request.getSession().setAttribute("menssaveuser", "No ha ingresado los nombres del empleado.");
						response.sendRedirect("./WCERH3gPpUFUmm8y7CN6vXL4XxDc7F.jsp");
					}
					//
				}
			}
			else {
				response.sendRedirect("../../../dashboard/desconectar");
			}
		} catch (SQLException e) {
			response.sendRedirect("../../../dashboard/desconectar");
		}
	}

}
