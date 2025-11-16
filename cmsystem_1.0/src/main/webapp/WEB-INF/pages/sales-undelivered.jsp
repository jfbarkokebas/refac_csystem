<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
<jsp:include page="head.jsp"></jsp:include>
<body>
	<div class="main-container">

		<jsp:include page="menu.jsp"></jsp:include>
		
		<div class="content-area" style="justify-content: center">
		
			<div class="action-container" style="width: 90%">
				<span class="main-title">Lista de Vendas Com Entregas Pendentes</span>
				<br>
				<br>
				<div id='count-area'>
					<span> Resultados: </span>
					<span id="salesCount">${count}</span>
				</div>
				<div class="table-wrapper">
					<table id="stock-data">
						<tr>
							<th>Vendido para</th>
							<th>Produto</th>
							<th>Quantidade</th>
							<th>Falta entregar</th>
							<th>Data da Venda</th>
							<th></th>
						</tr>

						<c:if test="${not empty sales}">
							<c:forEach var="sale" items="${sales}">
							
								<tr class="tr">
									<td>${sale.soldTo}</td>
									<td>${sale.product}</td>
									<td>${sale.formatedSaleQuantity}</td>
									<td>${sale.formatedPendency}</td>
									<td>${sale.saleDate}</td>
									<td>
										<a  href="${pageContext.request.contextPath}/sale?action=activeAgain&saleID=${sale.id}&product=${sale.product}">
											Reabrir
										</a>
									</td>
								</tr>
								
							</c:forEach>
						</c:if>

					</table>
				</div>
				<c:if test="${empty sales}">
					<br>
					<h3 id="data-info">Os dados não foram encontrados na sessão.</h3>
				</c:if>
			
			</div>
		
		</div>
		
	</div>
	<script> const SITE_ROOT = "${pageContext.request.contextPath}"; </script>
	<script src="${pageContext.request.contextPath}/scripts/sales-completed.js"></script>
	<script src="${pageContext.request.contextPath}/scripts/menu.js"></script>

</body>
</html>