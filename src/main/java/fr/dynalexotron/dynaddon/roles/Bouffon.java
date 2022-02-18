package fr.dynalexotron.dynaddon.roles;

import fr.ph1lou.werewolfapi.enums.Camp;
import fr.ph1lou.werewolfapi.enums.Prefix;
import fr.ph1lou.werewolfapi.enums.StatePlayer;
import fr.ph1lou.werewolfapi.events.game.life_cycle.FirstDeathEvent;
import fr.ph1lou.werewolfapi.game.WereWolfAPI;
import fr.ph1lou.werewolfapi.player.impl.PotionModifier;
import fr.ph1lou.werewolfapi.player.interfaces.IPlayerWW;
import fr.ph1lou.werewolfapi.player.utils.Formatter;
import fr.ph1lou.werewolfapi.role.impl.RoleNeutral;
import fr.ph1lou.werewolfapi.role.interfaces.IAffectedPlayers;
import fr.ph1lou.werewolfapi.role.interfaces.IPower;
import fr.ph1lou.werewolfapi.role.utils.DescriptionBuilder;
import org.bukkit.entity.Arrow;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Bouffon extends RoleNeutral implements IPower, IAffectedPlayers {
    private boolean power = true;
    private boolean hasBeenKilledByTarget = false;
    private IPlayerWW target = null;

    public static class BouffonTargetEvent extends Event {
        private static final HandlerList HANDLERS_LIST = new HandlerList();

        @Override
        public HandlerList getHandlers() {
            return HANDLERS_LIST;
        }

        public static HandlerList getHandlerList() {
            return HANDLERS_LIST;
        }
    }

    public Bouffon(WereWolfAPI game, IPlayerWW playerWW, String key) {
        super(game, playerWW, key);
    }

    @Override
    public @NotNull String getDescription() {
        return new DescriptionBuilder(this.game, this)
                .setDescription(this.game.translate("dynaddon.roles.bouffon.description"))
                .setPower(this.game.translate("dynaddon.roles.bouffon.power", Formatter.format("&available&", this.game.translate(this.hasPower() ? "dynaddon.roles.bouffon.avaiable" : "dynaddon.roles.bouffon.unavailable"))))
                .addExtraLines(this.game.translate(this.target == null ? "dynaddon.roles.bouffon.target_not_defined_yet" : "dynaddon.roles.bouffon.target", Formatter.player(this.target == null ? "" : this.target.getName())))
                .build();
    }

    @EventHandler
    public void onBouffonTarget(BouffonTargetEvent event) {
        List<IPlayerWW> availablePlayers = this.game.getPlayersWW().stream()
                .filter(pWW -> pWW.isState(StatePlayer.ALIVE))
                .filter(pWW -> pWW.getRole() != null)
                .filter(pWW -> pWW.getRole().isCamp(Camp.VILLAGER))
                .collect(Collectors.toList());

        if(availablePlayers.isEmpty()) return;

        this.target = availablePlayers.get(new Random().nextInt(availablePlayers.size()));
        this.getPlayerWW().sendMessageWithKey("dynaddon.roles.bouffon.target_reveal", Formatter.player(this.target.getName()));
    }

    @EventHandler
    public void onFirstDeath(FirstDeathEvent event) {
        if(event.isCancelled()) return;

        if(!event.getPlayerWW().equals(this.getPlayerWW())) return;
        if(this.target == null) return;
        if(!this.target.isState(StatePlayer.ALIVE)) return;
        if(!this.hasPower()) return;

        IPlayerWW killer = event.getPlayerWW().getLastKiller().orElse(null);

        if(killer == null) return;
        if(!killer.equals(this.target)) return;

        event.setCancelled(true);
        game.resurrection(this.getPlayerWW());
        this.setPower(false);
        this.hasBeenKilledByTarget = true;
        this.getPlayerWW().sendMessageWithKey(Prefix.GREEN.getKey(), "dynaddon.roles.bouffon.get_killed");

        this.getPlayerWW().addPlayerMaxHealth(6);
        this.getPlayerWW().addPotionModifier(PotionModifier.add(PotionEffectType.INCREASE_DAMAGE, "bouffon"));
    }

    @Override
    public void recoverPotionEffect() {
        super.recoverPotionEffect();
        if(this.hasBeenKilledByTarget) {
            this.getPlayerWW().addPotionModifier(PotionModifier.add(PotionEffectType.INCREASE_DAMAGE, "bouffon"));
        }
    }

    @Override
    public void recoverPower() {

    }

    @Override
    public void setPower(boolean power) {
        this.power = power;
    }

    @Override
    public boolean hasPower() {
        return this.power;
    }

    @Override
    public void addAffectedPlayer(IPlayerWW playerWW) {
        this.target = playerWW;
    }

    @Override
    public void removeAffectedPlayer(IPlayerWW playerWW) {
        if(this.target.getUUID().equals(playerWW.getUUID())) {
            this.target = null;
        }
    }

    @Override
    public void clearAffectedPlayer() {
        this.target = null;
    }

    @Override
    public List<? extends IPlayerWW> getAffectedPlayers() {
        return Collections.singletonList(this.target);
    }
}
