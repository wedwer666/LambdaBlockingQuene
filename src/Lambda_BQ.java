/**
 * Created by Maria on 29.10.2017.
 */
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


@FunctionalInterface
interface MathOperations
{
    int operation(int a, int b);
}
public class Lambda_BQ
{
    public static void main(String[] args)
    {
        BlockingQueue<MathOperations> sharedQ = new LinkedBlockingQueue<MathOperations>();
        Lambda_BQ lambda_bq = new Lambda_BQ();
        Producer p = new Producer(sharedQ);
        Consumer c = new Consumer(sharedQ);
        p.start();
        c.start();
    }

   }

class Producer extends Thread {
    public BlockingQueue<MathOperations> sharedQueue;
    public Producer(BlockingQueue<MathOperations> aQueue)
    {
        super("PRODUCER");
        this.sharedQueue = aQueue;
    }
    public int operate(int a, int b, MathOperations mathOperations)
    {
        return mathOperations.operation(a,b);
    }

    public void run() {
        MathOperations addition = (int a, int b) -> a + b;
        MathOperations substraction = (int a, int b) -> a - b;
        MathOperations multiplication = (int a, int b) -> {
            return a * b;
        };
        MathOperations division = (int a, int b) -> {
            return a / b;
        };
        synchronized (sharedQueue) {
            try {
                System.out.println(" Get notified at time:"+System.currentTimeMillis());

                sharedQueue.put(addition);
                sharedQueue.put(substraction);
                sharedQueue.put(multiplication);
                sharedQueue.put(division);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(" Got notified at time"+System.currentTimeMillis());
        }
    }
}

class Consumer extends Thread
{
    private BlockingQueue<MathOperations> sharedQueue;

    public Consumer(BlockingQueue<MathOperations> aQueue)
    {
        super("CONSUMER");
        this.sharedQueue = aQueue;
    }
        @Override
        public void run()
        {
                try {
                    System.out.println("The result of the first mathematical operation - " + sharedQueue.take().operation(6,8));
                    System.out.println("The result of the second mathematical operation - " + sharedQueue.take().operation(10,8));
                    System.out.println("The result of the third mathematical operation - " + sharedQueue.take().operation(100,54));
                    System.out.println("The result of the fourth mathematical operation - " + sharedQueue.take().operation(2,76));

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
}