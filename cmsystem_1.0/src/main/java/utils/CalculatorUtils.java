package utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import exceptions.BusinessRuleException;
import model.Purchase;
import model.Sale;

public class CalculatorUtils {

	public static Double subtractTwoNumber(Double minuend, Double subtrahend) {
		if (minuend == null || subtrahend == null) {
			throw new IllegalArgumentException("Os operadores precisam ser validos para subtração");
		}
		
		BigDecimal a = BigDecimal.valueOf(minuend);
		BigDecimal b = BigDecimal.valueOf(subtrahend);
		
		BigDecimal difference = a.subtract(b).setScale(2, RoundingMode.HALF_UP);
		
		Double result = difference.doubleValue();
		
		return result;
	}
	
	public static Double sumTwoNumber(Double a, Double b) {
		if (a == null || b == null) {
			throw new IllegalArgumentException("Os operadores precisam ser validos para subtração");
		}

		BigDecimal x = BigDecimal.valueOf(a);
		BigDecimal y = BigDecimal.valueOf(b);

		BigDecimal z = x.add(y).setScale(2, RoundingMode.HALF_UP);

		Double result = z.doubleValue();

		return result;
	}

	public static int calculateDaysSince(Timestamp timestamp) {
		if (timestamp == null) {
			throw new IllegalArgumentException("Timestamp não pode ser nulo.");
		}

		LocalDate pastDate = timestamp.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

		LocalDate today = LocalDate.now();

		return (int) ChronoUnit.DAYS.between(pastDate, today);
	}

	public static BigDecimal multiplyTwoNumbers(Number a, Number b) {
		if (a == null || b == null) {
			throw new IllegalArgumentException("Os operadores precisam ser válidos para multiplicação.");
		}

		BigDecimal x = convertToBigDecimal(a);
		BigDecimal y = convertToBigDecimal(b);

		return x.multiply(y).setScale(2, RoundingMode.HALF_UP);
	}

	public static BigDecimal divideTwoNumbers(Number a, Number b) {
		if (a == null || b == null) {
			throw new IllegalArgumentException("Os operadores precisam ser válidos para multiplicação.");
		}

		BigDecimal x = convertToBigDecimal(a);
		BigDecimal y = convertToBigDecimal(b);

		if (y.compareTo(BigDecimal.ZERO) == 0) {
			throw new ArithmeticException("Divisão por zero");
		}

		return x.divide(y, 2, RoundingMode.HALF_UP);
	}

	private static BigDecimal convertToBigDecimal(Number number) {

		if (number instanceof BigDecimal) {
			return (BigDecimal) number;
		} else if (number instanceof Long || number instanceof Integer) {
			return BigDecimal.valueOf(number.longValue());
		} else {
			return new BigDecimal(number.toString());
		}
	}

	/**
	 * @param purchase
	 * @param sale
	 * @return Map<String, Object> Keys: saleQuantity, saleDebit, delivered,
	 *         ownerQuantity, clientDebit, batchQuantity,completed.
	 */
	public static Map<String, Object> calcValuesToSupplySale(Purchase purchase, Sale sale) {

		Objects.requireNonNull(purchase, "calcValuesToSupplySale(): o parâmetro purchase não pode ser nulo");
		Objects.requireNonNull(sale, "calcValuesToSupplySale(): o parâmetro sale não pode ser nulo");

		Double purchaseQuantity = purchase.getQuantity();
		Double purchaseDebit = purchase.getDebit();
		Double clientDebit = purchaseDebit;

		Double saleQuantityToSupply;
		if (sale.getPendency() == null || Double.compare(sale.getPendency(), 0.00) == 0) {
			saleQuantityToSupply = sale.getSaleQuantity();
		} else {
			saleQuantityToSupply = sale.getPendency();
		}

		Boolean completed;
		Double delivered = subtractTwoNumber(purchaseQuantity, purchaseDebit);
		Double ownerQuantity;
		Double saleDebit;
		Double batchQuantity;

		// Cenário 1
		if (Double.compare(purchaseDebit, 0.00) == 0 && Double.compare(purchaseQuantity, saleQuantityToSupply) == 0) {

			saleDebit = 0.00;
			batchQuantity = purchaseQuantity;
			ownerQuantity = subtractTwoNumber(purchaseQuantity, saleQuantityToSupply);
			completed = true;

			return buildResult(saleQuantityToSupply, saleDebit, ownerQuantity, clientDebit, batchQuantity, delivered,
					completed);

			// Cenário 2
		} else if (Double.compare(purchaseDebit, 0.00) == 0
				&& Double.compare(purchaseQuantity, saleQuantityToSupply) < 0) {

			saleDebit = subtractTwoNumber(saleQuantityToSupply, purchaseQuantity);
			batchQuantity = purchaseQuantity;
			ownerQuantity = 0.00;
			completed = false;

			return buildResult(saleQuantityToSupply, saleDebit, ownerQuantity, clientDebit, batchQuantity, delivered,
					completed);
			
			// Cenário 3
		} else if (Double.compare(purchaseDebit, 0.00) == 0
				&& Double.compare(purchaseQuantity, saleQuantityToSupply) > 0) {
			
			saleDebit = 0.00;
			batchQuantity = saleQuantityToSupply;
			ownerQuantity = subtractTwoNumber(purchaseQuantity, saleQuantityToSupply);
			completed = true;

			return buildResult(saleQuantityToSupply, saleDebit, ownerQuantity, clientDebit, batchQuantity, delivered,
					completed);
			
			//cenario 4
		} else if (Double.compare(purchaseDebit, 0.00) > 0 && Double.compare(delivered, saleQuantityToSupply) > 0) {

			saleDebit = 0.00;
			batchQuantity = saleQuantityToSupply;
			ownerQuantity = subtractTwoNumber(delivered, saleQuantityToSupply);
			completed = true;

			return buildResult(saleQuantityToSupply, saleDebit, ownerQuantity, clientDebit, batchQuantity, delivered,
					completed);

			// Cenário 5
		} else if (Double.compare(purchaseDebit, 0.00) > 0 && Double.compare(delivered, saleQuantityToSupply) < 0) {

			saleDebit = subtractTwoNumber(saleQuantityToSupply, delivered);
			batchQuantity = delivered;
			ownerQuantity = 0.00;
			completed = false;

			return buildResult(saleQuantityToSupply, saleDebit, ownerQuantity, clientDebit, batchQuantity, delivered,
					completed);

			// Cenário 6
		} else if (Double.compare(purchaseDebit, 0.00) > 0 && Double.compare(delivered, saleQuantityToSupply) == 0
				&& Double.compare(purchaseQuantity, saleQuantityToSupply) > 0) {

			saleDebit = subtractTwoNumber(saleQuantityToSupply, delivered);
			batchQuantity = delivered;
			ownerQuantity = 0.00;
			completed = true;

			return buildResult(saleQuantityToSupply, saleDebit, ownerQuantity, clientDebit, batchQuantity, delivered,
					completed);

		} else {
			throw new BusinessRuleException("Condição de compra inesperada. Procure a equipe de desenvolvimento");
		}
	}

	private static Map<String, Object> buildResult(Double saleQuantity, Double saleDebit, Double ownerQuantity,
			Double ownerDebit, Double batchQuantity, Double delivered, Boolean completed) {
		Map<String, Object> result = new HashMap<>();
		result.put("saleQuantity", saleQuantity);
		result.put("saleDebit", saleDebit);
		result.put("ownerQuantity", ownerQuantity);
		result.put("ownerDebit", ownerDebit);
		result.put("batchQuantity", batchQuantity);
		result.put("delivered", delivered);
		result.put("completed", completed);
		return result;
	}

//	public static BigDecimal getActualSupplierCMP(SaleDAO dao , Long saleID) throws SQLException {
//
//		if (saleID == null)
//			throw new IllegalArgumentException("getActuaCMP() -> O id do produto não pode ser nulo");
//
//		List<PurchaseBatch> batchList = dao.getSuplierBatchsBySaleId(saleID);
//
//		if (batchList.isEmpty() || batchList == null) {
//			return BigDecimal.ZERO;
//		}
//
//		return calculateActualCMP(batchList);
//	}

//	public static BigDecimal calculateActualCMP(List<? extends Batch > availableBatchList) {
//
//		BigDecimal cmp = BigDecimal.ZERO;
//		BigDecimal totalAmount = BigDecimal.ZERO;
//		double totalQuantity = 0;
//
//		for (Batch batch : availableBatchList) {
//			BigDecimal parcialValue = multiplyTwoNumbers(batch.getQuantity(), batch.getUnitPrice());
//			totalAmount = totalAmount.add(parcialValue).setScale(2, RoundingMode.HALF_UP);
//			totalQuantity += batch.getQuantity();
//		}
//
//		if (totalQuantity == 0) {
//			return BigDecimal.ZERO;
//		}
//
//		cmp = divideTwoNumbers(totalAmount, totalQuantity);
//		return cmp;
//
//	}

}
