package me.lofro.cerdomania.game.enums;

import lombok.Getter;
import org.bukkit.entity.EntityType;

public enum RaceType {

    PIG(EntityType.PIG), STRIDER(EntityType.STRIDER);

    RaceType(EntityType entityType) {
        this.entityType = entityType;
    }

    private final @Getter EntityType entityType;

}
