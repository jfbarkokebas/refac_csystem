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
				<span class="main-title">Lista de Vendas Concluídas</span>
				<br>
				<br>
				<div id="filter">
				
					<form id="filter-entries-by-name" action="${pageContext.request.contextPath}/TransactionsServlet">
						<input type="hidden" name="action" value="searchByName">
					</form>
					
					<div id="filter-name-box">
						<button id="sales-by-name" class="btn-3">Nome</button>
						<input id="soldToName" type="text" name="s" placeholder="Buscar por nome">
					</div>
					<form id="form-concluded-sales" action="${pageContext.request.contextPath}/sale" method="get">
						<input type="hidden" name="action" value="salesByProduct">
					</form>
					<button id="coffeeBtn" class="btn-3">Café</button>
					<button id="pepperBtn" class="btn-3">Pimenta</button>
				</div>
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
							<th>Custo (Unid)</th>
							<th>Valor Total</th>
							<th>Lucro</th>
							<th>Data da Venda</th>
							<th></th>
						</tr>

						<c:if test="${not empty sales}">
							<c:forEach var="sale" items="${sales}">
							
								<tr class="tr">
									<td>${sale.soldTo}</td>
									<td>${sale.product}</td>
									<td>${sale.formatedSaleQuantity}</td>
									<td>${sale.costSale}</td>
									<td>${sale.totalSale}</td>
									<td>${sale.formatedProfit}</td>
									<td>${sale.saleDate}</td>
									<td>
										<a href="${pageContext.request.contextPath}/saledetail?saleID=${sale.id}"> 
											<img src="${pageContext.request.contextPath}/assets/info-50.png" 
												style="width: 35px;">
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
<body>

</body>
</html>