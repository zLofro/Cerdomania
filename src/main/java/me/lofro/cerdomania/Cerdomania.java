package me.lofro.cerdomania;

import lombok.Getter;
import me.lofro.utils.ChatColorFormatter;
import org.bukkit.plugin.java.JavaPlugin;

public class Cerdomania extends JavaPlugin {

    private static @Getter Cerdomania instance;

    public static final String name = ChatColorFormatter.stringToString("&c&lCerdomania");
    public static final String prefix = ChatColorFormatter.stringToString(name + " &r>> ");

    @Override
    public void onEnable() {
        instance = this;
    }

    @Override
    public void onDisable() {

    }

}
