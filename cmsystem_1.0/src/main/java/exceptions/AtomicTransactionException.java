package exceptions;

public class AtomicTransactionException extends RuntimeException{
	private static final long serialVersionUID = 1L;

	public AtomicTransactionException(String message) {
		super(message);
	}
}
