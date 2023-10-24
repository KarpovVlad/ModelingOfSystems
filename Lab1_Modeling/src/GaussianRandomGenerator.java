import java.util.Random;

public class GaussianRandomGenerator {

    private static final int N = 10000; // Кількість генерованих чисел
    private static final double THRESHOLD = 0.2; // Порогове значення для визначення, чи відповідає число нормальному розподілу
    private static Random random = new Random(); // Ініціалізація генератора випадкових чисел

    public static void main(String[] args) {
        double a = 0; // Середнє значення ('a') для нормального розподілу
        double sigma = 1; // Стандартне відхилення ('σ') для нормального розподілу

        // Цикл для генерування N випадкових чисел
        for (int i = 0; i < N; i++) {
            double xi = generateUniformRandomNumber(); // Генеруємо випадкове число з рівномірного розподілу
            double mu = calculateMu(); // Обчислення середнього значення з використанням методу центральної граничної теореми
            double x = mu + a; // Додаємо 'a' до середнього значення, щоб отримати 'x'

            // Обчислення функції густини ймовірності (f) для 'x'
            double f = (1 / (sigma * Math.sqrt(2 * Math.PI))) * Math.exp(-Math.pow((x - a), 2) / (2 * Math.pow(sigma, 2)));

            // Перевірка, чи значення 'f' менше за порогове значення
            if (f < THRESHOLD) {
                System.out.println("x: " + x + ", f(x): " + f + " не відповідає нормальному закону розподілу"); // Якщо так, виводимо відповідне повідомлення
            } else {
                System.out.println("x: " + x + ", f(x): " + f); // Якщо ні, просто виводимо 'x' та 'f(x)'
            }
        }
    }

    // Метод для генерування випадкового числа з рівномірного розподілу
    private static double generateUniformRandomNumber() {
        return random.nextDouble(); // Використовуємо вбудований метод nextDouble() класу Random
    }

    // Метод для обчислення 'μ' (mu) за допомогою методу центральної граничної теореми
    private static double calculateMu() {
        double sum = 0;
        // Сумуємо 12 випадкових чисел з рівномірного розподілу
        for (int i = 0; i < 12; i++) {
            sum += generateUniformRandomNumber(); // Додаємо кожне нове випадкове число до загальної суми
        }
        return sum - 6; // Віднімаємо 6 від суми, щоб центрувати результати біля 0
    }
}
