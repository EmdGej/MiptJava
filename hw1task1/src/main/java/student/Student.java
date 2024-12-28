package student;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import exceptions.IncorrectNameException;
import exceptions.IncorrectGradeException;

public class Student {
    public Student(String name) throws IncorrectNameException {
        if (name.isEmpty()) {
            throw new IncorrectNameException("Incorrect name format");
        } else {
            this.name = name;
        }
    }

    public Student(String name, List<Integer> grades) throws IncorrectNameException, IncorrectGradeException {
        if (name.isEmpty()) {
            throw new IncorrectNameException("Incorrect name format");
        } else {
            this.name = name;
        }

        CheckRecord checker = checkGradesLegitimacy(grades);
        if (checker.legit) {
            this.grades = new ArrayList<Integer>(grades);
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

    public void addGrade(Integer grade) throws IncorrectGradeException {
        CheckRecord checker = checkGradesLegitimacy(new ArrayList<Integer>(Arrays.asList(grade)));
        if (checker.legit) {
            grades.add(grade);
        } else {
            throw new IncorrectGradeException(checker.position);
        }
    }

    // Delete all grades which equal to grade
    public void deleteGradesByValue(Integer grade) throws IncorrectGradeException {
        CheckRecord checker = checkGradesLegitimacy(new ArrayList<Integer>(Arrays.asList(grade)));
        if (checker.legit) {
            List<Integer> newGrades = new ArrayList<Integer>();

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

    public ArrayList<Integer> getGrades() {
        return new ArrayList<Integer>(grades);
    }

    public boolean equals(Student other) {
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
    private List<Integer> grades = new ArrayList<>();

    private final int LOW_GRADE = 0;
    private final int UP_GRADE = 10;

    private record CheckRecord(boolean legit, int position) {}

    private CheckRecord checkGradesLegitimacy(List<Integer> grades) {
        for (int i = 0; i < grades.size(); ++i) {
            if (grades.get(i) < LOW_GRADE || grades.get(i) > UP_GRADE) {
                return new CheckRecord(false, i);
            }
        }

        return new CheckRecord(true, -1);
    }
}
