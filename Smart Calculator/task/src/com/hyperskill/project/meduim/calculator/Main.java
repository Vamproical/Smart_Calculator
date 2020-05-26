package com.hyperskill.project.meduim.calculator;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String nextExpression = scanner.nextLine();
            String[] arrayNextExpression = nextExpression.split(" ");
            if (nextExpression.equals("/exit")) {
                System.out.println("Bye!");
                break;
            }
            else if (nextExpression.equals("/help")) {
                System.out.println("The program calculates the sum of numbers");
            }
            else if (arrayNextExpression.length == 0 || nextExpression.isEmpty()) {
                continue;
            }
            else if (arrayNextExpression.length == 1) {
                System.out.println(arrayNextExpression[0]);
            }
            else {
                int[] arrayNumbers = new int[arrayNextExpression.length];
                int result = 0;
                for (int i = 0; i < arrayNumbers.length; i++) {
                    arrayNumbers[i] = Integer.parseInt(arrayNextExpression[i]);
                    result += arrayNumbers[i];
                }
                System.out.println(result);
            }

        }
    }
}
