$(document).ready(function() {
	$("#chkConsAcc").on("click", function() { $("#cmbAccesos").val("2"); });
	$("#chkConsCla").on("click", function() { $("#cmbAccesos").val("2"); });
	$("#chkConBit").on("click", function() { $("#cmbAccesos").val("2"); });
	$("#chkConsUse").on("click", function() { $("#cmbAccesos").val("2"); });
	$("#chkAddAcc").on("click", function() { $("#cmbAccesos").val("2"); });
	$("#chkDevAcc").on("click", function() { $("#cmbAccesos").val("2"); });
	$("#chkIngAcc").on("click", function() { $("#cmbAccesos").val("2"); });
	$("#chkModAcc").on("click", function() { $("#cmbAccesos").val("2"); });
	$("#chkRetAcc").on("click", function() { $("#cmbAccesos").val("2"); });
	$("#chkInsCla").on("click", function() { $("#cmbAccesos").val("2"); });
	$("#chkDelCla").on("click", function() { $("#cmbAccesos").val("2"); });
	$("#chModCla").on("click", function() { $("#cmbAccesos").val("2"); });
	$("#chkRepBit").on("click", function() { $("#cmbAccesos").val("2"); });
	$("#chkInsUse").on("click", function() { $("#cmbAccesos").val("2"); });
	$("#chkDelUse").on("click", function() { $("#cmbAccesos").val("2"); });
	$("#chModUse").on("click", function() { $("#cmbAccesos").val("2"); });
	$("#chAccUse").on("click", function() { $("#cmbAccesos").val("2"); });
});

//Accesorios
var deshabilitar_accesorios = function () {
	if($("#chkConsAcc").is(":checked")) {
		$("#chkAddAcc").prop("disabled", false);
		$("#chkDevAcc").prop("disabled", false);
		$("#chkIngAcc").prop("disabled", false);
		$("#chkModAcc").prop("disabled", false);
		$("#chkRetAcc").prop("disabled", false);
	}
	else {
		$("#chkAddAcc").prop("disabled", true);
		$("#chkAddAcc").prop("checked", false); 
		$("#chkDevAcc").prop("disabled", true);
		$("#chkDevAcc").prop("checked", false); 
		$("#chkIngAcc").prop("disabled", true);
		$("#chkIngAcc").prop("checked", false); 
		$("#chkModAcc").prop("disabled", true);
		$("#chkModAcc").prop("checked", false); 
		$("#chkRetAcc").prop("disabled", true);
		$("#chkRetAcc").prop("checked", false); 
	}
};
$(deshabilitar_accesorios);

//Clasificaciones
var deshabilitar_clasificaciones = function () {
	if($("#chkConsCla").is(":checked")) {
		$("#chkInsCla").prop("disabled", false);
		$("#chkDelCla").prop("disabled", false);
		$("#chModCla").prop("disabled", false);
	}
	else {
		$("#chkInsCla").prop("disabled", true);
		$("#chkInsCla").prop("checked", false); 
		$("#chkDelCla").prop("disabled", true);
		$("#chkDelCla").prop("checked", false); 
		$("#chModCla").prop("disabled", true);
		$("#chModCla").prop("checked", false); 
	}
};
$(deshabilitar_clasificaciones);

//Bitacoras
var deshabilitar_bitacoras = function () {
	if($("#chkConBit").is(":checked")) {
		$("#chkRepBit").prop("disabled", false);
	}
	else {
		$("#chkRepBit").prop("disabled", true);
		$("#chkRepBit").prop("checked", false); 
	}
};
$(deshabilitar_bitacoras);

//Usuarios
var deshabilitar_usuarios = function () {
	if($("#chkConsUse").is(":checked")) {
		$("#chkInsUse").prop("disabled", false);
		$("#chkDelUse").prop("disabled", false);
		$("#chModUse").prop("disabled", false);
		$("#chAccUse").prop("disabled", false);
	}
	else {
		$("#chkInsUse").prop("disabled", true);
		$("#chkInsUse").prop("checked", false); 
		$("#chkDelUse").prop("disabled", true);
		$("#chkDelUse").prop("checked", false);
		$("#chModUse").prop("disabled", true);
		$("#chModUse").prop("checked", false); 
		$("#chAccUse").prop("disabled", true);
		$("#chAccUse").prop("checked", false); 
	}
};
$(deshabilitar_usuarios);

//Para el Combobox
var cambio = function() {
	if($("#cmbAccesos").val() == "0") {
		$("#chkConsAcc").prop("checked", true);
		$("#chkConsCla").prop("checked", true);
		$("#chkConBit").prop("checked", true);
		$("#chkConsUse").prop("checked", true);
		//
		deshabilitar_accesorios();
		deshabilitar_clasificaciones();
		deshabilitar_bitacoras();
		deshabilitar_usuarios();
		$("#chkAddAcc").prop("checked", true);
		$("#chkDevAcc").prop("checked", true);
		$("#chkIngAcc").prop("checked", true);
		$("#chkModAcc").prop("checked", true);
		$("#chkRetAcc").prop("checked", true);
		$("#chkInsCla").prop("checked", true);
		$("#chkDelCla").prop("checked", true);
		$("#chModCla").prop("checked", true);
		$("#chkRepBit").prop("checked", true);
		$("#chkInsUse").prop("checked", true);
		$("#chkDelUse").prop("checked", true);
		$("#chModUse").prop("checked", true);
		$("#chAccUse").prop("checked", true);
	}
	else if($("#cmbAccesos").val() == "1") {
		$("#chkConsAcc").prop("checked", true);
		$("#chkConsCla").prop("checked", false);
		$("#chkConBit").prop("checked", false);
		$("#chkConsUse").prop("checked", false);
		//
		deshabilitar_accesorios();
		deshabilitar_clasificaciones();
		deshabilitar_bitacoras();
		deshabilitar_usuarios();
		$("#chkAddAcc").prop("checked", false);
		$("#chkDevAcc").prop("checked", false);
		$("#chkIngAcc").prop("checked", false);
		$("#chkModAcc").prop("checked", false);
		$("#chkRetAcc").prop("checked", false);
		$("#chkInsCla").prop("checked", false);
		$("#chkDelCla").prop("checked", false);
		$("#chModCla").prop("checked", false);
		$("#chkRepBit").prop("checked", false);
		$("#chkInsUse").prop("checked", false);
		$("#chkDelUse").prop("checked", false);
		$("#chModUse").prop("checked", false);
		$("#chAccUse").prop("checked", false);
	}
}
$(cambio);