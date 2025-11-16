"use strict"

const searchCoffeeBtn = document.getElementById("coffeeBtn");
const searchPepperBtn = document.getElementById("pepperBtn");
const searchCustomerBtn = document.getElementById("sales-by-name");
const soldToInput = document.getElementById("soldToName");
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
	input1.value = "saleByCostumer";

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
	const form = document.getElementById("form-concluded-sales");

	const input1 = document.createElement("input");
	input1.type = "hidden";
	input1.name = "productID";
	input1.value = searchID;

	form.appendChild(input1);

	form.submit();
	form.reset();

}

function init() {

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

//END FILE