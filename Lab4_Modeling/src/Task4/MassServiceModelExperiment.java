package Task4;

import java.util.*;

public class MassServiceModelExperiment {

    static class Event implements Comparable<Event> {
        int currentSystemId;
        Integer nextSystemId; // Може бути null, якщо подія досягла кінцевої точки обробки
        double arrivalTime;
        double serviceTime;

        public Event(int currentSystemId, Integer nextSystemId, double arrivalTime, double serviceTime) {
            this.currentSystemId = currentSystemId;
            this.nextSystemId = nextSystemId;
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
                Integer nextSystemId = (i % N == N - 1) ? null : (i % N) + 1; // Встановлюємо наступну систему або null, якщо це остання система
                events.add(new Event(i % N, nextSystemId, arrivalTime, serviceTime));
            }

            // Сортуємо події за часом прибуття
            Collections.sort(events, Comparator.comparingDouble(event -> event.arrivalTime)); // Сортування вимагає Comparator'а для типу double.

            // Вимірювання часу симуляції
            long startTime = System.currentTimeMillis();

            // Запуск симуляції
            simulate(N, events);

            long endTime = System.currentTimeMillis();

            System.out.println("Експеримент " + (experiment + 1) + ": кількість подій = " + numberOfEvents + ", час виконання = " + (endTime - startTime) + " мс");
        }
    }

    public static void simulate(int numberOfSystems, List<Event> events) {
        List<SystemMO> systems = new ArrayList<>();
        for (int i = 0; i < numberOfSystems; i++) {
            systems.add(new SystemMO(i));
        }
        // Створення черги подій і додавання всіх подій до неї
        Queue<Event> eventQueue = new PriorityQueue<>(events);

        while (!eventQueue.isEmpty()) {
            Event currentEvent = eventQueue.poll();
            SystemMO currentSystem = systems.get(currentEvent.currentSystemId);

            // Перевірка, чи система зараз зайнята
            if (!currentSystem.isBusy) {
                currentSystem.receiveEvent(currentEvent);
                currentSystem.processNextEvent();
            }

            // Якщо подія має наступний рівень, створюємо нову подію та додаємо її до черги
            if (currentEvent.nextSystemId != null) {
                Event nextEvent = new Event(currentEvent.nextSystemId, null, currentSystem.currentTime, currentEvent.serviceTime);
                eventQueue.offer(nextEvent);
            }
        }
    }
}

