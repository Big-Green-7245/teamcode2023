package org.firstinspires.ftc.teamcode.util;

import java.util.ArrayList;

public class MacroState {

    public boolean isFinished = false;

    public interface MicroState {
        int action();
        void end();

    }

    private ArrayList<MicroState> microStates = new ArrayList<MicroState>(){};

    public void addMicroState(MicroState addThis){
        microStates.add(addThis);
    }

    public void addMicroState(MicroState[] addThese){
        for (int i = 0; i < addThese.length; i++){
            addMicroState(addThese[i]);
        }
    }

    private int currentState = 0;
    public MicroState getCurrentState(){return microStates.get(currentState);}


    public void terminateAllProcesses(){
        getCurrentState().end();
        isFinished = true;
    }

    private ArrayList<Component> inUse = new ArrayList<Component>(){};

    public boolean isInUse(Component comp){
        if (inUse.contains(comp)){
            return true;
        }else{
            return false;
        }
    }

    public boolean canParallel(MacroState state){
        boolean canParallel = true;
        for (int i = 0; i < inUse.size(); i ++){
            if (state.isInUse(inUse.get(i))){
                canParallel = false;
                break;
            }
        }
        return canParallel;
    }


    public void runEachIteration(){
        if (!isFinished) {
            currentState += getCurrentState().action();
        }
        if (currentState == microStates.size()){
            isFinished = true;
        }
    }

}
