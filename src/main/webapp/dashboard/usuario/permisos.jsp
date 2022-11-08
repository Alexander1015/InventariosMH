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
			query = "SELECT UpdateAcceso FROM Accesos WHERE IdUsuario = '" + iduser + "'";
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
			String id = (String) session.getAttribute("idtab");
			String nombre = "", apellido = "", tipo = "";
			String RP = "", AP = "", DP = "", CP = "", UP = "", RetP = "", RT = "", CT = "", DT = "", UT = "", RB = "", CR = "", RU = "", CU = "", DU = "", UU = "", UA = "";
			String title = "INGRESOS", form = "", linea = "";
			int tot = 0;
			Boolean verif = false;
			if (id != null && id != "") {
				//			0				1
				query = "SELECT U.Nombres, U.Apellidos, "
					//	2				3					4					5					6				7
					+ "A.ReadProducto, A.AgregarProducto, A.DevolverProducto, A.CreateProducto, A.UpdateProducto, A.RetirarProducto, "
					//	8					9						10					11
					+ "A.ReadTipoProducto, A.CreateTipoProducto, A.DeleteTipoProducto, A.UpdateTipoProducto, "
					//	12				13
					+ "A.ReadBitacora, A.CreateReporte, "
					//	14				15				16					17				18
					+ "A.ReadUsuario, A.CreateUsuario, A.DeleteUsuario, A.UpdateUsuario, A.UpdateAcceso "
					+ "FROM Usuarios AS U INNER JOIN Accesos AS A ON U.Id = A.IdUsuario "
					+ "WHERE U.Id = '" + id + "'";
				ArrayList<String> datosobt = layout.getRow(query);
				if(datosobt.size() > 0)
				{
					nombre = datosobt.get(0);
					apellido = datosobt.get(1);
					RP = datosobt.get(2);
					AP = datosobt.get(3);
					DP = datosobt.get(4);
					CP = datosobt.get(5);
					UP = datosobt.get(6);
					RetP = datosobt.get(7);
					RT = datosobt.get(8);
					CT = datosobt.get(9);
					DT = datosobt.get(10);
					UT = datosobt.get(11);
					RB = datosobt.get(12);
					CR = datosobt.get(13);
					RU = datosobt.get(14);
					CU = datosobt.get(15);
					DU = datosobt.get(16);
					UU = datosobt.get(17);
					UA = datosobt.get(18);
					for(int i = 2; i <= 18; i++) {
						if(datosobt.get(i).equals("1") && i == 2) {
							verif = true;
							tot++;
						}
						else if(datosobt.get(i).equals("1")) {
							verif = false;
							tot++;
						}
					}
				}
			}
		%>
        <div id="panPrincipal" class="col col-md-12">
            <div class="row">
                <div class="col col-sm-12">
                	<a class="btn btn-regresar" href="./vista" role="button" data-bs-toggle="tooltip" data-bs-placement="left" title="Regresar al sitio anterior.">
	                	<i class="bi bi-chevron-double-left"></i>
						REGRESAR
	                </a>
                    <p class="encabezadodash">
						<a href="../usuario">MANEJO DE USUARIOS</a>
	                    <a href="./view.jsp" class="subencab"> / CONSULTAS</a>
	                    <span class="subencab"> / Accesos</span>
					</p>
					<div id="divInv" class="col col-sm-12 col-md-8 col-lg-6">
						<p class='titproduct center'>Accesos de: <%= nombre + " " + apellido %></p>
						<hr>
						<div class="expli">
	                      	<p>
	                      		Seleccione los accesos que tendrá el usuario seleccionado dándole al check en el cuadro izquierdo (Si deshabilita todos los accesos, el usuario tendrá acceso denegado al sistema y no podrá iniciar sesión); al revisar lo seleccionado presionar "GUARDAR Y TERMINAR".
	                      	</p>
                   		</div>
						<% //Combobox %>
						<div class="row">
                             <label for="cmbAccesos" class="form-label">Seleccione el tipo de acceso</label>
                             <div class="col-12 col-sm-12">
                                 <select class="form-select" aria-label="Clasificación de Accesos" required="required" id="cmbAccesos" name="cmbAccesos">
                                     <option disabled="disabled">Accesos</option>
                                     <option <%= tot == 17 ? "selected" : "" %> value="0">Usuario Administrador</option>
                                     <option <%= verif ? "selected" : "" %> value="1">Usuario Básico</option>
                                     <option <%= (tot < 17 && !verif) ? "selected" : "" %> value="2">Usuario Personalizado</option>
                               	</select>
                           	</div>
                       	</div>
                       	<%
	                      //Errores de accesos/permisos
							if(session.getAttribute("mensupdacces") != null && !session.getAttribute("mensupdacces").equals("")) {
								out.print("<div class='margsup'></div>");
								out.print(val.mostrarAlert(0, session.getAttribute("mensupdacces") .toString()));
								session.setAttribute("mensupdacces", null);
							}
                       		//Sector de Accesorios/Dispositivos
                       	%>
                       	<form name="frmaccesos" id="frmaccesos" class="col col-sm-12" action="access?id=<%= id %>" method="POST" onsubmit="confirmform(event, this, '¿Esta seguro de Guardar estos datos?')">
                       		<div class="btn-group col col-sm-12">
								<button type="submit" class="btn btn-guardar" data-bs-toggle="tooltip" data-bs-placement="bottom" title="Finalizar y guardar acceso/permiso">GUARDAR Y TERMINAR</button>
							</div>
	                     	<ul>
		                      	<li class="margsup">Manejo de Accesorios/Dispositivos al:</li>
		                      	<li class="quitarlista">
			                      	<ul>
			                      		<li>
				                      		<input class='checkinv form-check-input puntero' type='checkbox' name='chkConsAcc' id="chkConsAcc" value="true" <%= (RP.equals("1") ? "checked" : "") %>>
											<label for="chkConsAcc" class="form-label puntero">Consultar los datos almacenados de los Accesorios/Dispositivos</label>
										</li>
			                      		<li>
			                      			<input class='checkinv form-check-input puntero' type='checkbox' name='chkAddAcc' id="chkAddAcc" value="true" <%= (AP.equals("1") ? "checked" : "") %>>
											<label for="chkAddAcc" class="form-label puntero">Agregar más existencias de Accesorios/Dispositivos por Usuarios</label>
										</li>
										<li>
											<input class='checkinv form-check-input puntero' type='checkbox' name='chkDevAcc' id="chkDevAcc" value="true" <%= (DP.equals("1") ? "checked" : "") %>>
											<label for="chkDevAcc" class="form-label puntero">Devolver Accesorios/Dispositivos retirados por Usuarios</label>
										</li>
										<li>
											<input class='checkinv form-check-input puntero' type='checkbox' name='chkIngAcc' id="chkIngAcc" value="true" <%= (CP.equals("1") ? "checked" : "") %>>
											<label for="chkIngAcc" class="form-label puntero">Ingresar nuevos Accesorios/Dispositivos a la Base de Datos</label>
										</li>
										<li>
											<input class='checkinv form-check-input puntero' type='checkbox' name='chkModAcc' id="chkModAcc" value="true" <%= (UP.equals("1") ? "checked" : "") %>>
											<label for="chkModAcc" class="form-label puntero">Modificar la información de los Accesorios/Dispositivos</label>
										</li>
										
										<li>
											<input class='checkinv form-check-input puntero' type='checkbox' name='chkRetAcc' id="chkRetAcc" value="true" <%= (RetP.equals("1") ? "checked" : "") %>>
											<label for="chkRetAcc" class="form-label puntero">Retirar existencias de Accesorios/Dispositivos para Usuarios</label>
										</li>
									</ul>
								</li>
								<li class="margsup">Manejo de las Clasificaciones de los Accesorios/Dispositivos al:</li>
								<li class="quitarlista">
									<ul>
										<li>
											<input class='checkinv form-check-input puntero' type='checkbox' name='chkConsCla' id="chkConsCla" value="true" <%= (RT.equals("1") ? "checked" : "") %>>
											<label for="chkConsCla" class="form-label puntero">Consultar los datos almacenados de las Clasificaciones</label>
										</li>
										<li>
											<input class='checkinv form-check-input puntero' type='checkbox' name='chkInsCla' id="chkInsCla" value="true" <%= (CT.equals("1") ? "checked" : "") %>>
											<label for="chkInsCla" class="form-label puntero">Agregar nuevas Clasificaciones a la Base de Datos</label>
										</li>
										<li>
											<input class='checkinv form-check-input puntero' type='checkbox' name='chkDelCla' id="chkDelCla" value="true" <%= (DT.equals("1") ? "checked" : "") %>>
											<label for="chkDelCla" class="form-label puntero">Eliminar Clasificaciones que no tengan datos adjuntos</label>
										</li>
										<li>
											<input class='checkinv form-check-input puntero' type='checkbox' name='chModCla' id="chModCla" value="true" <%= (UT.equals("1") ? "checked" : "") %>>
											<label for="chModCla" class="form-label puntero">Modificar la información de las Clasificaciones</label>
										</li>
									</ul>
								</li>
								<li class="margsup">Manejo de la Bitácora al:</li>
								<li class="quitarlista">
									<ul>
										<li>
											<input class='checkinv form-check-input puntero' type='checkbox' name='chkConBit' id="chkConBit" value="true" <%= (RB.equals("1") ? "checked" : "") %>>
											<label for="chkConBit" class="form-label puntero">Consultar los movimientos generados al momento de manipular los Accesorios</label>
										</li>
										<li>
											<input class='checkinv form-check-input puntero' type='checkbox' name='chkRepBit' id="chkRepBit" value="true" <%= (CR.equals("1") ? "checked" : "") %>>
											<label for="chkRepBit" class="form-label puntero">Generar los reportes de los procesos realizados por Usuarios</label>
										</li>
									</ul>
								</li>
								<li class="margsup">Manejo de los Usuarios al:</li>
								<li class="quitarlista">
									<ul>
										<li>
											<input class='checkinv form-check-input puntero' type='checkbox' name='chkConsUse' id="chkConsUse" value="true" <%= (RU.equals("1") ? "checked" : "") %>>
											<label for="chkConsUse" class="form-label puntero">Consultar los datos almacenados de los Usuarios</label>
										</li>
										<li>
											<input class='checkinv form-check-input puntero' type='checkbox' name='chkInsUse' id="chkInsUse" value="true" <%= (CU.equals("1") ? "checked" : "") %>>
											<label for="chkInsUse" class="form-label puntero">Agregar nuevos Usuarios a la Base de Datos</label>
										</li>
										<li>
											<input class='checkinv form-check-input puntero' type='checkbox' name='chkDelUse' id="chkDelUse" value="true" <%= (DU.equals("1") ? "checked" : "") %>>
											<label for="chkDelUse" class="form-label puntero">Eliminar Usuarios que no tengan datos adjuntos</label>
										</li>
										<li>
											<input class='checkinv form-check-input puntero' type='checkbox' name='chModUse' id="chModUse" value="true" <%= (UU.equals("1") ? "checked" : "") %>>
											<label for="chModUse" class="form-label puntero">Modificar la información de los Usuarios</label>
										</li>
									</ul>
								</li>
								<li class="margsup">Manejo de los Accesos/Permisos al (Necesita acceso para manejo de Usuarios):</li>
								<li class="quitarlista">
									<ul>
										<li>
											<input class='checkinv form-check-input puntero' type='checkbox' name='chAccUse' id="chAccUse" value="true" <%= (UA.equals("1") ? "checked" : "") %>>
											<label for="chAccUse" class="form-label puntero">Modificar las áreas de acceso en la aplicacion según cada Usuario</label>
										</li>
									</ul>
								</li>
							</ul>
						</form>
					</div>
                </div>
            </div>
        </div>
		<%
			//fin del contenido 
			out.print(layout.footer(2)); 
		%>
		<script type="text/javascript" src="../../js/access.js"></script>
		<script type="text/javascript">
			$("#chkConsAcc").change(deshabilitar_accesorios);
			$("#chkConsCla").change(deshabilitar_clasificaciones);
			$("#chkConBit").change(deshabilitar_bitacoras);
			$("#chkConsUse").change(deshabilitar_usuarios);
			$("#cmbAccesos").change(cambio);
		</script>
	</body>
</html>