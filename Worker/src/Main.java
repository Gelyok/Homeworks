public class Main {
    public static void main(String[] args) {
        OnTaskDoneListener doneListener = System.out::println;

        OnTaskErrorListener errorListener = System.err::println;

        Worker worker = new Worker(doneListener, errorListener);

        worker.start();
    }
}