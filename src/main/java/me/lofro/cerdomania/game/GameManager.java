package me.lofro.cerdomania.game;

import lombok.Getter;
import me.lofro.cerdomania.Cerdomania;
import me.lofro.cerdomania.global.commands.CerdomaniaCMD;
import me.lofro.cerdomania.game.enums.GameStage;
import me.lofro.cerdomania.game.enums.RaceType;
import me.lofro.cerdomania.global.interfaces.Restorable;
import me.lofro.cerdomania.game.listeners.RaceListeners;
import me.lofro.data.utils.JsonConfig;
import me.lofro.utils.Commands;
import me.lofro.utils.Listeners;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class GameManager implements Restorable {

    private final Cerdomania cerdomania;

    private final CerdomaniaCMD cerdomaniaCMD;

    private final RaceListeners raceListeners;

    private @Getter GameStage gameStage;

    private @Getter RaceType raceType;

    private @Getter boolean beacon = false;

    public GameManager(final Cerdomania cerdomania) {
        this.cerdomania = cerdomania;
        this.cerdomaniaCMD = new CerdomaniaCMD(this);
        this.raceListeners = new RaceListeners(this);

        Listeners.registerListener(raceListeners);
        Commands.registerCommands(cerdomania.getPaperCommandManager(), cerdomaniaCMD);

        this.gameStage = GameStage.PRE_GAME;
    }

    @Override
    public void restore(JsonConfig jsonConfig) {

    }

    @Override
    public void save(JsonConfig jsonConfig) {

    }

    public void startRace(RaceType raceType, boolean beacon) {
        this.gameStage = GameStage.RUNNING;
        this.raceType = raceType;
        this.beacon = beacon;

        Bukkit.getOnlinePlayers().forEach(p -> {
            if (!p.getGameMode().equals(GameMode.SURVIVAL)) return;

            giveItems(p, this.beacon);

            if (p.getGameMode().equals(GameMode.SURVIVAL) && p.getVehicle() != null && p.getVehicle().getType().equals(raceType.getEntityType())) return;

            var vehicle = (raceType.equals(RaceType.PIG)) ? p.getWorld().spawnEntity(p.getLocation(), EntityType.PIG) : p.getWorld().spawnEntity(p.getLocation(), EntityType.STRIDER);

            vehicle.addPassenger(p);
        });
    }

    public void stopRace() {
        Bukkit.getOnlinePlayers().forEach(p -> {
            this.gameStage = GameStage.FINISHED;

            if (!p.getGameMode().equals(GameMode.SURVIVAL)) return;

            removeItems(p, this.beacon);

            if (!p.isInsideVehicle()) return;

            var vehicle = p.getVehicle();

            if (vehicle != null && vehicle.getType().equals(raceType.getEntityType())) {
                vehicle.remove();
            }
        });

        this.beacon = false;
        this.raceType = null;
    }

    public void giveItems(Player player, boolean beacon) {
        ItemStack item;
        if (raceType.equals(RaceType.PIG)) {
            item = new ItemStack(Material.CARROT_ON_A_STICK);
        } else {
            item = new ItemStack(Material.WARPED_FUNGUS_ON_A_STICK);
        }
        if (player.getInventory().contains(item.getType())) return;
        player.getInventory().addItem(item);
        if (beacon) player.getEquipment().setHelmet(new ItemStack(Material.BEACON));
    }

    public void removeItems(Player player, boolean beacon) {
        if (beacon) player.getEquipment().setHelmet(new ItemStack(Material.AIR));
        Material mat;
        if (raceType.equals(RaceType.PIG)) {
            mat = Material.CARROT_ON_A_STICK;
        } else {
            mat = Material.WARPED_FUNGUS_ON_A_STICK;
        }
        if (!player.getInventory().contains(mat)) return;
        player.getInventory().remove(mat);
    }

}
