package tokenring;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;

public class TokenRingTests {
    @BeforeEach
    public void setup() throws IOException {
        if (TEST_LOG.exists()) {
            TEST_LOG.delete();
        }

        List<Integer> dataAmount = Arrays.asList(1, 2, 1);
        List<Integer> targetNodes = Arrays.asList(0, 2, 1);

        ringProcessor = new RingProcessor(3, dataAmount, targetNodes, TEST_LOG);
    }


    @AfterEach
    public void cleanup() {
        if (TEST_LOG.exists()) {
            TEST_LOG.delete();
        }
    }

    @Test
    public void testRingProcessorInitialization() {
        assertNotNull(ringProcessor);
        assertTrue(TEST_LOG.exists());
    }

    @Test
    void testDataPackage() {
        DataPackage dataPackage = new DataPackage(5, "Test data");

        assertEquals(5, dataPackage.getDestinationNode());
        assertEquals("Test data", dataPackage.getData());
        assertTrue(dataPackage.getStartTime() > 0);

        dataPackage.setEndTime(100);
        assertEquals(100, dataPackage.getEndTime());

        dataPackage.setBufferInTime(50);
        assertEquals(50, dataPackage.getBufferInTime());

        dataPackage.setBufferOutTime(75);
        assertEquals(75, dataPackage.getBufferOutTime());

        assertFalse(dataPackage.isDelivered());
        dataPackage.setDelivered(true);
        assertTrue(dataPackage.isDelivered());
    }

    @Test
    public void testNodeFunctionality() throws InterruptedException {
        Node node = new Node(0, 0, 1, ringProcessor.getLogger());
        Node nextNode = new Node(1, 0, 1, ringProcessor.getLogger());
        node.setNextNode(nextNode);

        DataPackage dataPackage = new DataPackage(1, "Test data");
        node.addData(dataPackage);

        assertEquals(1, node.getBufferStack().size());
        executorService.submit(node);
        Thread.sleep(100);
        executorService.shutdownNow();

        assertEquals(0, node.getBufferStack().size());
    }

    @Test
    public void testCoordinatorHandling() {
        Node coordinator = new Node(0, 0, 1, ringProcessor.getLogger());
        DataPackage dataPackage = new DataPackage(0, "Test data");

        coordinator.addCoordinatorData(dataPackage);
        assertTrue(dataPackage.isDelivered());
        assertEquals(1, coordinator.getAllData().size());
    }

    @Test
    public void testDataPackageLifecycle() {
        DataPackage dataPackage = new DataPackage(1, "Test data");

        assertEquals(1, dataPackage.getDestinationNode());

        dataPackage.setBufferInTime(System.nanoTime());
        dataPackage.setBufferOutTime(System.nanoTime() + 10);
        dataPackage.setEndTime(System.nanoTime() + 20);

        assertTrue(dataPackage.getBufferOutTime() > dataPackage.getBufferInTime());
        assertTrue(dataPackage.getEndTime() > dataPackage.getBufferOutTime());
    }

    @Test
    public void testNodeBufferOverflow() throws InterruptedException {
        Node node = new Node(0, 0, 2, ringProcessor.getLogger());

        for (int i = 0; i < 10; ++i) {
            node.addData(new DataPackage(0, "Test data"));
        }

        assertEquals(10, node.getBufferStack().size());
        executorService.submit(node);
        Thread.sleep(100);
        executorService.shutdownNow();

        assertEquals(0, node.getBufferStack().size());
    }

    @Test
    void testRingProcessorDelivery() {
        List<Integer> dataAmount = Arrays.asList(3, 4);
        List<Integer> targetNodes = Arrays.asList(0, 1);

        RingProcessor ringProcessor = new RingProcessor(2, dataAmount, targetNodes, TEST_LOG);

        ringProcessor.startProcessing();

        assertEquals(7, ringProcessor.getAllData().size());
    }

    @Test
    void testNodeExceptions() {
        Node node = new Node(0, 0, 1, ringProcessor.getLogger());

        Thread nodeThread = new Thread(node);
        nodeThread.start();
        nodeThread.interrupt();

        assertTrue(nodeThread.isInterrupted());
    }

    private RingProcessor ringProcessor;
    private static final File TEST_LOG = new File("test_log.log");
    ExecutorService executorService = Executors.newCachedThreadPool();
}
