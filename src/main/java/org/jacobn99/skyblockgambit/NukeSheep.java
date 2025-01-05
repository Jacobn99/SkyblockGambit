package org.jacobn99.skyblockgambit;

import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.entity.Sheep;
import org.jacobn99.skyblockgambit.Processes.ProcessManager;

import java.util.List;

public class NukeSheep {
    private long _timeLeft;
    private Sheep _sheep;
    private List<NukeSheep> _nukeSheeps;
    private final long _flashDelay;
    private final long _delayBetweenFlashes;
    private long _timeToFlash;
    final private DyeColor _color;
    private ProcessManager _processManager;
    public NukeSheep(Sheep sheep, long timeLeft, ProcessManager processManager, List<NukeSheep> nukeSheeps) {
        _timeLeft = timeLeft;
        _processManager = processManager;
        _flashDelay = (long) (timeLeft/4);
        _delayBetweenFlashes = (long) (_flashDelay/2);
        _timeToFlash = _flashDelay;
        _sheep = sheep;
        _color = sheep.getColor();
        _nukeSheeps = nukeSheeps;
        _nukeSheeps.add(this);
    }
    public void Flash() {
        if(!_sheep.isDead()) {
            _sheep.setColor(DyeColor.RED);
            _processManager.CreateProcess(_sheep.getWorld().getFullTime()
                    + _delayBetweenFlashes, ()-> this.RevertColor());
        }
    }

    public void RevertColor() {
        if(!_sheep.isDead()) {
            _sheep.setColor(_color);
        }
    }

    public long get_flashDelay() {
        return _flashDelay;
    }

    public Sheep get_sheep() {
        return _sheep;
    }

    public long get_timeLeft() {
        return _timeLeft;
    }

    public void set_timeLeft(long timeLeft) {
        this._timeLeft = timeLeft;
    }

    public long get_timeToFlash() {
        return _timeToFlash;
    }

    public void set_timeToFlash(long timeToFlash) {
        this._timeToFlash = timeToFlash;
    }
}
