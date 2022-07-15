package me.lofro.cerdomania.game;

import com.google.common.collect.ImmutableList;
import lombok.Getter;
import me.lofro.cerdomania.Cerdomania;
import me.lofro.cerdomania.global.commands.CerdomaniaCMD;
import me.lofro.cerdomania.game.enums.GameStage;
import me.lofro.cerdomania.game.enums.RaceType;
import me.lofro.cerdomania.global.interfaces.Restorable;
import me.lofro.cerdomania.game.listeners.RaceListeners;
import me.lofro.data.types.GameData;
import me.lofro.data.utils.JsonConfig;
import me.lofro.utils.Commands;
import me.lofro.utils.Listeners;
import me.lofro.utils.Scoreboards;
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

    private @Getter GameData gameData;

    private @Getter GameStage gameStage;

    private @Getter RaceType raceType;

    private @Getter boolean beacon = false;

    public GameManager(final Cerdomania cerdomania) {
        this.cerdomania = cerdomania;
        this.cerdomaniaCMD = new CerdomaniaCMD(this);
        this.raceListeners = new RaceListeners(this);

        Listeners.registerListener(raceListeners);
        Commands.registerCommands(cerdomania.getPaperCommandManager(), cerdomaniaCMD);

        this.restore(cerdomania.getDataManager().getGameDataConfig());

        this.gameStage = GameStage.PRE_GAME;

        // Sets the location command completion.
        Cerdomania.getInstance().getPaperCommandManager().getCommandCompletions().registerCompletion(
                "@location", c -> ImmutableList.of("x,y,z"));
    }

    @Override
    public void restore(JsonConfig jsonConfig) {
        if (jsonConfig.getJsonObject().entrySet().isEmpty()) {
            this.gameData = new GameData();
        } else {
            this.gameData = Cerdomania.gson().fromJson(jsonConfig.getJsonObject(), GameData.class);
        }
    }

    @Override
    public void save(JsonConfig jsonConfig) {
        jsonConfig.setJsonObject(Cerdomania.gson().toJsonTree(gameData).getAsJsonObject());
        try {
            jsonConfig.save();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startRace(RaceType raceType, boolean beacon) {
        this.gameStage = GameStage.RUNNING;
        this.raceType = raceType;
        this.beacon = beacon;

        Bukkit.getOnlinePlayers().forEach(p -> loadDefaultPlayer(p, true));
    }

    public void stopRace() {
        this.gameStage = GameStage.FINISHED;

        Bukkit.getOnlinePlayers().forEach(p -> unloadDefaultPlayer(p, true));

        this.beacon = false;
        this.raceType = null;
    }

    public void loadPlayerFromGameMode(Player p, GameMode newGameMode) {
        if (newGameMode == GameMode.SURVIVAL) {
            loadPlayer(p, true);
        } else {
            unloadPlayer(p, true);
        }
    }

    public void loadDefaultPlayer(Player p, boolean giveItems) {
        if (!p.getGameMode().equals(GameMode.SURVIVAL)) return;

        loadPlayer(p, giveItems);
    }

    private void loadPlayer(Player p, boolean giveItems) {
        if (giveItems) giveItems(p, this.beacon);

        if (p.getVehicle() != null && p.getVehicle().getType().equals(raceType.getEntityType())) return;

        var vehicle = (raceType.equals(RaceType.PIG)) ? p.getWorld().spawnEntity(p.getLocation(), EntityType.PIG) : p.getWorld().spawnEntity(p.getLocation(), EntityType.STRIDER);

        vehicle.addPassenger(p);

        Scoreboards.addToRaceTeam(vehicle, p);
    }

    public void unloadDefaultPlayer(Player p, boolean removeItems) {
        if (!p.getGameMode().equals(GameMode.SURVIVAL)) return;

        unloadPlayer(p, removeItems);
    }

    private void unloadPlayer(Player p, boolean removeItems) {
        if (removeItems) removeItems(p, this.beacon);
        Scoreboards.removeFromRaceTeam(p);

        if (!p.isInsideVehicle()) return;

        var vehicle = p.getVehicle();

        if (vehicle != null && vehicle.getType().equals(raceType.getEntityType())) {
            vehicle.removePassenger(p);
            vehicle.remove();
        }
    }

    public void giveItems(Player player, boolean beacon) {
        if (beacon) player.getEquipment().setHelmet(new ItemStack(Material.BEACON));
        ItemStack item;
        if (raceType.equals(RaceType.PIG)) {
            item = new ItemStack(Material.CARROT_ON_A_STICK);
        } else {
            item = new ItemStack(Material.WARPED_FUNGUS_ON_A_STICK);
        }
        if (player.getInventory().contains(item.getType()) || player.getEquipment().getItemInOffHand().getType().equals(item.getType())) return;
        player.getInventory().addItem(item);
    }

    public void removeItems(Player player, boolean beacon) {
        if (beacon) player.getEquipment().setHelmet(new ItemStack(Material.AIR));
        Material mat;
        if (raceType.equals(RaceType.PIG)) {
            mat = Material.CARROT_ON_A_STICK;
        } else {
            mat = Material.WARPED_FUNGUS_ON_A_STICK;
        }
        if (player.getInventory().contains(mat)) player.getInventory().remove(mat);
        if (player.getEquipment().getItemInOffHand().getType().equals(mat)) player.getEquipment().setItemInOffHand(new ItemStack(Material.AIR));
    }

}
