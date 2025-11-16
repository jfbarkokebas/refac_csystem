package exceptions;

public class InvalidResultException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public InvalidResultException(String message) {
		super(message);
	}

}
