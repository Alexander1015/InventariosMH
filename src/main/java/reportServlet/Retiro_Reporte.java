package reportServlet;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lib.Validacion;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.JRLoader;

/**
 * Servlet implementation class Retiro_Reporte
 */
@SuppressWarnings("deprecation")
@WebServlet("/dashboard/inventario/ReporteRetiros")
public class Retiro_Reporte extends HttpServlet {
	
	Validacion val = new Validacion();
	
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Retiro_Reporte() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String query = "SELECT Nombres, Apellidos, Defecto "
				+ "FROM Usuarios WHERE Id = '" + request.getSession().getAttribute("id")
				+ "' ORDER BY Id DESC LIMIT 1";
		String usuario = "";
		//A mostrar el reporte
		response.setContentType("application/pdf");
	    ServletOutputStream out = response.getOutputStream();
	    Map parametros = new HashMap();
		try {
			ArrayList<String> datosobt = val.getRow(query);
			if (datosobt.size() > 0) {
				usuario = datosobt.get(0) + " " + datosobt.get(1);
			}
			if (request.getSession().getAttribute("id") != null
					&& !request.getSession().getAttribute("id").equals("")) {
				if (!val.verificarsesion(request.getSession().getAttribute("id").toString(), usuario,
						request.getSession().getAttribute("token").toString())) {
				    try
				    {
				    	JasperReport reporte = (JasperReport) JRLoader.loadObjectFromFile(getServletContext().getRealPath("resources/reportes/Vacio.jasper"));
				    	JasperPrint jasperPrint = JasperFillManager.fillReport(reporte, null);
				    	JRExporter exporter = new JRPdfExporter();
				    	exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
				    	exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, out);
				    	exporter.exportReport();
				    }
				    catch (Exception error) { }
				} 
				else if(datosobt.get(2).equals("0")) {
				    try
				    {
				    	JasperReport reporte = (JasperReport) JRLoader.loadObjectFromFile(getServletContext().getRealPath("resources/reportes/Vacio.jasper"));
				    	JasperPrint jasperPrint = JasperFillManager.fillReport(reporte, null);
				    	JRExporter exporter = new JRPdfExporter();
				    	exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
				    	exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, out);
				    	exporter.exportReport();
				    }
				    catch (Exception error) { }
				}
				else {
					try {
					   String id = request.getParameter("id");
					   if(id != null && !id.trim().equals("")) {
						   	query = "SELECT I.UsuarioExterno AS UsuarioExt, (U.Nombres || ' ' || U.Apellidos) AS UsuarioInt, I.Caso, U.Id, I.Autorizacion, I.FechaReporte, I.Referencia FROM Inventarios AS I "
						   			+ "INNER JOIN Usuarios AS U ON U.Id = I.IdUsuario WHERE I.Id = '" + id + "' ORDER BY I.Id LIMIT 1";
						   	ArrayList<String> usuariosbd = val.getRow(query);
						   	if(usuariosbd.size() > 0) {
						   		query = "SELECT CreateReporte FROM Accesos WHERE IdUsuario = '" + request.getSession().getAttribute("id") + "'";
								ArrayList<String> accesos = val.getRow(query);
								if(accesos.size() == 0) {
									accesos.add(0, "0");
								}
								if(accesos.get(0).equals("0") && !usuariosbd.get(3).equals(request.getSession().getAttribute("id"))) {
								    try
								    {
								    	JasperReport reporte = (JasperReport) JRLoader.loadObjectFromFile(getServletContext().getRealPath("resources/reportes/Vacio.jasper"));
								    	JasperPrint jasperPrint = JasperFillManager.fillReport(reporte, null);
								    	JRExporter exporter = new JRPdfExporter();
								    	exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
								    	exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, out);
								    	exporter.exportReport();
								    }
								    catch (Exception error) { }
								}
								else {
									parametros.put("Inventario", "'" + id + "'");
								   	if(usuariosbd.get(0) != null && !usuariosbd.get(0).trim().equals("")) parametros.put("Usuario", "I.UsuarioExterno");
								   	else if(usuariosbd.get(1) != null && !usuariosbd.get(1).trim().equals("")) parametros.put("Usuario", "(U.Nombres || ' ' || U.Apellidos)");
								   	else parametros.put("Usuario", "'Usuario no Identificado'");
								   	if(usuariosbd.get(2) != null && !usuariosbd.get(2).trim().equals("")) parametros.put("Caso", "I.Caso");
								   	else  parametros.put("Caso", "'-'");
									if(usuariosbd.get(6) != null && !usuariosbd.get(6).trim().equals("")) parametros.put("Referencia", "I.Referencia");
								   	else  parametros.put("Referencia", "'-'");
								   	if(usuariosbd.get(4) != null && !usuariosbd.get(4).trim().equals("")) parametros.put("Autorizador", "I.Autorizacion");
								   	else if(usuariosbd.get(1) != null && !usuariosbd.get(1).trim().equals("")) parametros.put("Autorizador", "(U.Nombres || ' ' || U.Apellidos)");
								   	else parametros.put("Autorizador", "'Autorización no Identificada'");
								   	try
								    {
								   		Date dNow = new Date();
										SimpleDateFormat Fsdf = new SimpleDateFormat("yyyy-MM-dd");
										String FHoy = Fsdf.format(dNow);
										if (FHoy.equals(usuariosbd.get(5))) {
											JasperReport reporte = (JasperReport) JRLoader.loadObjectFromFile(getServletContext().getRealPath("resources/reportes/Retiro_sFecha.jasper"));
									    	JasperPrint jasperPrint = JasperFillManager.fillReport(reporte, parametros, val.Conexion());
									    	JRExporter exporter = new JRPdfExporter();
									    	exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
									    	exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, out);
									    	exporter.exportReport();
										}
										else {
											JasperReport reporte = (JasperReport) JRLoader.loadObjectFromFile(getServletContext().getRealPath("resources/reportes/Retiro.jasper"));
									    	JasperPrint jasperPrint = JasperFillManager.fillReport(reporte, parametros, val.Conexion());
									    	JRExporter exporter = new JRPdfExporter();
									    	exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
									    	exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, out);
									    	exporter.exportReport();
										}
								    }
								    catch (Exception e)
								    {
									    try
									    {
									    	JasperReport reporte = (JasperReport) JRLoader.loadObjectFromFile(getServletContext().getRealPath("resources/reportes/Vacio.jasper"));
									    	JasperPrint jasperPrint = JasperFillManager.fillReport(reporte, null);
									    	JRExporter exporter = new JRPdfExporter();
									    	exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
									    	exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, out);
									    	exporter.exportReport();
									    }
									    catch (Exception error) { }
								    }
								}
						   	}
						   	else {
							    try
							    {
							    	JasperReport reporte = (JasperReport) JRLoader.loadObjectFromFile(getServletContext().getRealPath("resources/reportes/Vacio.jasper"));
							    	JasperPrint jasperPrint = JasperFillManager.fillReport(reporte, null);
							    	JRExporter exporter = new JRPdfExporter();
							    	exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
							    	exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, out);
							    	exporter.exportReport();
							    }
							    catch (Exception error) { }
						   	}
					   }
					   else {
						    try
						    {
						    	JasperReport reporte = (JasperReport) JRLoader.loadObjectFromFile(getServletContext().getRealPath("resources/reportes/Vacio.jasper"));
						    	JasperPrint jasperPrint = JasperFillManager.fillReport(reporte, null);
						    	JRExporter exporter = new JRPdfExporter();
						    	exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
						    	exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, out);
						    	exporter.exportReport();
						    }
						    catch (Exception error) { }
					   }
					} catch (Exception e) {
					    try
					    {
					    	JasperReport reporte = (JasperReport) JRLoader.loadObjectFromFile(getServletContext().getRealPath("resources/reportes/Vacio.jasper"));
					    	JasperPrint jasperPrint = JasperFillManager.fillReport(reporte, null);
					    	JRExporter exporter = new JRPdfExporter();
					    	exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
					    	exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, out);
					    	exporter.exportReport();
					    }
					    catch (Exception error) { }
					}
				}
			} else {
			    try
			    {
			    	JasperReport reporte = (JasperReport) JRLoader.loadObjectFromFile(getServletContext().getRealPath("resources/reportes/Vacio.jasper"));
			    	JasperPrint jasperPrint = JasperFillManager.fillReport(reporte, null);
			    	JRExporter exporter = new JRPdfExporter();
			    	exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			    	exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, out);
			    	exporter.exportReport();
			    }
			    catch (Exception error) { }
			}
		} catch (SQLException | IOException e) {
		    try
		    {
		    	JasperReport reporte = (JasperReport) JRLoader.loadObjectFromFile(getServletContext().getRealPath("resources/reportes/Vacio.jasper"));
		    	JasperPrint jasperPrint = JasperFillManager.fillReport(reporte, null);
		    	JRExporter exporter = new JRPdfExporter();
		    	exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		    	exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, out);
		    	exporter.exportReport();
		    }
		    catch (Exception error) { }
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@SuppressWarnings({ "rawtypes" })
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/pdf");
	    ServletOutputStream out = response.getOutputStream();
	    try
	    {
	    	JasperReport reporte = (JasperReport) JRLoader.loadObjectFromFile(getServletContext().getRealPath("resources/reportes/Vacio.jasper"));
	    	JasperPrint jasperPrint = JasperFillManager.fillReport(reporte, null);
	    	JRExporter exporter = new JRPdfExporter();
	    	exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
	    	exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, out);
	    	exporter.exportReport();
	    }
	    catch (Exception error) { }
	}

}
