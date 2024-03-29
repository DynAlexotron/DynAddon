package fr.dynalexotron.dynaddon;

import fr.dynalexotron.dynaddon.commands.Near;
import fr.dynalexotron.dynaddon.commands.TpLoin;
import fr.dynalexotron.dynaddon.enums.ECommands;
import fr.dynalexotron.dynaddon.enums.ERoles;
import fr.dynalexotron.dynaddon.enums.EScenarios;
import fr.dynalexotron.dynaddon.roles.Bouffon;
import fr.dynalexotron.dynaddon.scenarios.NoAlternateStones;
import fr.dynalexotron.dynaddon.scenarios.SafeMiners;
import fr.dynalexotron.dynaddon.scenarios.Timber;
import fr.dynalexotron.dynaddon.scenarios.TimberPvp;
import fr.ph1lou.werewolfapi.GetWereWolfAPI;
import fr.ph1lou.werewolfapi.enums.*;
import fr.ph1lou.werewolfapi.registers.impl.*;
import fr.ph1lou.werewolfapi.registers.interfaces.IRegisterManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    private final String addonKey = "dynaddon.name";
    private static Main instance;

    @Override
    public void onEnable() {
        instance = this;

        GetWereWolfAPI ww = getServer().getServicesManager().load(GetWereWolfAPI.class);
        IRegisterManager registerManager = ww.getRegisterManager();

        registerManager.registerAddon(new AddonRegister(this.addonKey, "fr", this));

        try {
            registerManager.registerRole(new RoleRegister(this.addonKey, ERoles.BOUFFON.getKey(), Bouffon.class)
                    .addCategory(Category.ADDONS)
                    .addCategory(Category.NEUTRAL)
                    .addLoreKey("dynaddon.dev_by")
            );
        } catch (Exception e) {
            e.printStackTrace();
        }

        registerManager.registerTimer(new TimerRegister(this.addonKey, "dynaddon.timers.bouffon")
                .onZero(api -> Bukkit.getPluginManager().callEvent(new Bouffon.BouffonTargetEvent()))
                .setRoleTimer(ERoles.BOUFFON.getKey())
                .setDefaultValue(5*60)
                .addPredicate(api -> api.getConfig().getTimerValue(TimerBase.ROLE_DURATION.getKey()) < 0 && !api.getConfig().isTrollSV())
                .addLoreKey("dynaddon.dev_by")
        );

        registerManager.registerAdminCommands(new CommandRegister(this.addonKey, ECommands.TPLOIN.getKey(), new TpLoin())
                .setHostAccess()
                .setModeratorAccess()
                .addStateWW(StateGame.GAME)
                .setDescription(ww.getWereWolfAPI().translate("dynaddon.dev_by"))
        );
        registerManager.registerAdminCommands(new CommandRegister(this.addonKey, ECommands.NEAR.getKey(), new Near())
                .setHostAccess()
                .setModeratorAccess()
                .addStateWW(StateGame.GAME)
                .setDescription(ww.getWereWolfAPI().translate("dynaddon.dev_by"))
        );
    }

    public static Main getInstance() {
        return instance;
    }
}
