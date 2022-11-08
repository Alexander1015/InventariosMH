//Para la tabla con paginación
$(document).ready(function() {
	//Para el buscador de datalist
	$('.flexdatalist').flexdatalist({
	     minLength: 1,
	});
	$("input:text:visible:first").focus();
	//Para Checkbox
	$("#chckall").on("click", function() {  
		$(".checks").prop("checked", this.checked);  
	});  
	 
	$(".checks").on("click", function() {  
		if ($(".checks").length == $(".checks:checked").length) {  
	    	$("#chckall").prop("checked", true);  
	  	} else {  
	   		$("#chckall").prop("checked", false);  
	  	}  
	});
	//Los Tooltips
	var tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'))
	tooltipTriggerList.map(function (tooltipTriggerEl) {
	  return new bootstrap.Tooltip(tooltipTriggerEl)
	});
	//Los combobox buscadores
	$('.clasificacion').select2();
	//Para las tablas
	$('#registros').DataTable({
		"order": [[0, "asc"]],
		"columnDefs": [{
			"targets": 0
		}],
		language: {
			"sProcessing": "Procesando...",
			"sLengthMenu": "Mostrar _MENU_ resultados",
			"sZeroRecords": "No se encontraron resultados",
			"sEmptyTable": "Ningun dato disponible en esta tabla",
			"sInfo": "Mostrando resultados _START_ - _END_ de _TOTAL_",
			"sInfoEmpty": "Mostrando resultados del 0 al 0 de un total de 0 registros",
			"sInfoFiltered": "(filtrado de un total de _MAX_ registros)",
			"sSearch": "Buscar:",
			"sLoadingRecords": "Cargando...",
			"oPaginate": {
				"sFirst": "Primero",
				"sLast": "Último",
				"sNext": "<i class='bi bi-caret-right-fill itabla'></i>",
				"sPrevious": "<i class='bi bi-caret-left-fill itabla'></i>"
			},
		}
	});
	
	$('#registrosbit').DataTable({
		"order": [[0, "desc"]],
		"columnDefs": [{
			"targets": 0
		}],
		language: {
			"sProcessing": "Procesando...",
			"sLengthMenu": "Mostrar _MENU_ resultados",
			"sZeroRecords": "No se encontraron resultados",
			"sEmptyTable": "Ningun dato disponible en esta tabla",
			"sInfo": "Mostrando resultados _START_ - _END_ de _TOTAL_",
			"sInfoEmpty": "Mostrando resultados del 0 al 0 de un total de 0 registros",
			"sInfoFiltered": "(filtrado de un total de _MAX_ registros)",
			"sSearch": "Buscar:",
			"sLoadingRecords": "Cargando...",
			"oPaginate": {
				"sFirst": "Primero",
				"sLast": "Último",
				"sNext": "<i class='bi bi-caret-right-fill itabla'></i>",
				"sPrevious": "<i class='bi bi-caret-left-fill itabla'></i>"
			},
		}
	});

	$('#registrosmin').DataTable({
		"order": [[0, "asc"]],
		"columnDefs": [{
			"targets": 0
		}],
		language: {
			"sProcessing": "Procesando...",
			"sLengthMenu": "Mostrar _MENU_ resultados",
			"sZeroRecords": "No se encontraron resultados",
			"sEmptyTable": "Ningún dato disponible en esta tabla",
			"sInfo": "Mostrando resultados _START_ - _END_ de _TOTAL_",
			"sInfoEmpty": "Mostrando resultados del 0 al 0 de un total de 0 registros",
			"sInfoFiltered": "(filtrado de un total de _MAX_ registros)",
			"sSearch": "Buscar:",
			"sLoadingRecords": "Cargando...",
				"oPaginate": {
				"sFirst": "Primero",
				"sLast": "Último",
				"sNext": "<i class='bi bi-caret-right-fill itabla'></i>",
				"sPrevious": "<i class='bi bi-caret-left-fill itabla'></i>"
			},
		}
	});
	
	$('#registrosdash').DataTable({
		"order": [[0, "asc"]],
		"columnDefs": [{
			"targets": 0
		}],
		language: {
			"sProcessing": "Procesando...",
			"sLengthMenu": "Mostrar _MENU_ resultados",
			"sZeroRecords": "No se encontraron resultados",
			"sEmptyTable": "Ningún dato disponible en esta tabla",
			"sInfo": "Mostrando resultados _START_ - _END_ de _TOTAL_",
			"sInfoEmpty": "Mostrando resultados del 0 al 0 de un total de 0 registros",
			"sInfoFiltered": "(filtrado de un total de _MAX_ registros)",
			"sSearch": "Buscar:",
			"sLoadingRecords": "Cargando...",
				"oPaginate": {
				"sFirst": "Primero",
				"sLast": "Último",
				"sNext": "<i class='bi bi-caret-right-fill itabla'></i>",
				"sPrevious": "<i class='bi bi-caret-left-fill itabla'></i>"
			},
		}
	});	
});

//Para mostrar los Toastr
toastr.options = {
	"closeButton": true,
	"debug": false,
	"newestOnTop": false,
	"progressBar": true,
	"positionClass": "toast-bottom-right",
	"preventDuplicates": true,
	"onclick": null,
	"showDuration": "300",
	"hideDuration": "1000",
	"timeOut": "3000",
	"extendedTimeOut": "1000",
	"showEasing": "swing",
	"hideEasing": "linear",
	"showMethod": "fadeIn",
	"hideMethod": "fadeOut"
}