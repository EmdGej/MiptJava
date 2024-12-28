package tokenring;

public class DataPackage {
    DataPackage(int destinationNode, String data) {
        this.destinationNode = destinationNode;
        this.data = data;
        startTime = System.nanoTime();
    }

    public int getDestinationNode() {
        return destinationNode;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public long getBufferInTime() {
        return bufferInTime;
    }

    public void setBufferInTime(long bufferInTime) {
        this.bufferInTime = bufferInTime;
    }

    public long getBufferOutTime() {
        return bufferOutTime;
    }

    public void setBufferOutTime(long bufferOutTime) {
        this.bufferOutTime = bufferOutTime;
    }

    public boolean isDelivered() {
        return isDelivered;
    }

    public void setDelivered(boolean delivered) {
        isDelivered = delivered;
    }

    public String getData() {
        return data;
    }

    private final int destinationNode;
    private final String data;
    private final long startTime;

    private long endTime;
    private long bufferInTime;
    private long bufferOutTime;
    volatile private boolean isDelivered = false;
}
