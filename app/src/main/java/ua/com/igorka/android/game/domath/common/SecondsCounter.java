package ua.com.igorka.android.game.domath.common;

import android.os.Handler;

/**
 * Created by Igor Kuzmenko on 17.11.15.
 *
 * This class is simply increment int value every second. To start counting use startCounter() method.
 * To stop counting use stopCounter() method. If you need to stop counter and reset its value to 0
 * use clear() method.
 * To get current int value of SecondsCounter you may use geCounterValue() method or callback method
 * update() of OnTimeCounterChangedListener interface.
 *
 */
public class SecondsCounter {

    private boolean isStarted = false;
    private int counter = 0;
    private Handler handler;
    private Runnable taskCounter;
    private OnTimeCounterChangedListener listener;

    public SecondsCounter() {
        handler = new Handler();
        taskCounter = new Runnable() {
            @Override
            public void run() {
                counter++;
                handler.postDelayed(taskCounter, 1000);
                if (listener != null) {
                    listener.update(counter);
                }
            }
        };
    }

    /**
     * Start counter
     */
    public void startCounter() {
        if (isStarted) {
            return;
        }
        handler.postDelayed(taskCounter, 1000);
        isStarted = true;
    }

    /**
     * Stop counter. Counter value is not reset.
     */
    public void stopCounter() {
        handler.removeCallbacks(taskCounter);
        isStarted = false;
    }

    /**
     * Stop counter and reset its value to 0.
     */
    public void clear() {
        stopCounter();
        counter = 0;
        if (listener != null) {
            listener.update(counter);
        }
    }

    /**
     * Get current counter value
     * @return counter value.
     */
    public int geCounterValue() {
        return counter;
    }

    /**
     * Registers a callback to be invoked when the counter changes its value.
     * @param listener The callback that will run
     */
    public void setTimeCounterChangedListener(OnTimeCounterChangedListener listener) {
        this.listener = listener;
    }

    /**
     * Interface definition for a callback to be invoked when a counter changes its value.
     */
    public interface OnTimeCounterChangedListener {
        void update(int newTime);
    }
}
