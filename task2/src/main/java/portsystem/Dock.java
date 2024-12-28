package portsystem;

import java.util.concurrent.BlockingQueue;

public class Dock {
    public Dock(BlockingQueue<Ship> dockQueue) {
        this.dockQueue = dockQueue;
    }

    public void loadShips() {
        while (true) {
            try {
                Ship ship = dockQueue.take();
                int timeToLoad = ship.getShipCapacity() / LOAD_FACTOR;

                System.out.printf("***START LOADING %s ID %d***: Start loading ship with params {%s, %d}\n",
                        ship.getShipType(), ship.getShipId(), ship.getShipType(), ship.getShipCapacity());
                Thread.sleep(timeToLoad * 1000L);
                System.out.printf("***END LOADING %s ID %d***: End loading ship with params {%s, %d}\n", ship.getShipType(),
                        ship.getShipId(), ship.getShipType(), ship.getShipCapacity());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("***INTERRUPTION***: Loading has been stopped");
                break;
            }
        }
    }

    private final BlockingQueue<Ship> dockQueue;
    private static final int LOAD_FACTOR = 10;
}
