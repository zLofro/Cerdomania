package me.lofro.utils;

import lombok.Getter;
import me.lofro.cerdomania.Cerdomania;
import org.bukkit.entity.Entity;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

/**
 * Class to manage the team-scoreboard prefixes for the game.
 */
public class Scoreboards {

    private static final @Getter ScoreboardManager scoreboardManager = Cerdomania.getInstance().getServer().getScoreboardManager();
    private static final @Getter Scoreboard scoreboard = scoreboardManager.getMainScoreboard();

    public static void addToRaceTeam(Entity entity) {
        var team = scoreboard.getTeam("RACE");

        var raceTeam = (team != null) ? team : scoreboard.registerNewTeam("RACE");
        raceTeam.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);

        raceTeam.addEntity(entity);
    }

    public static void removeFromRaceTeam(Entity entity) {
        var team = scoreboard.getTeam("RACE");

        var raceTeam = (team != null) ? team : scoreboard.registerNewTeam("RACE");
        raceTeam.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);

        raceTeam.removeEntity(entity);
    }

    public static void addToRaceTeam(Entity... entities) {
        for (Entity entity : entities) {
            addToRaceTeam(entity);
        }
    }

    public static void removeFromRaceTeam(Entity... entities) {
        for (Entity entity : entities) {
            removeFromRaceTeam(entity);
        }
    }

}
