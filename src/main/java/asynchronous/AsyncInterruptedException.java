/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package asynchronous;

/**
 *
 * @author jesse
 */
public class AsyncInterruptedException extends RuntimeException{
    public final InterruptedException original;
    public AsyncInterruptedException(InterruptedException original){
        super(original.getMessage(), original.getCause());
        this.original = original;
    }
    
}
