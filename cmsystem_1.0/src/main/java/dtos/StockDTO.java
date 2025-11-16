package dtos;

import utils.PtBr;

public class StockDTO {
	
	private Long id;
	private String name;
	private String cpf;
	private String product;
	private Double numQuantity;
	private String quantity;
	private String	unit;
	private Long clientID;
	
	public StockDTO() {
		super();
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getProduct() {
		return product;
	}
	public void setProduct(String product) {
		this.product = product;
	}
	public Double getNumQuantity() {
		return numQuantity;
	}
	
	public void setNumQuantity(Double quantity) {
		this.numQuantity = quantity;
		this.quantity = PtBr.formatDecimal(quantity);
	}
	
	public String getQuantity() {
		return quantity;
	}

	public String formatQuantity() {
		return PtBr.formatDecimal(this.numQuantity);
	}
	
	public String getUnit() {
		return unit;
	}
	
	public Long getClientID() {
		return clientID;
	}

	public void setClientID(Long clientID) {
		this.clientID = clientID;
	}

	/**
	 * recebe um produto e verifica qual unidade usar
	 * @param product
	 */
	public void setUnitByProduct(String product) {
		if("cafe".equalsIgnoreCase(product)) {
			this.unit = "Sacas";
		}else if("pimenta".equalsIgnoreCase(product)) {
			this.unit = "Kg";
		}else {
			throw new IllegalArgumentException("O produto passado não existe no estoque");
		}
	}
	
	public void setUnitByProduct(Long product_id) {
		if(product_id == 1) {
			this.unit = "Sacas";
		}else if(product_id == 2) {
			this.unit = "Kg";
		}else {
			throw new IllegalArgumentException("O produto passado não existe no estoque");
		}
	}

	@Override
	public String toString() {
		return "StockDTO [id=" + id + ", name=" + name + ", cpf=" + cpf + ", product=" + product + ", quantity="
				+ numQuantity + ", quantity=" + quantity + ", unit=" + unit + ", clientID=" + clientID + "]";
	}

}
