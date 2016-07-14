<%@ include file="../annex/header.jsp" %> 

<script>
window.onload = function ()
{
	document.getElementById('islandId').focus();	
}
</script>

<div class="main">
<form action="<c:url value="/mobile/delivery/select"/>" name="f1" method="post">
		  <table style="width:100px" border="0" cellspacing="5" cellpadding="1">
			<tr>
				<td>Ilha</td>
				<td>
					<select name="islandId" style="width:150px">
		                <c:forEach items="${islands}" var="islandx">
		                	<c:if test="${islandx.id == island.id}">
		                        <option value="${islandx.id}" selected>${islandx.id}</option>
		                	</c:if>
		                	<c:if test="${islandx.id != island.id}">
		                        <option value="${islandx.id}">${islandx.id}</option>
		                	</c:if>
		                </c:forEach>
				 	</select>
		 		</td>
			</tr>
			<tr>
				<td></td>
				<td>
					<button type="submit" id="submit" 
				    style="height:40px; font-weight:bold; width:150px;">
				    Selecionar</button>
				</td>
			</tr>
			<tr>
				<td></td>
				<td>				    
					<button type="button" 
			        onclick="location.href='<c:url value='/mobile'/>'"
			        style="height:40px; font-weight:bold; width:150px;">
			        Voltar</button>				    
				</td>
			</tr>
		  </table>
</form>	
</div>

<%@ include file="../annex/footer.jsp" %> 
