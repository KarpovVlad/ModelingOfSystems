package Task1_and_2;

import java.util.Queue;
import java.util.LinkedList;

public class ServiceDevice {
    private Queue<Client> queue;  // черга клієнтів
    private boolean isFree;  // статус пристрою (вільний чи зайнятий)
    private double activeTime;  // загальний час, коли пристрій був зайнятий
    private double currentTime; // поточний час системи


    public ServiceDevice() {
        this.queue = new LinkedList<>();
        this.isFree = true;
        this.activeTime = 0;
        this.currentTime = 0;
    }

    public void addClient(Client client) {
        queue.add(client);
    }

    public void serveNext() {
        if (!queue.isEmpty() && isFree) {
            Client client = queue.poll(); // обслуговуємо наступного клієнта

            // Додамо логіку для визначення часу обслуговування, наприклад, випадковий час
            double serviceTime = Math.random() * 5;  // випадковий час обслуговування (наприклад, від 0 до 5 одиниць часу)
            client.setServiceTime(serviceTime);

            // Обновляємо час активності
            activeTime += client.getServiceTime();
            currentTime += client.getServiceTime(); // також обновлюємо загальний час спостереження
            System.out.println("Клієнт " + client.getId() + " обслуговано за " + client.getServiceTime() + " од. часу.");


            isFree = false;
            release(); // Миттєво звільняємо пристрій після обслуговування
        }
    }

    public void release() {
        isFree = true;  // пристрій тепер вільний
    }

    public boolean isFree() {
        return isFree;
    }
    public double getActiveTime() {
        return activeTime;
    }

    public double getCurrentTime() {
        return currentTime;
    }

    public boolean hasClients() {
        return !queue.isEmpty();
    }
}
