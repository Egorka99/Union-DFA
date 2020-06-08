import java.util.List;
import java.util.Map;
import java.util.Set;

public class DFA {
    private List<Character> alphabet;
    private List<String> states;
    private String startState;
    private List<String> finalStates;
    private Map<Map.Entry<String, Character>, String> transitionTable;

    public DFA(List<Character> alphabet, List<String> states, String startState, List<String> finalStates, Map<Map.Entry<String, Character>, String> transitionTable) {
        this.alphabet = alphabet;
        this.states = states;
        this.startState = startState;
        this.finalStates = finalStates;
        this.transitionTable = transitionTable;
    }

    public List<Character> getAlphabet() {
        return alphabet;
    }

    public List<String> getStates() {
        return states;
    }

    public String getStartState() {
        return startState;
    }

    public List<String> getFinalStates() {
        return finalStates;
    }

    public Map<Map.Entry<String, Character>, String> getTransitionTable() {
        return transitionTable;
    }
}
