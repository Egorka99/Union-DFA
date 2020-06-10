import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;


public class FileInputOutput {
    public static DFA read(String filePath) throws IOException {
        StringBuilder fileInput = new StringBuilder();
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream(filePath), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            // log error
        }

        List<String> states = Arrays.asList(lines.get(0).split(" "));
        List<Character> alphabet = Arrays.stream(lines.get(1).split(" ")).map(s -> s.charAt(0)).collect(Collectors.toList());
        String startState = lines.get(2);
        List<String> finalStates = Arrays.asList(lines.get(3).split(" "));

        int countOfTransitions = Integer.parseInt(lines.get(4));

        Map<Map.Entry<String, Character>, String> transitionTable = new HashMap<>();
        for (int i = 0; i < countOfTransitions; i++) {
            String[] transitionArray = lines.get(5 + i).split(" ");
            transitionTable.put(Map.entry(transitionArray[0], transitionArray[1].charAt(0)), transitionArray[2]);
        }

        return new DFA(alphabet, states, startState, finalStates, transitionTable);

    }

    public static void write(String filePath, DFA dfa) throws FileNotFoundException {
        PrintStream printStream = new PrintStream(new FileOutputStream(filePath));

        printStream.println("Alphabet: ");
        dfa.getAlphabet().forEach(printStream::println);
        printStream.println("States: ");
        dfa.getStates().forEach(printStream::println);
        printStream.println("Initial State: " + dfa.getStartState());
        printStream.println("Final States:");
        dfa.getFinalStates().forEach(printStream::println);
        printStream.println("Transitions");
        dfa.getTransitionTable().forEach((r, c) -> printStream.println(r.getKey() + " - " + r.getValue() + " : " + c));

    }
}
