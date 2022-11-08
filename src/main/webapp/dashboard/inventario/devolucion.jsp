<%! @SuppressWarnings("unchecked") %>
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
			query = "SELECT DevolverProducto FROM Accesos WHERE IdUsuario = '" + iduser + "'";
			ArrayList<String> accesos = val.getRow(query);
			if(accesos.size() == 0) {
				accesos.add(0, "0");
			}
			if(accesos.get(0).equals("0")) {
				out.print("<script type=\"text/javascript\">");
				out.print("window.location.href = \"../inventario\"");
				out.print("</script>");
			}
			//Fin
			ArrayList<ArrayList<String>> datos = null;
			if(session.getAttribute("tabla_bit_min") != null) {
				datos = (ArrayList<ArrayList<String>>) session.getAttribute("tabla_bit_min");
				session.setAttribute("tabla_bit_min", null);
			}
			ArrayList<ArrayList<String>> productos = null;
			int cantprod = 0;
			if(session.getAttribute("tabla_productos") != null) {
				productos = (ArrayList<ArrayList<String>>) session.getAttribute("tabla_productos");
				cantprod = productos.size();
			}
			int mayor = 0;
			for (int i = 0; i < cantprod; i++) { 
				int nuevo = Integer.parseInt(productos.get(i).get(2)) - Integer.parseInt(productos.get(i).get(3));
				if(nuevo > 0) mayor++;
			}
			Date dNow = new Date();
			SimpleDateFormat Fsdf = new SimpleDateFormat ("YYYY-MM-dd");
			String max = Fsdf.format(dNow);
			//Datos de devolución
			String comentario = "", search = "", searchacc = "", inicio = "", fin = "";
			if (session.getAttribute("retdevsearch") != null && !session.getAttribute("retdevsearch").equals("")) {
				search = (String) session.getAttribute("retdevsearch");
				session.setAttribute("retdevsearch", null);
			}
			if (session.getAttribute("retdevsearchacc") != null && !session.getAttribute("retdevsearchacc").equals("")) {
				searchacc = (String) session.getAttribute("retdevsearchacc");
				session.setAttribute("retdevsearchacc", null);
			}
			if (session.getAttribute("retdevinicio") != null && !session.getAttribute("retdevinicio").equals("")) {
				inicio = (String) session.getAttribute("retdevinicio");
				session.setAttribute("retdevinicio", null);
			}
			if (session.getAttribute("retdevfin") != null && !session.getAttribute("retdevfin").equals("")) {
				fin = (String) session.getAttribute("retdevfin");
				session.setAttribute("retdevfin", null);
			}
			if (session.getAttribute("retdevcomenting") != null && !session.getAttribute("retdevcomenting").equals("")) {
				comentario = (String) session.getAttribute("retdevcomenting");
			}
		%>
        <div id="panPrincipal" class="col col-md-12">
            <div class="row">
                <div class="col col-sm-12">
                    <a class="btn btn-regresar" href="./" role="button" data-bs-toggle="tooltip" data-bs-placement="left" title="Regresar al sitio anterior.">
                        <i class="bi bi-chevron-double-left"></i>
                        REGRESAR
                    </a>
                    <p class="encabezadodash">
                        <a href="./">INVENTARIO DE ACCESORIOS/DISPOSITIVOS</a>
                        <span class="subencab">
                            / DEVOLUCIONES</span>
                    </p>
                    <div id="principalInvent">
                        <%
			 				if(cantprod > 0) {
			 					if(mayor > 0) {
			 						out.print("<div id='ingfin1' class='btn-group col col-sm-12'>");
									out.print("<a class='btn btn-guardar' onclick='confirmform(event, frmdevolucion, \"¿Esta seguro de Devolver al sistema esta cantidad de Accesorios/Dispositivos?\")' data-bs-toggle='tooltip' data-bs-placement='bottom' title='Terminar todo el proceso y guardar.'>DEVOLVER Y GUARDAR</a>");
									out.print("<hr>");
									out.print("</div>");
			 					}
			 				}
			 			%>
                        <div class="row">
                        	<%
                        		if(cantprod > 0) {
					 			%>	
	                                <div class="col-12 col-md-6 retirarinf forminit">
	                                    <div id="divInv" class="col-12 col-lg-12">
	                                    	<div class="cerrar" data-bs-toggle='tooltip' data-bs-placement='left' title='Quitar selección.'>
	                                    		<a class="puntero" onclick="confirmlink('¿Esta seguro de Quitar está Selección?', './guardardevolucion')"><i class="bi bi-x-lg"></i></a>
	                                    	</div>
	                                    	<%
	                                    		String idinventario = session.getAttribute("id_inventario").toString();
	                                    		query = "SELECT I.UsuarioExterno, (U.Nombres || ' ' || U.Apellidos) AS Completo FROM Inventarios AS I INNER JOIN Usuarios AS U ON I.IdUsuario = U.Id WHERE I.Id = '" + idinventario + "' ORDER BY I.Id ASC LIMIT 1";
	                                    		ArrayList<String> persona = layout.getRow(query);
	                                    		String bdpersona = "-";
	                                    		if(persona.get(0) != null && !persona.get(0).trim().equals("")) bdpersona = persona.get(0);
								  				else if(persona.get(1) != null && !persona.get(1).trim().equals("")) bdpersona = persona.get(1);
	                                    	%>
	                                        <p class='titproduct center'>Accesorios/Dispositivos a Devolver de <% out.print(bdpersona); %></p>
                                            <%
												out.print("<hr>");
		                                   	%>
		                              		<div class="expli-just">
			                                	<p>
			                                		Ingrese el comentario si es necesario, para luego definir la cantidad de accesorios/dispositivos que serán devueltos. Al terminar presionar "DEVOLVER Y GUARDAR".
			                                	</p>
		                               		</div>
	                                        <form name="frmdevolucion" id="frmdevolucion" class="col col-sm-12" action="guardardevolucion" method="POST" onsubmit="confirmform(event, this, '¿Esta seguro de Devolver al sistema esta cantidad de Accesorios/Dispositivos?')">
	                                            <%
		                                          	//Errores
				               						if(session.getAttribute("mensdevinventario") != null && !session.getAttribute("mensdevinventario").equals("")) {
				               							out.print("<div class='margsup'></div>");
				               							out.print(val.mostrarAlert(0, session.getAttribute("mensdevinventario") .toString()));
				               							out.print("<div class='margabajo'></div>");
				               							session.setAttribute("mensdevinventario", null);
				               						}
												%>
												<div class="col col-sm-12 inpform">
													<label for="txtComentario" class="form-label">Ingrese un Comentario <i id="cursiva">(Opcional)</i></label>
				                                    <div class="input-group">
				                                        <span class="input-group-text" id="lblComentario">
				                                            <i class="bi bi-chat-left-dots-fill"></i>
				                                        </span>
				                                        <input name="txtComentario" id="txtComentario" aria-describedby="txtComentario" placeholder="Comentario" class="form-control"
															autocomplete="off" type="text" spellcheck="false" maxlength="500"
															value="<%= comentario %>" />
				                                    </div>
												</div>
												<%
				               						if(mayor > 0) {
				               							out.print("<div id='ingfin3' class='btn-group col col-sm-12'>");
														out.print("<button type='submit' class='btn btn-guardar' data-bs-toggle='tooltip' data-bs-placement='left' title='Terminar el proceso y guardar.'>DEVOLVER Y GUARDAR</button>");
														out.print("</div>");
				               						}
                                               %>
                                                <div class="containTable_Min">
                                                    <table id="registrosmin"  data-page-length="5"class="table striped table-hover table-bordered table-sm align-middle">
														<thead class="tableheader">
                                                            <tr>
                                                                <th scope="col">ACCESORIO/DISPOSITIVO</th>
                                                                <th scope="col">CANTIDAD RETIRADA</th>
                                                                <th scope="col" data-orderable="false">CANTIDAD A DEVOLVER</th>
                                                            </tr>
                                                        </thead>
                                                        <tbody>	
                                                            <% 
																try { 
																	int mostrar = 0;
																	for (int i = 0; i < cantprod; i++) { 
																		out.print("<tr>");
																		out.print("<td>");
																		out.print(productos.get(i).get(1));
																		if(productos.get(i).get(4) != null && !productos.get(i).get(4).trim().equals("-") && !productos.get(i).get(4).trim().equals("")) {
														  					out.print(", Marca: " + productos.get(i).get(4));
														  				}
														  				if(productos.get(i).get(5) != null && !productos.get(i).get(5).trim().equals("-") && !productos.get(i).get(5).trim().equals("")) {
														  					out.print(", Modelo: " + productos.get(i).get(5));
														  				}
														  				if(productos.get(i).get(6) != null && !productos.get(i).get(6).trim().equals("-") && !productos.get(i).get(6).trim().equals("")) {
														  					out.print(", Serie: " + productos.get(i).get(6));
														  				}
														  				if(productos.get(i).get(7) != null && !productos.get(i).get(7).trim().equals("-") && !productos.get(i).get(7).trim().equals("")) {
														  					out.print(", Activo Fijo: " + productos.get(i).get(7));
														  				}
														  				if((productos.get(i).get(8) != null && !productos.get(i).get(8).trim().equals(""))) {
																  			out.print("<br />Comentario: <span data-bs-toggle=\"modal\" data-bs-target=\"#coment" + productos.get(i).get(0) + "\"><a class=\"aloneopc\" data-bs-toggle=\"tooltip\" type=\"button\" data-bs-placement=\"bottom\" title=\"Click para ver el comentario del accesorio/dispositivo.\"><i class=\"bi bi-eye-fill\"></i></a></span>");
															  			}
														  				out.print("</td>");
																		if(productos.get(i).get(3) != null && !productos.get(i).get(3).trim().equals("")) {
																			int nuevo = Integer.parseInt(productos.get(i).get(2)) - Integer.parseInt(productos.get(i).get(3));
																			out.print("<td>" + nuevo + "</td>");
																			mostrar = nuevo;
																		}
																		else {
																			out.print("<td>" + productos.get(i).get(2) + "</td>");
																			mostrar = Integer.parseInt(productos.get(i).get(2));
																		}
																		out.print("<td>");
																		out.print("<input name='txtCantidad' id='txtCantidad' aria-describedby='txtCantidad' placeholder='#' class='form-control'");
																		out.print("autocomplete='off' type='number' min='0' max='" + mostrar + "' spellcheck='false' maxlength='5' pattern='[0-9]{1,5}' title='Mínimo 1 y máximo 5 dígitos' required" + (mostrar == 0 ? " readonly" : "") + "");
																		out.print("value='0' />");
																		out.print("</td>");
																		out.print("</tr>");
																		if (productos.get(i).get(8) != null && !productos.get(i).get(8).trim().equals("")) {
																  			out.print("<div class=\"modal fade\" id=\"coment" + productos.get(i).get(0) + "\" tabindex=\"-1\" aria-labelledby=\"coment" + productos.get(i).get(0) + "Label\" aria-hidden=\"true\">");
																  			out.print("<div class=\"modal-dialog modal-dialog-scrollable\">");
																  			out.print("<div class=\"modal-content\">");
																  			out.print("<div class=\"modal-header\">");
																  			out.print("<h5 class=\"modal-title\" id=\"coment" + productos.get(i).get(0) + "Label\">Comentario del Accesorio/Dispositivo: <b class='bbig'>" + productos.get(i).get(1) + "</b></h5>");
																  			out.print("<button type=\"button\" class=\"btn-close\" data-bs-dismiss=\"modal\" aria-label=\"Close\"></button>");
																  			out.print("</div>");
																  			out.print("<div class=\"modal-body\">");
																  			out.print(productos.get(i).get(8));
																  			out.print("</div>");
																  			out.print("</div>");
																  			out.print("</div>");
																  			out.print("</div>");
														  				}
																	}
																}
																catch (Exception e) {
																	out.print("<tr>");
																	out.print("<td colspan=\"3\">No se encontraron elementos para mostrar.</td>");
																	out.print("</tr>");
																}
															%>
	                                                       </tbody>
	                                                   </table>
	                                               </div>
	                                           </form>
	                                       </div>
	                                   </div>
	                               <%
						 				}
				 					%>
                              </div>
                            <%
				 				if(cantprod > 0) {
			 						out.print("<div class='col-12 col-md-6 margsup-min'>");
			 						out.print("<div id='divInv' class='col-11 col-lg-11'>");	
				 				}
				 				else {
				 					out.print("<div class='divcentrado col col-12 col-lg-10'>");
			 						out.print("<div class='col col-12 col-sm-12'>");
				 				}
			 				%>
                            <form class="col col-sm-12" action="./bitacora_min" method="POST">
                                <div class="row grp-buscar">
                                    <div class="col col-12 <%= cantprod > 0 ? "col-sm-12" : "col-sm-8 col-md-8 col-lg-8" %>">
                                    	<div class="row">
	                                    	<table class="col col-12 <%= cantprod > 0 ? "col-sm-12" : "col-sm-6" %>">
	                                    		<tbody>
		                                    		<tr class="col col-12">
			                           					<td class="col col-12 col-sm-6">
			                           						<input name="txtSearchUser" id="txtSearchUser" aria-describedby="txtSearchUser" placeholder="Por requerimiento, personal y ubicación." class="form-control marginf"
																autocomplete="off" type="text" spellcheck="false" maxlength="100" title="Por requerimiento, personal y ubicación."
																value="<%= search %>" />
			                                    		</td>
			                           				</tr>
			                           				<tr class="col col-12">
			                           					<td class="col col-12 col-sm-6">
			                           						<input name="txtSearchAcc" id="txtSearchAcc" aria-describedby="txtSearchAcc" placeholder="Por accesorio/dispositivo." class="form-control marginf"
																autocomplete="off" type="text" spellcheck="false" maxlength="100" title="Por accesorio/dispositivo."
																value="<%= searchacc %>" />
														</td>
													</tr>
	                                    		</tbody>
	                                    	</table>
	                                    	<table class="col col-12 <%= cantprod > 0 ? "col-sm-12 marginf" : "col-sm-6" %>">
	                                    		<tbody>
		                                    		<tr class="col col-12">
			                              				<td class="col col-3 col-sm-3"><p>De:</p></td>
			                              				<td class="col col-9 col-sm-9">
			                              					<input autocomplete="off" type="date" value="<%= inicio %>" class="dtfecha" id="dtInicio" name="dtInicio" min="2020-01-01" max="<%= max %>" onkeydown="return false">
			                              				</td>
			                              			</tr>
			                              			<tr class="col col-12">
			                              				<td class="col col-3 col-sm-3"><p>Hasta:</p></td>
			                              				<td class="col col-9 col-sm-9">
			                              					<input autocomplete="off" type="date" value="<%= fin %>" class="dtfecha" id="dtFin" name="dtFin" min="2020-01-01" max="<%= max %>" onkeydown="return false">
			                                           </td>
			                              			</tr>
	                                    		</tbody>
	                                    	</table>
	                                    </div>	
                                    </div>
                                    <div class="col col-12 <%= cantprod > 0 ? "col-sm-12 searchdev" : "col-sm-4 col-md-4 col-lg-4" %>"> 
                                        <table class="col col-12">
		                               		<tbody>
		                               			<tr class="col col-12">
		                               				<td class="col-6">
						                                <button type="submit" class="btn btn-ingresar" data-bs-toggle="tooltip" data-bs-placement="bottom" title="Buscar registros.">
				                                                <i class="bi bi-search"></i>
				                                            BUSCAR RETIROS
				                                        </button>
						                            </td>
						                        </tr>
						                      	<tr class="col col-12">
						                            <td class="col-6">
						                            	<a id="btnlimpiar" class="btn btn-ingresar btnclean-bsup puntero" data-bs-toggle="tooltip" data-bs-placement="bottom" title="Borrar los datos ingresados.">
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
            						if(session.getAttribute("mensbitnventario") != null && !session.getAttribute("mensbitnventario").equals("")) {
            							out.print("<div class='margsup'></div>");
            							out.print(val.mostrarAlert(0, session.getAttribute("mensbitnventario") .toString()));
            							session.setAttribute("mensbitnventario", null);
            						}
                                 %>
                            </form>
                            <div class="expli">
                            	<p>
                            		Realice la búsqueda y seleccione los registros (Retiros realizados con antelación) que necesite dándole a "Seleccionar".
                            	</p>
                            </div>
                            <%
								if(session.getAttribute("existablabitmin") != null)
								{
									session.setAttribute("existablabitmin", null);
							%>
								<div class="containTable_Min col col-12">
						        	<table id="registrosbit" data-page-length="<%= cantprod > 0 ? "5" : "10" %>" class="table striped table-hover table-bordered table-sm align-middle">
										<thead class="tableheader">
	                                        <tr>
	                                        	<th scope="col">FECHA</th>
	                                        	<th scope="col" data-orderable="false">PROCESO A UTILIZAR</th>
	                                        	<th scope="col">CASO</th>
	                                        	<th scope="col" data-orderable="false">DESCRIPCIÓN</th>
	                                            <th scope="col">PERSONAL</th>
	                                            <th scope="col">UBICACIÓN</th>
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
												  			if(!registro.equals(datos.get(i).get(0))){
												  				query = "SELECT P.Codigo, P.Nombre, A.Cantidad, A.Agregado, A.Retirado, A.Devuelto, P.Marca, P.Modelo, P.Serie, P.ActivoFijo FROM Acciones AS A INNER JOIN Productos AS P ON A.IdProducto = P.Id WHERE A.IdInventario = '" + datos.get(i).get(0) + "'";	
													  			ArrayList<ArrayList<String>> datosProd = layout.getRows(query);
													  			int cantidad = datosProd.size();
													  			int verificar = 0;
													  			for(int j = 0; j < cantidad; j++) {
													  				if(datos.get(i).get(3).equals("3")) {
													  					int totres = Integer.parseInt(datosProd.get(j).get(4)) - Integer.parseInt(datosProd.get(j).get(5));
													  					if(totres == 0) {
													  						verificar++;
													  					}
													  				}
													  			}
													  			if(verificar != cantidad) {
														  			out.print("<tr>");
													  				String nwpersona = "-";
														  			//Arreglando la fecha
														  			String fecha = datos.get(i).get(4);
														  			fecha = fecha.charAt(6) + "" + fecha.charAt(7) + "-" + fecha.charAt(4) + "" + fecha.charAt(5) + "-" + fecha.charAt(0) + "" + fecha.charAt(1) + "" + fecha.charAt(2) + "" + fecha.charAt(3); 
														  			out.print("<td><span class='oculto'>" + datos.get(i).get(4) + "</span>" + fecha + "</td>");;
													  				//Botón para seleccionar
													  				out.print("<td>");
																	out.print("<a class='btn-agregar puntero' onclick=\"confirmlink('¿Esta seguro de Seleccionar esta lista de Retiro?', 'devolver?id=" + datos.get(i).get(0) + "')\" data-bs-toggle='tooltip' data-bs-placement='right' title='Utilizar la información de este registro.'><div><i class='bi bi-check-lg'></i> Seleccionar</div></a>");
																	out.print("</td>");
														  			if(datos.get(i).get(1) != null && !datos.get(i).get(1).trim().equals("")) nwpersona = datos.get(i).get(1);
													  				else if(datos.get(i).get(8) != null && !datos.get(i).get(8).trim().equals("")) nwpersona = datos.get(i).get(8);
														  			out.print("<td>" + ((datos.get(i).get(9) != null && !datos.get(i).get(9).trim().equals("")) && ((datos.get(i).get(10) != null && !datos.get(i).get(10).trim().equals(""))) ? "No. " + datos.get(i).get(9) + " / " + datos.get(i).get(10) : "-") + "</td>");
														  			out.print("<td>");
														  			out.print("<div class=\"dropdown\">");
														  			out.print("<span class=\"puntero\" data-bs-toggle=\"modal\" data-bs-target=\"#descript" + datos.get(i).get(0) + "\"><a class=\"aloneopc\" data-bs-toggle=\"tooltip\" data-bs-placement=\"right\" title=\"Click para ver la descripción del registro.\">");
														  			out.print("<i class=\"bi bi-eye-fill\"></i>");
														  			out.print("</a></span>");
														  			out.print("</div>");
										  							out.print("</td>");
													  				out.print("<td>" + nwpersona + "</td>");
														  			out.print("<td>" + ((datos.get(i).get(2) != null && !datos.get(i).get(2).trim().equals("")) ? datos.get(i).get(2) : "-") + "</td>");
														  			out.print("</tr>");
														  			out.print("<div class=\"modal fade\" id=\"descript" + datos.get(i).get(0) + "\" tabindex=\"-1\" aria-labelledby=\"descript" + datos.get(i).get(0) + "Label\" aria-hidden=\"true\">");
														  			out.print("<div class=\"modal-dialog modal-dialog-scrollable\">");
														  			out.print("<div class=\"modal-content\">");
														  			out.print("<div class=\"modal-header\">");
														  			out.print("<h5 class=\"modal-title\" id=\"descript" + datos.get(i).get(0) + "Label\">Descripción del Registro realizado por: " + nwpersona + "</h5>");
														  			out.print("<button type=\"button\" class=\"btn-close\" data-bs-dismiss=\"modal\" aria-label=\"Close\"></button>");
														  			out.print("</div>");
														  			out.print("<div class=\"modal-body\">");
														  			if(datos.get(i).get(11) != null && !datos.get(i).get(11).trim().equals("")) {
														  				out.print("<b>Comentario añadido:</b> " + datos.get(i).get(11) + "<br><br>");
														  			}
														  			if(cantidad == 1) out.print("Se ha manipulado el siguiente accesorio/dispositivo:<br>");
														  			else out.print("Se han manipulado los siguientes accesorio/dispositivo:<br>");
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
																		if(datos.get(i).get(3).equals("3")) {
														  					out.print("<li>Cantidad anterior: <b>" + datosProd.get(j).get(2) + "</b></li>");
														  					out.print("<li>Cantidad retirada: <b>" + datosProd.get(j).get(4) + "</b>");
														  					if(datosProd.get(j).get(5) != null && !datosProd.get(j).get(5).trim().equals("")  && !datosProd.get(j).get(5).equals("0")) {
														  						out.print(" - (" + datosProd.get(j).get(5) + " devuelto" + (datosProd.get(j).get(5).equals("1") ? "" : "s")  + ")");
														  					}
														  					out.print("</li>");
														  					int retirado = Integer.parseInt(datosProd.get(j).get(2)) - Integer.parseInt(datosProd.get(j).get(4));
														  					out.print("<li>Cantidad Resultante: <b>" + retirado + "</b></li>");
														  				}
														  				else {
														  					out.print("<li>N/A</li>");
														  				}
														  				out.print("</ul>");
														  				if((j + 1) < cantidad) {
														  					out.print("<br>");
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
												  		}
														if(i == 0) {
															out.print("<tr>");
															out.print("<td colspan=\"6\">No se encontraron elementos para mostrar.</td>");
															out.print("</tr>");
														}
										  			}
										  			else {
										  				out.print("<tr>");
										  				out.print("<td colspan=\"6\">No se encontraron elementos para mostrar.</td>");
										  				out.print("</tr>");
										  			}
										  		}
										  	 	catch (Exception e) {
										  	 		out.print("<tr>");
													out.print("<td colspan=\"6\">No se encontraron elementos para mostrar.</td>");
													out.print("</tr>");
										  		}
											%>
	                                 </tbody>
	                             </table>
	                         </div>
                        <%
							}
				 			out.print("</div>");
							out.print("</div>");
			 			%>
					</div>
                 </div>
             </div>
         </div>
		<%
			//Fin del Contenido
			out.print(layout.footer(2));
			if(session.getAttribute("succDevP") != null && !session.getAttribute("succDevP").equals("")) {
				out.print(val.mostrarToastr("Éxito", session.getAttribute("succDevP").toString()));
				session.setAttribute("succDevP", null);
			}
		%>
		<script type="text/javascript">
			$("#btnlimpiar").click(function() {
				$("#txtSearchUser").val("");
				$("#txtSearchAcc").val("");
				$("#dtInicio").val("");
				$("#dtFin").val("");
			});
		</script>
	</body>
</html>
