<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta name="viewport" content="width=device-width, initial-scale=1">

<title>Sistema Gerenciador de Frigoríficos</title>


<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib uri="http://vraptor.caelum.com.br/taglibs" prefix="vraptor"%>


<%-- <link rel="stylesheet" type="text/css" href="<c:url value='/resources/css/base.css'/>" /> --%>
<%--<link rel="stylesheet" type="text/css" href="<c:url value='/resources/css/mobile-style.css'/>">--%>
<link rel="stylesheet" type="text/css" href="<c:url value='/resources/css/jquery.mobile-1.4.5.min.css'/>">
<link rel="stylesheet" type="text/css" href="<c:url value='/resources/css/displaytag.css'/>" />
<link rel="stylesheet" type="text/css" href="<c:url value='/resources/css/redmond/jquery-ui-1.10.3.custom.min.css'/>" />
<link rel="stylesheet" type="text/css" href="<c:url value='/resources/css/sangoma-gray.css'/>" />
	
<!--[if lte IE 7]>
<style type="text/css">
html .jquerycssmenu{height: 1%;} /*Holly Hack for IE7 and below*/
</style>
<![endif]-->

<script type="text/javascript" src="<c:url value='/resources/js/jquery.mobile-1.4.5.min.js'/>"></script>
<script type="text/javascript" src="<c:url value='/resources/js/jquery-1.9.1.js'/>"></script>
<script type="text/javascript" src="<c:url value='/resources/js/jquery-ui-1.10.3.custom.min.js'/>"></script>
<script type="text/javascript" src="<c:url value='/resources/js/jquery.maskedinput.js'/>" ></script>
<script type="text/javascript" src="<c:url value='/resources/js/jquery.maskMoney.js'/>" ></script>
<script type="text/javascript" src="<c:url value='/resources/js/utils.js'/>"></script>
<script type="text/javascript" src="<c:url value='/resources/js/bootstrap.min.js'/>"></script>
<script type="text/javascript" src="<c:url value='/resources/js/jquery.datatables.min.js'/>"></script>
<script type="text/javascript" src="<c:url value='/resources/js/mascaras.js'/>" ></script>


<script type="text/javascript" charset="ISO-8859-1" src="<c:url value='/resources/js/mobileRenderMenu.js'/>" ></script>
<script type="text/javascript" charset="ISO-8859-1" src="<c:url value='/resources/js/mobileNoRenderTargetExcel.js'/>" ></script>


<script type="text/javascript">
  $(function(){
		$('#tabs').tabs();	
		$( "button, input:submit", ".ui" ).button();
		$("#accordion").accordion({
	         header: 'h3', 
	         autoHeight: false,
	         collapsible: true,
	         fillSpace: false,
	         active: false,
		 	 icons: { 'header': 'ui-icon-triangle-1-e', 'headerSelected': 'ui-icon-triangle-1-s' }
		 
	     });
	});

</script>

<script>
	function configurarMoeda() {
		$(".peso").maskMoney({ decimal: ".", thousands: "", allowZero: true, precision: 3 });
		$(".pesoNegativo").maskMoney({ decimal: ".", thousands: "", allowZero: true, precision: 3, allowNegative: true });
	}
	
	$(document).ready(function() {
		configurarMoeda();
	});
</script>

<script type="text/javascript">
function id(el){
    return document.getElementById(el);
}

 window.onload = function(){
	$('peso').onkeyup = function(){
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
			v = v.replace(/^(\d{1,})(\d{4})$/, "$1,$2");
		}
 
		this.value = v;
	}
}
</script>

<script>
	function confirm_remove(id, nome, controller) {

		msg = "Tem certeza que deseja excluir " + nome + "?";
		if (confirm(msg)) {
			location.href = "<c:url value='/"+ controller +"/remove/"+ id +"'/>";
		}
	}
</script>
<script>
	function confirm_cancel(id, nome, controller) {

		msg = "Tem certeza que deseja cancelar " + nome + "?";
		if (confirm(msg)) {
			location.href = "<c:url value='/"+ controller +"/cancel/"+ id +"'/>";			
		}
	}
</script>	

<script>
function onlyInteger(event) {
	var iKey = event.which || event.keyCode;
	//iKey = event.charCode;
	if (!((iKey > 47 && iKey < 58) || iKey == 0 || iKey == 8 || iKey == 13)) {
		return false;
	}
	return true;
}

function onlyFloat(event) {
	var iKey = event.which || event.keyCode;
	if ( (iKey > 47 && iKey < 58) || iKey == 0 || iKey == 8 || iKey == 13 || iKey == 46) {
		return true;
	}
	
	if(iKey == 44) {
		event.charCode = 46;
		return true;		
	}
	return false;
}



function Tara(o){
	
	var v = o.value
	integer = v.split('.')[0];
	 
	v = v.replace(",", ".");
		 
	o.value = v;
}  

// Novo método para o objeto 'String'
String.prototype.reverse = function(){
	return this.split('').reverse().join('');
};

</script>

<script language="JavaScript">

function adicionarPesoBr(){ 
	jQuery('.peso').priceFormat({  
        prefix: '',  
        centsSeparator: ',',  
        centsLimit: 3,  
        thousandsSeparator: '.'  
    });   
}  

function validaEan13(o){
	
	var numero = o.value;
	factorEAN13 = 3;
    sum = 0;  
    numlen = numero.length;
    
    if ( numero.length > 0 && numero.length != 12 && numero.length != 13 ){  
        alert("Digite um código de 12 caracteres para o cálculo do dígito verificador!");
        o.value = "";
    } 
    else {
    	
    	if ( numero.length == 13 ){
    		numero = numero.substring(0,11);
    	}
    	
    	if ( numero.length == 12 ){  
	        for(index = numlen; index > 0; --index){  
				sum = sum + numero.substring (index-1, index) * factorEAN13;  
		        factorEAN13 = 4 - factorEAN13;             
	        }  
	        cc = ((1000 - sum) % 10);  
	        
	        o.value = numero + cc;   
    	}
    }  
  
}  

function validaDun14(o){
	
	var numero = o.value;
	factorDUN14 = 1;
    sum = 0;  
    numlen = numero.length;  
    
    if (numero.length > 0 && numero.length != 13 && numero.length != 14 ){  
        alert("Digite um código de 13 caracteres para o cálculo do dígito verificador!");
        o.value = "";
    } 
    else {
    	
    	if ( numero.length == 14 ){
    		numero = numero.substring(0,12);
    	}
    
    	if ( numero.length == 13 ){  
	        for(index = numlen; index > 0; --index){  
	            sum = sum + numero.substring (index-1, index) * factorEAN13;  
	            factorEAN13 = 4 - factorEAN13;        
	        }  
	        cc = ((1000 - sum) % 10);  
	        
	        o.value = numero + cc;
	        
    	}
          
    }  
  
}  
</script> 

<script>

			/* Default class modification */
			$.extend( $.fn.dataTableExt.oStdClasses, {
				"sWrapper": "dataTables_wrapper form-inline"
			} );
			
			/* API method to get paging information */
			$.fn.dataTableExt.oApi.fnPagingInfo = function ( oSettings )
			{
				return {
					"iStart":         oSettings._iDisplayStart,
					"iEnd":           oSettings.fnDisplayEnd(),
					"iLength":        oSettings._iDisplayLength,
					"iTotal":         oSettings.fnRecordsTotal(),
					"iFilteredTotal": oSettings.fnRecordsDisplay(),
					"iPage":          Math.ceil( oSettings._iDisplayStart / oSettings._iDisplayLength ),
					"iTotalPages":    Math.ceil( oSettings.fnRecordsDisplay() / oSettings._iDisplayLength )
				};
			}
			
			/* Sangoma style pagination control */
			$.extend( $.fn.dataTableExt.oPagination, {
				"sangoma": {
					"fnInit": function( oSettings, nPaging, fnDraw ) {
						var oLang = oSettings.oLanguage.oPaginate;
						var fnClickHandler = function ( e ) {
							e.preventDefault();
							if ( oSettings.oApi._fnPageChange(oSettings, e.data.action) ) {
								fnDraw( oSettings );
							}
						};
						
						$(nPaging).addClass('pagination-right').append(
							'<ul class="pagination">'+
								'<li class="prev disabled"><a href="#">&larr; '+oLang.sPrevious+'</a></li>'+
								'<li class="next disabled"><a href="#">'+oLang.sNext+' &rarr; </a></li>'+
							'</ul>'
						);
						var els = $('a', nPaging);
						$(els[0]).bind( 'click.DT', { action: "previous" }, fnClickHandler );
						$(els[1]).bind( 'click.DT', { action: "next" }, fnClickHandler );
					},
					
					"fnUpdate": function ( oSettings, fnDraw ) {
						var iListLength = 5;
						var oPaging = oSettings.oInstance.fnPagingInfo();
						var an = oSettings.aanFeatures.p;
						var i, j, sClass, iStart, iEnd, iHalf=Math.floor(iListLength/2);
						
						if ( oPaging.iTotalPages < iListLength) {
							iStart = 1;
							iEnd = oPaging.iTotalPages;
						}
						else if ( oPaging.iPage <= iHalf ) {
							iStart = 1;
							iEnd = iListLength;
						} else if ( oPaging.iPage >= (oPaging.iTotalPages-iHalf) ) {
							iStart = oPaging.iTotalPages - iListLength + 1;
							iEnd = oPaging.iTotalPages;
						} else {
							iStart = oPaging.iPage - iHalf + 1;
							iEnd = iStart + iListLength - 1;
						}
						
						for ( i=0, iLen=an.length ; i<iLen ; i++ ) {
							// Remove the middle elements
							$('li:gt(0)', an[i]).filter(':not(:last)').remove();
							
							// Add the new list items and their event handlers
							for ( j=iStart ; j<=iEnd ; j++ ) {
								sClass = (j==oPaging.iPage+1) ? 'class="active"' : '';
								$('<li '+sClass+'><a href="#">'+j+'</a></li>')
									.insertBefore( $('li:last', an[i])[0] )
									.bind('click', function (e) {
										e.preventDefault();
										oSettings._iDisplayStart = (parseInt($('a', this).text(),10)-1) * oPaging.iLength;
										fnDraw( oSettings );
									} );
							}
							
							// Add / remove disabled classes from the static elements
							if ( oPaging.iPage === 0 ) {
								$('li:first', an[i]).addClass('disabled');
							} else {
								$('li:first', an[i]).removeClass('disabled');
							}
							
							if ( oPaging.iPage === oPaging.iTotalPages-1 || oPaging.iTotalPages === 0 ) {
								$('li:last', an[i]).addClass('disabled');
							} else {
								$('li:last', an[i]).removeClass('disabled');
							}
						}
					}
				}
			});
			
			/* Table #example */
			$(document).ready(function() {
				$('.datatable').dataTable( {
					"sDom": "<'row'<'col-xs-6'l><'col-xs-6'f>r>t<'row'<'col-xs-6'i><'col-xs-6'p>>",
					"sPaginationType": "sangoma",
					"oLanguage": {
						"sLengthMenu": "_MENU_ registros por pagina"
					}
				});
			});
		</script>

</head>


<body>
  <%-- <div class="sidebar">
    <div class="logo"><img src="<c:url value='/resources/img/cardeal.gif'/>" height="78" width="192"></div>

	<c:if test="${not empty userInfo.user}">   		
		
	<div id="accordion">
	  <h3>Cadastros</h3>
	  <div>
			<li><a href="<c:url value='/products'/>">Produtos</a></li>
			<li><a href="<c:url value='/orders'/>">Pedidos</a></li>
			<li><a href="<c:url value='/units'/>">Unidades</a></li>
			<li><a href="<c:url value='/partners'/>">Clientes/Fornecedores</a></li>
			<li><a href="<c:url value='/companies'/>">Filiais</a></li>
			<li><a href="<c:url value='/users'/>">Usuários</a></li>
			<li><a href="<c:url value='/profiles'/>">Perfis de Acesso</a></li>
	  </div>
	  <h3>Apontamentos</h3>
	  <div>
			<li><a href="<c:url value='#'/>">Produção</a></li>
			<li><a href="<c:url value='#'/>">Venda</a></li>
			<li><a href="<c:url value='#'/>">Recebimento</a></li>
			<li><a href="<c:url value='#'/>">Devolução</a></li>
	  </div>
	  <h3>Relatórios</h3>
	  <div>
			<li><a href="<c:url value='/stocks'/>">Estoque Atual</a></li>
			<li><a href="<c:url value='#'/>">Movimento</a></li>
			<li><a href="<c:url value='/stocks/pallets'/>">Pallets</a></li>
	  </div>
	  <h3>Manutenção</h3>
	  <div>
			<li><a href="<c:url value='/server/view'/>">Configurações</a></li>
			<li><a href="<c:url value='/terminals'/>">Terminais</a></li>
			<li><a href="<c:url value='#'/>">Etiquetas</a></li>
			<li><a href="<c:url value='#'/>">Banco de Dados</a></li>
	  </div>
	</div>

	</c:if>

    <p>
    	<strong>Versão 0.02</strong><br><br>
    	<c:if test="${not empty userInfo.user}">
    		${userInfo.user.name}<br>
    		<strong><a href="<c:url value='/logout'/>">Logout</a></strong><br>
    	</c:if>
    </p>
  </div><!-- /.sidebar --> --%>

 <%--  <div class="main">
  
		<c:if test="${not empty errors}">
			<div class="ui-widget">		
				<div class="ui-state-error ui-corner-all" style="padding: 0 .7em;margin-top: 20px;"> 
					<p><span class="ui-icon ui-icon-alert" style="float: left; margin-right: .3em;"></span> 		
			            <c:forEach items="${errors }" var="error">
			                <p>${error.category } - ${error.message }</p>
			            </c:forEach>		        
					</p>		
				</div>
			</div>		
			<br/>		
		</c:if>

		<c:if test="${not empty notice}">
			<div class="ui-widget">
				<div class="ui-state-highlight ui-corner-all" style="margin-top: 20px; padding: 0 .7em;"> 
					<p><span class="ui-icon ui-icon-info" style="float: left; margin-right: .3em;"></span>
					${notice}</p>
				</div>
			</div>	
			<br/>			
		</c:if> --%>


