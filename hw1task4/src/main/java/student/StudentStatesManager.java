package student;

import java.util.ArrayList;
import java.util.List;

public class StudentStatesManager<T> {
    StudentStatesManager() {
        states = new ArrayList<Student<T>>();
    }

    public void writeState(Student<T> state) {
        states.add(state);
    }

    public Student<T> setPreviousState() {
        if (states.size() > 1) {
            states.removeLast();

            Student<T> returnState = states.getLast();

            Student<T> copy = new Student<T>(states.getLast());
            states.removeLast();

            states.add(copy);

            return returnState;
        }

        return null;
    }

    private List<Student<T>> states;
}
