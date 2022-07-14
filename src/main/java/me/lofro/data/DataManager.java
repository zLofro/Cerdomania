package me.lofro.data;

import lombok.Getter;
import me.lofro.cerdomania.Cerdomania;
import me.lofro.data.utils.JsonConfig;

public class DataManager {

    private final Cerdomania cerdomania;

    private final @Getter JsonConfig gameDataConfig;

    public DataManager(final Cerdomania cerdomania) throws Exception {
        this.cerdomania = cerdomania;

        this.gameDataConfig = JsonConfig.cfg("gameData.json", cerdomania);
    }

    public void save() {
        cerdomania.gameManager().save(gameDataConfig);
    }

}
