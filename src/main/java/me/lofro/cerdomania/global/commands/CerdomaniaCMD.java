package me.lofro.cerdomania.global.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.Subcommand;
import com.mojang.datafixers.util.Pair;
import me.lofro.cerdomania.game.GameManager;
import me.lofro.cerdomania.game.enums.GameStage;
import me.lofro.cerdomania.game.enums.RaceType;
import me.lofro.utils.ChatColorFormatter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.LinkedList;
import java.util.stream.Collectors;

@CommandAlias("cerdomania | cm")
public class CerdomaniaCMD extends BaseCommand {

    private final GameManager gameManager;

    public CerdomaniaCMD(final GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @Subcommand("startRace")
    private void startRace(CommandSender sender, RaceType raceType) {
        if (!gameManager.getGameStage().equals(GameStage.RUNNING)) {
            gameManager.startRace(raceType, false);
            sender.sendMessage(ChatColorFormatter.stringToComponentWithPrefix("&7La carrera &6" + raceType + " &7ha dado comienzo."));
        } else {
            sender.sendMessage(ChatColorFormatter.stringToComponentWithPrefix("&cLa carrera ya esta siendo ejecutada."));
        }
    }

    @Subcommand("startRace")
    @CommandCompletion(" beacon")
    private void startRace(CommandSender sender, RaceType raceType, boolean beacon) {
        if (!gameManager.getGameStage().equals(GameStage.RUNNING)) {
            gameManager.startRace(raceType, beacon);
            sender.sendMessage(ChatColorFormatter.stringToComponentWithPrefix("&7La carrera &6" + raceType + " &7ha dado comienzo."));
        } else {
            sender.sendMessage(ChatColorFormatter.stringToComponentWithPrefix("&cLa carrera ya esta siendo ejecutada."));
        }
    }

    @Subcommand("stopRace")
    private void stopRace(CommandSender sender) {
        if (gameManager.getGameStage().equals(GameStage.RUNNING)) {
            gameManager.stopRace();
            sender.sendMessage(ChatColorFormatter.stringToComponentWithPrefix("&7La carrera ha finalizado."));
        } else {
            sender.sendMessage(ChatColorFormatter.stringToComponentWithPrefix("&cLa carrera no está siendo ejecutada."));
        }
    }

    @Subcommand("addCheckPoint")
    @CommandCompletion("@location @location")
    private void addCheckPoint(CommandSender sender, Location l1, Location l2) {
        var gameData = gameManager.getGameData();

        var pair = new Pair<>(l1, l2);

        if (gameData.getCheckPointLocations().stream().anyMatch(p -> p.equals(pair))) {
            sender.sendMessage(ChatColorFormatter.stringToComponentWithPrefix("&cLa región de checkpoint dada ya existe."));
            return;
        }

        gameData.getCheckPointLocations().add(pair);
        sender.sendMessage(ChatColorFormatter.stringToComponentWithPrefix("&7Se ha establecido la región [x:%d,y:%d,z:%d],[x:%d,y:%d,z:%d] como checkpoint."
                .formatted(l1.getBlockX(), l1.getBlockY(), l1.getBlockZ(), l2.getBlockX(), l2.getBlockY(), l2.getBlockZ())));
    }

    @Subcommand("removeCheckPoint")
    @CommandCompletion("@location @location")
    private void removeCheckPoint(CommandSender sender, Location l1, Location l2) {
        var gameData = gameManager.getGameData();

        var pair = new Pair<>(l1, l2);

        if (gameData.getCheckPointLocations().stream().noneMatch(p -> p.equals(pair))) {
            sender.sendMessage(ChatColorFormatter.stringToComponentWithPrefix("&cLa región de checkpoint dada no existe."));
            return;
        }

        var points = gameData.getCheckPointLocations().stream().filter(p -> p.equals(pair)).collect(Collectors.toCollection(LinkedList::new));
        gameData.getCheckPointLocations().removeAll(points);

        sender.sendMessage(ChatColorFormatter.stringToComponentWithPrefix("&7Se ha eliminado la región [x:%d,y:%d,z:%d],[x:%d,y:%d,z:%d] del conjunto de checkpoints."
                .formatted(l1.getBlockX(), l1.getBlockY(), l1.getBlockZ(), l2.getBlockX(), l2.getBlockY(), l2.getBlockZ())));
    }

    @Subcommand("checkPointAll")
    @CommandCompletion("@location")
    private void checkPointAll(CommandSender sender, Location location) {
        var gameData = gameManager.getGameData();

        Bukkit.getOnlinePlayers().forEach(p -> gameData.getRespawnLocations().put(p.getUniqueId(), location));
        sender.sendMessage(ChatColorFormatter.stringToComponentWithPrefix("&7Se ha establecido el checkpoint de todos los jugadores en [x:%d,y:%d,z:%d]."
                .formatted(location.getBlockX(), location.getBlockY(), location.getBlockZ())));
    }

    @Subcommand("checkPointAll")
    private void checkPointAll(CommandSender sender) {
        var gameData = gameManager.getGameData();

        if (!(sender instanceof Entity entity)) {
            sender.sendMessage(ChatColorFormatter.stringToComponentWithPrefix("&cNo tienes un espacio físico en el mundo, por lo que tu localización no ha podido ser obtenida."));
            return;
        }

        var location = entity.getLocation();

        Bukkit.getOnlinePlayers().forEach(p -> gameData.getRespawnLocations().put(p.getUniqueId(), location));
        sender.sendMessage(ChatColorFormatter.stringToComponentWithPrefix("&7Se ha establecido el checkpoint de todos los jugadores en [x:%d,y:%d,z:%d]."
                .formatted(location.getBlockX(), location.getBlockY(), location.getBlockZ())));
    }


}
