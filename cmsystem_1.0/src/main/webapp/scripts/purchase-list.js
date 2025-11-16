"use strict";

const formSearchByClientName = document.getElementById("formSearchPurchaseByName");
const searchByNameBtn = document.getElementById("search-by-name");
const searchCoffeBtn = document.getElementById("coffeeBtn");
const searchPepperBtn = document.getElementById("pepperBtn");
const debitsBtn = document.getElementById("purchase-debit");
const formSearchPurchase = document.getElementById("formSearchPurchase");
const COFFE_ID = 1;
const PEPPER_ID = 2;


function searchProduct(id){
	const input1 = document.createElement("input");
	input1.type="hidden";
	input1.name="action";
	input1.value="searchProduct";
	
	const input2 = document.createElement("input");
	input2.type="hidden";
	input2.name="productID";
	input2.value= id;
	
	formSearchPurchase.appendChild(input1);
	formSearchPurchase.appendChild(input2);
	formSearchPurchase.submit();	
}

function searchByDebit(){
	const input1 = document.createElement("input");
	input1.type="hidden";
	input1.name="action";
	input1.value="debits";
	
	formSearchPurchase.appendChild(input1);
	formSearchPurchase.submit();	
}

function init(){
	searchByNameBtn.addEventListener("click", ()=>{
		formSearchByClientName.submit();
	});
	
	searchCoffeBtn.addEventListener("click", ()=>{
		searchProduct(COFFE_ID);
	});
	
	searchPepperBtn.addEventListener("click", ()=>{
		searchProduct(PEPPER_ID);
	});
	
	debitsBtn.addEventListener("click", ()=>{
		searchByDebit();
	});
};

document.addEventListener("DOMContentLoaded", init);