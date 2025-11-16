package exceptions;

public class AtomicStockQueryException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public AtomicStockQueryException(String message) {
		super(message);
	}
}
