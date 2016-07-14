<%@ include file="../annex/header.jsp" %> 

<div data-role="content" id="personal-contact">
	<div data-role="header">
		<h1>Estoque</h1>
		<a href="<c:url value='/mobile/stocks'/>" data-icon="arrow-l" data-direction="reverse">Voltar</a> 
		<%-- <a href="<c:url value='/'/>" data-icon="home" data-direction="reverse">Home</a> --%>
	</div>
	
	<div role="main" class="ui-content">
		<table data-role="table" id="table-custom-2" data-mode="columntoggle" class="ui-body-d ui-shadow table-stripe ui-responsive" data-column-btn-theme="b" data-column-btn-text="Selecionar colunas" data-column-popup-theme="a">
         <thead>
           <tr class="ui-bar-d">
           	<th>Produto</th>
			<th data-priority="1">Quantidade</th>
			<th data-priority="3">Unidade</th>
			<th data-priority="2">Peças</th>
			<th data-priority="5">Peso (kg)</th>

           </tr>
         </thead>
         <tbody>
           <c:forEach items="${totals}" var="total">
				<tr>
					<td>${total.product}</td>
					<td>${total.secondaryQty}</td>
					<td>${total.product.unit.id}</td>
					<td>${total.primaryQty}</td>
					<td>${total.net}</td>
				</tr>
			</c:forEach>
           
         </tbody>
       </table>
	</div>
</div>


<%@ include file="../annex/footer.jsp" %> 	