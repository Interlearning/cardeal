<%@ include file="../annex/header.jsp" %> 

<script>
window.onload = function ()
{
}
</script>

<div class="main">
	<p>
	<strong>Ilha ${island.id}</strong> 
	</p>
	
	<p>
		<strong>Último Abastecimento</strong><br>
		<c:if test="${empty delivery}">
		Nenhum Abastecimento
		</c:if>
		<c:if test="${not empty delivery}">
			<font color="red">${delivery.vehicle.plate}</font><br>
			<font color="green">${delivery.volume}</font><br>
		</c:if>
	</p>
	
	<br>
	
	<table style="width:100px" border="0" cellspacing="5" cellpadding="1">
		<tr>
			<td></td>
			<td>
				<button type="submit" id="submit" onclick="location.href='<c:url value='/mobile/delivery/platenumber'/>'"
				  style="height:40px; font-weight:bold; width:150px">
				  Iniciar Abastecimento</button>
			</td>
		</tr>
		<tr>
			<td></td>
			<td>				    
		<button type="button" onclick="location.href='<c:url value='/mobile/delivery'/>'"
		  style="height:40px; font-weight:bold; width:150px">
	      Voltar</button>
			</td>
		</tr>
	</table>		
</div>

<%@ include file="../annex/footer.jsp" %> 
