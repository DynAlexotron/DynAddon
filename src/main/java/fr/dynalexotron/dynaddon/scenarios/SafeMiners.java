package fr.dynalexotron.dynaddon.scenarios;

import fr.ph1lou.werewolfapi.GetWereWolfAPI;
import fr.ph1lou.werewolfapi.enums.Prefix;
import fr.ph1lou.werewolfapi.events.game.game_cycle.StopEvent;
import fr.ph1lou.werewolfapi.events.game.timers.DiggingEndEvent;
import fr.ph1lou.werewolfapi.listeners.ListenerManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class SafeMiners extends ListenerManager {
    private boolean damageEnabled = false;
    private final JavaPlugin plugin;

    public SafeMiners(GetWereWolfAPI main, JavaPlugin plugin) {
        super(main);
        this.plugin = plugin;
    }

    @EventHandler
    public void onGameStop(StopEvent event) {
        this.damageEnabled = false;
    }

    @EventHandler
    public void onMiningEnd(DiggingEndEvent event) {
        Bukkit.broadcastMessage(this.getGame().translate(Prefix.ORANGE.getKey(), "dynaddon.scenarios.safeminers.mining_end"));
        Bukkit.getScheduler().scheduleSyncDelayedTask(this.plugin, new Runnable() {
            @Override
            public void run() {
                damageEnabled = true;
            }
        }, 20*15);
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        if(!(event.getEntity() instanceof Player)) return;
        if(event.getCause() == EntityDamageEvent.DamageCause.SUICIDE || event.getCause() == EntityDamageEvent.DamageCause.VOID || event.getCause() == EntityDamageEvent.DamageCause.CUSTOM) return;
        if(this.damageEnabled) return;

        if(event.getEntity().getLocation().getY() > 30 || event.getEntity().getLocation().getY() < 0) return;

        event.setCancelled(true);
    }
}
