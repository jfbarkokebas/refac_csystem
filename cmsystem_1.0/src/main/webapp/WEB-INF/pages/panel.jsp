<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<jsp:include page="head.jsp"></jsp:include>
<body>
	<div class="main-container">

		<jsp:include page="menu.jsp"></jsp:include>
		
		<div class="content-area">

            <div class="register-area">

				<div class="form-wrapper">
					<div class="title-area-panel">
						<h1>Filtro</h1>
					</div>
					<form id="filterForm" action="${pageContext.request.contextPath}/panel" method="get">
					<input type="hidden" name="action" value="filter">
						<div class="form-row">
							<label class="labelPanel" >Inicio</label> 
							<input type="datetime-local" style="padding: 4px 8px;"
							name="startDateFilter" id="startDateFilter">
						</div>
						<div class="form-row">
							<label class="labelPanel">Fim</label> 
							<input type="datetime-local" style="padding: 4px 8px;"
							name="endDateFilter" id="endDateFilter">
						</div>
						<p id="formMsg" style="color: red"></p>
					</form>
					<button id="filterBtn" class="btn-1">Buscar</button>
				</div>
			</div>

            <div class="action-panel-container">

                <div class="cards-area">

                    <div class="card" id="stocked">
                        <div class="title-card-area">
                            <h2>Comprado</h2>
                        </div>
                        <div class="info-area">
                            <p class="row-card">
                                <strong>Café:</strong>
                                <span id="coffee-purchased"> </span>
                                <span>Sacas</span>
                            </p>
                            <p class="row-card">
                                <strong>Pimenta:</strong>
                                <span id="pepper-purchased"> </span>
                                <span>Kg</span>
                            </p>
                        </div>
                    </div>

                    <div class="card" id="stocked">
                        <div class="title-card-area">
                            <h2>Vendido</h2>
                        </div>
                        <div class="info-area">
                            <p class="row-card">
                                <strong>Café:</strong>
                                <span id="coffee-sold"> </span>
                                <span>Sacas</span>
                            </p>
                            <p class="row-card">
                                <strong>Pimenta:</strong>
                                <span id="pepper-sold"> </span>
                                <span>Kg</span>
                            </p>
                        </div>
                    </div>

                    <div class="card" id="stocked">
                        <div class="title-card-area">
                            <h2>Lucrado</h2>
                        </div>
                        <div class="info-area">
                            <p class="row-card-profited">
                            	<strong class="info-label">Café: </strong>
                                <span id="coffee-profited"></span>
                                <span > / </span>
                                
                                <span id="coffee-percentual-profited"></span> 
                                <span>%</span> 
                            </p>
                            <p class="row-card-profited">
                            	<strong class="info-label">Pimenta: </strong>
                                <span id="pepper-profited"></span>
                                <span > / </span>
                                
                                <span id="pepper-percentual-profited"></span> 
                                <span>%</span> 
                            </p>
                        </div>
                    </div>

                </div>

                <div class="chart-area">
                    <canvas id="lucroPorPeriodoChart" width="840" height="420"></canvas>

                </div>

            </div>

        </div>
		
	</div>
	<script> const SITE_ROOT = "${pageContext.request.contextPath}"; </script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/chartjs/chartjs-min.js"></script>
	<script src="${pageContext.request.contextPath}/scripts/panel.js"></script>
	<script src="${pageContext.request.contextPath}/scripts/menu.js"></script>

</body>
</html>