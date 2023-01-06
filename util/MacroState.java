package org.firstinspires.ftc.teamcode.util;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.function.Function;

import java.lang.reflect.Method;

public class MacroState {

    private class MicroState{
        public Method action;
        public Method condition;
        public Component inUse;
    }


    public void terminateAllProcesses(){}
    public boolean canParallel(){return true;}

    public ArrayList<MicroState> microStates;

    private int currentState;
    public MicroState getCurrentState(){return microStates.get(currentState);}


    public void runEachTick(Object obj) throws InvocationTargetException, IllegalAccessException {
        getCurrentState().action.invoke(obj, null);
//        if(!getCurrentState().condition() && microStates.get(curretn))
    }

}
