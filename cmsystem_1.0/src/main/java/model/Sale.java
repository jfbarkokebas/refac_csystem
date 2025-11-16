package model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;

import utils.Const;
import utils.TimeUtils;

public class Sale {
	
	private Long id;
	private Long productID;
	private String soldTo;
	private BigDecimal pricePerUnit;
	private BigDecimal costPerUnit;//null
	private Double quantity;
	private Double pendency;
	private BigDecimal saleAmount;//calculated
	private Timestamp saleDate;
	private Boolean isCompleted;
	
	public Sale() {
		super();
		this.isCompleted= false;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public String getSoldTo() {
		return soldTo;
	}

	public void setSoldTo(String soldTo) {
		this.soldTo = soldTo;
	}
	
	public BigDecimal getPricePerUnitSold() {
		return pricePerUnit;
	}

	public void setPricePerUnitSold(BigDecimal pricePerUnitSold) {
		this.pricePerUnit = pricePerUnitSold;
	}

	public Double getSaleQuantity() {
		return quantity;
	}

	public void setQuantity(String saleQuantity) {
		if(saleQuantity == null) throw new IllegalArgumentException("saleQuantity não pode ser null");
		this.quantity = Double.parseDouble(saleQuantity);
	}
	
	public void setQuantity(Double saleQuantity) {
		this.quantity = saleQuantity;
	}
	
	public Double getPendency() {
		return pendency;
	}

	public void setPendency(Double pendency) {
		this.pendency = Math.abs(pendency);
	}

	public Timestamp getSaleDate() {
		return saleDate;
	}

	public void setSaleDate(Timestamp saleDate) {
		this.saleDate = saleDate;
	}
	
	public void setSaleDate(String saleDate) {
			this.saleDate = TimeUtils.stringToTimestamp(saleDate);
	}

	public BigDecimal getSaleAmount() {
		return saleAmount;
	}
	
	public void setSaleAmunt(BigDecimal totalSale) {
		this.saleAmount = totalSale;
	}

	public BigDecimal calcTotalSale() {
		BigDecimal result = this.pricePerUnit
				.multiply(BigDecimal.valueOf(this.quantity))
				.setScale(3, RoundingMode.HALF_UP);
		
		this.saleAmount = result;
		
		return result;
	}
	
	public BigDecimal calcAndSetCostSale() {
		if (this.costPerUnit != null && this.quantity != null) {
			BigDecimal quantity = BigDecimal.valueOf(this.quantity);
			BigDecimal result = this.costPerUnit.multiply(quantity)
													.setScale(3, RoundingMode.HALF_UP);
			
			return result;
		} else {
			return null;
		}
	}

	public Long getProductID() {
		return productID;
	}

	public void setProductID(Long productID) {
		this.productID = productID;
	}
	public void setProductID(String productID) {
		if(productID == null) throw new IllegalArgumentException("ProductID não pode ser null");
		
		this.productID = Long.parseLong(productID);
	}
	
	public void setProductIdByProductName(String product) {
		
		if("pimenta".equalsIgnoreCase(product)) {
			this.productID = Const.PEPPER_ID;
		}else {
			this.productID = Const.COFFEE_ID;
		}
	}

	public BigDecimal getCostPerUnit() {
		return costPerUnit;
	}

	public void setCostPerUnit(BigDecimal costPerUnitPurchased) {
		this.costPerUnit = costPerUnitPurchased;
	}

	public Boolean getIsCompleted() {
		return isCompleted;
	}

	public void setIsCompleted(Boolean isCompleted) {
		this.isCompleted = isCompleted;
	}

	@Override
	public String toString() {
		return "Sale [id=" + id + ", productID=" + productID + ", soldTo=" + soldTo
				+ ", pricePerUnitSold=" + pricePerUnit + ", costPerUnitPurchased=" + costPerUnit
				+ ", saleQuantity=" + quantity + ", pendency=" + pendency + ", totalSale=" + saleAmount
				+ ", saleDate=" + saleDate 
				+ ", isCompleted=" + isCompleted + "]";
	}

	
}
