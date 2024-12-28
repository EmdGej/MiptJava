package tokenring;

import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Node implements Runnable {
    Node(int nodeId, int coordinatorId, int handlingPower, Logger logger) {
        this.nodeId = nodeId;
        this.coordinatorId = coordinatorId;
        this.logger = logger;
        this.semaphore = new Semaphore(handlingPower);

        if (nodeId == coordinatorId) {
            allData = new CopyOnWriteArrayList<>();
        }
    }

    public  BlockingQueue<DataPackage> getBufferStack() {
        return bufferStack;
    }

    public ExecutorService getExecutorService() {
        return executorService;
    }

    @Override
    public void run() {
        while (true) {
            try {
                DataPackage dataPackage = bufferStack.take();
                semaphore.acquire();
                executorService.submit(() -> {
                    try {
                        dataPackage.setBufferOutTime(System.nanoTime());
                        Thread.sleep(HANDLE_PACKAGE_TIME);
                    } catch (InterruptedException e) {
                        logger.log(Level.SEVERE, "Work has been interrupted. Node ID: " + nodeId);
                        Thread.currentThread().interrupt();
                        return;
                    }

                    if (dataPackage.getDestinationNode() == nodeId) {
                        dataPackage.setEndTime(System.nanoTime());
                        logger.info("Node " + nodeId + " got data package as finish point");

                        Node coordinator = this;
                        while (coordinator.nodeId != coordinatorId) {
                            coordinator = coordinator.nextNode;
                        }

                        logger.info("Node " + nodeId + " forwarded data to coordinator Node " + coordinatorId);
                        coordinator.addCoordinatorData(dataPackage);
                    } else {
                        logger.info("Node " + nodeId + " forwarded data to Node " + nextNode.nodeId);
                        nextNode.addData(dataPackage);
                    }

                    semaphore.release();
                });
            } catch (InterruptedException e) {
                logger.log(Level.SEVERE, "Work has been interrupted. Node ID: " + nodeId);
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    public void addData(DataPackage dataPackage) {
        try {
            bufferStack.put(dataPackage);
            dataPackage.setBufferInTime(System.nanoTime());
        } catch (InterruptedException e) {
            logger.log(Level.SEVERE, "Work has been interrupted. Node ID: " + nodeId);
            Thread.currentThread().interrupt();
        }
    }

    void addCoordinatorData(DataPackage dataPackage) {
        allData.add(dataPackage);
        dataPackage.setDelivered(true);
    }

    public void setNextNode(Node nextNode) {
        this.nextNode = nextNode;
    }

    public CopyOnWriteArrayList<DataPackage> getAllData() {
        return allData;
    }

    private final int nodeId;
    private final int coordinatorId;
    private final Logger logger;
    private final Semaphore semaphore;

    private static final int HANDLE_PACKAGE_TIME = 1;

    private Node nextNode;

    private final BlockingQueue<DataPackage> bufferStack = new LinkedBlockingQueue<>();
    private CopyOnWriteArrayList<DataPackage> allData;
    private final ExecutorService executorService = Executors.newCachedThreadPool();
}
