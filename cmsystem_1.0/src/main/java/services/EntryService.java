package services;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import daos.EntryDAO;
import daos.StockDAO;
import enums.TypeEntry;
import model.Debit;
import model.Entry;
import model.EntryTransaction;
import model.Stock;
import utils.CalculatorUtils;
import utils.ProcessStringPurchaseList;
import utils.TimeUtils;

public class EntryService {

	EntryDAO dao;
	StockDAO stockDAO;

	public EntryService() {
		this.dao = new EntryDAO();
		this.stockDAO = new StockDAO();
//		this.purchaseDao = new PurchaseDao();
	}

	public boolean saveEntry(Entry entry, Stock stock) throws Exception {

		if (stock == null) {
			throw new IllegalArgumentException("O argumento stockData não pode ser null.");
		}

		boolean isSaved = false;

		Stock existingStock = this.stockDAO.getActualStock(stock);

		Double quantityToStockIn = stock.getQuantity();
		Long productID = stock.getProductId();
		Long clientID = stock.getClientId();

		EntryTransaction transactionEntry = new EntryTransaction();
		Debit debitInDB = this.dao.getClientDebit(productID, clientID);

		if (existingStock == null) {

			Boolean shouldADebitBeCreated = debitInDB == null;

			transactionEntry.setTypeEntry(TypeEntry.SIMPLE_ENTRY);
			transactionEntry.setClientEntry(entry.getQuantity());
			transactionEntry.setOwnerEntry(0.00);

			Long persistedEntryID = this.dao.saveFirstEntry(entry, stock, transactionEntry, shouldADebitBeCreated);

			isSaved = persistedEntryID > 0;

			return isSaved;
		}

		Double debit = debitInDB.getDebit();

		// se a entrada é anterior ao débito, débito = 0;
		if (TimeUtils.isEntryBeforeDebit(entry.getEntryDate(), debitInDB.getLastUpdate())) {
			debit = 0.00;
		}
		
		if (debit == 0.00 || debit == null) {// SEM DÉBITO

			transactionEntry.setTypeEntry(TypeEntry.SIMPLE_ENTRY);
			transactionEntry.setClientEntry(entry.getQuantity());
			transactionEntry.setOwnerEntry(0.00);

			isSaved = this.dao.saveEntry(entry, transactionEntry);

			return isSaved;
		}
		
		//AQUI SERIA CRIADA A PURCHASEBATCH
		
		if (quantityToStockIn < debit && debitInDB.isSupplier() == true) {// ABATER DÉBITO COMPRA SUPPLIER

			Double debitRemainder = CalculatorUtils.subtractTwoNumber(debit, quantityToStockIn);

			transactionEntry.setTypeEntry(TypeEntry.REDUCE_DEBIT);
			transactionEntry.setClientEntry(0.00);
			transactionEntry.setOwnerEntry(entry.getQuantity());

//			batch.setQuantity(quantityToStockIn);

			isSaved = this.dao.handleClientDebitsWithBatch(entry, transactionEntry, quantityToStockIn, debit,
					debitRemainder);

		} else if (quantityToStockIn < debit && debitInDB.isSupplier() == false) { // ABATER DÉBITO DE COMPRA COMUM
			Double debitRemainder = CalculatorUtils.subtractTwoNumber(debit, quantityToStockIn);

			transactionEntry.setTypeEntry(TypeEntry.REDUCE_DEBIT);
			transactionEntry.setClientEntry(0.00);
			transactionEntry.setOwnerEntry(entry.getQuantity());

			isSaved = this.dao.handleClientDebits(entry, transactionEntry, quantityToStockIn, debit, debitRemainder);

		} else if (quantityToStockIn >= debit && debitInDB.isSupplier() == true) {// QUITAR DÉBITO SUPPLIER

			isSaved = this.quitDebit(entry, transactionEntry, debitInDB, productID, debit, quantityToStockIn);

		} else {

			isSaved = this.quitDebit(entry, transactionEntry, debitInDB, productID, debit, quantityToStockIn);
		}

		return isSaved;

	}

	public Map<String, Object> getStockEntryDetails(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	public Map<String, Object> deleteStockEntry(Long stockEntryId) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private boolean quitDebit(Entry entry, EntryTransaction transactionEntry, Debit debitInDB, Long productID,
			Double debit, Double quantityToStockIn) throws SQLException {
		
		boolean isSaved = false;
		boolean isPaidOffTrue = false;
		boolean isDebitListDeleted = false;

		Double stockOwner = this.dao.getOwnerStockQuantity(productID);//2.5
		Double quitedQuantity = CalculatorUtils.sumTwoNumber(stockOwner, debit);//4
		Double clientQuantity = CalculatorUtils.subtractTwoNumber(entry.getQuantity(), debit);//2

		transactionEntry.setTypeEntry(TypeEntry.PAY_OFF_DEBIT);
		transactionEntry.setClientEntry(clientQuantity);
		transactionEntry.setOwnerEntry(debit);


		Boolean isQuited = this.dao.handleQuitClientDebits(entry, transactionEntry, quantityToStockIn, debit,
				quitedQuantity);

		// pegar a lista de compras com débito e setPaidOff = true
		Map<String, Object> purchasesWithDebitMap = ProcessStringPurchaseList.extractValuesFromPurchaseList(debitInDB);

		@SuppressWarnings("unchecked")
		List<Long> debitIdList = (List<Long>) purchasesWithDebitMap.get("list");

		for (int i = 0; i < debitIdList.size() - 1; i++) {
			isPaidOffTrue = this.dao.setPaidOffTRUE(debitIdList.get(i));
		}

		// apagar a purchase_list em debits
		isDebitListDeleted = this.dao.deleteDebitList(debitInDB);

		isSaved = isQuited && isPaidOffTrue && isDebitListDeleted;

		return isSaved;

	}

}
