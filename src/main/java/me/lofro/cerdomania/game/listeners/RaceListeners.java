package me.lofro.cerdomania.game.listeners;

import me.lofro.cerdomania.Cerdomania;
import me.lofro.cerdomania.game.GameManager;
import me.lofro.cerdomania.game.enums.GameStage;
import me.lofro.cerdomania.game.enums.RaceType;
import me.lofro.utils.Locations;
import me.lofro.utils.Scoreboards;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.*;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.inventory.PlayerInventory;

import java.util.LinkedList;
import java.util.stream.Collectors;

public class RaceListeners implements Listener {

    private final GameManager gameManager;

    public RaceListeners(final GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @EventHandler
    private void onClick(InventoryClickEvent e) {
        if (!gameManager.getGameStage().equals(GameStage.RUNNING)) return;

        if (e.getClickedInventory() instanceof PlayerInventory) {
            if (e.getSlotType().equals(InventoryType.SlotType.ARMOR)) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    private void onMove(PlayerMoveEvent e) {
        if (!e.hasChangedBlock() || !gameManager.getGameStage().equals(GameStage.RUNNING)) return;

        var player = e.getPlayer();
        var uuid = player.getUniqueId();
        var location = player.getLocation();

        var checkPoints = gameManager.getGameData().getCheckPointLocations();
        var respawnPoints = gameManager.getGameData().getRespawnLocations();

        checkPoints.forEach(c -> {
            if (Locations.isInCube(c.getFirst(), c.getSecond(), location)) respawnPoints.put(uuid, Locations.getCubeCenter2D(c.getFirst(), c.getSecond()));
        });
    }

    @EventHandler
    private void onVehicleExit(VehicleExitEvent e) {
        if (!gameManager.getGameStage().equals(GameStage.RUNNING)) return;

        if (!e.getVehicle().isValid()) return;

        var vehicle = e.getVehicle();
        var passenger = e.getExited();

        if (passenger instanceof Player player && player.getGameMode().equals(GameMode.SURVIVAL) && vehicle.getType().equals(gameManager.getRaceType().getEntityType()) && !player.isInvulnerable()) {

            gameManager.removeItems(player, gameManager.isBeacon());
            vehicle.remove();
            spawnBloodParticles(player, 3);
            player.setHealth(0);
        }
    }

    @EventHandler
    private void onGameModeChange(PlayerGameModeChangeEvent e) {
        if (!gameManager.getGameStage().equals(GameStage.RUNNING)) return;

        var player = e.getPlayer();
        var gameMode = e.getNewGameMode();

        if (gameMode != GameMode.SURVIVAL) player.setInvulnerable(true);

        gameManager.loadPlayerFromGameMode(player, e.getNewGameMode());

        if (gameMode != GameMode.SURVIVAL) Bukkit.getScheduler().runTaskLater(Cerdomania.getInstance(), () -> player.setInvulnerable(false), 5);
    }

    @EventHandler
    private void onPlayerJoin(PlayerJoinEvent e) {
        if (!gameManager.getGameStage().equals(GameStage.RUNNING)) return;

        var player = e.getPlayer();

        player.setInvulnerable(true);

        gameManager.loadDefaultPlayer(player, true);

        Bukkit.getScheduler().runTaskLater(Cerdomania.getInstance(), () -> player.setInvulnerable(false), 5);
    }

    @EventHandler
    private void onPlayerLeave(PlayerQuitEvent e) {
        if (!gameManager.getGameStage().equals(GameStage.RUNNING)) return;

        var player = e.getPlayer();

        player.setInvulnerable(true);

        gameManager.unloadDefaultPlayer(player, false);
    }

    @EventHandler
    private void onEntityDeath(EntityDeathEvent e) {
        if (!gameManager.getGameStage().equals(GameStage.RUNNING)) return;

        var entity = e.getEntity();

        if (entity instanceof Player player) {
            if (!(player.getVehicle() != null && player.getVehicle().getType().equals(gameManager.getRaceType().getEntityType()))) return;

            gameManager.removeItems(player, gameManager.isBeacon());

            if (player.getGameMode().equals(GameMode.SURVIVAL)) player.getVehicle().remove();

            var raceItems = e.getDrops().stream().filter(d -> {
                Material mat;
                if (gameManager.getRaceType().equals(RaceType.PIG)) {
                    mat = Material.CARROT_ON_A_STICK;
                } else {
                    mat = Material.WARPED_FUNGUS_ON_A_STICK;
                }
                return (gameManager.isBeacon()) ? d.getType().equals(mat) || d.getType().equals(Material.BEACON) : d.getType().equals(mat);
            });

            e.getDrops().removeAll(raceItems.collect(Collectors.toCollection(LinkedList::new)));
        } else if (entity.getType().equals(gameManager.getRaceType().getEntityType())) {
            var passengers = entity.getPassengers();
            if (passengers.stream().noneMatch(p -> p instanceof Player)) return;

            spawnBloodParticles(entity, 3);

            var player = (Player) passengers.get(0);

            gameManager.removeItems(player, gameManager.isBeacon());
            player.setHealth(0);

            e.getDrops().clear();
        }
    }

    @EventHandler
    private void onPlayerRespawn(PlayerRespawnEvent e) {
        if (!gameManager.getGameStage().equals(GameStage.RUNNING)) return;

        var player = e.getPlayer();

        if (player.getGameMode().equals(GameMode.SURVIVAL)) {
            gameManager.giveItems(player, gameManager.isBeacon());

            var checkPoint = gameManager.getGameData().getRespawnLocations().get(player.getUniqueId());
            var checkPointLocation = (checkPoint != null) ? checkPoint : e.getRespawnLocation();

            var passenger = player.getWorld().spawnEntity(checkPointLocation, gameManager.getRaceType().getEntityType());
            passenger.addPassenger(player);

            Scoreboards.addToRaceTeam(passenger);
        }
    }

    @EventHandler
    private void onEntityHurt(EntityDamageEvent e) {
        if (!gameManager.getGameStage().equals(GameStage.RUNNING)) return;

        var entity = e.getEntity();

        if (entity instanceof Player player) {
            if (!player.getGameMode().equals(GameMode.SURVIVAL) || !(player.getVehicle() != null && player.getVehicle().getType().equals(gameManager.getRaceType().getEntityType()))) return;

            spawnBloodParticles(player, 5);
        } else if (entity.getType().equals(gameManager.getRaceType().getEntityType())) {
            if (entity.getPassengers().stream().noneMatch(p -> p instanceof Player)) return;

            spawnBloodParticles(entity, 5);
        }
    }

    private void spawnBloodParticles(Entity entity, int ticks) {
        var task = Bukkit.getScheduler().runTaskTimer(Cerdomania.getInstance(), () -> entity.getWorld().spawnParticle(Particle.REDSTONE, entity.getLocation(), 1, new Particle.DustOptions(Color.RED, 5)), 0, 1);
        Bukkit.getScheduler().runTaskLater(Cerdomania.getInstance(), task::cancel, ticks);
    }

}
