package Task1;

import java.util.*;

public class QueueSystem {

    static class Request {
        int priority;
        long arrivalTime; // Час прибуття заявки

        public Request(int priority) {
            this.priority = priority;
            this.arrivalTime = System.currentTimeMillis(); // фіксуємо час прибуття
        }
    }

    PriorityQueue<Request> queue = new PriorityQueue<>(
            Comparator.comparingInt(req -> req.priority)
    );

    public void processRequests() {
        Random random = new Random();

        // Симуляція надходження заявок
        System.out.println("Generating requests...");
        for (int i = 0; i < 100; i++) {
            queue.add(new Request(random.nextInt(100)));
        }
        System.out.println("Requests are generated.\nStarting processing...");

        // Симуляція обробки заявок
        while (!queue.isEmpty()) {
            Request currentRequest = queue.poll();
            System.out.println("Processing request with priority: " + currentRequest.priority);

            // Додамо симуляцію часу обробки, використовуючи Thread.sleep
            try {
                // Симулюємо час обробки випадковим часом у межах 100-200 мс
                int serviceTime = 100 + random.nextInt(100);
                Thread.sleep(serviceTime);

                // Логуємо інформацію про обробку
                long waitingTime = System.currentTimeMillis() - currentRequest.arrivalTime;
                System.out.println("Request processed. Priority: " + currentRequest.priority +
                        ", Service time (ms): " + serviceTime +
                        ", Waiting time (ms): " + waitingTime);

            } catch (InterruptedException e) {
                System.out.println("Processing was interrupted.");
            }

            // Розгалуження за ймовірністю
            double probability = random.nextDouble();
            String route = probability < 0.5 ? "Route 1" : "Route 2";
            System.out.println("Request directed to " + route + ".\n");
        }

        System.out.println("All requests have been processed.");
    }

    public static void main(String[] args) {
        QueueSystem system = new QueueSystem();
        system.processRequests(); // Запускаємо систему обслуговування
    }
}
