package com.thunder.ticktoklib.util;

import java.util.function.Consumer;

public class TickTokCountdown {
    private final long duration;
    private final long startTick;
    private final Consumer<TickTokCountdown> onCompleteConsumer;
    private final Runnable onCompleteRunnable;
    private boolean completed;

    public TickTokCountdown(long duration, long startTick, Runnable onComplete) {
        this(duration, startTick, onComplete, null);
    }

    public TickTokCountdown(long duration, long startTick, Consumer<TickTokCountdown> onComplete) {
        this(duration, startTick, null, onComplete);
    }

    private TickTokCountdown(long duration, long startTick, Runnable onComplete, Consumer<TickTokCountdown> consumer) {
        this.duration = duration;
        this.startTick = startTick;
        this.onCompleteRunnable = onComplete;
        this.onCompleteConsumer = consumer;
    }

    public boolean isFinished(long currentTick) {
        return (currentTick - startTick) >= duration;
    }

    public long remaining(long currentTick) {
        return Math.max(0, duration - (currentTick - startTick));
    }

    /**
     * @return true if the countdown has reached 0 during this tick
     */
    public boolean tick(long currentTick) {
        if (completed) {
            return false;
        }
        if (isFinished(currentTick)) {
            completed = true;
            if (onCompleteRunnable != null) {
                onCompleteRunnable.run();
            }
            if (onCompleteConsumer != null) {
                onCompleteConsumer.accept(this);
            }
            return true;
        }
        return false;
    }
}
