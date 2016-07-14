document.getElementById("product.targetQty").onkeyup = function(){
	
	var weighingStyle = document.getElementById("product.weighingStyle");
	var targetQty = document.getElementById("product.targetQty");
	var message = decode_utf8("O tipo de pesagem devem ser variável se a quantidade de peças for 0");
	
	if(weighingStyle.value == "STANDARD_WEIGHT"){
		
		if(parseInt(targetQty.value) === parseInt(0)){
			alert(message);
			
			targetQty.focus();
			targetQty.value = "";
		}
	}
	
	
};

decode_utf8 = function(s) {

	return decodeURIComponent(escape(s));

};