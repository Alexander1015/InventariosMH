<%@page import="java.util.ArrayList"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="lib.Validacion" %>
<%@page import="lib.Plantilla" %>
<%
	Plantilla layout = new Plantilla();
	Validacion val = new Validacion();
	String query = "SELECT COUNT(Id) AS Cantidad FROM Usuarios";
	ArrayList<String> cantidadUser = val.getRow(query);
	if(cantidadUser.size() == 0 || !cantidadUser.get(0).equals("0")) {
		out.print("<script type=\"text/javascript\">");
		out.print("window.location.href = \"../../../dashboard/desconectar\"");
		out.print("</script>");
	}
	else {
		/* Inicio verificar sesión */
		query = "SELECT Nombres, Apellidos FROM Usuarios WHERE Id = '" + session.getAttribute("id")
				+ "' ORDER BY Id DESC LIMIT 1";
		String usuario = "";
		boolean verificar = false;
		ArrayList<String> datosobt = val.getRow(query);
		if (datosobt.size() > 0) {
			usuario = datosobt.get(0) + " " + datosobt.get(1);
		}
		if (session.getAttribute("id") != null
				&& !session.getAttribute("id").equals("")) {
			if (!val.verificarsesion(session.getAttribute("id").toString(), usuario,
					session.getAttribute("token").toString())) {
				verificar = false;
			} else {
				verificar = true;
				out.print("<script type=\"text/javascript\">");
				out.print("window.location.href = \"../../../dashboard/desconectar\"");
				out.print("</script>");
			}
		} else {
			verificar = false;
		}
		if(!verificar) {
			session.setAttribute("id", null);
			session.setAttribute("succConect", null);
			session.setAttribute("tablatipo", null);
			session.setAttribute("stipo", null);
			session.setAttribute("existablatipo", null);
			session.setAttribute("returntipo", null);
			session.setAttribute("mensviewtipo", null);
			session.setAttribute("idtab", null);
			session.setAttribute("returntipo", null);
			session.setAttribute("otro", null);
			session.setAttribute("succViewT", null);
			session.setAttribute("succSaveT", null);
			session.setAttribute("succIndexT", null);
			session.setAttribute("succSaveP", null);
			session.setAttribute("mensviewinventario", null);
			session.setAttribute("existablaprod", null);
			session.setAttribute("tablaprod", null);
			session.setAttribute("sinvent", null);
			session.setAttribute("sinventantes", null);
			session.setAttribute("sinventdespues", null);
			session.setAttribute("succViewP", null);
			session.setAttribute("mensinsmodinventario", null);
			session.setAttribute("succInsertP", null);
			session.setAttribute("tabla_min", null);
			session.setAttribute("existablamin", null);
			session.setAttribute("succAddP", null);
			session.setAttribute("succRetP", null);
			session.setAttribute("mensretinventario", null);
			session.setAttribute("mensaddinventario", null);
			session.setAttribute("tabla_bit_min", null);
			session.setAttribute("existablabitmin", null);
			session.setAttribute("mensbitnventario", null);
			session.setAttribute("succDevP", null);
			session.setAttribute("tabla_min", null);
			session.setAttribute("existablamin", null);
			session.setAttribute("tabla_productos", null);
			session.setAttribute("id_inventario", null);
			session.setAttribute("mensdevinventario", null);
			session.setAttribute("sbita", null);
			session.setAttribute("sbitantes", null);
			session.setAttribute("sbitadespues", null);
			session.setAttribute("sbitaccion", null);
			session.setAttribute("existablabita", null);
			session.setAttribute("mensbitacora", null);
			session.setAttribute("succViewB", null);
			session.setAttribute("sbitacce", null);
			session.setAttribute("tablauser", null);
			session.setAttribute("existablauser", null);
			session.setAttribute("suser", null);
			session.setAttribute("returnuser", null);
			session.setAttribute("succViewU", null);
			session.setAttribute("mensviewuser", null);
			session.setAttribute("succSaveU", null);
			session.setAttribute("succIndexU", null);
			session.setAttribute("idInventariodev", null);
			session.setAttribute("mensperfil", null);
			session.setAttribute("idreport", null);
			session.setAttribute("tipo", null);
			session.setAttribute("shisto", null);
			session.setAttribute("shistocce", null);
			session.setAttribute("shistoccion", null);
			session.setAttribute("shistontes", null);
			session.setAttribute("shistodespues", null);
			session.setAttribute("tablahisto", null);
			session.setAttribute("existablahisto", null);
			session.setAttribute("mensacces", null);
			session.setAttribute("mensupdacces", null);
			session.setAttribute("rettipnombre", null);
			session.setAttribute("rettipsimb", null);
			session.setAttribute("retusrnombre", null);
			session.setAttribute("retusrapellido", null);
			session.setAttribute("retusrusuario", null);
			session.setAttribute("retmodcodigo", null);
			session.setAttribute("retmodnombre", null);
			session.setAttribute("retmodcantidad", null);
			session.setAttribute("retmodubicacion", null);
			session.setAttribute("retmodcoment", null);
			session.setAttribute("retmodmarca", null);
			session.setAttribute("retmodmodelo", null);
			session.setAttribute("retmodserie", null);
			session.setAttribute("retmodactivo", null);
			session.setAttribute("retinstipo", null);
			session.setAttribute("retinsautor", null);
			session.setAttribute("retinscomenting", null);
			session.setAttribute("retinsyo", null);
			session.setAttribute("retaddautor", null);
			session.setAttribute("retaddcomenting", null);
			session.setAttribute("retaddyo", null);
			session.setAttribute("retdevcomenting", null);
			session.setAttribute("retretpersonal", null);
			session.setAttribute("retretubicacion", null);
			session.setAttribute("retretautor", null);
			session.setAttribute("retretcomenting", null);
			session.setAttribute("retretautoyo", null);
			session.setAttribute("retretpersoyo", null);
			session.setAttribute("sinventcant", null);
			session.setAttribute("retaddsearch", null);
			session.setAttribute("retretsearch", null);
			session.setAttribute("retdevsearch", null);
			session.setAttribute("retdevsearchacc", null);
			session.setAttribute("retdevinicio", null);
			session.setAttribute("retdevfin", null);
			session.setAttribute("sinputtipo", null);
			session.setAttribute("sinputuser", null);
			session.setAttribute("sinputhisto", null);
			session.setAttribute("sinputhistocce", null);
			session.setAttribute("sinputhistontes", null);
			session.setAttribute("sinputhistodespues", null);
			session.setAttribute("sinputhistoccion", null);
			session.setAttribute("sinputbita", null);
			session.setAttribute("sinputbitacce", null);
			session.setAttribute("sinputbitantes", null);
			session.setAttribute("sinputbitadespues", null);
			session.setAttribute("sinputbitaccion", null);
			session.setAttribute("sinputinvent", null);
			session.setAttribute("sinputinventcant", null);
			session.setAttribute("sinputbitantes", null);
			session.setAttribute("sinputinventdespues", null);
			session.setAttribute("retretcaso", null);
			session.setAttribute("retretref1", null);
			session.setAttribute("retretref2", null);
			session.setAttribute("retretref3", null);
			session.setAttribute("retaddcaso", null);
			session.setAttribute("retaddref1", null);
			session.setAttribute("retaddref2", null);
			session.setAttribute("retaddref3", null);
			session.setAttribute("retinscaso", null);
			session.setAttribute("retinsref1", null);
			session.setAttribute("retinsref2", null);
			session.setAttribute("retinsref3", null);
		}
	}
%>
<!DOCTYPE html>
<html lang="es">
	<head>
		<% out.print(layout.header("Login", 3)); %>
	</head>
	<body>
		<%
			out.print(layout.navbar("Login", null, null, 3));
		%>
		<div id="panPrincipal" class="col col-md-12">
		 	<div class="row">
		 		<div class="col col-sm-12">
                   <p class="encabezadodash">
                        <a href="../usuario">PRIMER USUARIO</a>
                    </p>
                     <form id="divInv" class="col col-sm-12 col-md-8 col-lg-6" action="./mVtbFWCDKXNmMT2NQVnMSq7XxGYWE4" method="POST" name="frmsave" id="frmsave" onsubmit="confirmform(event, this, '¿Esta seguro de Guardar estos datos?')">
			 			<p class="explicacion">Ingrese el primer Usuario que utilizara el sistema de Inventarios, debe recordar que este comenzará con permisos Administrador además de que podrá editar ya sean sus permisos/accesos y la información de sesión.</p>
			 			<div class="col col-sm-12 inpform">
							<label for="txtNombre" class="form-label">Ingrese los Nombres</label>
							<div class="input-group">
							  <span class="input-group-text" id="txtNombre"><i class="bi bi-person-fill"></i></span>
							  <input name="txtNombre" id="txtNombre" aria-describedby="txtNombre" placeholder="Nombre" class="form-control"
									autocomplete="off" type="text" spellcheck="false" maxlength="250" pattern="[A-Za-zÁÉÍÓÚáéíóú ]{2,250}" title="Alfabético de mínimo 2 y máximo 250 caracteres." required
									/>
							</div>
						</div>
						<div class="col col-sm-12 inpform">
							<label for="txtApellido" class="form-label">Ingrese los Apellidos</label>
							<div class="input-group">
							  <span class="input-group-text" id="txtApellido"><i class="bi bi-person-fill"></i></span>
							 <input name="txtApellido" id="txtApellido" aria-describedby="txtApellido" placeholder="Apellido" class="form-control"
									autocomplete="off" type="text" spellcheck="false" maxlength="250" pattern="[A-Za-zÁÉÍÓÚáéíóú ]{2,250}" title="Alfabético de mínimo 2 y máximo 250 caracteres." required
									/>
							</div>
						</div>
						<div class="col col-sm-12 inpform">
							<label for="txtUsuario" class="form-label">Ingrese el Usuario</label>
							<div class="input-group">
							  <span class="input-group-text" id="txtUsuario"><i class="bi bi-person-fill"></i></span>
							  <input name="txtUsuario" id="txtUsuario" aria-describedby=txtUsuario placeholder="Usuario" class="form-control"
									autocomplete="off" type="text" spellcheck="false" maxlength="100" pattern="[A-Za-zÁÉÍÓÚáéíóú ]{5,100}" title="Alfanumerico de mínimo 5 y máximo 100 caracteres." required
									/>
							</div>
						</div>
						<div class="col col-sm-12 inpform">
							<label for="txtContra" class="form-label">Ingrese la Contraseña</label>
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
									autocomplete="off" type="password" spellcheck="false" maxlength="50" pattern="[A-Za-z0-9]{14,50}" title="Alfanumerico de mínimo 14 y máximo 50 caracteres." 
									required />
							  <div class="input-group-append">
                              	<button class="mostrarContra btn" type="button" onclick="showPasswordRepeat()" title="Revelar/Ocultar contraseña.">
                              		<i class="bi bi-eye-slash icon2"></i>
                              	</button>
                              </div>
							</div>
						</div>
						<%
	                      	//Errores de guardado
							if(session.getAttribute("menssaveuser") != null && !session.getAttribute("menssaveuser").equals("")) {
								out.print("<div class='margsup'></div>");
								out.print(val.mostrarAlert(0, session.getAttribute("menssaveuser") .toString()));
								session.setAttribute("menssaveuser", null);
							}
                        %>
						<div class="btn-group col col-sm-12">
							<button type="submit" class="btn btn-guardar" data-bs-toggle="tooltip" data-bs-placement="bottom" title="Finalizar y guardar usuario.">GUARDAR Y TERMINAR</button>
						</div>
			 		</form>
		 		</div>
		 	</div>
		 </div>
		<%
			//Fin del Contenido
			out.print(layout.footer(3));
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
			function actualizar() {
				location.reload(true);
			}
			setInterval("actualizar()", 3000000);
		</script>
	</body>
</html>