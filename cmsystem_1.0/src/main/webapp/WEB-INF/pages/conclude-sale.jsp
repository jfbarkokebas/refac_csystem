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
						<h1 style="font-size: 2.9rem; margin-bottom: 1rem;">Concluir a Venda</h1>
					</div>
					<form id="concludeForm" action="${pageContext.request.contextPath}/SaleServlet" method="post">
						<input type="hidden" name="saleID" id="saleID" value="${saleID}">
						<input type="hidden" name="action" value="completeSale">
						
						<div class="info-form-row">
							<label for="qtdSale" class="">Produto:</label> 
							<input type="text" name="productName"  value="${product}"  readonly>
						</div>
						
						<div class="form-row">
							<label for="clientStock" class="">Vender para:</label> 
							<input type="text" name="soldTo" id="soldTo" required  value="${soldTo}" readonly>
						</div>
						
						<div class="form-row">
							<label for="qtdSale" class="">Quantidade:</label> 
							<input type="number" name="qtdSale" id="qtdSale"  step="0.01" readonly value="${quantity}" />
						</div>
						
						<div class="form-row">
							<label for="priceSale" class="">Valor por unidade:</label> 
							<input type="number" name="priceSale" id="priceSale" readonly value="${priceSale}" />
						</div>
						<div class="form-row">
							<label for="priceCost" class="">Custo por unidade:</label> 
							<input type="number" name="priceCost" id="cost-per-unit" />
						</div>
						
						<div class="info-form-row" style="margin: 1.2rem 0;">
							<label for="qtdSale" >Valor total: 
								<strong id="total-sale" >R$ ${totalSale}</strong> 
							</label> 
						</div>
						<div class="info-form-row" style="margin: 1.2rem 0;">
							<label for="qtdSale" >Custo total: 
								<strong id="total-cost" ></strong> 
							</label> 
						</div>
						<div class="info-form-row" style="margin: 1.2rem 0;">
							<label for="qtdSale">Lucro: 
								<strong id="sale-result" ></strong> 
							</label> 
						</div>
						
					</form>
					<div class="button-area">
						<button type="button" class="btn-1" id="conclude-btn">Concluir</button>
					</div>
				</div>
			</div>
		</div>
	</div>
	<script>const SITE_ROOT = "${pageContext.request.contextPath}";</script>
	<script src="${pageContext.request.contextPath}/scripts/conclude-sale.js"></script>
	<script src="${pageContext.request.contextPath}/scripts/menu.js"></script>
	
<body>

</body>
</html>