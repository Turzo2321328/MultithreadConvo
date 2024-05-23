import java.util.Scanner;

class Friend implements Runnable {
    private String name;
    private static volatile boolean isActive = true;
    private static final Object lock = new Object();
    private static String lastSpeaker = "";
    private static Scanner scanner = new Scanner(System.in);

    public Friend(String name) {
        this.name = name;
    }

    public void run() {
        while (isActive) {
            synchronized (lock) {
                if (lastSpeaker.equals(name) || !isActive) {
                    continue;
                }
                System.out.print(name + ": ");
                String message = scanner.nextLine();
                if (message.equalsIgnoreCase("Bye")) {
                    isActive = false;
                    System.out.println(name + " has left the conversation.");
                }
                lastSpeaker = name;
                lock.notifyAll();
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.out.println(name + " was interrupted.");
                }
            }
        }
    }

    public static void main(String[] args) {
        Friend friend1 = new Friend("Rahim");
        Friend friend2 = new Friend("Bivour");
        Friend friend3 = new Friend("Kareem");

        Thread thread1 = new Thread(friend1);
        Thread thread2 = new Thread(friend2);
        Thread thread3 = new Thread(friend3);

        thread1.start();
        thread2.start();
        thread3.start();

        try {
            thread1.join();
            thread2.join();
            thread3.join();
        } catch (InterruptedException e) {
            System.out.println("Main thread interrupted.");
        }

        System.out.println("Conversation ended.");
        scanner.close();
    }
}
