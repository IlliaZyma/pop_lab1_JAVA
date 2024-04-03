import java.util.Arrays;
import java.util.Scanner;
public class Main {

    public static void main(String[] args) {
        int[] time_arr;
        int[] arr_step;

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter num of thread:");
        int count_of_thread = Integer.parseInt(scanner.nextLine());

        time_arr = new int[count_of_thread];
        arr_step = new int[count_of_thread];

        for (int i = 0; i<count_of_thread; i++){
            System.out.println("Enter step of thread #"  + (i+1));
            arr_step[i] = Integer.parseInt(scanner.nextLine());
            System.out.println("Enter time of thread #"  + (i+1));
            time_arr[i] = Integer.parseInt(scanner.nextLine());
        }

        //System.out.println("Enter delay:");
        //int time = Integer.parseInt(scanner.nextLine());

        BreakThread breakThread = new BreakThread(time_arr);

        for (int i = 0; i<count_of_thread; i++){
            new MainThread(i, breakThread, arr_step[i]).start();
        }
        new Thread(breakThread).start();
    }
}
class BreakThread implements Runnable{
    public int[] time_arr;
    //private boolean[] off_arr;
    public BreakThread(int[] enter_time){
        time_arr = enter_time;
        //off_arr = enter_ctrl;
    }
    //static volatile boolean can_stop = false;
    @Override
    public void run() {
        int last_num = 0;
        int last_id = 0;

        int less_num = time_arr[0];
        int less_id = 0;

        for (int i = 0; i < time_arr.length; i++ ){
            for (int k = 0; k < time_arr.length; k++){
                if (time_arr[k] != 0 && less_num > time_arr[k])
                less_num = time_arr[k];
                less_id = k;
            }
            try {
                Thread.sleep(less_num - last_num*1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //off_arr[less_id] = true;
            last_num = less_num;
            last_id = less_id;
            time_arr[last_id]  = 0;
            for (int k = 0; k < time_arr.length; k++ ){
                if(time_arr[k] != 0){
                    less_num = time_arr[k];
                    less_id = 0;
                    break;
                }
            }
        }

        /*try {
            Thread.sleep(time * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        can_stop = true;*/
    }

    synchronized public boolean isCanBreak(int id) {
        if (time_arr[id] == 0) {
            return true;}
        else
            return false;

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
            break_bool = breakThread.isCanBreak(id);
        } while (!break_bool);
        System.out.println(id + " - " + sum);
    }
}
