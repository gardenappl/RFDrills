package goldenapple.omnidrills.config;

import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import goldenapple.omnidrills.reference.Reference;
import goldenapple.omnidrills.util.LogHelper;
import net.minecraftforge.common.config.Configuration;

import java.io.File;

public class ConfigHandler {
    public Configuration config; //public because of OmniDrillsGuiConfig

    public static String energyUnitName;
    public static EnumEnergyUnit energyUnit = EnumEnergyUnit.RF;

    public ConfigHandler(File file){
        if(config == null) {
            config = new Configuration(file);
        }
        loadConfig();
    }

    private void loadConfig(){
        energyUnitName = config.getString("energyUnit", Configuration.CATEGORY_GENERAL, "RF", "Energy unit to display in tooltips. Valid units: EU, RF, gJ (GalactiCraft Joules), J (Mekanism Joules)", new String[]{"RF", "EU", "gJ", "J"});

        if(energyUnitName.equals("EU")){
            energyUnit = EnumEnergyUnit.EU;
        }else if(energyUnitName.equals("J")){
            energyUnit = EnumEnergyUnit.J;
        }else if(energyUnitName.equals("gJ")){
            energyUnit = EnumEnergyUnit.gJ;
        }else if(energyUnitName.equals("RF")){
            energyUnit = EnumEnergyUnit.RF;
        }else{
            LogHelper.info("Invalid EnergyUnit value. Using default (RF)");
        }

        if(config.hasChanged()){
            config.save();
        }
    }

    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event){
        if(event.modID.equals(Reference.MOD_ID)){
            loadConfig();
        }
    }
}
