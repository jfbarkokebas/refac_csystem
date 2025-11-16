"use strict";

const transactionsButton = document.getElementById("stock-link");
const estoqueButton = document.getElementById("stockpage");
const panelLink = document.getElementById("panel-link");
const registerLink = document.getElementById("register-link");
const pendencySales = document.getElementById("pendencySales");
const concludedSales = document.getElementById("concludedSales");
const undeliveredSales = document.getElementById("undeliveredSales");
const logout = document.getElementById("logout");
const divForms = document.getElementById("forms");

const form = document.getElementById("get-data-form");

function init() {
	
	transactionsButton.addEventListener("click", () => {
		form.action = SITE_ROOT + "/transactions";
		form.submit();
	});

	registerLink.addEventListener("click", () => {
		form.action = SITE_ROOT + "/clients";
		document.getElementById("formInput").name = "action"
		document.getElementById("formInput").value = "login"
		form.submit();
	});

	pendencySales.addEventListener("click", () => {
		form.action = SITE_ROOT + "/sale";
		document.getElementById("formInput").name = "action"
		document.getElementById("formInput").value = "getList"
		form.submit();
	});

	concludedSales.addEventListener("click", () => {
		form.action = SITE_ROOT + "/sale";
		document.getElementById("formInput").name = "action"
		document.getElementById("formInput").value = "salesCompleted"
		form.submit();
	});
	
	undeliveredSales.addEventListener("click", () => {
		form.action = SITE_ROOT + "/sale";
		document.getElementById("formInput").name = "action"
		document.getElementById("formInput").value = "getUndeliveredSales"
		form.submit();
	});

	logout.addEventListener("click", () => {
		form.action = SITE_ROOT + "/LogoutServlet";
		form.submit();
	});

	estoqueButton.addEventListener("click", () => {
		const form = document.createElement("form");
		form.action = SITE_ROOT + "/stockinfo";
		form.method = "get";

		const input = document.createElement("input");
		input.type = "hidden";

		form.appendChild(input);
		divForms.appendChild(form);

		form.submit();
	});
}

document.addEventListener("DOMContentLoaded", init);

//END OF FILE