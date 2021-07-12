package br.com.ferreira.snacks.exception;

public class ImpossibleStartWorkException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public ImpossibleStartWorkException(String message) {
		super(message);
	}
	
}
