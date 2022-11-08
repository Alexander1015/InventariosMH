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
         <% out.print(layout.header("Bitacora", 2)); %>
	</head>
	<body>
		<%
			String iduser = (String)session.getAttribute("id");
			out.print(layout.navbar("Bitacora", iduser, (String)session.getAttribute("token"), 2));
			//Inicio del Contenido
			String query = "SELECT Defecto FROM Usuarios WHERE Id = '" + iduser + "' ORDER BY Id DESC LIMIT 1";
			ArrayList<String> usuariobd = layout.getRow(query);
			if(usuariobd.size() > 0 && usuariobd.get(0).equals("0")) {
				out.print("<script type=\"text/javascript\">");
				out.print("window.location.href = \"../perfil\"");
				out.print("</script>");
			}
			//Accesos
			query = "SELECT ReadBitacora FROM Accesos WHERE IdUsuario = '" + iduser + "'";
			ArrayList<String> accesos = val.getRow(query);
			if(accesos.size() == 0) {
				accesos.add(0, "0");
			}
			if(accesos.get(0).equals("0")) {
				out.print("<script type=\"text/javascript\">");
				out.print("window.location.href = \"../\"");
				out.print("</script>");
			}
			//Fin
		%>
		<script type="text/javascript">
			window.location.href = "./vista";
		</script>
		<%
			out.print(layout.footer(2));
		%>
	</body>
</html>
