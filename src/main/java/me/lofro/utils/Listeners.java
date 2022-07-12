package me.lofro.utils;

import me.lofro.cerdomania.Cerdomania;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

public class Listeners {

    private static final Plugin plugin = Cerdomania.getInstance();

    public static void registerListener(Listener listener) {
        Bukkit.getPluginManager().registerEvents(listener, plugin);
    }

    public static void registerListeners(Listener... listeners) {
        for (Listener listener : listeners) {
            Bukkit.getPluginManager().registerEvents(listener, plugin);
        }
    }

    public static void unregisterListeners(Listener... listeners) {
        for (Listener listener : listeners) {
            HandlerList.unregisterAll(listener);
        }
    }

    public static void unregisterListener(Listener listener) {
        HandlerList.unregisterAll(listener);
    }

}
