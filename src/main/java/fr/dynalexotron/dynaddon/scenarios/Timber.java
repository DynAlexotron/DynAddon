package fr.dynalexotron.dynaddon.scenarios;

import fr.ph1lou.werewolfapi.GetWereWolfAPI;
import fr.ph1lou.werewolfapi.enums.TimerBase;
import fr.ph1lou.werewolfapi.listeners.ListenerManager;
import fr.ph1lou.werewolfapi.registers.impl.TimerRegister;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;

public class Timber extends ListenerManager {
    public Timber(GetWereWolfAPI main) {
        super(main);
    }

    @EventHandler
    public void on(BlockBreakEvent event) {
        Block block = event.getBlock();

        if(!this.isLog(block.getType())) return;
        if(this instanceof TimberPvp) {
            if(this.getGame().getConfig().getTimerValue(TimerBase.PVP.getKey()) <= 0) return;
        }

        event.setCancelled(true);
        this.breakTree(block);
    }

    private void breakTree(Block block) {
        if(!this.isLog(block.getType())) return;

        block.breakNaturally();

        for (BlockFace face : BlockFace.values()) {
            breakTree(block.getRelative(face));
        }
    }

    private boolean isLog(Material m) {
        return m.equals(Material.LOG) || m.equals(Material.LOG_2);
    }
}
