package Task1_and_2;

import java.util.ArrayList;
import java.util.List;

public class ServiceSystem {
    private ServiceDevice device;
    private List<Client> clients;

    public ServiceSystem() {
        this.device = new ServiceDevice();
        this.clients = new ArrayList<>();
    }

    // Створення клієнтів для демонстрації
    public void generateClients(int numClients) {
        for (int i = 1; i <= numClients; i++) {
            clients.add(new Client(i, i * 2));
        }
    }

    public void startService() {
        for (Client client : clients) {
            device.addClient(client);  // клієнти прибувають та стають у чергу
        }

        while (device.hasClients()) {
            device.serveNext();
        }

        // Закінчення обслуговування
        double totalOperationTime = device.getCurrentTime();
        double activeTime = device.getActiveTime();

        // Обчислення середнього завантаження
        double averageLoad = (totalOperationTime > 0) ? (activeTime / totalOperationTime) : 0;

        System.out.println("Середнє завантаження пристрою: " + averageLoad);
    }

    public static void main(String[] args) {
        ServiceSystem system = new ServiceSystem();
        system.generateClients(10);  // генеруємо клієнтів
        system.startService();  // починаємо обслуговування
    }
}
