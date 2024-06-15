package org.jacobn99.skyblockgambit;

import java.util.HashMap;
import java.util.List;

public class Process {
    boolean _isDone;
    Process _previousProcess;
    private HashMap<Long, Process> _processGroup;
    //private HashMap<Long, Process>
    private Queueable _queueable;
    private long _executionTime;
    public Process(HashMap<Long, Queueable> processGroup) {
        _isDone = true;
        //isPreviousDone = true;
        //_processGroup = processGroup;
    }

    public Process(Long executionTime, Queueable queueable, final Process previousProcess) {
        //_processGroup = processGroup;
        _isDone = false;
        //_isPreviousDone = isPreviousDone;
        _queueable = queueable;
        _executionTime = executionTime;
        _previousProcess = previousProcess;
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

    public Process get_previousProcess() {
        return _previousProcess;
    }
    //    public boolean get_isPreviousDone() {
//        return _isPreviousDone;
//    }
//
//    public void set_isPreviousDone(boolean _isPreviousDone) {
//        this._isPreviousDone = _isPreviousDone;
//    }

    public Queueable get_queueable() {
        return _queueable;
    }

    public void set_queueable(Queueable _queueable) {
        this._queueable = _queueable;
    }

    public long get_executionTime() {
        return _executionTime;
    }
}
