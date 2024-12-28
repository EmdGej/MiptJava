package portsystem;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Semaphore;

public class Tunnel {
    public Tunnel(BlockingQueue<Ship> tunnelQueue, BlockingQueue<Ship> breadQueue, BlockingQueue<Ship> bananaQueue,
                  BlockingQueue<Ship> clothesQueue) {
        this.tunnelQueue = tunnelQueue;
        this.breadQueue = breadQueue;
        this.bananaQueue = bananaQueue;
        this.clothesQueue = clothesQueue;
    }

    public void processShips() {
        while (true) {
            try {
                Ship ship = tunnelQueue.take();
                semaphore.acquire();

                ship.getSemaphoreFromTunnel(semaphore);
                ship.setCanEnterTunnel(true);

                if (ship.getShipType() == Ship.ShipType.BREAD) {
                    ship.getQueueFromDock(breadQueue);
                } else if (ship.getShipType() == Ship.ShipType.BANANAS) {
                    ship.getQueueFromDock(bananaQueue);
                } else {
                    ship.getQueueFromDock(clothesQueue);
                }

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    private static final int TUNNEL_CAPACITY = 5;

    private final BlockingQueue<Ship> tunnelQueue;
    private final BlockingQueue<Ship> breadQueue;
    private final BlockingQueue<Ship> bananaQueue;
    private final BlockingQueue<Ship> clothesQueue;

    private final Semaphore semaphore = new Semaphore(TUNNEL_CAPACITY, true);
}
