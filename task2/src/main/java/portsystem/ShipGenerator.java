package portsystem;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

public class ShipGenerator {
    public ShipGenerator(BlockingQueue<Ship> tunnelQueue, int generationRate) {
        this.tunnelQueue = tunnelQueue;
        this.generationRate = generationRate;
    }

    public ShipGenerator(BlockingQueue<Ship> tunnelQueue) {
        this.tunnelQueue = tunnelQueue;
    }

    public void generateShips() {
        while (true) {
            try {
                Ship ship =
                        new Ship(Ship.ShipType.values()[ThreadLocalRandom.current().nextInt(Ship.ShipType.values().length)],
                                shipCapacity[ThreadLocalRandom.current().nextInt(shipCapacity.length)]);

                tunnelQueue.put(ship);
                executorService.execute(ship);

                System.out.printf("***CREATION ID %d***: Ship with params {%s, %d} has been generated\n",
                        ship.getShipId(), ship.getShipType(), ship.getShipCapacity());

                Thread.sleep(generationRate);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("***INTERRUPTION***: Ships generation has been stopped");
                executorService.shutdown();
                break;
            }
        }
    }

    private int generationRate = 1000;

    private final BlockingQueue<Ship> tunnelQueue;
    private final ExecutorService executorService = Executors.newCachedThreadPool();
    private final int[] shipCapacity = {10, 50, 100};
}
