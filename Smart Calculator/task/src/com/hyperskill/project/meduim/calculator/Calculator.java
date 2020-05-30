package com.hyperskill.project.meduim.calculator;

import java.math.BigInteger;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Calculator {
    private final Map<String, Integer> variables = new HashMap<>();
    private final Deque<String> stackForPostfix = new ArrayDeque<>();
    private final Deque<String> stackForCalculate = new ArrayDeque<>();

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
        if (!arrayString[0].matches("[a-zA-Z]+")) System.out.println("Invalid identifier");
        else if (!arrayString[0].matches("[a-zA-Z]+|[0-9]") || arrayString.length > 2)
            System.out.println("Invalid assignment");
        else if (arrayString[1].matches("[a-zA-Z]+[0-9]|[a-zA-Z]+[0-9]+[a-zA-Z]+"))
            System.out.println("Invalid assignment");
        else if (arrayString[1].matches("[a-zA-Z]+")) {
            if (variables.containsKey(arrayString[1])) {
                variables.put(arrayString[0], variables.get(arrayString[1]));
            } else {
                System.out.println("Unknown variable");
            }
        } else {
            variables.put(arrayString[0], Integer.parseInt(arrayString[1]));
        }
    }

    private void checkValue(String input) {
        boolean isValue = true;
        for (var str : variables.entrySet()) {
            if (str.getKey().equals(input)) {
                System.out.println(str.getValue());
                isValue = false;
            }
        }
        if (isValue) System.out.println("Unknown variable");
    }

    private boolean checkExpression(String input) {
        boolean flag = true;
        if (input.startsWith("(") || input.contains("***") || input.contains("//") || input.contains("**")) {
            flag = false;
        }
        String[] checkParentheses = input.split("");
        int firstParent = 0;
        int secondParent = 0;
        for (String str : checkParentheses) {
            if (str.equals("(")) firstParent++;
            if (str.equals(")")) secondParent++;
        }
        if (firstParent != secondParent) flag = false;
        return flag;
    }

    private String replaceIncorrectSymbols(String input) {
        StringBuilder result = new StringBuilder(input.replaceAll(" ", ""));
        for (int i = 1; i < result.length(); i++) {
            char ch = result.charAt(i);
            if (ch == '+' && ch == result.charAt(i - 1)) {
                result.deleteCharAt(i - 1);
                i--;
            } else if (ch == '-' && ch == result.charAt(i - 1)) {
                int count = 2;
                int j = i + 1;
                for (; j < result.length(); j++) {
                    if (result.charAt(j) == '-') ++count;
                    else break;
                }
                for (int k = i - 1; k < j - 1; k++) {
                    result.deleteCharAt(k);
                }
                result.setCharAt(i - 1, count % 2 == 0 ? '+' : '-');
                result.trimToSize();
                i = j - 1;
            }
        }
        return result.toString();
    }

    private int checkPriority(String str) {
        switch (str) {
            case "+":
            case "-":
                return 1;
            case "*":
            case "/":
                return 2;
            case "^":
                return 3;
        }
        return -1;
    }

    private String infixToPostfix(String input) {
        StringBuilder result = new StringBuilder();
        String[] arrayInput = input.split("(?<=\\W)|(?=\\W)");
        for (String firstStringInput : arrayInput) {
            if (firstStringInput.matches("[a-zA-Z]+")) {
                result.append(variables.get(firstStringInput)).append(" ");
            } else if (firstStringInput.matches("\\d+")) {
                result.append(firstStringInput).append(" ");
            } else if (firstStringInput.equals("(")) {
                stackForPostfix.offerLast(firstStringInput);
            } else if (firstStringInput.equals(")")) {
                while (!stackForPostfix.isEmpty() && checkPriority(firstStringInput) != checkPriority(stackForPostfix.peekLast())) {
                    result.append(stackForPostfix.pollLast()).append(" ");
                }
                if (!stackForPostfix.isEmpty() && !stackForPostfix.peekLast().equals("(")) {
                    return "Invalid Expression";
                } else {
                    stackForPostfix.pollLast();
                }
            } else {
                while (!stackForPostfix.isEmpty() && checkPriority(firstStringInput) <= checkPriority(stackForPostfix.peekLast())) {
                    if (stackForPostfix.peekLast().equals("("))
                        return "Invalid Expression";
                    result.append(stackForPostfix.pollLast()).append(" ");
                }
                stackForPostfix.offerLast(firstStringInput);
            }
        }
        while (!stackForPostfix.isEmpty()) {
            if (stackForPostfix.peekLast().equals("("))
                return "Invalid Expression";
            result.append(stackForPostfix.pollLast()).append(" ");
        }
        return result.toString();
    }

    public static BigInteger process(BigInteger y, String o, BigInteger x) {
        switch (o) {
            case "+":
                return y.add(x);
            case "-":
                return y.subtract(x);
            case "*":
                return y.multiply(x);
            case "^":
                return y.pow(Integer.parseInt(String.valueOf(x)));
            default:
                return y.divide(x);
        }
    }

    public void calculate() {
        final Scanner scanner = new Scanner(System.in);
        while (true) {
            String nextExpression = scanner.nextLine();
            Pattern pattern = Pattern.compile("\\d+[+-]|\\d+ \\d+");
            Matcher matcher = pattern.matcher(nextExpression);
            if (nextExpression.startsWith("/")) {
                if (checkPrefix(nextExpression)) break;
            } else {
                if (nextExpression.contains("=")) {
                    createVariable(nextExpression);
                } else if (nextExpression.matches("[a-zA-Z]+")) {
                    checkValue(nextExpression);
                } else if (nextExpression.equals("")) continue;
                else if (matcher.matches() || !checkExpression(nextExpression)) {
                    System.out.println("Invalid expression");
                } else {
                    nextExpression = replaceIncorrectSymbols(nextExpression);
                    String postfixString = infixToPostfix(nextExpression);
                    String[] arrayPostfixNumbers = postfixString.split(" ");
                    if (nextExpression.startsWith("-")) stackForCalculate.offerLast(String.valueOf(0));
                    try {
                        for (String str : arrayPostfixNumbers) {
                            if (str.matches("\\d+")) {
                                stackForCalculate.offerLast(str);
                            } else {
                                BigInteger x = new BigInteger(Objects.requireNonNull(stackForCalculate.pollLast()));
                                BigInteger y = new BigInteger(Objects.requireNonNull(stackForCalculate.pollLast()));
                                BigInteger count = process(y, str, x);
                                stackForCalculate.offerLast(String.valueOf(count));
                            }
                        }
                    } catch (Throwable t) {
                        System.out.println("Invalid expression");
                        break;
                    }
                    System.out.println(stackForCalculate.pollLast());
                }
            }
        }
    }
}
