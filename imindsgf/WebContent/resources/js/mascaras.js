//################################################################################################################################################
/*
*    Script:    Mascaras em Javascript
*    Autor:    Matheus Biagini de Lima Dias
*    Data:    26/08/2008
*    Obs:    
*/
/*Fun��o Pai de Mascaras*/
function formata(o,f){
	v_obj=o
	v_fun=f
	setTimeout("execmascara()",1)
}

function formatCpfCnpj(o) {
	
	if(o.value.length < 14)
		formata(o, Cpf);
	else 
		formata(o, Cnpj);
      
}

/*Fun��o que Executa os objetos*/
function execmascara(){
	v_obj.value=v_fun(v_obj.value)
}

/*Fun��o que Determina as express�es regulares dos objetos*/
function leech(v){
	v=v.replace(/o/gi,"0")
	v=v.replace(/i/gi,"1")
	v=v.replace(/z/gi,"2")
	v=v.replace(/e/gi,"3")
	v=v.replace(/a/gi,"4")
	v=v.replace(/s/gi,"5")
	v=v.replace(/t/gi,"7")
	return v
}

/*Fun��o que permite apenas numeros*/
function Integer(v){
	return v.replace(/\D/g,"")
}

/*Fun��o que padroniza telefone (11) 4184-1241*/
function Telefone(v){
	v=v.replace(/\D/g,"")                 
	v=v.replace(/^(\d\d)(\d)/g,"($1) $2") 
	v=v.replace(/(\d{4})(\d)/,"$1-$2")    
	return v
}

/*Fun��o que padroniza telefone (11) 41841241*/
function TelefoneCall(v){
	v=v.replace(/\D/g,"")                 
	v=v.replace(/^(\d\d)(\d)/g,"($1) $2")    
	return v
}

/*Fun��o que padroniza CPF*/
function Cpf(v){

	//Remove tudo que nao é digito
	v = v.replace(/\D/g, "")
    //Coloca um ponto entre o terceiro e o quarto dígitos
    v = v.replace(/(\d{3})(\d)/, "$1.$2")
    //Coloca um ponto entre o terceiro e o quarto dígitos
    //de novo (para o segundo bloco de números)
    v = v.replace(/(\d{3})(\d)/, "$1.$2")
    //Coloca um hífen entre o terceiro e o quarto dígitos
    v = v.replace(/(\d{3})(\d{1,2})$/, "$1-$2")
    
	return v
}

/*Fun��o que padroniza CEP*/
function Cep(v){
	v=v.replace(/D/g,"")                
	v=v.replace(/^(\d{5})(\d)/,"$1-$2") 
	return v
}

/*Fun��o que padroniza CNPJ*/
function Cnpj(v){
	 //Remove tudo o que não é dígito
    v = v.replace(/\D/g, "")
    //Coloca ponto entre o segundo e o terceiro dígitos
    v = v.replace(/^(\d{2})(\d)/, "$1.$2")
    //Coloca ponto entre o quinto e o sexto dígitos
    v = v.replace(/^(\d{2})\.(\d{3})(\d)/, "$1.$2.$3")
    //Coloca uma barra entre o oitavo e o nono dígitos
    v = v.replace(/\.(\d{3})(\d)/, ".$1/$2")
    //Coloca um hífen depois do bloco de quatro dígitos
    v = v.replace(/(\d{4})(\d)/, "$1-$2")
    return v
}

/*Fun��o que permite apenas numeros Romanos*/
function Romanos(v){
	v=v.toUpperCase()             
	v=v.replace(/[^IVXLCDM]/g,"") 
	
	while(v.replace(/^M{0,4}(CM|CD|D?C{0,3})(XC|XL|L?X{0,3})(IX|IV|V?I{0,3})$/,"")!="")
		v=v.replace(/.$/,"")
	return v
}

/*Fun��o que padroniza o Site*/
function Site(v){
	v=v.replace(/^http:\/\/?/,"")
	dominio=v
	caminho=""
	if(v.indexOf("/")>-1)
		dominio=v.split("/")[0]
		caminho=v.replace(/[^\/]*/,"")
		dominio=dominio.replace(/[^\w\.\+-:@]/g,"")
		caminho=caminho.replace(/[^\w\d\+-@:\?&=%\(\)\.]/g,"")
		caminho=caminho.replace(/([\?&])=/,"$1")
	if(caminho!="")dominio=dominio.replace(/\.+$/,"")
		v="http://"+dominio+caminho
	return v
}

/*Fun��o que padroniza DATA*/
function Data(v){
	v=v.replace(/\D/g,"") 
	v=v.replace(/(\d{2})(\d)/,"$1/$2") 
	v=v.replace(/(\d{2})(\d)/,"$1/$2") 
	return v
}

/*Fun��o que padroniza DATA*/
function Hora(v){
	v=v.replace(/\D/g,"") 
	v=v.replace(/(\d{2})(\d)/,"$1:$2")  
	return v
}

/*Fun��o que padroniza valor mon�tario*/
function Valor(v){
	v=v.replace(/\D/g,"") //Remove tudo o que n�o � d�gito
	v=v.replace(/^([0-9]{3}\.?){3}-[0-9]{2}$/,"$1.$2");
	//v=v.replace(/(\d{3})(\d)/g,"$1,$2")
	v=v.replace(/(\d)(\d{2})$/,"$1.$2") //Coloca ponto antes dos 2 �ltimos digitos
	return v
}

/*Fun��o que padroniza valor mon�tario*/
function Moeda(v){
	v=v.replace(/\D/g,"") //Remove tudo o que n�o � d�gito
	v=v.replace(/(\d)(\d{17})$/,"$1.$2") //Coloca ponto antes dos 2 �ltimos digitos
	v=v.replace(/(\d)(\d{14})$/,"$1.$2") //Coloca ponto antes dos 2 �ltimos digitos
	v=v.replace(/(\d)(\d{11})$/,"$1.$2") //Coloca ponto antes dos 2 �ltimos digitos
	v=v.replace(/(\d)(\d{8})$/,"$1.$2") //Coloca ponto antes dos 2 �ltimos digitos
	v=v.replace(/(\d)(\d{5})$/,"$1.$2") //Coloca ponto antes dos 2 �ltimos digitos
	v=v.replace(/(\d)(\d{2})$/,"$1,$2") //Coloca ponto antes dos 2 �ltimos digitos
	return v
}

/*Fun��o que padroniza Area*/
function Area(v){
	v=v.replace(/\D/g,"") 
	v=v.replace(/(\d)(\d{2})$/,"$1.$2") 
	return v
	
}

//################################################################################################################################################
//################################################################################################################################################

// desc: from prototype.js, basically a "getElementSByIdS"
// params: element IDs as strings, OR element IDs as single string array
// returns: array of element objects
function $$(){
	// this added by RE
	if(arguments.length==1 && arguments[0].push){
		arguments = arguments[0];
	}

	var elements = new Array();
	for (var i = 0; i < arguments.length; i++) {
		var element = arguments[i];
		if (typeof element == 'string'){
			element = document.getElementById(element);
		}
		if (arguments.length == 1){
			return element;
		}
		elements.push(element);
	}
	return elements;
}


// desc: climb DOM til we find right tagName
// params: element as object, tagName as string
// returns: element as object
function findElement(elem, tagName) {
	while (elem.parentNode && (!elem.tagName || (elem.tagName.toUpperCase() != tagName.toUpperCase()))){
		elem = elem.parentNode;
	}
	return elem;
}



/*teste samuel*/
function Peso(o){
	
	var v = this.value,
	integer = v.split(',')[0];


v = v.replace(/\D/, "");
v = v.replace(/^[0]+/, "");

if(v.length <= 4 || !integer)
{
	if(v.length === 1) v = '0,000' + v;
	if(v.length === 2) v = '0,00' + v;
	if(v.length === 3) v = '0,0' + v;
	if(v.length === 4) v = '0,' + v;
} else {
	v = v.replace(/^(\d{1,})(\d{4})$/, "$1.$2");
}

this.value = v;
	
	
	
	/*var v = o.value
	integer = v.split('.')[0];
	 
	v = v.replace(",", ".");

	if(v.lenght > 44) {
		event.charCode = 46;
		return true;		
	}
	
	o.value = v;*/
}