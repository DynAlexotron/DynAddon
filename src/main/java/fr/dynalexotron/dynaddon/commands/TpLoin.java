package fr.dynalexotron.dynaddon.commands;

import fr.ph1lou.werewolfapi.commands.ICommand;
import fr.ph1lou.werewolfapi.enums.StatePlayer;
import fr.ph1lou.werewolfapi.game.WereWolfAPI;
import fr.ph1lou.werewolfapi.player.interfaces.IPlayerWW;
import fr.ph1lou.werewolfapi.player.utils.Formatter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Random;

public class TpLoin implements ICommand {
    @Override
    public void execute(WereWolfAPI game, Player player, String[] args) {
        Player target = Bukkit.getPlayer(args[0]);

        if(target == null) {
            //TODO send message
            return;
        }

        IPlayerWW targetWW = game.getPlayerWW(target.getUniqueId()).orElse(null);

        if(targetWW == null) {
            //TODO send message
            return;
        }
        if(!targetWW.isState(StatePlayer.ALIVE)) {
            //TODO send message
            return;
        }

        game.getMapManager().transportation(targetWW, new Random().nextDouble() * 2 * Math.PI);

        player.sendMessage(game.translate("dynaddon.commands.tploin.sender_message", Formatter.player(targetWW.getName())));
        targetWW.sendMessageWithKey("dynaddon.commands.tploin.target_message");
    }
}
