package step2;

import java.util.*;

class Calculator {
    private Deque<String> list;

    public Calculator(){
        list = new ArrayDeque<>();
    }

    public String getList(){
        return toString(this.list);
    }

    public void setList(String result){
        this.list.addLast(result);
    }

    public void removeList(){
        this.list.removeFirst();
    }

    public int calculate(int a, char oper, int b){
        if (oper == '+') return a + b;
        else if (oper == '-') return a - b;
        else if (oper == '*') return a * b;
        else {
            try {
                return a / b;
            } catch (ArithmeticException e) {
                throw new ArithmeticException();
            }
        }
    }

    public String toString(Deque<String> list){
        StringBuilder sb = new StringBuilder();

        int id = 1;

        for (String s : list) sb.append(id++).append("번 연산 결과 : ").append(s).append("\n");

        return sb.toString();
    }
}

public class Main {
    public static void main(String[] args) {
        Scanner in =  new Scanner(System.in);
        Calculator calculator = new Calculator();

        while (true){
            System.out.println("============= 계산기 프로그램입니다. =============");
            System.out.println("1. 계산하기");
            System.out.println("2. 계산 히스토리 보기");
            System.out.println("3. 최근 히스토리 삭제");
            System.out.println("4. 계산기 종료");

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
                int firstNum = 0;

                while (true){
                    System.out.println("첫 번째 숫자(양의 정수)를 입력해 주세요.");

                    try {
                        firstNum = in.nextInt();
                        in.nextLine();
                        break;
                    } catch (InputMismatchException e) {
                        in.nextLine();
                        System.out.println("올바른 숫자를 입력 하셔야 합니다.");
                    }
                }

                char oper = ' ';

                HashSet<Character> hs = new HashSet<>();
                hs.add('+');
                hs.add('-');
                hs.add('*');
                hs.add('/');

                while (true){
                    System.out.println("연산을 할 연산자를 입력해주세요. (+ - * /)");

                    String input = in.next();
                    in.nextLine();

                    if (input.length() == 1 && hs.contains(input.charAt(0))) {
                        oper = input.charAt(0);
                        break;
                    } else {
                        System.out.println("올바른 연산자를 입력 하셔야 합니다.");
                    }
                }

                int secondNum = 0;

                while (true){
                    System.out.println("두 번째 숫자(양의 정수)를 입력해 주세요.");

                    try {
                        secondNum = in.nextInt();
                        in.nextLine();
                        break;
                    } catch (InputMismatchException e) {
                        in.nextLine();
                        System.out.println("올바른 숫자를 입력 하셔야 합니다.");
                    }
                }

                int res = 0;
                boolean divideZero = false;

                try {
                    res = calculator.calculate(firstNum, oper, secondNum);
                } catch (ArithmeticException e) {
                    divideZero = true;
                }

                String history = "";

                if (!divideZero){
                    history = "계산결과 : " + firstNum + " " + oper + " " + secondNum + " = " + res;
                    System.out.println(history);
                    System.out.println();
                } else {
                    history = "계산결과 : 0으로 나눌 수 없습니다.";
                    System.out.println(history);
                    System.out.println();
                }

                calculator.setList(history);
            } else if (c == 2){
                System.out.println();
                System.out.print(calculator.getList());
                System.out.println();
            } else if (c == 3){
                calculator.removeList();
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
