package org.firstinspires.ftc.teamcode.util;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.*;

public class EventHandler {

    public static EventHandler instance;

    public Map<String, MacroState> currentStates = new HashMap<String, MacroState>(){};

    public boolean willInterfere(MacroState state){
        boolean willInterfere = false;

        for (String key: currentStates.keySet()) {
            if(!currentStates.get(key).canParallel(state)){
                willInterfere = true;
                break;
            }
        }

        return willInterfere;

    }

    public void addState(MacroState state, String stateName){
        if (currentStates.containsKey(stateName)){
            throw new IllegalArgumentException("State with the name " + stateName + " is already running");
        }
        if (!willInterfere(state)) {
            currentStates.put(stateName, state);
        }
    }

    public String[] getIncompatibleStates(MacroState toCheck){
        ArrayList<String> incompatibleStates = new ArrayList<String>(){};
        for (String stateName : currentStates.keySet()){
            if (!currentStates.get(stateName).canParallel(toCheck)){
                incompatibleStates.add(stateName);
            }
        }
        return (String[]) incompatibleStates.toArray();
    }

    public void terminateState(String stateName){
        if (!currentStates.containsKey(stateName)){
            throw new IllegalArgumentException("No state with the name " + stateName + " exists");
        }
        currentStates.get(stateName).terminateAllProcesses();
        currentStates.remove(stateName);
    }
    public void terminateState(String[] stateNames){
        for (int i = 0; i < stateNames.length; i++){
            if (!currentStates.containsKey(stateNames[i])){
                throw new IllegalArgumentException("No state with the name " + stateNames[i] + " exists");
            }
            terminateState(stateNames[i]);
        }
    }

    public void ForceState(MacroState state, String stateName){
        if (currentStates.containsKey(stateName)){
            throw new IllegalArgumentException("State with the name " + stateName + " is already running");
        }
        terminateState(getIncompatibleStates(state));
        addState(state, stateName);
    }


    private Map<String, MacroState>  stateChain = new LinkedHashMap<String, MacroState>(){};
    public void ChainState(MacroState state, String stateName){
        stateChain.put(stateName, state);
    }

    public void InterruptAll(){
        for (int i = 0; i < currentStates.size(); i++){
            currentStates.get(i).terminateAllProcesses();
        }
    }

    public void iterate(){
        for (String state: currentStates.keySet()){
            if (!currentStates.get(state).isFinished) {
                currentStates.get(state).runEachIteration();
            }else{
                terminateState(state);
            }
        }
        for (Map.Entry entry : stateChain.entrySet()){
            addState((MacroState) entry.getValue(), (String)entry.getKey());
        }
    }

}

