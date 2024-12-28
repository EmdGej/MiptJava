package portsystem;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        ExecutorService portSystem = Executors.newCachedThreadPool();

        BlockingQueue<Ship> breadQueue = new LinkedBlockingQueue<>();
        BlockingQueue<Ship> bananaQueue = new LinkedBlockingQueue<>();
        BlockingQueue<Ship> clothesQueue = new LinkedBlockingQueue<>();

        Dock breadDock = new Dock(breadQueue);
        Dock bananaDock = new Dock(bananaQueue);
        Dock clothesDock = new Dock(clothesQueue);

        BlockingQueue<Ship> tunnelQueue = new LinkedBlockingQueue<>();
        ShipGenerator shipGenerator = new ShipGenerator(tunnelQueue, 10000);
        Tunnel portTunnel = new Tunnel(tunnelQueue, breadQueue, bananaQueue, clothesQueue);

        portSystem.submit(shipGenerator::generateShips);
        portSystem.submit(portTunnel::processShips);

        portSystem.submit(breadDock::loadShips);
        portSystem.submit(bananaDock::loadShips);
        portSystem.submit(clothesDock::loadShips);

        portSystem.shutdown();
    }
}
