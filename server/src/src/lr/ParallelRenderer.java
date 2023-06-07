package lr;

import java.util.concurrent.Callable;

public class ParallelRenderer implements Callable {
    private Renderer r;
    private int from;
    private int to;
    private int NBRAYONS;

    public void setFrom(int from) {
        this.from = from;
    }
    public void setTo(int to) {
        this.to = to;
    }
    public int getFrom() {
        return from;
    }
    public int getTo() {
        return to;
    }

    public ParallelRenderer(Renderer r, int from, int to) {
        this.r = r;
        this.from = from;
        this.to = to;
        this.NBRAYONS = LR.NBRAYONS;
    }

    public Object call() {
        System.out.println("Thread " + Thread.currentThread().getName() + " started");
        for (int i = from; i < to; i++){
            r.renderLine(i, NBRAYONS);
        }
        System.out.println("Thread " + Thread.currentThread().getName() + " finished");
        return 'a';
    }
}
