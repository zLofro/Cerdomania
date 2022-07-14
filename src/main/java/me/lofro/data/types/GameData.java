package me.lofro.data.types;

import com.mojang.datafixers.util.Pair;
import lombok.Getter;
import org.bukkit.Location;

import java.util.*;

public class GameData {

    private final @Getter List<Pair<Location, Location>> checkPointLocations;
    private final @Getter Map<UUID, Location> respawnLocations;

    public GameData(List<Pair<Location, Location>> checkPointLocations, Map<UUID, Location> respawnLocations) {
        this.checkPointLocations = checkPointLocations;
        this.respawnLocations = respawnLocations;
    }

    public GameData() {
        this.checkPointLocations = new ArrayList<>();
        this.respawnLocations = new HashMap<>();
    }

}
