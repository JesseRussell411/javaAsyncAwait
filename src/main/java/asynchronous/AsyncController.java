/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package asynchronous;
import java.util.LinkedList;
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
            Task<T> job = executionQueue.poll();
            if (job != null){
                job.run();
            }
            else {
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
    private Object executeLock = new Object();
    public void execute(){
        synchronized(executeLock){
            Queue<Task<T>> jobs = new LinkedList<Task<T>>();
            Task<T> job;
            while((job = executionQueue.poll()) != null){
                job.run();
                jobs.add(job);
            }

            while((job = jobs.poll()) != null){
                await(job);
            }
        }
    }
}
