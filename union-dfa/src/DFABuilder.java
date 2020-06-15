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
        List<String> reachableStates = new ArrayList<>();
        reachableStates.add(unionDfa.getStates().get(0));
        List<String> newStates = new ArrayList<>();
        newStates.add(unionDfa.getStates().get(0));

        do {
            List<String> temp = new ArrayList<>();
            for (String q : newStates) {
                for (Character c : unionDfa.getAlphabet()) {
                    temp.add(unionDfa.getTransitionTable().get(new AbstractMap.SimpleEntry(q, c)));
                }
            }
            newStates.addAll(temp);
            newStates.removeAll(reachableStates);
            reachableStates.addAll(newStates);
        } while (newStates.size() != 0);

        List<String> unreachableStates = unionDfa.getStates().stream().filter(s -> !reachableStates.contains(s)).collect(Collectors.toList());

        List<String> newDfaStates = unionDfa.getStates().stream().filter(s -> !unreachableStates.contains(s)).collect(Collectors.toList());
        List<String> newDfaFinalStates = unionDfa.getFinalStates().stream().filter(s -> !unreachableStates.contains(s)).collect(Collectors.toList());
        Set<Map.Entry<Map.Entry<String, Character>, String>> newDfaTransitionsEntrySet = unionDfa.getTransitionTable().entrySet().stream()
                .filter(e -> !unreachableStates.contains(e.getKey().getKey())).collect(Collectors.toSet());
        Map<Map.Entry<String, Character>, String> newDfaTransitions = newDfaTransitionsEntrySet.stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        return new DFA(unionDfa.getAlphabet(), newDfaStates, unionDfa.getStartState(), newDfaFinalStates, newDfaTransitions);
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
                Map.Entry funcParam = new AbstractMap.SimpleEntry(states.get(i), symbol);
                String funcValue = "(" + firstDfa.getTransitionTable().get(new AbstractMap.SimpleEntry(statesArray.get(i)[0], symbol)) + " , " +
                        secondDfa.getTransitionTable().get(new AbstractMap.SimpleEntry(statesArray.get(i)[1], symbol)) + ")";
                transitionTable.put(funcParam, funcValue);
            }
        }

        return new DFA(alphabet, states, startState, finalStates, transitionTable);

    }



}
