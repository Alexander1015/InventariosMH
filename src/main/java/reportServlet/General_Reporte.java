package reportServlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.JRLoader;

import lib.Validacion;

/**
 * Servlet implementation class General_Reporte
 */
@SuppressWarnings("deprecation")
@WebServlet("/dashboard/inventario/ReporteConsultas")
public class General_Reporte extends HttpServlet {
	
	Validacion val = new Validacion();
	
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public General_Reporte() {
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
						query = "SELECT ReadProducto FROM Accesos WHERE IdUsuario = '" + request.getSession().getAttribute("id") + "'";
						ArrayList<String> accesos = val.getRow(query);
						if(accesos.size() == 0) {
							accesos.add(0, "0");
						}
						if(accesos.get(0).equals("0")) {
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
						    String where = (String) request.getSession().getAttribute("reportebit");
						    if(where != null && !where.trim().equals("")) {
							  	parametros.put("Search", where);
							  	parametros.put("Usuario", "'" + usuario + "'");
							  	try
							    {
							    	JasperReport reporte = (JasperReport) JRLoader.loadObjectFromFile(getServletContext().getRealPath("resources/reportes/General.jasper"));
							    	JasperPrint jasperPrint = JasperFillManager.fillReport(reporte, parametros, val.Conexion());
							    	JRExporter exporter = new JRPdfExporter();
							    	exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
							    	exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, out);
							    	exporter.exportReport();
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
		//A mostrar el reporte
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
