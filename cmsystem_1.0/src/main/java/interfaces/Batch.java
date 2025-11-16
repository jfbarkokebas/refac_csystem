package interfaces;

import java.math.BigDecimal;
import java.sql.Timestamp;

public interface Batch {
	
	public Long getId();

	public void setId(Long id);

	public Long getClientID();

	public void setClientID(Long clientID);

	public Long getProductID();

	public void setProductID(Long productID);

	public BigDecimal getUnitPrice();

	public void setUnitPrice(BigDecimal unitPrice);

	public Double getQuantity();

	public void setQuantity(Double quantity);
	
	public Timestamp getCreatedAt();

	public void setCreatedAt(Timestamp createdAt);
	
	public Long getPurchaseId();

	public void setPurchaseId(Long purchaseId);

}
