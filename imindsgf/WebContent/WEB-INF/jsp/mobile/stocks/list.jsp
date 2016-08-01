<%@ include file="../annex/header.jsp" %> 

<div data-role="content" id="personal-contact">
	<div data-role="header">
		<h1>Estoque</h1>
		<a href="<c:url value='/mobile'/>" data-icon="arrow-l" data-direction="reverse">Voltar</a> 
		<%-- <a href="<c:url value='/'/>" data-icon="home" data-direction="reverse">Home</a> --%>
	</div>
	
	<div role="main" class="ui-content">
		<form name="form_search" method="post" action="<c:url value='/mobile/stocks/show'/>" >
			<div class="ui-field-contain">
				<label for="filter.id">Número de Série:</label>
				<input type="text" name="filter.id" id="filter.id" value="${filter.id}" />
			</div>
			<div class="ui-field-contain">	
				<label for="filter.productId">Produto:</label>
				<input type="text" name="filter.productId" id="filter.productId" value="${filter.productId}" />
			</div>	
			<div class="ui-field-contain">	
				<label for="filter.batch">Lote:</label>
				<input type="text" name="filter.batch" id="filter.batch" value="${filter.batch}" />
			</div>	
			<div class="ui-field-contain">	
				<label for="date1">Entrada a partir de:</label>
				<input type="date" name="date1" id="date1" value="${date1}">
				<%-- <input type="text" id="date1" name="date1" value="${date1}"/> --%>
			</div>
			<div class="ui-field-contain">	
				<label for="date1">Entrada até:</label>
				<input type="date" name="date2" id="date2" value="${date2}">
				<%-- <input type="text" id="date2" name="date2" value="${date2}"/> --%>												
			</div>
			<input class="ui-shadow ui-btn ui-corner-all" type="submit" data-inline="true" value="Pesquisar">
		</form>
	</div><!-- /content -->

</div>



<%@ include file="../annex/footer.jsp" %> 