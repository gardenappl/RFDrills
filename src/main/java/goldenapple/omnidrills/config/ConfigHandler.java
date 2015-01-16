package goldenapple.omnidrills.config;

import cpw.mods.fml.client.event.ConfigChangedEvent;
import goldenapple.omnidrills.reference.Reference;
import net.minecraftforge.common.config.Configuration;

import java.io.File;

public class ConfigHandler {
    public Configuration config; //public because of OmniDrillsGuiConfig

    public static String energyUnitName;
    public static EnumEnergyUnit energyUnit;

    public ConfigHandler(File file){
        config = new Configuration(file);
        loadConfig();
    }

    private void loadConfig(){
        energyUnitName = config.getString("energyUnit", Configuration.CATEGORY_GENERAL, "RF", "Energy unit to display in tooltips (gJ = GalactiCraft Joules, J = Mekanism Joules", new String[]{"RF", "EU", "gJ", "J"});

        if(energyUnitName.equals("EU")){
            energyUnit = EnumEnergyUnit.EU;
        }else if(energyUnitName.equals("J")){
            energyUnit = EnumEnergyUnit.J;
        }else if(energyUnitName.equals("gJ")){
            energyUnit = EnumEnergyUnit.gJ;
        }else {
            energyUnit = EnumEnergyUnit.RF;
        }
    }

    public void onConfigChanged(ConfigChangedEvent event){
        if(event.modID.equals(Reference.MOD_ID)){
            loadConfig();
        }
    }
}
