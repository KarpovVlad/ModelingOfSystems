package Task6;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

abstract class ProcessStep {
    protected int delay;
    protected List<ProcessStep> nextSteps = new ArrayList<>();
    protected String name;

    public ProcessStep(int delay, String name) {
        this.delay = delay;
        this.name = name;
    }

    abstract void execute();

    protected void applyDelay() {
        try {
            System.out.println("Executing " + name);
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void setNextSteps(ProcessStep... steps) {
        for (ProcessStep step : steps) {
            nextSteps.add(step);
        }
    }

    public ProcessStep getNextStep() {
        if (nextSteps.isEmpty()) {
            return null;
        }

        if (nextSteps.size() > 1) {
            Random random = new Random();
            return nextSteps.get(random.nextInt(nextSteps.size()));
        }

        return nextSteps.get(0);
    }
}

class CreateStep extends ProcessStep {
    public CreateStep(int delay) {
        super(delay, "Create");
    }

    @Override
    void execute() {
        applyDelay();
    }
}

class Process1Step extends ProcessStep {
    public Process1Step(int delay) {
        super(delay, "Process 1");
    }

    @Override
    void execute() {
        applyDelay();
    }
}

class Process2Step extends ProcessStep {
    public Process2Step(int delay) {
        super(delay, "Process 2");
    }

    @Override
    void execute() {
        applyDelay();
    }
}

class Process3Step extends ProcessStep {
    public Process3Step(int delay) {
        super(delay, "Process 3");
    }

    @Override
    void execute() {
        applyDelay();
    }
}

class DisposeStep extends ProcessStep {
    public DisposeStep(int delay) {
        super(delay, "Dispose");
    }

    @Override
    void execute() {
        applyDelay();
    }
}

class Workflow {
    private ProcessStep start;

    public Workflow(ProcessStep start) {
        this.start = start;
    }

    public void run() {
        ProcessStep currentStep = start;
        while (currentStep != null) {
            currentStep.execute();
            currentStep = currentStep.getNextStep();
        }
    }
    public static void main(String[] args) {
        CreateStep create = new CreateStep(500);
        Process1Step process1 = new Process1Step(500);
        Process2Step process2 = new Process2Step(500);
        Process3Step process3 = new Process3Step(500);
        DisposeStep dispose = new DisposeStep(500);

        create.setNextSteps(process1);
        process1.setNextSteps(process1, process2);
        process2.setNextSteps(process1, process3);
        process3.setNextSteps(process2, dispose);

        Workflow workflow = new Workflow(create);
        workflow.run();
    }
}

