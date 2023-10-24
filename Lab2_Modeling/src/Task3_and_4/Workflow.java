package Task3_and_4;

abstract class ProcessStep {
    protected int delay;

    public ProcessStep(int delay) {
        this.delay = delay;
    }

    protected abstract void execute();

    protected void applyDelay() {
        long startTime = System.currentTimeMillis(); // Початковий час
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long endTime = System.currentTimeMillis(); // Кінцевий час
        long timeSpent = endTime - startTime; // Затрачений час

        System.out.println("Time spent: " + timeSpent + " ms");
    }
}

class CreateStep extends ProcessStep {
    public CreateStep(int delay) {
        super(delay);
    }

    @Override
    protected void execute() {
        System.out.print("Executing CREATE step... ");
        applyDelay();
    }
}

class Process1Step extends ProcessStep {
    public Process1Step(int delay) {
        super(delay);
    }
    @Override
    protected void execute() {
        System.out.println("Executing PROCESS 1...");
        applyDelay();
    }
}

class Process2Step extends ProcessStep {
    public Process2Step(int delay) {
        super(delay);
    }
    @Override
    protected void execute() {
        System.out.println("Executing PROCESS 2...");
        applyDelay();
    }
}

class Process3Step extends ProcessStep {
    public Process3Step(int delay) {
        super(delay);
    }
    @Override
    protected void execute() {
        System.out.println("Executing PROCESS 3...");
        applyDelay();
    }
}

class DisposeStep extends ProcessStep {
    public DisposeStep(int delay) {
        super(delay);
    }
    @Override
    protected void execute() {
        System.out.println("Executing DISPOSE step...");
        applyDelay();
    }
}

public class Workflow {
    private final ProcessStep[] steps;

    public Workflow(int delayCreate, int delayProcess1, int delayProcess2, int delayProcess3, int delayDispose) {
        this.steps = new ProcessStep[] {
                new CreateStep(delayCreate),
                new Process1Step(delayProcess1),
                new Process2Step(delayProcess2),
                new Process3Step(delayProcess3),
                new DisposeStep(delayDispose)
        };
    }

    public void run() {
        for (ProcessStep step : steps) {
            step.execute();
        }
    }

    public static void main(String[] args) {
        Workflow workflow = new Workflow(2000, 2000, 2000, 2000, 2000);
        workflow.run();
    }
}
