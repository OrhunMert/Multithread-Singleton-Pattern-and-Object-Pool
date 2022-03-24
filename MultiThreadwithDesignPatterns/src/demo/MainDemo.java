package demo;

import ObjectPool.PoolManager;
import User.Login;
import User.LoginThread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MainDemo {

    private PoolManager<Login> pool;

    public void setUp(){

        pool = new PoolManager<Login>(4, 10, 5) {

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

        ExecutorService executor = Executors.newFixedThreadPool(5); // Thread pool.


        //It will be a for loop and must be global variable by Thread Number.
        executor.execute(new LoginThread(pool, 1));
        executor.execute(new LoginThread(pool, 2));
        executor.execute(new LoginThread(pool, 3));
        executor.execute(new LoginThread(pool, 4));
        executor.execute(new LoginThread(pool, 5));

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
