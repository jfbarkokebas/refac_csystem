package utils;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class PtBr {
	
	private static final Locale LOCALE_BR = new Locale("pt", "BR");
	 private static final DateTimeFormatter BR_DATE_TIME_FORMATTER =
	            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
	
	public static String parse(BigDecimal amount) {
		if(amount == null) {
			return "N/F";
		}
		NumberFormat formater = NumberFormat.getCurrencyInstance(LOCALE_BR);
        String formatedValue = formater.format(amount);
        
        return formatedValue;
        
	}
	
	public static String formatPercent(BigDecimal value) {
		if(value == null) {
			return "N/F";
		}
        NumberFormat formatter = NumberFormat.getPercentInstance(LOCALE_BR);
        formatter.setMinimumFractionDigits(2); // exibe duas casas decimais
        return formatter.format(value);
    }
	
	public static String formatDecimal(BigDecimal value) {
		if(value == null) {
			return "-";
		}
        NumberFormat formatter = NumberFormat.getNumberInstance(LOCALE_BR);
        formatter.setMinimumFractionDigits(2);
        formatter.setMaximumFractionDigits(2);
        return formatter.format(value);
    }
	
	public static String formatDecimal(Double value) {
		if(value == null) {
			return "N/F";
		}
	    NumberFormat formatter = NumberFormat.getNumberInstance(new Locale("pt", "BR"));
	    formatter.setMinimumFractionDigits(2);
	    formatter.setMaximumFractionDigits(2);
	    return formatter.format(value);
	}
	
	public static String formatTimestamp(Timestamp timestamp) {
		
        if (timestamp == null)  return "N/F";

        LocalDateTime dateTime = timestamp.toLocalDateTime();
        
        return dateTime.format(BR_DATE_TIME_FORMATTER);
    }
	
	 public static String convertFromDbTimestamp(Timestamp timestamp) {
	        LocalDateTime localDateTime = timestamp.toLocalDateTime();
	        ZonedDateTime recifeTime = localDateTime.atZone(ZoneId.of("America/Recife"));

	        return recifeTime.format(BR_DATE_TIME_FORMATTER); 
	    }

}
