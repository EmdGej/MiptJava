package exceptions;

public class IncorrectNameException extends Exception {
    public IncorrectNameException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return "Incorrect name format";
    }
}
