<%@ include file="../annex/header.jsp" %> 

<script>
window.onload = function ()
{
	document.getElementById('plate').focus();	
}
</script>

<div class="main">
<form action="<c:url value="/mobile/delivery/plateselected"/>" name="f1" method="post">
		  <table style="width:100px" border="0" cellspacing="5" cellpadding="1">
			<tr>
				<td>Veículo</td>
				<td>
					<select name="plate" style="width:150px">
		                <c:forEach items="${vehicles}" var="vehicle">
		                	<c:if test="${vehicle.plate == plate}">
		                        <option value="${vehicle.plate}" selected>${vehicle.plate}</option>
		                	</c:if>
		                	<c:if test="${vehicle.plate != plate}">
		                        <option value="${vehicle.plate}">${vehicle.plate}</option>
		                	</c:if>
		                </c:forEach>
				 	</select>
		 		</td>
			</tr>
			<tr>
				<td></td>
				<td>
					<button type="submit" id="submit" 
				    style="height:40px; font-weight:bold; width:150px;"
				    <c:if test="${empty vehicles}">disabled</c:if>
				    >
				    Selecionar</button>
				</td>
			</tr>
			<tr>
				<td></td>
				<td>				    
					<button type="button" 
			        onclick="location.href='<c:url value='/mobile/delivery/platenumber'/>'"
			        style="height:40px; font-weight:bold; width:150px;">
			        Voltar</button>				    
				</td>
			</tr>
		  </table>
</form>	
</div>

<%@ include file="../annex/footer.jsp" %> 
