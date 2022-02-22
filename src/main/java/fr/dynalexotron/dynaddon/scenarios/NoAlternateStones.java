package fr.dynalexotron.dynaddon.scenarios;

import fr.ph1lou.werewolfapi.GetWereWolfAPI;
import fr.ph1lou.werewolfapi.listeners.ListenerManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class NoAlternateStones extends ListenerManager {
    public NoAlternateStones(GetWereWolfAPI main) {
        super(main);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if(!event.getBlock().getType().equals(Material.STONE)) return;

        event.getBlock().setType(Material.AIR);
        event.getBlock().getWorld().dropItem(new Location(event.getBlock().getWorld(), event.getBlock().getLocation().getBlockX() + 0.5, event.getBlock().getLocation().getBlockY() + 0.5, event.getBlock().getLocation().getBlockZ() + 0.5),
                new ItemStack(Material.COBBLESTONE)
        );
    }
}
