package demo;

import ObjectPool.PoolManager;
import User.Login;
import User.LoginThread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MainDemo {

    private PoolManager<Login> pool;
    private static int ThreadNum = 5; // Thread Number in the Thread Pool.

    private static int minObject = 10;//It can be 10
    private static int maxObject = 100;//It can be 100

    public void setUp(){

        pool = new PoolManager<Login>(minObject, maxObject, 5) {

            @Override
            protected Login createObject() {

                return Login.getInstance();//from Singleton Object.
            }
        };
    }

    public void shutDownPool() {
        pool.shutdown();
    }

    public void DemoPoolManager(){

        ExecutorService executor = Executors.newFixedThreadPool(ThreadNum); // Thread pool.

        //It will be a for loop and must be global variable by Thread Number.

        int poolSize = pool.findLength();
        System.out.println("Starting Pool Size:"+poolSize);

        /*
        executor.execute(new LoginThread(pool, 1));
        executor.execute(new LoginThread(pool, 2));
        executor.execute(new LoginThread(pool, 3));
        executor.execute(new LoginThread(pool, 4));
        executor.execute(new LoginThread(pool, 5));
        */

        for(int i = 1 ; i <= ThreadNum ; i++){
            executor.execute(new LoginThread(pool , i));
        }


        poolSize = pool.findLength();
        System.out.println("Lastest Pool Size:"+poolSize);

        executor.shutdown();

        try {
            executor.awaitTermination(30, TimeUnit.SECONDS);
        }catch (InterruptedException e){
            e.printStackTrace();
        }

    }



    public static void main(String[] args){

        MainDemo op = new MainDemo();
        op.setUp();
        op.shutDownPool();
        op.DemoPoolManager();

    }


}
