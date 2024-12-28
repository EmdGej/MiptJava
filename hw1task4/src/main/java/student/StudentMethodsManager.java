package student;

public class StudentMethodsManager<T> {
    public void createStudent(String name) {
        try {
            student = new Student<T>(name);
        } catch (Exception exception) {
            student = null;
            System.out.println("ERROR: " + exception.getMessage());
        }
    }

    public void addGrade(T grade) {
        if (student == null) {
            System.out.println("ERROR: Student was not created");
            return;
        }

        if (grade == null) {
            return;
        }

        try {
            student.addGrade(grade);
        } catch (Exception exception) {
            System.out.println("ERROR: " + exception.getMessage());
        }
    }

    public void deleteGrade(T grade) {
        if (student == null) {
            System.out.println("ERROR: Student was not created");
            return;
        }

        if (grade == null) {
            return;
        }

        try {
            student.deleteLastGradeByValue(grade);
        } catch (Exception exception) {
            System.out.println("ERROR: " + exception.getMessage());
        }
    }

    public void studentInformation() {
        if (student == null) {
            System.out.println("ERROR: Student was not created");
            return;
        }

        System.out.println(student);
    }

    private Student<T> student;
}
