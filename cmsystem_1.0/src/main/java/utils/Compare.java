package utils;

import java.math.BigDecimal;

public class Compare {
	
	/**
     * Compara dois doubles com precisão usando BigDecimal.
     *
     * @param a primeiro número
     * @param b segundo número
     * @return -1 se a < b, 0 se a == b, 1 se a > b
     */
	public static int doubles(double a, double b) {
        BigDecimal x = BigDecimal.valueOf(a);
        BigDecimal y = BigDecimal.valueOf(b);
        return x.compareTo(y);
    }

}
