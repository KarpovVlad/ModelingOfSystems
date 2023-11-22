package Task1;

import java.util.*;

public class MassServiceModel {

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

            // Симуляція часу обробки
            currentTime = endTime;

            System.out.println(String.format("Система %d: завершила обробку події у час %.2f", systemId, currentTime));

            // Переходимо до наступної події в черзі
            processNextEvent();
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

    public static void main(String[] args) {
        int N = 5; // Кількість систем

        // Генерація списку подій (прибуття та тривалість обслуговування)
        List<Event> events = new ArrayList<>();
        for (int i = 0; i < N + 1; i++) {
            double arrivalTime = Math.random() * 10; // Наприклад, випадковий час прибуття
            double serviceTime = Math.random() * 2; // Наприклад, випадковий час обслуговування
            events.add(new Event(i % N, arrivalTime, serviceTime));
        }

        // Сортуємо події за часом прибуття
        Collections.sort(events);

        // Запуск симуляції
        simulate(N, events);
    }
}
