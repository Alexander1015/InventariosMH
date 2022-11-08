<%@page import="java.util.Collections"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Random" %>
<%@page import="lib.Validacion"%>
<%@page import="lib.Plantilla"%>
<%
	Plantilla layout = new Plantilla();
	Validacion val = new Validacion();
%>
<!DOCTYPE html>
<html lang="es">
	<head>
		<% out.print(layout.header("Dashboard", 1)); %>
	</head>
	<body>
		<% 
			String iduser = (String) session.getAttribute("id");
			out.print(layout.navbar("Dashboard", iduser, (String) session.getAttribute("token"), 1)); 
			//Inicio del Contenido 
			String query = "SELECT Defecto FROM Usuarios WHERE Id = '" + iduser + "' ORDER BY Id DESC LIMIT 1";
			ArrayList<String> usuariobd = layout.getRow(query);
			if(usuariobd.size() > 0 && usuariobd.get(0).equals("0")) {
				out.print("<script type=\"text/javascript\">");
				out.print("window.location.href = \"./perfil\"");
				out.print("</script>");
			}
			query = "SELECT ReadProducto, ReadBitacora, ReadUsuario FROM Accesos WHERE IdUsuario = '" + iduser + "'";
			ArrayList<String> accesos = val.getRow(query);
			if(accesos.size() == 0) {
				accesos.add(0, "0");
				accesos.add(1, "0");
				accesos.add(2, "0");
			}
			if(accesos.get(0).equals("0") && accesos.get(1).equals("0")) {
				out.print("<script type=\"text/javascript\">");
				out.print("window.location.href = \"./perfil\"");
				out.print("</script>");
			}
			//Para el grafico
			query = "SELECT T.Nombre, "
				+ "(SELECT SUM(P.Cantidad) FROM Productos AS P WHERE P.Tipo = T.Id ORDER BY P.Tipo) AS SUMA "
				+ "FROM Tipo_Productos AS T ORDER BY T.Id, SUMA ASC";
			ArrayList<ArrayList<String>> prodvsclas = new ArrayList<ArrayList<String>>();
			ArrayList<Integer> listnum = new ArrayList<Integer>();
			String valores = "", cantidades = "", colores = "";
			int r = 37, g = 43, b = 54;
			int x = 20, y = 50, z = 80;
			if(accesos.get(0).equals("1")) {
				prodvsclas = layout.getRows(query);
				for(int i = 0; i < prodvsclas.size(); i++) {
					if (prodvsclas.get(i).get(0) != null && !prodvsclas.get(i).get(0).equals("") && prodvsclas.get(i).get(1) != null && !prodvsclas.get(i).get(1).equals("")) {
						listnum.add(Integer.parseInt(prodvsclas.get(i).get(1).trim()));
					}
				}
				Collections.sort(listnum, Collections.reverseOrder());
				ArrayList<String> listval = new ArrayList<String>();
				if(listnum.size() > 0) {
					for(int i = 0; i < listnum.size(); i++) {
						for(int j = 0; j < prodvsclas.size(); j++) {
							if(prodvsclas.get(j).get(1) != null && !prodvsclas.get(j).get(1).equals("")){
								//En ambas lineas se verifica que no este vacio
								if (prodvsclas.get(j).get(1).trim().equals("" + listnum.get(i))) {
									//Verificamos que no existe el valor revisado
									if(listval.indexOf(prodvsclas.get(j).get(0)) == -1) {
										listval.add(prodvsclas.get(j).get(0));
									}
								}
							}
						}
						if (i < 16) {
							cantidades += "" + listnum.get(i) + ", ";
							valores += "'" + listval.get(i) + "',";
							colores += "'rgba(" + r + ", " + g + ", " + b + ", 1)',";
							r = (r + x) > 255 ? r - 255 + x : r + x;
							g = (g + y) > 255 ? g - 255 + y : g + y;
							b = (b + z) > 255 ? b - 255 + z : b + z;
						}
					}
				}
			}
			//Para la bitacora		
		%>
		<div id="panPrincipal" class="col col-md-12">
			<div class="row">
				<div class="col col-sm-12">
					<p class="encabezadodash">DASHBOARD</p>
					<div class="row">
						<div  class="col col-12 col-sm-12 col-md-12 col-lg-6">
						  	<%
								if(accesos.get(0).equals("1") || accesos.get(1).equals("1") || accesos.get(2).equals("1")) {
							%>
								<div class="btngroupdash row col-12">
									<div class="expli-just">
					                   	<p>
					                   		Menú inicial que permite redirigir a todas las secciones del sistema, dependiendo de los accesos que el usuario actual posea (para una navegación más completa revise el menú que se encuentra en la parte superior derecha).
					                   	</p>
				                  	</div>
									<%
			                        	if(accesos.get(0).equals("1")) {
			                        %>
										<div class="menuInv col col-12">
				                            <a href="./inventario" data-bs-toggle="tooltip" data-bs-placement="right" title="Inventario de Accesorios/Dispositivos.">
				                                <div class="iconmen">
				                                    <i class="bi bi-archive"></i>
				                                </div>
				                                <div class="pmen">
				                                    <p>
				                                        <i class="bi bi-archive"></i>
				                                        INVENTARIO DE ACCESORIOS/DISPOSITIVOS
				                                    </p>
				                                </div>
				                            </a>
				                        </div>
			                        <%
			                        	}
										if(accesos.get(1).equals("1")) {
			                        %>
				                        <div class="menuInv col col-12">
				                            <a href="./bitacora" data-bs-toggle="tooltip" data-bs-placement="right" title="Bitacora General de Procesos.">
				                                <div class="iconmen">
				                                    <i class="bi bi-file-earmark-bar-graph"></i>
				                                </div>
				                                <div class="pmen">
				                                    <p>
				                                        <i class="bi bi-file-earmark-bar-graph"></i>
				                                        BITACORA GENERAL DE PROCESOS
				                                    </p>
				                                </div>
				                            </a>
				                        </div>
				                    <%
										}
										if(accesos.get(2).equals("1")) {
				                    %>
				                        <div class="menuInv col col-12">
				                            <a href="./usuario" data-bs-toggle="tooltip" data-bs-placement="right" title="Manejo de Usuarios.">
				                                <div class="iconmen">
				                                    <i class="bi bi-people"></i>
				                                </div>
				                                <div class="pmen">
				                                    <p>
				                                        <i class="bi bi-people"></i>
				                                        MANEJO DE USUARIOS
				                                    </p>
				                                </div>
				                            </a>
				                        </div>
				                     <%
										}
				                     %>
								</div>
						  	<%
								}
						  	%>
						  </div>
						<%
						  	//Gráfico 
						  	if(accesos.get(0).equals("1") && prodvsclas.size() > 0) {
				  	 	%>
							<div id="divInv" class="col col-12 col-sm-12 col-md-5 col-lg-5 divcentrado containergraf">
							  	<p class="titledash">Accesorios/Dispositivos en Inventario según sus clasificaciones (máx. 15 clasificaciones) <a href="./inventario/vista" class="infvis"><i class="bi bi-chevron-double-left"></i>Para mayor información visite el Inventario de Accesorios/Dispositivos<i class="bi bi-chevron-double-right"></i></a></p>
							  	<div class="col col-12 col-sm-12 col-md-11">
							  		<canvas id="grfAccesorios"></canvas>
						  		</div>
							  </div>
							  <% //Tabla del gráfico %>
							  <div class="containTable_Min col col-12 col-sm-12 col-md-11 col-lg-10 containergraftable">
							  	<p class="titledash">Accesorios en Inventario según sus clasificaciones <a href="./inventario/vista" class="infvis"><i class="bi bi-chevron-double-left"></i>Para mayor información visite el Inventario de Accesorios/Dispositivos<i class="bi bi-chevron-double-right"></i></a></p>
								<table id="registrosdash" data-page-length="10" class="table striped table-hover">
									<thead>
										<tr>
											<th scope="col">TIPO DE ACCESORIO/DISPOSITIVO</th>
											<th scope="col">CANTIDAD</th>
										</tr>
									</thead>
									<tbody>
										<%
											try {
												int elementos = prodvsclas.size(); 
												int i = 0;
												for (i = 0; i < elementos; i++) { 
													if (prodvsclas.get(i).get(1) != null && !prodvsclas.get(i).get(1).trim().equals("")) {
														out.print("<tr>");
														out.print("<td>" + prodvsclas.get(i).get(0) + "</td>");
														out.print("<td>" + prodvsclas.get(i).get(1) + "</td>");
														out.print("</tr>");
													}
												}
												if(i == 0) {
													out.print("<tr>");
													out.print("<td colspan=\"2\">No se encontraron elementos para mostrar.</td>");
													out.print("</tr>");
												}
											}
											catch (Exception e) {
												out.print("<tr>");
												out.print("<td colspan=\"2\">No se encontraron elementos para mostrar.</td>");
												out.print("</tr>");
											}
										%>
									</tbody>
								</table>
							</div>
						  <% 
						  	}
						  %>
					</div>
				</div>
			</div>
		</div>
		<% 
			//Fin del Contenido 
			out.print(layout.footer(1)); 
			if(session.getAttribute("succConect") != null && !session.getAttribute("succConect").equals("")) {
				out.print(val.mostrarToastr("Bienvenido", "Inicio de sesión realizado correctamente."));
				session.setAttribute("succConect", null);
			}
			if(session.getAttribute("succPerfil") != null && !session.getAttribute("succPerfil").equals("")) {
				out.print(val.mostrarToastr("Éxito", "Proceso realizado correctamente."));
				session.setAttribute("succPerfil", null);
			}
		%>
		<script type="text/javascript">			
			var ctx = document.getElementById('grfAccesorios').getContext('2d');
			var myChart = new Chart(ctx, {
			    type: 'doughnut',
			    data: {
			        labels: [<%= valores %>],
			        datasets: [{
			            label: '# de Accesorios/Dispositivos',
			            data: [<%= cantidades %>],
			            backgroundColor: [<%= colores %>],
			            borderColor: [<%= colores %>]
			        }]
			    },
			    options: {
			    	indexAxis: 'y',
			    	scale: {
			    		display: false,
			    	},
			        /*scales: {
			            xAxes: {
			            	ticks: {
			            		beginAtZero: true
			            	},
			            	position: 'left'
			            }
			        },*/
			        plugins: {
			    		legend: {
			    			position: 'left',
				    		labels: {
				    			color: 'rgb(37, 43, 54)',
				    			font: {
				    				size: '10rem'
				    			}
				    		}
			    		}
			        }
			    }
			});
		</script>
		<script type="text/javascript">
			function actualizar() {
				location.reload(true);
			}
			setInterval("actualizar()", 60000);
		</script>
	</body>
</html>