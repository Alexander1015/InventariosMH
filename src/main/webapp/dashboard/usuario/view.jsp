<%! @SuppressWarnings("unchecked") %>
<%@page import="java.util.ArrayList"%>
<%@page import="lib.Validacion" %>
<%@page import="lib.Plantilla" %>
<%
	Plantilla layout = new Plantilla();
	Validacion val = new Validacion();
%>
<!DOCTYPE html>
<html lang="es">
	<head>
		<% out.print(layout.header("Usuario", 2)); %>
	</head>
	<body>
		<%
			String iduser = (String) session.getAttribute("id");
			out.print(layout.navbar("Usuario", iduser, (String) session.getAttribute("token"), 2));
			//Inicio del Contenido
			String query = "SELECT Defecto FROM Usuarios WHERE Id = '" + iduser + "' ORDER BY Id DESC LIMIT 1";
			ArrayList<String> usuariobd = layout.getRow(query);
			if(usuariobd.size() > 0 && usuariobd.get(0).equals("0")) {
				out.print("<script type=\"text/javascript\">");
				out.print("window.location.href = \"../perfil\"");
				out.print("</script>");
			}
			//Accesos
			query = "SELECT ReadUsuario, UpdateUsuario, DeleteUsuario, CreateUsuario, UpdateAcceso FROM Accesos WHERE IdUsuario = '" + iduser + "'";
			ArrayList<String> accesos = val.getRow(query);
			if(accesos.size() == 0) {
				accesos.add(0, "0");
				accesos.add(1, "0");
				accesos.add(2, "0");
				accesos.add(3, "0");
				accesos.add(4, "0");
			}
			if(accesos.get(0).equals("0")) {
				out.print("<script type=\"text/javascript\">");
				out.print("window.location.href = \"../usuario\"");
				out.print("</script>");
			}
			//Fin
			ArrayList<ArrayList<String>> datos = null;
			if(session.getAttribute("tablauser") != null) {
				datos = (ArrayList<ArrayList<String>>) session.getAttribute("tablauser");
				session.setAttribute("tablauser", null);
			}
			String search = "";
			if(session.getAttribute("sinputuser") != null && !session.getAttribute("sinputuser").equals("")) {
				search = (String) session.getAttribute("sinputuser");
			}
			request.getSession().setAttribute("returnuser", null);
		%>
			<div id="panPrincipal" class="col col-md-12">
			 	<div class="row">
			 		<div class="col col-sm-12">
			 			<%
			 				if(accesos.get(3).equals("1")) {
			 			%>
					 		<a class="btn btn-regresar" href="./" role="button" data-bs-toggle="tooltip" data-bs-placement="left" title="Regresar al sitio anterior.">
		                       <i class="bi bi-chevron-double-left"></i>
		                       REGRESAR
		                    </a>
		                <%
			 				}
		                %>
	                    <p class="encabezadodash">
	                        <a href="../usuario">MANEJO DE USUARIOS</a>
	                        <span class="subencab"> / CONSULTAS</span>
	                    </p>
                    <form class="col col-12 col-lg-10" action="vista" method="POST">
                        <div class="row grp-buscar">
                            <div class="col col-sm-9">
                            	<input name="txtSearch" id="txtSearch" aria-describedby="txtSearch" placeholder="Por nombre, apellido y usuario." class="form-control"
									autocomplete="off" type="text" spellcheck="false" maxlength="100"
									value="<%= search %>" title="Por nombre, apellido y usuario." />
                            </div>
                            <div class="col col-sm-3 col-md-3">
                            	<table class="col col-12">
                               		<tbody>
                               			<tr class="col col-12">
                               				<td class="col-6">
				                                <button type="submit" class="btn btn-ingresar" data-bs-toggle="tooltip" data-bs-placement="bottom" title="Buscar registro.">
				                                    <i class="bi bi-search"></i> BUSCAR
				                                </button>
				                            </td>
				                            <td class="col-6">
				                            	<a href="../borrar?view=Usuario" class="btn btn-ingresar btnclean-b" data-bs-toggle="tooltip" data-bs-placement="bottom" title="Borrar los datos ingresados.">
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
							if(session.getAttribute("mensviewuser") != null && !session.getAttribute("mensviewuser").equals("")) {
								out.print("<div class='margsup'></div>");
								out.print(val.mostrarAlert(0, session.getAttribute("mensviewuser") .toString()));
								session.setAttribute("mensviewuser", null);
							}
	                      	//Errores de accesos/permisos
							if(session.getAttribute("mensacces") != null && !session.getAttribute("mensacces").equals("")) {
								out.print("<div class='margsup'></div>");
								out.print(val.mostrarAlert(0, session.getAttribute("mensacces") .toString()));
								session.setAttribute("mensacces", null);
							}
                        %>
                        <div class="expli">
	                       	<p>
                       			Realice una búsqueda de los usuarios que gestiona el sistema con el objetivo de controlar la información que estos contienen, además de los accesos a las diferentes secciones.
	                       	</p>
                       	</div>
                    </form>
                     <%
						if(session.getAttribute("existablauser") != null)
						{
							session.setAttribute("existablauser", null);
					%>
					<div class="containTable col col-12 col-lg-11">
						<table id="registros" data-page-length="10" class="table table-hover table-bordered table-sm align-middle">
							<thead class="tableheader">
						   		<tr>
								     <th scope="col">PERSONAL</th>
								     <th scope="col">USUARIO</th>
								     <%
								     	if(accesos.get(1).equals("1") || accesos.get(2).equals("1") || accesos.get(4).equals("1")) {
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
									  			out.print("<td>" + datos.get(i).get(1) + " " + datos.get(i).get(2) + "</td>");
									  			out.print("<td>" + datos.get(i).get(3) + "</td>");
									  			if(accesos.get(1).equals("1") || accesos.get(2).equals("1") || accesos.get(4).equals("1")) {
										  			out.print("<td>");
										  			if(iduser.equals(datos.get(i).get(0))) {
										  				out.print("-");
										  			}
										  			else {
										  				query = "SELECT Id FROM Inventarios WHERE IdUsuario = '" + datos.get(i).get(0) + "'";
														ArrayList<String> evitar = val.getRow(query);
														//Botones
														String btnmodify = "<a class=\"aloneopc\" href=\"guardar?id=" + datos.get(i).get(0) + "&return=1\" data-bs-toggle=\"tooltip\" data-bs-placement=\"left\" title=\"Modificar registro.\"><i class=\"bi bi-pencil-square\"></i></a>";
										  				String btndelete = "<a class=\"aloneopc-delete puntero\" onclick=\"confirmlink('¿Esta seguro de Eliminar este Usuario?', 'eliminar?id=" + datos.get(i).get(0) + "');\" data-bs-toggle=\"tooltip\" data-bs-placement=\"left\" title=\"Eliminar registro.\"><i class=\"bi bi-trash\"></i></a>";
														String btnaccess = "<a class=\"aloneopc\" href=\"access?id=" + datos.get(i).get(0) + "\" data-bs-toggle=\"tooltip\" data-bs-placement=\"left\" title=\"Editar los accesos del usuario.\"><i class=\"bi bi-key-fill\"></i></a>";
										  				//Verificar los accesos
														if(accesos.get(1).equals("1") && accesos.get(2).equals("0") && accesos.get(4).equals("0")) {
										  					out.print(btnmodify);
										  				}
										  				else if(accesos.get(1).equals("0") && accesos.get(2).equals("1") && accesos.get(4).equals("0")) {
										  					out.print(btndelete);
										  				}
										  				else if(accesos.get(1).equals("0") && accesos.get(2).equals("0") && accesos.get(4).equals("1")) {
										  					out.print(btnaccess);
										  				}
										  				else if(accesos.get(1).equals("1") && accesos.get(4).equals("0") && (datos.get(i).get(0).equals("1") || datos.get(i).get(0).equals(session.getAttribute("id")))) {
										  					out.print(btnmodify);
										  				}
										  				else if(accesos.get(1).equals("0") && accesos.get(4).equals("1") && (datos.get(i).get(0).equals("1") || datos.get(i).get(0).equals(session.getAttribute("id")))) {
										  					out.print(btnaccess);
										  				}
										  				else if(evitar.size() > 0 && accesos.get(1).equals("0")) {
										  					out.print(btnaccess);
										  				}
										  				else {
										  					out.print("<div class=\"dropdown\">");
												  			out.print("<button class=\"btn dropdown-toggle\" type=\"button\" id=\"dmbAcciones\" data-bs-toggle=\"dropdown\" aria-expanded=\"false\"></button>");
												  			out.print("<ul class=\"dropdown-menu dropdown-menu-lg-end opcOtrAcciones\" aria-labelledby=\"dmbAcciones\">");
												  			if(datos.get(i).get(0).equals((String)session.getAttribute("id"))){
												  				out.print("<li><a class=\"dropdown-item btn-interno a-superior\" href=\"guardar?id=" + datos.get(i).get(0) + "&return=1\" data-bs-toggle=\"tooltip\" data-bs-placement=\"left\" title=\"Modificar registro.\">MODIFICAR <i class=\"bi bi-pencil-square\"></i></a></li>");
												  				out.print("<li><a class=\"dropdown-item btn-interno a-inferior\" href=\"access?id=" + datos.get(i).get(0) + "\" data-bs-toggle=\"tooltip\" data-bs-placement=\"left\" title=\"Editar los accesos del usuario.\">ACCESOS <i class=\"bi bi-key-fill\"></i></a></li>");
												  			}
												  			else {
												  				if(accesos.get(1).equals("1")) out.print("<li><a class=\"dropdown-item btn-interno a-superior\" href=\"guardar?id=" + datos.get(i).get(0) + "&return=1\" data-bs-toggle=\"tooltip\" data-bs-placement=\"left\" title=\"Modificar registro.\">MODIFICAR <i class=\"bi bi-pencil-square\"></i></a></li>");
																if(evitar.size() == 0) {
																	if(accesos.get(4).equals("1")) out.print("<li><a class=\"dropdown-item btn-interno" + (accesos.get(1).equals("0") ? " a-superior" : (accesos.get(2).equals("0") ? " a-inferior" : "")) + "\" href=\"access?id=" + datos.get(i).get(0) + "\" data-bs-toggle=\"tooltip\" data-bs-placement=\"left\" title=\"Editar los accesos del usuario.\">ACCESOS <i class=\"bi bi-key-fill\"></i></a></li>");
																	if(accesos.get(2).equals("1")) out.print("<li><a class=\"dropdown-item btn-interno-eliminar a-inferior puntero\" onclick=\"confirmlink('¿Esta seguro de Eliminar este Usuario?', 'eliminar?id=" + datos.get(i).get(0) + "');\" data-bs-toggle=\"tooltip\" data-bs-placement=\"left\" title=\"Eliminar registro.\">ELIMINAR <i class=\"bi bi-trash\"></i></a></li>");
																}
																else {
																	out.print("<li><a class=\"dropdown-item btn-interno a-inferior\" href=\"access?id=" + datos.get(i).get(0) + "\" data-bs-toggle=\"tooltip\" data-bs-placement=\"left\" title=\"Editar los accesos del usuario.\">ACCESOS <i class=\"bi bi-key-fill\"></i></a></li>");
																}
												  			}
												  			out.print("</ul>");
												  			out.print("</div>");
										  				}
										  			}
						  							out.print("</td>");
									  			}
									  			out.print("</tr>");
											}
											if(i == 0) {
												out.print("<tr>");
												out.print("<td colspan=\"3\">No se encontraron elementos para mostrar.</td>");
												out.print("</tr>");
											}
									  	}
									  	else {
											out.print("<tr>");
											out.print("<td colspan=\"3\">No se encontraron elementos para mostrar.</td>");
											out.print("</tr>");
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
	              		<%
							}
						%>
			 		</div>
			 	</div>
			 </div>
		<%
			//Fin del Contenido
			out.print(layout.footer(2));
			if(session.getAttribute("succViewU") != null && !session.getAttribute("succViewU").equals("")) {
				out.print(val.mostrarToastr("Éxito", "Proceso realizado correctamente."));
				session.setAttribute("succViewU", null);
			}
		%>
	</body>
</html>