package Task1_and_2;

public class Client {
    private final int id;
    private final double arrivalTime;  // час прибуття
    private double serviceTime;  // час, необхідний для обслуговування цього клієнта

    public Client(int id, double arrivalTime) {
        this.id = id;
        this.arrivalTime = arrivalTime;
    }

    public int getId() {
        return id;
    }

    public double getArrivalTime() {
        return arrivalTime;
    }

    public void setServiceTime(double serviceTime) {
        this.serviceTime = serviceTime;
    }

    public double getServiceTime() {
        return serviceTime;
    }
}
