package dtos;

import java.sql.Timestamp;

import utils.PtBr;

public class TransactionsDTO {

	private Long id;
	private String clientName;
	private String productName;
	private Double quantity;
	private String formatedQuantity;
	private String unit;
	private String entryDate;
	private String observation;

	public TransactionsDTO() {
		super();
	}

	public TransactionsDTO(String clientName, String productName, Double quantity, String unit,
			Timestamp entryDate, String observation) {
		super();
		this.clientName = clientName;
		this.productName = productName;
		this.quantity = quantity;
		this.unit = unit;
		this.entryDate = PtBr.formatTimestamp(entryDate);
		this.observation = observation;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Double getQuantity() {
		return quantity;
	}

	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}
	
	public String getFormatedQuantity() {
		return formatedQuantity;
	}

	public void setFormatedQuantity(Double formatedQuantity) {
		this.formatedQuantity = PtBr.formatDecimal(formatedQuantity);
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}
	
	public String getEntryDate() {
		return entryDate;
	}

	public void setEntryDate(Timestamp entryDate) {
		this.entryDate = PtBr.formatTimestamp(entryDate);
	}

	public String getObservation() {
		return observation;
	}

	public void setObservation(String observation) {
		this.observation = observation;
	}

	@Override
	public String toString() {
		return "TransactionsDTO [clientName=" + clientName + ", productName=" + productName + ", quantity=" + quantity
				+ ", unit=" + unit + ", entryDate=" + entryDate + ", observation=" + observation + "]";
	}
	

}
