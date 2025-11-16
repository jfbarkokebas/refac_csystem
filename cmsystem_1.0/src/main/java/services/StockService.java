package services;

import java.util.List;
import java.util.Map;

import daos.StockDAO;
import dtos.StockDTO;
import exceptions.AtomicTransactionException;
import utils.Const;

public class StockService {

	private StockDAO dao;

	public StockService() {
		this.dao = new StockDAO();
	}

	public List<StockDTO> getStock() throws Exception {
		return this.dao.getAllStocks();

	}

	public Map<String, Object> getTotalStock() throws Exception {
		return this.dao.getTotalCoffeAndPepper();
	}

	public List<StockDTO> getTableDataByProduct(String search, Boolean onlyPositiveStock) throws Exception {
		List<StockDTO> result = this.dao.getAllStocksByProduct(search, onlyPositiveStock);

		return result;
	}

	public List<StockDTO> getTableDataByName(String search) throws Exception {
		List<StockDTO> result;

		if (search.trim().isEmpty()) {
			result = this.dao.getAllStocks();
		} else {
			result = this.dao.getAllStocksByClientName(search);
		}

		return result;
	}

	public StockDTO getStockById(String id) throws Exception {

		return this.dao.getStockById(id);

	}

	public List<StockDTO> updateStockAndReturnStockList(String stockId, Double quantityPurchased) throws Exception {

		StockDTO stock = this.dao.getStockById(stockId);
		Double newQuantity = stock.getNumQuantity() - quantityPurchased;
		Double pendingQuantity = null;

		if (newQuantity < 0) {
			pendingQuantity = Math.abs(newQuantity);
			newQuantity = 0.0;
		}

		boolean updated = this.dao.updateStock(stockId, newQuantity, pendingQuantity);

		if (!updated) {
			throw new AtomicTransactionException("Não foi possivel concluir o update na tabela stocks");
		}

		List<StockDTO> result = this.getStock();

		return result;
	}

	public StockDTO getOwnerStock(String productID) throws Exception {

		Long clientId = Const.OWNER_ID;
		Long productId = Long.parseLong(productID);

		StockDTO resultStock = this.dao.getStockByClientAndProduct(clientId, productId);

		return resultStock;
	}

	public List<StockDTO> getTableDataByOwner() throws Exception {

		List<StockDTO> result = this.dao.getStockOwner();

		return result;
	}

	public List<StockDTO> getSupplyStock(Long prodID) throws Exception {
		
		return this.dao.getSupplyStocks(prodID);
	}
	
}
