let repeat = 0;
function confirmform (event, obj, mensaje){
	if(repeat == 0) {
		event.preventDefault();
		bootbox.confirm({
		    message: mensaje,
		    buttons: {
		        confirm: {
		            label: '<i class="bi bi-check-lg"></i> Si',
		            className: 'btn-success bootbox-btn-yes'
		        },
		        cancel: {
		            label: '<i class="bi bi-x-lg"></i> No',
		            className: 'btn-danger bootbox-btn-no'
		        }
		    },
		    callback: function (result) {
		    	if(result) {
		    		repeat++;
		    		$(obj).submit();
		    	}
				else repeat = 0;
		    }
		});
	}
	else repeat = 0;
};

function confirmlink (mensaje, direccion){
	bootbox.confirm({
	    message: mensaje,
	    buttons: {
	        confirm: {
	            label: '<i class="bi bi-check-lg"></i> Si',
	            className: 'btn-success bootbox-btn-yes'
	        },
	        cancel: {
	            label: '<i class="bi bi-x-lg"></i> No',
	            className: 'btn-danger bootbox-btn-no'
	        }
	    },
	    callback: function (result) {
	    	if(result) {
	    		window.location.href = direccion;
	    	}
			else repeat = 0;
	    }
	});
};