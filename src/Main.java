import java.util.Scanner;
public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter num of thread:");
        int count_of_thread = Integer.parseInt(scanner.nextLine());
        int[] arr_step = new int[count_of_thread];
        for (int i = 0; i<count_of_thread; i++){
            System.out.println("Enter " + (i+1) +" step");
            arr_step[i] = Integer.parseInt(scanner.nextLine());
        }
        System.out.println("Enter delay:");
        int time = Integer.parseInt(scanner.nextLine());
        BreakThread breakThread = new BreakThread(time);

        for (int i = 0; i<count_of_thread; i++){
            new MainThread(i+1, breakThread, arr_step[i]).start();
        }
        new Thread(breakThread).start();
    }
}
class BreakThread implements Runnable{
    private int time;
    public BreakThread(int enter_time){
        time = enter_time;
    }
    static volatile boolean can_stop = false;
    @Override
    public void run() {
        try {
            Thread.sleep(time * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        can_stop = true;
    }

    synchronized public boolean isCanBreak() {
        return can_stop;
    }
}

class MainThread extends Thread{
    private int step;
    private final int id;
    private final BreakThread breakThread;

    public MainThread(int id, BreakThread breakThread, int step) {
        this.step = step;
        this.id = id;
        this.breakThread = breakThread;
    }

    @Override
    public void run() {
        long sum = 0;
        boolean break_bool = false;
        do{
            sum++;
            break_bool = breakThread.isCanBreak();
        } while (!break_bool);
        System.out.println(id + " - " + sum);
    }
}
