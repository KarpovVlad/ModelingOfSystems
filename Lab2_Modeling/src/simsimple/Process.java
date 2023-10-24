package simsimple;

public class Process extends Element {
    private int queue, maxqueue, failure;
    private double meanQueue;
    private double totalWorkloadTime; // Total time the process was busy
    private double averageWorkload; // The average workload
    public Process(double delay) {
        super(delay);
        queue = 0;
        maxqueue = Integer.MAX_VALUE;
        meanQueue = 0.0;
    }
    // Call this method at every outAct event and when doing statistics
    private void updateWorkload(double currentTime) {
        // If the process is busy, add the time spent since the last event to the total workload time
        if (this.getState() == 1) {
            this.totalWorkloadTime += currentTime - this.getTcurr();
        }
    }
    public double getAverageWorkload() {
        if (this.getTcurr() == 0) {
            return 0;
        }
        // Calculate the average workload as the percentage of time the process was busy
        this.averageWorkload = (this.totalWorkloadTime / this.getTcurr()) * 100; // This gives a percentage
        return this.averageWorkload;
    }
    @Override
    public void inAct() {
        if (super.getState() == 0) {
            super.setState(1);
            super.setTnext(super.getTcurr() + super.getDelay());
        } else {
            if (getQueue() < getMaxqueue()) {
                setQueue(getQueue() + 1);
            } else {
                failure++;
            }
        }
    }
    @Override
    public void outAct() {
        updateWorkload(super.getTcurr());
        super.outAct();
        super.setTnext(Double.MAX_VALUE);
        super.setState(0);
        if (getQueue() > 0) {
            setQueue(getQueue() - 1);
            super.setState(1);
            super.setTnext(super.getTcurr() + super.getDelay());
        }
    }

    public int getFailure() {
        return failure;
    }
    public int getQueue() {
        return queue;
    }

    public void setQueue(int queue) {
        this.queue = queue;
    }
    public int getMaxqueue() {
        return maxqueue;
    }
    public void setMaxqueue(int maxqueue) {
        this.maxqueue = maxqueue;
    }
    @Override
    public void printInfo() {
        super.printInfo();
        System.out.println("failure = " + this.getFailure());
        System.out.println("Average Workload = " + this.getAverageWorkload() + "%");
    }
    @Override
    public void doStatistics(double delta) {
        meanQueue = getMeanQueue() + queue * delta;
        updateWorkload(this.getTcurr() + delta);
    }
    public double getMeanQueue() {
        return meanQueue;
    }
}

