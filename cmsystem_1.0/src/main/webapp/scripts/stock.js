"use strict";

const reloadStockBtn = document.getElementById("reload-img");
const lastReading = document.getElementById("updated-on");
const cofeTotalStockSpan = document.getElementById("p1-value");
const pimentaTotalStockSpan = document.getElementById("p2-value");
const menuMsg = document.getElementById("menu-msg");
const pepperBtn = document.getElementById("pepperBtn");
const cofeBtn = document.getElementById("cofeBtn");
const stockOwnerBtn = document.getElementById("stockOwnerBtn");
const formSearchProduct = document.getElementById("formSearchProduct");
const onlyCoffeeBtn = document.getElementById("onlyCoffee");
const onlyPepperBtn = document.getElementById("onlyPepper");

function searchProduct(product) {
	const input = document.createElement("input");
	input.type = "hidden";
	input.name = "search";
	input.value = product;
	formSearchProduct.appendChild(input);
	formSearchProduct.submit();
}

function searchPositiveStock(product) {
	const input = document.createElement("input");
	input.type = "hidden";
	input.name = "search";
	input.value = product;
	
	const input2 = document.createElement("input");
		input2.type = "hidden";
		input2.name = "withPositiveStock";
		input2.value = "yes";
	
	formSearchProduct.appendChild(input);
	formSearchProduct.appendChild(input2);
	formSearchProduct.submit();
}

function getStockOwner() {

	const filter = document.getElementById("filter");

	const form = document.createElement("form");
	form.method = "get";
	form.action = SITE_ROOT + "/stockinfo";

	const input1 = document.createElement("input");
	input1.type = "hidden";
	input1.name = "action";
	input1.value = "ownerStock";

	filter.appendChild(form);
	form.appendChild(input1);

	form.submit();
	form.reset();

}

function getStock() {

	const url = SITE_ROOT + "/stockinfo?action=stock";

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

function init() {

	cofeBtn.addEventListener("click", () => {
		searchPositiveStock("cafe")
	});

	pepperBtn.addEventListener("click", () => {
		searchPositiveStock("pimenta")
	});
	
	onlyCoffeeBtn.addEventListener("click", () => {
		searchProduct("cafe")
	});

	onlyPepperBtn.addEventListener("click", () => {
		searchProduct("pimenta")
	});

	reloadStockBtn.addEventListener("click", () => {
		getStock();
	});

	stockOwnerBtn.addEventListener("click", () => {
		getStockOwner();
	});

}

document.addEventListener("DOMContentLoaded", init);