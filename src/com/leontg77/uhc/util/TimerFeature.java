package com.leontg77.uhc.util;

import com.leontg77.uhc.Main;

public class TimerFeature {
    private TimerRunnable m_runnable = null;

    /**
     * @param ticks number of seconds to run for
     * @param message the message to send
     * @return true if running, false if one already running
     */
    public boolean startTimer(int ticks, String message) {
        if(m_runnable != null && m_runnable.isRunning()){
            return false;
        }
        m_runnable = new TimerRunnable(ticks, message, Main.plugin);
        m_runnable.start();
        return true;
    }

    public boolean stopTimer(){
        if(m_runnable == null || !m_runnable.isRunning()){
            return false;
        }
        m_runnable.stopTimer();
        return true;
    }
}