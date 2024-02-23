public class Main {

    public static void main(String[] args) {
        Calculator calc = Calculator.instance.get();

        int a = calc.plus.apply(1, 2);
        int b = calc.minus.apply(1, 1);
        //int c = calc.devide.apply(a, b);
        //calc.println.accept(c);


        //Ошибка попыткой деления на ноль в переменной с = calc.divide.apply(a, b);
        //Если второй аргумент b равен нулю то
        // будет выброшено исключение ArithmeticException

        // Проверка деления на ноль
        if (b != 0) {
            int c = calc.divide.apply(a, b);
            calc.println.accept(c);
        } else {
            System.out.println("Деление на ноль невозможно");
        }
    }
}

