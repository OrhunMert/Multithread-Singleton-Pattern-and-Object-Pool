package User;

public class Login {

    //We will create this class's object with LoginThread Class.
    //also We will use Singleton Pattern in this Loginn class.

    //Singleton Pattern

    private static Login instance = null;
    private static Object lock_obj = new Object();

    private Login(){

    }

    public static Login getInstance(){

        if(instance == null){

            synchronized (lock_obj){

                if(instance == null){

                    instance = new Login();

                }

            }

        }

        return instance;
    }

}
