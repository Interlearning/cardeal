function mascara(o, f) {
	v_obj = o;
	v_fun = f;
	setTimeout("execmascara()", 1);
}

function execmascara() {
	v_obj.value = v_fun(v_obj.value);
}

function mtel(v) {
	v = v.replace(',', '.');
	return v;
}

function id(el) {
	return document.getElementById(el);
}

window.onload = function() {
	id('product.netWeight').onkeyup = function() {
		mascara(this, mtel);
	}
	
	id('product.targetWeight').onkeyup = function() {
		mascara(this, mtel);
	}
	
	id('product.minWeight').onkeyup = function() {
		mascara(this, mtel);
	}
	
	id('product.maxWeight').onkeyup = function() {
		mascara(this, mtel);
	}
	
	id('product.tareBox').onkeyup = function() {
		mascara(this, mtel);
	}
	
	id('product.tareEmbala').onkeyup = function() {
		mascara(this, mtel);
	}
	
	
}