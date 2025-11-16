package model;

import java.sql.Timestamp;

import utils.HandleStrings;

public class Debit {

	private Long id;
	private Long clientID;
	private Long productID;
	private Double debit;
	private Timestamp lastUpdate;
	private String purchaseList;
	private boolean isSupplier;
	
	public Debit() {
		
	}
	
	public Debit(Purchase purchase) {
		this.clientID = purchase.getClientID();
		this.productID = purchase.getProducID();
		this.debit = purchase.getDebit();
	}

	public Long getID() {
		return this.id;
	}

	public void setID(Long id) {
		this.id = id;
	}
	
	public void setID(String id) {
		this.id = HandleStrings.stringToLong(id);
	}

	public Long getClientID() {
		return clientID;
	}

	public void setClientID(Long clientID) {
		this.clientID = clientID;
	}
	
	public void setClientID(String clientID) {
		this.clientID =  HandleStrings.stringToLong(clientID);
	}

	public Long getProductID() {
		return productID;
	}

	public void setProductID(String productID) {
		this.productID = HandleStrings.stringToLong(productID);
	}
	
	public void setProductID(Long productID) {
		this.productID = productID;
	}

	public Double getDebit() {
		return debit;
	}

	public void setDebit(Double debit) {
		this.debit = debit;
	}

	public Timestamp getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Timestamp lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public String getPurchaseList() {
		return purchaseList;
	}

	public void setPurchaseList(String purchaseList) {
		this.purchaseList = purchaseList;
	}
	
	public boolean isSupplier() {
		return isSupplier;
	}

	public void setSupplier(boolean isSupplier) {
		this.isSupplier = isSupplier;
	}

	@Override
	public String toString() {
		return "Debit [ID=" + id + ", clientID=" + clientID + ", productID=" + productID + ", debit=" + debit
				+ ", lastUpdate=" + lastUpdate + ", purchaseList=" + purchaseList + ", isSupplier=" + isSupplier + "]\n";
	}

}
