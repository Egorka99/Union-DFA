import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DFABuilder {
    private DFA firstDfa;
    private DFA secondDfa;

    public DFABuilder(DFA firstDfa, DFA secondDfa) {
        this.firstDfa = firstDfa;
        this.secondDfa = secondDfa;
    }

    public DFA build() {
        return null;
    }

    private static DFA multiplyDFA(DFA firstMultiplier, DFA secondMultiplier) {
        List<Character> alphabet = firstMultiplier.getAlphabet();
        List<String> states = new ArrayList<>();
        List<String[]> statesArray = new ArrayList<>();
        List<String> finalStates = new ArrayList<>();
        String startState;
        Map<Map.Entry<String, Character>, String> transitionTable = new HashMap<>();

        for (String firstDfaState : firstMultiplier.getStates()) {
            for (String secondDfaState : secondMultiplier.getStates()) {
                String newState = "(" + firstDfaState + " , " + secondDfaState + ")";
                statesArray.add(new String[]{firstDfaState, secondDfaState});
                states.add(newState);
            }
        }
        startState = "(" + firstMultiplier.getStartState() + " , " + secondMultiplier.getStartState() + ")";

        for (String firstDfaFinalState : firstMultiplier.getFinalStates()) {
            for (String secondDfaFinalState : secondMultiplier.getFinalStates()) {
                String newFinalState = "(" + firstDfaFinalState + " , " + secondDfaFinalState + ")";
                finalStates.add(newFinalState);
            }
        }

        for (int i = 0; i < statesArray.size(); i++) {
            for (Character symbol : alphabet) {
                Map.Entry funcParam = Map.entry(states.get(i), symbol);
                String funcValue = "(" + firstMultiplier.getTransitionTable().get(Map.entry(statesArray.get(i)[0], symbol)) + " , " +
                        secondMultiplier.getTransitionTable().get(Map.entry(statesArray.get(i)[1], symbol)) + ")";
                transitionTable.put(funcParam, funcValue);
            }
        }

        return new DFA(alphabet, states, startState, finalStates, transitionTable);

    }

    public static void main(String[] args) {
        DFA firstDfa = new DFA(List.of('a', 'b'), List.of("q0", "q1"), "p0", List.of("q0"),
                Map.of(
                        Map.entry("q0", 'a'), "q1",
                        Map.entry("q0", 'b'), "q0",
                        Map.entry("q1", 'a'), "q0",
                        Map.entry("q1", 'b'), "q1"

                ));
        DFA secondDfa = new DFA(List.of('a', 'b'), List.of("p0", "p1", "p2"), "p0", List.of("p2"),
                Map.of(
                        Map.entry("p0", 'a'), "p0",
                        Map.entry("p0", 'b'), "p1",
                        Map.entry("p1", 'a'), "p0",
                        Map.entry("p1", 'b'), "p2",
                        Map.entry("p2", 'a'), "p2",
                        Map.entry("p2", 'b'), "p2"
                ));

        DFA dfa = multiplyDFA(firstDfa, secondDfa);
        System.out.println("Alphabet: ");
        dfa.getAlphabet().forEach(System.out::println);
        System.out.println("States: ");
        dfa.getStates().forEach(System.out::println);
        System.out.println("Initial State: " + dfa.getStartState());
        System.out.println("Final States:");
        dfa.getFinalStates().forEach(System.out::println);
        System.out.println("Transitions");
        dfa.getTransitionTable().forEach((r,c) -> System.out.println(r.getKey() + " - " + r.getValue() + " : " + c));
    }


}
