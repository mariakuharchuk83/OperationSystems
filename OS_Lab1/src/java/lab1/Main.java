package lab1;

import java.util.Optional;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        int x = lab1.Utils.getX();
        lab1.Manager manager = new lab1.Manager();
        long startTime = System.currentTimeMillis();
        manager.run(x, new lab1.ConcurrentBiConsumer<Optional<Double>, Manager.Status>() {

            @Override
            public void acceptOnce(Optional<Double> result, lab1.Manager.Status status) {
                long duration = System.currentTimeMillis() - startTime;
                System.out.println("Result: F(x) * G(x) = " + (result.isPresent() ? result.get() : "undefined"));
                System.out.println("Status: " + status.name());
                System.out.println("(took " + duration + " ms)");
                System.exit(0);
            }
        });
    }
}
