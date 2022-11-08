<%! @SuppressWarnings("unchecked") %>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.*" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="lib.Validacion"%>
<%@ page import="lib.Plantilla"%>
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
			//Accesos
			query = "SELECT ReadProducto, UpdateProducto, ReadTipoProducto, DevolverProducto, RetirarProducto, AgregarProducto, CreateProducto FROM Accesos WHERE IdUsuario = '" + iduser + "'";
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
			if(accesos.get(0).equals("0")) {
				out.print("<script type=\"text/javascript\">");
				out.print("window.location.href = \"../inventario\"");
				out.print("</script>");
			}
			//Fin
			ArrayList<ArrayList<String>> datos = null;
			if(session.getAttribute("tablaprod") != null) {
				datos = (ArrayList<ArrayList<String>>) session.getAttribute("tablaprod");
				session.setAttribute("tablaprod", null);
			}
			String search = "";
			if(session.getAttribute("sinvent") != null && !session.getAttribute("sinvent").equals("")) {
				search = (String) session.getAttribute("sinvent");
				session.setAttribute("sinvent", null);
			}
			String searchcant = "";
			if(session.getAttribute("sinventcant") != null && !session.getAttribute("sinventcant").equals("")) {
				searchcant = (String) session.getAttribute("sinventcant");
				session.setAttribute("sinventcant", null);
			}
			String FAntes = "", FDespues = "";
			if(session.getAttribute("sinventantes") != null && !session.getAttribute("sinventantes").equals("")) {
				FAntes = (String) session.getAttribute("sinventantes");
				session.setAttribute("sinventantes", null);
			}
			else FAntes = "";
			if(session.getAttribute("sinventdespues") != null && !session.getAttribute("sinventdespues").equals("")) {
				FDespues = (String) session.getAttribute("sinventdespues");
				session.setAttribute("sinventdespues", null);
			}
			Date dNow = new Date();
			SimpleDateFormat Fsdf = new SimpleDateFormat ("YYYY-MM-dd");
			String max = Fsdf.format(dNow);
		%>
        <div id="panPrincipal" class="col col-md-12">
            <div class="row">
                <div class="col col-sm-12">
                	<%
	                	int verif = 0;
	        			for(int i = 2; i <= 6; i++) {
	        				if(accesos.get(i).equals("1")) {
	        					verif++;
	        				}
	        			}
	        			if(verif > 0 && accesos.get(0).equals("1")) {
					%>
	                    <a class="btn btn-regresar" href="./" role="button" data-bs-toggle="tooltip" data-bs-placement="left" title="Regresar al sitio anterior.">
	                        <i class="bi bi-chevron-double-left"></i>
	                        REGRESAR
	                    </a>
	                <%
	        			}
	                %>
                    <p class="encabezadodash">
                        <a href="./">INVENTARIO DE ACCESORIOS/DISPOSITVOS</a>
                        <span class="subencab">
                            / CONSULTAS</span>
                    </p>
                    <div class="grp-buscar col col-12">
                        <form class="col col-12 col-sm-12 col-md-11 col-lg-10" action="vista" method="POST">
                            <div class="row grp-buscar">
                                <div class="col col-12 col-sm-7 col-md-5">
                                	<table class="col col-12">
                                   		<tbody>
                                   			<tr class="col col-12">
                                   				<td class="col col-12 col-sm-12" colspan="3">
                                   					<input name="txtSearch" id="txtSearch" aria-describedby="txtSearch" placeholder="Por código, tipo, nombre, ubicación, marca, modelo, serie y activo fijo." class="form-control"
														autocomplete="off" type="text" spellcheck="false" maxlength="100" title="Por código, tipo, nombre, ubicación, marca, modelo, serie y activo fijo."
														value="<%= search %>" />
			                                    </td>
                                   			</tr>
                                   			<tr class="col col-12">
                                   				<td class="col col-3 col-sm-3"><p>Desde la cantidad de:</p></td>
                                   				<td class="col col-9 col-sm-9">
                                   					<input name="txtSearchCant" id="txtSearchCant" aria-describedby="txtSearchCant" placeholder="Por cantidad de accesorios/dispositivos." class="form-control"
														autocomplete="off" type="number" min="0" max="999999" spellcheck="false" maxlength="5" pattern="[0-9]{1,5}" title="Por cantidad de accesorios/dispositivos."
														value="<%= searchcant %>" />
                                   				</td>
                                   			</tr>
                                   		</tbody>
                               		</table>
                                </div>
                                <div class="col col-12 col-sm-5 col-md-4">
                                   	<table class="col col-12">
                                   		<tbody>
                                   			<tr class="col col-12">
                                   				<td class="col col-3 col-sm-3"><p>De:</p></td>
                                   				<td class="col col-9 col-sm-9">
                                   					<input  autocomplete="off" type="date" 
		                                            	class="dtfecha" id="dtInicio" name="dtInicio" 
		                                            	value="<%= FAntes %>" min="2020-01-01" max="<%= max %>" onkeydown="return false">
                                   				</td>
                                   			</tr>
                                   			<tr class="col col-12">
                                   				<td class="col col-3 col-sm-3"><p>Hasta:</p></td>
                                   				<td class="col col-9 col-sm-9">
                                   					 <input  autocomplete="off" type="date"
		                                            	class="dtfecha" id="dtFin" name="dtFin"
		                                            	value="<%= FDespues %>" min="2020-01-01" max="<%= max %>" onkeydown="return false">
                                   				</td>
                                   			</tr>
                                   		</tbody>
                               		</table>
                                </div>
                                <div class="col col-12 col-md-3">
                                	<table class="col col-12">
                                		<tbody>
                                			<tr class="col col-12">
                                				<td class="col-6">
				                            		<button type="submit" class="btn btn-ingresar btnclean-a" data-bs-toggle="tooltip" data-bs-placement="<% out.print(session.getAttribute("existablaprod") == null ? "bottom" : "top"); %>" title="Buscar registro.">
				                            			<i class="bi bi-search"></i> BUSCAR
				                           			</button>
				                           		</td>
				                           		<%
				                            		if(session.getAttribute("existablaprod") != null)
													{
				                            			out.print("<td class='col-6'>");
				                            			out.print("<a id='btnlimpiar' class='btn btn-ingresar btnclean-b puntero' data-bs-toggle='tooltip' data-bs-placement='bottom' title='Borrar los datos ingresados.'>");
				                            			out.print("<i class='bi bi-eraser-fill'></i> LIMPIAR");
				                            			out.print("</a>");
				                            			out.print("</td>");
				                            			out.print("<tr class='col col-12'>");
						                        		out.print("<td colspan='2' class='col'>");
						                        		out.print("<a class='btn btn-ingresar areporte' id='Reporte' href='./ReporteConsultas' target='_blank' data-bs-toggle='tooltip' data-bs-placement='bottom' title='Generar reporte predeterminado de esta consulta.'>");
						                        		out.print("<i class='bi bi-journal-text'></i> GENERAR REPORTE");
						                        		out.print("</a>");
						                        		out.print("</td>");
													}
				                            		else {
				                            			out.print("</tr>");
				                            			out.print("<tr>");
				                            			out.print("<td class='col-12'>");
				                            			out.print("<a id='btnlimpiar' class='btn btn-ingresar btnclean-bsup puntero' data-bs-toggle='tooltip' data-bs-placement='bottom' title='Borrar los datos ingresados.'>");
				                            			out.print("<i class='bi bi-eraser-fill'></i> LIMPIAR");
				                            			out.print("</a>");
				                            			out.print("</td>");
				                            		}
				                           		%>
			                           		</tr>
		                        		</tbody>
		                        	</table>
                                </div>
                            </div>
                            <%
	    	                  	//Errores de busqueda
	    						if(session.getAttribute("mensviewinventario") != null && !session.getAttribute("mensviewinventario").equals("")) {
	    							out.print("<div class='margsup'></div>");
	    							out.print(val.mostrarAlert(0, session.getAttribute("mensviewinventario") .toString()));
	    							session.setAttribute("mensviewinventario", null);
	    						}
                            %>
                            <div class="expli">
		                       	<p>
		                       		Realice una búsqueda de accesorios/dispositivos (Ya sea para comprobar existencias, verificar la información almacenada, etc. [Si desea obtener de resultado todos los accesorios/dispositivos genere la busqueda con: @all]) y si desea crear un reporte a partir de la vista generada presione "GENERAR REPORTE".
		                       	</p>
	                       	</div>
                        </form>
                    </div>
                    <%
						if(session.getAttribute("existablaprod") != null)
						{
							session.setAttribute("existablaprod", null);
					%>
					<div class="containTable margsup col col-12 col-lg-11">
                        <table id="registros" data-page-length="10" class="table table-hover table-bordered table-sm align-middle">
							<thead class="tableheader">
                                <tr>
                                    <th scope="col">CÓDIGO</th>
                                    <th scope="col">NOMBRE</th>
                                    <th scope="col">CLASIFICACIÓN</th>
                                    <th scope="col">UBICACIÓN ESPERADA</th>
                                    <th scope="col">CANTIDAD</th>
                                    <th scope="col">INGRESO</th>
                                    <th scope="col">ULTIMA ACTUALIZACIÓN</th>
                                    <%
                                    	if(accesos.get(1).equals("1")) {
                                    %>
                                    	<th scope="col" data-orderable="false">ACCIONES</th>
                                   	<%
                                   		}
                                   	%>
                                </tr>
                            </thead>
                            <tbody>
	                        	<%
							  		try {
							  			if(datos != null) {
							  				int elementos = datos.size();
							  				int i = 0;
									  		for (i = 0; i < elementos; i++) {
									  			out.print("<tr>");
									  			out.print("<td>" + datos.get(i).get(7) + "</td>");
								  				out.print("<td>");
												out.print(datos.get(i).get(1));
												if(datos.get(i).get(9) != null && !datos.get(i).get(9).trim().equals("-") && !datos.get(i).get(9).trim().equals("")) {
								  					out.print(", Marca: " + datos.get(i).get(9));
								  				}
								  				if(datos.get(i).get(10) != null && !datos.get(i).get(10).trim().equals("-") && !datos.get(i).get(10).trim().equals("")) {
								  					out.print(", Modelo: " + datos.get(i).get(10));
								  				}
								  				if(datos.get(i).get(11) != null && !datos.get(i).get(11).trim().equals("-") && !datos.get(i).get(11).trim().equals("")) {
								  					out.print(", Serie: " + datos.get(i).get(11));
								  				}
								  				if(datos.get(i).get(12) != null && !datos.get(i).get(12).trim().equals("-") && !datos.get(i).get(12).trim().equals("")) {
								  					out.print(", Activo Fijo: " + datos.get(i).get(12));
								  				}
								  				if((datos.get(i).get(6) != null && !datos.get(i).get(6).trim().equals(""))) {
										  			out.print("<br />Comentario: <span data-bs-toggle=\"modal\" data-bs-target=\"#coment" + datos.get(i).get(0) + "\"><a class=\"aloneopc\" data-bs-toggle=\"tooltip\" type=\"button\" data-bs-placement=\"bottom\" title=\"Click para ver el comentario del accesorio/dispositivo.\"><i class=\"bi bi-eye-fill\"></i></a></span>");
									  			}
								  				out.print("</td>");
									  			out.print("<td>" + datos.get(i).get(2) + "</td>");
									  			out.print("<td>" + datos.get(i).get(3) + "</td>");
									  			out.print("<td >" + datos.get(i).get(4) + "</td>");
									  			//Arreglando la fecha
									  			String fecha = datos.get(i).get(5);
									  			fecha = fecha.charAt(6) + "" + fecha.charAt(7) + "-" + fecha.charAt(4) + "" + fecha.charAt(5) + "-" + fecha.charAt(0) + "" + fecha.charAt(1) + "" + fecha.charAt(2) + "" + fecha.charAt(3); 
									  			out.print("<td><span class='oculto'>" + datos.get(i).get(5) + "</span>" + fecha + "</td>");
									  			//Arreglando la fecha de actualización
									  			String actu = datos.get(i).get(8);
									  			actu = actu.charAt(6) + "" + actu.charAt(7) + "-" + actu.charAt(4) + "" + actu.charAt(5) + "-" + actu.charAt(0) + "" + actu.charAt(1) + "" + actu.charAt(2) + "" + actu.charAt(3); 
									  			out.print("<td><span class='oculto'>" + datos.get(i).get(5) + "</span>" + actu + "</td>");
									  			if(accesos.get(1).equals("1")) {
						  							out.print("<td>");
													out.print("<a class=\"aloneopc\" href=\"modificar?id=" + datos.get(i).get(0) + " \" data-bs-toggle=\"tooltip\" data-bs-placement=\"right\" title=\"Click para modificar el registro.\"><i class=\"bi bi-pencil-square\"></i></a>");
													out.print("</td>");
									  			}
									  			out.print("</tr>");
								  				if (datos.get(i).get(6) != null && !datos.get(i).get(6).trim().equals("")) {
										  			out.print("<div class=\"modal fade\" id=\"coment" + datos.get(i).get(0) + "\" tabindex=\"-1\" aria-labelledby=\"coment" + datos.get(i).get(0) + "Label\" aria-hidden=\"true\">");
										  			out.print("<div class=\"modal-dialog modal-dialog-scrollable\">");
										  			out.print("<div class=\"modal-content\">");
										  			out.print("<div class=\"modal-header\">");
										  			out.print("<h5 class=\"modal-title\" id=\"coment" + datos.get(i).get(0) + "Label\">Comentario del Accesorio/Dispositivo: <b class='bbig'>" + datos.get(i).get(1) + "</b></h5>");
										  			out.print("<button type=\"button\" class=\"btn-close\" data-bs-dismiss=\"modal\" aria-label=\"Close\"></button>");
										  			out.print("</div>");
										  			out.print("<div class=\"modal-body\">");
										  			out.print(datos.get(i).get(6));
										  			out.print("</div>");
										  			out.print("</div>");
										  			out.print("</div>");
										  			out.print("</div>");
								  				}
									  		}
									  		if(i == 0) {
												out.print("<tr>");
												out.print("<td colspan=\"9\">No se encontraron elementos para mostrar.</td>");
												out.print("</tr>");
											}
							  			}
							  			else {
							  				out.print("<tr>");
							  				out.print("<td colspan=\"9\">No se encontraron elementos para mostrar.</td>");
							  				out.print("</tr>");
							  			}
							  		}
							  	 	catch (Exception e) {
							  	 		out.print("<tr>");
										out.print("<td colspan=\"9\">No se encontraron elementos para mostrar.</td>");
										out.print("</tr>");
							  		}
						  		%>
	                        </tbody>
                       	</table>
                   	</div>
                	<%
						}
					%>
	            </div>
	        </div>
	    </div>
		<%
			//fin del contenido
			out.print(layout.footer(2));
			if(session.getAttribute("succViewP") != null && !session.getAttribute("succViewP").equals("")) {
				out.print(val.mostrarToastr("Éxito", "Proceso realizado correctamente."));
				session.setAttribute("succViewP", null);
			}
		%>
		<script type="text/javascript">
			$("#btnlimpiar").click(function() {
				$("#txtSearch").val("");
				$("#txtSearchCant").val("");
				$("#dtInicio").val("");
				$("#dtFin").val("");
			});
		</script>
	</body>
</html>
