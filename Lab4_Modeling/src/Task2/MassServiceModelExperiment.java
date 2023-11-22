package Task2;

import java.util.*;

public class MassServiceModelExperiment {

    public static class Event implements Comparable<Event> {
        public int systemId;
        public double arrivalTime;
        public double serviceTime;

        public Event(int systemId, double arrivalTime, double serviceTime) {
            this.systemId = systemId;
            this.arrivalTime = arrivalTime;
            this.serviceTime = serviceTime;
        }

        @Override
        public int compareTo(Event other) {
            return Double.compare(this.arrivalTime, other.arrivalTime);
        }
    }

    public static class SystemMO {
        int systemId;
        double currentTime = 0;
        Queue<Event> queue = new PriorityQueue<>();
        boolean isBusy = false;

        public SystemMO(int systemId) {
            this.systemId = systemId;
        }

        public void receiveEvent(Event event) {
            System.out.println(String.format("Система %d: отримано подію у час %.2f. Час обслуговування: %.2f", systemId, event.arrivalTime, event.serviceTime));
            queue.offer(event);
            if (!isBusy) {
                processNextEvent();
            }
        }

        public void processNextEvent() {
            if (!queue.isEmpty()) {
                isBusy = true;
                Event event = queue.poll();
                currentTime = Math.max(currentTime, event.arrivalTime);
                double endTime = currentTime + event.serviceTime;

                System.out.println(String.format("Система %d: починає обробку події у час %.2f. Очікуваний час завершення: %.2f", systemId, currentTime, endTime));
                processEvent(event, endTime);
            } else {
                isBusy = false;
            }
        }

        public void processEvent(Event event, double endTime) {
            // Логіка специфічної обробки може бути додана тут

            // Симуляція часу обробки
            currentTime = endTime;

            System.out.println(String.format("Система %d: завершила обробку події у час %.2f", systemId, currentTime));

            // Переходимо до наступної події в черзі
            processNextEvent();
        }
    }

    public static void main(String[] args) {
        int N = 5; // Кількість систем

        // Параметри для контролю експерименту
        int initialNumberOfEvents = 1000; // початкова кількість подій
        int step = 100; // крок збільшення кількості подій
        int experiments = 100; // кількість експериментів

        for (int experiment = 0; experiment < experiments; experiment++) {
            int numberOfEvents = initialNumberOfEvents + step * experiment;

            // Генерація списку подій
            List<Event> events = new ArrayList<>();
            for (int i = 0; i < numberOfEvents; i++) {
                double arrivalTime = Math.random() * 10;
                double serviceTime = Math.random() * 2;
                events.add(new Event(i % N, arrivalTime, serviceTime));
            }

            // Сортуємо події за часом прибуття
            Collections.sort(events);

            // Вимірювання часу симуляції
            long startTime = System.currentTimeMillis();

            // Запуск симуляції
            simulate(N, events);

            long endTime = System.currentTimeMillis();

            System.out.println("Експеримент " + (experiment + 1) + ": кількість подій = " + numberOfEvents + ", час виконання = " + (endTime - startTime) + " мс");
        }
    }

    public static void simulate(int numberOfSystems, List<Event> events) {
        // Ініціалізація систем масового обслуговування
        List<SystemMO> systems = new ArrayList<>();
        for (int i = 0; i < numberOfSystems; i++) {
            systems.add(new SystemMO(i));
        }

        // Передача подій до відповідних систем
        for (Event event : events) {
            systems.get(event.systemId).receiveEvent(event);
        }
    }
}
