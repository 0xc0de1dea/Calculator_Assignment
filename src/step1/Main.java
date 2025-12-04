package step1;

import java.util.HashSet;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner in =  new Scanner(System.in);

        while (true){
            System.out.println("============= 계산기 프로그램입니다. =============");

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

            

            System.out.println("더 계산하시겠습니까? (exit 입력시 종료 / 그 외의 아무거나 입력시 계속)");

            String choice = in.next();

            if (choice.equals("exit")){
                System.out.println("============= 계산기를 종료합니다. =============");
                break;
            }
        }
    }
}
