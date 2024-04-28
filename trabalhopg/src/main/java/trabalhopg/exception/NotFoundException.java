package trabalhopg.exception;

public class NotFoundException extends Exception {
	private String message;
	private static final long serialVersionUID = 1L;
	
	public NotFoundException( String message ) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
