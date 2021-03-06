package com.leontg77.uhc.util;

import java.lang.reflect.InvocationTargetException;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;

public class TimerRunnable implements Runnable {
    public static final int TICKS_PER_SECOND = 20;
    public static final float DRAGON_HEALTH = 200.0F;
    public static final long SECONDS_PER_HOUR = 3600;
    public static final long SECONDS_PER_MINUTE = 60;
    public static final int Y_COORD = -200;
    public static final int Y_MULTIPLIER = 32;
    public static final int X_MULTIPLIER = 32;
    public static final int Z_MULTIPLIER = 32;
    public static final int INVISIBLE_FLAG = 0x20;
    public static final int MAX_STRING_LEGNTH = 64;
    private static final int ENTITY_ID = Short.MAX_VALUE - 375;

    private final ProtocolManager m_protocolManager = ProtocolLibrary.getProtocolManager();
    private final PacketContainer m_spawnPacket = m_protocolManager.createPacket(PacketType.Play.Server.SPAWN_ENTITY_LIVING);
    private final PacketContainer m_destroyPacket = m_protocolManager.createPacket(PacketType.Play.Server.ENTITY_DESTROY);

    private int m_secondsLeft = 0;
    private int m_jobID = -1;
    private final Plugin m_plugin;
    private final String m_message;

    /**
     * A new timer
     * @param seconds the ticks to run for
     * @param message the message to display
     * @param plugin the plugin we run under
     */
    public TimerRunnable(int seconds, String message, Plugin plugin){
        m_secondsLeft = seconds;
        m_plugin = plugin;
        m_message = message;
        m_destroyPacket.getIntegerArrays().write(0, new int[]{ENTITY_ID});
    }

    /**
     * Start the runnable
     */
    public void start(){
        m_jobID = Bukkit.getScheduler().scheduleSyncRepeatingTask(m_plugin, this, 0, TICKS_PER_SECOND);
    }

    /**
     * Stop the timer
     */
    public void stopTimer() {
        if (m_jobID != -1) {
            Bukkit.getScheduler().cancelTask(m_jobID);
            m_jobID = -1;
        }
        destroyTimer();
        m_secondsLeft = 0;
    }

    /**
     * Converts the seconds to human readable
     * @param ticks the  number of ticks
     * @return the human readable version
     */
    public static String ticksToString(long ticks) {
        int hours = (int) Math.floor(ticks / (double) SECONDS_PER_HOUR); //half seconds in a hour
        ticks -= hours * SECONDS_PER_HOUR;
        int minutes = (int) Math.floor(ticks / (double)SECONDS_PER_MINUTE);    //half seconds in a minute
        ticks -= minutes * SECONDS_PER_MINUTE;
        int seconds = (int) ticks;

        StringBuilder output = new StringBuilder();
        if (hours > 0) {
            output.append(hours).append('h');
        }
        if (minutes > 0) {
            output.append(minutes).append('m');
        }
        output.append(seconds).append('s');

        return output.toString();
    }

    /**
     * @return Is it still running or not
     */
    public boolean isRunning(){
        return m_secondsLeft > 0;
    }

    /**
     * Gets rid of the timer for all players
     */
    private void destroyTimer() {
        for (Player p : PlayersUtil.getPlayers()) {
            try {
                m_protocolManager.sendServerPacket(p, m_destroyPacket);
            }catch (InvocationTargetException ignored){}
        }
    }

    @Override
    public void run() {
        --m_secondsLeft;
        if (m_secondsLeft == 0) {
            stopTimer();
            return;
        }
        displayTextBar(m_message + " " + ticksToString(m_secondsLeft), m_secondsLeft / ((float) m_secondsLeft * DRAGON_HEALTH));
    }

    /**
     * Display the text for all
     * @param text the text to show
     * @param health the health for the bar to be on
     */
	private void displayTextBar(String text, float health) {
        destroyTimer();
        for (Player player : PlayersUtil.getPlayers()) {
            try {
                PacketContainer pc = m_spawnPacket.deepClone();
                pc.getIntegers()
                        .write(0, ENTITY_ID)
                        .write(1, 63) 
                        .write(2, (int) player.getLocation().getX() * X_MULTIPLIER)       
                        .write(3, Y_COORD * Y_MULTIPLIER)                             
                        .write(4, (int) player.getLocation().getZ() * Z_MULTIPLIER);       
                WrappedDataWatcher watcher = pc.getDataWatcherModifier().read(0);
                watcher.setObject(0, (byte) INVISIBLE_FLAG); 
                watcher.setObject(6, health);
                watcher.setObject(10, text.substring(0, Math.min(text.length(), MAX_STRING_LEGNTH)));
                m_protocolManager.sendServerPacket(player, pc);
            } catch (InvocationTargetException ignored) {}
        }
    }
}