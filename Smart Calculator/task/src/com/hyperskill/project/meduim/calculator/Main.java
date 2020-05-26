package com.hyperskill.project.meduim.calculator;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static String checkOp(String p, String op){
        if (op.equals(p) && p.equals("-")) return "+";
        else if (op.equals("+") && p.equals("-")) return "-";
        else return "+";
    }
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String operation = "+";
        Pattern pattern = Pattern.compile("[+-]?\\d+");
        Matcher matcher;
        label:
        while (true) {
            String nextExpression = scanner.nextLine();
            int result = 0;
            switch (nextExpression) {
                case "/exit":
                    System.out.println("Bye!");
                    break label;
                case "/help":
                    System.out.println("The program calculates the sum of numbers");
                    break;
                case "":
                    continue;
                default:
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
                    break;
            }
        }
    }
}
