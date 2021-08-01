/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;
import java.util.function.*;

/**
 *
 * @author jesse
 */
public class Task<T>{
    private Supplier<Promise<T>> work;
    private Consumer<T> resolve;
    private boolean started = false;
    private Promise<T> promise;
    public Promise<T> getPromise() { return promise; }
    public boolean hasStarted() { return started; }
    public boolean isComplete() { return promise.isResolved(); }
    public boolean isWorking() { return started && !isComplete(); }
    
    public Promise<T> run(){
        if (!started){
            work.get().then((r) -> { resolve.accept(r); });
        }
        started = true;
        return promise;
    }
    
    public Task(Supplier<Promise<T>> work){
        this.work = work;
        promise = new Promise<T>((resolve) -> {this.resolve = resolve;});
    }
    
    public Task(Promise<T> outsideWork){
        started = true;
        promise = outsideWork;
    }
}
