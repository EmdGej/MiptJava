package student;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import exceptions.IncorrectNameException;
import exceptions.IncorrectGradeException;
import validators.GradeValidator;

public class Student<T> {
    public Student(String name) throws IncorrectNameException {
        if (name.isEmpty()) {
            throw new IncorrectNameException("Incorrect name format");
        } else {
            this.name = name;
        }
    }

    public Student(String name, GradeValidator<T> gradeValidator) throws IncorrectNameException {
        this(name);

        if (gradeValidator != null) {
            this.gradeValidator = gradeValidator;
        }
    }

    public Student(String name, List<T> grades) throws IncorrectNameException, IncorrectGradeException {
        if (name.isEmpty()) {
            throw new IncorrectNameException("Incorrect name format");
        } else {
            this.name = name;
        }

        CheckRecord checker = checkGradesLegitimacy(grades);
        if (checker.legit) {
            this.grades = new ArrayList<T>(grades);
        } else {
            throw new IncorrectGradeException(checker.position);
        }
    }

    public Student(String name, List<T> grades, GradeValidator<T> gradeValidator) throws IncorrectNameException, IncorrectGradeException {
        this(name);

        if (gradeValidator != null) {
            this.gradeValidator = gradeValidator;
        }

        CheckRecord checker = checkGradesLegitimacy(grades);
        if (checker.legit) {
            this.grades = new ArrayList<T>(grades);
        } else {
            throw new IncorrectGradeException(checker.position);
        }
    }

    public void changeName(String name) throws IncorrectNameException {
        if (name.isEmpty()) {
            throw new IncorrectNameException("Incorrect name format");
        } else {
            this.name = name;
        }
    }

    public void addGrade(T grade) throws IncorrectGradeException {
        CheckRecord checker = checkGradesLegitimacy(new ArrayList<T>(Arrays.asList(grade)));
        if (checker.legit) {
            grades.add(grade);
        } else {
            throw new IncorrectGradeException(checker.position);
        }
    }

    // Delete all grades equal to grade
    public void deleteGradesByValue(T grade) throws IncorrectGradeException {
        CheckRecord checker = checkGradesLegitimacy(new ArrayList<T>(Arrays.asList(grade)));
        if (checker.legit) {
            List<T> newGrades = new ArrayList<T>();

            for (int i = 0; i < grades.size(); ++i) {
                if (!grades.get(i).equals(grade)) {
                    newGrades.add(grades.get(i));
                }
            }

            grades = newGrades;
        } else {
            throw new IncorrectGradeException(checker.position);
        }
    }

    // Delete grade by index
    public void deleteGradeByPosition(Integer position) throws ArrayIndexOutOfBoundsException {
        grades.remove(position.intValue());
    }

    public String getName() {
        return name;
    }

    public ArrayList<T> getGrades() {
        return new ArrayList<T>(grades);
    }

    public boolean equals(Student<T> other) {
        if (!name.equals(other.name)) {
            return false;
        }

        if (grades == null && other.getGrades() != null || grades != null && other.getGrades() == null) {
            return false;
        }

        if (grades == null && other.getGrades() == null) {
            return true;
        }

        if (grades.size() != other.getGrades().size()) {
            return false;
        }

        for (int i = 0; i < grades.size(); ++i) {
            if (!grades.get(i).equals(other.getGrades().get(i))) {
                return false;
            }
        }

        return true;
    }

    @Override
    public String toString() {
        if (grades == null || grades.isEmpty()) {
            return "Name: " + name + ", Grades: No grades";
        }

        return "Name: " + name + ", Grades: " + grades;
    }

    private String name;
    private List<T> grades = new ArrayList<T>();
    private GradeValidator<T> gradeValidator = T -> true;

    private record CheckRecord(boolean legit, int position) {}

    private CheckRecord checkGradesLegitimacy(List<T> grades) {
        for (int i = 0; i < grades.size(); ++i) {
            if (!gradeValidator.isValid(grades.get(i))) {
                return new CheckRecord(false, i);
            }
        }

        return new CheckRecord(true, -1);
    }
}
