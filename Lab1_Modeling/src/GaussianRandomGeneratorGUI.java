import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class GaussianRandomGeneratorGUI {

    // Базові параметри для генерування випадкових чисел і створення гістограми
    private static final int N = 10000; // Кількість генерованих чисел
    private static final int BINS = 50; // Кількість бінів (стовпців) у гістограмі
    private static Random random = new Random(); // Генератор випадкових чисел

    // Масив для зберігання частоти кожного біна
    private static int[] frequency = new int[BINS];
    private static double maxVal = Double.MIN_VALUE; // Максимальне значення серед згенерованих чисел
    private static double minVal = Double.MAX_VALUE; // Мінімальне значення серед згенерованих чисел
    private static double sum = 0; // Сума всіх чисел для обчислення середнього значення
    private static double sumSquared = 0; // Сума квадратів чисел для обчислення дисперсії

    //Функції для математичних обчислень
    private static double calculateExpectedFrequency(double lower, double upper, double mean, double sigma) {
        return N * (phi(upper, mean, sigma) - phi(lower, mean, sigma));
    }

    private static double phi(double x, double mean, double sigma) {
        return 0.5 * (1 + erf((x - mean) / (sigma * Math.sqrt(2))));
    }

    private static double erf(double z) {
        double t = 1.0 / (1.0 + 0.47047 * Math.abs(z));
        double poly = t * (0.3480242 - t * (0.0958798 - t * 0.7478556));
        double ans = 1 - poly * Math.exp(-z * z);
        if (z >= 0) return ans;
        else return -ans;
    }

    public static void main(String[] args) {
        // Генерація випадкових даних і обчислення статистичних характеристик
        generateData();

        // Створення та налаштування графічного вікна
        JFrame frame = new JFrame("Гістограма частот");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.add(new HistogramPanel()); // Додавання панелі з гістограмою
        frame.setVisible(true);

        // Обчислення і виведення статистичних характеристик
        double mean = sum / N; // Середнє значення
        double variance = (sumSquared - sum * mean) / (N - 1); // Дисперсія
        double sigma = Math.sqrt(variance); // Стандартне відхилення (σ)
        double chiSquared = calculateChiSquared(mean, sigma); // Обчислення χ^2

        // Виведення обчислених значень
        System.out.println("Значення χі^2: " + chiSquared);
        System.out.println("Середнє: " + mean);
        System.out.println("Дисперсія: " + variance);
    }

    //Продовження визначень методів для генерації даних, обчислення статистик і оновлення гістограми
    private static double calculateChiSquared(double mean, double sigma) {
        double chiSquared = 0;
        for (int i = 0; i < BINS; i++) {
            double lowerBound = minVal + (maxVal - minVal) * i / BINS;
            double upperBound = minVal + (maxVal - minVal) * (i + 1) / BINS;
            double expectedFrequency = calculateExpectedFrequency(lowerBound, upperBound, mean, sigma);
            chiSquared += Math.pow(frequency[i] - expectedFrequency, 2) / expectedFrequency;
        }
        return chiSquared;
    }

    private static void generateData() {
        double a = 7;
        double sigma = 3;

        for (int i = 0; i < N; i++) {
            double xi = generateUniformRandomNumber();
            double mu = calculateMu();
            double x = mu + a;

            sum += x;
            sumSquared += x * x;

            if (x > maxVal) maxVal = x;
            if (x < minVal) minVal = x;

            updateHistogram(x);
        }
    }

    private static double generateUniformRandomNumber() {
        return random.nextDouble();
    }

    private static double calculateMu() {
        double result = 0;
        for (int i = 0; i < 12; i++) {
            result += generateUniformRandomNumber();
        }
        return result - 6;
    }

    public static void updateHistogram(double value) {
        int bin = (int) ((value - minVal) / (maxVal - minVal) * BINS);
        if (bin >= BINS) bin = BINS - 1;
        frequency[bin]++;
    }


    // Внутрішній клас для створення панелі гістограми
    static class HistogramPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g); // Виклик методу суперкласу для базового малювання

            // Параметри для малювання гістограми
            int panelWidth = getWidth();
            int panelHeight = getHeight();
            int binWidth = panelWidth / BINS;

            int maxFreq = getMaxFrequency(); // Найвища частота серед усіх бінів

            // Малювання кожного біна гістограми
            for (int i = 0; i < BINS; i++) {
                int binHeight = (int) (((double) frequency[i] / maxFreq) * panelHeight); // Висота стовпця
                g.fillRect(i * binWidth, panelHeight - binHeight, binWidth, binHeight); // Малювання стовпця

                // Малювання інтервальних значень для кожного стовпця
                String value = String.format("%.2f", (minVal + (maxVal - minVal) * i / BINS));
                g.setColor(Color.RED); // Зміна кольору для тексту
                g.drawString(value, i * binWidth, panelHeight - binHeight / 2); // Малювання значення
                g.setColor(Color.BLACK); // Відновлення кольору для наступного стовпця
            }
        }

        // Метод для визначення максимальної частоти серед усіх бінів гістограми
        private int getMaxFrequency() {
            //Код для пошуку максимальної частоти
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
