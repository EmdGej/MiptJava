package tokenring;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class RingProcessor {
    RingProcessor(int nodesAmount, List<Integer> dataAmount, List<Integer> targetNodes, File logs) {
        this.nodesAmount = nodesAmount;
        this.dataAmount = dataAmount;
        this.logs = logs;
        this.logger = Logger.getLogger("TokenRingLogger");
        this.executorService = Executors.newCachedThreadPool();

        try {
            setupLogger();
        } catch (IOException e) {
            return;
        }

        init(targetNodes);
    }

    private void setupLogger() throws IOException {
        logger.setUseParentHandlers(false);
        FileHandler fileHandler = new FileHandler(logs.getAbsolutePath());
        fileHandler.setFormatter(new SimpleFormatter());
        logger.addHandler(fileHandler);
    }

    private void init(List<Integer> targetNodes) {
        for (int i = 0; i < nodesAmount; ++i) {
            nodeList.add(new Node(i, 0, HANDLE_POWER, logger));
        }

        for (int i = 0; i < nodesAmount; ++i) {
            nodeList.get(i).setNextNode(nodeList.get((i + 1) % nodesAmount));
        }

        for (int i = 0; i < nodesAmount; ++i) {
            for (int j = 0; j < dataAmount.get(i); ++j) {
                DataPackage dataPackage = new DataPackage(targetNodes.get(i), "some data");
                dataPackages.add(dataPackage);
                nodeList.get(i).addData(dataPackage);
            }
        }

        logger.info("TokenRing initialized with " + nodesAmount + " nodes:");
        for (int i = 0; i < nodesAmount; ++i) {
            logger.info(dataAmount.get(i) + " data packages for Node " + i);
        }
    }

    public void startProcessing() {
        for (Node node : nodeList) {
            executorService.submit(node);
        }

        boolean packageForwarding = true;
        while (packageForwarding) {
            packageForwarding = false;
            for (DataPackage dataPackage : dataPackages) {
                if (!dataPackage.isDelivered()) {
                    packageForwarding = true;
                    break;
                }
            }
        }

        executorService.shutdownNow();
        for (Node node: nodeList) {
            node.getExecutorService().shutdownNow();
        }

        calculateAverageTimes();
    }

    private void calculateAverageTimes() {
        long allTimeInNetwork = 0;
        long allTimeInBuffer = 0;

        for (DataPackage dataPackage : dataPackages) {
            allTimeInNetwork += dataPackage.getEndTime() - dataPackage.getStartTime();
            allTimeInBuffer += dataPackage.getBufferOutTime() - dataPackage.getBufferInTime();
        }

        if (!dataPackages.isEmpty()) {
            logger.info("Average delivery time: " + (allTimeInNetwork / dataPackages.size()) + " ns");
            logger.info("Average buffer staying time: " + (allTimeInBuffer / dataPackages.size()) + " ns");
        }
    }

    public Logger getLogger() {
        return logger;
    }

    public CopyOnWriteArrayList<DataPackage> getAllData() {
        return nodeList.getFirst().getAllData();
    }

    private static final int HANDLE_POWER = 2;

    private final int nodesAmount;
    private final List<Integer> dataAmount;

    private final File logs;
    private final Logger logger;
    private final ExecutorService executorService;
    private final List<Node> nodeList = new ArrayList<>();
    private final List<DataPackage> dataPackages = new ArrayList<>();
}
