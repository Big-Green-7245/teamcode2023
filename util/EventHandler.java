package org.firstinspires.ftc.teamcode.util;
import java.util.*;

public class EventHandler {

    public static EventHandler instance;

    public Map<String, MacroState> currentStates;


    public void addState(MacroState state, String stateName){
        boolean canParallel = true;

        for (String key: currentStates.keySet()) {
            if(!currentStates.get(key).canParallel()){
                canParallel = false;
                break;
            }
        }

        if (canParallel) {
            currentStates.put(stateName, state);
        }
    }
    public void terminateState(String stateName){
        currentStates.get(stateName).terminateAllProcesses();
    }
    public void InterruptAll(){}
    public void ForceState(){}
    public void ChainState(){}

}

