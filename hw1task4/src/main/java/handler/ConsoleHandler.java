package handler;

import student.StudentMethodsManager;
import java.util.Scanner;

public class ConsoleHandler {
    public ConsoleHandler() {
        in = new Scanner(System.in);
        studentMethodsManager = new StudentMethodsManager<>();
    }

    private void showConsoleOptions() {
        System.out.println("##### Options #####");
        System.out.println("# Input 1 to create new Student");
        System.out.println("# Input 2 to add grade to Student's grades");
        System.out.println("# Input 3 to delete last occurrence of grade in Student's grade");
        System.out.println("# Input 4 to show Student's information");
        System.out.println("# Input q to stop session");
    }

    private void startSession() {
        in = new Scanner(System.in);
        studentMethodsManager = new StudentMethodsManager<>();
    }

    private void handleSession() {
        String operation;
        boolean session = true;

        while(session) {
            System.out.println("Input operation number: ");
            operation = in.nextLine();

            switch (operation) {
                case "1":
                    studentMethodsManager.createStudent(scannerReadName());
                    break;

                case "2":
                    studentMethodsManager.addGrade(scannerReadGrade());
                    break;
                case "3":
                    studentMethodsManager.deleteGrade(scannerReadGrade());
                    break;
                case "4":
                    studentMethodsManager.studentInformation();
                    break;

                case "q":
                    session = false;
                    break;

                default:
                    System.out.println("There is no such operation");
            }
        }
    }

    private void closeSession() {
        in.close();
    }

    public void start() {
        showConsoleOptions();
        startSession();
        handleSession();
        closeSession();
    }

    private Integer scannerReadGrade() {
        System.out.println("Input grade: ");

        try {
            Integer grade = in.nextInt();
            return grade;

        } catch (Exception exception) {
            System.out.println("ERROR: Input was not an Integer value");
            return null;
        } finally {
            in.nextLine();
        }
    }

    private String scannerReadName() {
        System.out.println("Input name: ");
        return in.nextLine();
    }

    private static final String ANSI_GREEN = "\u001B[32m";
    private Scanner in;
    private StudentMethodsManager<Integer> studentMethodsManager;
}
