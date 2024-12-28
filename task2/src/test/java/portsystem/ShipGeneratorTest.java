package portsystem;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import org.junit.jupiter.api.Test;

class ShipGeneratorTest {
    @Test
    void testShipGeneration() throws InterruptedException {
        BlockingQueue<Ship> tunnelQueue = new LinkedBlockingQueue<>();
        ShipGenerator shipGenerator = new ShipGenerator(tunnelQueue, 100);

        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.submit(shipGenerator::generateShips);
        Thread.sleep(1000);
        executorService.shutdownNow();

        assertEquals(10, tunnelQueue.size());
    }
}
