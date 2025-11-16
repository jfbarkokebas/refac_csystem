"use strict";

const formFilter = document.getElementById("filter-debits");
const coffeeBtn = document.getElementById("coffeeBtn");
const pepperBtn = document.getElementById("pepperBtn");
const allBtn = document.getElementById("allBtn");
const COFFE_ID = 1;
const PEPPER_ID = 2;

function getInfo(value){
	
	const input1 = document.createElement("input");
	input1.type = "hidden";
	input1.name = "action";
	input1.value = "resumeByProduct";
	
	const input2 = document.createElement("input");
	input2.type = "hidden";
	input2.name = "productID";
	input2.value = value;
	
	formFilter.appendChild(input1);
	formFilter.appendChild(input2);
	
	formFilter.submit();	
}


function init(){
	
	allBtn.addEventListener("click", ()=> formFilter.submit());
	
	coffeeBtn.addEventListener("click", ()=> getInfo(COFFE_ID));
	
	pepperBtn.addEventListener("click", ()=> getInfo(PEPPER_ID));
	
};

document.addEventListener("DOMContentLoaded", init);
