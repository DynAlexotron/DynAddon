package fr.dynalexotron.dynaddon;

import fr.dynalexotron.dynaddon.commands.Near;
import fr.dynalexotron.dynaddon.commands.TpLoin;
import fr.dynalexotron.dynaddon.roles.Bouffon;
import fr.dynalexotron.dynaddon.scenarios.SafeMiners;
import fr.dynalexotron.dynaddon.scenarios.Timber;
import fr.dynalexotron.dynaddon.scenarios.TimberPvp;
import fr.ph1lou.werewolfapi.GetWereWolfAPI;
import fr.ph1lou.werewolfapi.enums.Category;
import fr.ph1lou.werewolfapi.enums.ScenariosBase;
import fr.ph1lou.werewolfapi.enums.StateGame;
import fr.ph1lou.werewolfapi.enums.TimerBase;
import fr.ph1lou.werewolfapi.events.roles.elder.ElderResurrectionEvent;
import fr.ph1lou.werewolfapi.registers.impl.*;
import fr.ph1lou.werewolfapi.registers.interfaces.IRegisterManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    private final String addonKey = "dynaddon.name";

    @Override
    public void onEnable() {
        GetWereWolfAPI ww = getServer().getServicesManager().load(GetWereWolfAPI.class);
        IRegisterManager registerManager = ww.getRegisterManager();

        registerManager.registerAddon(new AddonRegister(this.addonKey, "fr", this));

        try {
            registerManager.registerRole(new RoleRegister(this.addonKey, "dynaddon.roles.bouffon.name", Bouffon.class)
                    .addCategory(Category.ADDONS)
                    .addCategory(Category.NEUTRAL)
                    .addLoreKey("dynaddon.dev_by")
            );
        } catch (Exception e) {
            e.printStackTrace();
        }

        registerManager.registerTimer(new TimerRegister(this.addonKey, "dynaddon.timers.bouffon")
                .onZero(api -> Bukkit.getPluginManager().callEvent(new Bouffon.BouffonTargetEvent()))
                .setRoleTimer("dynaddon.roles.bouffon.name")
                .setDefaultValue(5*60)
                .addPredicate(api -> api.getConfig().getTimerValue(TimerBase.ROLE_DURATION.getKey()) < 0 && !api.getConfig().isTrollSV())
        );

        registerManager.getScenariosRegister().removeIf(sc -> sc.getKey().equalsIgnoreCase(ScenariosBase.TIMBER.getKey()));
        registerManager.registerScenario(new ScenarioRegister(this.addonKey, "dynaddon.scenarios.safeminers.name", new SafeMiners(ww, this))
                .setDefaultValue()
        );
        registerManager.registerScenario(new ScenarioRegister(this.addonKey, "dynaddon.scenarios.timber.name", new Timber(ww))
                .addIncompatibleScenario("dynaddon.scenarios.timber_pvp.name")
        );
        registerManager.registerScenario(new ScenarioRegister(this.addonKey, "dynaddon.scenarios.timber_pvp.name", new TimberPvp(ww))
                .addIncompatibleScenario("dynaddon.scenarios.timber.name")
        );

        registerManager.registerAdminCommands(new CommandRegister(this.addonKey, "dynaddon.commands.tploin.name", new TpLoin())
                .setHostAccess()
                .setModeratorAccess()
                .addStateWW(StateGame.GAME)
        );
        registerManager.registerAdminCommands(new CommandRegister(this.addonKey, "dynaddon.commands.near.name", new Near())
                .setHostAccess()
                .setModeratorAccess()
                .addStateWW(StateGame.GAME)
        );
    }
}
