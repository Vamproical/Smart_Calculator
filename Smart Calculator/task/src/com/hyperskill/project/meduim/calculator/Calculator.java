package com.hyperskill.project.meduim.calculator;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Calculator {
    private String checkOp(String p, String op){
        if (op.equals(p) && p.equals("-")) return "+";
        else if (op.equals("+") && p.equals("-")) return "-";
        else return "+";
    }
    public void calculate() {
        final Scanner scanner = new Scanner(System.in);
        String operation = "+";
        Pattern pattern = Pattern.compile("[+-]?\\d+");
        Matcher matcher;
        while (true) {
            String nextExpression = scanner.nextLine();
            String regexForCommand = "/[a-zA-Z]+";
            Pattern pattern1 = Pattern.compile("[a-zA-Z]+|\\d+[+-]|\\d+ \\d+");
            Matcher matcher1 = pattern1.matcher(nextExpression);
            int result = 0;
            if (nextExpression.equals("/exit")) {
                System.out.println("Bye!");
                break;
            }
            else if (nextExpression.equals("/help")) {
                System.out.println("The program calculates the sum of numbers");
            }
            else if (nextExpression.matches(regexForCommand)) {
                System.out.println("Unknown command");
            }
            else if (nextExpression.equals("")) continue;
            else if (matcher1.matches()) {
                System.out.println("Invalid expression");
            }
            else {

                for (String str : nextExpression.split(" +")) {
                    matcher = pattern.matcher(str);
                    if (matcher.matches()) {
                        if (operation.equals("-")) {
                            result -= Integer.parseInt(str);
                            operation = "+";
                        } else if (operation.equals("+")) {
                            result += Integer.parseInt(str);
                        }

                    } else {
                        char[] charStr = str.toCharArray();
                        if (charStr.length > 1) {
                            for (char c : charStr) {
                                operation = checkOp(Character.toString(c), operation);
                            }
                        } else {
                            operation = checkOp(str, operation);
                        }
                    }
                }
                System.out.println(result);
            }
        }
    }
}
