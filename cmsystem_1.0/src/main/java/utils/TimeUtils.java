package utils;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public class TimeUtils {
	
	
	public static Timestamp stringToTimestamp(String date) {
		
		if(date == null || "".equalsIgnoreCase(date.trim())) {
			throw new IllegalArgumentException("A data não pode ser nula ou vazia");
		}
		
		Timestamp result;
		try {
			LocalDateTime localDateTime = LocalDateTime.parse(date);
			result = Timestamp.valueOf(localDateTime);
		} catch (Exception e) {
			throw new IllegalArgumentException("A data não pôde ser convertida de String para Timestamp");
		}
		return result;
	}
	
	
	 public static boolean isEntryBeforeDebit(Timestamp entryDate, Timestamp debitDate) {
	        if (entryDate == null || debitDate == null) {
	            throw new IllegalArgumentException("As datas não podem ser nulas.");
	        }
	        return entryDate.before(debitDate);
	    }

}
