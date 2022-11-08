<%@page import="java.util.ArrayList"%>
<%@page import="java.sql.ResultSet"%>
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
			String id = (String) session.getAttribute("idtab");
			query = "SELECT CreateUsuario, UpdateUsuario FROM Accesos WHERE IdUsuario = '" + iduser + "'";
			ArrayList<String> accesos = val.getRow(query);
			if(accesos.size() == 0) {
				accesos.add(0, "0");
				accesos.add(1, "0");
			}
			if(accesos.get(0).equals("0") && (id == null || id.trim().equals(""))) {
				out.print("<script type=\"text/javascript\">");
				out.print("window.location.href = \"../usuario\"");
				out.print("</script>");
			}
			else if(accesos.get(1).equals("0") && id != null && !id.trim().equals("")) {
				out.print("<script type=\"text/javascript\">");
				out.print("window.location.href = \"../usuario\"");
				out.print("</script>");
			}
			//Fin
			String nombre = "", apellido = "", usuario = "", contra = "";
			String title = "INGRESOS", form = "", linea = "";
			int cambio = 0;
			if (id != null && !id.equals("")) {
				query = "SELECT Nombres, Apellidos, Usuario, Password FROM Usuarios WHERE Id = '" + id + "'";
				ArrayList<String> datosobt = layout.getRow(query);
				if(datosobt.size() > 0)
				{
					nombre = datosobt.get(0);
					apellido = datosobt.get(1);
					usuario = datosobt.get(2);
					contra = datosobt.get(3);
					form = "?id=" + id;
					title = "CONSULTAS / MODIFICACIONES";
					linea = "<hr>";
					cambio++;
				}
			}
			else {
				if (session.getAttribute("retusrnombre") != null && !session.getAttribute("retusrnombre").equals("")) {
					nombre = (String) session.getAttribute("retusrnombre");
				}
				if (session.getAttribute("retusrapellido") != null && !session.getAttribute("retusrapellido").equals("")) {
					apellido = (String) session.getAttribute("retusrapellido");
				}
				if (session.getAttribute("retusrusuario") != null && !session.getAttribute("retusrusuario").equals("")) {
					usuario = (String) session.getAttribute("retusrusuario");
				}
			}
			String retrn = (String) request.getSession().getAttribute("returnuser");
			String link = "./";
			if(retrn != null && !retrn.trim().equals("")) {
				link += "vista";
			}
		%>
		<div id="panPrincipal" class="col col-md-12">
		 	<div class="row">
		 		<div class="col col-sm-12">
			 		<a class="btn btn-regresar" href="<%= link %>" role="button" data-bs-toggle="tooltip" data-bs-placement="left" title="Regresar al sitio anterior.">
	                        <i class="bi bi-chevron-double-left"></i>
	                        REGRESAR</a>
                   <p class="encabezadodash">
                        <a href="../usuario">MANEJO DE USUARIOS</a>
                        <span class="subencab">
                            / <%= title %></span>
                    </p>
                     <form id="divInv" class="col col-sm-12 col-md-8 col-lg-6" action="guardar<%= form %>" method="POST" name="frmsave" id="frmsave" onsubmit="confirmform(event, this, '¿Esta seguro de Guardar estos datos?')">
			 			<div class="expli-just">
	                      	<p>
	                      		<% out.print(cambio > 0 ? "Modifique" : "Ingrese"); %> los datos descritos a continuación para generar un nuevo usuario en el sistema. Cuando los datos estén revisados presionar "GUARDAR Y TERMINAR"<% out.print(cambio > 0 ? "" : " o en su defecto la opción secundaria \"GUARDAR Y AGREGAR OTRO\""); %>.
	                      	</p>
                   		</div>
			 			<div class="col col-sm-12 inpform">
							<label for="txtNombre" class="form-label">Ingrese los Nombres</label>
							<div class="input-group">
							  <span class="input-group-text" id="txtNombre"><i class="bi bi-person-fill"></i></span>
							  <input name="txtNombre" id="txtNombre" aria-describedby="txtNombre" placeholder="Nombre" class="form-control"
									autocomplete="off" type="text" spellcheck="false" maxlength="250" pattern="[A-Za-zÁÉÍÓÚáéíóú ]{2,250}" title="Alfabético de mínimo 2 y máximo 250 caracteres." required
									value="<%= nombre %>" />
							</div>
						</div>
						<div class="col col-sm-12 inpform">
							<label for="txtApellido" class="form-label">Ingrese los Apellidos</label>
							<div class="input-group">
							  <span class="input-group-text" id="txtApellido"><i class="bi bi-person-fill"></i></span>
							  <input name="txtApellido" id="txtApellido" aria-describedby="txtApellido" placeholder="Apellido" class="form-control"
									autocomplete="off" type="text" spellcheck="false" maxlength="250" pattern="[A-Za-zÁÉÍÓÚáéíóú ]{2,250}" title="Alfabético de mínimo 2 y máximo 250 caracteres." required
									value="<%= apellido %>" />
							</div>
						</div>
						<div class="col col-sm-12 inpform">
							<label for="txtUsuario" class="form-label">Ingrese el Usuario</label>
							<div class="input-group">
							  <span class="input-group-text" id="txtUsuario"><i class="bi bi-person-fill"></i></span>
							  <input name="txtUsuario" id="txtUsuario" aria-describedby=txtUsuario placeholder="Usuario" class="form-control"
									autocomplete="off" type="text" spellcheck="false" maxlength="100" pattern="[A-Za-zÁÉÍÓÚáéíóú ]{5,100}" title="Alfanumerico de mínimo 5 y máximo 100 caracteres." required
									value="<%= usuario %>" />
							</div>
						</div>
						<%
							out.print(linea);
						%>
						<div class="col col-sm-12 inpform">
							<label for="txtContra" class="form-label">Ingrese la Contraseña<br />(Esta debe ser de carácter temporal ya que al iniciar sesión el usuario, se le pedirá cambiarla)</label>
							<div class="input-group">
							  <span class="input-group-text" id="lblContra"><i class="bi bi-asterisk"></i></span>
							  <input name="txtContra" id="txtContra" aria-describedby=txtContra placeholder="Contraseña" class="form-control"
									autocomplete="off" type="password" spellcheck="false" maxlength="50" pattern="[A-Za-z0-9]{14,50}" title="Alfanumerico de mínimo 14 y máximo 50 caracteres." <% out.print(session.getAttribute("idtab") != null && !session.getAttribute("idtab").equals("") ? "" : "required"); %>
									/>
							  <div class="input-group-append">
                              	<button class="mostrarContra btn" type="button" onclick="showPassword()" title="Revelar/Ocultar contraseña.">
                              		<i class="bi bi-eye-slash icon1"></i>
                              	</button>
                              </div>
							</div>
						</div>
						<div class="col col-sm-12 inpform">
							<label for="txtContraRep" class="form-label">Repita la Contraseña</label>
							<div class="input-group">
							  <span class="input-group-text" id="lblContraRep"><i class="bi bi-asterisk"></i></span>
							   <input name="txtContraRep" id="txtContraRep" aria-describedby=txtContraRep placeholder="Contraseña" class="form-control"
									autocomplete="off" type="password" spellcheck="false" maxlength="50" pattern="[A-Za-z0-9]{14,50}" title="Alfanumerico de mínimo 14 y máximo 50 caracteres." <% out.print(session.getAttribute("idtab") != null && !session.getAttribute("idtab").equals("") ? "" : "required"); %>
									/>
							  <div class="input-group-append">
                              	<button class="mostrarContra btn" type="button" onclick="showPasswordRepeat()" title="Revelar/Ocultar contraseña.">
                              		<i class="bi bi-eye-slash icon2"></i>
                              	</button>
                              </div>
							</div>
						</div>
						<%
	                      	//Errores de busqueda
							if(session.getAttribute("menssaveuser") != null && !session.getAttribute("menssaveuser").equals("")) {
								out.print("<div class='margsup'></div>");
								out.print(val.mostrarAlert(0, session.getAttribute("menssaveuser") .toString()));
								session.setAttribute("menssaveuser", null);
							}
                        %>
						<div class="btn-group col col-sm-12">
							<button type="submit" class="btn btn-guardar" data-bs-toggle="tooltip" data-bs-placement="bottom" title="Finalizar y guardar usuario.">GUARDAR Y TERMINAR</button>
                            <% 
                            	if(session.getAttribute("idtab") == null || session.getAttribute("idtab").equals("")) {
                         	%>
                         	<button type="button" id="drp" class="btn btn-guardar dropdown-toggle dropdown-toggle-split" data-bs-toggle="dropdown" aria-expanded="false" title="Click para otra opción.">
                                <span class="visually-hidden">Toggle Dropdown</span>
                            </button>
                            <ul class="dropdown-menu otguardar">
                                <li>
                                    <button type="submit" class="dropdown-item" onclick="document.frmsave.action = 'guardar<%= form %>?otro=1'">GUARDAR Y AGREGAR OTRO</button>
                                </li>
                            </ul>
                          	<%
                            	}
                            %>
						</div>
			 		</form>
		 		</div>
		 	</div>
		 </div>
		<%
			//Fin del Contenido
			out.print(layout.footer(2));
			if(session.getAttribute("succSaveU") != null && !session.getAttribute("succSaveU").equals("")) {
				out.print(val.mostrarToastr("Éxito", "Proceso realizado correctamente."));
				session.setAttribute("succSaveU", null);
			}
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
			function showPasswordRepeat() {
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
	</body>
</html>