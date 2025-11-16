package utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import model.Debit;
import model.Purchase;

public class ProcessStringPurchaseList {

	public static String createStringPurchaseDebitList(Debit debit, Purchase purchase) {
		
		if (purchase == null || debit == null) {
			throw new IllegalArgumentException("createStringPurchaseDebitList(): os parâmetros não podem ser null");
		}
		
		StringBuilder sb = new StringBuilder();

		String currentDebitList = debit.getPurchaseList();

		if (currentDebitList == null) {
			currentDebitList = "";
		}else {
			currentDebitList.trim();
		}

		if (!currentDebitList.isEmpty() && !currentDebitList.endsWith(",")) {
			currentDebitList = currentDebitList+",";
		}

		sb.append(currentDebitList)
			.append(purchase.getId())
			.append(":")
			.append(purchase.getDebit())
			.append(",");

		return sb.toString();
	}
	
	public static String deleteItemInPurchaseList(Purchase purchaseToDelete, Debit debitInDB) {
		
		if (purchaseToDelete == null || debitInDB == null) {
			throw new IllegalArgumentException("deleteItemInPurchaseList(): os parâmetros não podem ser null");
		}
		
		StringBuilder sb = new StringBuilder();
		sb.append(purchaseToDelete.getId())
		.append(":")
		.append(purchaseToDelete.getDebit())
		.append(",");
		
		String debitItem = sb.toString();
		
		String currentDebitList = debitInDB.getPurchaseList();
		
		String newDebitList = currentDebitList.replaceFirst(Pattern.quote(debitItem), "");
		
		return newDebitList;
	}

	/**
	 * Retorna uma map com duas listas: purchaseId e map <purhaesId, debit>
	 * Keys: 'list' e 'mapList'
	 * @param debit
	 * @return Map<String, Object>
	 */
	public static Map<String, Object> extractValuesFromPurchaseList(Debit debit) {
		
		Map<String, Object> result = new HashMap<String, Object>();

		String currentList = debit.getPurchaseList();

		if (currentList == null || currentList.trim().isEmpty()) {
			throw new IllegalArgumentException(
					"extractValuesFromPurchaseList(): getPurchaseList não pode ser null ou vazio");
		}

		List<Map<Long, Double>> mapList = new ArrayList<>();
		List<Long> idList = new ArrayList<Long>();

		String purchaseList = currentList;
		String[] entries = purchaseList.split(",");//string to array
		
		int  NUMBER_OF_ELEMENTS = 2;

		for (String entry : entries) {
			if (entry.isBlank()) continue;
			
			String[] values = entry.split(":");//separa o ID da qtde
			if (values.length != NUMBER_OF_ELEMENTS) {
                throw new IllegalArgumentException("extractValuesFromPurchaseList(): Formato inválido na purchaseList: " + entry);
            }

			try {
                Long purchaseID = Long.parseLong(values[0].trim());
                Double purchaseDebit = Double.parseDouble(values[1].trim());

                Map<Long, Double> mapPurchaseAndDebit = new HashMap<>();
                mapPurchaseAndDebit.put(purchaseID, purchaseDebit);
                
                idList.add(purchaseID);
                mapList.add(mapPurchaseAndDebit);
                
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("extractValuesFromPurchaseList(): Erro ao converter valores da purchaseList: " + entry, e);
            }
			
			result.put("list", idList);
            result.put("mapList", mapList);

		}

		return result;// RETORNAR LISTA DE ID E MAP COM ID E DÉBITO;
	}
	
}
