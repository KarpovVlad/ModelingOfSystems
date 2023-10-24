import javax.swing.*;
import java.awt.*;

public class LinearCongruentialGeneratorGUI {
    // Оголошення констант, які будуть використовуватися в генераторі та ініціалізація масиву для частот.
    private static final int N = 10000;
    private static final int BINS = 50;
    private static final long A = (long) Math.pow(5, 13);
    private static final long C = (long) Math.pow(2, 31);
    private static final long MOD = (long) Math.pow(2, 31);

    private static long z = System.currentTimeMillis() % MOD;
    private static int[] frequency = new int[BINS];
    private static double maxVal = 1.0;
    private static double minVal = 0.0;
    // Змінні для обчислення статистичних показників.
    private static double sum = 0;
    private static double sumOfSquares = 0;

    public static void main(String[] args) {
        // Генерування даних та обчислення статистичних показників.
        generateData();

        double mean = sum / N;
        double variance = (sumOfSquares / N) - (mean * mean);
        double standardDeviation = Math.sqrt(variance);

        System.out.println("Середнє: " + mean);
        System.out.println("Дисперсія: " + variance);
        System.out.println("Стандартне відхилення: " + standardDeviation);
        double chiSquared = calculateChiSquared();
        System.out.println("Значення χі^2: " + chiSquared);

        // Створення графічного вікна для відображення гістограми.
        JFrame frame = new JFrame("Гістограма частот");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.add(new HistogramPanel());
        frame.setVisible(true);
    }
    // Метод для обчислення значення хі-квадрат.
    private static double calculateChiSquared() {
        double expectedFrequency = (double) N / BINS;
        double chiSquared = 0;

        for (int i = 0; i < BINS; i++) {
            chiSquared += Math.pow(frequency[i] - expectedFrequency, 2) / expectedFrequency;
        }

        return chiSquared;
    }
    // Генерація випадкових чисел і оновлення статистичних змінних та гістограми.
    private static void generateData() {
        for (int i = 0; i < N; i++) {
            z = (A * z) % MOD;
            double x = (double) z / C;

            sum += x;
            sumOfSquares += x * x;

            updateHistogram(x);
        }
    }
    // Оновлення гістограми на основі нового згенерованого значення.
    public static void updateHistogram(double value) {
        int bin = (int) ((value - minVal) / (maxVal - minVal) * BINS);
        if (bin >= BINS) bin = BINS - 1;
        frequency[bin]++;
    }
    // Внутрішній клас для рендерингу гістограми.
    static class HistogramPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            // Метод, який відповідає за візуалізацію гістограми на панелі.
            super.paintComponent(g);
            int panelWidth = getWidth();
            int panelHeight = getHeight();
            int binWidth = panelWidth / BINS;
            // Отримання максимальної частоти для масштабування гістограми.
            int maxFreq = getMaxFrequency();
            // Малювання прямокутників гістограми та відображення значень інтервалів.
            for (int i = 0; i < BINS; i++) {
                int binHeight = (int) (((double) frequency[i] / maxFreq) * panelHeight);
                g.fillRect(i * binWidth, panelHeight - binHeight, binWidth, binHeight);

                // Draw interval values
                String value = String.format("%.2f", (minVal + (maxVal - minVal) * i / BINS));
                g.setColor(Color.RED);
                g.drawString(value, i * binWidth, panelHeight - binHeight / 2);
                g.setColor(Color.BLACK);
            }
        }
        // Метод для знаходження найбільшої частоти в гістограмі, необхідної для масштабування.
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
