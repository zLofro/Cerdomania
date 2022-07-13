package me.lofro.cerdomania.global.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.Subcommand;
import me.lofro.cerdomania.game.GameManager;
import me.lofro.cerdomania.game.enums.GameStage;
import me.lofro.cerdomania.game.enums.RaceType;
import me.lofro.utils.ChatColorFormatter;
import org.bukkit.command.CommandSender;

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

}
