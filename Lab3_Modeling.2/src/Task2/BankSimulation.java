package Task2;

import java.util.*;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class BankSimulation {
    // Максимальна кількість автомобілів в системі.
    private static final int MAX_CARS = 8;
    // Параметри розподілу.
    private static final double ARRIVAL_MEAN = 0.5;
    private static final double SERVICE_MEAN = 0.3;
    private static final double END_TIME = 60.0;
    // Параметри для нормального розподілу часу обслуговування
    private Random random;
    private ExecutorService cashierService;

    // Черги для обслуговування.
    private Queue<Integer> firstLane;
    private Queue<Integer> secondLane;
    private int missedCars;

    // Додаткові змінні для зберігання статистики
    private int totalServedCars; // Загальна кількість обслугованих авто
    private double totalServiceTime; // Загальний час обслуговування
    private double lastDepartureTime; // Час від'їзду останнього авто
    private int laneChanges; // Зміни під'їзних смуг

    // Імітація часу.
    private double currentTime;

    public BankSimulation() {
        this.firstLane = new LinkedList<>();
        this.secondLane = new LinkedList<>();
        this.cashierService = Executors.newFixedThreadPool(2);
        this.random = new Random();
        this.currentTime = 0.0;
        this.missedCars = 0;
        this.totalServedCars = 0;
        this.totalServiceTime = 0.0;
        this.lastDepartureTime = 0.0;
        this.laneChanges = 0;

        for (int i = 0; i < 2; i++) { // Додаємо два автомобілі до кожної черги на початку симуляції
            this.firstLane.add(i);
            this.secondLane.add(i);
        }
    }

    public void simulate() {
        while (currentTime < END_TIME) {
            double nextArrival = getNextArrivalTime();
            currentTime += nextArrival;

            // Логіка обробки прибуття автомобіля.
            if (firstLane.size() + secondLane.size() < MAX_CARS) {
                // Додаємо автомобіль до вибраної черги.
                Queue<Integer> chosenLane = selectLaneAndRecordChanges();
                chosenLane.add(1);

                // Генеруємо час обслуговування.
                double serviceTime = getServiceTime();

                // Подамо завдання в ExecutorService.
                cashierService.submit(() -> {
                    try {
                        Thread.sleep((long) (serviceTime)); // Перетворюємо час у мілісекунди
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    synchronized (this) {
                        chosenLane.poll();
                        totalServedCars++;
                        totalServiceTime += serviceTime;
                        double departureTime = currentTime + serviceTime;
                        lastDepartureTime = departureTime;
                        System.out.println("Time spend for service: " + (serviceTime));
                        System.out.println("Car service finished and leaves the queue. Time: " + (currentTime + serviceTime));
                    }
                });
            } else {
                System.out.println("Car has to leave because the bank is full. Time: " + currentTime);
                missedCars++;
            }

            // Оновлення стану симуляції.
            System.out.println("Current time: " + currentTime);
            System.out.println("Cars in the first lane: " + firstLane.size());
            System.out.println("Cars in the second lane: " + secondLane.size());

            // Перевірка умови завершення симуляції. Наприклад, ви можете завершити симуляцію після певного часу.
            if (currentTime >= END_TIME) {
                break;
            }
        }
        cashierService.shutdown(); // Закриття служби після циклу.
        try {
            if (!cashierService.awaitTermination(60, TimeUnit.SECONDS)) {
                cashierService.shutdownNow();
            }
        } catch (InterruptedException e) {
            cashierService.shutdownNow();
        }
        System.out.println("Simulation has ended. Total missed cars: " + missedCars);
        // Після закінчення симуляції розраховуємо фінальну статистику
        double averageLoad = totalServiceTime / END_TIME;
        double averageCarsInBank = (double) (totalServedCars + missedCars) / END_TIME;
        double averageDepartureInterval = lastDepartureTime / totalServedCars;
        double averageStayInBank = totalServiceTime / totalServedCars;
        double averageQueueLength = ((double) firstLane.size() + secondLane.size()) / 2;
        double percentMissed = (double) missedCars / (totalServedCars + missedCars) * 100;

        // Друк статистики
        System.out.println("Average load per cashier: " + averageLoad);
        System.out.println("Average number of cars in bank: " + averageCarsInBank);
        System.out.println("Average departure interval: " + averageDepartureInterval);
        System.out.println("Average stay in bank: " + averageStayInBank);
        System.out.println("Average queue length: " + averageQueueLength);
        System.out.println("Percentage of missed cars: " + percentMissed + "%");
        System.out.println("Number of lane changes: " + laneChanges); // Потребує додаткової логіки для коректного підрахунку
        System.out.println("Total missed cars: " + missedCars);
    }
    private double getNextArrivalTime() {
        double lambda = 1.0 / ARRIVAL_MEAN;
        return Math.log(1 - random.nextDouble()) / (-lambda);
    }
    private double getServiceTime() {
        double lambda = 1.0 / SERVICE_MEAN; // SERVICE_MEAN тут є математичним очікуванням (середнім)
        // Генеруємо експоненційний час обслуговування.
        return Math.log(1 - random.nextDouble()) / (-lambda);
    }
    private Queue<Integer> selectLaneAndRecordChanges() {
        Queue<Integer> chosenLane;

        // Перевіряємо умови для зміни смуги.
        if (firstLane.size() - secondLane.size() >= 2 && !firstLane.isEmpty()) {
            chosenLane = secondLane; // Якщо перша смуга має на два автомобілі більше, авто переходить у другу смугу.
            laneChanges++;
        } else if (secondLane.size() - firstLane.size() >= 2 && !secondLane.isEmpty()) {
            chosenLane = firstLane; // Якщо друга смуга має на два автомобілі більше, авто переходить у першу смугу.
            laneChanges++;
        } else {
            // Якщо умови не виконані, автомобіль встає в ту смугу, де менше автомобілів.
            chosenLane = (firstLane.size() <= secondLane.size()) ? firstLane : secondLane;
        }

        return chosenLane;
    }


    public static void main(String[] args) {
        BankSimulation simulation = new BankSimulation();
        simulation.simulate();
    }
}

