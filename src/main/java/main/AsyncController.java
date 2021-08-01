/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;
import java.util.Queue;
import java.util.LinkedList;
import java.util.function.*;

/**
 *
 * @author jesse
 */
public class AsyncController<T> {
    private final static int LISTENER_TIMEOUT_MILLISECONDS = 1;
    private Queue<Task<T>> executionQueue = new LinkedList<>();
    
    public Promise<T> later(Supplier<Promise<T>> func){
        Task<T> task = new Task(func);
        executionQueue.add(task);
        return task.getPromise();
    }
    
    public T await(Supplier<Promise<T>> func) {
        var promise = later(func);
        while(!promise.isResolved()){
            if (!executionQueue.isEmpty()){
                Task<T> toRun = executionQueue.poll();
                toRun.run();
            }
            else{
                try{
                    Thread.sleep(LISTENER_TIMEOUT_MILLISECONDS);
                }
                catch(InterruptedException e){}
            }
        }
        return promise.getResult();
    }
    
    public void execute(){
        while(!executionQueue.isEmpty()){
            Task<T> toRun = executionQueue.poll();
            toRun.run();
        }
    }
}
