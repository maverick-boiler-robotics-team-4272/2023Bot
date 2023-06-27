package frc.robot.utils;

public class ExponentialAverage {
    private static class DataNode {
        public final double data;
        public DataNode next;
        public DataNode prev;

        public DataNode(double data) {
            this.data = data;
            this.next = null;
            this.prev = null;
        }
    }

    private final int capacity;
    private final double ratio;
    private int currentSize;
    private DataNode head;
    private DataNode tail;


    public ExponentialAverage(double ratio, int capacity) {
        this.ratio = ratio;
        this.capacity = capacity;

        this.currentSize = 0;
        this.head = null;
        this.tail = null;
    }

    public double compute() {
        double sum = 0;
        double scale = (1 - ratio) / (1 - Math.pow(ratio, currentSize));

        DataNode current = head;
        for(int i = 0; i < currentSize; i++) {
            sum += current.data * Math.pow(ratio, i);
            current = current.next;
        }

        return sum * scale;
    }

    public double update(double current) {
        if(head == null) {
            head = new DataNode(current);
            tail = head;
            currentSize++;

            return current;
        } else if(currentSize == capacity) {
            DataNode t = tail;
            tail = t.prev;

            t.next = null;
            t.prev = null;
            tail.next = null;

            currentSize--;
        }

        DataNode h = head;
        head = new DataNode(current);
        h.prev = head;
        head.next = h;
        currentSize++;

        return compute();
    }
}
