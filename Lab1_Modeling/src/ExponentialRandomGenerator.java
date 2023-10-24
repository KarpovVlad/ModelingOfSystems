import java.util.Random;

public class ExponentialRandomGenerator {

    // Статичні константи для управління параметрами програми
    private static final int N = 10000; // Кількість випадкових чисел, які потрібно згенерувати
    private static final double LAMBDA = 1.0; // Параметр швидкості або "інтенсивності" для експоненційного розподілу
    private static Random random = new Random(); // Об'єкт для генерування випадкових чисел

    public static void main(String[] args) {
        // Головний цикл для генерації випадкових чисел та перевірки їх відповідності експоненційному розподілу
        for (int i = 0; i < N; i++) {
            double xi = random.nextDouble(); // Генеруємо випадкове число в інтервалі (0,1)
            double result = generateExponentialRandom(xi, LAMBDA); // Отримуємо експоненційно розподілене випадкове число
            System.out.println(result); // Виводимо згенероване число

            // Перевіряємо, чи відповідає згенероване число експоненційному закону розподілу
                if (!validate(result, LAMBDA)) {
                System.out.println("Число " + result + " не відповідає експоненційному закону розподілу!");
            }
        }
    }

    // Метод для генерації експоненційно розподіленого випадкового числа на основі однорідно розподіленого вхідного значення
    public static double generateExponentialRandom(double xi, double lambda) {
        // Використовуємо інверсію функції розподілу для отримання експоненційного розподілу з однорідного
        return (-1 / lambda) * Math.log(1 - xi); // Формула інверсії
    }

    // Метод для перевірки, чи відповідає згенероване число експоненційному розподілу, порівнюючи його з теоретичним розподілом
    public static boolean validate(double x, double lambda) {
        // Обчислюємо теоретичну ймовірність, що x буде меншим або рівним певному значенню в експоненційному розподілі
        double calculatedProbability = 1 - Math.exp(-lambda * x); // Функція розподілу експоненційної змінної

        // Генеруємо ще одне випадкове число для порівняння з обчисленою ймовірністю
        double actualProbability = random.nextDouble();

        // Якщо реальна ймовірність менша або рівна обчисленій, це підтверджує, що число відповідає експоненційному розподілу
        return actualProbability <= calculatedProbability;
    }
}
