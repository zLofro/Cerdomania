package me.lofro.cerdomania.game.listeners;

import me.lofro.cerdomania.Cerdomania;
import me.lofro.cerdomania.game.GameManager;
import me.lofro.cerdomania.game.enums.GameStage;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.GameMode;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;

public class RaceListeners implements Listener {

    private final GameManager gameManager;

    public RaceListeners(final GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @EventHandler
    private void onVehicleExit(VehicleExitEvent e) {
        if (!gameManager.getGameStage().equals(GameStage.RUNNING)) return;

        var vehicle = e.getVehicle();
        var passenger = e.getExited();

        if (passenger instanceof Player player && player.getGameMode().equals(GameMode.SURVIVAL) && vehicle.getType().equals(gameManager.getRaceType().getEntityType())) {
            gameManager.removeItems(player, gameManager.isBeacon());
            vehicle.remove();
            player.setHealth(0);
        }
    }

    @EventHandler
    private void onEntityDeath(EntityDeathEvent e) {
        if (!gameManager.getGameStage().equals(GameStage.RUNNING)) return;

        var entity = e.getEntity();

        if (entity instanceof Player player) {
            if (!(player.getVehicle() != null && player.getVehicle().getType().equals(gameManager.getRaceType().getEntityType()))) return;

            gameManager.removeItems(player, gameManager.isBeacon());

            if (player.getGameMode().equals(GameMode.SURVIVAL)) player.getVehicle().remove();
        }
    }

    @EventHandler
    private void onPlayerRespawn(PlayerRespawnEvent e) {
        if (!gameManager.getGameStage().equals(GameStage.RUNNING)) return;

        var player = e.getPlayer();

        if (player.getGameMode().equals(GameMode.SURVIVAL)) {
            gameManager.giveItems(player, gameManager.isBeacon());

            var passenger = player.getWorld().spawnEntity(player.getLocation(), gameManager.getRaceType().getEntityType());
            passenger.addPassenger(player);
        }
    }

    @EventHandler
    public void onEntityHurt(EntityDamageEvent e) {
        if (!gameManager.getGameStage().equals(GameStage.RUNNING)) return;

        var entity = e.getEntity();

        if (entity instanceof Player player) {
            if (!player.getGameMode().equals(GameMode.SURVIVAL) || !(player.getVehicle() != null && player.getVehicle().getType().equals(gameManager.getRaceType().getEntityType()))) return;

            var task = Bukkit.getScheduler().runTaskTimer(Cerdomania.getInstance(), () -> player.getWorld().spawnParticle(Particle.REDSTONE, player.getLocation(), 1, new Particle.DustOptions(Color.RED, 5)), 0, 1);
            Bukkit.getScheduler().runTaskLater(Cerdomania.getInstance(), task::cancel, 5);
        } else if (entity.getType().equals(gameManager.getRaceType().getEntityType())) {
            if (entity.getPassengers().stream().noneMatch(p -> p instanceof Player)) return;

            var task = Bukkit.getScheduler().runTaskTimer(Cerdomania.getInstance(), () -> entity.getWorld().spawnParticle(Particle.REDSTONE, entity.getLocation(), 1, new Particle.DustOptions(Color.RED, 5)), 0, 1);
            Bukkit.getScheduler().runTaskLater(Cerdomania.getInstance(), task::cancel, 5);
        }
    }

}
