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
            statesManager.writeState(new Student(this));
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
            statesManager.writeState(new Student(this));
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

    Student(Student other) {
        name = other.name;
        grades = new ArrayList<T>(other.grades);
    }

    public void changeName(String name) throws IncorrectNameException {
        if (name.isEmpty()) {
            throw new IncorrectNameException("Incorrect name format");
        } else {
            this.name = name;
            statesManager.writeState(new Student(this));
        }
    }

    public void addGrade(T grade) throws IncorrectGradeException {
        CheckRecord checker = checkGradesLegitimacy(new ArrayList<T>(Arrays.asList(grade)));
        if (checker.legit) {
            grades.add(grade);
            statesManager.writeState(new Student(this));
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
            statesManager.writeState(new Student(this));
        } else {
            throw new IncorrectGradeException(checker.position);
        }
    }

    // Delete grade by index
    public void deleteGradeByPosition(Integer position) throws ArrayIndexOutOfBoundsException {
        grades.remove(position.intValue());
        statesManager.writeState(new Student(this));
    }

    // Delete last occurrence of grade by value
    public void deleteLastGradeByValue(T grade) throws IncorrectGradeException {
        CheckRecord checker = checkGradesLegitimacy(new ArrayList<T>(Arrays.asList(grade)));
        if (checker.legit) {
            grades = new ArrayList<T>(grades);
            for (int i = grades.size() - 1; i >= 0; --i) {
                if (grades.get(i) == grade) {
                    grades.remove(i);
                    break;
                }
            }

            statesManager.writeState(new Student(this));
        } else {
            throw new IncorrectGradeException(checker.position);
        }
    }

    public String getName() {
        return name;
    }

    public ArrayList<T> getGrades() {
        return new ArrayList<T>(grades);
    }

    public Boolean equals(Student<T> other) {
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

    public void undo() {
        this.changeFields(statesManager.setPreviousState());
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
    private StudentStatesManager<T> statesManager = new StudentStatesManager<T>();

    private record CheckRecord(boolean legit, int position) {}

    private CheckRecord checkGradesLegitimacy(List<T> grades) {
        for (int i = 0; i < grades.size(); ++i) {
            if (!gradeValidator.isValid(grades.get(i))) {
                return new CheckRecord(false, i);
            }
        }

        return new CheckRecord(true, -1);
    }

    private void changeFields(Student other) {
        if (other == null) {
            return;
        }

        name = other.name;
        grades = other.grades;
    }
}
