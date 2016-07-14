<%@ include file="../annex/header.jsp" %> 

<%@ include file="../annex/menu.jsp" %> 
<section class="container"><%@ include file="../annex/messages.jsp" %></section>
    

<!-- Main page container -->
<section class="container" role="main">

	<!-- Grid row -->
	<div class="row">
		<!-- Data block -->
		<article class="col-sm-12">
			<div class="data-block">
				<header>
					<h2>Cadastro de Terminais</h2>
					<ul class="data-header-actions">
						<li><a href="<c:url value="/terminals"/>"> Sair</a></li>
					</ul>
				</header>
				<section>
				<c:if test="${empty terminal}">
					<form class="form-horizontal" action="<c:url value="/terminals/add"/>" method="post" style="margin:0">
				</c:if>
				<c:if test="${not empty terminal}">
					<form class="form-horizontal" action="<c:url value="/terminals/update"/>" method="post" style="margin:0">
				</c:if>
				<div class="row">						
					<div class="col-md-8 col-md-offset-2">
						<h5>IDENTIFICAÇÃO</h5>
						<div class="form-group">
							<label for="terminal.company.id" class="col-sm-3 control-label">Filial</label>
							<div class="col-sm-6">
								<input class="form-control" type="text" maxlength="2" name="terminal.company.id" id="terminal.company.id" value="${terminal.company.id}" <c:if test="${not empty terminal and terminal.id != 0}">readonly</c:if> />
							</div>
						</div>						
						<div class="form-group">
							<label for="terminal.id" class="col-sm-3 control-label">Código</label>
							<div class="col-sm-6">
								<input class="form-control" type="text" maxlength="2" name="terminal.id" id="terminal.id" value="${terminal.id}" <c:if test="${not empty terminal and terminal.id != 0}">readonly</c:if> />
							</div>
						</div>	
						<div class="form-group">
							<label for="terminal.description" class="col-sm-3 control-label">Descrição</label>
							<div class="col-sm-6">
								<input class="form-control" type="text" maxlength="13" name="terminal.description" id="terminal.description"  value="${terminal.description}" />
							</div>
						</div>
						<h5>IMPRESSORA</h5>
						<div class="form-group">
							<label for="terminal.printerModel" class="col-sm-3 control-label">Tipo</label>
							<div class="col-sm-6">
								<select class="form-control" name="terminal.printerModel">
					                <c:forEach items="${printerModels}" var="model">
					                	<c:if test="${terminal.printerModel == model}">
					                        <option value="${model}" selected>${model.name}</option>
					                	</c:if>
					                	<c:if test="${terminal.printerModel != model}">
					                        <option value="${model}">${model.name}</option>
					                	</c:if>
					                </c:forEach>
							 	</select>
							</div>
						</div>
						<div class="form-group">
							<label for="terminal.printerPort" class="col-sm-3 control-label">Porta</label>
							<div class="col-sm-6">
								<input class="form-control" type="text" maxlength="18" name="terminal.printerPort" id="terminal.printerPort"  value="${terminal.printerPort}" />
							</div>
						</div>
						<div class="form-group">
							<label for="terminal.printerName" class="col-sm-3 control-label">Nome Impressora</label>
							<div class="col-sm-6">
								<input class="form-control" type="text" maxlength="40" name="terminal.printerName" id="terminal.printerName"  value="${terminal.printerName}" />
							</div>
						</div>
						<div class="form-group">
							<label for="terminal.printerBaudRate" class="col-sm-3 control-label">Bits por segundo:</label>
							<div class="col-sm-6">
								<select class="form-control" name="terminal.printerBaudRate">
					                <c:forEach items="${baudrate}" var="printerBaudRate">
					                	<c:if test="${terminal.printerBaudRate == printerBaudRate.param}">
					                        <option value="${printerBaudRate}" selected>${printerBaudRate.description}</option>
					                	</c:if>
					                </c:forEach>
							 	</select>
							</div>
						</div>
						<div class="form-group">
							<label for="terminal.printerDataBits" class="col-sm-3 control-label">Bits de dados:</label>
							<div class="col-sm-6">
								<select class="form-control" name="terminal.printerDataBits">
					                <c:forEach items="${databits}" var="printerDataBits">
					                	<c:if test="${terminal.printerDataBits == printerDataBits.param}">
					                        <option value="${printerDataBits}" selected>${printerDataBits.description}</option>
					                	</c:if>
					                </c:forEach>
							 	</select>
							</div>
						</div>
						<div class="form-group">
							<label for="terminal.printerParity" class="col-sm-3 control-label">Paridade:</label>
							<div class="col-sm-6">
								<select class="form-control" name="terminal.printerParity">
					                <c:forEach items="${parity}" var="printerParity">
					                	<c:if test="${terminal.printerParity == printerParity.param}">
					                        <option value="${printerParity}" selected>${printerParity.description}</option>
					                	</c:if>
					                </c:forEach>
							 	</select>
							</div>
						</div>
						<div class="form-group">
							<label for="terminal.printerStopBits" class="col-sm-3 control-label">Bits de parada:</label>
							<div class="col-sm-6">
								<select class="form-control" name="terminal.printerStopBits">
					                <c:forEach items="${stopbits}" var="printerStopBits">
					                	<c:if test="${terminal.printerStopBits == printerStopBits.param}">
					                        <option value="${printerStopBits}" selected>${printerStopBits.description}</option>
					                	</c:if>
					                </c:forEach>
							 	</select>
							</div>
						</div>
						<h5>BALANÇA 1</h5>
						<div class="form-group">
							<label for="terminal.scale1Model" class="col-sm-3 control-label">Tipo</label>
							<div class="col-sm-6">
								<select class="form-control" name="terminal.scale1Model">
					                <c:forEach items="${scaleModels}" var="model">
					                	<c:if test="${terminal.scale1Model == model}">
					                        <option value="${model}" selected>${model.name}</option>
					                	</c:if>
					                	<c:if test="${terminal.scale1Model != model}">
					                        <option value="${model}">${model.name}</option>
					                	</c:if>
					                </c:forEach>
							 	</select>
							</div>
						</div>
						<div class="form-group">
							<label for="terminal.scale1Unit" class="col-sm-3 control-label">Unidade</label>
							<div class="col-sm-6">
								<select class="form-control" name="terminal.scale1Unit">
					                <c:forEach items="${scaleUnits}" var="unit">
					                	<c:if test="${terminal.scale1Unit == unit}">
					                        <option value="${unit}" selected>${unit.name}</option>
					                	</c:if>
					                	<c:if test="${terminal.scale1Unit != unit}">
					                        <option value="${unit}">${unit.name}</option>
					                	</c:if>
					                </c:forEach>
							 	</select>
							</div>
						</div>
						<div class="form-group">
							<label for="terminal.scale1Port" class="col-sm-3 control-label">Porta</label>
							<div class="col-sm-6">
								<input class="form-control" type="text" maxlength="18" name="terminal.scale1Port" id="terminal.scale1Port"  value="${terminal.scale1Port}" />
							</div>
						</div>
						<div class="form-group">
							<label for="terminal.scale1BaudRate" class="col-sm-3 control-label">Bits por segundo:</label>
							<div class="col-sm-6">
								<select class="form-control" name="terminal.scale1BaudRate">
					                <c:forEach items="${baudrate}" var="scale1BaudRate">
					                	<c:if test="${terminal.scale1BaudRate == scale1BaudRate.param}">
					                        <option value="${scale1BaudRate}" selected>${scale1BaudRate.description}</option>
					                	</c:if>
					                </c:forEach>
							 	</select>
							</div>
						</div>
						<div class="form-group">
							<label for="terminal.scale1DataBits" class="col-sm-3 control-label">Bits de dados:</label>
							<div class="col-sm-6">
								<select class="form-control" name="terminal.scale1DataBits">
					                <c:forEach items="${databits}" var="scale1DataBits">
					                	<c:if test="${terminal.scale1DataBits == scale1DataBits.param}">
					                        <option value="${scale1DataBits}" selected>${scale1DataBits.description}</option>
					                	</c:if>
					                </c:forEach>
							 	</select>
							</div>
						</div>
						<div class="form-group">
							<label for="terminal.scale1Parity" class="col-sm-3 control-label">Paridade:</label>
							<div class="col-sm-6">
								<select class="form-control" name="terminal.scale1Parity">
					                <c:forEach items="${parity}" var="scale1Parity">
					                	<c:if test="${terminal.scale1Parity == scale1Parity.param}">
					                        <option value="${scale1Parity}" selected>${scale1Parity.description}</option>
					                	</c:if>
					                </c:forEach>
							 	</select>
							</div>
						</div>
						<div class="form-group">
							<label for="terminal.scale1StopBits" class="col-sm-3 control-label">Bits de parada:</label>
							<div class="col-sm-6">
								<select class="form-control" name="terminal.scale1StopBits">
					                <c:forEach items="${stopbits}" var="scale1StopBits">
					                	<c:if test="${terminal.scale1StopBits == scale1StopBits.param}">
					                        <option value="${scale1StopBits}" selected>${scale1StopBits.description}</option>
					                	</c:if>
					                </c:forEach>
							 	</select>
							</div>
						</div>
						<div class="form-group">
							<label for="terminal.timeWait1" class="col-sm-3 control-label">Tempo para estabilizar peso (segundos)</label>
							<div class="col-sm-6">
								<input class="form-control" type="text" maxlength="18" name="terminal.timeWait1" id="terminal.timeWait1"  value="${terminal.timeWait1}" />
							</div>
						</div>
						<h5>BALANÇA 2</h5>
						<div class="form-group">
							<label for="terminal.scale2Model" class="col-sm-3 control-label">Tipo</label>
							<div class="col-sm-6">
								<select class="form-control" name="terminal.scale2Model">
					                <c:forEach items="${scaleModels}" var="model">
					                	<c:if test="${terminal.scale2Model == model}">
					                        <option value="${model}" selected>${model.name}</option>
					                	</c:if>
					                	<c:if test="${terminal.scale2Model != model}">
					                        <option value="${model}">${model.name}</option>
					                	</c:if>
					                </c:forEach>
							 	</select>
							</div>
						</div>
						<div class="form-group">
							<label for="terminal.scale2Unit" class="col-sm-3 control-label">Unidade</label>
							<div class="col-sm-6">
								<select class="form-control" name="terminal.scale2Unit">
					                <c:forEach items="${scaleUnits}" var="unit">
					                	<c:if test="${terminal.scale2Unit == unit}">
					                        <option value="${unit}" selected>${unit.name}</option>
					                	</c:if>
					                	<c:if test="${terminal.scale2Unit != unit}">
					                        <option value="${unit}">${unit.name}</option>
					                	</c:if>
					                </c:forEach>
							 	</select>
							</div>
						</div>
						<div class="form-group">
							<label for="terminal.scale2Port" class="col-sm-3 control-label">Porta</label>
							<div class="col-sm-6">
								<input class="form-control" type="text" maxlength="18" name="terminal.scale2Port" id="terminal.scale2Port"  value="${terminal.scale2Port}" />
							</div>
						</div>
						<div class="form-group">
							<label for="terminal.scale2BaudRate" class="col-sm-3 control-label">Bits por segundo:</label>
							<div class="col-sm-6">
								<select class="form-control" name="terminal.scale2BaudRate">
					                <c:forEach items="${baudrate}" var="scale2BaudRate">
					                	<c:if test="${terminal.scale2BaudRate == scale2BaudRate.param}">
					                        <option value="${scale2BaudRate}" selected>${scale2BaudRate.description}</option>
					                	</c:if>
					                </c:forEach>
							 	</select>
							</div>
						</div>
						<div class="form-group">
							<label for="terminal.scale2DataBits" class="col-sm-3 control-label">Bits de dados:</label>
							<div class="col-sm-6">
								<select class="form-control" name="terminal.scale2DataBits">
					                <c:forEach items="${databits}" var="scale2DataBits">
					                	<c:if test="${terminal.scale2DataBits == scale2DataBits.param}">
					                        <option value="${scale2DataBits}" selected>${scale2DataBits.description}</option>
					                	</c:if>
					                </c:forEach>
							 	</select>
							</div>
						</div>
						<div class="form-group">
							<label for="terminal.scale2Parity" class="col-sm-3 control-label">Paridade:</label>
							<div class="col-sm-6">
								<select class="form-control" name="terminal.scale2Parity">
					                <c:forEach items="${parity}" var="scale2Parity">
					                	<c:if test="${terminal.scale2Parity == scale2Parity.param}">
					                        <option value="${scale2Parity}" selected>${scale2Parity.description}</option>
					                	</c:if>
					                </c:forEach>
							 	</select>
							</div>
						</div>
						<div class="form-group">
							<label for="terminal.scale2StopBits" class="col-sm-3 control-label">Bits de parada:</label>
							<div class="col-sm-6">
								<select class="form-control" name="terminal.scale2StopBits">
					                <c:forEach items="${stopbits}" var="scale2StopBits">
					                	<c:if test="${terminal.scale2StopBits == scale2StopBits.param}">
					                        <option value="${scale2StopBits}" selected>${scale2StopBits.description}</option>
					                	</c:if>
					                </c:forEach>
							 	</select>
							</div>
						</div>
						<div class="form-group">
							<label for="terminal.timeWait2" class="col-sm-3 control-label">Tempo para estabilizar peso (segundos)</label>
							<div class="col-sm-6">
								<input class="form-control" type="text" maxlength="18" name="terminal.timeWait2" id="terminal.timeWait2"  value="${terminal.timeWait2}" />
							</div>
						</div>
						
						<h5>OUTROS DADOS</h5>
						<div class="form-group">
							<label for="terminal.tempDirectory" class="col-sm-3 control-label">Diretório Temporario Local</label>
							<div class="col-sm-6">
								<input class="form-control" type="text" maxlength="255" name="terminal.tempDirectory" id="terminal.tempDirectory"  value="${terminal.tempDirectory}" />
							</div>	
						</div>							
					</div>
				</div>			
				<div class="row">
					<div class="col-md-8 col-md-offset-2">
						<button type="submit" class="btn btn-md btn-inverse pull-right" id="submit">Salvar</button>
						<button type="button" class="btn btn-md btn-default pull-left" onClick="window.location.href='<c:url value="/terminals"/>';return false;">Cancelar</button>					
					</div>
				</div>
				</form>	
				</section>
				<footer></footer>
			</div>
		</article>
	</div>
</section>				

<%-- <div class="ui">

<h2></h2>

<p>



<c:if test="${empty terminal}">
  <form action="<c:url value="/terminals/add"/>" method="post" style="margin:0">
</c:if>
<c:if test="${not empty terminal}">
  <form action="<c:url value="/terminals/update"/>" method="post" style="margin:0">
</c:if>

  <table width="100%" border="0" cellpadding="0" cellspacing="4" class="tabela_borda">    
	<tr>
		<td class="form-subtitle">IDENTIFICAÇÃO</td>
		<td class="form-subtitle"></td>
	</tr>
    <tr>
	  <td width="40%"><label for="terminal.id"><strong>Código</strong></label></td>
	  <td><input type="text" name="terminal.id" id="terminal.id" value="${terminal.id}" <c:if test="${not empty terminal and terminal.id != 0}">readonly</c:if> /></td>
    </tr>
	<tr>
		<td><label for="terminal.description"><strong>Descrição</strong></label></td>
		<td><input type="text" maxlength="13" name="terminal.description" id="terminal.description"  value="${terminal.description}" /></td>
	</tr>

	
	<tr>
		<td class="form-subtitle">IMPRESSORA</td>
		<td class="form-subtitle"></td>
	</tr>
	<tr>
		<td><label for="terminal.printerModel"><strong>Tipo</strong></label></td>
		<td>
		 	<select name="terminal.printerModel" style="width:202px">
                <c:forEach items="${printerModels}" var="model">
                	<c:if test="${terminal.printerModel == model}">
                        <option value="${model}" selected>${model.name}</option>
                	</c:if>
                	<c:if test="${terminal.printerModel != model}">
                        <option value="${model}">${model.name}</option>
                	</c:if>
                </c:forEach>
		 	</select>
	 	</td>	
	</tr>
	
	<tr>
		<td><label for="terminal.printerPort"><strong>Porta</strong></label></td>
		<td><input type="text" maxlength="18" name="terminal.printerPort" id="terminal.printerPort"  value="${terminal.printerPort}" /></td>
	</tr>
	
	
	<tr>
		<td><label for="terminal.printerName"><strong>Nome Windows</strong></label></td>
		<td><input type="text" maxlength="40" name="terminal.printerName" id="terminal.printerName"  value="${terminal.printerName}" /></td>
	</tr>
	
	<tr>
		<td class="form-subtitle">BALANÇA 1</td>
		<td class="form-subtitle"></td>
	</tr>
	<tr>
		<td><label for="terminal.scale1Model"><strong>Tipo</strong></label></td>
		<td>
		 	<select name="terminal.scale1Model" style="width:202px">
                <c:forEach items="${scaleModels}" var="model">
                	<c:if test="${terminal.scale1Model == model}">
                        <option value="${model}" selected>${model.name}</option>
                	</c:if>
                	<c:if test="${terminal.scale1Model != model}">
                        <option value="${model}">${model.name}</option>
                	</c:if>
                </c:forEach>
		 	</select>
	 	</td>	
	</tr>
	<tr>
		<td><label for="terminal.scale1Unit"><strong>Unidade</strong></label></td>
		<td>
		 	<select name="terminal.scale1Unit" style="width:202px">
                <c:forEach items="${scaleUnits}" var="unit">
                	<c:if test="${terminal.scale1Unit == unit}">
                        <option value="${unit}" selected>${unit.name}</option>
                	</c:if>
                	<c:if test="${terminal.scale1Unit != unit}">
                        <option value="${unit}">${unit.name}</option>
                	</c:if>
                </c:forEach>
		 	</select>
	 	</td>	
	</tr>
	
	<tr>
		<td><label for="terminal.scale1Port"><strong>Porta</strong></label></td>
		<td><input type="text" maxlength="18" name="terminal.scale1Port" id="terminal.scale1Port"  value="${terminal.scale1Port}" /></td>
	</tr>


	<tr>
		<td class="form-subtitle">BALANÇA 2</td>
		<td class="form-subtitle"></td>
	</tr>
	<tr>
		<td><label for="terminal.scale2Model"><strong>Tipo</strong></label></td>
		<td>
		 	<select name="terminal.scale2Model" style="width:202px">
                <c:forEach items="${scaleModels}" var="model">
                	<c:if test="${terminal.scale2Model == model}">
                        <option value="${model}" selected>${model.name}</option>
                	</c:if>
                	<c:if test="${terminal.scale2Model != model}">
                        <option value="${model}">${model.name}</option>
                	</c:if>
                </c:forEach>
		 	</select>
	 	</td>	
	</tr>
	<tr>
		<td><label for="terminal.scale2Unit"><strong>Unidade</strong></label></td>
		<td>
		 	<select name="terminal.scale2Unit" style="width:202px">
                <c:forEach items="${scaleUnits}" var="unit">
                	<c:if test="${terminal.scale2Unit == unit}">
                        <option value="${unit}" selected>${unit.name}</option>
                	</c:if>
                	<c:if test="${terminal.scale2Unit != unit}">
                        <option value="${unit}">${unit.name}</option>
                	</c:if>
                </c:forEach>
		 	</select>
	 	</td>	
	</tr>
	
	<tr>
		<td><label for="terminal.scale2Port"><strong>Porta</strong></label></td>
		<td><input type="text" maxlength="18" name="terminal.scale2Port" id="terminal.scale2Port"  value="${terminal.scale2Port}" /></td>
	</tr>


	<tr>
		<td class="form-subtitle">SCANNER</td>
		<td class="form-subtitle"></td>
	</tr>

	
	<tr>
		<td><label for="terminal.scannerPort"><strong>Porta</strong></label></td>
		<td><input type="text" maxlength="18" name="terminal.scannerPort" id="terminal.scannerPort"  value="${terminal.scannerPort}" /></td>
	</tr>
    

	<tr>
		<td class="form-subtitle">OUTROS DADOS</td>
		<td class="form-subtitle"></td>
	</tr>
	<tr>
		<td><label for="terminal.tempDirectory"><strong>Diretório Temporário Local</strong></label></td>
		<td><input type="text" maxlength="255" name="terminal.tempDirectory" id="terminal.tempDirectory"  value="${terminal.tempDirectory}" /></td>
	</tr>
	

	<tr><td>&nbsp;</td><td>&nbsp;</td></tr>
		
	
	<tr>
		<td></td>
		<td>
			<button type="submit" id="submit">Salvar</button>
			<button type="button" onClick="window.location.href='<c:url value="/terminals"/>';return false;">Cancelar</button>					
		</td>
	</tr>
  </table>
</form>
</p>

</div> --%>

<%@ include file="../annex/footer.jsp" %> 
