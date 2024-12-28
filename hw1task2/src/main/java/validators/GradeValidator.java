package validators;

public interface GradeValidator<T> {
    boolean isValid(T grade);
}
