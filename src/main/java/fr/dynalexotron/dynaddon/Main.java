package fr.dynalexotron.dynaddon;

import fr.ph1lou.werewolfapi.GetWereWolfAPI;
import fr.ph1lou.werewolfapi.registers.impl.AddonRegister;
import fr.ph1lou.werewolfapi.registers.interfaces.IRegisterManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        GetWereWolfAPI ww = getServer().getServicesManager().load(GetWereWolfAPI.class);
        IRegisterManager registerManager = ww.getRegisterManager();
        String addonkey = "dynaddon.name";

        registerManager.registerAddon(new AddonRegister(addonkey, "fr", this));
    }
}
