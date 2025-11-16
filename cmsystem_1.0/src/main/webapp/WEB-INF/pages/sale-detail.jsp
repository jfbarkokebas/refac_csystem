<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<jsp:include page="head.jsp"></jsp:include>
<body>
	<div class="main-container">

		<jsp:include page="menu.jsp"></jsp:include>
		
		 <div class="content-area" style="flex-direction: column; padding: 2rem 3vw;">
            <div class="top-area">
                <h1>Conclusão da Venda para ${dataMap.clientName}</h1>
            </div>

            <div class="info-sale-area">
                <div class="show-product">
                    Produto: &nbsp; <span id="productName">${dataMap.product}</span>
                </div>
                <div class="show-product">
                   <!-- armazenada da sessão do usuário -->
					Compra:  ${dataMap.quantity}
                </div>
                <div class="show-product">
                   <!-- armazenada da sessão do usuário -->
					CMP: ${dataMap.cmp}
                </div>
            </div>
            
            <div class="table-area">
                <div class="table-wrapper" style="margin-top: 1rem; max-height: 45vh;">
                    <table id="sale-table">
                        <tr>
                            <th>Venda (unid)</th>
							<th>Fornecedor</th>
							<th>Quantidade</th>
							<th>Compra (unid)</th>
							<th>Custo</th>
							<th>Apurado </th>
							<th>Lucro Total</th>
							<th>Lucro Unid</th>
							<th>Lucro %</th>
							<th>Data</th>
                        </tr>
                       <c:if test="${not empty dataMap.purchaseList}">
							<c:forEach var="purchase" items="${dataMap.purchaseList}">
								<tr class="tr">
									<td>${purchase.formatedPricePerUnitSold}</td>
									<td>${purchase.clientName}</td>
									<td>${purchase.formatedQuantity}</td>
									<td>${purchase.formatedCostPerUnit}</td>
									<td>${purchase.fmtTotalCost}</td>
									<td>${purchase.fmtTotalRevenue}</td>
									<td>${purchase.fmtTotalProfit}</td>
									<td>${purchase.fmtProfitPerUnitAmount}</td>
									<td>${purchase.fmtProfitPerUnitPercentual}</td>
									<td>${purchase.formatedCreatedAt}</td>
								</tr>
							</c:forEach>
						</c:if>
                    </table>
                </div>
            </div>
        </div>
		
	</div>
	<script> const SITE_ROOT = "${pageContext.request.contextPath}"; </script>
	<script src="${pageContext.request.contextPath}/scripts/menu.js"></script>
<body>

</body>
</html>