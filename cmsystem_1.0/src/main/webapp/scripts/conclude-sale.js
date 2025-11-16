"use strict";

const inputQuantitySold = document.getElementById("qtdSale");//quantidade
const inputCostPerUnit = document.getElementById("cost-per-unit");//custoUnidade
const priceUnitSold = document.getElementById("priceSale");//
const inputTotalSale = document.getElementById("total-sale");
const fieldTotalCost = document.getElementById("total-cost");//campoValorTotal
const fieldProfit = document.getElementById("sale-result");
const concludeBtn = document.getElementById("conclude-btn");
const concludeForm = document.getElementById("concludeForm");

function calcTotalCost() {
	const costPerUnit = parseFloat(inputCostPerUnit.value);
	const quantitySold = parseFloat(inputQuantitySold.value);

	if (!isNaN(costPerUnit) && !isNaN(quantitySold)) {
		const total = costPerUnit * quantitySold;

		const totalFormated = new Intl.NumberFormat('pt-BR', {
			style: 'currency',
			currency: 'BRL'
		}).format(total);

		fieldTotalCost.innerText = totalFormated;
	} else {
		fieldTotalCost.innerText = "R$ 0,00";
	}

}

function calcProfit() {
	let totalSaleTxt = inputTotalSale.innerText;
	let totalCostTxt = fieldTotalCost.innerText;

	// Remove tudo que não é dígito (mantém apenas números)
	const totalSale = parseInt(totalSaleTxt.replace(/\D/g, ""));
	const totalCost = parseInt(totalCostTxt.replace(/\D/g, ""));

	const result = (totalSale - totalCost) / 100;

	const resultFormatted = new Intl.NumberFormat('pt-BR', {
		style: 'currency',
		currency: 'BRL'
	}).format(result);
	
	fieldProfit.innerText = resultFormatted;
}


function init() {
	
	console.log("Botão encontrado:", concludeBtn);


	concludeBtn.addEventListener("click", () => {
		
		console.log("concluir")
		concludeForm.submit();
	});


	inputCostPerUnit.addEventListener("input", () => {
		calcTotalCost();
		calcProfit();
	});
	
};

document.addEventListener("DOMContentLoaded", init);