package goldenapple.rfdrills.config;

import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import goldenapple.rfdrills.item.ToolTier;
import goldenapple.rfdrills.reference.Reference;
import goldenapple.rfdrills.util.LogHelper;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.util.EnumHelper;

import java.io.File;

public class ConfigHandler {
    public static Configuration config;
    public static int configVersion = 2;

    public static boolean integrateTE;
    public static boolean integrateEIO;
    public static boolean integrateRArs;
    public static boolean shearsDefault;

    public ConfigHandler(File file){
        if(config == null) {
            config = new Configuration(file);
        }
        loadConfig();
    }

    private void loadConfig(){
        int currentConfigVersion = config.get(Configuration.CATEGORY_GENERAL, "Config Version", 1).setShowInGui(false).getInt();
        LogHelper.info("Current config version: %d; Required config version: %d", currentConfigVersion, configVersion);

        integrateTE = config.get(Configuration.CATEGORY_GENERAL, "Integrate TE", true).setLanguageKey("config.integrateTE").setRequiresMcRestart(true).getBoolean();
        integrateEIO = config.get(Configuration.CATEGORY_GENERAL, "Integrate EIO", true).setLanguageKey("config.integrateEIO").setRequiresMcRestart(true).getBoolean();
        integrateRArs = config.get(Configuration.CATEGORY_GENERAL, "Integrate Redstone Arsenal", true).setLanguageKey("config.integrateRArs").setRequiresMcRestart(true).getBoolean();
        shearsDefault = config.get(Configuration.CATEGORY_GENERAL, "Shears Default", true).setLanguageKey("config.shearsDefault").getBoolean();

        ToolTier.DRILL1 = getToolTierInfo("drill_tier1", 20000, 80, 80, EnumRarity.common, true, false, 2, 6.0F, 2.0F, 0);
        ToolTier.DRILL2 = getToolTierInfo("drill_tier2", 100000, 200, 400, EnumRarity.common, false, false, 3, 8.0F, 3.0F, 0);
        ToolTier.DRILL3 = getToolTierInfo("drill_tier3", 1000000, 800, 1500, EnumRarity.uncommon, false, true, 3, 10.0F, 4.0F, 10);
        ToolTier.DRILL4 = getToolTierInfo("drill_tier4", 5000000, 1500, 5000, EnumRarity.rare, false, true, 4, 12.0F, 5.0F, 15);

        ToolTier.FLUX_CRUSHER = getToolTierInfo("fluxcrusher", 25000000, 3000, 10000, EnumRarity.epic, false, true, 5, 15.0F, 8.0F, 20);
        ToolTier.SOUL_CRUSHER = getSoulCrusherTierInfo();
        ToolTier.HOE = getHoeTierInfo();

        ToolTier.CHAINSAW1 = getToolTierInfo("chainsaw_tier1", 20000, 80, 80, EnumRarity.common, true, false, 2, 6.0F, 3.0F, 0);
        ToolTier.CHAINSAW2 = getToolTierInfo("chainsaw_tier2", 100000, 200, 400, EnumRarity.common, false, false, 3, 7.0F, 4.0F, 0);
        ToolTier.CHAINSAW3 = getToolTierInfo("chainsaw_tier3", 1000000, 800, 1500, EnumRarity.uncommon, false, true, 3, 10.0F, 5.0F, 10);
        ToolTier.CHAINSAW4 = getToolTierInfo("chainsaw_tier4", 5000000, 1500, 5000, EnumRarity.rare, false, true, 4, 12.0F, 6.0F, 15);

        if(config.hasCategory("hoe_tier1")) {
            LogHelper.warn("Outdated config contains category \"hoe_tier1\"! Removing category...");
            config.removeCategory(config.getCategory("hoe_tier1"));
        }
        if(config.hasCategory("drill_tier5")){
            LogHelper.warn("Outdated config contains category \"drill_tier5\"! Removing category...");
            config.removeCategory(config.getCategory("drill_tier5"));
        }

        if(config.hasChanged()){
            config.get(Configuration.CATEGORY_GENERAL, "Config Version", 1).set(configVersion);
            config.save();
        }
    }

    private ToolTier getToolTierInfo(String category, int maxEnergy, int energyPerBlock, int rechargeRate, EnumRarity rarity, boolean canBreak, boolean hasModes, int miningLevel, float efficiency, float damage, int enchant){
        maxEnergy = config.get(category, "Maximum Energy", maxEnergy).setLanguageKey("config.maxEnergy").setRequiresMcRestart(true).getInt();
        energyPerBlock = config.get(category, "Energy Per Block", energyPerBlock).setLanguageKey("config.energyPerBlock").setRequiresMcRestart(true).getInt();
        rechargeRate = config.get(category, "Recharge Rate", rechargeRate).setLanguageKey("config.rechargeRate").setRequiresMcRestart(true).getInt();
        canBreak = config.get(category, "Can Break", canBreak).setLanguageKey("config.canBreak").setRequiresMcRestart(true).getBoolean();
        hasModes = config.get(category, "Has Modes", hasModes).setLanguageKey("config.hasModes").setRequiresMcRestart(true).getBoolean();
        miningLevel = config.get(category, "Mining Level", miningLevel).setLanguageKey("config.miningLevel").setRequiresMcRestart(true).getInt();
        efficiency = (float)config.get(category, "Efficiency", efficiency).setLanguageKey("config.efficiency").setRequiresMcRestart(true).getDouble();
        damage = (float)config.get(category, "Damage", damage).setLanguageKey("config.damage").setRequiresMcRestart(true).getDouble();
        enchant = config.get(category, "Enchantablility", enchant).setLanguageKey("config.enchant").setRequiresMcRestart(true).getInt();

        Item.ToolMaterial material = EnumHelper.addToolMaterial(category.toUpperCase(), miningLevel, 9000, efficiency, damage, enchant);
        return new ToolTier(material, maxEnergy, rechargeRate, energyPerBlock, rarity, canBreak, hasModes);
    }

    private ToolTier getSoulCrusherTierInfo(){
        int miningLevel = config.get("soulcrusher", "Mining Level", 5).setLanguageKey("config.miningLevel").setRequiresMcRestart(true).getInt();
        float efficiency = (float)config.get("soulcrusher", "Efficiency", 12.0F).setLanguageKey("config.efficiency").setRequiresMcRestart(true).getDouble();
        float damage = (float)config.get("soulcrusher", "Damage", 8.0F).setLanguageKey("config.damage").setRequiresMcRestart(true).getDouble();
        int enchant = config.get("soulcrusher", "Enchantability", 20).setLanguageKey("config.enchant").setRequiresMcRestart(true).getInt();
        boolean canBreak = config.get("soulcrusher", "Can Break", false).setLanguageKey("config.canBreak").setRequiresMcRestart(true).getBoolean();

        return new ToolTier(EnumHelper.addToolMaterial("SOUL_CRUSHER", miningLevel, 9000, efficiency, damage, enchant), 200000, 400, 400, EnumRarity.epic, canBreak, true);
    }

    private ToolTier getHoeTierInfo(){
        int maxEnergy = config.get("hoe", "Maximum Energy", 20000).setLanguageKey("config.maxEnergy").setRequiresMcRestart(true).getInt();
        int energyPerBlock = config.get("hoe", "Energy Per Block", 80).setLanguageKey("config.energyPerBlock").setRequiresMcRestart(true).getInt();
        int rechargeRate = config.get("hoe", "Recharge Rate", 80).setLanguageKey("config.rechargeRate").setRequiresMcRestart(true).getInt();
        boolean canBreak = config.get("hoe", "Can Break", true).setLanguageKey("config.canBreak").setRequiresMcRestart(true).getBoolean();
        int miningLevel = config.get("hoe", "Mining Level", 2).setLanguageKey("config.miningLevel").setRequiresMcRestart(true).getInt();
        float efficiency = (float)config.get("hoe", "Efficiency", 6.0F).setLanguageKey("config.efficiency").setRequiresMcRestart(true).getDouble();
        float damage = (float)config.get("hoe", "Damage", 2.0F).setLanguageKey("config.damage").setRequiresMcRestart(true).getDouble();
        int enchant = config.get("hoe", "Enchantablility", 0).setLanguageKey("config.enchant").setRequiresMcRestart(true).getInt();

        Item.ToolMaterial material = EnumHelper.addToolMaterial("HOE", miningLevel, 9000, efficiency, damage, enchant);
        return new ToolTier(material, maxEnergy, rechargeRate, energyPerBlock, EnumRarity.common, canBreak, false);
    }

    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event){
        if(event.modID.equals(Reference.MOD_ID)){
            loadConfig();
        }
    }
}
