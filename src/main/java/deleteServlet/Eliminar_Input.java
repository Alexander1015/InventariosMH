package deleteServlet;

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
 * Servlet implementation class Eliminar_Input
 */
@WebServlet("/dashboard/borrar")
public class Eliminar_Input extends HttpServlet {
	
	Validacion val = new Validacion();
	
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Eliminar_Input() {
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
					response.sendRedirect("./desconectar");
				} 
				else if(datosobt.get(2).equals("0")) {
					response.sendRedirect("./perfil");
				}
				else {
					query = "SELECT ReadTipoProducto, ReadUsuario, ReadProducto, ReadBitacora, AgregarProducto, RetirarProducto, DevolverProducto FROM Accesos WHERE IdUsuario = '" + request.getSession().getAttribute("id") + "'";
					ArrayList<String> accesos = val.getRow(query);
					if(accesos.size() == 0) {
						accesos.add(0, "0");
						accesos.add(1, "0");
						accesos.add(2, "0");
						accesos.add(3, "0");
						accesos.add(4, "0");
						accesos.add(5, "0");
						accesos.add(6, "0");
					}
					String ubicacion = request.getParameter("view");
					if (ubicacion.equals("Clasificacion") && accesos.get(0).equals("1")) {
						request.getSession().setAttribute("sinputtipo", null);
						response.sendRedirect("./tipo_producto/vista");
					}
					else if (ubicacion.equals("Usuario") && accesos.get(1).equals("1")) {
						request.getSession().setAttribute("sinputuser", null);
						response.sendRedirect("./usuario/vista");
					}
					else if (ubicacion.equals("Historial")) {
						request.getSession().setAttribute("sinputhisto", null);
						request.getSession().setAttribute("sinputhistocce", null);
						request.getSession().setAttribute("sinputhistontes", null);
						request.getSession().setAttribute("sinputhistodespues", null);
						request.getSession().setAttribute("sinputhistoccion", null);
						response.sendRedirect("./perfil/historial");
					}
					else if (ubicacion.equals("Bitacora") && accesos.get(3).equals("1")) {
						request.getSession().setAttribute("sinputbita", null);
						request.getSession().setAttribute("sinputbitacce", null);
						request.getSession().setAttribute("sinputbitantes", null);
						request.getSession().setAttribute("sinputbitadespues", null);
						request.getSession().setAttribute("sinputbitaccion", null);
						response.sendRedirect("./bitacora/vista");
					}
					else if (ubicacion.equals("Producto") && accesos.get(2).equals("1")) {
						response.sendRedirect("./inventario/vista");
					}
					else {
						response.sendRedirect("../dashboard");
					}
					/*Aqui va el codigo*/
				}
			} else {
				response.sendRedirect("./desconectar");
			}
		} catch (SQLException | IOException e) {
			response.sendRedirect("./desconectar");
		}
		/* Fin de verificar sesión */
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
					response.sendRedirect("./desconectar");
				} 
				else if(datosobt.get(2).equals("0")) {
					response.sendRedirect("./perfil");
				}
				else {
					response.sendRedirect("../dashboard"); /*Se envia al dashboard*/
				}
			} else {
				response.sendRedirect("./desconectar");
			}
		} catch (SQLException | IOException e) {
			response.sendRedirect("./desconectar");
		}
		/* Fin de verificar sesión */
	}

}
