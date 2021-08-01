/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;
import java.util.LinkedList;
import java.util.Queue;
import java.util.function.*;
/**
 *
 * @author jesse
 */
public class Promise<T> {
    private Function<T, T> nextFunc = null;
    private Promise<T> nextPromise = null;
    private T result;
    private boolean resolved = false;
    public boolean isResolved() { return resolved; }
    public T getResult() { return result; }
    
    public Promise(){}
    
    public Promise(Consumer<Consumer<T>> func){
        func.accept((t) -> resolve(t));
    }
    
    private void runNextTask(){
        if (nextFunc != null){
            nextPromise.resolve(nextFunc.apply(result));
        }
    }
    
    public Promise<T> then(Function<T, T> func){
        if (nextFunc != null){
            return nextPromise.then(func);
        }
        nextFunc = func;
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
