package model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;

import jakarta.validation.constraints.NotNull;
import utils.Const;

public class Purchase {
	
	@NotNull
	private long id;
	private long clientID;
	private long productID;
	private BigDecimal pricePerUnit;
	private Double quantity;
	private Double debit;
	private BigDecimal amount;
	private Timestamp date;
	private boolean isPaidOff;
	private Long saleId;
	
	public Purchase() {
		
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	public long getClientID() {
		return clientID;
	}

	public void setClientID(long clientID) {
		this.clientID = clientID;
	}

	public long getProducID() {
		return productID;
	}

	public void setProducIDByProductName(String product) {
		if("cafe".equalsIgnoreCase(product)) {
			this.productID = Const.COFFEE_ID;
		}else if("pimenta".equalsIgnoreCase(product)) {
			this.productID = Const.PEPPER_ID;
		}else {
			throw new IllegalArgumentException("Parametro inválido para produtoID");
		}
	}
	
	public void setProducID(long productID) {
		this.productID = productID;
	}
	
	public BigDecimal getPricePerUnit() {
		return pricePerUnit;
	}

	public void setPricePerUnit(BigDecimal pricePerUnit) {
		this.pricePerUnit = pricePerUnit;
	}

	public Double getDebit() {
		return debit;
	}

	public void setDebit(Double debit) {
		this.debit = debit;
	}

	public long getProductID() {
		return productID;
	}

	public void setProductID(long productID) {
		this.productID = productID;
	}

	public Timestamp setActualDateAndReturnHim() {
		Timestamp now = new Timestamp(System.currentTimeMillis());
		this.date = now;
		
		return now;
	}

	public boolean isPaidOff() {
		return isPaidOff;
	}

	public void setPaidOff(boolean isPaidOff) {
		this.isPaidOff = isPaidOff;
	}
	
	public void setPaidOff() {
		if(this.debit > 0) {
			this.isPaidOff = false;
		}else {
			this.isPaidOff = true;
		}
	}
	
	public Long getSaleId() {
		return saleId;
	}

	public void setSaleId(Long saleId) {
		this.saleId = saleId;
	}
	
	public Double getQuantity() {
		return quantity;
	}

	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Timestamp getDate() {
		return date;
	}

	public void setDate(Timestamp date) {
		this.date = date;
	}

	/**
	 * método set pra quantity e debit vindos do client side.
	 * @param quantity
	 * @param clientStockQuantity
	 */
	public void calcAndSetQuantityAndDebit(Double quantityPurchased, Double clientStockQuantity) {//5, 3
		
		if (quantityPurchased == null || clientStockQuantity == null || quantityPurchased < 0 || clientStockQuantity < 0) {
		    throw new IllegalArgumentException("Valores invalidos para cálculo");
		}
		
		this.quantity = quantityPurchased;
		
		BigDecimal purchased = BigDecimal.valueOf(this.quantity); //5     
		BigDecimal clientStock = BigDecimal.valueOf(clientStockQuantity);  //3    
		
		if (clientStock.compareTo(purchased) < 0) {
			
			BigDecimal debitAmount = purchased.subtract(clientStock)
					.setScale(2, RoundingMode.HALF_UP);
			
			this.debit = debitAmount.doubleValue();//2               
			this.quantity = clientStock.doubleValue();  //3       
		} else {
			this.quantity = quantityPurchased;
			this.debit = 0.0;                                     
			
		}
	}

	

	
}
