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
			query = "SELECT CreateUsuario, ReadUsuario, ReadProducto, ReadBitacora  FROM Accesos WHERE IdUsuario = '" + iduser + "'";
			ArrayList<String> accesos = val.getRow(query);
			if(accesos.size() == 0) {
				accesos.add(0, "0");
				accesos.add(1, "0");
				accesos.add(2, "0");
				accesos.add(3, "0");
			}
			if(accesos.get(1).equals("0")) {
				out.print("<script type=\"text/javascript\">");
				out.print("window.location.href = \"../\"");
				out.print("</script>");
			}
			if(accesos.get(0).equals("0") && accesos.get(1).equals("1")) {
				out.print("<script type=\"text/javascript\">");
				out.print("window.location.href = \"./vista\"");
				out.print("</script>");
			}
			//Fin
		%>
        <div id="panPrincipal" class="col col-md-12">
            <div class="row">
                <div class="col col-sm-12">
                   	<%
                		if(accesos.get(2).equals("1") || accesos.get(3).equals("1")) {
                			out.print("<a class='btn btn-regresar' href='../' role='button' data-bs-toggle='tooltip' data-bs-placement='left' title='Regresar a Dashboard.'>");
                			out.print("<i class='bi bi-chevron-double-left'></i>");
                			out.print("IR A DASHBOARD");
                			out.print("</a>");
                		}
                	%>
                    <p class="encabezadodash">MANEJO DE USUARIOS</p>
                    <div class="row contmenInv col-12 col-sm-10 col-lg-6">
                    	<div class="expli">
		                   	<p>
	                   			Sección del sistema orientada al control de los usuarios que tienen acceso a los procesos del sistema.
		                   	</p>
	                  	</div>
	                  	<%
	                  		if(!accesos.get(1).equals("0")) {
	                  	%>
	                        <div class="menuInv col col-12 col-sm-6">
	                            <a href="guardar" data-bs-toggle="tooltip" data-bs-placement="top" title="Para agregar nuevos usuarios.">
	                                <div class="iconmen">
	                                    <i class="bi bi-person-plus"></i>
	                                </div>
	                                <div class="pmen">
	                                    <p>INGRESAR NUEVO USUARIO</p>
	                                </div>
	                            </a>
	                        </div>
                        <%
	                  		}
                        %>
                        <div class="menuInv col col-12 col-sm-6">
                            <a href="vista" data-bs-toggle="tooltip" data-bs-placement="bottom" title="Para los usuarios almacenados en el sistema.">
                                <div class="iconmen">
                                    <i class="bi bi-person-lines-fill"></i>
                                </div>
                                <div class="pmen">
                                    <p>CONSULTAR USUARIOS</p>
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
	        if(session.getAttribute("succIndexU") != null && !session.getAttribute("succIndexU").equals("")) {
				out.print(val.mostrarToastr("Éxito", "Proceso realizado correctamente."));
				session.setAttribute("succIndexU", null);
			}
		%>
	</body>
</html>