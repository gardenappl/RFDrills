package goldenapple.rfdrills.config;

import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import goldenapple.rfdrills.DrillTier;
import goldenapple.rfdrills.reference.Reference;
import goldenapple.rfdrills.util.LogHelper;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.util.EnumHelper;

import java.io.File;

public class ConfigHandler {
    public static Configuration config; //public because of OmniDrillsGuiConfig

    public static boolean integrateTE;
    public static boolean integrateEIO;
    public static boolean shearsDefault;
  /*public static String energyUnitName;
    public static EnumEnergyUnit energyUnit = EnumEnergyUnit.RF; */

    public ConfigHandler(File file){
        if(config == null) {
            config = new Configuration(file);
        }
        loadConfig();
    }

    private void loadConfig(){
      /*energyUnitName = config.getString("energyUnit", Configuration.CATEGORY_GENERAL, "RF", "Energy unit to display in tooltips. Valid units: EU, RF, gJ (GalactiCraft Joules), J (Mekanism Joules)", new String[]{"RF", "EU", "gJ", "J"});

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
        } */

        config.setCategoryRequiresMcRestart(Configuration.CATEGORY_GENERAL, true);
        integrateTE = config.getBoolean("integrateTE", Configuration.CATEGORY_GENERAL, true, "Set this to false to disable Thermal Expansion integration");
        integrateEIO = config.getBoolean("integrateEIO", Configuration.CATEGORY_GENERAL, true, "Set this to false to disable EnderIO integration");
        shearsDefault = config.getBoolean("shearsDefault", Configuration.CATEGORY_GENERAL, true, "Set this to false to disable shears functionality for lower tier chainsaws");
        try {
            DrillTier.DRILL1 = getDrillTierInfo("drill", 1, DrillTier.DRILL1_MATERIAL, 20000, 80, 80, EnumRarity.common, true, false, 2, 6.0F, 2.0F, 0);
            DrillTier.DRILL2 = getDrillTierInfo("drill", 2, DrillTier.DRILL2_MATERIAL, 100000, 200, 400, EnumRarity.common, false, false, 3, 8.0F, 3.0F, 0);
            DrillTier.DRILL3 = getDrillTierInfo("drill", 3, DrillTier.DRILL3_MATERIAL, 1000000, 1000, 1500, EnumRarity.uncommon, false, true, 3, 11.0F, 4.0F, 10);
            DrillTier.DRILL4 = getDrillTierInfo("drill", 4, DrillTier.DRILL4_MATERIAL, 4000000, 2000, 5000, EnumRarity.rare, false, true, 4, 13.0F, 5.0F, 15);

            DrillTier.CHAINSAW1 = getDrillTierInfo("chainsaw", 1, DrillTier.CHAINSAW1_MATERIAL, 20000, 80, 80, EnumRarity.common, true, false, 2, 6.0F, 3.0F, 0);
            DrillTier.CHAINSAW2 = getDrillTierInfo("chainsaw", 2, DrillTier.CHAINSAW2_MATERIAL, 100000, 200, 400, EnumRarity.common, false, false, 3, 7.0F, 4.0F, 0);
            DrillTier.CHAINSAW3 = getDrillTierInfo("chainsaw", 3, DrillTier.CHAINSAW3_MATERIAL, 1000000, 1000, 1500, EnumRarity.uncommon, false, true, 3, 8.0F, 5.0F, 10);
            DrillTier.CHAINSAW4 = getDrillTierInfo("chainsaw", 4, DrillTier.CHAINSAW4_MATERIAL, 4000000, 2000, 5000, EnumRarity.rare, false, true, 4, 10.0F, 6.0F, 15);
        }catch (Exception e){
            LogHelper.warn("Something went wrong with config files!");
            e.printStackTrace();
        }

        if(config.hasChanged()){
            config.save();
        }
    }

    private DrillTier getDrillTierInfo(String tool, int tierNumber, Item.ToolMaterial material, int maxEnergy, int energyPerBlock, int rechargeRate, EnumRarity rarity, boolean canBreak, boolean hasModes, int miningLevel, float efficiency, float damage, int enchant){
        maxEnergy = config.getInt("maxEnergy", tool + "_tier" + tierNumber, maxEnergy, 0, Integer.MAX_VALUE, "The max amount of energy that the tool can hold");
        energyPerBlock = config.getInt("energyPerBlock", tool + "_tier" + tierNumber, energyPerBlock, 0, Integer.MAX_VALUE, "The amount of energy that the tool uses to mine 1 block (uses twice as much energy for attacking)");
        rechargeRate = config.getInt("rechargeRate", tool + "_tier" + tierNumber, rechargeRate, 0, Integer.MAX_VALUE, "RF that the tool can recharge per tick");
        canBreak = config.getBoolean("canBreak", tool + "_tier" + tierNumber, canBreak, "Can the tool break when it runs out of energy");
        hasModes = config.getBoolean("hasModes", tool + "_tier" + tierNumber, hasModes, "Can the tool change modes on shift+right click");
        miningLevel = config.getInt("miningLevel", tool + "_tier" + tierNumber, miningLevel, 0, Integer.MAX_VALUE, "Mining level of the tool (1 - can mine iron, 2 - can mine diamonds etc.)");
        efficiency = config.getFloat("efficiency", tool + "_tier" + tierNumber, efficiency, 0, Float.MAX_VALUE, "Efficiency of the tool");
        damage = config.getFloat("damage", tool + "_tier" + tierNumber, damage, 0, Float.MAX_VALUE, "Damage that the tool does when attacking enemies");
        enchant = config.getInt("enchant", tool + "_tier" + tierNumber, enchant, 0, Integer.MAX_VALUE, "Enchantablity of the tool (0 - not enchantable)");
        material = EnumHelper.addToolMaterial(tool.toUpperCase() + tierNumber, miningLevel, 10, efficiency, damage, enchant);
        return new DrillTier(material, maxEnergy, rechargeRate, energyPerBlock, rarity, canBreak, hasModes);
    }

    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event){
        if(event.modID.equals(Reference.MOD_ID)){
            loadConfig();
        }
    }
}
