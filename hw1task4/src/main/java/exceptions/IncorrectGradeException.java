package exceptions;

public class IncorrectGradeException extends Exception {
    public IncorrectGradeException(String message) {
        super(message);
    }

    public IncorrectGradeException(int position) {
        this.position = position;
    }

    @Override
    public String getMessage() {
        return "Grades[" + position + "] contain an incorrect grade";
    }

    private int position;
}
