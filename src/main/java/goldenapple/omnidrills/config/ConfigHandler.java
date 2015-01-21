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
    public static EnumEnergyUnit energyUnit;

    public ConfigHandler(File file){
        if(config == null) {
            config = new Configuration(file);
        }
        loadConfig();
    }

    private void loadConfig(){
        energyUnitName = config.getString("energyUnit", Configuration.CATEGORY_GENERAL, "RF", "Energy unit to display in tooltips. Valid units: EU, RF, gJ (GalactiCraft Joules), J (Mekanism Joules)", new String[]{"RF", "EU", "gJ", "J"});

        if(energyUnitName.equals("EU")){
            LogHelper.info("Using EU");
            energyUnit = EnumEnergyUnit.EU;
        }else if(energyUnitName.equals("J")){
            LogHelper.info("Using Joules");
            energyUnit = EnumEnergyUnit.J;
        }else if(energyUnitName.equals("gJ")){
            LogHelper.info("Using Galacticraft Joules");
            energyUnit = EnumEnergyUnit.gJ;
        }else {
            LogHelper.info("Using RF");
            energyUnit = EnumEnergyUnit.RF;
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
