<%@ page import="java.util.*" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@page import="lib.Validacion"%>
<%@page import="lib.Plantilla"%>
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
			query = "SELECT UpdateProducto FROM Accesos WHERE IdUsuario = '" + iduser + "'";
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
			//Obtener los Tipos de Productos
			query = "SELECT Id, Simbolo, Nombre FROM Tipo_Productos ORDER BY Nombre ASC"; 
			ArrayList<ArrayList<String>> datos = layout.getRows(query);
			String id = (String) session.getAttribute("idtab");
			if(id == null || id.equals("")) {
				out.print("<script type=\"text/javascript\">");
				out.print("window.location.href = \"./vista\"");
				out.print("</script>");
			}
			String codigo = "", nombre = "", tipo = "", ubicacion = "", cantidad = "", comentario = "", marca = "", modelo = "", serie = "", activofijo = "";
			if (id != null && id != "") {
				query = "SELECT Codigo, Tipo, Nombre, Cantidad, Ubicacion, Comentario, Marca, Modelo, Serie, ActivoFijo FROM Productos WHERE Id = '" + id + "' ORDER BY Id LIMIT 1";
				ArrayList<String> datosobt = layout.getRow(query);
				if(datosobt.size() > 0)
				{
					codigo = datosobt.get(0);
					tipo = datosobt.get(1);
					nombre = datosobt.get(2);
					cantidad = datosobt.get(3);
					ubicacion = datosobt.get(4);
					comentario = datosobt.get(5);
					marca = datosobt.get(6).equals("-") ? "" : datosobt.get(6);
					modelo = datosobt.get(7).equals("-") ? "" : datosobt.get(7);
					serie = datosobt.get(8).equals("-") ? "" : datosobt.get(8);
					activofijo = datosobt.get(9).equals("-") ? "" : datosobt.get(9);
				}
			}
		%>
        <div id="panPrincipal" class="col col-md-12">
            <div class="row">
                <div class="col col-sm-12">
                    <a class="btn btn-regresar" href="./vista" role="button" data-bs-toggle="tooltip" data-bs-placement="left" title="Regresar al sitio anterior.">
                        <i class="bi bi-chevron-double-left"></i>
                        REGRESAR
                    </a>
                    <p class="encabezadodash">
                   		<a href="./">INVENTARIO DE ACCESORIOS/DISPOSITIVOS</a>
                        <span class="subencab">
                            / <a href="./vista">CONSULTAS</a>
                            / MODIFICACIONES
                        </span>
                    </p>
                    <div id="principalInvent">
	                  	<div class="row">
		                    <% //Formulario Inicial %>
		                    <div class='col-12'>
                            	<div id='divInv' class='col col-12 col-sm-8 col-md-7 col-lg-8'>
	                            	<form class="col col-12" action="modificar?id=<%= id %>&vista=1" method="POST" name="frmsave" id="frmsave"  onsubmit="confirmform(event, this, '¿Esta seguro de guardar estos datos?')">
		                            	<div class='col col-sm-12 inpform'>
		                                	<label for='txtCodigo' class='form-label'>Código del accesorio/dispositivo:</label>
		                                    <div class='input-group'>
		                                    	<span class='input-group-text' id='txtCodigo'>
		                                    		<i class='bi bi-flag-fill'></i>
		                                    	</span>
		                                    	<input name="txtCodigo" id="txtCodigo" aria-describedby="txtCodigo" placeholder="Codigo" class="form-control" title='Alfanumérico de entre 8 y 9 caracteres.'
													autocomplete="off" type="text" spellcheck="false" maxlength="9" pattern="[A-Za-z0-9 ]{8,9}" required
													value="<%= codigo %>" />
		                                    </div>
		                                </div>
	                                    <div class="col col-sm-12 inpform">
	                                    	<label for="txtNombre" class="form-label">Ingrese el nombre del accesorio/dispositivo</label>
	                                        <div class="input-group">
	                                        	<span class="input-group-text" id="txtNombre">
	                                            	<i class="bi bi-clipboard"></i>
	                                            </span>
	                                            <input name="txtNombre" id="txtNombre" aria-describedby="txtNombre" placeholder="Nombre" class="form-control flexdatalist" title='Mínimo 1 y máximo 150 caracteres.'
													autocomplete="off" type="text" spellcheck="false" maxlength="150" required
													value="<%= nombre %>" data-min-length="1" list="accesorio"/>
	                                        </div>
	                                    </div>
	                                    <div class="col col-sm-12 inpform">
			                                <label for="txtMarca" class="form-label">Ingrese la marca <i id="cursiva">(Opcional)</i></label>
			                                <div class="input-group">
			                                    <span class="input-group-text" id="txtMarca">
			                                        <i class="bi bi-laptop"></i>
			                                    </span>
			                                    <input name="txtMarca" id="txtMarca" aria-describedby="txtMarca" placeholder="Marca" class="form-control flexdatalist" title='Mínimo 1 y máximo 100 caracteres.'
													autocomplete="off" type="text" spellcheck="false" maxlength="100"
													value="<%= marca %>" data-min-length="1" list="marca"/>
			                                </div>
			                            </div>
			                           <div class="col col-sm-12 inpform">
			                                <label for="txtModelo" class="form-label">Ingrese el modelo <i id="cursiva">(Opcional)</i></label>
			                                <div class="input-group">
			                                    <span class="input-group-text" id="txtModelo">
			                                        <i class="bi bi-laptop"></i>
			                                    </span>
			                                    <input name="txtModelo" id="txtModelo" aria-describedby="txtModelo" placeholder="Modelo" class="form-control" title='Mínimo 1 y máximo 100 caracteres.'
													autocomplete="off" type="text" spellcheck="false" maxlength="100"
													value="<%= modelo %>"/>
			                                </div>
			                            </div>
			                            <div class="col col-sm-12 inpform">
			                                <label for="txtSerie" class="form-label">Ingrese la serie <i id="cursiva">(Opcional)</i></label>
			                                <div class="input-group">
			                                    <span class="input-group-text" id="txtSerie">
			                                        <i class="bi bi-laptop"></i>
			                                    </span>
			                                    <input name="txtSerie" id="txtSerie" aria-describedby="txtSerie" placeholder="Serie" class="form-control" title='Mínimo 1 y máximo 100 caracteres.'
													autocomplete="off" type="text" spellcheck="false" maxlength="100"
													value="<%= serie %>"/>
			                                </div>
			                            </div>
			                            <div class="col col-sm-12 inpform">
			                                <label for="txtActivoFijo" class="form-label">Ingrese el activo fijo <i id="cursiva">(Opcional)</i></label>
			                                <div class="input-group">
			                                    <span class="input-group-text" id="txtActivoFijo">
			                                        <i class="bi bi-laptop"></i>
			                                    </span>
			                                    <input name="txtActivoFijo" id="txtActivoFijo" aria-describedby="txtActivoFijo" placeholder="Activo Fijo" class="form-control" title='Mínimo 14 y máximo 20 dígitos'
													autocomplete="off" type="text" spellcheck="false" maxlength="20" pattern="[0-9]{14,20}"
													value="<%= activofijo %>"/>
			                                </div>
			                            </div>
	                                    <div class="col col-sm-12 inpform">
	                                    	<div class="row">
	                                        	<label for="cmbTipos" class="form-label">Ingrese la clasificación del accesorio/dispositivo</label>
	                                            <div class="col col-sm-12">
	                                            	<select class="form-select clasificacion" aria-label="Clasificación de Accesorios/Dispositivos" required="required" name="cmbTipos">
	                                                	<option selected disabled="disabled">Clasificación de Accesorios/Dispositivos</option>
	                                                    <% 
		            										if(datos != null) {
	            								  				int elementos = datos.size();
	            								  				for (int j = 0; j < elementos; j++) {
	            								  					if(tipo.equals(datos.get(j).get(0))) {
	            								  						out.print("<option value=\"" + datos.get(j).get(1) + "\" selected>" + datos.get(j).get(2) + "</option>");
	            								  					} else {
	            								  						out.print("<option value=\"" + datos.get(j).get(1) + "\">" + datos.get(j).get(2) + "</option>");	
	            								  					}
	            								  				}
	            											}
														%>
                                                  	</select>
                                           		</div>
                                         	</div>
                                       	</div>
	                                    <div class="col col-sm-12 inpform">
	                                    	<label for="txtUbicacion" class="form-label">Ingrese la ubicación</label>
                                        	<div class="input-group">
                                            	<span class="input-group-text" id="txtUbicacion">
                                                	<i class="bi bi-geo-alt-fill"></i>
                                                </span>
                                                <input name="txtUbicacion" id="txtUbicacion" aria-describedby="txtUbicacion" placeholder="Ubicación" class="form-control flexdatalist" title='Alfanumérico de mínimo 1 y máximo 100 caracteres.'
													autocomplete="off" type="text" spellcheck="false" maxlength="100" required
													value="<%= ubicacion %>" data-min-length="1" list="ubicacion"/>
                                            </div>
	                                    </div>
	                                    <div class="col col-sm-12 inpform">
	                                    	<label for="txtCantidad" class="form-label">Ingrese la cantidad</label>
	                                        <div class="input-group">
	                                        	<span class="input-group-text" id="txtCantidad">
	                                            	<i class="bi bi-hash"></i>
	                                            </span>
	                                            <input name="txtCantidad" id="txtCantidad" aria-describedby="txtCantidad" placeholder="Cantidad" title="Mínimo 1 y máximo 5 dígitos" class="form-control"
													autocomplete="off" type="number" spellcheck="false" maxlength="5" min="0" max="99999" required
													value="<%= cantidad %>" />
	                                        </div>
	                                    </div>
	                                    <div class="col col-sm-12 inpform">
	                                    	<label for="txtComentario" class="form-label">Ingrese un Comentario <i id="cursiva">(Opcional)</i></label>
	                                        <div class="input-group">
	                                        	<span class="input-group-text" id="txtComentario">
	                                            	<i class="bi bi-chat-left-dots-fill"></i>
	                                            </span>
	                                            <input name="txtComentario" id="txtComentario" aria-describedby="txtComentario" placeholder="Comentario" class="form-control"
													autocomplete="off" type="text" spellcheck="false" maxlength="500"
													value="<%= comentario %>" />
	                                        </div>
	                                    </div>
	                                    <%
				    	                  	//Errores
				    						if(session.getAttribute("mensinsmodinventario") != null && !session.getAttribute("mensinsmodinventario").equals("")) {
				    							out.print("<div class='margsup'></div>");
				    							out.print(val.mostrarAlert(0, session.getAttribute("mensinsmodinventario") .toString()));
				    							session.setAttribute("mensinsmodinventario", null);
				    						}
						                %>
                                        <div class="btn-group col col-sm-12">
                                            <button type="submit" class="btn btn-guardar" data-bs-toggle="tooltip" data-bs-placement="bottom" title="Guardar las correcciones y terminar.">GUARDAR Y TERMINAR</button>
                                        </div>
	                            	</form>
		                      	</div>
							</div>
						</div>
                  	</div>
             	</div>
     		</div>
       	</div>
       	<datalist id="ubicacion">
			<%
				ArrayList<String> arrayubicacion = val.obtenerLocate();
				if (arrayubicacion.size() > 0) {
					for (int i = 0; i < arrayubicacion.size(); i++) {
						out.print("<option value='" + arrayubicacion.get(i) + "'>" + arrayubicacion.get(i) + "</option>");
					}
				}
			%>
		</datalist>
		<datalist id="marca">
			<%
				ArrayList<String> arraymarca = val.obtenerMarca();
				if (arraymarca.size() > 0) {
					for (int i = 0; i < arraymarca.size(); i++) {
						out.print("<option value='" + arraymarca.get(i) + "'>" + arraymarca.get(i) + "</option>");
					}
				}
			%>
		</datalist>
		<datalist id="accesorio">
			<%
				ArrayList<String> arrayaccesorio = val.obtenerProducto();
				if (arrayaccesorio.size() > 0) {
					for (int i = 0; i < arrayaccesorio.size(); i++) {
						out.print("<option value='" + arrayaccesorio.get(i) + "'>" + arrayaccesorio.get(i) + "</option>");
					}
				}
			%>
		</datalist>
		<%
			//Fin del Contenido
			out.print(layout.footer(2));
		%>
    </body>
</html>
