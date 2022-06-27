import java.io.*;
import java.lang.reflect.Array;
import java.math.*;
import java.security.*;
import java.text.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.*;
import java.util.regex.*;
import java.util.stream.*;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

public class InterviewPrep {


    public static List<Integer> getRank(List<Integer> inArray) {
        // Sort array to determine rank
        List<Integer> sortedArray = new ArrayList<>(inArray);
        Collections.sort(sortedArray, Collections.reverseOrder());

        List<Integer> result = new ArrayList<>();
        int currentRank = 0;
        Map<Integer, Queue<Integer>> rankMap = new HashMap<>();

        for (int i = 0; i < inArray.size(); i++) {
            int currentNumber = sortedArray.get(i);
            if (!rankMap.keySet().contains(currentNumber)) {
                rankMap.put(currentNumber, new ArrayDeque<>());
            }

            currentRank += 1;
            rankMap.get(currentNumber).add(currentRank);

        }

        for (int i = 0; i < inArray.size(); i++) {
            int number = inArray.get(i);
            int rank = rankMap.get(number).remove();
            result.add(rank);
        }


        return result;
    }


    public static List<String> getUniqueStrings(List<String> strArr1, List<String> strArr2) {
        Set<String> resultSet = new HashSet<>();

        for (int i = 0; i < strArr1.size(); i++) {
            resultSet.add(strArr1.get(i).toLowerCase());
        }

        for (int i = 0; i < strArr2.size(); i++) {
            resultSet.add(strArr2.get(i).toLowerCase());
        }


        List<String> result = new ArrayList<>(resultSet);
        Collections.sort(result);

        return result;

    }

    static class SuperStack {
        private LinkedList<Integer> stack = new LinkedList<>();

        public void printHead(){
//            System.out.println(stack);
            if(stack.isEmpty()){
                System.out.println("EMPTY");
            } else {
                System.out.println(stack.getLast());
            }
//            System.out.println();
        }

        public void push(Integer i){
            this.stack.add(i);
            this.printHead();
        }

        public void pop(){
            this.stack.removeLast();
            this.printHead();
        }

        public void increment(Integer maxIndex, Integer value){
            ListIterator<Integer> it = this.stack.listIterator();

            for (int i = 0; i < maxIndex; i++) {
                it.set(it.next() + value);

            }

            printHead();

        }
    }


    static void superStack(String[] operations) {
        SuperStack stack = new SuperStack();

        for (int i = 0; i < operations.length; i++) {
            String[] methodCall = operations[i].split(" ");
//            System.out.println(Arrays.asList(methodCall));

            if(methodCall[0].equals("push")){
                stack.push(Integer.parseInt(methodCall[1]));
            } else if (methodCall[0].equals("pop")){
                stack.pop();
            } else {
                stack.increment(Integer.parseInt(methodCall[1]), Integer.parseInt(methodCall[2]));
            }
        }

    }


    public static void main(String[] argv) throws Exception {


        String[] operations = {"push 4", "pop", "push 3", "push 5", "push 2", "inc 3 1", "pop", "push 1", "inc 2 2", "push 4", "pop", "pop"};
//        String[] operations = {"push 4", "push 5", "inc 2 1", "pop", "pop"};
        superStack(operations);


//        List<String> result =  new ArrayList<>();
//        Collections.addAll(result, argv);
//        System.out.println(result);


//        List<String> l1 = Arrays.asList("ab", "Cd", "AB", "e");
//        List<String> l2 = Arrays.asList("abc", "CD", "BA", "E");
//
//        System.out.println(InterviewPrep.getUniqueStrings(l1, l2));


//        List<Integer> l1 = Arrays.asList(5, 1, 3, 2, 4);
//        List<Integer> l2 = Arrays.asList(4, 1, 3, 1, 4);
//
//        System.out.println(InterviewPrep.getRank(l1));
//        System.out.println(InterviewPrep.getRank(l2));




    }



    class AdditionMagic{

        private final DecimalFormat df = new DecimalFormat("0.0");

        public double round(double value, int places) {
            if (places < 0) throw new IllegalArgumentException();

            long factor = (long) Math.pow(10, places);
            value = value * factor;
            long tmp = Math.round(value);
            return (double) tmp / factor;
        }


        public String add(double a, String b){
            return String.format("%d%s", a, b);
        }

        public String add(double a, double b){
            return String.format("%f", round(a + b, 2));
        }

        public String add(String a, String b){
            return String.format("%s,%s", a, b);
        }

    }
}
