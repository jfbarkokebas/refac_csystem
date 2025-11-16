<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<jsp:include page="head.jsp"></jsp:include>
<body>
	<div class="main-container">

		<jsp:include page="menu.jsp"></jsp:include>

		<div class="content-area" style="justify-content: center;">

			<div class="action-container" style="width: 90%">
				<form id="stock-form"
					action="${pageContext.request.contextPath}/stockinfo"
					method="get">
					<input type="hidden" name="action" value="stock">
				</form>
				<br> 
				<span class="main-title">Lista de Compras 
				</span>
				<div id="filter">
					<form id="formSearchPurchaseByName" method="get"
						action="${pageContext.request.contextPath}/purchase">
						<div id="filter-name-box">
							<button id="search-by-name" class="btn-3">Nome</button>
							<input type="text" name="search" placeholder="Buscar por nome">
							<input type="hidden" name="action" value="searchByName">
						</div>
					</form>
					<form id="formSearchPurchase" action="${pageContext.request.contextPath}/purchase"
						method="get">
					</form>
					<button id="coffeeBtn" class="btn-3">Café</button>
					<button id="pepperBtn" class="btn-3">Pimenta</button>
					<button id="purchase-debit" class="btn-3" 
							style="background-color: #323232; width: auto; padding: 0 .7rem;">
						Falta Receber
					</button>
				</div>
				<div id='count-area'>
					<span> Resultados: </span>
					<span id="salesCount">${data.countRows}</span>
				</div>
				<div class="table-wrapper">
					<table id="stock-data">
						<tr>
							<th>Fornecedor</th>
							<th>Produto</th>
							<th>Quantidade</th>
							<th>Total Compra</th>
							<th style="width: 35px"></th>
							<th>Valor (unid)</th>
							<th>Débito (qtde)</th>
							<th>Data da Compra</th>
							<th></th>
						</tr>

						<c:if test="${not empty data.purchaseList}">
							<c:forEach var="purchase" items="${data.purchaseList}">
								<tr class="tr">
									<td>${purchase.clientName}</td>
									<td>${purchase.productName}</td>
									<td>${purchase.formatedQuantityPurchased}</td>
									<td> ${purchase.formatedTotalPurchase}</td>
									<td>
										<c:choose>
										    <c:when test="${not purchase.isPaymentPaidOff}">
										        <a href="${pageContext.request.contextPath}/payments?action=startPayment&purchaseID=${purchase.id}&clientName=${purchase.clientName}">
										            	<img style="margin-left: 1.2rem" src="${pageContext.request.contextPath}/assets/dívida-45.png" width="30">
											</a>
										    </c:when>
										    <c:otherwise>
										     <a href="${pageContext.request.contextPath}/payments?action=startPayment&purchaseID=${purchase.id}&clientName=${purchase.clientName}">
										        <img style="margin-left: 1.2rem" src="${pageContext.request.contextPath}/assets/dinheiro-60.png" width="30">
											</a>
										    </c:otherwise>
										</c:choose>
									</td>
									<td>
										${purchase.formatedPricePerUnit}
									</td>
									<td> 
										${purchase.formatedDebit}
										<c:if test="${not purchase.isPaidOff}">
											<img width="22" src="${pageContext.request.contextPath}/assets/not-ok.png">
										</c:if>
									</td>
									<td>${purchase.formatedCreatedAt}</td>
									<td style="text-align: center;">
										<a href="${pageContext.request.contextPath}/purchase?action=delete&purchaseId=${purchase.id}">
											 <img style="width: 30px" src="${pageContext.request.contextPath}/assets/lixo-45.png">
										</a>
									</td>
								</tr>
							</c:forEach>
						</c:if>

					</table>
				</div>
				<c:if test="${empty data.purchaseList}">
					<br>
					<h3 id="data-info">Os dados não foram encontrados na sessão.</h3>
				</c:if>
			</div>

		</div>
	</div>
	<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
		<script>const SITE_ROOT = "${pageContext.request.contextPath}";</script>
		<script src="${pageContext.request.contextPath}/scripts/purchase-list.js"></script>
		<script src="${pageContext.request.contextPath}/scripts/menu.js"></script>

</body>
</html>