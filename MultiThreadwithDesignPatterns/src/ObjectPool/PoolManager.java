package ObjectPool;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public abstract class PoolManager<T> {

    protected abstract T createObject();

     /*
        Question 1:
        -->Do we need to abstract class for PoolManager?

        *Object Design Pattern with using Singleton Pattern.

     */

    private ConcurrentLinkedQueue<T> pool;//We made Thread Safe with Queue. Because FIFO.
    private ScheduledExecutorService executorService;

    public PoolManager(final int minObjects)
    {
        initialize(minObjects);// initialize pool

    }

    public PoolManager(final int minObjects, final int maxObjects, final long validationInterval) {

        initialize(minObjects);// initialize pool

        // check pool conditions in a separate thread
        executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleWithFixedDelay(new Runnable()
        {
            @Override
            public void run() {

                int size = pool.size();

                if (size < minObjects) {
                    int sizeToBeAdded = minObjects + size;
                    for (int i = 0; i < sizeToBeAdded; i++) {
                        pool.add(createObject());
                    }
                } else if (size > maxObjects) {
                    int sizeToBeRemoved = size - maxObjects;
                    for (int i = 0; i < sizeToBeRemoved; i++) {
                        pool.poll();
                    }
                }
            }
        }, validationInterval, validationInterval, TimeUnit.SECONDS);
    }

    public T borrowObject() {

        /*
        pool = [10,7,5]
        print(pool.poll) --> 10
        new pool = [7,5]
         */
        //System.out.println("Pool Size:"+findLength());
        T object;
        if ((object = pool.poll()) == null)
        {
            object = createObject();

        }

        return object;
    }

    public void returnObject(T object) {
        if (object == null) {
            return;
        }
        this.pool.offer(object);//Like .add() and .append() functions.
    }

    /*
        Shutdown this pool.
    */
    public void shutdown(){
        if (executorService != null){
            executorService.shutdown();
        }
    }

    public int findLength(){
        return pool.size();
    }

    private void initialize(final int minObjects)  {
        pool = new ConcurrentLinkedQueue<T>();
        for (int i = 0; i < minObjects; i++) {
            pool.add(createObject());
        }
    }


}
