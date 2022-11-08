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
        	session.setAttribute("idInventariodev", null);
			out.print(layout.navbar("Inventario", iduser, (String)session.getAttribute("token"), 2));
			//Inicio del Contenido
			String query = "SELECT Defecto FROM Usuarios WHERE Id = '" + iduser + "' ORDER BY Id DESC LIMIT 1";
			ArrayList<String> usuariobd = layout.getRow(query);
			if(usuariobd.size() > 0 && usuariobd.get(0).equals("0")) {
				out.print("<script type=\"text/javascript\">");
				out.print("window.location.href = \"../perfil\"");
				out.print("</script>");
			}
			//Accesos
			query = "SELECT CreateProducto, CreateTipoProducto FROM Accesos WHERE IdUsuario = '" + iduser + "'";
			ArrayList<String> accesos = val.getRow(query);
			if(accesos.size() == 0) {
				accesos.add(0, "0");
				accesos.add(1, "0");
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
			//Obtener los Inventarios
			query = "SELECT Id FROM Inventarios WHERE IdUsuario = '" + iduser + "' AND Accion = '1' AND Estado = '0' ORDER BY Fecha ASC LIMIT 1";
			ArrayList<String> idInventario = layout.getRow(query);
			int existId = idInventario.size(), cantidad = 0;
			ArrayList<ArrayList<String>> registros = null;
			ArrayList<ArrayList<String>> cantProductos = null;
			int acciones = 0;
			if(existId > 0) {
				query = "SELECT A.IdProducto FROM Acciones AS A INNER JOIN Inventarios AS I ON I.Id = A.IdInventario WHERE I.Id = '" + idInventario.get(0) + "' ORDER BY A.IdProducto ASC"; 
				registros = layout.getRows(query);
				cantidad = registros.size();
 				query = "SELECT IdProducto FROM Acciones WHERE IdInventario = '" + idInventario.get(0) + "'"; 
 				cantProductos = layout.getRows(query);
 				acciones = cantProductos.size();
			}
			request.getSession().setAttribute("returntipo", null);
			//Datos de los productos
			String nombre = "", txtcantidad = "", ubicacion = "", comentario = "", marca = "", modelo = "", serie = "", activofijo = "", tipo = "";
			if (session.getAttribute("retinsnombre") != null && !session.getAttribute("retinsnombre").equals("")) {
				nombre = (String) session.getAttribute("retinsnombre");
			}
			if (session.getAttribute("retinscantidad") != null && !session.getAttribute("retinscantidad").equals("")) {
				txtcantidad = (String) session.getAttribute("retinscantidad");
			}
			if (session.getAttribute("retinsubicacion") != null && !session.getAttribute("retinsubicacion").equals("")) {
				ubicacion = (String) session.getAttribute("retinsubicacion");
			}
			if (session.getAttribute("retinscoment") != null && !session.getAttribute("retinscoment").equals("")) {
				comentario = (String) session.getAttribute("retinscoment");
			}
			if (session.getAttribute("retinsmarca") != null && !session.getAttribute("retinsmarca").equals("") && !session.getAttribute("retinsmarca").equals("-")) {
				marca = (String) session.getAttribute("retinsmarca");
			}
			if (session.getAttribute("retinsmodelo") != null && !session.getAttribute("retinsmodelo").equals("") && !session.getAttribute("retinsmodelo").equals("-")) {
				modelo = (String) session.getAttribute("retinsmodelo");
			}
			if (session.getAttribute("retinsserie") != null && !session.getAttribute("retinsserie").equals("") && !session.getAttribute("retinsserie").equals("-")) {
				serie = (String) session.getAttribute("retinsserie");
			}
			if (session.getAttribute("retinsactivo") != null && !session.getAttribute("retinsactivo").equals("") && !session.getAttribute("retinsactivo").equals("-")) {
				activofijo = (String) session.getAttribute("retinsactivo");
			}
			if (session.getAttribute("retinstipo") != null && !session.getAttribute("retinstipo").equals("") && !session.getAttribute("retinsactivo").equals("-")) {
				tipo = (String) session.getAttribute("retinstipo");
			}
			//Datos de ingreso
			String autorizador = "", caso = "", RF = "", referencia1 = "", referencia2 = "", referencia3 = "", comentarioing = "", yo = "";
			if (session.getAttribute("retinsautor") != null && !session.getAttribute("retinsautor").equals("")) {
				autorizador = (String) session.getAttribute("retinsautor");
			}
			if (session.getAttribute("retinscaso") != null && !session.getAttribute("retinscaso").equals("")) {
				caso = (String) session.getAttribute("retinscaso");
			}
			if (session.getAttribute("retinsRF") != null && !session.getAttribute("retinsRF").equals("")) {
				RF = (String) session.getAttribute("retinsRF");
			}
			if (session.getAttribute("retinsref1") != null && !session.getAttribute("retinsref1").equals("")) {
				referencia1 = (String) session.getAttribute("retinsref1");
			}
			if (session.getAttribute("retinsref2") != null && !session.getAttribute("retinsref2").equals("")) {
				referencia2 = (String) session.getAttribute("retinsref2");
			}
			if (session.getAttribute("retinsref3") != null && !session.getAttribute("retinsref3").equals("")) {
				referencia3 = (String) session.getAttribute("retinsref3");
			}
			if (session.getAttribute("retinscomenting") != null && !session.getAttribute("retinscomenting").equals("")) {
				comentarioing = (String) session.getAttribute("retinscomenting");
			}
			if (session.getAttribute("retinsyo") != null && !session.getAttribute("retinsyo").equals("")) {
				yo = (String) session.getAttribute("retinsyo");
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
                            / INGRESOS
                        </span>
                    </p>
                    <div id="principalInvent">
                    	<%
    	                  	//Errores
    						if(session.getAttribute("mensinsmodinventario") != null && !session.getAttribute("mensinsmodinventario").equals("")) {
    							out.print("<div class='margsup'></div>");
    							out.print(val.mostrarAlert(0, session.getAttribute("mensinsmodinventario") .toString()));
    							out.print("<div class='margabajo'></div>");
    							session.setAttribute("mensinsmodinventario", null);
    						}
				 			if(existId > 0) {
				 				if(acciones > 0) {
			 						out.print("<div id='ingfin1' class='btn-group col col-sm-12'>");
									out.print("<a class='btn btn-guardar' onclick='confirmform(event, frminsert, \"¿Esta seguro de Ingresar al sistema esta lista de Accesorios/Dispositivos?\")' data-bs-toggle='tooltip' data-bs-placement='bottom' title='Terminar el proceso y guardar.'>INSERTAR LISTA Y GUARDAR</a>");
									out.print("<hr>");
									out.print("</div>");
				 				}
				 			}
			 			%>
                        <div class="row">
                        	<%
			 					if(existId > 0) {
					 				if(acciones > 0) {
					 					session.setAttribute("idInventariodev", idInventario.get(0));
					 					out.print("<div class='forminit col-12 col-md-6'>");
					 					out.print("<div id='divInv' class='col col-12 col-lg-12'>");
				 						out.print("<p class='titproduct center'>Accesorios/Dispositivos a Ingresar</p>");
				 						out.print("<hr>");
	                              		out.print("<div class='expli-just'>");
	                              		out.print("<p>");
	                              		out.print("Ingrese los datos descritos a continuación para luego revisar que todos los accesorios/dispositivos ingresados están correctos, al terminar presionar \"INSERTAR LISTA Y GUARDAR\".");
	                              		out.print("</p>");
                      					out.print("</div>");
				 						out.print("<form class='col col-12' action='./guardar' method='POST' name='frminsert' id='frminsert' onsubmit='confirmform(event, this, \"¿Esta seguro de Ingresar al sistema esta lista de Accesorios/Dispositivos?\")'>");
										out.print("<div class='col col-sm-12 inpform'>");
										out.print("<label for='txtPersonal' class='form-label'>");
										out.print("Ingrese el nombre completo de la persona que autoriza este movimiento");
										out.print("</label>");
										out.print("<div class='row'>");
										out.print("<div class='col col-sm-10'>");
										out.print("<div class='input-group'>");
										out.print("<span class='input-group-text' id='lblAutorizacion'>");
										out.print("<i class='bi bi-shield-shaded'></i>");
										out.print("</span>");
										out.print("<input name='txtAutorizacion' id='txtAutorizacion' aria-describedby='txtAutorizacion' placeholder='Autorizador' class='form-control flexdatalist'");
										out.print("autocomplete='off' type='text' spellcheck='false' maxlength='100' pattern='[A-Za-zÁÉÍÓÚáéíóú ]{1,100}' title='Alfabético de máximo 100 caracteres.' required ");
										out.print("value='" + autorizador + "' data-min-length='1' list='personal' />");
										out.print("<datalist id='personal'>");
										ArrayList<String> arraydatos = val.obtenerUsers();
										if (arraydatos.size() > 0) {
											for (int i = 0; i < arraydatos.size(); i++) {
												out.print("<option value='" + arraydatos.get(i) + "'>" + arraydatos.get(i) + "</option>");
											}
										}
										out.print("</datalist>");
										out.print("</div>");
										out.print("</div>");
										out.print("<div class='col col-sm-2'>");
										out.print("<input class='checkinv form-check-input puntero' type='checkbox' name='chkauto' id='chkauto'" + (yo.equals("1") ? " checked='checked' " : " ") + ">");
										out.print("<label for='chkauto' class='form-label puntero margliz'>");
										out.print("Yo");
										out.print("</label>");
										out.print("</div>");
										out.print("</div>");
										out.print("</div>");
										out.print("<div class='col col-sm-12 inpform'>");
										out.print("<label for='txtCaso' class='form-label'>");
										out.print("Ingrese el número del caso");
										out.print("</label>");
										out.print("<div class='input-group'>");
										out.print("<span class='input-group-text' id='lblCaso'>");
										out.print("<i class='bi bi-link'></i>");
										out.print("</span>");
										out.print("<input name='txtCaso' id='txtCaso' aria-describedby='txtCaso' placeholder='Número del caso' class='form-control'");
										out.print("autocomplete='off' type='number' min='0' max='99999999' spellcheck='false' maxlength='8' title='Numérico de mínimo 1 y máximo 8 digitos.' required");
										out.print("value='" + caso + "' oninput='numcaso()' />");
										out.print("</div>");
										out.print("</div>");
										out.print("<div class='col col-sm-12 inpform'>");
										out.print("<label for='txtReferencia' class='form-label'>");
										out.print("Ingrese la referencia del caso");
										out.print("</label>");
										out.print("<div class='input-group'>");
										out.print("<span class='input-group-text' id='lblReferencia'>");
										out.print("<i class='bi bi-link'></i>");
										out.print("</span>");
										out.print("<div class='col col col-sm-1'>");
										out.print("<input autocomplete='off' type='text' class='form-control'");
										out.print("name='txtRF' id='txtRF' value='" + (RF.equals("") ? "RF" : RF) + "' pattern='[A-Za-z]{1,6}' maxlength='6' ");
										out.print("placeholder='RF' ");
										out.print("required >");
										out.print("</div>");
										out.print("<div class='col col col-sm-1'>");
										out.print("<input autocomplete='off' type='text' class='form-control' ");
										out.print("name='G1' id='G1' value='-' disabled='disabled'");
										out.print("placeholder='-'");
										out.print("required='required'>");
										out.print("</div>");
										out.print("<div class='col col col-sm'>");
										out.print("<input name='txtReferencia1' id='txtReferencia1' aria-describedby='txtReferencia1' placeholder='No. Referencia' class='form-control'");
										out.print("autocomplete='off' type='number' min='0' max='99999' spellcheck='false' maxlength='5' title='Numérico de máximo 5 digitos.' required");
										out.print("value='" + referencia1 + "' />");
										out.print("</div>");
										out.print("<div class='col col col-sm-1'>");
										out.print("<input autocomplete='off' type='text' class='form-control' ");
										out.print("name='G2' id='G2' value='-' disabled='disabled'");
										out.print("placeholder='-'");
										out.print("required >");
										out.print("</div>");
										out.print("<div class='col col col-sm'>");
										out.print("<input name='txtReferencia2' id='txtReferencia2' aria-describedby='txtReferencia2' placeholder='No. Referencia' class='form-control'");
										out.print("autocomplete='off' type='number' min='0' max='99999' spellcheck='false' maxlength='5' title='Numérico de máximo 5 digitos.' required ");
										out.print("value='" + referencia2 + "' />");
										out.print("</div>");
										out.print("<div class='col col col-sm-1'>");
										out.print("<input autocomplete='off' type='text' class='form-control' ");
										out.print("name='G3' id='G3' value='-' disabled='disabled'");
										out.print("placeholder='-'");
										out.print("required >");
										out.print("</div>");
										out.print("<div class='col col col-sm'>");
										out.print("<input name='txtReferencia3' id='txtReferencia3' aria-describedby='txtReferencia3' placeholder='No. Referencia' class='form-control'");
										out.print("autocomplete='off' type='number' min='0' max='99999' spellcheck='false' maxlength='5' title='Numérico de máximo 5 digitos.' readonly required ");
										out.print("value='" + referencia3 + "' />");
										out.print("</div>");
										out.print("</div>");
										out.print("</div>");
										out.print("<div class='col col-sm-12 inpform'>");
										out.print("<label for='txtComentario' class='form-label'>Ingrese un comentario <i id='cursiva'>(Opcional)</i>");
										out.print("</label>");
										out.print("<div class='input-group'>");
										out.print("<span class='input-group-text' id='lblComentario'>");
										out.print("<i class='bi bi-chat-left-dots-fill'></i>");
										out.print("</span>");
										out.print("<input name='txtComentario' id='txtComentario' aria-describedby='txtComentario' placeholder='Comentario' class='form-control'");
				 						out.print("autocomplete='off' type='text' spellcheck='false' maxlength='500'");
		 								out.print("value='" + comentarioing + "' />");
										out.print("</div>");
										out.print("</div>");
				 						out.print("<div id='ingfin2' class='btn-group col col-sm-12'>");
										out.print("<button type='submit' class='btn btn-guardar' data-bs-toggle='tooltip' data-bs-placement='left' title='Terminar el proceso y guardar.'>INSERTAR LISTA Y GUARDAR</button>");
										out.print("</div>");
				 						out.print("</form>");
				 						out.print("<div class='col col-12'>");
				 						out.print("<ul>");
				 						for(int i = 0; i < cantidad; i++) {
							 				query = "SELECT Id, Codigo, Tipo, Nombre, Cantidad, Ubicacion, Comentario, Marca, Modelo, Serie, ActivoFijo FROM Productos WHERE Id = '" + registros.get(i).get(0) + "' AND Estado = '0' ORDER BY Id ASC LIMIT 1"; 
							 				ArrayList<String> productos = layout.getRow(query);
							 				out.print("<li><a class='ainvent' href='#' data-bs-toggle='modal' data-bs-target='#producto" + productos.get(0) + "' title='Click para editar.'>");
							 				out.print(productos.get(3));
							 				if(productos.get(7) != null && !productos.get(7).trim().equals("-") && !productos.get(7).trim().equals("")) {
							  					out.print(", Marca: " + productos.get(7));
							  				}
							  				if(productos.get(8) != null && !productos.get(8).trim().equals("-") && !productos.get(8).trim().equals("")) {
							  					out.print(", Modelo: " + productos.get(8));
							  				}
							  				if(productos.get(9) != null && !productos.get(9).trim().equals("-") && !productos.get(9).trim().equals("")) {
							  					out.print(", Serie: " + productos.get(9));
							  				}
							  				if(productos.get(10) != null && !productos.get(10).trim().equals("-") && !productos.get(10).trim().equals("")) {
							  					out.print(", Activo Fijo: " + productos.get(10));
							  				}
							 				out.print(" - (Revisar/Editar <i class='bi bi-pencil-square'></i>)</a></li>");
								  			out.print("<div class='modal fade' id='producto" + productos.get(0) + "' tabindex='-1' aria-labelledby='producto" + productos.get(0) + "Label' aria-hidden='true'>");
								  			out.print("<div class='modal-dialog modal-dialog-scrollable'>");
								  			out.print("<div class='modal-content'>");
								  			out.print("<div class='modal-header'>");
								  			out.print("<h5 class='modal-title' id='producto" + productos.get(0) + "Label'>" + productos.get(3) + "</h5>");
								  			out.print("<button type='button' class='btn-close' data-bs-dismiss='modal' aria-label='Close'></button>");
								  			out.print("</div>");
								  			out.print("<div class='modal-body'>");
		                              		out.print("<div class='expli-just'>");
		                              		out.print("<p>");
		                              		out.print("Revise los datos ingresados, si alguno esta incorrecto cambiarlo y presionar \"MODIFICAR\"; si al contrario desea deshacerse estos datos presionar \"ELIMINAR\".");
		                              		out.print("</p>");
	                      					out.print("</div>");
							 				out.print("<form class='col col-12' action='modificar?id=" + productos.get(0) + "' method='POST' name='frmsave" + productos.get(0) + "' id='frmsave' onsubmit='confirmform(event, this, \"¿Esta seguro de realizar esta acción?\")'>");
							 				out.print("<div class='col col-sm-12 inpform'>");
							 				out.print("<label for='txtCodigo' class='form-label'>Código del accesorio/dispositivo:</label>");
							 				out.print("<div class='input-group'>");
							 				out.print("<span class='input-group-text' id='txtCodigo'><i class='bi bi-flag-fill'></i></span>");
							 				out.print("<input name='txtCodigo' id='txtCodigo' aria-describedby='txtCodigo' placeholder='Codigo' class='form-control' title='Alfanumérico de entre 8 y 9 caracteres.'");
					 						out.print("autocomplete='off' type='text' spellcheck='false' maxlength='9' pattern='[A-Za-z0-9]{1,9}' required ");
			 								out.print("value='" + productos.get(1) + "'/>");
							 				out.print("</div>");
							 				out.print("</div>");
							 				out.print("<div class='col col-sm-12 inpform'>");
							 				out.print("<label for='txtNombre' class='form-label'>Ingrese el nombre del accesorio/dispositivo</label>");
							 				out.print("<div class='input-group'>");
							 				out.print("<span class='input-group-text' id='txtNombre'><i class='bi bi-clipboard'></i></span>");
							 				out.print("<input name='txtNombre' id='txtNombre' aria-describedby='txtNombre' placeholder='Nombre' class='form-control flexdatalist' title='Mínimo 1 y máximo 150 caracteres.'");
					 						out.print("autocomplete='off' type='text' spellcheck='false' maxlength='150' required ");
			 								out.print("value='" + productos.get(3) + "' data-min-length='1' list='accesorio'/>");
							 				out.print("</div>");
							 				out.print("</div>");
							 				out.print("<div class='col col-sm-12 inpform'>");
					 						out.print("<label for='txtMarca' class='form-label'>Ingrese la marca <i id='cursiva'>(Opcional)</i>");
	 										out.print("</label>");
											out.print("<div class='input-group'>");
											out.print("<span class='input-group-text' id='txtMarca'>");
											out.print("<i class='bi bi-laptop'></i>");
											out.print("</span>");
											out.print("<input name='txtMarca' id='txtMarca' aria-describedby='txtMarca' placeholder='Marca' class='form-control flexdatalist' title='Mínimo 1 y máximo 100 caracteres.'");
					 						out.print("autocomplete='off' type='text' spellcheck='false' maxlength='100' ");
			 								out.print("value='" + (productos.get(7).trim().equals("-") ? "" : productos.get(7)) + "' data-min-length='1' list='marca'/>");
											out.print("</div>");
											out.print("</div>");
											out.print("<div class='col col-sm-12 inpform'>");
											out.print("<label for='txtModelo' class='form-label'>Ingrese el modelo <i id='cursiva'>(Opcional)</i>");
											out.print("</label>");
											out.print("<div class='input-group'>");
											out.print("<span class='input-group-text' id='txtModelo'>");
											out.print("<i class='bi bi-laptop'></i>");
											out.print("</span>");
											out.print("<input name='txtModelo' id='txtModelo' aria-describedby='txtModelo' placeholder='Modelo' class='form-control' title='Mínimo 1 y máximo 100 caracteres.'");
					 						out.print("autocomplete='off' type='text' spellcheck='false' maxlength='100' ");
			 								out.print("value='" + (productos.get(8).trim().equals("-") ? "" : productos.get(8)) + "'/>");
											out.print("</div>");
											out.print("</div>");
											out.print("<div class='col col-sm-12 inpform'>");
											out.print("<label for='txtSerie' class='form-label'>Ingrese la serie <i id='cursiva'>(Opcional)</i>");
											out.print("</label>");
											out.print("<div class='input-group'>");
											out.print("<span class='input-group-text' id='txtSerie'>");
											out.print("<i class='bi bi-laptop'></i>");
											out.print("</span>");
											out.print("<input name='txtSerie' id='txtSerie' aria-describedby='txtSerie' placeholder='Serie' class='form-control' title='Mínimo 1 y máximo 100 caracteres.'");
					 						out.print("autocomplete='off' type='text' spellcheck='false' maxlength='100' ");
			 								out.print("value='" + (productos.get(9).trim().equals("-") ? "" : productos.get(9)) + "'/>");
											out.print("</div>");
											out.print("</div>");
											out.print("<div class='col col-sm-12 inpform'>");
											out.print("<label for='txtActivoFijo' class='form-label'>Ingrese el activo fijo <i id='cursiva'>(Opcional)</i>");
											out.print("</label>");
											out.print("<div class='input-group'>");
											out.print("<span class='input-group-text' id='txtActivoFijo'>");
											out.print("<i class='bi bi-laptop'></i>");
											out.print("</span>");
											out.print("<input name='txtActivoFijo' id='txtActivoFijo' aria-describedby='txtActivoFijo' placeholder='Activo Fijo' class='form-control' title='Mínimo 1 y máximo 100 caracteres.'");
					 						out.print("autocomplete='off' type='number' spellcheck='false' maxlength='20' pattern='[0-9]{14, 20}' ");
			 								out.print("value='" + (productos.get(10).trim().equals("-") ? "" : productos.get(10)) + "'/>");
											out.print("</div>");
											out.print("</div>");
							 				out.print("<div class='col col-sm-12 inpform'>");
							 				out.print("<label for='cmbTipos' class='form-label'>Ingrese la clasificación del accesorio/dispositivo</label>");
							 				out.print("<select class='form-select clasificacion-" + productos.get(0) + "' aria-label='Clasificación de Accesorios/Dispositivos' required name='cmbTipos'>");
											if(datos != null) {
								  				int elementos = datos.size();
								  				for (int j = 0; j < elementos; j++) {
								  					if(productos.get(2).equals(datos.get(j).get(0))) {
								  						out.print("<option value=\"" + datos.get(j).get(1) + "\" selected>" + datos.get(j).get(2) + "</option>");
								  					} else {
								  						out.print("<option value=\"" + datos.get(j).get(1) + "\">" + datos.get(j).get(2) + "</option>");	
								  					}
								  				}
											}
							 				out.print("</select>");
							 				out.print("</div>");
							 				out.print("<div class='col col-sm-12 inpform'>");
							 				out.print("<label for='txtUbicacion' class='form-label'>Ingrese la ubicación</label>");
							 				out.print("<div class='input-group'>");
							 				out.print("<span class='input-group-text' id='txtUbicacion'><i class='bi bi-geo-alt-fill'></i></span>");
							 				out.print("<input name='txtUbicacion' id='txtUbicacion' aria-describedby='txtUbicacion' placeholder='Ubicación' class='form-control flexdatalist' title='Alfanumérico de mínimo 1 y máximo 100 caracteres.'");
					 						out.print("autocomplete='off' type='text' spellcheck='false' maxlength='100' required ");
			 								out.print("value='" + productos.get(5) + "' data-min-length='1' list='ubicacion'/>");
							 				out.print("</div>");
							 				out.print("</div>");
							 				out.print("<div class='col col-sm-12 inpform'>");
							 				out.print("<label for='txtCantidad' class='form-label'>Ingrese la cantidad</label>");
							 				out.print("<div class='input-group'>");
							 				out.print("<span class='input-group-text' id='txtCantidad'><i class='bi bi-hash'></i></span>");
							 				out.print("<input name='txtCantidad' id='txtCantidad' aria-describedby='txtCantidad' placeholder='Cantidad' title='Mínimo 1 y máximo 5 dígitos' class='form-control'");
					 						out.print("autocomplete='off' type='number' spellcheck='false' maxlength='5' min='0' max='99999' required ");
			 								out.print("value='" + productos.get(4) + "' />");
							 				out.print("</div>");
							 				out.print("</div>");
							 				out.print("<div class='col col-sm-12 inpform'>");
							 				out.print("<label for='txtComentario' class='form-label'>Ingrese un comentario <i id='cursiva'>(Opcional)</i></label>");
							 				out.print("<div class='input-group'>");
							 				out.print("<span class='input-group-text' id='txtComentario'><i class='bi bi-chat-left-dots-fill'></i></span>");
							 				out.print("<input name='txtComentario' id='txtComentario' aria-describedby='txtComentario' placeholder='Comentario' class='form-control'");
					 						out.print("autocomplete='off' type='text' spellcheck='false' maxlength='500'");
			 								out.print("value='" + productos.get(6) + "' />");
							 				out.print("</div>");
							 				out.print("</div>");
							 				out.print("<div class='btn-group col col-12 col-sm-12'>");
							 				out.print("<button type='' class='btn btn-guardar duplex' data-bs-toggle='tooltip' data-bs-placement='bottom' title='Guardar solo las correcciones de este accesorio/dispositivo.'>MODIFICAR</button>");
							 				out.print("<button type='' class='btn btn-guardar duplex-eliminar' onclick='document.frmsave" + productos.get(0) + ".action = \"eliminar?inv=" + idInventario.get(0) + "&id=" + productos.get(0) + "\"' data-bs-toggle='tooltip' data-bs-placement='top' title='Eliminar de la lista.'>ELIMINAR</button>");
							 				out.print("</div>");
							 				out.print("</form>");
								  			out.print("</div>");
								  			out.print("</div>");
								  			out.print("</div>");
								  			out.print("</div>");
				 						}
				 						out.print("</ul>");
				 						out.print("</div>");
				 						out.print("</div>");
				 						out.print("</div>");
					 				}
			 					}
				 				if(existId > 0) {
					 				if(acciones > 0) {
				 						out.print("<div class='col-12 col-md-6 margsup-min'>");
				 						out.print("<div id='divInv' class='col col-12 col-lg-11'>");	
					 				}
					 				else {
					 					out.print("<div class='col-12'>");
				 						out.print("<div id='divInv' class='col col-12 col-sm-8 col-md-7 col-lg-7'>");
					 				}
			 					}
			 					else {
			 						out.print("<div class='col-12'>");
			 						out.print("<div id='divInv' class='col col-12 col-sm-8 col-md-7 col-lg-8'>");
			 					}
			 				%>
                            <% //Formulario Inicial %>
                      		<div class="expli-just">
	                         	<p>
	                         		Ingrese los datos descritos a continuación para generar un nuevo accesorio/dispositivo en el sistema. Cuando los datos estén revisados presionar "AGREGAR A LA LISTA DE INGRESOS".
	                         	</p>
                       		</div>
                            <form class="col col-12" action="insertar" method="POST" name="frmsave" id="frmsave" onsubmit="confirmform(event, this, '¿Esta seguro de Agregar a la lista este Accesorio/Dispositivo?')">
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
                                        <div class="col <%= (accesos.get(1).equals("0") ? "col-sm-12" : "col-sm-8") %>">
                                            <select class="form-select clasificacion" aria-label="Clasificación de Accesorios/Dispositivos" required name="cmbTipos">
                                                <option selected disabled="disabled">Clasificación de Accesorios/Dispositivos</option>
                                                <% 
													if(datos != null) {
										  				int elementos = datos.size();
										  				for (int j = 0; j < elementos; j++) {
									  						out.print("<option value=\"" + datos.get(j).get(1) + "\"" + (tipo.equals(datos.get(j).get(1)) ? " selected " : " ") + ">" + datos.get(j).get(2) + "</option>");	
										  				}
													}
												%>
                                          	</select>
                                       	</div>
                                       	<%
                                       		if(accesos.get(1).equals("1")) {
                                       	%>
	                                        <div class="col col-sm-4 btn-mini">
	                                        	<a class="btn btn-ingresar" href="../tipo_producto/guardar?return=1" data-bs-toggle="tooltip" data-bs-placement="right" title="Agregar nueva clasificación.">
	                                            	 <i class="bi bi-plus-circle-fill"></i> NUEVO
	                                            </a>
	                                        </div>
	                                    <%
                                       		}
	                                    %>
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
											value="<%= txtcantidad %>" />
                                    </div>
                                </div>
                                <div class="col col-sm-12 inpform">
                                    <label for="txtComentario" class="form-label">Ingrese un comentario <i id="cursiva">(Opcional)</i></label>
                                    <div class="input-group">
                                        <span class="input-group-text" id="txtComentario">
                                            <i class="bi bi-chat-left-dots-fill"></i>
                                        </span>
                                        <input name="txtComentario" id="txtComentario" aria-describedby="txtComentario" placeholder="Comentario" class="form-control"
											autocomplete="off" type="text" spellcheck="false" maxlength="500"
											value="<%= comentario %>" />
                                    </div>
                                </div>
                                <div class="btn-group col-12 col-sm-12">
                                    <button type="submit" class="btn btn-guardar" data-bs-toggle="tooltip" data-bs-placement="bottom" title="Agregar a una lista antes de guardar y terminar.">AGREGAR A LA LISTA DE INGRESOS</button>
                                </div>
                            </form>
                            <%
					 			out.print("</div>");
								out.print("</div>");
                            %>
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
			if(session.getAttribute("succInsertP") != null && !session.getAttribute("succInsertP").equals("")) {
				out.print(val.mostrarToastr("Éxito", "Proceso realizado correctamente."));
				session.setAttribute("succInsertP", null);
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
			var numcaso = function () {
				let caso = $("#txtCaso").val();
				$("#txtReferencia3").val(caso);
			}
			<% 
				if(existId > 0) {
	 				if(acciones > 0) {
	 					for(int i = 0; i < cantidad; i++) {
	 						query = "SELECT Id, Codigo, Tipo, Nombre, Cantidad, Ubicacion, Comentario FROM Productos WHERE Id = '" + registros.get(i).get(0) + "' AND Estado = '0' ORDER BY Id ASC LIMIT 1"; 
			 				ArrayList<String> productos = layout.getRow(query);
			 				out.print("$('.clasificacion-" + productos.get(0) + "').select2({");
	 						out.print("dropdownParent: $('#producto" + productos.get(0)  + "')");
	 						out.print("});");
	 					}
	 				}
				}
			%>
		</script>
    </body>
</html>
