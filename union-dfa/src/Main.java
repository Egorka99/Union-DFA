import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        DFA dfa1 = FileInputOutput.read("input1.txt");
        DFA dfa2 = FileInputOutput.read("input2.txt");
        System.out.println("Чтение из файла..");
        DFABuilder dfaBuilder = new DFABuilder(dfa1, dfa2);
        FileInputOutput.write("output.txt", dfaBuilder.build());
        System.out.println("Запись в файл..");
        System.out.println("Успешно!");
    }
}
