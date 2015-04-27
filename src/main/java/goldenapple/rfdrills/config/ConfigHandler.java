package goldenapple.rfdrills.config;

import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
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
    /*public static String energyUnitName;
    public static EnumEnergyUnit energyUnit = EnumEnergyUnit.RF; */

    /* TIER1_MATERIAL = EnumHelper.addToolMaterial("TIER1_DRILL", 2, 9001, 6.0F, 2.0F, 0);
    TIER2_MATERIAL = EnumHelper.addToolMaterial("TIER2_DRILL", 3, 9001, 7.0F, 3.0F, 0);
    TIER3_MATERIAL = EnumHelper.addToolMaterial("TIER3_DRILL", 3, 9001, 8.0F, 4.0F, 0);
    TIER4_MATERIAL = EnumHelper.addToolMaterial("TIER4_DRILL", 4, 9001, 10.0F, 5.0F, 10); */

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
        integrateTE = config.getBoolean("integrateTE", Configuration.CATEGORY_GENERAL, true, "Set this to false to disable Thermal Expansion drills");
        integrateEIO = config.getBoolean("integrateEIO", Configuration.CATEGORY_GENERAL, true, "Set this to false to disable EnderIO drills");
        try {
            DrillTier.TIER_1 = getTierInfo(1, DrillTier.TIER1_MATERIAL, 20000, 80, 80, EnumRarity.common, true, 2, 6.0F, 2.0F, 0);
            DrillTier.TIER_2 = getTierInfo(2, DrillTier.TIER2_MATERIAL, 100000, 200, 400, EnumRarity.common, false, 3, 7.0F, 3.0F, 0);
            DrillTier.TIER_3 = getTierInfo(3, DrillTier.TIER3_MATERIAL, 1000000, 1000, 1500, EnumRarity.uncommon, false, 3, 8.0F, 4.0F, 0);
            DrillTier.TIER_4 = getTierInfo(4, DrillTier.TIER4_MATERIAL, 4000000, 2000, 5000, EnumRarity.rare, false, 4, 10.0F, 5.0F, 10);
        }catch (Exception e){
            LogHelper.warn("Something went wrong with config files!");
            e.printStackTrace();
        }

        if(config.hasChanged()){
            config.save();
        }
    }

    private DrillTier getTierInfo(int tierNumber, Item.ToolMaterial material, int maxEnergy, int energyPerBlock, int rechargeRate, EnumRarity rarity, boolean canBreak, int miningLevel, float efficiency, float damage, int enchant){
        maxEnergy = config.getInt("maxEnergy", "tier" + tierNumber, maxEnergy, 0, Integer.MAX_VALUE, "How much energy can tier" + tierNumber + " drills hold");
        energyPerBlock = config.getInt("energyPerBlock", "tier" + tierNumber, energyPerBlock, 0, Integer.MAX_VALUE, "How much energy will tier" + tierNumber + " drills require to mine a block");
        rechargeRate = config.getInt("rechargeRate", "tier" + tierNumber, rechargeRate, 0, Integer.MAX_VALUE, "How much RF can tier" + tierNumber + " drills recharge per tick");
        canBreak = config.getBoolean("canBreak", "tier" + tierNumber, canBreak, "Can tier " + tierNumber + " drills break if they run out of energy");
        miningLevel = config.getInt("miningLevel", "tier" + tierNumber, miningLevel, 0, Integer.MAX_VALUE, "Mining level of tier " + tierNumber + " drills (1 - can mine iron, 2 - can mine diamonds etc.) REQUIRES MC RESTART");
        efficiency = config.getFloat("efficiency", "tier" + tierNumber, efficiency, 0, Float.MAX_VALUE, "Efficiency of tier " + tierNumber + " drills REQUIRES MC RESTART");
        damage = config.getFloat("damage", "tier" + tierNumber, damage, 0, Float.MAX_VALUE, "Damage that tier " + tierNumber + " drills do when attacking enemies REQUIRES MC RESTART");
        enchant = config.getInt("enchant", "tier" + tierNumber, enchant, 0, Integer.MAX_VALUE, "Enchantablity of tier " + tierNumber + " drills (0 - not enchantable) REQUIRES MC RESTART");
        material = EnumHelper.addToolMaterial("DRILL_TIER" + tierNumber, miningLevel, 10, efficiency, damage, enchant);
        return new DrillTier(material, maxEnergy, rechargeRate, energyPerBlock, rarity, canBreak);
    }

    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event){
        if(event.modID.equals(Reference.MOD_ID)){
            loadConfig();
        }
    }
}
