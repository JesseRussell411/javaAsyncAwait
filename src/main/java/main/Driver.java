/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

/**
 *
 * @author jesse
 */
public class Driver {
    static AsyncController<String> async = new AsyncController<>();
    static Promise<String> getHello(){
        return new Promise<String>((resolve) -> 
            new Thread(() -> {
                try{
                        Thread.sleep(100000);
                } catch(InterruptedException e) { }
                resolve.accept("hello");
                    }).start());
            
        }
    static Promise<String> getSpace(){
        return new Promise<String>((resolve) -> { resolve.accept(" "); });
    }
    static Promise<String> getWorld() {
        return new Promise<String>((resolve) -> { resolve.accept("world"); });
    }
    static Promise<String> getSpaceWorld(){
        return new Promise<String>((resolve) -> resolve.accept(async.await(() -> getSpace()) + async.await(() -> getWorld())));
    }
    
    public static void main(String[] args) {
        System.out.println("goodbye world");
        async.later(() -> new Promise<String>((resolve) -> {
            System.out.println(async.await(() -> getHello()) + async.await(() -> getSpaceWorld()));
            resolve.accept(null);
        }));
        
        async.execute();
    }
}
