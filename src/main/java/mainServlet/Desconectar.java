package mainServlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lib.Plantilla;

/**
 * Servlet implementation class Desconectar
 */
@WebServlet("/dashboard/desconectar")
public class Desconectar extends HttpServlet {

	Plantilla master = new Plantilla();

	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Desconectar() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String query = "UPDATE Usuarios SET Token = " + null + " WHERE Id = '" + request.getSession().getAttribute("id") + "'";
		request.getSession().setAttribute("menslogin", null);
		request.getSession().setAttribute("id", null);
		request.getSession().setAttribute("token", null);
		request.getSession().setAttribute("succConect", null);
		request.getSession().setAttribute("tablatipo", null);
		request.getSession().setAttribute("stipo", null);
		request.getSession().setAttribute("existablatipo", null);
		request.getSession().setAttribute("returntipo", null);
		request.getSession().setAttribute("mensviewtipo", null);
		request.getSession().setAttribute("idtab", null);
		request.getSession().setAttribute("returntipo", null);
		request.getSession().setAttribute("otro", null);
		request.getSession().setAttribute("succViewT", null);
		request.getSession().setAttribute("succSaveT", null);
		request.getSession().setAttribute("succIndexT", null);
		request.getSession().setAttribute("succSaveP", null);
		request.getSession().setAttribute("mensviewinventario", null);
		request.getSession().setAttribute("existablaprod", null);
		request.getSession().setAttribute("tablaprod", null);
		request.getSession().setAttribute("sinvent", null);
		request.getSession().setAttribute("sinventantes", null);
		request.getSession().setAttribute("sinventdespues", null);
		request.getSession().setAttribute("succViewP", null);
		request.getSession().setAttribute("mensinsmodinventario", null);
		request.getSession().setAttribute("succInsertP", null);
		request.getSession().setAttribute("tabla_min", null);
		request.getSession().setAttribute("existablamin", null);
		request.getSession().setAttribute("succAddP", null);
		request.getSession().setAttribute("succRetP", null);
		request.getSession().setAttribute("mensretinventario", null);
		request.getSession().setAttribute("mensaddinventario", null);
		request.getSession().setAttribute("tabla_bit_min", null);
		request.getSession().setAttribute("existablabitmin", null);
		request.getSession().setAttribute("mensbitnventario", null);
		request.getSession().setAttribute("succDevP", null);
		request.getSession().setAttribute("tabla_min", null);
		request.getSession().setAttribute("existablamin", null);
		request.getSession().setAttribute("tabla_productos", null);
		request.getSession().setAttribute("id_inventario", null);
		request.getSession().setAttribute("mensdevinventario", null);
		request.getSession().setAttribute("sbita", null);
		request.getSession().setAttribute("sbitantes", null);
		request.getSession().setAttribute("sbitadespues", null);
		request.getSession().setAttribute("sbitaccion", null);
		request.getSession().setAttribute("existablabita", null);
		request.getSession().setAttribute("mensbitacora", null);
		request.getSession().setAttribute("succViewB", null);
		request.getSession().setAttribute("sbitacce", null);
		request.getSession().setAttribute("tablauser", null);
		request.getSession().setAttribute("existablauser", null);
		request.getSession().setAttribute("suser", null);
		request.getSession().setAttribute("returnuser", null);
		request.getSession().setAttribute("succViewU", null);
		request.getSession().setAttribute("mensviewuser", null);
		request.getSession().setAttribute("succSaveU", null);
		request.getSession().setAttribute("succIndexU", null);
		request.getSession().setAttribute("idInventariodev", null);
		request.getSession().setAttribute("mensperfil", null);
		request.getSession().setAttribute("idreport", null);
		request.getSession().setAttribute("tipo", null);
		request.getSession().setAttribute("shisto", null);
		request.getSession().setAttribute("shistocce", null);
		request.getSession().setAttribute("shistoccion", null);
		request.getSession().setAttribute("shistontes", null);
		request.getSession().setAttribute("shistodespues", null);
		request.getSession().setAttribute("tablahisto", null);
		request.getSession().setAttribute("existablahisto", null);
		request.getSession().setAttribute("mensacces", null);
		request.getSession().setAttribute("mensupdacces", null);
		request.getSession().setAttribute("rettipnombre", null);
		request.getSession().setAttribute("rettipsimb", null);
		request.getSession().setAttribute("retusrnombre", null);
		request.getSession().setAttribute("retusrapellido", null);
		request.getSession().setAttribute("retusrusuario", null);
		request.getSession().setAttribute("retmodcodigo", null);
		request.getSession().setAttribute("retmodnombre", null);
		request.getSession().setAttribute("retmodcantidad", null);
		request.getSession().setAttribute("retmodubicacion", null);
		request.getSession().setAttribute("retmodcoment", null);
		request.getSession().setAttribute("retmodmarca", null);
		request.getSession().setAttribute("retmodmodelo", null);
		request.getSession().setAttribute("retmodserie", null);
		request.getSession().setAttribute("retmodactivo", null);
		request.getSession().setAttribute("retinstipo", null);
		request.getSession().setAttribute("retinsautor", null);
		request.getSession().setAttribute("retinscomenting", null);
		request.getSession().setAttribute("retinsyo", null);
		request.getSession().setAttribute("retaddautor", null);
		request.getSession().setAttribute("retaddcomenting", null);
		request.getSession().setAttribute("retaddyo", null);
		request.getSession().setAttribute("retdevcomenting", null);
		request.getSession().setAttribute("retretpersonal", null);
		request.getSession().setAttribute("retretubicacion", null);
		request.getSession().setAttribute("retretautor", null);
		request.getSession().setAttribute("retretcomenting", null);
		request.getSession().setAttribute("retretautoyo", null);
		request.getSession().setAttribute("retretpersoyo", null);
		request.getSession().setAttribute("sinventcant", null);
		request.getSession().setAttribute("retaddsearch", null);
		request.getSession().setAttribute("retretsearch", null);
		request.getSession().setAttribute("retdevsearch", null);
		request.getSession().setAttribute("retdevsearchacc", null);
		request.getSession().setAttribute("retdevinicio", null);
		request.getSession().setAttribute("retdevfin", null);
		request.getSession().setAttribute("inituser", null);
		request.getSession().setAttribute("sinputtipo", null);
		request.getSession().setAttribute("sinputuser", null);
		request.getSession().setAttribute("sinputhisto", null);
		request.getSession().setAttribute("sinputhistocce", null);
		request.getSession().setAttribute("sinputhistontes", null);
		request.getSession().setAttribute("sinputhistodespues", null);
		request.getSession().setAttribute("sinputhistoccion", null);
		request.getSession().setAttribute("sinputbita", null);
		request.getSession().setAttribute("sinputbitacce", null);
		request.getSession().setAttribute("sinputbitantes", null);
		request.getSession().setAttribute("sinputbitadespues", null);
		request.getSession().setAttribute("sinputbitaccion", null);
		request.getSession().setAttribute("sinputinvent", null);
		request.getSession().setAttribute("sinputinventcant", null);
		request.getSession().setAttribute("sinputbitantes", null);
		request.getSession().setAttribute("sinputinventdespues", null);
		request.getSession().setAttribute("retretcaso", null);
		request.getSession().setAttribute("retretref1", null);
		request.getSession().setAttribute("retretref2", null);
		request.getSession().setAttribute("retretref3", null);
		request.getSession().setAttribute("retaddcaso", null);
		request.getSession().setAttribute("retaddref1", null);
		request.getSession().setAttribute("retaddref2", null);
		request.getSession().setAttribute("retaddref3", null);
		request.getSession().setAttribute("retinscaso", null);
		request.getSession().setAttribute("retinsref1", null);
		request.getSession().setAttribute("retinsref2", null);
		request.getSession().setAttribute("retinsref3", null);
		request.getSession().setAttribute("retaddRF", null);
		request.getSession().setAttribute("retretRF", null);
		request.getSession().setAttribute("retinsRF", null);
		master.executeQuery(query);
		response.sendRedirect("../");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String query = "UPDATE Usuarios SET Token = " + null + " WHERE Id = '" + request.getSession().getAttribute("id") + "'";
		request.getSession().setAttribute("menslogin", null);
		request.getSession().setAttribute("id", null);
		request.getSession().setAttribute("token", null);
		request.getSession().setAttribute("succConect", null);
		request.getSession().setAttribute("tablatipo", null);
		request.getSession().setAttribute("stipo", null);
		request.getSession().setAttribute("existablatipo", null);
		request.getSession().setAttribute("returntipo", null);
		request.getSession().setAttribute("mensviewtipo", null);
		request.getSession().setAttribute("idtab", null);
		request.getSession().setAttribute("returntipo", null);
		request.getSession().setAttribute("otro", null);
		request.getSession().setAttribute("succViewT", null);
		request.getSession().setAttribute("succSaveT", null);
		request.getSession().setAttribute("succIndexT", null);
		request.getSession().setAttribute("succSaveP", null);
		request.getSession().setAttribute("mensviewinventario", null);
		request.getSession().setAttribute("existablaprod", null);
		request.getSession().setAttribute("tablaprod", null);
		request.getSession().setAttribute("sinvent", null);
		request.getSession().setAttribute("sinventantes", null);
		request.getSession().setAttribute("sinventdespues", null);
		request.getSession().setAttribute("succViewP", null);
		request.getSession().setAttribute("mensinsmodinventario", null);
		request.getSession().setAttribute("succInsertP", null);
		request.getSession().setAttribute("tabla_min", null);
		request.getSession().setAttribute("existablamin", null);
		request.getSession().setAttribute("succAddP", null);
		request.getSession().setAttribute("succRetP", null);
		request.getSession().setAttribute("mensretinventario", null);
		request.getSession().setAttribute("mensaddinventario", null);
		request.getSession().setAttribute("tabla_bit_min", null);
		request.getSession().setAttribute("existablabitmin", null);
		request.getSession().setAttribute("mensbitnventario", null);
		request.getSession().setAttribute("succDevP", null);
		request.getSession().setAttribute("tabla_min", null);
		request.getSession().setAttribute("existablamin", null);
		request.getSession().setAttribute("tabla_productos", null);
		request.getSession().setAttribute("id_inventario", null);
		request.getSession().setAttribute("mensdevinventario", null);
		request.getSession().setAttribute("sbita", null);
		request.getSession().setAttribute("sbitantes", null);
		request.getSession().setAttribute("sbitadespues", null);
		request.getSession().setAttribute("sbitaccion", null);
		request.getSession().setAttribute("existablabita", null);
		request.getSession().setAttribute("mensbitacora", null);
		request.getSession().setAttribute("succViewB", null);
		request.getSession().setAttribute("sbitacce", null);
		request.getSession().setAttribute("tablauser", null);
		request.getSession().setAttribute("existablauser", null);
		request.getSession().setAttribute("suser", null);
		request.getSession().setAttribute("returnuser", null);
		request.getSession().setAttribute("succViewU", null);
		request.getSession().setAttribute("mensviewuser", null);
		request.getSession().setAttribute("succSaveU", null);
		request.getSession().setAttribute("succIndexU", null);
		request.getSession().setAttribute("idInventariodev", null);
		request.getSession().setAttribute("mensperfil", null);
		request.getSession().setAttribute("idreport", null);
		request.getSession().setAttribute("tipo", null);
		request.getSession().setAttribute("shisto", null);
		request.getSession().setAttribute("shistocce", null);
		request.getSession().setAttribute("shistoccion", null);
		request.getSession().setAttribute("shistontes", null);
		request.getSession().setAttribute("shistodespues", null);
		request.getSession().setAttribute("tablahisto", null);
		request.getSession().setAttribute("existablahisto", null);
		request.getSession().setAttribute("mensacces", null);
		request.getSession().setAttribute("mensupdacces", null);
		request.getSession().setAttribute("rettipnombre", null);
		request.getSession().setAttribute("rettipsimb", null);
		request.getSession().setAttribute("retusrnombre", null);
		request.getSession().setAttribute("retusrapellido", null);
		request.getSession().setAttribute("retusrusuario", null);
		request.getSession().setAttribute("retmodcodigo", null);
		request.getSession().setAttribute("retmodnombre", null);
		request.getSession().setAttribute("retmodcantidad", null);
		request.getSession().setAttribute("retmodubicacion", null);
		request.getSession().setAttribute("retmodcoment", null);
		request.getSession().setAttribute("retmodmarca", null);
		request.getSession().setAttribute("retmodmodelo", null);
		request.getSession().setAttribute("retmodserie", null);
		request.getSession().setAttribute("retmodactivo", null);
		request.getSession().setAttribute("retinstipo", null);
		request.getSession().setAttribute("retinsautor", null);
		request.getSession().setAttribute("retinscomenting", null);
		request.getSession().setAttribute("retinsyo", null);
		request.getSession().setAttribute("retaddautor", null);
		request.getSession().setAttribute("retaddcomenting", null);
		request.getSession().setAttribute("retaddyo", null);
		request.getSession().setAttribute("retdevcomenting", null);
		request.getSession().setAttribute("retretpersonal", null);
		request.getSession().setAttribute("retretubicacion", null);
		request.getSession().setAttribute("retretautor", null);
		request.getSession().setAttribute("retretcomenting", null);
		request.getSession().setAttribute("retretautoyo", null);
		request.getSession().setAttribute("retretpersoyo", null);
		request.getSession().setAttribute("sinventcant", null);
		request.getSession().setAttribute("retaddsearch", null);
		request.getSession().setAttribute("retretsearch", null);
		request.getSession().setAttribute("retdevsearch", null);
		request.getSession().setAttribute("retdevsearchacc", null);
		request.getSession().setAttribute("retdevinicio", null);
		request.getSession().setAttribute("retdevfin", null);
		request.getSession().setAttribute("inituser", null);
		request.getSession().setAttribute("sinputtipo", null);
		request.getSession().setAttribute("sinputuser", null);
		request.getSession().setAttribute("sinputhisto", null);
		request.getSession().setAttribute("sinputhistocce", null);
		request.getSession().setAttribute("sinputhistontes", null);
		request.getSession().setAttribute("sinputhistodespues", null);
		request.getSession().setAttribute("sinputhistoccion", null);
		request.getSession().setAttribute("sinputbita", null);
		request.getSession().setAttribute("sinputbitacce", null);
		request.getSession().setAttribute("sinputbitantes", null);
		request.getSession().setAttribute("sinputbitadespues", null);
		request.getSession().setAttribute("sinputbitaccion", null);
		request.getSession().setAttribute("sinputinvent", null);
		request.getSession().setAttribute("sinputinventcant", null);
		request.getSession().setAttribute("sinputbitantes", null);
		request.getSession().setAttribute("sinputinventdespues", null);
		request.getSession().setAttribute("retretcaso", null);
		request.getSession().setAttribute("retretref1", null);
		request.getSession().setAttribute("retretref2", null);
		request.getSession().setAttribute("retretref3", null);
		request.getSession().setAttribute("retaddcaso", null);
		request.getSession().setAttribute("retaddref1", null);
		request.getSession().setAttribute("retaddref2", null);
		request.getSession().setAttribute("retaddref3", null);
		request.getSession().setAttribute("retinscaso", null);
		request.getSession().setAttribute("retinsref1", null);
		request.getSession().setAttribute("retinsref2", null);
		request.getSession().setAttribute("retinsref3", null);
		request.getSession().setAttribute("retaddRF", null);
		request.getSession().setAttribute("retretRF", null);
		request.getSession().setAttribute("retinsRF", null);
		master.executeQuery(query);
		response.sendRedirect("../");
	}

}
