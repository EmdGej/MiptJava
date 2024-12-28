package portsystem;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Semaphore;

public class Ship implements Runnable {
    public Ship(ShipType shipType, int capacity) {
        this.shipType = shipType;
        this.shipCapacity = capacity;
        this.shipId = ++shipsAmount;
    }

    public ShipType getShipType() {
        return shipType;
    }

    public int getShipCapacity() {
        return shipCapacity;
    }

    public boolean getCanEnterTunnel() {
        return canEnterTunnel;
    }

    public void setCanEnterTunnel(boolean canEnterTunnel) {
        this.canEnterTunnel = canEnterTunnel;
    }

    void getSemaphoreFromTunnel(Semaphore semaphore) {
        this.semaphore = semaphore;
    }

    void getQueueFromDock(BlockingQueue<Ship> dockQueue) {
        this.dockQueue = dockQueue;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                if (canEnterTunnel) {
                    System.out.printf("***ENTER TUNNEL ID: %d***: Ship with params {%s, %d} has entered tunnel\n",
                            shipId, shipType, shipCapacity);
                    Thread.sleep(TUNNEL_PASS_TIME);
                    System.out.printf("***EXIT TUNNEL ID %d***: Ship with params {%s, %d} has exited tunnel\n", shipId,
                            shipType, shipCapacity);

                    canEnterTunnel = false;
                    if (semaphore != null) {
                        semaphore.release();
                    }

                    if (dockQueue != null) {
                        dockQueue.put(this);
                    }

                    break;
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.printf("***INTERRUPTION ID %d***: Ship has been disappeared\n", shipId);
                break;
            }
        }
    }

    public long getShipId() {
        return shipId;
    }

    public enum ShipType { BREAD, BANANAS, CLOTHES }

    private final ShipType shipType;
    private final int shipCapacity;
    private static final int TUNNEL_PASS_TIME = 1000;
    private static long shipsAmount = 0;
    private volatile long shipId;

    private boolean canEnterTunnel = false;
    private Semaphore semaphore = null;
    private BlockingQueue<Ship> dockQueue = null;
}
