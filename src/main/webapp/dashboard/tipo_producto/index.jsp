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
			query = "SELECT CreateTipoProducto, ReadTipoProducto FROM Accesos WHERE IdUsuario = '" + iduser + "'";
			ArrayList<String> accesos = val.getRow(query);
			if(accesos.size() == 0) {
				accesos.add(0, "0");
				accesos.add(1, "0");
			}
			if(accesos.get(1).equals("0")) {
				out.print("<script type=\"text/javascript\">");
				out.print("window.location.href = \"../inventario\"");
				out.print("</script>");
			}
			if(accesos.get(0).equals("0") && accesos.get(1).equals("1")) {
				out.print("<script type=\"text/javascript\">");
				out.print("window.location.href = \"./vista\"");
				out.print("</script>");
			}
		%>
		<div id="panPrincipal" class="col col-md-12">
			<div class="row">
				<div class="col col-sm-12">
					<a class="btn btn-regresar" href="../inventario" role="button" data-bs-toggle="tooltip" data-bs-placement="left" title="Regresar al sitio anterior.">
						<i class="bi bi-chevron-double-left"></i> REGRESAR
					</a>
					<p class="encabezadodash">
						<a href="../inventario">INVENTARIO DE ACCESORIOS/DISPOSITIVOS</a>
						<span class="subencab"> / CLASIFICACIÓN DE LOS ACCESORIOS/DISPOSITIVOS</span>
					</p>
					<div class="row contmenInv col-12 col-sm-10 col-lg-6">
						<div class="expli-just">
		                   	<p>
	                   			Sección del sistema orientada al almacenamiento de las posibles clasificaciones que el sistema posee, con el objetivo tener un control ordenado por tipos en los accesorios/dispositivos. Las opciones siguientes pueden variar según los permisos/accesos aplicados.
		                   	</p>
	                  	</div>
						<%
							if(accesos.get(0).equals("1")) {
						%>
							<div class="menuInv col col-12 col-sm-6">
								<a href="guardar"  data-bs-toggle="tooltip" data-bs-placement="top" title="Para agregar nuevas clasificaciones.">
									<div class="iconmen">
										<i class="bi bi-award"></i>
									</div>
									<div class="pmen">
										<p>INGRESAR NUEVA CLASIFICACIÓN</p>
									</div>
								</a>
							</div>
						<%
							}
						%>
						<div class="menuInv col col-12 col-sm-6">
							<a href="vista" data-bs-toggle="tooltip" data-bs-placement="bottom" title="Para las clasificaciones almacenadas en el sistema.">
								<div class="iconmen">
									<i class="bi bi-journal-bookmark-fill"></i>
								</div>
								<div class="pmen">
									<p>CONSULTAR CLASIFICACIONES</p>
								</div>
							</a>
						</div>
					</div>
				</div>
			</div>
		</div>
		<% 
			//fin del contenido 
			out.print(layout.footer(2)); 
			if(session.getAttribute("succIndexT") != null && !session.getAttribute("succIndexT").equals("")) {
				out.print(val.mostrarToastr("Éxito", "Proceso realizado correctamente."));
				session.setAttribute("succIndexT", null);
			}
		%>
	</body>
</html>