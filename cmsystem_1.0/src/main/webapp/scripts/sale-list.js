"use strict"

const searchCoffeeBtn = document.getElementById("coffeeSales");
const searchPepperBtn = document.getElementById("pepperSales");
const searchCustomerBtn = document.getElementById("sales-by-name");
const soldToInput = document.getElementById("soldToName");
const concludeButton = document.getElementById("concludeButton");
const product = document.getElementById("product");
const COFFEE = 1;
const PEPPER = 2;

function searchSalesByCustomerName() {

	const filter = document.getElementById("filter");

	const form = document.createElement("form");
	form.method = "get";
	form.action = SITE_ROOT + "/sale";

	const input1 = document.createElement("input");
	input1.type = "hidden";
	input1.name = "action";
	input1.value = "searchByCostumer";

	const input2 = document.createElement("input");
	input2.type = "hidden";
	input2.name = "soldToName";
	input2.value = document.getElementById("soldToName").value;

	filter.appendChild(form);
	form.appendChild(input1);
	form.appendChild(input2);

	form.submit();
	form.reset();

}

function searchSalesWithForm(searchID) {
	const form = document.getElementById("formSearchSales");

	const input1 = document.createElement("input");
	input1.type = "hidden";
	input1.name = "productID";
	input1.value = searchID;

	form.appendChild(input1);

	form.submit();
	form.reset();

}

function confirmDelete() {
	return confirm("Deseja excluir essa venda?");
}

function confirmDelivery(){
	return confirm('Quantidade imcompleta, tem certeza que quer entregar?');
}

function init() {

	concludeButton.addEventListener("click", () => {
		const form = document.createElement("form");
		form.action = SITE_ROOT + "/stockinfo";
		form.method = "get";

		const input = document.createElement("input");
		input.type = "hidden";
		input.name = "action";
		input.value = "supplyList";
		
		const productID = product.value ==="pimenta"? 2: 1;
		console.log(productID)
		
		const input2 = document.createElement("input");
		input2.type = "hidden";
		input2.name = "productID";
		input2.value = productID;

		form.appendChild(input);
		form.appendChild(input2);
		divForms.appendChild(form);

		form.submit();
	});

	searchCustomerBtn.addEventListener("click", () => {
		searchSalesByCustomerName();
	});

	searchCoffeeBtn.addEventListener("click", () => {
		searchSalesWithForm(COFFEE);
	});

	searchPepperBtn.addEventListener("click", () => {
		searchSalesWithForm(PEPPER);
	});
};

document.addEventListener("DOMContentLoaded", init);