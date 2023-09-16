import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    private static final int MAX_INT = 100000000;
    private static final int CONCURRENCY = 10;
    private static AtomicInteger totalPrimeNumbers = new AtomicInteger(0);
    private static AtomicInteger currentNum = new AtomicInteger(2);

    private static void checkPrime(int x) {
        if ((x & 1) == 0) {
            return;
        }
        for (int i = 3; i <= Math.sqrt(x); i++) {
            if (x % i == 0) {
                return;
            }
        }
        totalPrimeNumbers.incrementAndGet();
    }

    private static void doWork(String name, AtomicInteger remainingThreads, int index) {
        long start = System.currentTimeMillis();

        while (true) {
            int x = currentNum.incrementAndGet();
            if (x > MAX_INT) {
                break;
            }
            checkPrime(x);
        }

        System.out.printf("Thread %s completed in %d ms%n", name,( System.currentTimeMillis() - start)/1000);
        if (remainingThreads.decrementAndGet() == 0) {
            System.out.printf("Checking till %d found %d prime numbers. Took %d ms%n", MAX_INT, totalPrimeNumbers.get() + 1, ( System.currentTimeMillis() - start)/1000);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        long start = System.currentTimeMillis();

        Thread[] threads = new Thread[CONCURRENCY];
        AtomicInteger remainingThreads = new AtomicInteger(CONCURRENCY);

        for (int i = 0; i < CONCURRENCY; i++) {
            final int index = i;
            threads[i] = new Thread(() -> doWork(Integer.toString(index), remainingThreads, index));
            threads[i].start();
        }

        for (Thread thread : threads) {
            thread.join();
        }
    }
}
