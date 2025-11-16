package model;

import java.sql.Timestamp;
import java.time.LocalDate;

import utils.TimeUtils;

public class Entry {

	private Long id;
	private Long clientId;
	private Long productId;
	private Double quantity;
	private Timestamp entryDate;
	private String observation;
	private boolean purchased;

	public Entry() {
		this.purchased = false;
	}

	public Entry(Long clientId, Long productId, Double quantity, Timestamp entryDate, String observation) {
		super();
		this.clientId = clientId;
		this.productId = productId;
		this.quantity = quantity;
		this.entryDate = entryDate;
		this.observation = observation;
	}

	public Entry(String clientId, String productId, String quantity, String entryDate, String observation) {
		super();
		this.clientId = Long.parseLong(clientId);
		this.productId = Long.parseLong(productId);
		this.quantity = Double.parseDouble(quantity);
		this.observation = observation;

		try {
			LocalDate localDate = LocalDate.parse(entryDate);
			this.entryDate = Timestamp.valueOf(localDate.atStartOfDay());
		} catch (Exception e) {
			throw new IllegalArgumentException("A data não pôde ser convertida de String para Timestamp");
		}
	}

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

	public void setClientId(String clientId) {
		this.clientId = Long.parseLong(clientId);
	}

	public Long getProduct_id() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public void setProductId(String product_id) {
		if(product_id == null) {
			throw new IllegalArgumentException("O productId não pode ser nulo");
		}
		
		this.productId = Long.parseLong(product_id);
	}

	public Double getQuantity() {
		return quantity;
	}

	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = Double.parseDouble(quantity);
	}

	public Timestamp getEntryDate() {
		return entryDate;
	}

	public void setEntryDate(Timestamp entryDate) {
		this.entryDate = entryDate;
	}

	public void setEntryDate(String entryDate) {
			this.entryDate = TimeUtils.stringToTimestamp(entryDate);
	}

	public String getObservation() {
		return observation;
	}

	public void setObservation(String observation) {
		this.observation = observation;
	}
	
	public boolean isPurchased() {
		return purchased;
	}

	public void setPurchased(boolean purchased) {
		this.purchased = purchased;
	}

	@Override
	public String toString() {
		return "Entry [id=" + id + ", clientId=" + clientId + ", productId=" + productId + ", quantity=" + quantity
				+ ", entryDate=" + entryDate + ", observation=" + observation + ", purchased=" + purchased + "]";
	}

}
