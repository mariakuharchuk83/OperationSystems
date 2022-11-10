package lab1;

import os.lab1.compfuncs.advanced.DoubleOps;

import java.util.Optional;
import java.util.OptionalDouble;
import java.util.logging.Level;
import java.util.logging.Logger;

import static os.lab1.compfuncs.advanced.DoubleOps.*;

public class RunningThread extends Thread{
    private static final Logger log = Logger.getLogger(RunningThread.class.getName());
    private final String functionName;
    public Optional<Double> result;
    private final int testCase;
    public boolean calculated = false;
    public boolean notUsed = true;

    public RunningThread(String functionName, int testCase) {
        this.functionName = functionName;
        this.testCase = testCase;
    }

    @Override
    public void run() {
        try{
            if(functionName.equals("F")){

                result = trialF(testCase).get();
                synchronized (this){
                    if(result.isPresent()){
                        calculated = true;
                    }
                }
            }
            else if(functionName.equals("G")){
                result = trialG(testCase).get();
                synchronized (this){
                    if(result.isPresent()){
                        calculated = true;
                    }
                }
            }
        } catch (InterruptedException ex){
            log.log(Level.SEVERE, "Exception: ", ex);
        }
    }
}
