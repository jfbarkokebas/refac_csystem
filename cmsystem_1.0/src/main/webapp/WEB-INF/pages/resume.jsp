<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<jsp:include page="head.jsp"></jsp:include>
<body>
	<div class="main-container">

		<jsp:include page="menu.jsp"></jsp:include>
		
		<div class="content-area" style="justify-content: center">
		
			<div id="container" class="action-container">
                <div class="top-cards">
                
                	<div class="card-finance">
                        <span>Valores</span>
                        <table style="font-size: 1rem">
                            <tr>
                                <td>Est. café:</td>
                                <td style="padding-left: 8px;">${resumeMap.coffeFinances.formatedTotalAmount}</td>
                            </tr>
                            <tr>
                                <td>Café unid:</td>
                                <td style="padding-left: 8px;">${resumeMap.coffeFinances.formatedCMP}</td>
                            </tr>
                            <tr>
                                <td>Est. pimenta:</td>
                                <td style="padding-left: 8px;">${resumeMap.pepperFinances.formatedTotalAmount}</td>
                            </tr>
                            <tr>
                                <td>Pimenta unid:</td>
                                <td style="padding-left: 8px;">${resumeMap.pepperFinances.formatedCMP}</td>
                            </tr>
                        </table>
                    </div>
                    
                    <div class="card-info">
                        <span>MEU ESTOQUE</span>
                        <table>
                            <tr>
                                <td>Café:</td>
                                <td style="padding-left: 8px;">${resumeMap.ownerCoffeStock } sacas</td>
                            </tr>
                            <tr>
                                <td>Pimenta:</td>
                                <td style="padding-left: 8px;">${resumeMap.ownerPepperStock } Kg</td>
                            </tr>
                        </table>
                    </div>
                
                    <div class="card-info">
                        <span>CLIENTES</span>
                        <table>
                            <tr>
                                <td>Café:</td>
                                <td style="padding-left: 8px;">${resumeMap.clientsStockCoffee } sacas</td>
                            </tr>
                            <tr>
                                <td>Pimenta:</td>
                                <td style="padding-left: 8px;">${resumeMap.clientsStockPepper } Kg</td>
                            </tr>
                        </table>
                    </div>
                    
                    <div class="card-info">
                        <span>ESTOQUE IDEAL</span>
                        <table>
                            <tr>
                                <td>Café:</td>
                                <td style="padding-left: 8px;">${resumeMap.idealStockCoffee } sacas</td>
                            </tr>
                            <tr>
                                <td>Pimenta:</td>
                                <td style="padding-left: 8px;">${resumeMap.idealStockPepper } Kg</td>
                            </tr>
                        </table>
                    </div>
                    
                    <div class="card-info">
                        <span>DÉBITO TOTAL</span>
                        <table>
                            <tr>
                                <td>Café:</td>
                                <td style="padding-left: 8px;">${resumeMap.coffeeDebit } sacas</td>
                            </tr>
                            <tr>
                                <td>Pimenta:</td>
                                <td style="padding-left: 8px;">${resumeMap.pepperDebit } Kg</td>
                            </tr>
                        </table>
                    </div>
                    
                    
                </div>
                <div class="bottom-cards">
				
                    <div class="table-content">
                        <span class="main-title">Lista de Devedores</span>
                        
                        <div id="resume-filter">

                            <form id="filter-debits"
                                action="${pageContext.request.contextPath}/resumo">
                            </form>

                            <form id="formSearchProduct" action="${pageContext.request.contextPath}/TransactionsServlet"
                                method="get">
                                <input type="hidden" name="action" value="resumeByProduct">
                            </form>
                            <button id="allBtn" class="btn-3" >Todos</button>
                            <button id="coffeeBtn" class="btn-3" style="margin: 0 1rem" >Café</button>
                            <button id="pepperBtn" class="btn-3" >Pimenta</button>
                        </div>
                        <div id='count-area'>
							<span> Resultados: </span>
							<span id="salesCount">${resumeMap.count}</span>
						</div>
                        
                        <div class="table-wrapper"  style="max-height: 46vh">

                            <table id="stock-data">
                                <tr>
                                    <th>Cliente</th>
                                    <th>Produto</th>
                                    <th>Débito</th>
                                    <th>Telefone</th>
                                    <th>Tempo (dias)</th>
                                </tr>
                                 <c:if test="${not empty resumeMap.debitorsList}">
                                    <c:forEach var="debitor" items="${resumeMap.debitorsList}">
                                        <tr class="tr">
                                            <td>${debitor.clientName}</td>
                                            <td>${debitor.productName}</td>
                                            <td>${debitor.debit}</td>
                                            <td>${debitor.clientPhone}</td>
                                            <td>${debitor.sinceOf}</td>
                                        </tr>
                                    </c:forEach>
                                </c:if> 
                            </table>
                        </div>
                    </div>
                </div>

            </div>
		
		</div>
		
	</div>
	<script> const SITE_ROOT = "${pageContext.request.contextPath}"; </script>
		<script src="${pageContext.request.contextPath}/scripts/page.js"></script>
		<script src="${pageContext.request.contextPath}/scripts/menu.js"></script>
		<script src="${pageContext.request.contextPath}/scripts/resume.js"></script>
<body>

</body>
</html>