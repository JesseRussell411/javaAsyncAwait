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
public class Promise<T> {
    private Promise<T> nextPromise = null;
    private Function<T, T> nextTask = null;
    private T result;
    private boolean resolved = false;
    public boolean isResolved() { return resolved; }
    public T getResult() { return result; }
    
    public Promise(){}
    
    public Promise(Consumer<Consumer<T>> func){
        func.accept((t) -> resolve(t));
    }
    
    private void runNextTask(){
        if (nextTask != null){
            nextPromise.resolve(nextTask.apply(result));
        }
    }
    
    public Promise<T> then(Function<T, T> func){
        nextTask = func;
        nextPromise = new Promise<>();
        if (resolved) { runNextTask(); }
        return nextPromise;
    }
    
    public Promise<T> then(Consumer<T> func){
        return then((r) -> {
            func.accept(r);
            return result;
        });
    }
    
    public Promise<T> then(Runnable func) {
        return then((r) -> {
            func.run();
            return result;
        });
    }
    
    public Promise<T> then(Supplier<T> func) {
        return then((r) -> {
            return func.get();
        });
    }
    
    private void resolve(T result){
        this.result = result;
        resolved = true;
        
        runNextTask();
    }
}
