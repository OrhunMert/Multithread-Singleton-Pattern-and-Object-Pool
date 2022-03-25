package User;

import ObjectPool.PoolManager;

import static java.lang.Thread.*;

// also We can implements Runnable. difference is between Thread and Runnable about object creating.
public class LoginThread extends Thread {

    private PoolManager<Login> pool;
    private int threadNo;


    public LoginThread(PoolManager<Login> pool , int threadNo){

        this.pool = pool;
        this.threadNo = threadNo;

    }

    //override function from Thread Class in Java.
    public void run(){

        // get an object from the pool
        Login loginProcess = pool.borrowObject();
        System.out.println("Thread " + threadNo + " was borrowed from pool. Object's Hash Code: "
                + loginProcess.hashCode());



        //you can  do something here in future
        // .........


        // return loginProcess instance back to the pool
        pool.returnObject(loginProcess);
        System.out.println("Thread " + threadNo + " was returned to pool. Object's Hash Code: "
                + loginProcess.hashCode());

    }

}
