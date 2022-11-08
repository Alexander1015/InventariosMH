<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.*" %>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="lib.Validacion" %>
<%@ page import="lib.Plantilla" %>
<%
	Plantilla layout = new Plantilla();
	Validacion val = new Validacion();
%>
<!DOCTYPE html>
<html lang="es">
    <head>
        <% out.print(layout.header("Inventario", 2)); %>
    </head>
    <body>
        <% 
        	String iduser = (String) session.getAttribute("id");
			out.print(layout.navbar("Inventario", iduser, (String) session.getAttribute("token"), 2)); 
			//Inicio del Contenido 
			String query = "SELECT Defecto FROM Usuarios WHERE Id = '" + iduser + "' ORDER BY Id DESC LIMIT 1";
			ArrayList<String> usuariobd = layout.getRow(query);
			if(usuariobd.size() > 0 && usuariobd.get(0).equals("0")) {
				out.print("<script type=\"text/javascript\">");
				out.print("window.location.href = \"../perfil\"");
				out.print("</script>");
			}
			query = "SELECT ReadTipoProducto, DevolverProducto, RetirarProducto, AgregarProducto, CreateProducto, ReadProducto, ReadBitacora "
					+ "FROM Accesos WHERE IdUsuario = '" + iduser + "'";
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
			if(accesos.get(5).equals("0")) {
				out.print("<script type=\"text/javascript\">");
				out.print("window.location.href = \"../\"");
				out.print("</script>");
			}
			int verif = 0;
			for(int i = 0; i < 5; i++) {
				if(accesos.get(i).equals("1")) {
					verif++;
				}
			}
			if(verif == 0 && accesos.get(5).equals("1")) {
				out.print("<script type=\"text/javascript\">");
				out.print("window.location.href = \"./vista\"");
				out.print("</script>");
			}
		%>
        <div id="panPrincipal" class="col col-md-12">
            <div class="row">
                <div class="col col-sm-12">
                	<%
                		if(accesos.get(5).equals("1") || accesos.get(6).equals("1")) {
                			out.print("<a class='btn btn-regresar' href='../' role='button' data-bs-toggle='tooltip' data-bs-placement='left' title='Regresar a Dashboard.'>");
                			out.print("<i class='bi bi-chevron-double-left'></i>");
                			out.print("IR A DASHBOARD");
                			out.print("</a>");
                		}
                	%>
                    <p class="encabezadodash">INVENTARIO DE ACCESORIOS/DISPOSITIVOS</p>
                    <div class="row contmenInv col-12 col-sm-10 col-lg-6">
                        <div class="expli-just">
		                   	<p>
		                   		Sección del sistema orientada al control del inventario de accesorios/dispositivos, con el objetivo de comprobar existencias, datos almacenados, etc. Las opciones siguientes pueden variar según los permisos/accesos aplicados.
		                   	</p>
	                  	</div>
                        <%
                        	if(accesos.get(4).equals("1")) {
                        %>
	                        <div class="menuInv col col-12 col-sm-4">
	                            <a href="./insertar" data-bs-toggle="tooltip" data-bs-placement="top" title="Para nuevos accesorios/dispositivos en el sistema.">
	                                <div class="iconmen">
	                                    <i class="bi bi-box-seam"></i>
	                                </div>
	                                <div class="pmen">
	                                    <p>
	                                        <i class="bi bi-box-seam"></i>
	                                        INGRESOS
	                                    </p>
	                                </div>
	                            </a>
	                        </div>
	                    <%
                        	}
	                    %>
                        <div class="menuInv col col-12 col-sm-4">
                            <a href="vista" data-bs-toggle="tooltip" data-bs-placement="top" title="Para revisar el inventario actual.">
                                <div class="iconmen">
                                    <i class="bi bi-book-half"></i>
                                </div>
                                <div class="pmen">
                                    <p>
                                        <i class="bi bi-book-half"></i>
                                        CONSULTAS</p>
                                </div>
                            </a>
                        </div>
                        <%
                        	if(accesos.get(3).equals("1")) {
                        %>
	                        <div class="menuInv col col-12 col-sm-4">
	                            <a href="add.jsp" data-bs-toggle="tooltip" data-bs-placement="top" title="Para agregar cantidades a las existencias actuales.">
	                                <div class="iconmen">
	                                    <i class="bi bi-clipboard-plus"></i>
	                                </div>
	                                <div class="pmen">
	                                    <p>
	                                        <i class="bi bi-clipboard-plus"></i>
	                                        ADICIONES</p>
	                                </div>
	                            </a>
	                        </div>
                        <%
                        	}
                        	if(accesos.get(2).equals("1")) {
                        %>
	                        <div class="menuInv col col-12 col-sm-4">
	                            <a href="retiro.jsp" data-bs-toggle="tooltip" data-bs-placement="bottom" title="Para retirar cantidades de las existencias actuales.">
	                                <div class="iconmen">
	                                    <i class="bi bi-inbox-fill"></i>
	                                </div>
	                                <div class="pmen">
	                                    <p>
	                                        <i class="bi bi-inbox-fill"></i>
	                                        RETIROS</p>
	                                </div>
	                            </a>
	                        </div>
                        <%
                        	}
                        	if(accesos.get(1).equals("1")) {
                        %>
	                        <div class="menuInv col col-12 col-sm-4">
	                            <a href="devolucion.jsp" data-bs-toggle="tooltip" data-bs-placement="bottom" title="Para que un usuario pueda devolver accesorios/dispositivos.">
	                                <div class="iconmen">
	                                    <i class="bi bi-archive"></i>
	                                </div>
	                                <div class="pmen">
	                                    <p>
	                                        <i class="bi bi-archive"></i>
	                                        DEVOLUCIONES</p>
	                                </div>
	                            </a>
	                        </div>
                        <%
                        	}
                        	if(accesos.get(0).equals("1")) {
                        %>
	                        <div class="menuInv col col-12 col-sm-4">
	                            <a href="../tipo_producto/" data-bs-toggle="tooltip" data-bs-placement="bottom" title="Manejo de las clasificaciones de los accesorios/dispositivos.">
	                                <div class="iconmen">
	                                    <i class="bi bi-ui-checks"></i>
	                                </div>
	                                <div class="pmen">
	                                    <p>
	                                        <i class="bi bi-ui-checks"></i>
	                                        CLASIFICACIONES</p>
	                                </div>
	                            </a>
	                        </div>
	                     <%
                        	}
	                     %>
                    </div>
                </div>
            </div>
        </div>
        <% 
			//Fin del Contenido 
			out.print(layout.footer(2)); 
	        if(session.getAttribute("succIndexP") != null && !session.getAttribute("succIndexP").equals("")) {
				out.print(val.mostrarToastr("Éxito", "Proceso realizado correctamente."));
				if(session.getAttribute("tipo") != null && !session.getAttribute("tipo").equals("")) {
					if(session.getAttribute("idreport") != null && !session.getAttribute("idreport").equals("")) {
				        Date dNow = new Date();
				        SimpleDateFormat forma = new SimpleDateFormat ("dd/MM/yyyy");
				        String Hoy = forma.format(dNow);
			  			out.print("<div class=\"modal fade\" id=\"report\" tabindex=\"-1\" aria-labelledby=\"reportLabel\" aria-hidden=\"true\">");
			  			out.print("<div class=\"modal-dialog modal-dialog-scrollable\">");
			  			out.print("<div class=\"modal-content reportcontent\">");
			  			out.print("<div class=\"modal-header\">");
			  			out.print("<h5 class=\"modal-title\" id=\"reportLabel\">Reporte resultante <b class='bbig'>(" + Hoy + ")</b>:</h5>");
			  			out.print("<button type=\"button\" class=\"btn-close\" data-bs-dismiss=\"modal\" aria-label=\"Close\"></button>");
			  			out.print("</div>");
			  			out.print("<div class=\"modal-body\">");
			  			out.print("<iframe height='100%' width='100%' src='./");
			  			if(session.getAttribute("tipo").equals("1")) out.print("ReporteIngresos?id=" + session.getAttribute("idreport"));
		  				else if(session.getAttribute("tipo").equals("2")) out.print("ReporteAdiciones?id=" + session.getAttribute("idreport"));
	  					else if(session.getAttribute("tipo").equals("3")) out.print("ReporteRetiros?id=" + session.getAttribute("idreport"));
  						else if(session.getAttribute("tipo").equals("4")) out.print("ReporteDevoluciones?id=" + session.getAttribute("idreport"));
			  			out.print("'></iframe>");
			  			out.print("</div>");
			  			out.print("</div>");
			  			out.print("</div>");
			  			out.print("</div>");
			  			out.print("<script type=\"text/javascript\">");
			  			out.print("var myModal = new bootstrap.Modal(document.getElementById('report'), {keyboard: false});");
			  			out.print("myModal.show();");
			  			out.print("</script>");
						session.setAttribute("succIndexP", null);
						session.setAttribute("idreport", null);
						session.setAttribute("tipo", null);
					}
				}
			}
		%>
    </body>
</html>
