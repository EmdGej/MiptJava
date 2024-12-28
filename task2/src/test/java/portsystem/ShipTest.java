package portsystem;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.*;
import org.junit.jupiter.api.Test;

public class ShipTest {
    @Test
    public void testShipInitialization() {
        Ship ship = new Ship(Ship.ShipType.BREAD, 50);

        assertEquals(Ship.ShipType.BREAD, ship.getShipType());
        assertEquals(50, ship.getShipCapacity());
        assertTrue(ship.getShipId() > 0);
    }

    @Test
    public void testRunMethod() throws InterruptedException, ExecutionException {
        Ship ship = new Ship(Ship.ShipType.BREAD, 50);

        Semaphore semaphore = new Semaphore(1);
        BlockingQueue<Ship> queue = new LinkedBlockingQueue<>();

        ship.getSemaphoreFromTunnel(semaphore);
        ship.getQueueFromDock(queue);
        ship.setCanEnterTunnel(true);

        semaphore.acquire();

        ExecutorService executorService = Executors.newCachedThreadPool();
        Future<?> future = executorService.submit(ship);
        future.get();

        assertEquals(1, semaphore.availablePermits());
        assertEquals(1, queue.size());
    }

    @Test
    public void testRunInterrupted() throws InterruptedException {
        Ship ship = new Ship(Ship.ShipType.BREAD, 50);

        Thread thread = new Thread(ship);
        thread.start();
        thread.interrupt();

        assertTrue(thread.isInterrupted());
    }
}
