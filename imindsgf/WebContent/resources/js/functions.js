//################################################################################################################################################
/*
*    Script:    Mascaras em Javascript
*    Autor:    Matheus Biagini de Lima Dias
*    Data:    26/08/2008
*    Obs:    
*/
/*Função Pai de Mascaras*/
function formata(o,f){
	v_obj=o
	v_fun=f
	setTimeout("execmascara()",1)
}

/*Função que Executa os objetos*/
function execmascara(){
	v_obj.value=v_fun(v_obj.value)
}

/*Função que Determina as expressões regulares dos objetos*/
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

/*Função que permite apenas numeros*/
function Integer(v){
	return v.replace(/\D/g,"")
}

/*Função que padroniza telefone (11) 4184-1241*/
function Telefone(v){
	v=v.replace(/\D/g,"")                 
	v=v.replace(/^(\d\d)(\d)/g,"($1) $2") 
	v=v.replace(/(\d{4})(\d)/,"$1-$2")    
	return v
}

/*Função que padroniza telefone (11) 41841241*/
function TelefoneCall(v){
	v=v.replace(/\D/g,"")                 
	v=v.replace(/^(\d\d)(\d)/g,"($1) $2")    
	return v
}

/*Função que padroniza CPF*/
function Cpf(v){
	v=v.replace(/\D/g,"")                    
	v=v.replace(/(\d{3})(\d)/,"$1.$2")       
	v=v.replace(/(\d{3})(\d)/,"$1.$2")       
	v=v.replace(/(\d{3})(\d{1,2})$/,"$1-$2") 
	return v
}

/*Função que padroniza CEP*/
function Cep(v){
	v=v.replace(/D/g,"")                
	v=v.replace(/^(\d{5})(\d)/,"$1-$2") 
	return v
}

/*Função que padroniza CNPJ*/
function Cnpj(v){
	v=v.replace(/\D/g,"")                   
	v=v.replace(/^(\d{2})(\d)/,"$1.$2")     
	v=v.replace(/^(\d{2})\.(\d{3})(\d)/,"$1.$2.$3") 
	v=v.replace(/\.(\d{3})(\d)/,".$1/$2")           
	v=v.replace(/(\d{4})(\d)/,"$1-$2")              
	return v
}

/*Função que permite apenas numeros Romanos*/
function Romanos(v){
	v=v.toUpperCase()             
	v=v.replace(/[^IVXLCDM]/g,"") 
	
	while(v.replace(/^M{0,4}(CM|CD|D?C{0,3})(XC|XL|L?X{0,3})(IX|IV|V?I{0,3})$/,"")!="")
		v=v.replace(/.$/,"")
	return v
}

/*Função que padroniza o Site*/
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

/*Função que padroniza DATA*/
function Data(v){
	v=v.replace(/\D/g,"") 
	v=v.replace(/(\d{2})(\d)/,"$1/$2") 
	v=v.replace(/(\d{2})(\d)/,"$1/$2") 
	return v
}

/*Função que padroniza DATA*/
function Hora(v){
	v=v.replace(/\D/g,"") 
	v=v.replace(/(\d{2})(\d)/,"$1:$2")  
	return v
}

/*Função que padroniza valor monétario*/
function Valor(v){
	v=v.replace(/\D/g,"") //Remove tudo o que não é dígito
	v=v.replace(/^([0-9]{3}\.?){3}-[0-9]{2}$/,"$1.$2");
	//v=v.replace(/(\d{3})(\d)/g,"$1,$2")
	v=v.replace(/(\d)(\d{2})$/,"$1.$2") //Coloca ponto antes dos 2 últimos digitos
	return v
}

/*Função que padroniza valor monétario*/
function Moeda(v){
	v=v.replace(/\D/g,"") //Remove tudo o que não é dígito
	v=v.replace(/(\d)(\d{17})$/,"$1.$2") //Coloca ponto antes dos 2 últimos digitos
	v=v.replace(/(\d)(\d{14})$/,"$1.$2") //Coloca ponto antes dos 2 últimos digitos
	v=v.replace(/(\d)(\d{11})$/,"$1.$2") //Coloca ponto antes dos 2 últimos digitos
	v=v.replace(/(\d)(\d{8})$/,"$1.$2") //Coloca ponto antes dos 2 últimos digitos
	v=v.replace(/(\d)(\d{5})$/,"$1.$2") //Coloca ponto antes dos 2 últimos digitos
	v=v.replace(/(\d)(\d{2})$/,"$1,$2") //Coloca ponto antes dos 2 últimos digitos
	return v
}

/*Função que padroniza Area*/
function Area(v){
	v=v.replace(/\D/g,"") 
	v=v.replace(/(\d)(\d{2})$/,"$1.$2") 
	return v
	
}

function DecimalASP(campo,n){

	c=$$(campo).value;		// Pega o valor do meu campo
	c=c.replace(/\D/g,"") 	// Remove tudo o que não é dígito
	total = c.length;		// Total de caracteres
	
	// separo a parte inteira da decimal e trato de forma apropriada.
	if(total > n){			
		decimal = c.substr(total-n,n);
		inteiro = parseFloat(c.substr(0,total-n));
	}else{
		inteiro = 0;
		decimal = c;
		for (i=total;i<n;i++){
			decimal = "0" + decimal;
		}
	}

	// aqui vou colocar a virgula a cada 3 casas decimais... se quiser mudar para ponto é só trocar a virgula ao lado do $1 por ponto.
	xxx = inteiro.toString();
	xxx = xxx.replace(/(\d)(\d{21})$/,"$1,$2") //Coloca virgula
	xxx = xxx.replace(/(\d)(\d{18})$/,"$1,$2") //Coloca virgula
	xxx = xxx.replace(/(\d)(\d{15})$/,"$1,$2") //Coloca virgula
	xxx = xxx.replace(/(\d)(\d{12})$/,"$1,$2") //Coloca virgula
	xxx = xxx.replace(/(\d)(\d{9})$/,"$1,$2") //Coloca virgula
	xxx = xxx.replace(/(\d)(\d{6})$/,"$1,$2") //Coloca virgula
	xxx = xxx.replace(/(\d)(\d{3})$/,"$1,$2") //Coloca virgula
	
	$$(campo).value = xxx + "." + decimal; // se quiser virgula na casa decimal, troque aqui.
}
//################################################################################################################################################
//################################################################################################################################################

// FORMATANDO TEXTO
//<input name="tx_telefone" type="text" id="tx_telefone" onkeypress="return txtBoxFormat(this, '(99) 9999-9999', event);" maxlength="14"/>
//################################################################################################################################################
//################################################################################################################################################
function txtBoxFormat(campo, sMask, evtKeyPress) {
var i, nCount, sValue, fldLen, mskLen,bolMask, sCod, nTecla;

if(document.all) { // Internet Explorer
nTecla = evtKeyPress.keyCode;
} else if(document.layers) { // Nestcape
nTecla = evtKeyPress.which;
} else {
nTecla = evtKeyPress.which;
if (nTecla == 8) {
return true;
}
}

sValue = campo.value;
// Limpa todos os caracteres de formatação que
// já estiverem no campo.
sValue = sValue.toString().replace( "-", "" );
sValue = sValue.toString().replace( "-", "" );
sValue = sValue.toString().replace( ".", "" );
sValue = sValue.toString().replace( ".", "" );
sValue = sValue.toString().replace( "/", "" );
sValue = sValue.toString().replace( "/", "" );
sValue = sValue.toString().replace( "(", "" );
sValue = sValue.toString().replace( "(", "" );
sValue = sValue.toString().replace( ")", "" );
sValue = sValue.toString().replace( ")", "" );
sValue = sValue.toString().replace( ":", "" );
sValue = sValue.toString().replace( " ", "" );
sValue = sValue.toString().replace( " ", "" );
fldLen = sValue.length;
mskLen = sMask.length;

i = 0;
nCount = 0;
sCod = "";
mskLen = fldLen;

while (i <= mskLen) {
bolMask = ((sMask.charAt(i) == "-") || (sMask.charAt(i) == ":") || (sMask.charAt(i) == ".") || (sMask.charAt(i) == "/"))
bolMask = bolMask || ((sMask.charAt(i) == "(") || (sMask.charAt(i) == ")") || (sMask.charAt(i) == " "))

if (bolMask) {
sCod += sMask.charAt(i);
mskLen++;
} else {
sCod += sValue.charAt(nCount);
nCount++;
}
i++;
}

campo.value = sCod;
if (nTecla != 8) { // backspace
if (sMask.charAt(i-1) == "9") { // apenas números...
return ((nTecla > 47) && (nTecla < 58)); } // números de 0 a 9
else { // qualquer caracter...
return true;
}
} else {
return true;
}
}
//################################################################################################################################################
//################################################################################################################################################

//VALIDANDO E-MAIL
//################################################################################################################################################
//################################################################################################################################################
function isValidEmail(email, required) {
    if (required==undefined) {   // if not specified, assume it's required
        required=true;
    }
    if (email==null) {
        if (required) {
            return false;
        }
        return true;
    }
    if (email.length==0) {  
        if (required) {
            return false;
        }
        return true;
    }
    if (! allValidChars(email)) {  // check to make sure all characters are valid
        return false;
    }
    if (email.indexOf("@") < 1) { //  must contain @, and it must not be the first character
        return false;
    } else if (email.lastIndexOf(".") <= email.indexOf("@")) {  // last dot must be after the @
        return false;
    } else if (email.indexOf("@") == email.length) {  // @ must not be the last character
        return false;
    } else if (email.indexOf("..") >=0) { // two periods in a row is not valid
	return false;
    } else if (email.indexOf(".") == email.length) {  // . must not be the last character
	return false;
    }
    return true;
}

function allValidChars(email) {
  var parsed = true;
  var validchars = "abcdefghijklmnopqrstuvwxyz0123456789@.-_";
  for (var i=0; i < email.length; i++) {
    var letter = email.charAt(i).toLowerCase();
    if (validchars.indexOf(letter) != -1)
      continue;
    parsed = false;
    break;
  }
  return parsed;
}
//################################################################################################################################################
//################################################################################################################################################


// VALIDANDO CPF E CNPJ
//by bamboo, szilard & igbassani
//################################################################################################################################################
function VALIDAR(CNUMB,CTYPE)
{
  if(Verify(CNUMB, CTYPE))
  {
    // alert(CTYPE+" válido!");
	return true;
}
  else
  {
    // alert(CTYPE+" inválido!");
	return false;
  }
  // return;
}

function ClearStr(str, char)
{
  while((cx=str.indexOf(char))!=-1)
  {		
    str = str.substring(0,cx)+str.substring(cx+1);
  }
  return(str);
}

function ParseNumb(c)
{
  c=ClearStr(c,'-');
  c=ClearStr(c,'/');
  c=ClearStr(c,',');
  c=ClearStr(c,'.');
  c=ClearStr(c,'(');
  c=ClearStr(c,')');
  c=ClearStr(c,' ');
  if((parseFloat(c) / c != 1))
  {
    if(parseFloat(c) * c == 0)
    {
      return(c);
    }
    else
    {
      return(0);
    }
  }
  else
  {
    return(c);
  }
}

function Verify(CNUMB,CTYPE)
{
  CNUMB=ParseNumb(CNUMB)
  if(CNUMB == 0)
  {
    return(false);
  }
  else
  {
    g=CNUMB.length-2;
    if(TestDigit(CNUMB,CTYPE,g))
    {
      g=CNUMB.length-1;
      if(TestDigit(CNUMB,CTYPE,g))
      {	
        return(true);
      }
      else
      {
        return(false);
      }
    }
    else
    {
      return(false);
    }
  }
}

function TestDigit(CNUMB,CTYPE,g)
{
  var dig=0;
  var ind=2;
  for(f=g;f>0;f--)
  {
    dig+=parseInt(CNUMB.charAt(f-1))*ind;
    if (CTYPE=='CNPJ')
    { if(ind>8) {ind=2} else {ind++} }
    else
    { ind++ }
  }
  dig%=11;
  if(dig<2)
  {
    dig=0;
  }
  else
  {
    dig=11-dig;
  }
  if(dig!=parseInt(CNUMB.charAt(g)))
  {
    return(false);
  }
  else
  {
    return(true);
  }
}
//################################################################################################################################################
//################################################################################################################################################


// ===================================================================
// Author: Matt Kruse <matt@mattkruse.com>
// WWW: http://www.mattkruse.com/
//
// NOTICE: You may use this code for any purpose, commercial or
// private, without any further permission from the author. You may
// remove this notice from your final code if you wish, however it is
// appreciated by the author if at least my web site address is kept.
//
// You may *NOT* re-distribute this code in any way except through its
// use. That means, you can include it in your product, or your web
// site, or any other form where the code is actually being used. You
// may not put the plain javascript up on your site for download or
// include it in your javascript libraries for download. 
// If you wish to share this code with others, please just point them
// to the URL instead.
// Please DO NOT link directly to my .js files from your site. Copy
// the files to your server and use them there. Thank you.
// ===================================================================

// HISTORY
// ------------------------------------------------------------------
// May 17, 2003: Fixed bug in parseDate() for dates <1970
// March 11, 2003: Added parseDate() function
// March 11, 2003: Added "NNN" formatting option. Doesn't match up
//                 perfectly with SimpleDateFormat formats, but 
//                 backwards-compatability was required.

// ------------------------------------------------------------------
// These functions use the same 'format' strings as the 
// java.text.SimpleDateFormat class, with minor exceptions.
// The format string consists of the following abbreviations:
// 
// Field        | Full Form          | Short Form
// -------------+--------------------+-----------------------
// Year         | yyyy (4 digits)    | yy (2 digits), y (2 or 4 digits)
// Month        | MMM (name or abbr.)| MM (2 digits), M (1 or 2 digits)
//              | NNN (abbr.)        |
// Day of Month | dd (2 digits)      | d (1 or 2 digits)
// Day of Week  | EE (name)          | E (abbr)
// Hour (1-12)  | hh (2 digits)      | h (1 or 2 digits)
// Hour (0-23)  | HH (2 digits)      | H (1 or 2 digits)
// Hour (0-11)  | KK (2 digits)      | K (1 or 2 digits)
// Hour (1-24)  | kk (2 digits)      | k (1 or 2 digits)
// Minute       | mm (2 digits)      | m (1 or 2 digits)
// Second       | ss (2 digits)      | s (1 or 2 digits)
// AM/PM        | a                  |
//
// NOTE THE DIFFERENCE BETWEEN MM and mm! Month=MM, not mm!
// Examples:
//  "MMM d, y" matches: January 01, 2000
//                      Dec 1, 1900
//                      Nov 20, 00
//  "M/d/yy"   matches: 01/20/00
//                      9/2/00
//  "MMM dd, yyyy hh:mm:ssa" matches: "January 01, 2000 12:30:45AM"
// ------------------------------------------------------------------

var MONTH_NAMES=new Array('January','February','March','April','May','June','July','August','September','October','November','December','Jan','Feb','Mar','Apr','May','Jun','Jul','Aug','Sep','Oct','Nov','Dec');
var DAY_NAMES=new Array('Sunday','Monday','Tuesday','Wednesday','Thursday','Friday','Saturday','Sun','Mon','Tue','Wed','Thu','Fri','Sat');
function LZ(x) {return(x<0||x>9?"":"0")+x}

// ------------------------------------------------------------------
// isDate ( date_string, format_string )
// Returns true if date string matches format of format string and
// is a valid date. Else returns false.
// It is recommended that you trim whitespace around the value before
// passing it to this function, as whitespace is NOT ignored!
// ------------------------------------------------------------------
function isDate(val,format) {
	var date=getDateFromFormat(val,format);
	if (date==0) { return false; }
	return true;
	}

// -------------------------------------------------------------------
// compareDates(date1,date1format,date2,date2format)
//   Compare two date strings to see which is greater.
//   Returns:
//   1 if date1 is greater than date2
//   0 if date2 is greater than date1 of if they are the same
//  -1 if either of the dates is in an invalid format
// -------------------------------------------------------------------
function compareDates(date1,dateformat1,date2,dateformat2) {
	var d1=getDateFromFormat(date1,dateformat1);
	var d2=getDateFromFormat(date2,dateformat2);
	if (d1==0 || d2==0) {
		return -1;
		}
	else if (d1 > d2) {
		return 1;
		}
	return 0;
	}

// ------------------------------------------------------------------
// formatDate (date_object, format)
// Returns a date in the output format specified.
// The format string uses the same abbreviations as in getDateFromFormat()
// ------------------------------------------------------------------
function formatDate(date,format) {
	format=format+"";
	var result="";
	var i_format=0;
	var c="";
	var token="";
	var y=date.getYear()+"";
	var M=date.getMonth()+1;
	var d=date.getDate();
	var E=date.getDay();
	var H=date.getHours();
	var m=date.getMinutes();
	var s=date.getSeconds();
	var yyyy,yy,MMM,MM,dd,hh,h,mm,ss,ampm,HH,H,KK,K,kk,k;
	// Convert real date parts into formatted versions
	var value=new Object();
	if (y.length < 4) {y=""+(y-0+1900);}
	value["y"]=""+y;
	value["yyyy"]=y;
	value["yy"]=y.substring(2,4);
	value["M"]=M;
	value["MM"]=LZ(M);
	value["MMM"]=MONTH_NAMES[M-1];
	value["NNN"]=MONTH_NAMES[M+11];
	value["d"]=d;
	value["dd"]=LZ(d);
	value["E"]=DAY_NAMES[E+7];
	value["EE"]=DAY_NAMES[E];
	value["H"]=H;
	value["HH"]=LZ(H);
	if (H==0){value["h"]=12;}
	else if (H>12){value["h"]=H-12;}
	else {value["h"]=H;}
	value["hh"]=LZ(value["h"]);
	if (H>11){value["K"]=H-12;} else {value["K"]=H;}
	value["k"]=H+1;
	value["KK"]=LZ(value["K"]);
	value["kk"]=LZ(value["k"]);
	if (H > 11) { value["a"]="PM"; }
	else { value["a"]="AM"; }
	value["m"]=m;
	value["mm"]=LZ(m);
	value["s"]=s;
	value["ss"]=LZ(s);
	while (i_format < format.length) {
		c=format.charAt(i_format);
		token="";
		while ((format.charAt(i_format)==c) && (i_format < format.length)) {
			token += format.charAt(i_format++);
			}
		if (value[token] != null) { result=result + value[token]; }
		else { result=result + token; }
		}
	return result;
	}
	
// ------------------------------------------------------------------
// Utility functions for parsing in getDateFromFormat()
// ------------------------------------------------------------------
function _isInteger(val) {
	var digits="1234567890";
	for (var i=0; i < val.length; i++) {
		if (digits.indexOf(val.charAt(i))==-1) { return false; }
		}
	return true;
	}
function _getInt(str,i,minlength,maxlength) {
	for (var x=maxlength; x>=minlength; x--) {
		var token=str.substring(i,i+x);
		if (token.length < minlength) { return null; }
		if (_isInteger(token)) { return token; }
		}
	return null;
	}
	
// ------------------------------------------------------------------
// getDateFromFormat( date_string , format_string )
//
// This function takes a date string and a format string. It matches
// If the date string matches the format string, it returns the 
// getTime() of the date. If it does not match, it returns 0.
// ------------------------------------------------------------------
function getDateFromFormat(val,format) {
	val=val+"";
	format=format+"";
	var i_val=0;
	var i_format=0;
	var c="";
	var token="";
	var token2="";
	var x,y;
	var now=new Date();
	var year=now.getYear();
	var month=now.getMonth()+1;
	var date=1;
	var hh=now.getHours();
	var mm=now.getMinutes();
	var ss=now.getSeconds();
	var ampm="";
	
	while (i_format < format.length) {
		// Get next token from format string
		c=format.charAt(i_format);
		token="";
		while ((format.charAt(i_format)==c) && (i_format < format.length)) {
			token += format.charAt(i_format++);
			}
		// Extract contents of value based on format token
		if (token=="yyyy" || token=="yy" || token=="y") {
			if (token=="yyyy") { x=4;y=4; }
			if (token=="yy")   { x=2;y=2; }
			if (token=="y")    { x=2;y=4; }
			year=_getInt(val,i_val,x,y);
			if (year==null) { return 0; }
			i_val += year.length;
			if (year.length==2) {
				if (year > 70) { year=1900+(year-0); }
				else { year=2000+(year-0); }
				}
			}
		else if (token=="MMM"||token=="NNN"){
			month=0;
			for (var i=0; i<MONTH_NAMES.length; i++) {
				var month_name=MONTH_NAMES[i];
				if (val.substring(i_val,i_val+month_name.length).toLowerCase()==month_name.toLowerCase()) {
					if (token=="MMM"||(token=="NNN"&&i>11)) {
						month=i+1;
						if (month>12) { month -= 12; }
						i_val += month_name.length;
						break;
						}
					}
				}
			if ((month < 1)||(month>12)){return 0;}
			}
		else if (token=="EE"||token=="E"){
			for (var i=0; i<DAY_NAMES.length; i++) {
				var day_name=DAY_NAMES[i];
				if (val.substring(i_val,i_val+day_name.length).toLowerCase()==day_name.toLowerCase()) {
					i_val += day_name.length;
					break;
					}
				}
			}
		else if (token=="MM"||token=="M") {
			month=_getInt(val,i_val,token.length,2);
			if(month==null||(month<1)||(month>12)){return 0;}
			i_val+=month.length;}
		else if (token=="dd"||token=="d") {
			date=_getInt(val,i_val,token.length,2);
			if(date==null||(date<1)||(date>31)){return 0;}
			i_val+=date.length;}
		else if (token=="hh"||token=="h") {
			hh=_getInt(val,i_val,token.length,2);
			if(hh==null||(hh<1)||(hh>12)){return 0;}
			i_val+=hh.length;}
		else if (token=="HH"||token=="H") {
			hh=_getInt(val,i_val,token.length,2);
			if(hh==null||(hh<0)||(hh>23)){return 0;}
			i_val+=hh.length;}
		else if (token=="KK"||token=="K") {
			hh=_getInt(val,i_val,token.length,2);
			if(hh==null||(hh<0)||(hh>11)){return 0;}
			i_val+=hh.length;}
		else if (token=="kk"||token=="k") {
			hh=_getInt(val,i_val,token.length,2);
			if(hh==null||(hh<1)||(hh>24)){return 0;}
			i_val+=hh.length;hh--;}
		else if (token=="mm"||token=="m") {
			mm=_getInt(val,i_val,token.length,2);
			if(mm==null||(mm<0)||(mm>59)){return 0;}
			i_val+=mm.length;}
		else if (token=="ss"||token=="s") {
			ss=_getInt(val,i_val,token.length,2);
			if(ss==null||(ss<0)||(ss>59)){return 0;}
			i_val+=ss.length;}
		else if (token=="a") {
			if (val.substring(i_val,i_val+2).toLowerCase()=="am") {ampm="AM";}
			else if (val.substring(i_val,i_val+2).toLowerCase()=="pm") {ampm="PM";}
			else {return 0;}
			i_val+=2;}
		else {
			if (val.substring(i_val,i_val+token.length)!=token) {return 0;}
			else {i_val+=token.length;}
			}
		}
	// If there are any trailing characters left in the value, it doesn't match
	if (i_val != val.length) { return 0; }
	// Is date valid for month?
	if (month==2) {
		// Check for leap year
		if ( ( (year%4==0)&&(year%100 != 0) ) || (year%400==0) ) { // leap year
			if (date > 29){ return 0; }
			}
		else { if (date > 28) { return 0; } }
		}
	if ((month==4)||(month==6)||(month==9)||(month==11)) {
		if (date > 30) { return 0; }
		}
	// Correct hours value
	if (hh<12 && ampm=="PM") { hh=hh-0+12; }
	else if (hh>11 && ampm=="AM") { hh-=12; }
	var newdate=new Date(year,month-1,date,hh,mm,ss);
	return newdate.getTime();
	}

// ------------------------------------------------------------------
// parseDate( date_string [, prefer_euro_format] )
//
// This function takes a date string and tries to match it to a
// number of possible date formats to get the value. It will try to
// match against the following international formats, in this order:
// y-M-d   MMM d, y   MMM d,y   y-MMM-d   d-MMM-y  MMM d
// M/d/y   M-d-y      M.d.y     MMM-d     M/d      M-d
// d/M/y   d-M-y      d.M.y     d-MMM     d/M      d-M
// A second argument may be passed to instruct the method to search
// for formats like d/M/y (european format) before M/d/y (American).
// Returns a Date object or null if no patterns match.
// ------------------------------------------------------------------
function parseDate(val) {
	var preferEuro=(arguments.length==2)?arguments[1]:false;
	generalFormats=new Array('y-M-d','MMM d, y','MMM d,y','y-MMM-d','d-MMM-y','MMM d');
	monthFirst=new Array('M/d/y','M-d-y','M.d.y','MMM-d','M/d','M-d');
	dateFirst =new Array('d/M/y','d-M-y','d.M.y','d-MMM','d/M','d-M');
	var checkList=new Array('generalFormats',preferEuro?'dateFirst':'monthFirst',preferEuro?'monthFirst':'dateFirst');
	var d=null;
	for (var i=0; i<checkList.length; i++) {
		var l=window[checkList[i]];
		for (var j=0; j<l.length; j++) {
			d=getDateFromFormat(val,l[j]);
			if (d!=0) { return new Date(d); }
			}
		}
	return null;
	}


//################################################################################################################################################
//################################################################################################################################################


// DETECTAR BROWSER
//document.write('<p class="accent">You\'re using ' + BrowserDetect.browser + ' ' + BrowserDetect.version + ' on ' + BrowserDetect.OS + '!</p>');
//################################################################################################################################################
var BrowserDetect = {
	init: function () {
		this.browser = this.searchString(this.dataBrowser) || "An unknown browser";
		this.version = this.searchVersion(navigator.userAgent)
			|| this.searchVersion(navigator.appVersion)
			|| "an unknown version";
		this.OS = this.searchString(this.dataOS) || "an unknown OS";
	},
	searchString: function (data) {
		for (var i=0;i<data.length;i++)	{
			var dataString = data[i].string;
			var dataProp = data[i].prop;
			this.versionSearchString = data[i].versionSearch || data[i].identity;
			if (dataString) {
				if (dataString.indexOf(data[i].subString) != -1)
					return data[i].identity;
			}
			else if (dataProp)
				return data[i].identity;
		}
	},
	searchVersion: function (dataString) {
		var index = dataString.indexOf(this.versionSearchString);
		if (index == -1) return;
		return parseFloat(dataString.substring(index+this.versionSearchString.length+1));
	},
	dataBrowser: [
		{
			string: navigator.userAgent,
			subString: "Chrome",
			identity: "Chrome"
		},
		{ 	string: navigator.userAgent,
			subString: "OmniWeb",
			versionSearch: "OmniWeb/",
			identity: "OmniWeb"
		},
		{
			string: navigator.vendor,
			subString: "Apple",
			identity: "Safari",
			versionSearch: "Version"
		},
		{
			prop: window.opera,
			identity: "Opera"
		},
		{
			string: navigator.vendor,
			subString: "iCab",
			identity: "iCab"
		},
		{
			string: navigator.vendor,
			subString: "KDE",
			identity: "Konqueror"
		},
		{
			string: navigator.userAgent,
			subString: "Firefox",
			identity: "Firefox"
		},
		{
			string: navigator.vendor,
			subString: "Camino",
			identity: "Camino"
		},
		{		// for newer Netscapes (6+)
			string: navigator.userAgent,
			subString: "Netscape",
			identity: "Netscape"
		},
		{
			string: navigator.userAgent,
			subString: "MSIE",
			identity: "Explorer",
			versionSearch: "MSIE"
		},
		{
			string: navigator.userAgent,
			subString: "Gecko",
			identity: "Mozilla",
			versionSearch: "rv"
		},
		{ 		// for older Netscapes (4-)
			string: navigator.userAgent,
			subString: "Mozilla",
			identity: "Netscape",
			versionSearch: "Mozilla"
		}
	],
	dataOS : [
		{
			string: navigator.platform,
			subString: "Win",
			identity: "Windows"
		},
		{
			string: navigator.platform,
			subString: "Mac",
			identity: "Mac"
		},
		{
			   string: navigator.userAgent,
			   subString: "iPhone",
			   identity: "iPhone/iPod"
	    },
		{
			string: navigator.platform,
			subString: "Linux",
			identity: "Linux"
		}
	]

};
BrowserDetect.init();
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

//FUNCAO PARA PREENCHER OS COMBOBOXES QUE USO...
//################################################################################################################################################
function carregaCombo(acao,combo,vars,sele){
	var head		= document.getElementsByTagName('head').item(0);
	var eScript		= document.createElement("script");
	$$(combo).options.length = 0;
	eScript.setAttribute('src','classes/classe_combo.asp?comb='+combo+"&vars="+vars+"&acao="+acao+"&sele="+sele);
	head.appendChild(eScript);
}
//################################################################################################################################################
//################################################################################################################################################
