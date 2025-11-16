package utils;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HandleTimeData {
	
	@SuppressWarnings("unused")
	public static String getActualFormattedDateTime() {
	    return LocalDateTime
	    		.now()
	    		.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
	}
	
	public static Timestamp parseStringToTimestamp(String date) {
		
		try {
			LocalDateTime localDateTime = LocalDateTime.parse(date);
			return Timestamp.valueOf(localDateTime);
			
		} catch (Exception e) {
			throw new IllegalArgumentException("A data não pôde ser convertida de String para Timestamp");
		}
		
	}

}
