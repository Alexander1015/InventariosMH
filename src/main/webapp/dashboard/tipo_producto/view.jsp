<%! @SuppressWarnings("unchecked") %>
<%@page import="java.util.ArrayList"%>
<%@page import="lib.Validacion"%>
<%@page import="lib.Plantilla"%>
<%
	Plantilla layout = new Plantilla();
	Validacion val = new Validacion();
%>
<!DOCTYPE html>
<html lang="es">
	<head>
		<%
			out.print(layout.header("Tipos de Productos", 2)); 
		%>
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
			query = "SELECT ReadTipoProducto, UpdateTipoProducto, DeleteTipoProducto, CreateTipoProducto FROM Accesos WHERE IdUsuario = '" + iduser + "'";
			ArrayList<String> accesos = val.getRow(query);
			if(accesos.size() == 0) {
				accesos.add(0, "0");
				accesos.add(1, "0");
				accesos.add(2, "0");
				accesos.add(3, "0");
			}
			if(accesos.get(0).equals("0")) {
				out.print("<script type=\"text/javascript\">");
				out.print("window.location.href = \"../tipo_producto\"");
				out.print("</script>");
			}
			//Fin
			ArrayList<ArrayList<String>> datos = null;
			if(session.getAttribute("tablatipo") != null) {
				datos = (ArrayList<ArrayList<String>>) session.getAttribute("tablatipo");
				session.setAttribute("tablatipo", null);
			}
			String search = "";
			if(session.getAttribute("sinputtipo") != null && !session.getAttribute("sinputtipo").equals("")) {
				search = (String) session.getAttribute("sinputtipo");
			}
			request.getSession().setAttribute("returntipo", null);
		%>
		<div id="panPrincipal" class="col col-md-12">
			<div class="row">
				<div class="col col-sm-12">
					<a class="btn btn-regresar" href="<%= accesos.get(3).equals("1") ? "./" : "../inventario" %>" role="button" data-bs-toggle="tooltip" data-bs-placement="left" title="Regresar al sitio anterior.">
						<i class="bi bi-chevron-double-left"></i> REGRESAR
					</a>
					<p class="encabezadodash">
						<a href="../inventario">INVENTARIO DE ACCESORIOS/DISPOSITIVOS</a>
						<span class="subencab"> / <a href="./">
							CLASIFICACIÓN DE LOS ACCESORIOS/DISPOSITIVOS
							</a> / CONSULTAS
						</span>
					</p>
					<form class="col col-sm-12 col-lg-10" action="vista" method="POST">
						<div class="row grp-buscar">
							<div class="col col-12 col-sm-9 col-md-9">
								<input name="txtSearch" id="txtSearch" aria-describedby="txtSearch" placeholder="Por clasificación de accesorios/dispositivos ó simbología." class="form-control"
									autocomplete="off" type="text" spellcheck="false" maxlength="100"
									value="<%= search %>" title="Por clasificación de accesorios/dispositivos ó simbología."/>
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
				                            	<a href="../borrar?view=Clasificacion" class="btn btn-ingresar btnclean-b" data-bs-toggle="tooltip" data-bs-placement="bottom" title="Borrar los datos ingresados.">
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
							if(session.getAttribute("mensviewtipo") != null && !session.getAttribute("mensviewtipo").equals("")) {
								out.print("<div class='margsup'></div>");
								out.print(val.mostrarAlert(0, session.getAttribute("mensviewtipo") .toString()));
								session.setAttribute("mensviewtipo", null);
							}
							//Errores de Eliminación
							if(session.getAttribute("mensdelettipo") != null && !session.getAttribute("mensdelettipo").equals("")) {
								out.print("<div class='margsup'></div>");
								out.print(val.mostrarAlert(0, session.getAttribute("mensdelettipo") .toString()));
								session.setAttribute("mensdelettipo", null);
							}
							//Errores de Modificación
							if(session.getAttribute("mensupdatipo") != null && !session.getAttribute("mensupdatipo").equals("")) {
								out.print("<div class='margsup'></div>");
								out.print(val.mostrarAlert(0, session.getAttribute("mensupdatipo") .toString()));
								session.setAttribute("mensupdatipo", null);
							}
						%>
						<div class="expli">
	                       	<p>
                       			Realice una búsqueda de las clasificaciones con el objetivo de conocer y controlar las posibles agrupaciones que se generen en el sistema.
	                       	</p>
                       	</div>
					</form>
				<%
					if(session.getAttribute("existablatipo") != null)
					{
						session.setAttribute("existablatipo", null);
				%>
						<div class="containTable margsup col col-12 col-lg-11">
							<table id="registros" data-page-length="10" class="table table-hover table-bordered table-sm align-middle">
								<thead class="tableheader">
									<tr>
										<th scope="col">TIPO DE ACCESORIO/DISPOSITIVO</th>
										<th scope="col">SIMBOLOGÍA</th>
										<%
											if(accesos.get(1).equals("1") && accesos.get(2).equals("1")) {
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
												ArrayList<String> exist = null;
												for (i = 0; i < elementos; i++) { 
													out.print("<tr>");
													out.print("<td>" + datos.get(i).get(2) + "</td>");
													out.print("<td>" + datos.get(i).get(1) + "</td>");
													if(accesos.get(1).equals("1") && accesos.get(2).equals("1")) { //Tanto Modificar y Eliminar
														out.print("<td>");
														query = "SELECT Id FROM Productos WHERE Tipo = '" + datos.get(i).get(0) + "' ORDER BY Id ASC LIMIT 1";
														exist = layout.getRow(query);
														if(exist.size() > 0) {
															if(accesos.get(1).equals("1")) { //Solo Modificar
																out.print("<a class=\"aloneopc\" href=\"guardar?id=" + datos.get(i).get(0) + " \" data-bs-toggle=\"tooltip\" data-bs-placement=\"right\" title=\"Modificar registro.\"><i class=\"bi bi-pencil-square\"></i></a>");
															}
															else out.print("-");
														}
														else {
															if(accesos.get(1).equals("1") && accesos.get(2).equals("1")) { //Tanto Modificar y Eliminar
																out.print("<div class=\"dropdown\">");
																out.print("<button class=\"btn dropdown-toggle\" type=\"button\" id=\"dmbAcciones\" data-bs-toggle=\"dropdown\" aria-expanded=\"false\">ACCIONES</button>");
																out.print("<ul class=\"dropdown-menu dropdown-menu-lg-end opcOtrAcciones\" aria-labelledby=\"dmbAcciones\">");
																out.print("<li><a class=\"dropdown-item btn-interno a-superior\" href=\"guardar?id=" + datos.get(i).get(0) + "\" data-bs-toggle=\"tooltip\" data-bs-placement=\"left\" title=\"Modificar registro.\">MODIFICAR <i class=\"bi bi-pencil-square\"></i></a></li>");
																out.print("<li><a class=\"dropdown-item btn-interno-eliminar a-inferior puntero\" onclick=\"confirmlink('¿Esta seguro de Eliminar este registro?', 'eliminar?id=" + datos.get(i).get(0) + "');\" data-bs-toggle=\"tooltip\" data-bs-placement=\"left\" title=\"Eliminar registro.\">ELIMINAR <i class=\"bi bi-trash\"></i></a></li>");
																out.print("</ul>");
																out.print("</div>");
															}
															else if(accesos.get(1).equals("1")) { //Solo Modificar
																out.print("<a class=\"aloneopc\" href=\"guardar?id=" + datos.get(i).get(0) + " \" data-bs-toggle=\"tooltip\" data-bs-placement=\"right\" title=\"Modificar registro.\"><i class=\"bi bi-pencil-square\"></i></a>");
															}
															else if(accesos.get(2).equals("1")) { //Solo Eliminar
																out.print("<a class=\"aloneopc-delete puntero\" onclick=\"confirmlink('¿Esta seguro de eliminar este registro?', 'eliminar?id=" + datos.get(i).get(0) + "');\" data-bs-toggle=\"tooltip\" data-bs-placement=\"right\" title=\"Eliminar registro.\"><i class=\"bi bi-trash\"></i></a>");
															}
															else out.print("-");
														}
														out.print("</td>");
													}
													out.print("</tr>");
												}
												if(i == 0) {
													out.print("<tr>");
													out.print("<td colspan=\"4\">No se encontraron elementos para mostrar.</td>");
													out.print("</tr>");
												}
											}
											else {
												out.print("<tr>");
												out.print("<td colspan=\"4\">No se encontraron elementos para mostrar.</td>");
												out.print("</tr>");
											}
										}
										catch (Exception e) {
											out.print("<tr>");
											out.print("<td colspan=\"4\">No se encontraron elementos para mostrar.</td>");
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
			if(session.getAttribute("succViewT") != null && !session.getAttribute("succViewT").equals("")) {
				out.print(val.mostrarToastr("Éxito", "Proceso realizado correctamente."));
				session.setAttribute("succViewT", null);
			}
		%>
	</body>
</html>
