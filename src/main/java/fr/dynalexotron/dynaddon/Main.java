package fr.dynalexotron.dynaddon;

import fr.dynalexotron.dynaddon.commands.TpLoin;
import fr.ph1lou.werewolfapi.GetWereWolfAPI;
import fr.ph1lou.werewolfapi.enums.StateGame;
import fr.ph1lou.werewolfapi.registers.impl.AddonRegister;
import fr.ph1lou.werewolfapi.registers.impl.CommandRegister;
import fr.ph1lou.werewolfapi.registers.interfaces.IRegisterManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    private final String addonKey = "dynaddon.name";

    @Override
    public void onEnable() {
        GetWereWolfAPI ww = getServer().getServicesManager().load(GetWereWolfAPI.class);
        IRegisterManager registerManager = ww.getRegisterManager();

        registerManager.registerAddon(new AddonRegister(this.addonKey, "fr", this));

        registerManager.registerAdminCommands(new CommandRegister(this.addonKey, "dynaddon.commands.tploin.name", new TpLoin())
                .setHostAccess()
                .setModeratorAccess()
                .addStateWW(StateGame.GAME)
        );
    }
}
