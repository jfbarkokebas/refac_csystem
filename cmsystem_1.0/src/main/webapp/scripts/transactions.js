"use strict";

import { initClientSelect } from './modules/clientSelect.js';

const stockDialog = document.getElementById("stockup-card");
const openStock = document.getElementById("open-st");
const saleCoffe = document.getElementById("sale-coffe");
const salePepper = document.getElementById("sale-pepper");
const clientsForm = document.getElementById("get-clients-to-select");
const options = document.querySelector(".option");
const tr = document.querySelector(".tr");
const reloadImg = document.getElementById("reload-img");
const lastReading = document.getElementById("updated-on");
const cofeTotalStockSpan = document.getElementById("p1-value");
const pimentaTotalStockSpan = document.getElementById("p2-value");
const formPostEntry = document.getElementById("form-post-entry");
const sendStockEntryBtn = document.getElementById("stock-button");
const menuMsg = document.getElementById("menu-msg");
const formSearchProduct = document.getElementById("formSearchProduct");
const formEntriesByName = document.getElementById("filter-entries-by-name");
const entriesByNameBtn = document.getElementById("entries-by-name");
const pepperBtn = document.getElementById("pepperBtn");
const cofeBtn = document.getElementById("cofeBtn");
const deleteEntry = document.getElementById("delete-entry");
const COFFEE = 1;
const PEPPER = 2;


function sale(productID) {
	const form = document.createElement("form");
	form.action = SITE_ROOT + "/stockinfo";
	form.method = "get";

	const input1 = document.createElement("input");
	input1.type = "hidden";
	input1.name = "action";
	input1.value = "sell";

	const input2 = document.createElement("input");
	input2.type = "hidden";
	input2.name = "clientID";
	input2.value = "1";

	const input3 = document.createElement("input");
	input3.type = "hidden";
	input3.name = "productID";
	input3.value = productID;

	form.appendChild(input1);
	form.appendChild(input2);
	form.appendChild(input3);

	document.getElementById("registerArea").
		appendChild(form);

	console.log(form)

	form.submit();

}

function searchProduct(product) {
	const input = document.createElement("input");
	input.type = "hidden";
	input.name = "search";
	input.value = product;
	formSearchProduct.appendChild(input);
	formSearchProduct.submit();
	formSearchProduct.reset();
}

function openDialog(element) {
	closeAllDialogs();
	element.style.display = "block"
}

function closeAllDialogs() {
	stockDialog.style.display = "none";
}

function showStockInfo() {
	document.getElementById("stock-form").submit();
}

function getTotalStockInfoAjax() {
	const url = SITE_ROOT + "/transactions?action=stock"; 

	fetch(url, {
		method: "GET",
		headers: {
			"Accept": "application/json"
		}
	})
		.then(response => {
			if (!response.ok) {
				throw new Error(`Erro HTTP! status: ${response.status}`);
			}
			return response.json();

		})
		.then(data => {
			lastReading.textContent = data.stockUpdateDate;
			cofeTotalStockSpan.textContent = data.cafe;
			pimentaTotalStockSpan.textContent = data.pimenta;
		})
		.catch(error => {
			menuMsg.textContent = "Não foi possível atualizar o estoque!";
			console.error("Erro na requisição:", error);
		});
}

function sendStockEntries() {
	const form = document.getElementById("form-post-entry");

	const produto = form.querySelector('[name="productStock"]');
	const quantidade = form.querySelector('[name="qtdStock"]');
	const dataEntrada = form.querySelector('[name="dateStock"]');

	if (!produto.value.trim()) {
		alert("O campo 'Produto' é obrigatório.");
		produto.focus();
		return;
	}

	if (!quantidade.value || isNaN(quantidade.value) || parseFloat(quantidade.value) < 0) {
		alert("Informe uma quantidade válida (maior que zero).");
		quantidade.focus();
		return;
	}

	if (!dataEntrada.value) {
		alert("A data de entrada é obrigatória.");
		dataEntrada.focus();
		return;
	}

	form.submit();

}

function resetForms() {
	const allForms = document.querySelectorAll("form");
	allForms.forEach(form => form.reset());

	const protectedForm = document.getElementById("form-post-entry");
	if (protectedForm && window.history.replaceState && window.performance.navigation.type === 1) {
		window.history.replaceState(null, null, window.location.href);
	}
}

function loadTransactionsData() {

	if (!tr) {
		estoqueForm.action = SITE_ROOT + "/transactions";
		estoqueForm.submit();

	}
}

function filterEntriesByName() {
	const input = document.createElement("input");
	input.type = "hidden";
	input.name = "search";
	input.value = document.getElementById("search_name").value;

	formEntriesByName.appendChild(input);
	formEntriesByName.submit();
	formEntriesByName.reset();

}

function disableButtom(buttom) {
    buttom.disabled = true;
	buttom.style.backgroundColor = "gray"
    buttom.innerText = "Processando"; // opcional, muda o texto
}

function setCurrentDate() {
  const input = document.getElementById("dateStock");
  const agora = new Date();

  // Ajusta para o formato aceito pelo input datetime-local
  const ano = agora.getFullYear();
  const mes = String(agora.getMonth() + 1).padStart(2, '0');
  const dia = String(agora.getDate()).padStart(2, '0');
  const hora = String(agora.getHours()).padStart(2, '0');
  const minuto = String(agora.getMinutes()).padStart(2, '0');

  input.value = `${ano}-${mes}-${dia}T${hora}:${minuto}`;
}

function init() {
	
	initClientSelect("#clientSelect", SITE_ROOT)

	// Adiciona um estado inicial para bloquear "voltar" sem querer
	history.pushState(null, '', location.href);

	window.addEventListener('popstate', function() {
		if (!confirm('Ação perigosa! Quer continuar?')) {
			// Reempurra o estado para impedir a navegação
			history.pushState(null, '', location.href);
		}
	});

	if (deleteEntry) {
		deleteEntry.addEventListener("click", (e) => {
			const confirmado = confirm("Tem certeza que deseja continuar?");
			if (!confirmado) {
				e.preventDefault(); // cancela o clique
			}
		});

	}

	saleCoffe.addEventListener("click", () => {
		disableButtom(saleCoffe)
		sale(COFFEE);
	});

	salePepper.addEventListener("click", () => {
		disableButtom(salePepper);
		sale(PEPPER);
	});

	entriesByNameBtn.addEventListener("click", () => {
		filterEntriesByName();
	});

	cofeBtn.addEventListener("click", () => {
		searchProduct("cafe")
	});

	pepperBtn.addEventListener("click", () => {
		searchProduct("pimenta")
	});

	sendStockEntryBtn.addEventListener("click", () => {
		disableButtom(sendStockEntryBtn);
		sendStockEntries();
	});

	openStock.addEventListener("click", () => {
		stockDialog.style.display = "block";
		setCurrentDate();
	});

	reloadImg.addEventListener("click", () => {

		getTotalStockInfoAjax();
	});

	resetForms();

}

document.addEventListener("DOMContentLoaded", init);

//END FILE