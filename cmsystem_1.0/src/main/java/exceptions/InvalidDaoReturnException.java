package exceptions;

public class InvalidDaoReturnException extends RuntimeException{
	private static final long serialVersionUID = 1L;

	public InvalidDaoReturnException(String message) {
		super(message);
	}
}
