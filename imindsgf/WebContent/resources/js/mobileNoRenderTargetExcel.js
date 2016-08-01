$(function(){
	
	var stock           = document.getElementById("listStock");
	var stockTotal      = document.getElementById("listStockTotal");
	var dailyProduction = document.getElementById("dailyProduction");
	var pallets  		= document.getElementById("pallets");
	var history  		= document.getElementById("history");
	var orders          = document.getElementById("listOrders");
	
	if(stock){
		detectMobile(stock.id);
	}
	
	if(stockTotal){
		detectMobile(stockTotal.id);
	}
	
	if(dailyProduction){
		detectMobile(dailyProduction.id);
	}
	
	if(pallets){
		detectMobile(pallets.id);
	}
	
	if(history){
		detectMobile(history.id);
	}
	
	if(orders){
		detectMobile(orders.id);
	}
	
});

var detectMobile = function(id) {
	if (!navigator.userAgent.match(/Android/i)
			|| navigator.userAgent.match(/webOs/i)
			|| navigator.userAgent.match(/iPhone/i)
			|| navigator.userAgent.match(/Ipad/i)
			|| navigator.userAgent.match(/Ipdod/i)
			|| navigator.userAgent.match(/BlackBerry/i)
			|| navigator.userAgent.match(/Windows Phone/i)) {

		typeId(id);
	}
};
var typeId = function(id){
	var a;
	var enterDateDe = 		document.getElementById("enterDateDe");
	var enterDateAte = 		document.getElementById("enterDateAte");
	var productIdMascDe = 	document.getElementById("productIdMascDe");
	var productIdMascAte = 	document.getElementById("productIdMascAte");
	var serialNumberDe = 	document.getElementById("serialNumberDe");
	var serialNumberAte = 	document.getElementById("serialNumberAte");
	var manufactureDateDe = document.getElementById("manufactureDateDe");
	var manufactureDateAte =document.getElementById("manufactureDateAte");
	var typeStock = 		document.getElementById("typeStock");
	var terminalId =		document.getElementById("terminalId");
	var terminalId2 =		document.getElementById("terminalId2");
	var filialId =			document.getElementById("companyId");
	var filialId2 =			document.getElementById("companyId2");
	var palletIdDe = 		document.getElementById("palletIdDe");	
	var palletIdAte = 		document.getElementById("palletIdAte");
	var orderIdDe = 		document.getElementById("orderIdDe");
	var orderIdAte = 		document.getElementById("orderIdAte");
	var orderStatus = 		document.getElementById("orderStatus");
	
	switch (id) 
	{
		case "listStock":
			a = '<header><h2>Opções para Exportação</h2>' +
				'<ul class="data-header-actions">'+
				'<li><form class="form-horizontal" name="form_search" method="post" action="/imindsgf/stocks/exportStockProductionToPDF">' +
				'<input type="hidden" class="form-control" name="filter.companyIdDe" id="filter.companyIdDe" value="'+ filialId.value +'" >' +
				'<input type="hidden" class="form-control" name="filter.companyIdAte" id="filter.companyIdAte" value="'+ filialId2.value +'" />'+
				'<input type="hidden" class="form-control" name="filter.terminalId" id="filter.terminalId" value="'+ terminalId.value +'" >' +
				'<input type="hidden" class="form-control" name="filter.terminalId_2" id="filter.terminalId_2" value="'+ terminalId2.value +'" />'+
				'<input type="hidden" class="form-control" name="filter.idMasc" id="filter.idMasc" value="'+ productIdMascDe.value +'" />'+
				'<input type="hidden" class="form-control" name="filter.idMasc_2" id="filter.idMasc_2" value="'+ productIdMascAte.value +'" />'+
				'<input type="hidden" class="form-control" name="filter.id" id="filter.id" value="'+ serialNumberDe.value +'" />'+
				'<input type="hidden" class="form-control" name="filter.id_2" id="filter.id_2" value="'+ serialNumberAte.value +'" />'+
				'<input type="hidden" class="form-control" name="filter.typeStock" id="filter.typeStock" value="'+ typeStock.value +'" />'+
				'<input type="hidden" class="form-control" id="date1" name="date1" value="'+ enterDateDe.value +'"/>' +
				'<input type="hidden" class="form-control" id="date2" name="date2" value="'+ enterDateAte.value +'"/>' +
				'<input type="hidden" class="form-control" id="date3" name="date3" value="'+ manufactureDateDe.value +'"/>' +
				'<input type="hidden" class="form-control" id="date4" name="date4" value="'+ manufactureDateAte.value +'"/>' + 
			    '<input type="submit" value="PDF" class="btn btn-md btn-inverse pull-right"/></li>' +
			    '</form>' +
			    '<li><form class="form-horizontal" name="form_search" method="post" action="/imindsgf/stocks/exportStockToExcel">' +
			    '<input type="hidden" class="form-control" name="filter.companyIdDe" id="filter.companyIdDe" value="'+ filialId.value +'" >' +
				'<input type="hidden" class="form-control" name="filter.companyIdAte" id="filter.companyIdAte" value="'+ filialId2.value +'" />'+
				'<input type="hidden" class="form-control" name="filter.terminalId" id="filter.terminalId" value="'+ terminalId.value +'" >' +
				'<input type="hidden" class="form-control" name="filter.terminalId_2" id="filter.terminalId_2" value="'+ terminalId2.value +'" />'+
				'<input type="hidden" class="form-control" name="filter.idMasc" id="filter.idMasc" value="'+ productIdMascDe.value +'" />'+
				'<input type="hidden" class="form-control" name="filter.idMasc_2" id="filter.idMasc_2" value="'+ productIdMascAte.value +'" />'+
				'<input type="hidden" class="form-control" name="filter.id" id="filter.id" value="'+ serialNumberDe.value +'" />'+
				'<input type="hidden" class="form-control" name="filter.id_2" id="filter.id_2" value="'+ serialNumberAte.value +'" />'+
				'<input type="hidden" class="form-control" name="filter.typeStock" id="filter.typeStock" value="'+ typeStock.value +'" />'+
				'<input type="hidden" class="form-control" id="date1" name="date1" value="'+ enterDateDe.value +'"/>' +
				'<input type="hidden" class="form-control" id="date2" name="date2" value="'+ enterDateAte.value +'"/>' +
				'<input type="hidden" class="form-control" id="date3" name="date3" value="'+ manufactureDateDe.value +'"/>' +
				'<input type="hidden" class="form-control" id="date4" name="date4" value="'+ manufactureDateAte.value +'"/>' + 
			    '<input type="submit" value="Excel" class="btn btn-md btn-inverse pull-right"/></li>' +
			    '</form>' +
				'</ul></header>';
			renderTarget(a);
			break;
			
		case "listStockTotal":
			a = '<header><h2>Opções para Exportação</h2>' +
				'<ul class="data-header-actions">'+
				'<li><form class="form-horizontal" name="form_search" method="post" action="/imindsgf/stocks/exportStockTotalsToPDF">' +
				'<input type="hidden" class="form-control" name="filter.companyIdDe" id="filter.companyIdDe" value="'+ filialId.value +'" >' +
				'<input type="hidden" class="form-control" name="filter.companyIdAte" id="filter.companyIdAte" value="'+ filialId2.value +'" />'+
				'<input type="hidden" class="form-control" name="filter.terminalId" id="filter.terminalId" value="'+ terminalId.value +'" >' +
				'<input type="hidden" class="form-control" name="filter.terminalId_2" id="filter.terminalId_2" value="'+ terminalId2.value +'" />'+
				'<input type="hidden" class="form-control" name="filter.idMasc" id="filter.idMasc" value="'+ productIdMascDe.value +'" />'+
				'<input type="hidden" class="form-control" name="filter.idMasc_2" id="filter.idMasc_2" value="'+ productIdMascAte.value +'" />'+
				'<input type="hidden" class="form-control" name="filter.id" id="filter.id" value="'+ serialNumberDe.value +'" />'+
				'<input type="hidden" class="form-control" name="filter.id_2" id="filter.id_2" value="'+ serialNumberAte.value +'" />'+
				'<input type="hidden" class="form-control" name="filter.typeStock" id="filter.typeStock" value="'+ typeStock.value +'" />'+
				'<input type="hidden" class="form-control" id="date1" name="date1" value="'+ enterDateDe.value +'"/>' +
				'<input type="hidden" class="form-control" id="date2" name="date2" value="'+ enterDateAte.value +'"/>' +
				'<input type="hidden" class="form-control" id="date3" name="date3" value="'+ manufactureDateDe.value +'"/>' +
				'<input type="hidden" class="form-control" id="date4" name="date4" value="'+ manufactureDateAte.value +'"/>' + 
			    '<input type="submit" value="PDF" class="btn btn-md btn-inverse pull-right"/></li>' +
			    '</form>' +
			    '<li><form class="form-horizontal" name="form_search" method="post" action="/imindsgf/stocks/exportStockTotalsToExcel">' +
			    '<input type="hidden" class="form-control" name="filter.companyIdDe" id="filter.companyIdDe" value="'+ filialId.value +'" >' +
				'<input type="hidden" class="form-control" name="filter.companyIdAte" id="filter.companyIdAte" value="'+ filialId2.value +'" />'+
				'<input type="hidden" class="form-control" name="filter.terminalId" id="filter.terminalId" value="'+ terminalId.value +'" >' +
				'<input type="hidden" class="form-control" name="filter.terminalId_2" id="filter.terminalId_2" value="'+ terminalId2.value +'" />'+
				'<input type="hidden" class="form-control" name="filter.idMasc" id="filter.idMasc" value="'+ productIdMascDe.value +'" />'+
				'<input type="hidden" class="form-control" name="filter.idMasc_2" id="filter.idMasc_2" value="'+ productIdMascAte.value +'" />'+
				'<input type="hidden" class="form-control" name="filter.id" id="filter.id" value="'+ serialNumberDe.value +'" />'+
				'<input type="hidden" class="form-control" name="filter.id_2" id="filter.id_2" value="'+ serialNumberAte.value +'" />'+
				'<input type="hidden" class="form-control" name="filter.typeStock" id="filter.typeStock" value="'+ typeStock.value +'" />'+
				'<input type="hidden" class="form-control" id="date1" name="date1" value="'+ enterDateDe.value +'"/>' +
				'<input type="hidden" class="form-control" id="date2" name="date2" value="'+ enterDateAte.value +'"/>' +
				'<input type="hidden" class="form-control" id="date3" name="date3" value="'+ manufactureDateDe.value +'"/>' +
				'<input type="hidden" class="form-control" id="date4" name="date4" value="'+ manufactureDateAte.value +'"/>' +
			    '<input type="submit" value="Excel" class="btn btn-md btn-inverse pull-right"/></li>' +
			    '</form>' +
				'</ul></header>';
			renderTarget(a);
			break;
		
		case "dailyProduction":
		
			a = '<header><h2>Opções para Exportação</h2>' +
				'<ul class="data-header-actions">'+
				'<li><form class="form-horizontal" name="form_search" method="post" action="/imindsgf/stocks/exportDailyProduction">' +
				'<input type="hidden" class="form-control" name="filter.companyIdDe" id="filter.companyIdDe" value="'+ filialId.value +'" >' +
				'<input type="hidden" class="form-control" name="filter.companyIdAte" id="filter.companyIdAte" value="'+ filialId2.value +'" />'+
				'<input type="hidden" class="form-control" name="filter.terminalId" id="filter.terminalId" value="'+ terminalId.value +'" >' +
				'<input type="hidden" class="form-control" name="filter.terminalId_2" id="filter.terminalId_2" value="'+ terminalId2.value +'" />'+
				'<input type="hidden" class="form-control" id="date1" name="date1" value="'+ enterDateDe.value +'"/>' +
				'<input type="hidden" class="form-control" id="date2" name="date2" value="'+ enterDateAte.value +'"/>' + 
			    '<input type="submit" value="PDF" class="btn btn-md btn-inverse pull-right"/></li>' +
			    '</form>' +
			    '<li><form class="form-horizontal" name="form_search" method="post" action="/imindsgf/stocks/exportStockTotalsToExcel">' +
			    '<input type="hidden" class="form-control" name="filter.companyIdDe" id="filter.companyIdDe" value="'+ filialId.value +'" >' +
				'<input type="hidden" class="form-control" name="filter.companyIdAte" id="filter.companyIdAte" value="'+ filialId2.value +'" />'+
				'<input type="hidden" class="form-control" name="filter.terminalId" id="filter.terminalId" value="'+ terminalId.value +'" >' +
				'<input type="hidden" class="form-control" name="filter.terminalId_2" id="filter.terminalId_2" value="'+ terminalId2.value +'" />'+
				'<input type="hidden" class="form-control" id="date1" name="date1" value="'+ enterDateDe.value +'"/>' +
				'<input type="hidden" class="form-control" id="date2" name="date2" value="'+ enterDateAte.value +'"/>' + 
			    '<input type="submit" value="Excel" class="btn btn-md btn-inverse pull-right"/></li>' +
			    '</form>' +
				'</ul></header>';
			 renderTarget(a);
			 break;
		
		case "pallets":
			a = '<header><h2>Opções para Exportação</h2>' +
			'<ul class="data-header-actions">'+
			'<li><form class="form-horizontal" name="form_search" method="post" action="/imindsgf/stocks/exportPalletsToPDF">' +
			'<input type="hidden" class="form-control" name="filter.companyIdDe" id="filter.companyIdDe" value="'+ filialId.value +'" >' +
			'<input type="hidden" class="form-control" name="filter.companyIdAte" id="filter.companyIdAte" value="'+ filialId2.value +'" />'+
			'<input type="hidden" class="form-control" name="filter.terminalId" id="filter.terminalId" value="'+ terminalId.value +'" >' +
			'<input type="hidden" class="form-control" name="filter.terminalId_2" id="filter.terminalId_2" value="'+ terminalId2.value +'" />'+
			'<input type="hidden" class="form-control" name="filter.palletIdDe" id="filter.palletIdDe" value="'+ palletIdDe.value +'" />'+
			'<input type="hidden" class="form-control" name="filter.palletIdAte" id="filter.palletIdAte" value="'+ palletIdAte.value +'" />'+
			'<input type="hidden" class="form-control" name="filter.idMasc" id="filter.idMasc" value="'+ productIdMascDe.value +'" />'+
			'<input type="hidden" class="form-control" name="filter.idMasc_2" id="filter.idMasc_2" value="'+ productIdMascAte.value +'" />'+
			'<input type="hidden" class="form-control" id="date3" name="date3" value="'+ manufactureDateDe.value +'"/>' +
			'<input type="hidden" class="form-control" id="date4" name="date4" value="'+ manufactureDateAte.value +'"/>' + 
		    '<input type="submit" value="PDF" class="btn btn-md btn-inverse pull-right"/></li>' +
		    '</form>' +
		    '<li><form class="form-horizontal" name="form_search" method="post" action="/imindsgf/stocks/exportPalletsToExcel">' +
		    '<input type="hidden" class="form-control" name="filter.companyIdDe" id="filter.companyIdDe" value="'+ filialId.value +'" >' +
			'<input type="hidden" class="form-control" name="filter.companyIdAte" id="filter.companyIdAte" value="'+ filialId2.value +'" />'+
			'<input type="hidden" class="form-control" name="filter.terminalId" id="filter.terminalId" value="'+ terminalId.value +'" >' +
			'<input type="hidden" class="form-control" name="filter.terminalId_2" id="filter.terminalId_2" value="'+ terminalId2.value +'" />'+
			'<input type="hidden" class="form-control" name="filter.palletIdDe" id="filter.palletIdDe" value="'+ palletIdDe.value +'" />'+
			'<input type="hidden" class="form-control" name="filter.palletIdAte" id="filter.palletIdAte" value="'+ palletIdAte.value +'" />'+
			'<input type="hidden" class="form-control" name="filter.idMasc" id="filter.idMasc" value="'+ productIdMascDe.value +'" />'+
			'<input type="hidden" class="form-control" name="filter.idMasc_2" id="filter.idMasc_2" value="'+ productIdMascAte.value +'" />'+
			'<input type="hidden" class="form-control" id="date3" name="date3" value="'+ manufactureDateDe.value +'"/>' +
			'<input type="hidden" class="form-control" id="date4" name="date4" value="'+ manufactureDateAte.value +'"/>' +
		    '<input type="submit" value="Excel" class="btn btn-md btn-inverse pull-right"/></li>' +
		    '</form>' +
			'</ul></header>';
			renderTarget(a);
			break;
		
		case "history":
			a = "<header><h2>Opções para Exportação</h2>" +
				"<ul class='data-header-actions'>" +
				"<li><input type='submit' value='Excel' class='btn btn-md btn-inverse pull-right'/></li>" +
				"</ul></header>";
			renderTarget(a);			
			break;
			
		case "listOrders":
			a = '<header><h2>Opções para Exportação</h2>' +
				'<ul class="data-header-actions">'+				
			    '<li><form class="form-horizontal" name="form_search" method="post" action="/imindsgf/orders/exportOrdersToExcel">' +
			    '<input type="hidden" class="form-control" name="filter.idOrderImportDe" id="filter.idOrderImportDe" value="'+ orderIdDe.value +'" >' +
				'<input type="hidden" class="form-control" name="filter.idOrderImportAte" id="filter.idOrderImportAte" value="'+ orderIdAte.value +'" />'+
				'<input type="hidden" class="form-control" id="date1" name="date1" value="'+ enterDateDe.value +'"/>' +
				'<input type="hidden" class="form-control" id="date2" name="date2" value="'+ enterDateAte.value +'"/>' +
				'<input type="hidden" class="form-control" id="filter.orderStatus" name="filter.orderStatus" value="'+ orderStatus.value +'"/>' +
			    '<input type="submit" value="Excel" class="btn btn-md btn-inverse pull-right"/></li>' +
			    '</form>' +
				'</ul></header>';
			renderTarget(a);
			break;
	}
	
}

var renderTarget = function(result) {
	try {
		if(result !== undefined){
			var a = decode_utf8(result)
			document.getElementById('targetExcel').innerHTML = a;
		}
	} catch (e) {
		console.log(e);
	}
}

decode_utf8 = function(s) {
	return decodeURIComponent(escape(s));
};


