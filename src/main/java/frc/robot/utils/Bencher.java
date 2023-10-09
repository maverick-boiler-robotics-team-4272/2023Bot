package frc.robot.utils;

import java.util.Random;

public class Bencher {
    private static interface ExpAvg {
        public void update(double val);
        public double compute(); 
    }

    private static class ExpAvgLinked implements ExpAvg {
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
    
    
        public ExpAvgLinked(double ratio, int capacity) {
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
    
        @Override
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

        @Override
        public void update(double current) {
            if(currentSize != capacity) {
                currentSize++;
            }
    
            head.next.data = current;
            head = head.next;
    
            // return compute();
        }
    }

    private static class ExpAvgArr implements ExpAvg {
        private double[] buffer;
        private int size;
        private int front;

        private final int capacity;
        private final double ratio;

        public ExpAvgArr(double ratio, int capacity) {
            this.buffer = new double[capacity];
            this.size = 0;
            this.front = 0;
            
            this.capacity = capacity;
            this.ratio = ratio;
        }

        @Override
        public void update(double val) {
            front = (front + 1) % capacity;
            if(size != capacity)
                size++;

            buffer[front] = val;

            // return compute();
        }

        @Override
        public double compute() {
            double sum = 0;
            double scale = (1 - ratio) / (1 - Math.pow(ratio, size));
    
            for(int i = 0; i < size; i++) {
                sum += buffer[(front - i + capacity) % capacity] * Math.pow(ratio, i);
            }
    
            return sum * scale;
        }
    }

    public static void main(String[] args) {
        long startTime = 0, endTime = 0;

        long linkedTime = 0, arrayTime = 0;

        Random r = new Random();

        ExpAvg avg = null;

        avg = new ExpAvgLinked(0.5, 30);
        r.setSeed(10);

        startTime = System.nanoTime();

        for(int i = 0; i < 10000000; i++) {
            avg.update(r.nextDouble(30));
        }

        endTime = System.nanoTime();

        linkedTime = endTime - startTime;

        System.out.printf("Linked List: %.3f ms\n", linkedTime / 1000000.0);

        avg = new ExpAvgArr(0.5, 30);
        r.setSeed(10);

        startTime = System.nanoTime();

        for(int i = 0; i < 10000000; i++) {
            avg.update(r.nextDouble(30));
        }

        endTime = System.nanoTime();

        arrayTime = endTime - startTime;

        System.out.printf("Array: %.3f ms\n", arrayTime / 1000000.0);

        if(linkedTime < arrayTime) {
            System.out.printf("Linked List faster by %.3f ms\n", (arrayTime - linkedTime) / 1000000.0);
        } else {
            System.out.printf("Array faster by %.3f ms\n", (linkedTime - arrayTime) / 1000000.0);
        }
    }
}
