package list;

public class StackTest {
    public static void main(String[] args) {

        Stack<Integer> integerStack = new Stack<>();
        integerStack.push(1);
        integerStack.push(2);
        integerStack.push(3);
        integerStack.push(4);
        integerStack.push(4);
        integerStack.push(4);
        integerStack.push(4);

        while (!integerStack.isEmpty()) {
            System.out.println(integerStack.pop());
        }


        System.out.println(integerStack);


    }
}
