package org.example;

import java.util.Scanner;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MultiThreadedConversation {
    public static void main(String[] args) {
        Lock lock = new ReentrantLock();

        Friend rahim = new Friend("Rahim", lock);
        Friend bivour = new Friend("Bivour", lock);
        Friend kareem = new Friend("Kareem", lock);

        Thread t1 = new Thread(rahim);
        Thread t2 = new Thread(bivour);
        Thread t3 = new Thread(kareem);

        t1.start();
        t2.start();
        t3.start();
    }
}

class Friend implements Runnable {
    private String name;
    private Lock lock;
    private static boolean running = true;

    public Friend(String name, Lock lock) {
        this.name = name;
        this.lock = lock;
    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);

        while (running) {
            lock.lock();
            try {
                if (!running) break;

                System.out.print(name + ": ");
                String message = scanner.nextLine();

                if (message.equalsIgnoreCase("Bye")) {
                    System.out.println(name + " has left the conversation.");
                    running = false;
                } else {
                    System.out.println(name + ": " + message);
                }
            } finally {
                lock.unlock();
                try {
                    // Give a little time for other threads
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
