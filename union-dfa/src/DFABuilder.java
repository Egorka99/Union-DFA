import java.util.*;
import java.util.stream.Collectors;

public class DFABuilder {
    private DFA firstDfa;
    private DFA secondDfa;

    public DFABuilder(DFA firstDfa, DFA secondDfa) {
        this.firstDfa = firstDfa;
        this.secondDfa = secondDfa;
    }

    public DFA build() {
        return removeUnreachableStates(multiplyDFA());
    }

    private DFA removeUnreachableStates(DFA unionDfa) {
        List<String> reachableStates = new ArrayList<>(List.of(unionDfa.getStates().get(0)));
        List<String> newStates = new ArrayList<>(List.of(unionDfa.getStates().get(0)));

        do {
            List<String> temp = new ArrayList<>();
            for (String q : newStates) {
                for (Character c : unionDfa.getAlphabet()) {
                    temp.add(unionDfa.getTransitionTable().get(Map.entry(q, c)));
                }
            }
            newStates.addAll(temp);
            newStates.removeAll(reachableStates);
            reachableStates.addAll(newStates);
        } while (newStates.size() != 0);

        List<String> unreachableStates = unionDfa.getStates().stream().filter(s -> !reachableStates.contains(s)).collect(Collectors.toList());

        List<String> newDfaStates =  unionDfa.getStates().stream().filter(s -> !unreachableStates.contains(s)).collect(Collectors.toList());
        List<String> newDfaFinalStates =  unionDfa.getFinalStates().stream().filter(s -> !unreachableStates.contains(s)).collect(Collectors.toList());
        Set<Map.Entry<Map.Entry<String, Character>, String>> newDfaTransitionsEntrySet = unionDfa.getTransitionTable().entrySet().stream()
                .filter(e -> !unreachableStates.contains(e.getKey().getKey())).collect(Collectors.toSet());
        Map<Map.Entry<String, Character>, String> newDfaTransitions = newDfaTransitionsEntrySet.stream().collect(Collectors.toMap(Map.Entry::getKey,Map.Entry::getValue));

        return new DFA(unionDfa.getAlphabet(),newDfaStates,unionDfa.getStartState(),newDfaFinalStates, newDfaTransitions);
    }

    private DFA multiplyDFA() {
        List<Character> alphabet = firstDfa.getAlphabet();
        List<String> states = new ArrayList<>();
        List<String[]> statesArray = new ArrayList<>();
        List<String> finalStates = new ArrayList<>();
        String startState;
        Map<Map.Entry<String, Character>, String> transitionTable = new HashMap<>();

        for (String firstDfaState : firstDfa.getStates()) {
            for (String secondDfaState : secondDfa.getStates()) {
                String newState = "(" + firstDfaState + " , " + secondDfaState + ")";
                statesArray.add(new String[]{firstDfaState, secondDfaState});
                states.add(newState);
            }
        }
        startState = "(" + firstDfa.getStartState() + " , " + secondDfa.getStartState() + ")";

        for (String firstDfaFinalState : firstDfa.getFinalStates()) {
            for (String secondDfaFinalState : secondDfa.getFinalStates()) {
                String newFinalState = "(" + firstDfaFinalState + " , " + secondDfaFinalState + ")";
                finalStates.add(newFinalState);
            }
        }

        for (int i = 0; i < statesArray.size(); i++) {
            for (Character symbol : alphabet) {
                Map.Entry funcParam = Map.entry(states.get(i), symbol);
                String funcValue = "(" + firstDfa.getTransitionTable().get(Map.entry(statesArray.get(i)[0], symbol)) + " , " +
                        secondDfa.getTransitionTable().get(Map.entry(statesArray.get(i)[1], symbol)) + ")";
                transitionTable.put(funcParam, funcValue);
            }
        }

        return new DFA(alphabet, states, startState, finalStates, transitionTable);

    }

    public static void main(String[] args) {
//        DFA firstDfa = new DFA(List.of('a', 'b'), List.of("q0", "q1"), "p0", List.of("q0"),
//                Map.of(
//                        Map.entry("q0", 'a'), "q1",
//                        Map.entry("q0", 'b'), "q0",
//                        Map.entry("q1", 'a'), "q0",
//                        Map.entry("q1", 'b'), "q1"
//
//                ));
//        DFA secondDfa = new DFA(List.of('a', 'b'), List.of("p0", "p1", "p2"), "p0", List.of("p2"),
//                Map.of(
//                        Map.entry("p0", 'a'), "p0",
//                        Map.entry("p0", 'b'), "p1",
//                        Map.entry("p1", 'a'), "p0",
//                        Map.entry("p1", 'b'), "p2",
//                        Map.entry("p2", 'a'), "p2",
//                        Map.entry("p2", 'b'), "p2"
//                ));
        DFA firstDfa = new DFA(List.of('a', 'b'), List.of("s1", "t1"), "s1", List.of("t1"),
                Map.of(
                        Map.entry("s1", 'a'), "s1",
                        Map.entry("s1", 'b'), "t1",
                        Map.entry("t1", 'a'), "t1",
                        Map.entry("t1", 'b'), "t1"

                ));
                DFA secondDfa = new DFA(List.of('a', 'b'), List.of("s2", "q2", "t21","t22"), "s2", List.of("t21","t22"),
                Map.of(
                        Map.entry("s2", 'b'), "s2",
                        Map.entry("s2", 'a'), "q2",
                        Map.entry("q2", 'a'), "q2",
                        Map.entry("q2", 'b'), "t21",
                        Map.entry("t21", 'a'), "t21",
                        Map.entry("t21", 'b'), "t21",
                        Map.entry("t22", 'a'), "t22",
                        Map.entry("t22", 'b'), "t22"
                ));

        DFABuilder dfaBuilder = new DFABuilder(firstDfa, secondDfa);
        DFA dfa = dfaBuilder.multiplyDFA();
        dfa = dfaBuilder.removeUnreachableStates(dfa);

        System.out.println("Alphabet: ");
        dfa.getAlphabet().forEach(System.out::println);
        System.out.println("States: ");
        dfa.getStates().forEach(System.out::println);
        System.out.println("Initial State: " + dfa.getStartState());
        System.out.println("Final States:");
        dfa.getFinalStates().forEach(System.out::println);
        System.out.println("Transitions");
        dfa.getTransitionTable().forEach((r, c) -> System.out.println(r.getKey() + " - " + r.getValue() + " : " + c));
    }


}
