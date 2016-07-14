$(function() {

	detect();

});

var detect = function() {
	if (navigator.userAgent.match(/Android/i)
			|| navigator.userAgent.match(/webOs/i)
			|| navigator.userAgent.match(/iPhone/i)
			|| navigator.userAgent.match(/Ipad/i)
			|| navigator.userAgent.match(/Ipdod/i)
			|| navigator.userAgent.match(/BlackBerry/i)
			|| navigator.userAgent.match(/Windows Phone/i)) {
		renderMenu(true);
	} else {
		renderMenu(false);
	}
};
var renderMenu = function(deviceType) {

	var navigation;

	switch (deviceType) {
	case false:

		navigation = '<nav class="main-navigation navbar navbar-default" role="navigation">'
				+ '<div class="navbar-header">'
				+ '<button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".main-navigation-collapse">'
				+ '<span class="elusive icon-home"> Menu</span>'
				+ '</button>'
				+ '</div>'
				+ '<div class="main-navigation-collapse collapse navbar-collapse">'
				+ '<ul class="nav navbar-nav">'
				+ '<li class="dropdown">'
				+ '<a href="#" class="dropdown-toggle" data-toggle="dropdown">'
				+ '<span class="elusive icon-list-alt"></span>Cadastros '
				+ '<b class="caret"></b></a>'
				+ '<ul class="dropdown-menu">'
				+ '<li><a href="/cardealsgf_dev/products">Produtos</a></li>'
				+ '<!--<li><a href="<c:url value='
				+ '/orders'
				+ '/>">Pedidos</a></li>-->'
				+ '<li><a href="/cardealsgf_dev/enterprise">Empresa</a></li>'
				+ '<li><a href="/cardealsgf_dev/companies">Filiais</a></li>'
				+ '<li><a href="/cardealsgf_dev/etiquetas">Etiquetas</a></li>'
				+ '<li><a href="/cardealsgf_dev/units">Unidades</a></li>'
				+ '<li><a href="/cardealsgf_dev/partners">Clientes/Fornecedores</a></li>'				
				+ '<li><a href="/cardealsgf_dev/users">Usuários</a></li>'
				+ '<li><a href="/cardealsgf_dev/profiles">Perfis de Acesso</a></li>'
				+ '</ul></li>'
				+ '<!-- Dropdown navigation with multi-level dropdown -->'
				+ '<li class="dropdown"><a href="#" class="dropdown-toggle"data-toggle="dropdown">'
				+ '<span class="elusive icon-file"></span>Relatórios <b class="caret"></b></a>'
				+ '<ul class="dropdown-menu">'
				+ '<li><a href="/cardealsgf_dev/stocks">Estoque Atual</a></li> '
				+ '<li><a href="/cardealsgf_dev/stocks/producaoDiaria">Produção Diária</a></li>'
				+ '<li><a href="/cardealsgf_dev/stocks/pallets">Pallets</a></li>'
				+ '<li><a href="/cardealsgf_dev/historics">Histórico</a></li>'
				+ '</ul></li>'
				+ '<!-- /Dropdown navigation with multi-level dropdown -->'
				+ '<li class="dropdown"><a href="#" class="dropdown-toggle" data-toggle="dropdown"></span> Sistema <b class="caret"></b></a>'
				+ '<ul class="dropdown-menu"> '
				+ '<li><a href="/cardealsgf_dev/server/view">Configurações</a></li> '
				+ '<li><a href="/cardealsgf_dev/terminals">Terminais</a></li>'
				+ '<li><a href="/cardealsgf_dev/controlAccess">Monitor</a></li>'
				+ '</ul></li>'
				+ '<li class="dropdown">'
				+ '<a href="#" class="dropdown-toggle" data-toggle="dropdown">'
				+ '<span class="elusive icon-wrench"></span>Manutenção <b class="caret"></b></a>'
				+ '<ul class="dropdown-menu">'
				+ '<li><a href="/cardealsgf_dev/server/viewEtiquetas">Etiquetas - Definição de Layouts</a></li>'
				+ '<li><a href="/cardealsgf_dev/stocks/palletsManuten">Pallets</a></li>'				
				+ '<li><a href="/cardealsgf_dev/orders">Expedição</a></li>'
				+ '<li><a href="/cardealsgf_dev/receipts">Recebimento</a></li>'
				+ '<li><a href="/cardealsgf_dev/stocks/stockManuten">Ajuste Estoque</a></li>'
				+ '<li><a href="/cardealsgf_dev/stocks/listManuten">Estoque não paletizado</a></li>'
				+ '</ul></li>'
				+ '</ul>'
				+ '<!-- /Navigation items -->'
				+ '</div><!-- /Navigation --></nav>';

		render(navigation);

		break;

	case true:

		navigation = '<nav class="main-navigation navbar navbar-default" role="navigation">'
				+ '<div class="navbar-header">'
				+ '<button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".main-navigation-collapse">'
				+ '<span class="elusive icon-home"> Menu</span>'
				+ '</button>'
				+ '</div>'
				+ '<div class="main-navigation-collapse collapse navbar-collapse">'
				+ '<ul class="nav navbar-nav">'				
				+ '<!-- Dropdown navigation with multi-level dropdown -->'
				+ '<li class="dropdown"><a href="#" class="dropdown-toggle"data-toggle="dropdown">'
				+ '<span class="elusive icon-file"></span>Relatórios <b class="caret"></b></a>'
				+ '<ul class="dropdown-menu">'
				+ '<li><a href="/cardealsgf_dev/stocks/listMobile">Estoque Atual</a></li> '
				+ '<li><a href="/cardealsgf_dev/stocks/producaoDiariaMobile">Produção Diária</a></li>'
				+ '<li><a href="/cardealsgf_dev/stocks/palletsMobile">Pallets</a></li>'
				+ '</ul></li>'
				+ '</ul>'
				+ '<!-- /Navigation items -->'
				+ '</div><!-- /Navigation --></nav>';

		render(navigation);

		break;

	}

};

var render = function(r) {
	try {

		var a = decode_utf8(r)
		document.getElementById('navigation').innerHTML = a;
	} catch (e) {
		console.log(e);
	}
}

decode_utf8 = function(s) {

	return decodeURIComponent(escape(s));

};
