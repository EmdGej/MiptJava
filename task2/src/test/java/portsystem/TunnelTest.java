package portsystem;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import org.junit.jupiter.api.Test;

class TunnelTest {
    @Test
    public void testProcessShips() throws InterruptedException {
        BlockingQueue<Ship> tunnelQueue = new LinkedBlockingQueue<>();
        BlockingQueue<Ship> breadQueue = new LinkedBlockingQueue<>();
        BlockingQueue<Ship> bananaQueue = new LinkedBlockingQueue<>();
        BlockingQueue<Ship> clothesQueue = new LinkedBlockingQueue<>();

        Tunnel portTunnel = new Tunnel(tunnelQueue, breadQueue, bananaQueue, clothesQueue);
        ShipGenerator shipGenerator = new ShipGenerator(tunnelQueue, 100);

        ExecutorService executorService = Executors.newCachedThreadPool();

        executorService.submit(shipGenerator::generateShips);
        executorService.submit(portTunnel::processShips);

        Thread.sleep(2000);

        assertEquals(9, tunnelQueue.size());
    }
}
