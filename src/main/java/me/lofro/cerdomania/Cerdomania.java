package me.lofro.cerdomania;

import co.aikar.commands.PaperCommandManager;
import lombok.Getter;
import me.lofro.cerdomania.game.GameManager;
import me.lofro.data.DataManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Cerdomania extends JavaPlugin {

    private static @Getter Cerdomania instance;

    private GameManager gameManager;

    private DataManager dataManager;

    private @Getter PaperCommandManager paperCommandManager;

    @Override
    public void onEnable() {
        instance = this;

        paperCommandManager = new PaperCommandManager(this);

        this.gameManager = new GameManager(this);
        try {
            this.dataManager = new DataManager(this);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onDisable() {
        this.dataManager.save();


    }

    public GameManager gameManager() {
        return gameManager;
    }

}
