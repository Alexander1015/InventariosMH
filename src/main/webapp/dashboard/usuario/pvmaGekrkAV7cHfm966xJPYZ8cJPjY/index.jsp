<%@page import="java.util.ArrayList"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="lib.Validacion" %>
<%@page import="lib.Plantilla" %>
<%
	Plantilla layout = new Plantilla();
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
		<script type="text/javascript">
		window.location.href = \"../../../dashboard/desconectar\"
		</script>
		<%
			//Fin del Contenido
			out.print(layout.footer(3));
		%>
	</body>
</html>