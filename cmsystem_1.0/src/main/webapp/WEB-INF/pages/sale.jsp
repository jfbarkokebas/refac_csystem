<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
<jsp:include page="head.jsp"></jsp:include>
<body>
	<div class="main-container">

		<jsp:include page="menu.jsp"></jsp:include>

		<div class="content-area"
			style="justify-content: center; align-items: center">

			<div class="register-area" style="justify-content: center; width: 30%">
				<div class="form-wrapper" id="sellerArea">
					<div class="title-area">
						<h1 style="font-size: 2.9rem; margin-bottom: 1rem;">Vender</h1>
					</div>
					<form action="${pageContext.request.contextPath}/sale" method="POST" id="formSale">
						<input type="hidden" name="productID" id="productID" value="${productID}">
						<input type="hidden" name="clientID" id="clientID"  value="${clientID}" >
						<input type="hidden" name="availableStock" id="availableStock"  value="${quantity}" >
						
						<div class="info-form-row">
							<label for="qtdSale" class="">Produto: <strong id="productName" >${product}</strong> </label> 
							<input type="hidden" name="productName"  value="${product}" >
						</div>
						
						<div class="info-form-row">
							<label for="qtdSale" class="">Estoque disponível: 
							<strong id="quantityStock" >${quantity}</strong> </label> 
						</div>
						
						<div class="form-row">
							<label for="clientStock" class="">Vender para:</label> 
							<input type="text" name="soldTo" id="soldTo" required>
						</div>
						
						<div class="form-row">
							<label for="qtdSale" class="">Quantidade:</label> 
							<input type="number" name="qtdSale" id="qtdSale"  step="0.01" required/>
						</div>
						
						<div class="form-row">
							<label for="priceSale" class="">Valor por unidade:</label> 
							<input type="number" name="priceSale" id="priceSale" step="0.01" required/>
						</div>
						
						<div class="form-row">
							<label for="DateSale" class="">Data da venda:</label> 
							<input type="datetime-local" name="dateSale" id="dateSale" required/>
						</div>
						
						<div class="info-form-row" style="margin: 1.2rem 0;">
							<label for="qtdSale" class="">Valor total: 
							<strong id="totalAmount" ></strong> </label> 
						</div>
						
					</form>
						<div class="button-area">
							<button class="btn-1" id="saleBtn">Enviar</button>
						</div>
						
				</div>
			</div>
		</div>
	</div>
	<script>const SITE_ROOT = "${pageContext.request.contextPath}";</script>
	<script src="${pageContext.request.contextPath}/scripts/sale.js"></script>
	<script src="${pageContext.request.contextPath}/scripts/menu.js"></script>
	
  </body>
</html>