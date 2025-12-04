package step3;

import java.util.*;

@FunctionalInterface
interface Operator {
    double calculate(double a, double b);
}

enum OperatorType {
    ADD('+', (a, b) -> a + b),
    SUBTRACT('-', (a, b) -> a - b),
    MULTIPLY('*', (a, b) -> a * b),
    DIVIDE('/', (a, b) -> {
        if (b == 0) throw new ArithmeticException();
        return a / b;
    });

    private final char symbol;
    private final Operator operation;

    OperatorType(char symbol, Operator operation) {
        this.symbol = symbol;
        this.operation = operation;
    }

    public double calculate(double a, double b){
        return operation.calculate(a, b);
    }

    public char getSymbol() {
        return symbol;
    }
}

class NumberBox {
    private Number val;

    public NumberBox(Number val) {
        this.val = val;
    }

    public Number getValue() {
        return val;
    }

    public double toDouble() {
        return val.doubleValue();
    }

    public int toInt() {
        return val.intValue();
    }
}

class ArithmeticCalculator<T extends Number> {
    private Deque<String> list;

    public ArithmeticCalculator(){
        list = new ArrayDeque<>();
    }

    public String getList(){
        return toString(this.list);
    }

    public String getConditionList(double trg){
        return this.list.stream().filter(s -> {
            String[] split = s.split("= ");

            double res = Double.parseDouble(split[1].trim());

            return res > trg;
        }).collect(
            StringBuilder::new,
            (sb, s) -> sb.append(s).append('\n'),
            StringBuilder::append
        ).toString();
    }

    public void setList(String result){
        this.list.addLast(result);
    }

    public void removeList(){
        this.list.removeFirst();
    }

    public double calculate(T a, char oper, T b){
        double x = a.doubleValue();
        double y = b.doubleValue();

        OperatorType operType = null;

        for (OperatorType op : OperatorType.values()){
            if (op.getSymbol() == oper){
                operType = op;
                break;
            }
        }

        double res = operType.calculate(x, y);

        return res;
    }

    public String toString(Deque<String> list){
        StringBuilder sb = new StringBuilder();

        int id = 1;

        for (String s : list) sb.append(id++).append("번 연산 결과 : ").append(s).append("\n");

        return sb.toString();
    }
}

public class Main {
    public static boolean isInt(String s) {
        return s.matches("^-?\\d+$");
    }

    public static boolean isDouble(String s) {
        return s.matches("^-?\\d+\\.\\d+$");
    }

    public static void main(String[] args) {
        Scanner in =  new Scanner(System.in);
        ArithmeticCalculator<Integer> calculatorInt = new ArithmeticCalculator<>();
        ArithmeticCalculator<Double> calculatorDouble = new ArithmeticCalculator<>();

        while (true){
            System.out.println("============= 계산기 프로그램입니다. =============");
            System.out.println("1. 계산하기");
            System.out.println("2. 임의의 값보다 큰 계산식을 히스토리에서 찾기");
            System.out.println("3. 계산기 종료");

            int c = 0;

            while (true){
                try {
                    c = in.nextInt();
                    in.nextLine();
                    break;
                } catch (InputMismatchException e) {
                    in.nextLine();
                    System.out.println("올바른 선택을 해주세요.");
                }
            }

            if (c == 1) {
                NumberBox firstNum = null;

                while (true){
                    System.out.println("첫 번째 숫자를 입력해 주세요.");

                    try {
                        String tmp = in.next();
                        in.nextLine();

                        if (isInt(tmp)){
                            firstNum = new NumberBox(Integer.parseInt(tmp));
                        } else {
                            firstNum = new NumberBox(Double.parseDouble(tmp));
                        }

                        break;
                    } catch (InputMismatchException e) {
                        in.nextLine();
                        System.out.println("올바른 숫자를 입력 하셔야 합니다.");
                    }
                }

                char oper = ' ';

                while (true){
                    System.out.println("연산을 할 연산자를 입력해주세요. (+ - * /)");

                    String input = in.next();
                    in.nextLine();

                    boolean flag = false;

                    if (input.length() == 1){
                        for (OperatorType op : OperatorType.values()){
                            if (op.getSymbol() == input.charAt(0)){
                                flag = true;
                                break;
                            }
                        }
                    }

                    if (flag) {
                        oper = input.charAt(0);
                        break;
                    } else {
                        System.out.println("올바른 연산자를 입력 하셔야 합니다.");
                    }
                }

                NumberBox secondNum = null;

                while (true){
                    System.out.println("두 번째 숫자를 입력해 주세요.");

                    try {
                        String tmp = in.next();
                        in.nextLine();

                        if (isInt(tmp)){
                            secondNum = new NumberBox(Integer.parseInt(tmp));
                        } else {
                            secondNum = new NumberBox(Double.parseDouble(tmp));
                        }

                        break;
                    } catch (InputMismatchException e) {
                        in.nextLine();
                        System.out.println("올바른 숫자를 입력 하셔야 합니다.");
                    }
                }

                double res = 0;
                boolean divideZero = false;

                try {
                    if (isInt(firstNum.getValue().toString()) && isInt(secondNum.getValue().toString())){
                        res = calculatorInt.calculate(firstNum.toInt(), oper, secondNum.toInt());
                    } else {
                        res = calculatorDouble.calculate(firstNum.toDouble(), oper, secondNum.toDouble());
                    }
                } catch (ArithmeticException e) {
                    divideZero = true;
                }

                String history = "";

                if (!divideZero){
                    history = "계산결과 : " + firstNum.getValue().doubleValue() + " " + oper + " " + secondNum.getValue().doubleValue() + " = " + res;
                    System.out.println(history);
                    System.out.println();
                } else {
                    history = "계산결과 : 0으로 나눌 수 없습니다.";
                    System.out.println(history);
                    System.out.println();
                }

                calculatorDouble.setList(history);
            } else if (c == 2){
                double trg = 0;
                System.out.println("임의의 값을 선택하세요.");

                while (true){
                    try {
                        trg = in.nextDouble();
                        in.nextLine();
                        break;
                    } catch (InputMismatchException e) {
                        in.nextLine();
                        System.out.println("올바른 값을 입력하세요.");
                    }
                }

                System.out.print(calculatorDouble.getConditionList(trg));
                System.out.println();
            } else if (c == 3){
                System.out.println("============= 계산기를 종료합니다. =============");
                break;
            } else {
                System.out.println("올바른 선택이 아닙니다.");
                System.out.println();
            }
        }
    }
}
