package tokenring;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        RingProcessor processor = new RingProcessor(
                1, new ArrayList<>(Arrays.asList(10)), new ArrayList<>(Arrays.asList(0)), new File("logPath.log"));
        processor.startProcessing();
    }
}
