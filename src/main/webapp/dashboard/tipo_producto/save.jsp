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
			String id = (String) session.getAttribute("idtab");
			query = "SELECT CreateTipoProducto, UpdateTipoProducto FROM Accesos WHERE IdUsuario = '" + iduser + "'";
			ArrayList<String> accesos = val.getRow(query);
			if(accesos.size() == 0) {
				accesos.add(0, "0");
				accesos.add(1, "0");
			}
			if(accesos.get(0).equals("0") && (id == null || id.trim().equals(""))) {
				out.print("<script type=\"text/javascript\">");
				out.print("window.location.href = \"../tipo_producto\"");
				out.print("</script>");
			}
			else if(accesos.get(1).equals("0") && id != null && !id.trim().equals("")) {
				out.print("<script type=\"text/javascript\">");
				out.print("window.location.href = \"../tipo_producto\"");
				out.print("</script>");
			}
			String tipo = "", simb = "", boton = "INGRESOS", form = "", link = "./";
			if (id != null && !id.trim().equals("")) {
				query = "SELECT Nombre, Simbolo FROM Tipo_Productos WHERE Id = '" + id + "' ORDER BY Id ASC LIMIT 1";
				ArrayList<String> datosobt = layout.getRow(query);
				if(datosobt.size() > 0)
				{
					tipo = datosobt.get(0);
					simb = datosobt.get(1);
					form = "?id=" + id;
					boton = "MODIFICACIONES";
					link = "./vista";
				}
				else {
					request.getSession().setAttribute("mensupdatipo", "El registro seleccionado no existe.");
					out.print("<script type=\"text/javascript\">");
					out.print("window.location.href = \"./vista\"");
					out.print("</script>");
				}
			}
			else {
				if (session.getAttribute("rettipnombre") != null && !session.getAttribute("rettipnombre").equals("")) {
					tipo = (String) session.getAttribute("rettipnombre");
				}
				if (session.getAttribute("rettipsimb") != null && !session.getAttribute("rettipsimb").equals("")) {
					simb = (String) session.getAttribute("rettipsimb");
				}
			}
			String retrn = (String) request.getSession().getAttribute("returntipo");
			if(retrn != null && retrn != "") {
				link = "../inventario/insertar";
			}
		%>
		<div id="panPrincipal" class="col col-md-12">
			<div class="row">
				<div class="col col-sm-12">
					<a class="btn btn-regresar" href="<%= link %>" role="button" data-bs-toggle="tooltip" data-bs-placement="left" title="Regresar al sitio anterior.">
						<i class="bi bi-chevron-double-left"></i> REGRESAR
					</a>
					<p class="encabezadodash">
						<a href="../">INVENTARIO DE ACCESORIOS/DISPOSITIVOS</a> <span class="subencab">
							/ <a href="./">CLASIFICACIÓN DE LOS ACCESORIOS/DISPOSITIVOS</a> / <%= boton %></span>
					</p>
					<form id="divInv" class="col col-sm-12 col-md-8 col-lg-6" onsubmit="confirmform(event, this, '¿Esta seguro de Guardar estos datos?')"
						action="guardar<%= form %>" method="POST" name="frmsave"
						id="frmsave">
						<div class="expli-just">
	                      	<p>
	                      		Ingrese los datos descritos a continuación para generar una nueva clasificación en el sistema. Cuando los datos estén revisados presionar "GUARDAR Y TERMINAR" o en su defecto la opción secundaria "GUARDAR Y AGREGAR OTRO".
	                      	</p>
                   		</div>
						<div class="col col-sm-12 inpform">
							<label for="txtTipo" class="form-label">
								Ingrese el nombre de la clasificación
							</label>
							<div class="input-group">
								<span class="input-group-text" id="txtTipo">
									<i class="bi bi-card-checklist"></i>
								</span>
								<input name="txtTipo" id="txtTipo" aria-describedby="txtTipo" placeholder="Clasificación" title="Alfanumérico de mínimo 2 y máximo 100 caracteres" class="form-control"
									autocomplete="off" type="text" spellcheck="false" maxlength="100" pattern="[A-Za-zA-Za-zÁÉÍÓÚáéíóú0-9 ]{2,100}" required
									value="<%= tipo %>" />
							</div>
						</div>
						<div class="col col-sm-12 inpform">
							<label for="txtSimbolo" class="form-label">
								Ingrese la simbología que utilizara la clasificación
								<i id="cursiva">(Ejem. AA ó AAA)</i>
							</label>
							<div class="input-group">
								<span class="input-group-text" id="txtSimbolo">
									<i class="bi bi-spellcheck"></i>
								</span>
								<input name="txtSimbolo" id="txtSimbolo" aria-describedby="txtSimbolo" placeholder="Clasificación" title="Mayúsculas de entre 2 y 3 caracteres." class="form-control"
									autocomplete="off" type="text" spellcheck="false" maxlength="3" pattern="[A-Z]{2,3}" required
									value="<%= simb %>" />
							</div>
						</div>
						<%
							if(session.getAttribute("menssavetipo") != null && !session.getAttribute("menssavetipo").equals("")) {
								out.print(val.mostrarAlert(0, session.getAttribute("menssavetipo") .toString()));
								session.setAttribute("menssavetipo", null);
							}
						%>
						<div class="btn-group col-12 col-sm-12">
							<button type="submit" class="btn btn-guardar" data-bs-toggle="tooltip" data-bs-placement="bottom" title="Finalizar y guardar clasificación.">
								GUARDAR Y TERMINAR
							</button>
							<% 
	                            if(session.getAttribute("idtab") == null || session.getAttribute("idtab").equals(""))
	                            {
                         	%>
									<button type="submit" id="drp"
										class="btn btn-guardar dropdown-toggle dropdown-toggle-split"
										data-bs-toggle="dropdown" aria-expanded="false"
										data-bs-toggle="tooltip" data-bs-placement="bottom" title="Click para otra opción.">
										<span class="visually-hidden">Toggle Dropdown</span>
									</button>
									<ul class="dropdown-menu otguardar">
										<li>
											<button type="submit" class="dropdown-item"
												onclick="document.frmsave.action = 'guardar<%= form %>?otro=1'">
												GUARDAR Y AGREGAR OTRO
											</button>
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
			//fin del contenido
			out.print(layout.footer(2));
			if(session.getAttribute("succSaveT") != null && !session.getAttribute("succSaveT").equals("")) {
				out.print(val.mostrarToastr("Éxito", "Proceso realizado correctamente."));
				session.setAttribute("succSaveT", null);
			}
		%>
	</body>
</html>