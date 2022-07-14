package me.lofro.cerdomania;

import co.aikar.commands.PaperCommandManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;
import me.lofro.cerdomania.game.GameManager;
import me.lofro.data.DataManager;
import me.lofro.data.adapters.LocationSerializer;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

public class Cerdomania extends JavaPlugin {

    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Location.class, new LocationSerializer())
            .registerTypeAdapter(Location[].class, LocationSerializer.getArraySerializer())
            .setPrettyPrinting()
            .serializeNulls()
            .create();

    private static @Getter Cerdomania instance;

    private GameManager gameManager;

    private @Getter DataManager dataManager;

    private @Getter PaperCommandManager paperCommandManager;

    @Override
    public void onEnable() {
        instance = this;

        paperCommandManager = new PaperCommandManager(this);

        try {
            this.dataManager = new DataManager(this);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        this.gameManager = new GameManager(this);
    }

    @Override
    public void onDisable() {
        this.dataManager.save();
    }

    public static Gson gson() {
        return gson;
    }

    public GameManager gameManager() {
        return gameManager;
    }

}
