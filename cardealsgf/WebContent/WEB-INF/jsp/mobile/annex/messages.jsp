<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:if test="${not empty errors}">
	<div class="row" class="col-md-10 col-md-offset-1">
		<div class="alert alert-warning alert-block alert-dismissable fade in">
			<button class="close" data-dismiss="alert">×</button>
			<c:forEach items="${errors }" var="error">
	                <p>${error.category } - ${error.message }</p>
	        </c:forEach>
		</div>
	</div>		

<%-- 	<div class="ui-widget">		
	<div class="ui-state-error ui-corner-all" style="padding: 0 .7em;margin-top: 20px;"> 
			<p><span class="ui-icon ui-icon-alert" style="float: left; margin-right: .3em;"></span> 		
	            <c:forEach items="${errors }" var="error">
	                <p>${error.category } - ${error.message }</p>
	            </c:forEach>		        
			</p>		
		</div>
	</div>		
	<br/> --%>		
</c:if>

<c:if test="${not empty notice}">
	<div class="row" class="col-md-10 col-md-offset-1">
		<div class="alert alert-warning alert-block alert-dismissable fade in">
			<button class="close" data-dismiss="alert">×</button>
			<p>${notice}</p>	
		</div>
	</div>		

	<%-- <div class="ui-widget">
		<div class="ui-state-highlight ui-corner-all" style="margin-top: 20px; padding: 0 .7em;"> 
			<p><span class="ui-icon ui-icon-info" style="float: left; margin-right: .3em;"></span>
			${notice}</p>
		</div>
	</div>	
	<br/>	 --%>		
</c:if>