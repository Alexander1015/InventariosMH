<%@page import="java.util.ArrayList"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="lib.Validacion"%>
<%@page import="lib.Plantilla"%>
<%
	Plantilla layout = new Plantilla();
	Validacion val = new Validacion();
%>
<!DOCTYPE html>
<html lang="es">
    <head>
        <% out.print(layout.header("Perfil", 2)); %>
    </head>
    <body>
        <%
        	String iduser = (String)session.getAttribute("id");
			out.print(layout.navbar("Perfil", iduser, (String)session.getAttribute("token"), 2));
			//Inicio del Contenido
			String query = "SELECT Nombres, Apellidos, Usuario, Password FROM Usuarios WHERE Id = '" + (String)session.getAttribute("id") + "'";
			ArrayList<String> datosobt = layout.getRow(query);
			int verif = datosobt.size();
			String nombre = "";
			String apellido = "";
			String usuario = "";
			String contra = "";
            query = "SELECT Defecto FROM Usuarios WHERE Id = '" + iduser + "' ORDER BY Id DESC LIMIT 1";
			ArrayList<String> usuariobd = layout.getRow(query);
			if(verif > 0) {
				nombre = datosobt.get(0);
				apellido = datosobt.get(1);
				usuario = datosobt.get(2);
				//Accesos
				query = "SELECT ReadProducto, ReadBitacora  FROM Accesos WHERE IdUsuario = '" + iduser + "'";
				ArrayList<String> accesos = val.getRow(query);
				if(accesos.size() == 0) {
					accesos.add(0, "0");
					accesos.add(1, "0");
				}
		%>
	        <div id="panPrincipal" class="col col-md-12">
	            <div class="row">
	                <div class="col col-sm-12">
   	                   <%
	                		if(accesos.get(0).equals("1") || accesos.get(1).equals("1")) {
	                			out.print("<a class='btn btn-regresar' href='../' role='button' data-bs-toggle='tooltip' data-bs-placement='left' title='Regresar a Dashboard.'>");
	                			out.print("<i class='bi bi-chevron-double-left'></i>");
	                			out.print("IR A DASHBOARD");
	                			out.print("</a>");
	                		}
	                	%>
	                	<p class="encabezadodash">
	                        PERFIL
	                        <span class="subencab"> / <% out.print((nombre + " " + apellido).toUpperCase()); %></span>
	                    </p>
	                    <form id="divInv" class="col col-sm-12 col-md-8 col-lg-6" action="../perfil/modificar" method="POST" name="frmsave" id="frmsave" onsubmit="confirmform(event, this, '¿Esta seguro de Guardar estos datos?')">
	                        <%
	                        	if(usuariobd.get(0).equals("0")) {
	                        		out.print("<div class='oculto'>");
	                        	}
	                        %>
		                        <div class="expli-just">
			                      	<p>
			                      		Los datos descritos a continuación pertenecen a su usuario, si desea modificar la información mostrada presione "MODIFICAR INFORMACIÓN", en cambio sí cambiara su contraseña presione "ACTUALIZAR CONTRASEÑA".
			                      	</p>
		                   		</div>
		                        <div class="col col-sm-12 inpform">
		                            <label for="txtNombre" class="form-label">Nombres en el sistema</label>
		                            <div class="input-group">
		                                <span class="input-group-text" id="lblNombre">
		                                    <i class="bi bi-person-fill"></i>
		                                </span>
		                                <input name="txtNombre" id="txtNombre" aria-describedby="txtNombre" placeholder="Nombre" class="form-control"
											autocomplete="off" type="text" spellcheck="false" maxlength="250" pattern="[A-Za-zÁÉÍÓÚáéíóú ]{2,250}" title="Alfabético de mínimo 2 y máximo 250 caracteres." readonly
											value="<%= nombre %>" />
		                            </div>
		                        </div>
		                        <div class="col col-sm-12 inpform">
		                            <label for="txtApellido" class="form-label">Apellidos en el sistema</label>
		                            <div class="input-group">
		                                <span class="input-group-text" id="lblApellido">
		                                    <i class="bi bi-person-fill"></i>
		                                </span>
		                                <input name="txtApellido" id="txtApellido" aria-describedby="txtApellido" placeholder="Apellido" class="form-control"
											autocomplete="off" type="text" spellcheck="false" maxlength="250" pattern="[A-Za-zÁÉÍÓÚáéíóú ]{2,250}" title="Alfabético de mínimo 2 y máximo 250 caracteres." readonly
											value="<%= apellido %>" />
		                            </div>
		                        </div>
		                        <div class="col col-sm-12 inpform">
		                            <label for="txtUsuario" class="form-label">Usuario en el sistema</label>
		                            <div class="input-group">
		                                <span class="input-group-text" id="lblUsuario">
		                                    <i class="bi bi-person-fill"></i>
		                                </span>
		                                <input name="txtUsuario" id="txtUsuario" aria-describedby=txtUsuario placeholder="Usuario" class="form-control"
											autocomplete="off" type="text" spellcheck="false" maxlength="100" pattern="[A-Za-zÁÉÍÓÚáéíóú ]{5,100}" title="Alfanumerico de mínimo 5 y máximo 100 caracteres." readonly
											value="<%= usuario %>" />
		                            </div>
		                        </div>
	                       	<%
		                       	if(usuariobd.get(0).equals("0")) {
	                        		out.print("</div>");
	                        	}
								if(session.getAttribute("mensperfil") != null && !session.getAttribute("mensperfil").equals("")) {
									out.print(val.mostrarAlert(0, session.getAttribute("mensperfil") .toString()));
									session.setAttribute("mensperfil", null);
								}
	                        	if(!usuariobd.get(0).equals("0")) {
	                        %>
       	                        <div class='btn-group col col-12 col-sm-12' id="conjuntoduples">
		                        	<a class='btn btn-guardar duplex' onclick="mostrarModify()" data-bs-toggle='tooltip' data-bs-placement='bottom' title='Modificar solamente mi información personal.'>MODIFICAR INFORMACIÓN</a>
		                        	<a class='btn btn-guardar duplex btnbordeizq' onclick="mostrarPassword()" data-bs-toggle='tooltip' data-bs-placement='bottom' title='Actualizar mi contraseña'>ACTUALIZAR CONTRASEÑA</a>
		                        </div>
		                        <div id="modifycontra" class="oculto">
			                        <div class="btn-group col col-sm-12">
										<button type="submit" class="btn btn-guardar duplex" onclick="document.frmsave.action = '../perfil/modificar?tipo=1'" data-bs-toggle="tooltip" data-bs-placement="bottom" title="Actualizar mi información del sistema.">GUARDAR INFORMACIÓN Y FINALIZAR</button>
										<a class="btn btn-guardar duplex-eliminar" onclick="cancelar()" data-bs-toggle="tooltip" data-bs-placement="bottom" title="Cancelar proceso.">CANCELAR</a>
		                        	</div>
		                        </div>
		                        <div id="viewcontra" class="oculto">
		                    <%
	                        	}
	                        	else {
	                        		out.print("<div>");
	                        	}
		                    %>
		                        <%
			            			if(usuariobd.get(0).equals("0")) {
			            				out.print("<div class='marginf'>");
			            				out.print(val.mostrarAlert(0, "Debe actualizar la contraseña, debido a que la suministrada no es personal."));
			            				out.print("</div>");
			            			}
			            			else {
			            				out.print("<hr>");
			            			}
								%>
		                         <div class="col col-sm-12 inpform">
									<label for="txtContraAnt" class="form-label">Ingrese la contraseña actual</label>
									<div class="input-group">
									  <span class="input-group-text" id="lblContra"><i class="bi bi-asterisk"></i></span>
									   <input name="txtContraAnt" id="txtContraAnt" aria-describedby=txtContraAnt placeholder="Contraseña" class="form-control"
											autocomplete="off" type="password" spellcheck="false" maxlength="50" pattern="[A-Za-z0-9]{14,50}" title="Alfanumerico de mínimo 14 y máximo 50 caracteres." <% out.print(!usuariobd.get(0).equals("0") ? "readonly" : ""); %>
											/>
									  <div class="input-group-append">
		                              	<button class="mostrarContra btn" type="button" onclick="showPasswordAnt()" title="Revelar/Ocultar contraseña.">
		                              		<i class="bi bi-eye-slash iconAnt"></i>
		                              	</button>
		                              </div>
									</div>
								</div>
		                        <div class="col col-sm-12 inpform">
									<label for="txtContra" class="form-label">Ingrese la nueva contraseña</label>
									<div class="input-group">
									  <span class="input-group-text" id="lblContra"><i class="bi bi-asterisk"></i></span>
									  <input name="txtContra" id="txtContra" aria-describedby=txtContra placeholder="Contraseña" class="form-control"
											autocomplete="off" type="password" spellcheck="false" maxlength="50" pattern="[A-Za-z0-9]{14,50}" title="Alfanumerico de mínimo 14 y máximo 50 caracteres." <% out.print(!usuariobd.get(0).equals("0") ? "readonly" : ""); %>
											/>
									  <div class="input-group-append">
		                              	<button class="mostrarContra btn" type="button" onclick="showPassword()" title="Revelar/Ocultar contraseña.">
		                              		<i class="bi bi-eye-slash icon1"></i>
		                              	</button>
		                              </div>
									</div>
								</div>
								<div class="col col-sm-12 inpform">
									<label for="txtContraRep" class="form-label">Verifique la contraseña ingresada</label>
									<div class="input-group">
									  <span class="input-group-text" id="lblContraRep"><i class="bi bi-asterisk"></i></span>
									  <input name="txtContraRep" id="txtContraRep" aria-describedby=txtContraRep placeholder="Contraseña" class="form-control"
											autocomplete="off" type="password" spellcheck="false" maxlength="50" pattern="[A-Za-z0-9]{14,50}" title="Alfanumerico de mínimo 14 y máximo 50 caracteres." <% out.print(!usuariobd.get(0).equals("0") ? "readonly" : ""); %>
											/>
									  <div class="input-group-append">
		                              	<button class="mostrarContra btn" type="button" onclick="showPasswordRep()" title="Revelar/Ocultar contraseña.">
		                              		<i class="bi bi-eye-slash icon2"></i>
		                              	</button>
		                              </div>
									</div>
								</div>
		                        <div class="btn-group col col-sm-12">
		                        	<%
		                        		if(!usuariobd.get(0).equals("0")) {
		                        	%>
										<button type="submit" onclick="document.frmsave.action = '../perfil/modificar?tipo=2'" class="btn btn-guardar duplex" data-bs-toggle="tooltip" data-bs-placement="bottom" title="Actualizar mi contraseña.">ACTUALIZAR CONTRASEÑA</button>
			                        	<a class="btn btn-guardar duplex-eliminar" onclick="cancelar()" data-bs-toggle="tooltip" data-bs-placement="bottom" title="Cancelar proceso.">CANCELAR</a>
			                        <%
		                        		}
		                        		else {
		                        			out.print("<button type='submit' onclick='document.frmsave.action = \"../perfil/modificar?tipo=3\"' class='btn btn-guardar' data-bs-toggle='tooltip' data-bs-placement='bottom' title='Actualizar mi contraseña.'>ACTUALIZAR CONTRASEÑA</button>");
		                        		}
			                        %>
		                        </div>
		                	</div>
	                    </form>
	                </div>
	            </div>
	        </div>
			<%
				}
				else {
			%>
				<script type="text/javascript">
					window.location.href = "../desconectar";
				</script>
			<%
				}
				//fin del contenido
				out.print(layout.footer(2));
			%>
			<script type="text/javascript">
				function showPassword() {
					var cambio = document.getElementById("txtContra");
					if (cambio.type == "password") {
						cambio.type = "text";
						$('.icon').removeClass('bi bi-eye-slash').addClass('bi bi-eye');
					} else {
						cambio.type = "password";
						$('.icon').removeClass('bi bi-eye').addClass('bi bi-eye-slash');
					}
				}
				
				function showPasswordAnt() {
					var cambio = document.getElementById("txtContraAnt");
					if (cambio.type == "password") {
						cambio.type = "text";
						$('.icon').removeClass('bi bi-eye-slash').addClass('bi bi-eye');
					} else {
						cambio.type = "password";
						$('.icon').removeClass('bi bi-eye').addClass('bi bi-eye-slash');
					}
				}
				function showPasswordRep() {
					var cambio = document.getElementById("txtContraRep");
					if (cambio.type == "password") {
						cambio.type = "text";
						$('.icon').removeClass('bi bi-eye-slash').addClass('bi bi-eye');
					} else {
						cambio.type = "password";
						$('.icon').removeClass('bi bi-eye').addClass('bi bi-eye-slash');
					}
				}
			</script>
			<script type="text/javascript">
				$(document).ready(function() {
					cancelar();
				});
				
				function mostrarModify() {
					$("#txtNombre").prop("readonly", false);
					$("#txtApellido").prop("readonly", false);
					$("#txtUsuario").prop("readonly", false);
					$("#txtContraAnt").val("");
					$("#txtContra").val("");
					$("#txtContraRep").val("");
					$("#txtContraAnt").prop("readonly", true);
					$("#txtContra").prop("readonly", true);
					$("#txtContraRep").prop("readonly", true);
					$("#conjuntoduples").hide();
					$("#modifycontra").show();
					$("#viewcontra").hide();
				}
				
				function mostrarPassword() {
					$("#txtNombre").prop("readonly", true);
					$("#txtApellido").prop("readonly", true);
					$("#txtUsuario").prop("readonly", true);
					$("#txtContraAnt").prop("readonly", false);
					$("#txtContra").prop("readonly", false);
					$("#txtContraRep").prop("readonly", false);
					$("#txtContraAnt").val("");
					$("#txtContra").val("");
					$("#txtContraRep").val("");
					$("#conjuntoduples").hide();
					$("#modifycontra").hide();
					$("#viewcontra").show();
					$("#txtContraAnt").focus();
				}
				
				function cancelar() {
                	<%
	            		if(usuariobd.get(0).equals("0")) {
	            	%>
						$("#txtContraAnt").prop("readonly", false);
						$("#txtContra").prop("readonly", false);
						$("#txtContraRep").prop("readonly", false);
	            	<%
	            		}
	            		else {
	            	%>
						$("#txtContraAnt").prop("readonly", true);
						$("#txtContra").prop("readonly", true);
						$("#txtContraRep").prop("readonly", true);
	            	<%	
	            		}
	            	%>
					$("#txtNombre").prop("readonly", true);
					$("#txtApellido").prop("readonly", true);
					$("#txtUsuario").prop("readonly", true);
					$("#txtNombre").val(<%= "'" + nombre + "'" %>);
					$("#txtApellido").val(<%= "'" + apellido + "'" %>);
					$("#txtUsuario").val(<%= "'" + usuario + "'" %>);
					$("#txtContraAnt").val("");
					$("#txtContra").val("");
					$("#txtContraRep").val("");
					$("#conjuntoduples").show();
					$("#modifycontra").hide();
					$("#viewcontra").hide();
				}
			</script>
	</body>
</html>
