<%@ include file="../annex/header.jsp" %> 

<script>
window.onload = function ()
{
	document.getElementById('chamber').focus();	
}
</script>

<div class="main">
<form action="<c:url value="/mobile/delivery/chamberselected"/>" name="f1" method="post">
		  <table style="width:100px" border="0" cellspacing="5" cellpadding="1">
			<tr>
				<td>Ve�culo</td>
				<td><input type="text" name="vehicle.plate" id="vehicle.plate" 
				     value="${vehicle.plate}"
				     readonly
				     />
					
		 		</td>
			</tr>
			<tr>
				<td>Comp.</td>
				<td>
					<select name="chamber" style="width:150px">
		                <c:forEach items="${chambers}" var="chamberx">
		                        <option value="${chamberx.id}">${chamberx}</option>
		                </c:forEach>
				 	</select>
		 		</td>
			</tr>
			<tr>
				<td></td>
				<td>
					<button type="submit" id="submit" 
				    style="height:40px; font-weight:bold; width:150px;"
				    >
				    Selecionar</button>
				</td>
			</tr>
			<tr>
				<td></td>
				<td>				    
					<button type="button" 
			        onclick="location.href='<c:url value='/mobile/delivery/selectcompany'/>'"
			        style="height:40px; font-weight:bold; width:150px;">
			        Voltar</button>				    
				</td>
			</tr>
		  </table>
</form>	
</div>

<%@ include file="../annex/footer.jsp" %> 
