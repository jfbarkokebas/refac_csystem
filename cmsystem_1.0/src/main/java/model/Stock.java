package model;

import jakarta.validation.constraints.NotNull;

public class Stock {
	
	@NotNull
	private Long id;
	@NotNull(message = "O id do cliente é obrigatório")
	private Long clientId;
	@NotNull(message = "O id do produto é obrigatório")
	private Long productId;
	@NotNull(message = "A quantidade estocada é obrigatória")
	private Double quantity;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getClientId() {
		return clientId;
	}
	public void setClientId(Long clientId) {
		this.clientId = clientId;
	}
	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public Double getQuantity() {
		return quantity;
	}

	public void setQuantity(Double currentQuantity) {
		this.quantity = currentQuantity;
	}
	
	@Override
	public String toString() {
		return "Stock [id=" + id + ", clientId=" + clientId + ", productId=" + productId + ", quantity="
				+ quantity + "]";
	}
	
}
