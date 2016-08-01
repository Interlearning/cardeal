<%@ include file="../annex/header.jsp" %> 

<div id="login">
<!-- Main page container -->
		<section class="container" role="main">
			<div class="row">
				<%@ include file="../annex/messages.jsp" %>
			</div>
			
			<div class="row">
			  <div class="col-md-6">
				<!-- Login header -->
				
			<!-- /Login header -->
			  </div>
			  <div class="col-md-6">
				<!-- Login form -->
				<form method="post" action="<c:url value="/home/login"/>">
					<div class="form-group">
						<div class="input-group">
							<span class="input-group-addon"><span class="elusive icon-user"></span></span>
							<input class="form-control" type="text" placeholder="Digite seu login" name="login">
						</div>
					</div>
					<div class="form-group">
						<div class="input-group">
							<span class="input-group-addon"><span class="elusive icon-key"></span></span>
							<input class="form-control" type="password" placeholder="Digite sua senha" name="password" id="password">
						</div>
					</div>
					<button class="btn btn-primary btn-lg btn-block" type="submit">Login</button>
					<!-- <a class="lost-password" href="<c:url value="/home/lostPassword"/>">Esqueceu sua senha?</a>  -->
				</form>
				<!-- /Login form -->
			  </div>
			</div>
			<br>
			
			<!-- 
			<div class="row">
			  <div class="col-md-10 col-md-offset-1">
				<p style="background-color: #fff; text-align: center; border: 1px solid #fff; -webkit-border-radius: 4px; -moz-border-radius: 4px; border-radius: 4px;">
					Informe o Login/Senha para ter acesso ao sistema
				</p>
			  </div>
			</div>
			 -->
			 
			<div class="row">
			  <div class="col-md-4  col-md-offset-6">
				<p style="background-color: #fff; text-align: center; border: 1px solid #fff; -webkit-border-radius: 4px; -moz-border-radius: 4px; border-radius: 4px;">
					Versão 1.0
				</p>
			  </div>
			</div>  
			
		</section>
</div>		


<%@ include file="../annex/footer.jsp" %> 
