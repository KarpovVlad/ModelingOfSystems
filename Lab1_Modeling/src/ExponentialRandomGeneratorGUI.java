import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class ExponentialRandomGeneratorGUI {

    // Основні параметри для генерації випадкових чисел і створення гістограми
    private static final int N = 10000; // Кількість чисел для генерації
    private static final double LAMBDA = 1.0; // "Швидкість" експоненційного розподілу
    private static Random random = new Random(); // Генератор випадкових чисел
    private static final int BINS = 50; // Кількість бінів у гістограмі
    private static int[] frequency = new int[BINS]; // Масив для зберігання частоти кожного біну
    private static double maxVal = Double.MIN_VALUE; // Максимальне значення зі згенерованих чисел
    private static double sum = 0; // Сума всіх згенерованих чисел для обчислення середнього
    private static double sumSquared = 0; // Сума квадратів чисел для обчислення дисперсії

    // Метод для обчислення очікуваної частоти випадкових чисел між двома межами
    private static double calculateExpectedFrequency(double lower, double upper) {
        // Обчислення за формулою ймовірності експоненційного розподілу
        return N * (Math.exp(-LAMBDA * lower) - Math.exp(-LAMBDA * upper));
    }

    public static void main(String[] args) {
        generateData(); // Генерування випадкових даних

        // Обчислення статистичних показників
        double chiSquared = calculateChiSquared();
        System.out.println("Значення χі^2: " + chiSquared);
        double mean = sum / N; // Середнє значення
        double variance = (sumSquared / N) - (mean * mean); // Дисперсія
        System.out.println("Середнє: " + mean);
        System.out.println("Дисперсія: " + variance);

        // Створення і відображення графічного інтерфейсу
        JFrame frame = new JFrame("Гістограма частот");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.add(new HistogramPanel()); // Додавання панелі з гістограмою
        frame.setVisible(true); // Робимо вікно видимим
    }

    // Метод для обчислення значення хі-квадрат
    private static double calculateChiSquared() {
        double chiSquared = 0;
        // Проходимо через всі біни і порівнюємо очікувану та фактичну частоти
        for (int i = 0; i < BINS; i++) {
            double lowerBound = maxVal * i / BINS; // Нижня межа біну
            double upperBound = maxVal * (i + 1) / BINS; // Верхня межа біну

            double expectedFrequency = calculateExpectedFrequency(lowerBound, upperBound); // Очікувана частота
            // Обчислення хі-квадрат для поточного біну
            chiSquared += Math.pow(frequency[i] - expectedFrequency, 2) / expectedFrequency;
        }
        return chiSquared;
    }

    // Генерування випадкових чисел та збереження статистичних даних
    private static void generateData() {
        for (int i = 0; i < N; i++) {
            double xi = random.nextDouble(); // Генеруємо випадкове число в інтервалі [0,1]
            double result = generateExponentialRandom(xi, LAMBDA); // Перетворюємо його в експоненційно розподілений

            sum += result; // Оновлюємо суму
            sumSquared += result * result; // Оновлюємо суму квадратів

            // Оновлюємо максимальне значення
            if (result > maxVal) maxVal = result;
        }
    }

    // Метод для генерування експоненційного випадкового числа та оновлення гістограми
    public static double generateExponentialRandom(double xi, double lambda) {
        double result = (-1 / lambda) * Math.log(1 - xi); // Застосовуємо інверсну функцію розподілу
        updateHistogram(result); // Оновлюємо гістограму з новим значенням
        return result;
    }

    // Метод для оновлення гістограми новим значенням
    public static void updateHistogram(double value) {
        int bin = (int) (value / maxVal * BINS); // Визначаємо, до якого біну належить значення
        if (bin >= BINS) bin = BINS - 1; // Корекція, якщо бін виходить за межі діапазону
        frequency[bin]++; // Збільшуємо частоту відповідного біну
    }

    // Внутрішній клас для малювання гістограми
    static class HistogramPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            int panelWidth = getWidth(); // Ширина панелі
            int panelHeight = getHeight(); // Висота панелі
            int binWidth = panelWidth / BINS; // Ширина біну
            int maxFreq = getMaxFrequency(); // Найбільша частота для масштабування

            // Малюємо кожний бін як прямокутник
            for (int i = 0; i < BINS; i++) {
                int binHeight = (int) (((double) frequency[i] / maxFreq) * panelHeight); // Висота біну
                g.fillRect(i * binWidth, panelHeight - binHeight, binWidth, binHeight); // Малюємо прямокутник

                // Позначення значення на бінах
                String value = String.format("%.2f", (maxVal * i / BINS));
                g.setColor(Color.RED); // Колір тексту
                g.drawString(value, i * binWidth, panelHeight - binHeight/2); // Малюємо значення
                g.setColor(Color.BLACK); // Встановлюємо колір назад для наступного прямокутника
            }
        }

        // Метод для отримання максимальної частоти серед усіх бінів
        private int getMaxFrequency() {
            int max = 0;
            for (int i = 0; i < BINS; i++) {
                if (frequency[i] > max) {
                    max = frequency[i];
                }
            }
            return max;
        }
    }
}
