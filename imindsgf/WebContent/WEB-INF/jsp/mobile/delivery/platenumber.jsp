<%@ include file="../annex/header.jsp" %> 

<script>
window.onload = function ()
{
	document.getElementById('plate').focus();	
}
</script>

<div class="main">

<p>
<strong>Ilha ${island.id}</strong> 
</p>

<form action="<c:url value="/mobile/delivery/platenumber"/>" name="f1" method="post">
		  <table style="width:200px" border="0" cellspacing="5" cellpadding="1">
			<tr>
				<td>Placa</td>
				<td><input type="text" name="plate" id="plate" 
				     onkeypress="return onlyNumbers(event);"
				     value="${plate}"
				     maxlength="4"
				     />
					
		 		</td>
			</tr>
			<tr><td></td><td><font color="red">Somente números</font></td></tr>
			<tr><td>&nbsp;</td><td>&nbsp;</td></tr>
			<tr>
				<td></td>
				<td>
					<button type="submit" id="submit" 
				    style="height:40px; font-weight:bold; width:150px;">
				    OK</button>
				</td>
			</tr>
			<tr>
				<td></td>
				<td>				    
					<button type="button" 
			        onclick="location.href='<c:url value='/mobile/delivery/lastdelivery'/>'"
			        style="height:40px; font-weight:bold; width:150px;">
			        Voltar</button>				    
				</td>
			</tr>
		  </table>
</form>	
</div>

<%@ include file="../annex/footer.jsp" %> 
