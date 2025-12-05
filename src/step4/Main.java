package step4;

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
    private HashMap<String, Integer> priority;

    public ArithmeticCalculator(){
        list = new ArrayDeque<>();
        priority = new HashMap<>();

        priority.put("+", 1);
        priority.put("-", 1);
        priority.put("*", 2);
        priority.put("/", 2);
        priority.put("(", 0);
        priority.put(")", 0);
    }

    public HashMap<String, Integer> getPriority() {
        return this.priority;
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

    public String convertInfixToPostfix(String ori){
        StringBuilder postfix = new StringBuilder();
        Deque<String> stack = new ArrayDeque<>();

        String[] infix = ori.split(" ");

        for (String s : infix){
            if (!priority.containsKey(s)){
                postfix.append(s).append(' ');
            } else if (s.equals("(")){
                stack.push(s);
            } else if (s.equals(")")){
                while (!stack.isEmpty() && !stack.peek().equals("(")){
                    postfix.append(stack.pop()).append(' ');
                }

                stack.pop();
            } else {
                while (!stack.isEmpty() && priority.get(stack.peek()) >= priority.get(s)){
                    postfix.append(stack.pop()).append(' ');
                }

                stack.push(s);
            }
        }

        while (!stack.isEmpty()){
            postfix.append(stack.pop()).append(' ');
        }

        return postfix.delete(postfix.length() - 1, postfix.length()).toString();
    }

    public double calculatePostfix(String ori){
        Deque<Double> stack = new ArrayDeque<>();

        String[] postfix = ori.split(" ");

        for (String s : postfix){
            if (!priority.containsKey(s)){
                stack.push(Double.parseDouble(s));
            } else {
                T b = (T) stack.pop();
                T a = (T) stack.pop();

                try {
                    stack.push(calculate(a, s.charAt(0), b));
                } catch (ArithmeticException e) {
                    throw new ArithmeticException();
                }
            }
        }

        return stack.pop();
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
        ArithmeticCalculator<Number> calculator = new ArithmeticCalculator<>();

        while (true) {
            System.out.println("============= 계산기 프로그램입니다. =============");
            System.out.println("1. 간단한 계산하기");
            System.out.println("2. 계산식을 이용한 계산하기");
            System.out.println("3. 임의의 값보다 큰 계산식을 히스토리에서 찾기");
            System.out.println("4. 계산기 종료");

            int c = 0;

            while (true) {
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

                while (true) {
                    System.out.println("첫 번째 숫자를 입력해 주세요.");

                    try {
                        String tmp = in.next();
                        in.nextLine();

                        if (isInt(tmp)) {
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

                while (true) {
                    System.out.println("연산을 할 연산자를 입력해주세요. (+ - * /)");

                    String input = in.next();
                    in.nextLine();

                    boolean flag = false;

                    if (input.length() == 1) {
                        for (OperatorType op : OperatorType.values()) {
                            if (op.getSymbol() == input.charAt(0)) {
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

                while (true) {
                    System.out.println("두 번째 숫자를 입력해 주세요.");

                    try {
                        String tmp = in.next();
                        in.nextLine();

                        if (isInt(tmp)) {
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
                    res = calculator.calculate(firstNum.getValue(), oper, secondNum.getValue());
                } catch (ArithmeticException e) {
                    divideZero = true;
                }

                String history = "";

                if (!divideZero) {
                    history = "계산결과 : " + firstNum.toDouble() + " " + oper + " " + secondNum.toDouble() + " = " + res;
                    System.out.println(history);
                    System.out.println();
                } else {
                    history = "계산결과 : 0으로 나눌 수 없습니다.";
                    System.out.println(history);
                    System.out.println();
                }

                calculator.setList(history);
            } else if (c == 2){
                while (true){
                    System.out.println("계산식을 입력해주세요. ex) ");

                    String infix = in.nextLine();

                    HashMap<String, Integer> priority = calculator.getPriority();

                    boolean flag = true;

                    for (String s : infix.split(" ")){
                        if (!priority.containsKey(s)){
                            if (!isInt(s) && !isDouble(s)){
                                flag = false;
                                break;
                            }
                        }
                    }

                    if (!flag){
                        System.out.println("올바른 계산식을 입력해주세요.");
                    } else {
                        String postfix = calculator.convertInfixToPostfix(infix);
                        double res = 0;

                        try {
                            res = calculator.calculatePostfix(postfix);
                            System.out.println("계산결과 : " + infix + " = " + res);
                            calculator.setList("계산결과 : " + infix + " = " + res);
                        } catch (ArithmeticException e) {
                            System.out.println("계산결과 : 0으로 나눌 수 없습니다.");
                            calculator.setList("계산결과 : 0으로 나눌 수 없습니다.");
                        }

                        break;
                    }
                }
            } else if (c == 3){
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

                System.out.print(calculator.getConditionList(trg));
                System.out.println();
            } else if (c == 4){
                System.out.println("============= 계산기를 종료합니다. =============");
                break;
            } else {
                System.out.println("올바른 선택이 아닙니다.");
                System.out.println();
            }
        }
    }
}
