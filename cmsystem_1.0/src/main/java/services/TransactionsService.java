package services;

import java.util.HashMap;
import java.util.Map;

import daos.EntryDAO;
import daos.StockDAO;

public class TransactionsService {

	private EntryDAO entryDAO;
	private StockDAO stockDAO;

	public TransactionsService() {
		this.entryDAO = new EntryDAO();
		this.stockDAO = new StockDAO();
	}

	public HashMap<String, Object> getTableDataByName(String search) throws Exception {
		HashMap<String, Object> result;
		if (search == null) {
			throw new IllegalArgumentException("A busca não pode ser NULL");
		}

		if (search.trim().isEmpty()) {
			result = this.entryDAO.getTableData();
			return result;

		} else {
			result = this.entryDAO.getTableDataByClient(search);
			return result;
		}

	}

	public HashMap<String, Object> getTableDataByProduct(String search) throws Exception {
		HashMap<String, Object> result = new HashMap<>();

		if (search == null) {
			throw new IllegalArgumentException("A busca não pode ser NULL");
		}

		result = this.entryDAO.getTableDataByProduct(search);

		return result;
	}

	public HashMap<String, Object> getTableData() throws Exception {

		HashMap<String, Object> result = this.entryDAO.getTableData();

		return result;
	}

	public Map<String, Object> getTotalStockMap() throws Exception {

		Map<String, Object> result = this.stockDAO.getTotalCoffeAndPepper();

		return result;
	}

}
