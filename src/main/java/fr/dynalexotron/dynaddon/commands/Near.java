package fr.dynalexotron.dynaddon.commands;

import fr.ph1lou.werewolfapi.commands.ICommand;
import fr.ph1lou.werewolfapi.enums.Prefix;
import fr.ph1lou.werewolfapi.enums.StatePlayer;
import fr.ph1lou.werewolfapi.game.WereWolfAPI;
import fr.ph1lou.werewolfapi.player.interfaces.IPlayerWW;
import fr.ph1lou.werewolfapi.player.utils.Formatter;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class Near implements ICommand {
    @Override
    public void execute(WereWolfAPI game, Player player, String[] args) {
        int d = 100;

        List<IPlayerWW> players = game.getPlayersWW().stream()
                .filter(pWW -> pWW.isState(StatePlayer.ALIVE))
                .filter(pWW -> pWW.getLocation().distance(player.getLocation()) <= d)
                .collect(Collectors.toList());

        if(players.isEmpty()) {
            player.sendMessage(game.translate(Prefix.RED.getKey(), "dynaddon.commands.near.no_players"));
            return;
        }

        player.sendMessage(game.translate("dynaddon.commands.near.list_title", Formatter.number(d)));
        players.forEach(pWW -> player.sendMessage(game.translate("dynaddon.commands.near.player", Formatter.player(pWW.getName()), Formatter.number(Math.round(Math.round(pWW.getLocation().distance(player.getLocation())))))));
    }
}
