package portsystem;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.concurrent.*;
import org.junit.jupiter.api.Test;

class DockTest {
    @Test
    void testLoadShips() throws InterruptedException, ExecutionException {
        BlockingQueue<Ship> breadQueue = new LinkedBlockingQueue<>();
        Dock dock = new Dock(breadQueue);

        Ship ship = new Ship(Ship.ShipType.BREAD, 50);
        breadQueue.put(ship);

        ExecutorService executorService = Executors.newCachedThreadPool();
        Future<?> future = executorService.submit(dock::loadShips);
        Thread.sleep(6000);

        assertEquals(0, breadQueue.size());
    }

    @Test
    void testDockHandlesEmptyQueue() throws InterruptedException {
        LinkedBlockingQueue<Ship> breadQueue = new LinkedBlockingQueue<>();
        Dock dock = new Dock(breadQueue);

        ExecutorService executorService = Executors.newCachedThreadPool();
        Future<?> future = executorService.submit(dock::loadShips);
        Thread.sleep(500);
        executorService.shutdownNow();

        assertEquals(0, breadQueue.size());
    }
}
