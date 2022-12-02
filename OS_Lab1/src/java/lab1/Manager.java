package lab1;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;

import static lab1.Utils.runMenuEsc;

public class Manager {
    private static final int MENU_DELAY = 1000;
    private static boolean prompt = true;
    private static final Scanner input = new Scanner(System.in);
    private static final int N = 2;
    private static final ArrayList<Double> receivedResults = new ArrayList<>();

    public enum Status {
        SHORT_CIRCUIT, SUCCESS, UNDEFINED, CANCELLED
    }

    private static final double ZERO_VALUE = 0;

    public Manager() { }

    public void run(int x, BiConsumer<Optional<Double>, Status> onCalculated) throws InterruptedException {
        RunningThread threadF = new RunningThread("F", x);
        threadF.start();
        RunningThread threadG = new RunningThread("G", x);
        threadG.start();
//        threadF.join();
//        threadG.join();
        defineResult(threadF, threadG, onCalculated);
    }

    private void defineResult(RunningThread threadF, RunningThread threadG,  BiConsumer<Optional<Double>, Status> onCalculated) throws InterruptedException {
        AtomicInteger counter = new AtomicInteger(0);
        while (true) {
            if(threadF.calculated && threadF.notUsed){
                double inputDouble = threadF.result.get();
                if (Double.compare(inputDouble, ZERO_VALUE) == 0) {
                    if (counter.get() < N - 1) {
                        onCalculated.accept(Optional.of(ZERO_VALUE), Status.SHORT_CIRCUIT);
                    } else {
                        onCalculated.accept(Optional.of(ZERO_VALUE), Status.SUCCESS);
                    }
                    closeAll(threadF, threadG);
                    return;
                } else if (Double.isNaN(inputDouble)) {
                    onCalculated.accept(Optional.empty(), Status.UNDEFINED);
                    closeAll(threadF, threadG);
                    return;
                }
                receivedResults.add(inputDouble);
                threadF.notUsed = false;
                if (counter.incrementAndGet() == N) {
                    calculateResult(onCalculated);
                    closeAll(threadF, threadG);
                }
            }
            if(threadG.calculated && threadG.notUsed){
                double inputDouble = threadG.result.get();
                if (Double.compare(inputDouble, ZERO_VALUE) == 0) {
                    if (counter.get() < N - 1) {
                        onCalculated.accept(Optional.of(ZERO_VALUE), Status.SHORT_CIRCUIT);
                    } else {
                        onCalculated.accept(Optional.of(ZERO_VALUE), Status.SUCCESS);
                    }
                    closeAll(threadF, threadG);
                    return;
                } else if (Double.isNaN(inputDouble)) {
                    onCalculated.accept(Optional.empty(), Status.UNDEFINED);
                    closeAll(threadF, threadG);
                    return;
                }

                receivedResults.add(inputDouble);
                threadG.notUsed = false;
                if (counter.incrementAndGet() == N) {
                    calculateResult(onCalculated);
                    closeAll(threadF, threadG);
                }
            }
            runMenu(threadF, threadG, onCalculated);
        }
    }

    private static void calculateResult(BiConsumer<Optional<Double>, Status> onCalculated) {
        double endResult = 1;
        for (double r : Manager.receivedResults) {
            endResult *= r;
            if (r == 0) {
                throw new AssertionError("No zeros should be in results");
            }
        }
        onCalculated.accept(Optional.of(endResult), Status.SUCCESS);
    }

    public static void closeAll(RunningThread threadF, RunningThread threadG) {
        threadF.interrupt();
        threadG.interrupt();
    }

    public void runMenu(RunningThread threadF, RunningThread threadG, BiConsumer<Optional<Double>, Status> onCalculated){
        long start = System.currentTimeMillis();
        long end;
        AtomicBoolean promptWorking = new AtomicBoolean(true);
        runMenuEsc();
        try {
            Thread.sleep(MENU_DELAY);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        synchronized (promptWorking) {
            if (prompt) {
                short answer;
                synchronized (System.out) {
                    end = System.currentTimeMillis();
                    System.out.println("Time between prompts: " + (end - start) + "ms");
                    System.out.println("1. Continue\n" +
                            "2. Continue without prompt\n" +
                            "3. Cancel\n"
                    );
                    answer = input.nextShort();
                    input.nextLine();
                }
                if (answer == 1) {
                    start = System.currentTimeMillis();
                } else if (answer == 2) {
                    prompt = false;
                    promptWorking.set(false);
                } else if (answer == 3) {
                    if (threadF.calculated && threadG.calculated) {
                        calculateResult(onCalculated);
                    }
                    else {
                        onCalculated.accept(Optional.empty(), Status.CANCELLED);
                    }
                }
            }
        }
    }
}
