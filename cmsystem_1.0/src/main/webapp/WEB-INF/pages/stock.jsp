<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html>
<jsp:include page="head.jsp"></jsp:include>
<body>
	<div class="main-container">

		<jsp:include page="menu.jsp"></jsp:include>

		<div class="content-area" style="justify-content: center;">

			<div class="action-container">
				<form id="stock-form"
					action="${pageContext.request.contextPath}/stockinfo"
					method="get">
					<input type="hidden" name="action" value="stock">
				</form>
				<div class="stock-information" id="stock-area">
					<div id="stock-title">
						<img id="reload-img" src="${pageContext.request.contextPath}/assets/reload.png">
						<span>Estoque atualizado em: <span
							style="margin-left: 1rem" id="updated-on">${dataStock.stockUpdateDate}</span>
						</span>
					</div>
					<div class="stock" id="stock-p1">
						<span>Café = </span> <span id="p1-value">${dataStock.cafe}</span>
						<span id="p1-measure">Sacas</span>

					</div>
					<div class="stock" id="stock-p2">
						<span>Pimenta =</span> <span id="p2-value">${dataStock.pimenta}</span>
						<span id="p2-measure">Kg</span>
					</div>
				</div>
				<br> <span class="main-title">Estoque
				</span>
				<div id="filter">
					<form action="">
						<div id="filter-name-box">
							<button class="btn-3">Nome</button>
							<input type="text" name="search" placeholder="Buscar por nome">
							<input type="hidden" name="action" value="searchByName">
						</div>
					</form>
					<form id="formSearchProduct" action="${pageContext.request.contextPath}/stockinfo"
						method="get">
						<input type="hidden" name="action" value="searchProduct">
					</form>
					<button id="cofeBtn" class="btn-3">Café</button>
					<button id="pepperBtn" class="btn-3">Pimenta</button>
					<button id="stockOwnerBtn" class="btn-3" 
							style="background-color: #323232; width: auto; padding: 0 .7rem;">
						Meu Estoque
					</button>
					 <div class="btn-3" style="width: auto; padding: 2px; background-color: rgb(150, 150, 150);" id="onlyCoffee">
                        <img src="${pageContext.request.contextPath}/assets/coffee-48.png " width="30px">
                    </div>
                    <div class="btn-3" style="width: auto; padding: 2px; background-color: rgb(161, 161, 161);" id="onlyPepper">
                        <img src="${pageContext.request.contextPath}/assets/pepper-48.png" width="30px">
                    </div>
				</div>
				<div id='count-area'>
					<span> Resultados: </span>
					<span id="salesCount">${dataStock.count}</span>
				</div>
				<div class="table-wrapper">
					<table id="stock-data">
						<tr>
							<th>Cliente</th>
							<th>Produto</th>
							<th>Quantidade</th>
							<th>Unidade</th>
							<th id="th-btn"></th>
						</tr>

						<c:if test="${not empty dataStock.tableData}">
							<c:forEach var="stock" items="${dataStock.tableData}">
								<tr class="tr">
									<td>${stock.name}</td>
									<td>${stock.product}</td>
									<td>${stock.formatedQuantity}</td>
									<td>${stock.unit}</td>
									<td style="text-align: center;">
									
										 <fmt:parseNumber var="clientIdNum" value="${stock.clientID}" integerOnly="true" />

							            <c:choose>
							                <c:when test="${clientIdNum > 1}">
							                    <a class="btn-2"
							                       href="${pageContext.request.contextPath}/stockinfo?action=detail&stockId=${stock.id}&clientID=${stock.clientID}">
							                       Comprar
							                    </a>
							                </c:when>
							                <c:otherwise>
							                    —
							                </c:otherwise>
							            </c:choose>
									</td>
								</tr>
							</c:forEach>
						</c:if>

					</table>
				</div>
				<c:if test="${empty dataStock.tableData}">
					<br>
					<h3 id="data-info">Os dados não foram encontrados na sessão.</h3>
				</c:if>
			</div>

		</div>
		

	</div>
	<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
		<script>const SITE_ROOT = "${pageContext.request.contextPath}";</script>
		<script src="${pageContext.request.contextPath}/scripts/stock.js"></script>
		<script src="${pageContext.request.contextPath}/scripts/menu.js"></script>
<body>

</body>
</html>