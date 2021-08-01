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
public class GenericAsync {
    private AsyncController<Object> controller = new AsyncController<>();
    public <T> T await(Task<T> task){
        return (T)controller.await((Task<Object>) task);
    }
    public <T> T await(Supplier<Promise<T>> work){
        return (T)controller.await((Task<Object>) new Task<T>(work));
    }
    public <T> T await(Promise<T> outsideWork){
        return (T)controller.await((Promise<Object>) outsideWork);
    }
    
    public <T> Promise<T> later(Task<T> task){
        return (Promise<T>) controller.later((Task<Object>)task);
    }
    public <T> Promise<T> later(Supplier<Promise<T>> work){
        return (Promise<T>) controller.later((Task<Object>) new Task<T>(work));
    }
    public <T> Promise<T> later(Promise<T> outsideWork){
        return (Promise<T>) controller.later((Promise<Object>) outsideWork);
    }
    
    public void execute(){
        controller.execute();
    }
}
