package com.hyperskill.project.meduim.calculator;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Calculator {
    private final Map<String,Integer> variables = new HashMap<>();
    private String checkOp(String p, String op){
        if (op.equals(p) && p.equals("-")) return "+";
        else if (op.equals("+") && p.equals("-")) return "-";
        else return "+";
    }
    private boolean checkPrefix(String input) {
        boolean isExit = false;
        switch (input) {
            case "/exit":
                System.out.println("Bye!");
                isExit = true;
                break;
            case "/help":
                System.out.println("The program calculates the sum of numbers");
                break;
            default:
                System.out.println("Unknown command");
                break;
        }
        return isExit;
    }
    private void createVariable(String input) {
        String[] arrayString = input.split("\\s*=\\s*");
        if  (!arrayString[0].matches("[a-zA-Z]+")) System.out.println("Invalid identifier");
        else if (!arrayString[0].matches("[a-zA-Z]+|[0-9]") || arrayString.length > 2) System.out.println("Invalid assignment");
        else if (arrayString[1].matches("[a-zA-Z]+[0-9]|[a-zA-Z]+[0-9]+[a-zA-Z]+")) System.out.println("Invalid assignment");
        else if (arrayString[1].matches("[a-zA-Z]+")) {
            if (variables.containsKey(arrayString[1])) {
                variables.put(arrayString[0],variables.get(arrayString[1]));
            }
            else {
                System.out.println("Unknown variable");
            }
        }
        else {
            variables.put(arrayString[0],Integer.parseInt(arrayString[1]));
        }
    }
    private void checkValue(String input) {
        boolean isValue = true;
        for (var str: variables.entrySet()) {
            if (str.getKey().equals(input)) {
                System.out.println(str.getValue());
                isValue = false;
            }
        }
        if (isValue) System.out.println("Unknown variable");
    }
    public void calculate() {
        final Scanner scanner = new Scanner(System.in);
        String operation = "+";
        Pattern pattern = Pattern.compile("[+-]?(\\d+|[a-zA-Z]+)");
        Matcher matcher;
        while (true) {
            String nextExpression = scanner.nextLine();
            Pattern pattern1 = Pattern.compile("\\d+[+-]|\\d+ \\d+");
            Matcher matcher1 = pattern1.matcher(nextExpression);
            int result = 0;
            if (nextExpression.startsWith("/"))  {
                if (checkPrefix(nextExpression)) break;
            }
            else if (nextExpression.contains("=")) {
                createVariable(nextExpression);
            }
            else if (nextExpression.matches("[a-zA-Z]+")) {
                checkValue(nextExpression);
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
                            if (str.matches("[a-zA-Z]+")) {
                                result -= variables.get(str);
                            }
                            else {
                                result -= Integer.parseInt(str);
                            }
                            operation = "+";
                        } else if (operation.equals("+")) {
                            if (str.matches("[a-zA-Z]+")) {
                                result += variables.get(str);
                            }
                            else {
                                result += Integer.parseInt(str);
                            }
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
