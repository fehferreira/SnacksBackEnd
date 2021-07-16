package br.com.ferreira.snacks.exception;

public class UpdateWorkingStatusException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public UpdateWorkingStatusException(String message) {
		super(message);
	}
}
