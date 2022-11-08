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
			query = "SELECT AgregarProducto FROM Accesos WHERE IdUsuario = '" + iduser + "'";
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
			if(session.getAttribute("tabla_min") != null) {
				datos = (ArrayList<ArrayList<String>>) session.getAttribute("tabla_min");
				session.setAttribute("tabla_min", null);
			}
			query = "SELECT Id FROM Inventarios WHERE IdUsuario = '" + request.getSession().getAttribute("id") + "' AND Estado = '0' AND Accion = '2' ORDER BY Id DESC LIMIT 1";
			ArrayList<String> idInventario = layout.getRow(query);
			int cant = idInventario.size();
			int cantprod = 0;
			ArrayList<ArrayList<String>> idproductos = null;
			ArrayList<String> productos = null;
			if(cant > 0) {
				query = "SELECT A.IdProducto, P.Nombre FROM Acciones AS A INNER JOIN Productos AS P ON P.Id = A.IdProducto WHERE A.IdInventario = '" + idInventario.get(0) + "' ORDER BY P.Nombre ASC";
				idproductos = layout.getRows(query);
				cantprod = idproductos.size();
			}
			//Datos de adición
			String autorizador = "", caso = "", RF = "", referencia1 = "", referencia2 = "", referencia3 = "", comentario = "", yo = "", search = "";
			if (session.getAttribute("retaddsearch") != null && !session.getAttribute("retaddsearch").equals("")) {
				search = (String) session.getAttribute("retaddsearch");
				session.setAttribute("retaddsearch", null);
			}
			if (session.getAttribute("retaddautor") != null && !session.getAttribute("retaddautor").equals("")) {
				autorizador = (String) session.getAttribute("retaddautor");
			}
			if (session.getAttribute("retaddcaso") != null && !session.getAttribute("retaddcaso").equals("")) {
				caso = (String) session.getAttribute("retaddcaso");
			}
			if (session.getAttribute("retaddRF") != null && !session.getAttribute("retaddRF").equals("")) {
				RF = (String) session.getAttribute("retaddRF");
			}
			if (session.getAttribute("retaddref1") != null && !session.getAttribute("retaddref1").equals("")) {
				referencia1 = (String) session.getAttribute("retaddref1");
			}
			if (session.getAttribute("retaddref2") != null && !session.getAttribute("retaddref2").equals("")) {
				referencia2 = (String) session.getAttribute("retaddref2");
			}
			if (session.getAttribute("retaddref3") != null && !session.getAttribute("retaddref3").equals("")) {
				referencia3 = (String) session.getAttribute("retaddref3");
			}
			if (session.getAttribute("retaddcomenting") != null && !session.getAttribute("retaddcomenting").equals("")) {
				comentario = (String) session.getAttribute("retaddcomenting");
			}
			if (session.getAttribute("retaddyo") != null && !session.getAttribute("retaddyo").equals("")) {
				yo = (String) session.getAttribute("retaddyo");
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
                        <span class="subencab"> / ADICIONES</span>
                    </p>
                    <div id="principalInvent">
                    	<%
				 			if(cant > 0) {
				 				if(cantprod > 0) {
			 						out.print("<div id='ingfin1' class='btn-group col col-sm-12'>");
									out.print("<a class='btn btn-guardar' onclick='confirmform(event, frmadicion, \"¿Esta seguro de Adicionar al sistema esta lista de Accesorios/Dispositivos?\")' data-bs-toggle='tooltip' data-bs-placement='bottom' title='Terminar el proceso y guardar.'>ADICIONAR Y GUARDAR</a>");
									out.print("<hr>");
									out.print("</div>");
				 				}
				 			}
			 			%>
                        <div class="row">
                        	<%
				 				if(cant > 0) {
					 				if(cantprod > 0) {
				 			%>
                           <div class="col-12 col-md-6 retirarinf forminit">
                               <div id="divInv" class="col-12 col-lg-12">
                                   <p class='titproduct center'>Accesorios/Dispositivos a Adicionar</p>
                                   <%
										out.print("<hr>");
                                   %>
                              		<div class="expli-just">
	                                	<p>
	                                		Ingrese los datos descritos a continuación para luego definir la cantidad de accesorios, al terminar presionar "ADICIONAR Y GUARDAR".
	                                	</p>
                               		</div>
                                   <form name="frmadicion" id="frmadicion" class="col col-sm-12" action="guardaradicion" method="POST" onsubmit="confirmform(event, this, '¿Esta seguro de Adicionar al sistema esta lista de Accesorios/Dispositivos?')">
                                       <%
		                               		//Errores
		               						if(session.getAttribute("mensaddinventario") != null && !session.getAttribute("mensaddinventario").equals("")) {
		               							out.print("<div class='margsup'></div>");
		               							out.print(val.mostrarAlert(0, session.getAttribute("mensaddinventario") .toString()));
		               							session.setAttribute("mensaddinventario", null);
		               						}
	                               		%>
											<div class="col col-sm-12 inpform">
												<label for="txtPersonal" class="form-label">
													Ingrese el nombre completo de la persona que autoriza este movimiento
												</label>
												<div class="row">
		                               				<div class="col col-sm-10">
		                               					<div class="input-group">
			                               					<span class="input-group-text" id="lblAutorizacion">
																<i class="bi bi-shield-shaded"></i>
															</span>
															<input name="txtAutorizacion" id="txtAutorizacion" aria-describedby="txtAutorizacion" placeholder="Autorizador" class="form-control flexdatalist"
																autocomplete="off" type="text" spellcheck="false" maxlength="100" pattern="[A-Za-zÁÉÍÓÚáéíóú ]{1,100}" title="Alfabético de máximo 100 caracteres." required
																value="<%= autorizador %>" data-min-length="1" list="personal" />
															<datalist id="personal">
																<%
																	ArrayList<String> arraydatos = val.obtenerUsers();
																	if (arraydatos.size() > 0) {
																		for (int i = 0; i < arraydatos.size(); i++) {
																			out.print("<option value='" + arraydatos.get(i) + "'>" + arraydatos.get(i) + "</option>");
																		}
																	}
																%>
															</datalist>
														</div>
													</div>
													<div class="col col-sm-2">
														<input class='checkinv form-check-input puntero' type='checkbox' name='chkauto' id="chkauto"<%= (yo.equals("1") ? " checked='checked' " : " ") %>>
														<label for="chkauto" class="form-label puntero">
																Yo
														</label>
													</div>
												</div>
											</div>
											<div class="col col-sm-12 inpform">
												<label for="txtCaso" class="form-label">
													Ingrese el número del caso
												</label>
												<div class="input-group">
													<span class="input-group-text" id="lblCaso">
														<i class="bi bi-link"></i>
													</span>
													<input name="txtCaso" id="txtCaso" aria-describedby="txtCaso" placeholder="Número del caso" class="form-control"
														autocomplete="off" type="number" min="0" max="99999999" spellcheck="false" maxlength="8" title="Numérico de mínimo 1 y máximo 8 digitos." required
														value="<%= caso %>" oninput="numcaso()" />
												</div>
											</div>
											<div class="col col-sm-12 inpform">
												<label for="txtReferencia" class="form-label">
													Ingrese la referencia del caso
												</label>
												<div class="input-group">
													<span class="input-group-text" id="lblReferencia">
														<i class="bi bi-link"></i>
													</span>
													<div class="col col col-sm-1">
														<input autocomplete="off" type="text" class="form-control" 
															name="txtRF" id="txtRF" value="<% out.print(RF.equals("") ? "RF" : RF); %>"
															placeholder="RF" pattern="[A-Za-z]{1,6}" maxlength="6"
															required="required">
													</div>
													<div class="col col col-sm-1">
														<input autocomplete="off" type="text" class="form-control" 
															name="G1" id="G1" value="-" disabled="disabled"
															placeholder="-"
															required="required">
													</div>
													<div class="col col col-sm">
														<input name="txtReferencia1" id="txtReferencia1" aria-describedby="txtReferencia1" placeholder="No. Referencia" class="form-control"
															autocomplete="off" type="number" min="0" max="99999" spellcheck="false" maxlength="5" title="Numérico de máximo 5 digitos." required
															value="<%= referencia1 %>" />
													</div>
													<div class="col col col-sm-1">
														<input autocomplete="off" type="text" class="form-control" 
															name="G2" id="G2" value="-" disabled="disabled"
															placeholder="-"
															required="required">
													</div>
													<div class="col col col-sm">
														<input name="txtReferencia2" id="txtReferencia2" aria-describedby="txtReferencia2" placeholder="No. Referencia" class="form-control"
															autocomplete="off" type="number" min="0" max="99999" spellcheck="false" maxlength="5" title="Numérico de máximo 5 digitos." required
															value="<%= referencia2 %>" />
													</div>
													<div class="col col col-sm-1">
														<input autocomplete="off" type="text" class="form-control" 
															name="G3" id="G3" value="-" disabled="disabled"
															placeholder="-"
															required="required">
													</div>
													<div class="col col col-sm">
														<input name="txtReferencia3" id="txtReferencia3" aria-describedby="txtReferencia3" placeholder="No. Referencia" class="form-control"
															autocomplete="off" type="number" min="0" max="99999999" spellcheck="false" maxlength="8" title="Numérico de máximo 8 digitos." readonly required
															value="<%= referencia3 %>" />
													</div>
												</div>
											</div>
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
												out.print("<div id='ingfin3' class='btn-group col col-sm-12'>");
												out.print("<button type='submit' class='btn btn-guardar' data-bs-toggle='tooltip' data-bs-placement='bottom' title='Terminar el proceso y guardar.'>ADICIONAR Y GUARDAR</button>");
												out.print("</div>");
											%>
                                            <div class="containTable_Min">
                                            	<table id="registrosmin" data-page-length="6" class="table striped table-hover table-bordered table-sm align-middle">
													<thead class="tableheader">
                                                        <tr>
                                                            <th scope="col">ACCESORIO/DISPOSITIVO</th>
                                                            <th scope="col">UBICACIÓN</th>
                                                            <th scope="col">CANTIDAD ACTUAL</th>
                                                            <th scope="col" data-orderable="false">NUEVA CANTIDAD</th>
                                                            <th scope="col" data-orderable="false"></th>
                                                        </tr>
                                                    </thead>
                                                    <tbody>
                                                        <% 
															try { 
																for (int i = 0; i < cantprod; i++) { 
																	query = "SELECT Id, Codigo, Nombre, Cantidad, Ubicacion, Marca, Modelo, Serie, ActivoFijo, Comentario FROM Productos WHERE Id='" + idproductos.get(i).get(0) + "' AND Cantidad >= '0' ORDER BY Nombre DESC";
																	productos = layout.getRow(query);
																	out.print("<tr>");
																	out.print("<td>");
																	out.print(productos.get(2));
																	if(productos.get(5) != null && !productos.get(5).trim().equals("-") && !productos.get(5).trim().equals("")) {
													  					out.print(", Marca: " + productos.get(5));
													  				}
													  				if(productos.get(6) != null && !productos.get(6).trim().equals("-") && !productos.get(6).trim().equals("")) {
													  					out.print(", Modelo: " + productos.get(6));
													  				}
													  				if(productos.get(7) != null && !productos.get(7).trim().equals("-") && !productos.get(7).trim().equals("")) {
													  					out.print(", Serie: " + productos.get(7));
													  				}
													  				if(productos.get(8) != null && !productos.get(8).trim().equals("-") && !productos.get(8).trim().equals("")) {
													  					out.print(", Activo Fijo: " + productos.get(8));
													  				}
													  				if((productos.get(9) != null && !productos.get(9).trim().equals(""))) {
															  			out.print("<br />Comentario: <span data-bs-toggle=\"modal\" data-bs-target=\"#coment2" + productos.get(0) + "\"><a class=\"aloneopc\" data-bs-toggle=\"tooltip\" type=\"button\" data-bs-placement=\"bottom\" title=\"Click para ver el comentario del accesorio/dispositivo.\"><i class=\"bi bi-eye-fill\"></i></a></span>");
														  			}
													  				out.print("</td>");
																	out.print("<td>" + productos.get(4) + "</td>");
																	out.print("<td>" + productos.get(3) + "</td>");
																	out.print("<td>");
																	out.print("<input name='txtCantidad' id='txtCantidad' aria-describedby='txtCantidad' placeholder='#' class='form-control'");
																	out.print("autocomplete='off' type='number' min='0' spellcheck='false' maxlength='5' pattern='[0-9]{1,5}' title='Mínimo 1 y máximo 5 dígitos' required");
																	out.print("value='0' />");
																	out.print("</td>");
																	out.print("<td>");
																	out.print("<a class='aloneopc-delete puntero' onclick=\"confirmlink('¿Esta seguro de Quitar este Accesorio/Dispositivo de la lista?', 'adicion?id=" + productos.get(0) + "')\" data-bs-toggle='tooltip' data-bs-placement='left' title='Quitar de la lista de accesorios/dispositivos.'><i class='bi bi-x-circle-fill'></i></a>");
																	out.print("</td>");
																	out.print("</tr>");
																	if (productos.get(9) != null && !productos.get(9).trim().equals("")) {
															  			out.print("<div class=\"modal fade\" id=\"coment2" + productos.get(0) + "\" tabindex=\"-1\" aria-labelledby=\"coment2" + productos.get(0) + "Label\" aria-hidden=\"true\">");
															  			out.print("<div class=\"modal-dialog modal-dialog-scrollable\">");
															  			out.print("<div class=\"modal-content\">");
															  			out.print("<div class=\"modal-header\">");
															  			out.print("<h5 class=\"modal-title\" id=\"coment2" + productos.get(0) + "Label\">Comentario del Accesorio/Dispositivo: <b class='bbig'>" + productos.get(2) + "</b></h5>");
															  			out.print("<button type=\"button\" class=\"btn-close\" data-bs-dismiss=\"modal\" aria-label=\"Close\"></button>");
															  			out.print("</div>");
															  			out.print("<div class=\"modal-body\">");
															  			out.print(productos.get(9));
															  			out.print("</div>");
															  			out.print("</div>");
															  			out.print("</div>");
															  			out.print("</div>");
													  				}
																}
															}
															catch (Exception e) {
																out.print("<tr>");
																out.print("<td colspan=\"5\">No se encontraron elementos para mostrar.</td>");
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
			 					}
				 				if(cant > 0) {
					 				if(cantprod > 0) {
				 						out.print("<div class='col-12 col-md-6 margsup-min'>");
				 						out.print("<div id='divInv' class='col-12 col-lg-11'>");	
					 				}
					 				else {
					 					out.print("<div class='divcentrado col col-12 col-lg-10'>");
				 						out.print("<div class='col col-12 col-sm-12'>");
					 				}
								}
								else {
									out.print("<div class='divcentrado col col-12 col-lg-10'>");
									out.print("<div class='col col-12 col-sm-12'>");
								}
			 				%>
                            <div class="row">
                                <form class="col col-sm-12<% out.print((session.getAttribute("existablamin") == null) ? " marginf" : ""); %>" action="./vista_min?lugar=add" method="POST">
                                    <div class="row grp-buscar">
                                        <div class="col col-sm-9 col-md-8 col-lg-9">
                                        	<input name="txtSearch" id="txtSearch" aria-describedby="txtSearch" placeholder="Por accesorio/dispositivo y ubicación de almacenado." class="form-control"
												autocomplete="off" type="text" spellcheck="false" maxlength="100" title="Por accesorio/dispositivo y ubicación de almacenado."
												value="<%= search %>" />
                                        </div>
                                        <div class="col col-sm-3 col-md-4 col-lg-3">
	                                        <table class="col col-12">
			                               		<tbody>
			                               			<tr class="col col-12">
			                               				<td class="col-6">
							                                <button type="submit" class="btn btn-ingresar" data-bs-toggle="tooltip" data-bs-placement="bottom" title="Buscar registro.">
				                                                <i class="bi bi-search"></i> BUSCAR
				                                            </button>
							                            </td>
							                        </tr>
							                      	<tr class="col col-12">
							                            <td class="col-6">
							                            	<a id='btnlimpiar' class="btn btn-ingresar btnclean-bsup puntero" data-bs-toggle="tooltip" data-bs-placement="bottom" title="Borrar los datos ingresados.">
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
	            						if(session.getAttribute("menssearchinventario") != null && !session.getAttribute("menssearchinventario").equals("")) {
	            							out.print("<div class='margsup'></div>");
	            							out.print(val.mostrarAlert(0, session.getAttribute("menssearchinventario") .toString()));
	            							session.setAttribute("menssearchinventario", null);
	            						}
                                    %>
                                </form>
                                <div class="expli-just">
                                	<p>
                                		Realice la búsqueda y seleccione los accesorios/dispositivos que necesite dándole al check en el cuadro derecho, para luego presionar "AGREGAR A LA LISTA DE ADICIONES".
                                	</p>
                                </div>
                                <%
									if(session.getAttribute("existablamin") != null)
									{
										session.setAttribute("existablamin", null);
								%>
                                	<form class="col col-sm-12" action="./adicion" method="POST" onsubmit="confirmform(event, this, '¿Esta seguro de Agregar a la lista estos Accesorios/Dispositivos?')">
                                    <%
										if(datos != null) { 
											int elementos = datos.size(); 
											if(elementos > 0) {
												out.print("<div class='btn-group col col-sm-12'>");
												out.print("<button type='submit' class='btn btn-guardar' data-bs-toggle='tooltip' data-bs-placement='bottom' title='Agregar a la lista sin finalizar.'>AGREGAR A LA LISTA DE ADICIONES</button>");
												out.print("</div>");
											}
										}
									%>
                                    <div class="containTable_Min">
                                        <table id="registros" data-page-length="10" class="table table-hover table-bordered table-sm align-middle">
											<thead class="tableheader">
                                                <tr>
                                                    <th scope="col">ACCESORIO/DISPOSITIVO</th>
                                                    <th scope="col">ALMACENADO EN</th>
                                                    <th scope="col">CANTIDAD ACTUAL</th>
                                                    <th scope="col" data-orderable="false"><input class='checkinv form-check-input' type='checkbox' name='chckall' id="chckall"></th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                            	<% 
													try { 
														if(datos != null) { 
															int elementos = datos.size(); 
															int total = 0;
															for (int i = 0; i < elementos; i++) { 
																int verif = 0;
																for (int j = 0; j < cantprod; j++) { 
																	query = "SELECT Id FROM Productos WHERE Id='" + idproductos.get(j).get(0) + "' AND Cantidad >= '0' ORDER BY Nombre DESC";
																	 productos = layout.getRow(query);
																	if(datos.get(i).get(0).equals(productos.get(0))) {
																		verif = 1;
																	}
																}
																if(verif == 0) {
																	out.print("<tr>");
																	out.print("<td>");
																	out.print(datos.get(i).get(2));
																	if(datos.get(i).get(5) != null && !datos.get(i).get(5).trim().equals("-") && !datos.get(i).get(5).trim().equals("")) {
													  					out.print(", Marca: " + datos.get(i).get(5));
													  				}
													  				if(datos.get(i).get(6) != null && !datos.get(i).get(6).trim().equals("-") && !datos.get(i).get(6).trim().equals("")) {
													  					out.print(", Modelo: " + datos.get(i).get(6));
													  				}
													  				if(datos.get(i).get(7) != null && !datos.get(i).get(7).trim().equals("-") && !datos.get(i).get(7).trim().equals("")) {
													  					out.print(", Serie: " + datos.get(i).get(7));
													  				}
													  				if(datos.get(i).get(8) != null && !datos.get(i).get(8).trim().equals("-") && !datos.get(i).get(8).trim().equals("")) {
													  					out.print(", Activo Fijo: " + datos.get(i).get(8));
													  				}
													  				if((datos.get(i).get(9) != null && !datos.get(i).get(9).trim().equals(""))) {
															  			out.print("<br />Comentario: <span data-bs-toggle=\"modal\" data-bs-target=\"#coment1" + datos.get(i).get(0) + "\"><a class=\"aloneopc\" data-bs-toggle=\"tooltip\" type=\"button\" data-bs-placement=\"bottom\" title=\"Click para ver el comentario del accesorio/dispositivo.\"><i class=\"bi bi-eye-fill\"></i></a></span>");
														  			}
													  				out.print("</td>");
																	out.print("<td>" + datos.get(i).get(4) + "</td>");
																	out.print("<td>" + datos.get(i).get(3) + "</td>");
																	out.print("<td>");
																	out.print("<input class='checkinv form-check-input checks' type='checkbox' name='checks' value='" + datos.get(i).get(0) + "'");
																	out.print("</td>");
																	out.print("</tr>");
																	if (datos.get(i).get(9) != null && !datos.get(i).get(9).trim().equals("")) {
															  			out.print("<div class=\"modal fade\" id=\"coment1" + datos.get(i).get(0) + "\" tabindex=\"-1\" aria-labelledby=\"coment1" + datos.get(i).get(0) + "Label\" aria-hidden=\"true\">");
															  			out.print("<div class=\"modal-dialog modal-dialog-scrollable\">");
															  			out.print("<div class=\"modal-content\">");
															  			out.print("<div class=\"modal-header\">");
															  			out.print("<h5 class=\"modal-title\" id=\"coment1" + datos.get(i).get(0) + "Label\">Comentario del Accesorio/Dispositivo: <b class='bbig'>" + datos.get(i).get(2) + "</b></h5>");
															  			out.print("<button type=\"button\" class=\"btn-close\" data-bs-dismiss=\"modal\" aria-label=\"Close\"></button>");
															  			out.print("</div>");
															  			out.print("<div class=\"modal-body\">");
															  			out.print(datos.get(i).get(9));
															  			out.print("</div>");
															  			out.print("</div>");
															  			out.print("</div>");
															  			out.print("</div>");
													  				}
																	total++;
																}
																else {
																	total--;
																}
															}
															if(total == 0) {
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
	                           </form>
                               	<%
									}
								%>
	                         </div>
	                     	<%
					 			out.print("</div>");
								out.print("</div>");
							%>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
			<%
				//Fin del Contenido
				out.print(layout.footer(2));
				if(session.getAttribute("succAddP") != null && !session.getAttribute("succAddP").equals("")) {
					out.print(val.mostrarToastr("Éxito", session.getAttribute("succAddP").toString()));
					session.setAttribute("succAddP", null);
				}
			%>
			<script type="text/javascript">
				var deshabilitarauto_input = function () {
					if($("#chkauto").is(":checked")) {
						$("#txtAutorizacion").prop("disabled", "disabled");
						$("#txtAutorizacion-flexdatalist").prop("disabled", "disabled");
					}
					else {
						$("#txtAutorizacion").prop("disabled", false);
						$("#txtAutorizacion-flexdatalist").prop("disabled", false);
					}
				};
				$(deshabilitarauto_input);
				$("#chkauto").change(deshabilitarauto_input);
				$("#btnlimpiar").click(function() {
					$("#txtSearch").val("");
				});
				
				var numcaso = function () {
					let caso = $("#txtCaso").val();
					$("#txtReferencia3").val(caso);
				}
			</script>
	</body>
</html>