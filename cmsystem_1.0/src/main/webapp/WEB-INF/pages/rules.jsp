<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<jsp:include page="head.jsp"></jsp:include>
<body>
	<div class="main-container">

		<jsp:include page="menu.jsp"></jsp:include>
		
		<div class="content-area" style="justify-content: center; align-items: center; flex-direction: column;">
		
			<h1 style="font-size: 5rem;">${ruleTitle}</h1>
			<p style="font-size: 2rem;">${ruleMsg}</p>
		
		</div>
		
	</div>
	<script> const SITE_ROOT = "${pageContext.request.contextPath}"; </script>
		<script src="${pageContext.request.contextPath}/scripts/page.js"></script>
		<script src="${pageContext.request.contextPath}/scripts/menu.js"></script>

</body>
</html>