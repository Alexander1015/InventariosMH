<%! @SuppressWarnings("unchecked") %>
<%@page import="java.util.ArrayList"%>
<%@page import="java.text.SimpleDateFormat" %>
<%@page import="java.util.Date" %>
<%@page import="lib.Validacion"%>
<%@page import="lib.Plantilla"%>
<%
	Plantilla layout = new Plantilla();
	Validacion val = new Validacion();
%>
<!DOCTYPE html>
<html lang="es">
    <head>
        <% out.print(layout.header("Bitacora", 2)); %>
    </head>
    <body>
    	<%
    		String iduser = (String)session.getAttribute("id");
			out.print(layout.navbar("Bitacora", iduser, (String)session.getAttribute("token"), 2));
			//Inicio del Contenido
			String query = "SELECT Defecto FROM Usuarios WHERE Id = '" + iduser + "' ORDER BY Id DESC LIMIT 1";
			ArrayList<String> usuariobd = layout.getRow(query);
			if(usuariobd.size() > 0 && usuariobd.get(0).equals("0")) {
				out.print("<script type=\"text/javascript\">");
				out.print("window.location.href = \"../perfil\"");
				out.print("</script>");
			}
			//Accesos
			query = "SELECT ReadBitacora, CreateReporte, ReadProducto FROM Accesos WHERE IdUsuario = '" + iduser + "'";
			ArrayList<String> accesos = val.getRow(query);
			if(accesos.size() == 0) {
				accesos.add(0, "0");
				accesos.add(1, "0");
				accesos.add(2, "0");
			}
			if(accesos.get(0).equals("0")) {
				out.print("<script type=\"text/javascript\">");
				out.print("window.location.href = \"../\"");
				out.print("</script>");
			}
			//Fin
			ArrayList<ArrayList<String>> datos = null;
			if(session.getAttribute("tablabita") != null) {
				datos = (ArrayList<ArrayList<String>>) session.getAttribute("tablabita");
				session.setAttribute("tablabita", null);
			}
			String search = "";
			if(session.getAttribute("sinputbita") != null && !session.getAttribute("sinputbita").equals("")) {
				search = (String) session.getAttribute("sinputbita");
			}
			String saccesorio = "";
			if(session.getAttribute("sinputbitacce") != null && !session.getAttribute("sinputbitacce").equals("")) {
				saccesorio = (String) session.getAttribute("sinputbitacce");
			}
			String FAntes = "", FDespues = "";
			if(session.getAttribute("sinputbitantes") != null && !session.getAttribute("sinputbitantes").equals("")) {
				FAntes = (String) session.getAttribute("sinputbitantes");
			}
			else FAntes = "";
			if(session.getAttribute("sinputbitadespues") != null && !session.getAttribute("sinputbitadespues").equals("")) {
				FDespues = (String) session.getAttribute("sinputbitadespues");
			}
			String accion = "";
			if(session.getAttribute("sinputbitaccion") != null && !session.getAttribute("sinputbitaccion").equals("")) {
				accion = (String) session.getAttribute("sinputbitaccion");
			}
			Date dNow = new Date();
			SimpleDateFormat Fsdf = new SimpleDateFormat ("YYYY-MM-dd");
			String max = Fsdf.format(dNow);
		%>
        <div id="panPrincipal" class="col col-md-12">
            <div class="row">
                <div class="col col-sm-12">
                   	<%
                		if(accesos.get(0).equals("1") || accesos.get(2).equals("1")) {
                			out.print("<a class='btn btn-regresar' href='../' role='button' data-bs-toggle='tooltip' data-bs-placement='left' title='Regresar a Dashboard.'>");
                			out.print("<i class='bi bi-chevron-double-left'></i>");
                			out.print("IR A DASHBOARD");
                			out.print("</a>");
                		}
                	%>
                    <p class="encabezadodash">BITACORA DE PROCESOS</p>
                    <div class="grp-buscar col col-12">
                        <form class="col col-12 col-sm-12 col-md-11 col-lg-10" action="vista" method="POST">
	                        <div class="row grp-buscar">
	                        	<div class="col col-12 col-sm-12 col-md-4">
	                            	<table class="col col-12">
	                           			<tbody>
	                           				<tr class="col col-12">
	                           					<td class="col col-12 col-sm-6">
	                           						<input name="txtSearch" id="txtSearch" aria-describedby="txtSearch" placeholder="Por asignación, autorización, ubicación o No. de caso y referencia" class="form-control marginf"
														autocomplete="off" type="text" spellcheck="false" maxlength="100"
														value="<%= search %>" title="Por asignación, autorización, ubicación o No. de caso y referencia."/>
	                           					</td>
	                           				</tr>
	                           				<tr class="col col-12">
	                           					<td class="col col-12 col-sm-6">
	                           						<input name="txtSearchAcc" id="txtSearchAcc" aria-describedby="txtSearchAcc" placeholder="Por accesorio/dispositivo." class="form-control marginf"
														autocomplete="off" type="text" spellcheck="false" maxlength="100"
														value="<%= saccesorio %>" title="por accesorio/dispositivo."/>
	                           					</td>
	                           				</tr>
	                           			</tbody>
	                           		</table>
	                            </div>
                                <div class="col col-sm-12 col-md-2 accionbit">
                                    <select class="form-select" aria-label="Acciones" name="cmbAccion">
                                        <option <%= (accion == null || accion.trim().equals("")) ? "selected" : "" %> disabled="disabled">Por proceso</option>
                                        <option value="2" <%= accion.equals("2") ? "selected" : "" %>>Adiciones</option>
                                        <option value="0" <%= accion.equals("0") ? "selected" : "" %>>Consultas</option>
                                        <option value="4" <%= accion.equals("4") ? "selected" : "" %>>Devoluciones</option>
                                        <option value="1" <%= accion.equals("1") ? "selected" : "" %>>Ingresos</option>
                                        <option value="5" <%= accion.equals("5") ? "selected" : "" %>>Modificaciones</option>
                                        <option value="3" <%= accion.equals("3") ? "selected" : "" %>>Retiros</option>
                                        <option value="6" <%= accion.equals("6") ? "selected" : "" %>>N/A</option>
                                    </select>
                                </div>
                                <div class="col col-12 col-sm-12 col-md-4">
	                              	<table class="col col-12">
	                           			<tbody>
	                              			<tr class="col col-12">
	                              				<td class="col col-3 col-sm-3"><p>De:</p></td>
	                              				<td class="col col-9 col-sm-9">
	                              					<input autocomplete="off" type="date" class="dtfecha" id="dtInicio" name="dtInicio" value="<%= FAntes %>" min="2020-01-01" max="<%= max %>" onkeydown="return false">
	                              				</td>
	                              			</tr>
	                              			<tr class="col col-12">
	                              				<td class="col col-3 col-sm-3"><p>Hasta:</p></td>
	                              				<td class="col col-9 col-sm-9">
	                              					<input autocomplete="off" type="date" class="dtfecha" id="dtFin" name="dtFin" value="<%= FDespues %>" min="2020-01-01" max="<%= max %>" onkeydown="return false">
	                                           </td>
	                              			</tr>
	                              		</tbody>
                       				</table>
                           		</div>
                        		<div class="row col col-12 col-md-2 divcentrado">
                        			<table class="col col-12">
	                           			<tbody>
	                              			<tr class="col col-12">
	                              				<td class="col">
					                          		<button type="submit" class="btn btn-ingresar" data-bs-toggle="tooltip" data-bs-placement="bottom" title="Buscar registro.">
					                          			<i class="bi bi-search"></i> BUSCAR
					                         		</button>
				                         		</td>
	                              			</tr>
	                              			<tr class="col col-12">
	                              				<td class="col">
	                              					<a href="../borrar?view=Bitacora" class="btn btn-ingresar areporte" data-bs-toggle="tooltip" data-bs-placement="bottom" title="Borrar los datos ingresados.">
					                          			<i class="bi bi-eraser-fill"></i> LIMPIAR
					                         		</a>
	                              				</td>
	                              			</tr>
                              			</tbody>
                           			</table>
                        		</div>
                           	</div>
                           	<%
	    	                  	//Errores de busqueda
	    						if(session.getAttribute("mensbitacora") != null && !session.getAttribute("mensbitacora").equals("")) {
	    							out.print("<div class='margsup'></div>");
	    							out.print(val.mostrarAlert(0, session.getAttribute("mensbitacora") .toString()));
	    							session.setAttribute("mensbitacora", null);
	    						}
                            %>
                            <div class="expli-just">
		                       	<p>
	                       			Realice una búsqueda de los procesos realizados en la sección del Inventario de Accesorios/Dispositivos (excluyendo el apartado del manejo de clasificaciones), si no ha realizado una búsqueda (o deja en blanco todos los datos que se piden) se mostrarán los últimos 10 procesos en el sistema (no consultas).
		                       	</p>
	                       	</div>
	                     </form>
	                 </div>
                    <%
						if(session.getAttribute("existablabita") != null)
						{
							session.setAttribute("existablabita", null);
					%>
	                    <div class="containTable col col-12 col-lg-11">
	                        <table id="registrosbit" data-page-length="10" class="table table-hover table-bordered table-sm align-middle">
								<thead class="tableheader">
	                                <tr>
	                                	<th scope="col">FECHA</th>
	                                	<th scope="col">HORA</th>
	                                	<th scope="col">CASO</th>
	                                    <th scope="col">INGRESADO POR</th>
	                                    <th scope="col">ASIGNADO A</th>
	                                    <th scope="col">AUTORIZADO POR</th>
	                                    <th scope="col">MOVIMIENTO</th>
	                                    <th scope="col" data-orderable="false">DESCRIPCIÓN</th>
                                    	<th scope="col" data-orderable="false">ACCIONES</th>
	                                </tr>
	                            </thead>
	                            <tbody>
			                        <%
								  		try {
								  			if(datos != null) {
								  				int elementos = datos.size();
								  				int i = 0;
								  				String registro = "";
										  		for (i = 0; i < elementos; i++) {
										  			if(!registro.equals(datos.get(i).get(0))) {
										  				out.print("<tr>");
										  				if(!datos.get(i).get(3).equals("0")) {
										  					//Arreglando la fecha
													  		String fecha = datos.get(i).get(4);
												  			fecha = fecha.charAt(6) + "" + fecha.charAt(7) + "-" + fecha.charAt(4) + "" + fecha.charAt(5) + "-" + fecha.charAt(0) + "" + fecha.charAt(1) + "" + fecha.charAt(2) + "" + fecha.charAt(3); 
												  			out.print("<td><span class='oculto'>" + datos.get(i).get(4) + "</span>" + fecha + "</td>");
											  				//Arreglamos la hora
											  				String hora = datos.get(i).get(5);
												  			int canthora = Integer.parseInt(datos.get(i).get(5).charAt(0) + "" + datos.get(i).get(5).charAt(1));
												  			if(canthora > 12) {
												  				canthora = canthora - 12;
												  				String conj = canthora < 10 ? "0" + canthora : "" + canthora;
											  					hora = conj.charAt(0) + "" + conj.charAt(1) + "" + hora.charAt(2) + "" + hora.charAt(3) + "" + hora.charAt(4) + " p.m.";
												  			}
												  			else hora = hora.charAt(0) + "" + hora.charAt(1) + "" + hora.charAt(2) + "" + hora.charAt(3) + "" + hora.charAt(4) + " a.m.";
												  			out.print("<td><span class='oculto'>" + datos.get(i).get(5) + "</span>" + hora + "</td>");
													  		
											  			}
											  			else {
											  				out.print("<td>-</td>");
											  				out.print("<td>-</td>");
											  			}
										  				out.print("<td>" + ((datos.get(i).get(9) != null && !datos.get(i).get(9).trim().equals("")) && ((datos.get(i).get(13) != null && !datos.get(i).get(13).trim().equals(""))) ? "No. " + datos.get(i).get(9) + " / " + datos.get(i).get(13) : "-") + "</td>");
										  				//Atendido por
										  				out.print("<td>" + datos.get(i).get(1) + " " + datos.get(i).get(2) + "</td>");
										  				//Asignado
										  				if(datos.get(i).get(3).equals("3") || datos.get(i).get(3).equals("4")) {
										  					out.print("<td>" + ((datos.get(i).get(7) != null && !datos.get(i).get(7).trim().equals("")) ? datos.get(i).get(7) + ((datos.get(i).get(8) != null && !datos.get(i).get(8).trim().equals("")) ? ", en " + datos.get(i).get(8) : "") : datos.get(i).get(1) + " " + datos.get(i).get(2) + ((datos.get(i).get(8) != null && !datos.get(i).get(8).trim().equals("")) ? ", en " + datos.get(i).get(8) : "")) + "</td>");
										  				}
										  				else {
										  					out.print("<td>-</td>");
										  				}
										  				//Autorizado
										  				if(!datos.get(i).get(3).equals("0") && !datos.get(i).get(3).equals("5") && !datos.get(i).get(3).equals("4")) {
											  				out.print("<td>" + ((datos.get(i).get(12) != null && !datos.get(i).get(12).trim().equals("")) ? datos.get(i).get(12) : datos.get(i).get(1) + " " + datos.get(i).get(2)) + "</td>");
										  				}
											  			else {
										  					out.print("<td>-</td>");
										  				}
										  				out.print("<td>");
											  			if(datos.get(i).get(3).equals("0")) out.print("Consultas");
											  			else if(datos.get(i).get(3).equals("1")) out.print("Ingresos");
											  			else if(datos.get(i).get(3).equals("2")) out.print("Adiciones");
											  			else if(datos.get(i).get(3).equals("3")) out.print("Retiros");
											  			else if(datos.get(i).get(3).equals("4")) out.print("Devoluciones");
											  			else if(datos.get(i).get(3).equals("5")) out.print("Modificaciones");
											  			else out.print("N/A");
											  			out.print("</td>");
											  			out.print("<td>");
											  			out.print("<span class=\"puntero\" data-bs-toggle=\"modal\" data-bs-target=\"#descript" + datos.get(i).get(0) + "\"><a class=\"aloneopc\" data-bs-toggle=\"tooltip\" data-bs-placement=\"left\" title=\"Click para ver la descripción del registro.\">");
											  			out.print("<i class=\"bi bi-eye-fill\"></i>");
											  			out.print("</a></span>");
							  							out.print("</td>");
							  							out.print("<td>");
							  							if(accesos.get(1).equals("1") || (accesos.get(1).equals("0") && datos.get(i).get(10).equals(iduser))) {
								  							if(datos.get(i).get(3).equals("0") || datos.get(i).get(3).equals("5")) {
									  							out.print("-");
								  							}
								  							else {
								  								out.print("<a class=\"btn btn-ingresar\" href=\"");
								  								//Diferentes reportes
								  								if(datos.get(i).get(3).equals("1")) out.print("../inventario/ReporteIngresos?id=" + datos.get(i).get(0));
								  								else if(datos.get(i).get(3).equals("2")) out.print("../inventario/ReporteAdiciones?id=" + datos.get(i).get(0));
								  								else if(datos.get(i).get(3).equals("3")) out.print("../inventario/ReporteRetiros?id=" + datos.get(i).get(0));
								  								else if(datos.get(i).get(3).equals("4")) out.print("../inventario/ReporteDevoluciones?id=" + datos.get(i).get(0));
								  								else out.print("#");
								  								out.print("\" target=\"_blank\" data-bs-toggle=\"tooltip\" data-bs-placement=\"left\" title=\"Generar reporte del registro.\">");
													  			out.print("<i class='bi bi-journal-text'></i> Generar Reporte");
													  			out.print("</a>");
								  							}
								  							
							  							}
							  							else {
							  								out.print("-");
							  							}
							  							out.print("</td>");
											  			out.print("</tr>");
											  			out.print("<div class=\"modal fade\" id=\"descript" + datos.get(i).get(0) + "\" tabindex=\"-1\" aria-labelledby=\"descript" + datos.get(i).get(0) + "Label\" aria-hidden=\"true\">");
											  			out.print("<div class=\"modal-dialog modal-dialog-scrollable\">");
											  			out.print("<div class=\"modal-content\">");
											  			out.print("<div class=\"modal-header\">");
											  			out.print("<h5 class=\"modal-title\" id=\"descript" + datos.get(i).get(0) + "Label\">Descripción del Registro ingresado por: <b class='bbig'>" + datos.get(i).get(1) + " " + datos.get(i).get(2) + "</b>" + ((datos.get(i).get(7) != null && !datos.get(i).get(7).trim().equals("")) ? (" para <b class='bbig'>" + datos.get(i).get(7) +  "</b>") : "") + "</h5>");
											  			out.print("<button type=\"button\" class=\"btn-close\" data-bs-dismiss=\"modal\" aria-label=\"Close\"></button>");
											  			out.print("</div>");
											  			out.print("<div class=\"modal-body\">");
											  			if(datos.get(i).get(3).equals("0")) {
											  				query = "SELECT Descripcion, HoraDescripcion, FechaDescripcion FROM Acciones WHERE IdInventario = '" + datos.get(i).get(0) + "' ORDER BY FechaDescripcion DESC, HoraDescripcion DESC";	
												  			ArrayList<ArrayList<String>> datosBusc = layout.getRows(query);
												  			int cantBusc = datosBusc.size();
												  			if(cantBusc > 0) {
												  				String fechaverif = "", horaverif = "";
												  				for(int j = 0; j < cantBusc; j++){
												  					if(!fechaverif.equals(datosBusc.get(j).get(2))) {
												  						if(datosBusc.get(j).get(2) != null && !datosBusc.get(j).get(2).trim().equals("")) {
												  							String nwfecha = datosBusc.get(j).get(2);
													  						nwfecha = nwfecha.charAt(6) + "" + nwfecha.charAt(7) + "-" + nwfecha.charAt(4) + "" + nwfecha.charAt(5) + "-" + nwfecha.charAt(0) + "" + nwfecha.charAt(1) + "" + nwfecha.charAt(2) + "" + nwfecha.charAt(3); 
													  						out.print("<b>[" + nwfecha + "]</b>");
												  						}
												  						else {
												  							out.print("<b>[N/A]</b>");
												  						}
												  						out.print("<ul>");
												  					}
												  					String hora = datosBusc.get(j).get(1);
												  					String date = datosBusc.get(j).get(1).charAt(0) + "" + datosBusc.get(j).get(1).charAt(1);
												  					int canthora = Integer.parseInt(date);
														  			if(canthora > 12) {
														  				canthora = canthora - 12;
														  				String conj = canthora < 10 ? "0" + canthora : "" + canthora;
													  					hora = conj.charAt(0) + "" + conj.charAt(1) + "" + hora.charAt(2) + "" + hora.charAt(3) + "" + hora.charAt(4) + " p.m.";
														  			}
														  			else hora = hora.charAt(0) + "" + hora.charAt(1) + "" + hora.charAt(2) + "" + hora.charAt(3) + "" + hora.charAt(4) + " a.m.";
														  			if(!horaverif.trim().equals("") && !horaverif.equals(hora)) {
														  				out.print("</li><br>");
														  			}
														  			if(!horaverif.equals(hora)) {
														  				out.print("<li><b>[" + hora + "]</b><br>" + datosBusc.get(j).get(0) + "");
														  			}
														  			else {
														  				out.print("<br>" + datosBusc.get(j).get(0) + "");
														  			}
											  						fechaverif = datosBusc.get(j).get(2);
														  			if((j+1) == cantBusc || !fechaverif.equals(datosBusc.get(j+1).get(2))) {
														  				out.print("</ul>");
														  				horaverif = "";
														  			}
														  			else horaverif = hora;
												  				}
												  			}
												  			else {
												  				out.print("Ha ocurrido un error");
												  			}
											  			}
											  			else if(datos.get(i).get(3).equals("5")) {
											  				query = "SELECT Descripcion FROM Acciones WHERE IdInventario = '" + datos.get(i).get(0) + "'";	
												  			ArrayList<String> datosBusc = layout.getRow(query);
												  			int cantBusc = datosBusc.size();
												  			if(cantBusc > 0) {
												  				out.print(datosBusc.get(0));
												  			}
												  			else {
												  				out.print("Ha ocurrido un error");
												  			}
											  			}
											  			else {
											  				query = "SELECT P.Codigo, P.Nombre, A.Cantidad, A.Agregado, A.Retirado, A.Devuelto, P.Marca, P.Modelo, P.Serie, P.ActivoFijo FROM Acciones AS A INNER JOIN Productos AS P ON A.IdProducto = P.Id WHERE A.IdInventario = '" + datos.get(i).get(0) + "'";	
												  			ArrayList<ArrayList<String>> datosProd = layout.getRows(query);
												  			if(datos.get(i).get(11) != null && !datos.get(i).get(11).trim().equals("")) {
												  				out.print("<b>Comentario añadido:</b> " + datos.get(i).get(11) + "<br><br>");
												  			}
												  			int cantidad = datosProd.size();
												  			if(cantidad == 1) out.print("Se ha manipulado el siguiente producto:<br>");
												  			else out.print("Se han manipulado los siguientes productos:<br>");
												  			out.print("<ul>");
												  			for(int j = 0; j < cantidad; j++) {
												  				out.print("<li><b>");
												  				out.print(datosProd.get(j).get(1));
												  				if(datosProd.get(j).get(6) != null && !datosProd.get(j).get(6).trim().equals("-") && !datosProd.get(j).get(6).trim().equals("")) {
												  					out.print(", Marca: " + datosProd.get(j).get(6));
												  				}
												  				if(datosProd.get(j).get(7) != null && !datosProd.get(j).get(7).trim().equals("-") && !datosProd.get(j).get(7).trim().equals("")) {
												  					out.print(", Modelo: " + datosProd.get(j).get(7));
												  				}
												  				if(datosProd.get(j).get(8) != null && !datosProd.get(j).get(8).trim().equals("-") && !datosProd.get(j).get(8).trim().equals("")) {
												  					out.print(", Serie: " + datosProd.get(j).get(8));
												  				}
												  				if(datosProd.get(j).get(9) != null && !datosProd.get(j).get(9).trim().equals("-") && !datosProd.get(j).get(9).trim().equals("")) {
												  					out.print(", Activo Fijo: " + datosProd.get(j).get(9));
												  				}
										  						out.print("</b></li>");
												  				out.print("<ul>");
												  				if(datos.get(i).get(3).equals("1")) out.print("<li>Cantidad ingresada: <b>" + datosProd.get(j).get(2) + "</b></li>");
												  				else if(datos.get(i).get(3).equals("2")) {
												  					out.print("<li>Cantidad anterior: <b>" + datosProd.get(j).get(2) + "</b></li>");
												  					out.print("<li>Cantidad ingresada: <b>" + datosProd.get(j).get(3) + "</b></li>");
												  					int agregado = Integer.parseInt(datosProd.get(j).get(2)) + Integer.parseInt(datosProd.get(j).get(3));
												  					out.print("<li>Cantidad Resultante: <b>" + agregado + "</b></li>");
												  				}
												  				else if(datos.get(i).get(3).equals("3")) {
												  					out.print("<li>Cantidad anterior: <b>" + datosProd.get(j).get(2) + "</b></li>");
												  					out.print("<li>Cantidad retirada: <b>" + datosProd.get(j).get(4) + "</b>");
												  					if(datosProd.get(j).get(5) != null && !datosProd.get(j).get(5).trim().equals("") && !datosProd.get(j).get(5).equals("0")) {
												  						out.print(" - (" + datosProd.get(j).get(5) + " devuelto" + (datosProd.get(j).get(5).equals("1") ? "" : "s")  + ")");
												  					}
												  					out.print("</li>");
												  					int retirado = Integer.parseInt(datosProd.get(j).get(2)) - Integer.parseInt(datosProd.get(j).get(4));
												  					out.print("<li>Cantidad Resultante: <b>" + retirado + "</b></li>");
												  				}
												  				else if(datos.get(i).get(3).equals("4")) {
												  					out.print("<li>Cantidad anterior: <b>" + datosProd.get(j).get(2) + "</b></li>");
												  					out.print("<li>Cantidad devuelta: <b>" + datosProd.get(j).get(5) + "</b></li>");
												  					int devuelto = Integer.parseInt(datosProd.get(j).get(2)) + Integer.parseInt(datosProd.get(j).get(5));
												  					out.print("<li>Existencias resultantes: <b>" + devuelto + "</b></li>");
												  				}
												  				else {
												  					out.print("<li>N/A</li>");
												  				}
												  				out.print("</ul>");
												  				if((j + 1) < cantidad) {
												  					out.print("<br>");
												  				}
												  			}
											  			}
											  			out.print("</ul>");
											  			out.print("</div>");
											  			out.print("</div>");
											  			out.print("</div>");
											  			out.print("</div>");
											  			out.print("</tr>");
										  			}
										  			registro = datos.get(i).get(0);
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
				//Fin del Contenido
				out.print(layout.footer(2));
				if(session.getAttribute("succViewB") != null && !session.getAttribute("succViewB").equals("")) {
					out.print(val.mostrarToastr("Éxito", "Proceso realizado correctamente."));
					session.setAttribute("succViewB", null);
				}
			%>
	</body>
</html>
