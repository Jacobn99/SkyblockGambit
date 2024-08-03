package org.jacobn99.skyblockgambit.Processes;

import java.util.HashMap;

public class Process {
    boolean _isDone;
//    Process _previousProcess;
    private HashMap<Long, Process> _processGroup;
    //private HashMap<Long, Process>
    private Queueable _queueable;
    private long _executionTime;
    public Process(HashMap<Long, Process> processGroup) {
        _isDone = false;
        //isPreviousDone = true;
        _processGroup = processGroup;
    }

    public Process(Long executionTime, Queueable queueable) {
        //_processGroup = processGroup;
        //_isDone = false;
        //_isPreviousDone = isPreviousDone;
        _queueable = queueable;
        _executionTime = executionTime;
//        _previousProcess = previousProcess;
        //processGroup.put(_executionTime, this);
    }
    public void ExecuteFunction()
    {
        _queueable.Execute();
    }
    public boolean get_isDone() {
        return _isDone;
    }

    public void set_isDone(boolean isDone) {
        this._isDone = isDone;
    }
    public Queueable get_queueable() {
        return _queueable;
    }

    public void set_queueable(Queueable _queueable) {
        this._queueable = _queueable;
    }

    public long get_executionTime() {
        return _executionTime;
    }

    public void set_executionTime(long _executionTime) {
        this._executionTime = _executionTime;
    }
}
