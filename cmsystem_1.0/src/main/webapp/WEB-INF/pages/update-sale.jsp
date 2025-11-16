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
					<form action="${pageContext.request.contextPath}/sale" method="post">
						<input type="hidden" name="saleID" id="saleID" value="${saleID}">
						<input type="hidden" name="pendency" id="pendency" value="${pendency}">
						<input type="hidden" name="action" value="dataToUpdate">
						
						<div class="info-form-row">
							<label for="qtdSale" class="">Produto:</label> 
							<input type="text" name="productName"  value="${product}" required readonly>
						</div>
						
						<div class="form-row">
							<label for="clientStock" class="">Vender para:</label> 
							<input type="text" name="soldTo" id="soldTo" required  value="${soldTo}" >
						</div>
						
						<div class="form-row">
							<label for="qtdSale" class="">Quantidade:</label> 
							<input type="number" name="qtdSale" id="qtdSale"  step="0.01" required value="${quantity}"/>
						</div>
						
						<div class="form-row">
							<label for="priceSale" class="">Valor por unidade:</label> 
							<input type="number" name="priceSale" id="priceSale" required value="${priceSale}" readonly/>
						</div>
						<div class="form-row">
							<label id="checkbox">
						        <input type="checkbox" name="useStock" value="sim">
						        Usar o estoque atual?
						    </label>
						</div>
						<div class="info-form-row" style="margin: 1.2rem 0;">
							<label for="qtdSale" class="">Valor total: 
							<strong id="totalAmount" ></strong> </label> 
						</div>
						
						<div class="button-area">
							<input type="submit" class="btn-1" id="saller-btn">
						</div>
						
					</form>
				</div>
			</div>
		</div>
	</div>
	<script>const SITE_ROOT = "${pageContext.request.contextPath}";</script>
	<script src="${pageContext.request.contextPath}/scripts/update-sale.js"></script>
	<script src="${pageContext.request.contextPath}/scripts/menu.js"></script>
	
<body>

</body>
</html>