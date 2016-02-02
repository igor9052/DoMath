package ua.com.igorka.android.game.domath.common;

import android.os.Handler;

/**
 * Created by Igor Kuzmenko on 17.11.15.
 *
 * This class is simply decrement int value every second. To start counting use startCounter() method.
 * To stop counting use stopCounter() method. If you need to stop counter and reset its value to init value
 * use clear() method.
 * To get current int value of CountDownCounter you may use getCounterValue() method or callback method
 * update() of OnTimeCounterChangedListener interface.
 *
 */
public class CountDownCounter {

    private boolean isStarted = false;
    private int counter = 0;

    public void setInitValue(int initValue) {
        this.initValue = initValue;
    }

    private int initValue = 0;
    private Handler handler;
    private Runnable taskCounter;
    private OnTimeCounterChangedListener listener;

    public CountDownCounter(int initValue) {
        this.initValue = initValue;
        counter = initValue;
        handler = new Handler();
        taskCounter = new Runnable() {
            @Override
            public void run() {
                subFromCounter(1);
                if (counter <= 0) {
                    stopCounter();
                }
                else {
                    handler.postDelayed(taskCounter, 1000);
                }
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
        counter = initValue;
        if (listener != null) {
            listener.update(counter);
        }
    }

    /**
     * Get current counter value
     * @return counter value.
     */
    public int getCounterValue() {
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
     * Add time seconds to the current timer value. Only if timer is running.
     * @param time
     */
    public void addTime(int time) {
        if (isStarted) {
            addToCounter(time);
        }
    }

    /**
     * Subtract time seconds from the current timer value. Only if timer is running.
     * @param time
     */
    public void subTime(int time) {
        if (isStarted) {
            subFromCounter(time);
        }
    }

    private synchronized int addToCounter(int time) {
        counter = counter + time;
        if (counter > initValue) {
            counter = initValue;
        }
        return counter;
    }

    private synchronized int subFromCounter(int time) {
        counter = counter - time;
        if (counter < 0) {
            counter = 0;
        }
        return counter;
    }


    /**
     * Interface definition for a callback to be invoked when a counter changes its value.
     */
    public interface OnTimeCounterChangedListener {
        void update(int newTime);
    }


}
