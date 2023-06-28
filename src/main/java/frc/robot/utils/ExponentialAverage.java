package frc.robot.utils;

public class ExponentialAverage {
    private static class DataNode {
        public double data;
        public DataNode next;
        public DataNode prev;

        public DataNode(double data) {
            this.data = data;
            this.next = null;
        }
    }

    private final int capacity;
    private final double ratio;
    private int currentSize;
    private DataNode head;


    public ExponentialAverage(double ratio, int capacity) {
        this.ratio = ratio;
        this.capacity = capacity;

        this.currentSize = 0;
        this.head = new DataNode(0);

        DataNode tail = null;
        DataNode current = head;

        for(int i = 1; i < capacity; i++) {
            tail = new DataNode(0);
            current.next = tail;
            tail.prev = current;
            current = tail;
        }

        tail.next = this.head;
        this.head.prev = tail;
    }

    public double compute() {
        double sum = 0;
        double scale = (1 - ratio) / (1 - Math.pow(ratio, currentSize));

        DataNode current = head;
        for(int i = 0; i < currentSize; i++) {
            sum += current.data * Math.pow(ratio, i);
            current = current.prev;
        }

        return sum * scale;
    }

    public double update(double current) {
        if(currentSize != capacity) {
            currentSize++;
        }

        head.next.data = current;
        head = head.next;

        return compute();
    }
}
