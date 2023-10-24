package Task5;

abstract class ProcessStep {
    protected int delay;
    protected int numDevices; // Кількість пристроїв

    public ProcessStep(int delay, int numDevices) {
        this.delay = delay;
        this.numDevices = numDevices;
    }

    abstract void execute();

    protected void applyDelay() {
        long startTime = System.currentTimeMillis();
        try {
            // Зменшуємо затримку на основі кількості пристроїв
            Thread.sleep(delay / numDevices);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long endTime = System.currentTimeMillis();
        long timeSpent = endTime - startTime;

        System.out.println("Time spent: " + timeSpent + " ms");
    }
}

class CreateStep extends ProcessStep {
    public CreateStep(int delay) {
        super(delay, 1); // Create крок завжди має 1 пристрій
    }

    @Override
    void execute() {
        System.out.print("Executing CREATE step... ");
        applyDelay();
    }
}

class Process1Step extends ProcessStep {
    public Process1Step(int delay, int numDevices) {
        super(delay, numDevices);
    }

    @Override
    void execute() {
        System.out.print("Executing PROCESS 1... ");
        applyDelay();
    }
}

class Process2Step extends ProcessStep {
    public Process2Step(int delay, int numDevices) {
        super(delay, numDevices);
    }
    @Override
    void execute() {
        System.out.println("Executing PROCESS 2...");
        applyDelay();
    }
}

class Process3Step extends ProcessStep {
    public Process3Step(int delay, int numDevices) {
        super(delay, numDevices);
    }
    @Override
    void execute() {
        System.out.println("Executing PROCESS 3...");
        applyDelay();
    }
}

class DisposeStep extends ProcessStep {
    public DisposeStep(int delay) {
        super(delay, 1); // Dispose крок завжди має 1 пристрій
    }

    @Override
    void execute() {
        System.out.print("Executing DISPOSE step... ");
        applyDelay();
    }
}

public class Workflow {
    private final ProcessStep[] steps;

    public Workflow(int delayCreate, int delayProcess1, int numDevicesProcess1, int delayProcess2, int numDevicesProcess2, int delayProcess3, int numDevicesProcess3, int delayDispose) {
        this.steps = new ProcessStep[]{
                new CreateStep(delayCreate),
                new Process1Step(delayProcess1, numDevicesProcess1),
                new Process2Step(delayProcess2, numDevicesProcess2),
                new Process3Step(delayProcess3, numDevicesProcess3),
                new DisposeStep(delayDispose)
        };
    }

    public void run() {
        for (ProcessStep step : steps) {
            step.execute();
        }
    }

    public static void main(String[] args) {
        // Для прикладу, використовуємо 2 пристрої для PROCESS 1, 3 пристрої для PROCESS 2 та 1 пристрій для PROCESS 3
        Workflow workflow = new Workflow(500, 1000, 2,
                1500, 3, 2000, 1, 2500);
        workflow.run();
    }
}

