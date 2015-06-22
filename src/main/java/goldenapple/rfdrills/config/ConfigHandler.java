package goldenapple.rfdrills.config;

import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import goldenapple.rfdrills.DrillTier;
import goldenapple.rfdrills.reference.Reference;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.util.EnumHelper;

import java.io.File;

public class ConfigHandler {
    public static Configuration config; //public because of OmniDrillsGuiConfig

    public static boolean integrateTE;
    public static boolean integrateEIO;
    public static boolean integrateRArs;
    public static boolean integrateSJ;
    public static boolean integrateRArm;
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

        integrateTE = config.getBoolean("Integrate TE", Configuration.CATEGORY_GENERAL, true, "Set this to false to disable Thermal Expansion integration");
        integrateEIO = config.getBoolean("Integrate EIO", Configuration.CATEGORY_GENERAL, true, "Set this to false to disable EnderIO integration");
        integrateRArs = config.getBoolean("Integrate Redstone Arsenal", Configuration.CATEGORY_GENERAL, true, "Set this to false to disable using Redstone Arsenal items in the mod's recipes");
        integrateRArm = config.getBoolean("Integrate Redstone Armory", Configuration.CATEGORY_GENERAL, true, "Set this to false to disable using Redstone Armory items in the mod's recipes");
        integrateSJ = config.getBoolean("Integrate Simply Jetpacks", Configuration.CATEGORY_GENERAL, true, "Set this to false to disable Simply Jetpacks integration");
        shearsDefault = config.getBoolean("Shears Default", Configuration.CATEGORY_GENERAL, true, "Set this to false to disable shears functionality for lower tier chainsaws");

        DrillTier.DRILL1 = getDrillTierInfo("drill", 1, 20000, 80, 80, EnumRarity.common, true, false, 2, 6.0F, 2.0F, 0);
        DrillTier.DRILL2 = getDrillTierInfo("drill", 2, 100000, 200, 400, EnumRarity.common, false, false, 3, 8.0F, 3.0F, 0);
        DrillTier.DRILL3 = getDrillTierInfo("drill", 3, 1000000, 800, 1500, EnumRarity.uncommon, false, true, 3, 10.0F, 4.0F, 10);
        DrillTier.DRILL4 = getDrillTierInfo("drill", 4, 5000000, 1500, 5000, EnumRarity.rare, false, true, 4, 12.0F, 5.0F, 15);
        DrillTier.FLUX_CRUSHER = getDrillTierInfo("drill", 5, 25000000, 5000, 10000, EnumRarity.epic, false, true, 5, 15.0F, 8.0F, 20);
        int soulCrusherMiningLevel = config.getInt("Mining Level", "soulcrusher", 5, 0, Integer.MAX_VALUE, "Mining level of the tool (1 - can mine iron, 2 - can mine diamonds etc.)");
        float soulCrusherEfficiency = config.getFloat("Efficiency", "soulcrusher", 12.0F, 0, Float.MAX_VALUE, "Efficiency of the tool without upgrades");
        float soulCrusherDamage = config.getFloat("Damage", "soulcrusher", 8.0F, 0, Float.MAX_VALUE, "Damage that the tool does when attacking enemies");
        int soulCrusherEnchant = config.getInt("Enchantability", "soulcrusher", 20, 0, Integer.MAX_VALUE, "Enchantablity of the tool (0 - not enchantable)");
        boolean soulCrusherHasModes = config.getBoolean("Has Modes", "soulcrusher", true, "Can the tool change modes on shift+right click");
        DrillTier.SOUL_CRUSHER = new DrillTier(EnumHelper.addToolMaterial("SOUL_CRUSHER", soulCrusherMiningLevel, 9000, soulCrusherEfficiency, soulCrusherDamage, soulCrusherEnchant), 200000, 400, 750, EnumRarity.rare, true, soulCrusherHasModes);

        DrillTier.CHAINSAW1 = getDrillTierInfo("chainsaw", 1, 20000, 80, 80, EnumRarity.common, true, false, 2, 6.0F, 3.0F, 0);
        DrillTier.CHAINSAW2 = getDrillTierInfo("chainsaw", 2, 100000, 200, 400, EnumRarity.common, false, false, 3, 7.0F, 4.0F, 0);
        DrillTier.CHAINSAW3 = getDrillTierInfo("chainsaw", 3, 1000000, 800, 1500, EnumRarity.uncommon, false, true, 3, 10.0F, 5.0F, 10);
        DrillTier.CHAINSAW4 = getDrillTierInfo("chainsaw", 4, 5000000, 1500, 5000, EnumRarity.rare, false, true, 4, 12.0F, 6.0F, 15);

        if(config.hasChanged()){
            config.save();
        }
    }

    private DrillTier getDrillTierInfo(String tool, int tierNumber, int maxEnergy, int energyPerBlock, int rechargeRate, EnumRarity rarity, boolean canBreak, boolean hasModes, int miningLevel, float efficiency, float damage, int enchant){
        maxEnergy = config.getInt("Maximum Energy", tool + "_tier" + tierNumber, maxEnergy, 0, Integer.MAX_VALUE, "The max amount of energy that the tool can hold");
        energyPerBlock = config.getInt("Energy Per Block", tool + "_tier" + tierNumber, energyPerBlock, 0, Integer.MAX_VALUE, "The amount of energy that the tool uses to mine 1 block (uses twice as much energy for attacking)");
        rechargeRate = config.getInt("Recharge Rate", tool + "_tier" + tierNumber, rechargeRate, 0, Integer.MAX_VALUE, "RF that the tool can recharge per tick");
        canBreak = config.getBoolean("Can Break", tool + "_tier" + tierNumber, canBreak, "Can the tool break when it runs out of energy");
        hasModes = config.getBoolean("Has Modes", tool + "_tier" + tierNumber, hasModes, "Can the tool change modes on shift+right click");
        miningLevel = config.getInt("Mining Level", tool + "_tier" + tierNumber, miningLevel, 0, Integer.MAX_VALUE, "Mining level of the tool (1 - can mine iron, 2 - can mine diamonds etc.)");
        efficiency = config.getFloat("Efficiency", tool + "_tier" + tierNumber, efficiency, 0, Float.MAX_VALUE, "Efficiency of the tool");
        damage = config.getFloat("Damage", tool + "_tier" + tierNumber, damage, 0, Float.MAX_VALUE, "Damage that the tool does when attacking enemies");
        enchant = config.getInt("Enchantablility", tool + "_tier" + tierNumber, enchant, 0, Integer.MAX_VALUE, "Enchantablity of the tool (0 - not enchantable)");

        Item.ToolMaterial material = EnumHelper.addToolMaterial(tool.toUpperCase() + tierNumber, miningLevel, 9000, efficiency, damage, enchant);
        return new DrillTier(material, maxEnergy, rechargeRate, energyPerBlock, rarity, canBreak, hasModes);
    }

    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event){
        if(event.modID.equals(Reference.MOD_ID)){
            loadConfig();
        }
    }
}
