/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package asynchronous;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.*;

/**
 *
 * @author jesse
 */
public class AsyncController<T> {
    private final static long LISTENER_TIMEOUT_MILLISECONDS = 1;
    private final static int LISTENER_TIMEOUT_NANOSECONDS = 0;
    private final Queue<Task<T>> executionQueue = new ConcurrentLinkedQueue<>();
    
    public Promise<T> later(Task<T> task){
        executionQueue.add(task);
        return task.getPromise();
    }
    public Promise<T> later(Supplier<Promise<T>> work){
        return later(new Task<T>(work));
    }
    public Promise<T> later(Promise<T> outsideWork){
        return later(new Task<T>(outsideWork));
    }
    public T await(Task<T> task) {
            var promise = later(task);
            while(!promise.isResolved()){
                boolean isEmpty;
                synchronized(this){
                    if (!(isEmpty = executionQueue.isEmpty())){
                        executionQueue.poll().run();
                    }
                }
                
                if (isEmpty) {
                    try{
                        Thread.sleep(LISTENER_TIMEOUT_MILLISECONDS, LISTENER_TIMEOUT_NANOSECONDS);
                    } catch(InterruptedException e) {
                        System.exit(1);
                    }
                }

        }
            return promise.getResult();
    }
    public T await(Supplier<Promise<T>> work){
        return await(new Task<T>(work));
    }
    public T await(Promise<T> outsideWork){
        return await(new Task<T>(outsideWork));
    }
    
    public  void execute(){
        synchronized(this){
            while(!executionQueue.isEmpty()){
                executionQueue.poll().run();
            }
        }
    }
}
