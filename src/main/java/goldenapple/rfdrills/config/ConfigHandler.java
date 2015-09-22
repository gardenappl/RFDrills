package goldenapple.rfdrills.config;

import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import goldenapple.rfdrills.item.ToolTier;
import goldenapple.rfdrills.reference.Reference;
import goldenapple.rfdrills.util.LogHelper;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.common.util.EnumHelper;

import java.io.File;
import java.util.Map;

import static net.minecraftforge.common.config.Configuration.CATEGORY_GENERAL;

public class ConfigHandler {
    public static Configuration config;

    public static boolean integrateTE;
    public static boolean integrateEIO;
    public static boolean integrateRArs;

    public static boolean shearsDefault;
    public static boolean modeShiftClickTE;
    public static boolean modeShiftClickEIO;
    public static int animationType;

    public ConfigHandler(File file){
        if(config == null)
            config = new Configuration(file);
        loadConfig();
    }

    private void loadConfig(){
        if(config.hasCategory("hoe_tier1"))
            renameCategory("hoe_tier1", "hoe");

        if(config.hasCategory("drill_tier5"))
            renameCategory("drill_tier5", "fluxcrusher");

        integrateTE = config.get(CATEGORY_GENERAL, "Integrate TE", true).setLanguageKey("config.integrateTE").setRequiresMcRestart(true).getBoolean();
        integrateEIO = config.get(CATEGORY_GENERAL, "Integrate EIO", true).setLanguageKey("config.integrateEIO").setRequiresMcRestart(true).getBoolean();
        integrateRArs = config.get(CATEGORY_GENERAL, "Integrate Redstone Arsenal", true).setLanguageKey("config.integrateRArs").setRequiresMcRestart(true).getBoolean();

        shearsDefault = config.get(CATEGORY_GENERAL, "Shears Default", true).setLanguageKey("config.shearsDefault").getBoolean();
        modeShiftClickTE = config.get(CATEGORY_GENERAL, "TE Mode Switch Shift+Click", false).setLanguageKey("config.modeShiftClickTE").getBoolean();
        modeShiftClickEIO = config.get(CATEGORY_GENERAL, "EIO Mode Switch Shift+Click", true).setLanguageKey("config.modeShiftClickEIO").getBoolean();
        animationType = config.get(CATEGORY_GENERAL, "Mining Animation Type", 2).setLanguageKey("config.animationType").setMinValue(0).setMaxValue(2).setRequiresMcRestart(true).getInt();

        ToolTier.DRILL1 = getToolTierInfo("drill_tier1", 20000, 80, 80, EnumRarity.common, true, false, 2, 6.0F, 2.0F, 0);
        ToolTier.DRILL2 = getToolTierInfo("drill_tier2", 100000, 200, 400, EnumRarity.common, false, false, 3, 8.0F, 3.0F, 0);
        ToolTier.DRILL3 = getToolTierInfo("drill_tier3", 1000000, 800, 1500, EnumRarity.uncommon, false, true, 3, 10.0F, 4.0F, 10);
        ToolTier.DRILL4 = getToolTierInfo("drill_tier4", 5000000, 1500, 5000, EnumRarity.rare, false, true, 4, 12.0F, 5.0F, 15);

        ToolTier.FLUX_CRUSHER = getToolTierInfo("fluxcrusher", 25000000, 5000, 10000, EnumRarity.epic, false, true, 5, 15.0F, 8.0F, 20);
        ToolTier.SOUL_CRUSHER = getSoulCrusherTierInfo();
        ToolTier.HOE = getHoeTierInfo();

        ToolTier.CHAINSAW1 = getToolTierInfo("chainsaw_tier1", 20000, 80, 80, EnumRarity.common, true, false, 2, 6.0F, 3.0F, 0);
        ToolTier.CHAINSAW2 = getToolTierInfo("chainsaw_tier2", 100000, 200, 400, EnumRarity.common, false, false, 3, 7.0F, 4.0F, 0);
        ToolTier.CHAINSAW3 = getToolTierInfo("chainsaw_tier3", 1000000, 800, 1500, EnumRarity.uncommon, false, true, 3, 10.0F, 5.0F, 10);
        ToolTier.CHAINSAW4 = getToolTierInfo("chainsaw_tier4", 5000000, 1500, 5000, EnumRarity.rare, false, true, 4, 12.0F, 6.0F, 15);

        for(String categoryName : config.getCategoryNames()){
            for(Map.Entry<String, Property> entry : config.getCategory(categoryName).entrySet()){
                if(entry.getKey().equals("Enchantablility"))     //yeah that was a fun typo
                    renameProperty(categoryName, entry.getKey(), "Enchantability");
                else if(entry.getKey().equals("Enchantibility")) //that was fun as well
                    renameProperty(categoryName, entry.getKey(), "Enchantability");
            }
        }
        if(config.getCategory(CATEGORY_GENERAL).containsKey("TE Mode Switch Sound"))
            removeProperty(CATEGORY_GENERAL, "TE Mode Switch Sound");
        if(config.getCategory(CATEGORY_GENERAL).containsKey("EIO Mode Switch Sound"))
            removeProperty(CATEGORY_GENERAL, "EIO Mode Switch Sound");


        if(config.hasChanged())
            config.save();
    }

    private static ToolTier getToolTierInfo(String category, int maxEnergy, int energyPerBlock, int rechargeRate, EnumRarity rarity, boolean canBreak, boolean hasModes, int miningLevel, float efficiency, float damage, int enchant){
        maxEnergy = config.get(category, "Maximum Energy", maxEnergy).setLanguageKey("config.maxEnergy").setRequiresMcRestart(true).getInt();
        energyPerBlock = config.get(category, "Energy Per Block", energyPerBlock).setLanguageKey("config.energyPerBlock").setRequiresMcRestart(true).getInt();
        rechargeRate = config.get(category, "Recharge Rate", rechargeRate).setLanguageKey("config.rechargeRate").setRequiresMcRestart(true).getInt();
        canBreak = config.get(category, "Can Break", canBreak).setLanguageKey("config.canBreak").setRequiresMcRestart(true).getBoolean();
        hasModes = config.get(category, "Has Modes", hasModes).setLanguageKey("config.hasModes").setRequiresMcRestart(true).getBoolean();
        miningLevel = config.get(category, "Mining Level", miningLevel).setLanguageKey("config.miningLevel").setRequiresMcRestart(true).getInt();
        efficiency = (float)config.get(category, "Efficiency", efficiency).setLanguageKey("config.efficiency").setRequiresMcRestart(true).getDouble();
        damage = (float)config.get(category, "Damage", damage).setLanguageKey("config.damage").setRequiresMcRestart(true).getDouble();
        enchant = config.get(category, "Enchantability", enchant).setLanguageKey("config.enchant").setRequiresMcRestart(true).getInt();

        Item.ToolMaterial material = EnumHelper.addToolMaterial("rfdrills:" + category.toUpperCase(), miningLevel, 9000, efficiency, damage, enchant);
        return new ToolTier(material, maxEnergy, rechargeRate, energyPerBlock, rarity, canBreak, hasModes);
    }

    private static ToolTier getSoulCrusherTierInfo(){
        int miningLevel = config.get("soulcrusher", "Mining Level", 5).setLanguageKey("config.miningLevel").setRequiresMcRestart(true).getInt();
        float efficiency = (float)config.get("soulcrusher", "Efficiency", 12.0F).setLanguageKey("config.efficiency").setRequiresMcRestart(true).getDouble();
        float damage = (float)config.get("soulcrusher", "Damage", 8.0F).setLanguageKey("config.damage").setRequiresMcRestart(true).getDouble();
        int enchant = config.get("soulcrusher", "Enchantability", 20).setLanguageKey("config.enchant").setRequiresMcRestart(true).getInt();
        boolean canBreak = config.get("soulcrusher", "Can Break", false).setLanguageKey("config.canBreak").setRequiresMcRestart(true).getBoolean();

        return new ToolTier(EnumHelper.addToolMaterial("rfdrills:SOUL_CRUSHER", miningLevel, 9000, efficiency, damage, enchant), 200000, 400, 400, EnumRarity.epic, canBreak, true);
    }

    private static ToolTier getHoeTierInfo(){
        int maxEnergy = config.get("hoe", "Maximum Energy", 20000).setLanguageKey("config.maxEnergy").setRequiresMcRestart(true).getInt();
        int energyPerBlock = config.get("hoe", "Energy Per Block", 80).setLanguageKey("config.energyPerBlock").setRequiresMcRestart(true).getInt();
        int rechargeRate = config.get("hoe", "Recharge Rate", 80).setLanguageKey("config.rechargeRate").setRequiresMcRestart(true).getInt();
        boolean canBreak = config.get("hoe", "Can Break", true).setLanguageKey("config.canBreak").setRequiresMcRestart(true).getBoolean();
        int miningLevel = config.get("hoe", "Mining Level", 2).setLanguageKey("config.miningLevel").setRequiresMcRestart(true).getInt();
        float efficiency = (float)config.get("hoe", "Efficiency", 6.0F).setLanguageKey("config.efficiency").setRequiresMcRestart(true).getDouble();
        float damage = (float)config.get("hoe", "Damage", 2.0F).setLanguageKey("config.damage").setRequiresMcRestart(true).getDouble();
        int enchant = config.get("hoe", "Enchantability", 0).setLanguageKey("config.enchant").setRequiresMcRestart(true).getInt();

        Item.ToolMaterial material = EnumHelper.addToolMaterial("rfdrills:HOE", miningLevel, 9000, efficiency, damage, enchant);
        return new ToolTier(material, maxEnergy, rechargeRate, energyPerBlock, EnumRarity.common, canBreak, false);
    }


    private static void removeProperty(String category, String name){
        if(config.hasCategory(category) && config.getCategory(category).containsKey(name)){
            LogHelper.info("Outdated config contains property %s in category %s! Removing..", name, category);
            config.getCategory(category).remove(name);
        }
    }

    private static void renameProperty(String category, String nameOld, String nameNew){
        LogHelper.info("Outdated config contains property %s in category %s! Renaming to %s...", nameOld, category, nameNew);
        config.renameProperty(category, nameOld, nameNew);
    }

    private static void renameCategory(String categoryOld, String categoryNew){
        if(config.hasCategory(categoryOld)) {
            LogHelper.info("Outdated config contains category %s! Renaming to %s...", categoryOld, categoryNew);

            for (Map.Entry<String, Property> entry : config.getCategory(categoryOld).entrySet())
                config.getCategory(categoryNew).put(entry.getKey(), entry.getValue());
            config.removeCategory(config.getCategory(categoryOld));
        }
    }


    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event){
        if(event.modID.equals(Reference.MOD_ID))
            loadConfig();
    }
}
