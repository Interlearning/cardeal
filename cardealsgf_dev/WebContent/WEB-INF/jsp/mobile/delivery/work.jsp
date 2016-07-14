<%@ include file="../annex/header.jsp" %> 

<script>
window.onload = function ()
{
	document.getElementById('buttonok').focus();	
}
</script>

<div class="main">
<strong><font color="green">Abastecimento em andamento</font></strong>
<form action="<c:url value="/mobile/delivery/initconfirmed"/>" name="f1" method="post">
		  <table style="width:100px" border="0" cellspacing="5" cellpadding="1">
			<tr>
				<td>Veículo</td>
				<td><input type="text" name="vehicle.plate" id="vehicle.plate" 
				     value="${vehicle.plate}"
				     readonly
				     />
					
		 		</td>
			</tr>
			<tr>
				<td>Empresa</td>
				<td><input type="text" name="company" id="company" 
				     value="${company.name}"
				     readonly
				     />
					
		 		</td>
			</tr>
			<tr>
				<td>Compart.</td>
				<td><input type="text" name="chamber" id="chamber" 
				     value="${chamber}"
				     readonly
				     />
					
		 		</td>
			</tr>
			<tr>
				<td>Setpoint</td>
				<td><input type="text" name="setpoint" id="setpoint" 
				     value="${setpoint}"
				     readonly
				     />
					
		 		</td>
			</tr>
			<tr>
				<td></td>
				<td>
					<button type="submit" id="submit" 
				    style="height:40px; font-weight:bold; width:150px;"
				    id="buttonok">
				    Sim</button>
				</td>
			</tr>
			<tr>
				<td></td>
				<td>				    
					<button type="button" 
			        onclick="location.href='<c:url value='/mobile/delivery/inputsetpoint'/>'"
			        style="height:40px; font-weight:bold; width:150px;">
			        Não</button>				    
				</td>
			</tr>
		  </table>
</form>	
</div>

<%@ include file="../annex/footer.jsp" %> 
