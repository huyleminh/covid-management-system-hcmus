package shared.exceptions;

public class DBConnectionException extends Exception {
	public static final DBConnectionException INSTANCE = new DBConnectionException("The database server is closed");

	public DBConnectionException(String message) {
		super(message);
	}
}
