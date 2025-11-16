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
				<span class="main-title">Lista de Vendas Abertas</span>
				<br>
				<br>
				<div id='count-area'>
					<span> Resultados: </span>
					<span id="salesCount">${count}</span>
				</div>
				<div class="table-wrapper">
					<table id="stock-data">
						<tr>
							<th></th>
							<th>Vendido para</th>
							<th>Produto</th>
							<th>Quantidade</th>
							<th>Pendência</th>
							<th>Preço (unid)</th>
							<th>Valor Total</th>
							<th>Data da Venda</th>
							<th></th>
							<th></th>
							<th></th>

						</tr>

						<c:if test="${not empty sales}">
							<c:forEach var="sale" items="${sales}">
								<tr class="tr">
									<td>
										<a onclick="return confirmDelete()" 
										  href="${pageContext.request.contextPath}/sale?action=delete&saleID=${sale.id}">
											<img src="${pageContext.request.contextPath}/assets/lixo-45.png" 
												style="width: 40px;">
										</a>
									</td>
									<td>${sale.soldTo}</td>
									<td>${sale.product}</td>
									<td>${sale.saleQuantity}</td>
									<td>${sale.formatedPendency}</td>
									<td>${sale.formatedPricePerUnitSold}</td>
									<td>${sale.totalSale}</td>
									<td>${sale.saleDate}</td>
									<td style="text-align: center;"> 
									
										<a onclick="return confirmDelivery()"
										 href="${pageContext.request.contextPath}/sale?action=pendingDelivery&saleID=${sale.id}">
											 <img src="${pageContext.request.contextPath}/assets/delivery-50.png" style="width: 40px;">
										 </a>
										
									</td>
									<td style="text-align: center;"> 
										<a  style="font-weight: 600"
											 href="${pageContext.request.contextPath}/sale?action=detail&saleID=${sale.id}">
											 Lista
										 </a>
									</td>
									<td style="text-align: center;"> 
										<a class="btn-2" style="background-color: #00bdffa9;"
											 href="${pageContext.request.contextPath}/sale?action=update&saleID=${sale.id}">
											 Editar
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
				
				<c:if test="${not empty dataMap}">
					<div id ="pepper-dialog" class="info-wrapper">
					<h2 style="margin-left: 0.5rem;">Lista de compras da venda para ${dataMap.clientName}</h2>
						<table id="sale-detail">
							<tr >
								<th>Fornecedor</th>
								<th>Quantidade</th>
								<th>Preço p/ unidade</th>
								<th>Data</th>
							</tr>
								<c:forEach var="purchase" items="${dataMap.purchaseList}">
									<tr class="tr">
										<td>${purchase.clientName}</td>
										<td>${purchase.formatedQuantity}</td>
										<td>${purchase.formatedCostPerUnit}</td>
										<td>${purchase.formatedCreatedAt}</td>
									</tr>
								</c:forEach>
						
						</table>
					</div>
				</c:if>
			
			</div>
		
		</div>
		
	</div>
	<script> const SITE_ROOT = "${pageContext.request.contextPath}"; </script>
	<script src="${pageContext.request.contextPath}/scripts/sale-list.js"></script>
	<script src="${pageContext.request.contextPath}/scripts/menu.js"></script>

</body>
</html>