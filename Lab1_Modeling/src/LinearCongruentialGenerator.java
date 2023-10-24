public class LinearCongruentialGenerator {

    // Константи, що визначаються для LCG. N - кількість чисел, які потрібно згенерувати.
    private static final int N = 10000;

    // Параметри для лінійного конгруентного рівняння.
    // A - множник, C - приріст, MOD - модуль, використовується для обмеження розміру згенерованих чисел.
    private static final long A = (long) Math.pow(5, 13); // параметр a
    private static final long C = (long) Math.pow(2, 31); // параметр c
    private static final long MOD = (long) Math.pow(2, 31); // модуль

    // Початкове значення, яке зазвичай базується на часі для забезпечення унікальності.
    private static long z = System.currentTimeMillis() % MOD; // початкове знаення

    public static void main(String[] args) {
        // Цикл, що генерує N псевдовипадкових чисел.
        for (int i = 0; i < N; i++) {
            // Оновлення значення 'z' з використанням лінійного конгруентного рівняння.
            z = (A * z) % MOD;
            // Перетворення 'z' у число з плаваючою комою в інтервалі (0;1), діленням на 'C'.
            double x = (double) z / C;

            // Вивід згенерованого числа.
            System.out.println(x);

            // Перевірка, чи знаходиться згенероване число в інтервалі (0;1) для підтвердження рівномірності розподілу.
            if (x < 0 || x > 1) {
                System.out.println("Число " + x + " не відповідає рівномірному закону розподілу");
            }
        }
    }
}
