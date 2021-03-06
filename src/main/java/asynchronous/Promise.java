/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package asynchronous;
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
    
    private Promise(){}
    
    public Promise(Consumer<Consumer<T>> func){
        func.accept((t) -> resolve(t));
    }
    
    private void runNextFunc(){
        if (nextFunc != null){
            nextPromise.resolve(nextFunc.apply(result));
        }
    }
    
    public synchronized Promise<T> then(Function<T, T> func){
        // If then has already been called on this promise, call on the nextPromise instead.
        if (nextFunc != null){
            // This way, the func given will get called after the next one completes.
            // And if the next promise has already had then called on it, the same thing will happen.
            return nextPromise.then(func);
        }
        
        // otherwise, set up the nextFunc and Promise.
        nextFunc = func;
        nextPromise = new Promise<>();
        // If this promise has already resolved, run next now, don't wait for it to resolve (because it never will now).
        if (resolved) { runNextFunc(); }
        // return the promise for the next function.
        return nextPromise;
    }
    
    public synchronized Promise<T> then(Consumer<T> func){
        return then((r) -> {
            func.accept(r);
            return result;
        });
    }
    
    public synchronized Promise<T> then(Runnable func) {
        return then((r) -> {
            func.run();
            return result;
        });
    }
    
    public synchronized Promise<T> then(Supplier<T> func) {
        return then((r) -> {
            return func.get();
        });
    }
    
    private synchronized void resolve(T result){
        // cancel if this promise has already been resolved.
        if (resolved) {return;}
        
        // take the result
        this.result = result;
        
        // set resolved
        resolved = true;
        
        // run the next function (given to us by the then method)
        runNextFunc();
    }
}
