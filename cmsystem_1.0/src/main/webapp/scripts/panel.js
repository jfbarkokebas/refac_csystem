"use strict";

const coffeePurchased = document.getElementById("coffee-purchased");
const pepperPurchased = document.getElementById("pepper-purchased");
const coffeeSold = document.getElementById("coffee-sold");
const pepperSold = document.getElementById("pepper-sold");
const coffeeProfit = document.getElementById("coffee-profited");
const coffeePercentualProfit = document.getElementById("coffee-percentual-profited");
const pepperProfit = document.getElementById("pepper-profited");
const pepperPercentualProfit = document.getElementById("pepper-percentual-profited");
const filterBtn = document.getElementById("filterBtn");
const formMsg = document.getElementById("formMsg");
const ctx = document.getElementById('lucroPorPeriodoChart').getContext('2d');

let chartInstance = null;

function renderChart(data) {
	const coffeeList = data.chartData.coffeeProfitList;
	const pepperList = data.chartData.pepperProfitList;
	const biggest = Math.max(coffeeList.length, pepperList.length);
	const dynamicLabels = Array.from({ length: biggest }, (_, i) => `venda ${i + 1}`);

	const dadosLucro = {
		labels: dynamicLabels,
		datasets: [
			{
				label: 'Lucro Café (%)',
				data: coffeeList,
				fill: true,
				borderColor: 'green',
				backgroundColor: 'rgba(144, 238, 144, 0.3)',
				tension: 0.2,
				pointBackgroundColor: 'green',
			},
			{
				label: 'Lucro Pimenta (%)',
				data: pepperList,
				fill: true,
				borderColor: 'red',
				backgroundColor: 'rgba(231, 76, 60, 0.2)',
				tension: 0.2,
				pointBackgroundColor: 'red',
			}
		]
	};

	// Destroi o gráfico anterior, se existir
	if (chartInstance) {
		chartInstance.destroy();
	}

	// Cria novo gráfico
	chartInstance = new Chart(ctx, {
		type: 'line',
		data: dadosLucro,
		options: {
			responsive: true,
			plugins: {
				legend: { position: 'top' },
				title: {
					display: true,
					text: 'Lucro Percentual por Venda - Café e Pimenta'
				}
			},
			scales: {
				y: {
					title: { display: true, text: 'Lucro (%)' },
					beginAtZero: true
				},
				x: {
					title: { display: true, text: 'Venda' }
				}
			}
		}
	});
}

function atualizarCards(data) {
	coffeeSold.textContent = data.cards.coffeSold;
	coffeePercentualProfit.textContent = data.cards.coffeePercentualProfit;
	coffeeProfit.textContent = data.cards.coffeeProfit;
	coffeePurchased.textContent = data.cards.coffeePurchased;
	pepperPercentualProfit.textContent = data.cards.pepperPercentualProfit;
	pepperProfit.textContent = data.cards.pepperProfit;
	pepperPurchased.textContent = data.cards.pepperPurchased;
	pepperSold.textContent = data.cards.pepperSold;
}

function getMapData(startDate = null, endDate = null) {
	let url = SITE_ROOT + "/panel";

	if (startDate && endDate) {
		url += `?action=filter&startDateFilter=${encodeURIComponent(startDate)}&endDateFilter=${encodeURIComponent(endDate)}`;
	}

	fetch(url, {
		method: "GET",
		headers: {
			"Accept": "application/json"
		}
	})
		.then(response => {
			if (!response.ok) throw new Error(`Erro HTTP! status: ${response.status}`);
			return response.json();
		})
		.then(data => {
			atualizarCards(data);
			renderChart(data);
		})
		.catch(error => {
			document.getElementById("menu-msg").textContent = "Não foi possível atualizar os dados!";
			console.error("Erro na requisição:", error);
		});
}

function init() {
	
	filterBtn.addEventListener("click", (e) => {
		e.preventDefault(); 

		const startInput = document.getElementById('startDateFilter');
		const endInput = document.getElementById('endDateFilter');

		const startDate = startInput.value;
		const endDate = endInput.value;

		if (new Date(startDate) <= new Date(endDate)) {
			formMsg.textContent = '';
			
			getMapData(startDate, endDate); 
			
		} else {
			formMsg.textContent = 'A data de início não pode ser maior que a data final.';
		}
	});

	getMapData();
}

document.addEventListener("DOMContentLoaded", init);

//END FILE