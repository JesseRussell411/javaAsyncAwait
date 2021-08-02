/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import asynchronous.GenericAsync;
import asynchronous.Task;
import asynchronous.Promise;

/**
 *
 * @author jesse
 */
public class Driver {
    static GenericAsync async = new GenericAsync();
    
    static  Promise<String> getHello(){
        return new Promise<String>((resolve) -> 
            new Thread(() -> {
                try{
                        Thread.sleep(800);
                } catch(InterruptedException e) { }
                System.out.println("got hello");
                resolve.accept("hello");
                    }).start());
            
    }
    static Promise<String> getSpace(){
        return new Promise<String>((resolve) -> 
            new Thread(() -> {
                try{
                        Thread.sleep(700);
                } catch(InterruptedException e) { }
                System.out.println("got space");
                resolve.accept(" ");
                    }).start());
    }
    static Promise<String> getWorld() {
        return new Promise<String>((resolve) -> 
            new Thread(() -> {
                try{
                        Thread.sleep(700);
                } catch(InterruptedException e) { }
                System.out.println("got world");
                resolve.accept("world");
                    }).start());
    }
    static Promise<Integer> getOne() {
        return new Promise<Integer>((resolve) -> 
            new Thread(() -> {
                try{
                        Thread.sleep(500);
                } catch(InterruptedException e) { }
                System.out.println("got one");
                resolve.accept(1);
                    }).start());
    }
    static Promise<Integer> getTwo() {
        return new Promise<Integer>((resolve) -> 
            new Thread(() -> {
                try{
                        Thread.sleep(500);
                } catch(InterruptedException e) { }
                System.out.println("got two");
                resolve.accept(2);
                    }).start());
    }
    static Promise<Integer> complicatedGetOne() {
        return new Promise<Integer>((resolve) -> 
                resolve.accept(async.await(() -> 
                        getTwo()) - async.await(() -> getOne())));
    }
    static Promise<String> getSpaceWorld(){
        return new Promise<String>((resolve) -> resolve.accept(async.await(() -> getSpace()) + async.await(() -> getWorld())));
    }
    
    public static void main(String[] args) {
        var prom = async.later(() -> new Promise<String>(
        resolve -> {
            System.out.println("Started quick brown fox.");
            new Thread(() -> {
                try{
                    Thread.sleep(5000);
                } catch(InterruptedException e) {}
                resolve.accept("The quick brown fox jumps over the lazy dog.");
            }).start();
        }
        ));
        prom.then((r) -> {System.out.println(r);});
        prom.then((r) -> {System.out.println(r.toUpperCase()); return r.toLowerCase();});
        prom.then((r) -> r + "and then what?");
        prom.then((r) -> {System.out.println(r);});
        
        async.later(() -> new Promise<Object>((resolve) -> {
            final var hello = async.later(() -> getHello());
            final var spaceWorld = async.later(() -> getSpaceWorld());
            final var one = async.later(() -> complicatedGetOne());
            
            
            System.out.println(async.await(hello) + async.await(spaceWorld) + "!" + async.await(one));
            
            resolve.accept(null);
        }));
        System.out.println("goodbye world");
        
        async.execute();
    }
}
