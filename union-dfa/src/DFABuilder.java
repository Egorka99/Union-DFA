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

    private DFA multiplyDFA(DFA firstMultiplier, DFA secondMultiplier) {
        List<Character> alphabet = firstMultiplier.getAlphabet();
        List<String> states = new ArrayList<>();
        List<String[]> statesArray = new ArrayList<>();
        List<String> finalStates = new ArrayList<>();
        String startState;
        Map<Map.Entry<String, Character>, String> transitionTable= new HashMap<>();

        for (String firstDfaState : firstMultiplier.getStates()) {
            for (String secondDfaState : secondMultiplier.getStates()) {
                String newState = "{" + firstDfaState + " , " + secondDfaState + "}";
                statesArray.add(new String[]{firstDfaState, secondDfaState});
                states.add(newState);
            }
        }
        startState = "{" + firstMultiplier.getStartState() + " , " + secondMultiplier.getStartState() + "}";

        for (String firstDfaFinalState : firstMultiplier.getFinalStates()) {
            for (String secondDfaFinalState : secondMultiplier.getFinalStates()) {
                String newFinalState = "{" + firstDfaFinalState + " , " + secondDfaFinalState + "}";
                finalStates.add(newFinalState);
            }
        }

        for (String[] state : statesArray) {
            for (Character symbol : alphabet) {
                Map.Entry funcParam = Map.entry(states, symbol);
                String funcValue = "{" + firstMultiplier.getTransitionTable().get(Map.entry(state[0],symbol)) + " , " +
                        secondMultiplier.getTransitionTable().get(Map.entry(state[1],symbol)) + "}";
                transitionTable.put(funcParam,funcValue);
            }
        }

        return new DFA(alphabet,states,startState,finalStates,transitionTable);

    }
 
    public static void main(String[] args) {

    }


}
