package goldenapple.rfdrills.config;

import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraftforge.common.util.EnumHelper;

public class DrillTier { //It's not an enumeration because of ConfigHandler
    public static DrillTier TIER_1;
    public static DrillTier TIER_2;
    public static DrillTier TIER_3;
    public static DrillTier TIER_4;

    public static Item.ToolMaterial TIER1_MATERIAL;
    public static Item.ToolMaterial TIER2_MATERIAL;
    public static Item.ToolMaterial TIER3_MATERIAL;
    public static Item.ToolMaterial TIER4_MATERIAL;

    public Item.ToolMaterial material;
    public int maxEnergy;
    public int rechargeRate;
    public int energyPerBlock;
    public EnumRarity rarity;
    public boolean canBreak;
    public DrillTier(Item.ToolMaterial material, int maxEnergy, int rechargeRate, int energyPerBlock, EnumRarity rarity, boolean canBreak){
        this.material = material;
        this.maxEnergy = maxEnergy;
        this.rechargeRate = rechargeRate;
        this.energyPerBlock = energyPerBlock;
        this.rarity = rarity;
        this.canBreak = canBreak;
    }

    public static void init(){/*
        TIER1_MATERIAL = EnumHelper.addToolMaterial("TIER1_DRILL", 2, 9001, 6.0F, 2.0F, 0);
        TIER2_MATERIAL = EnumHelper.addToolMaterial("TIER2_DRILL", 3, 9001, 7.0F, 3.0F, 0);
        TIER3_MATERIAL = EnumHelper.addToolMaterial("TIER3_DRILL", 3, 9001, 8.0F, 4.0F, 0);
        TIER4_MATERIAL = EnumHelper.addToolMaterial("TIER4_DRILL", 4, 9001, 10.0F, 5.0F, 10);

        TIER_1 = new DrillTier(20000, 80, 80, EnumRarity.common, true); //250 uses
        TIER_2 = new DrillTier(100000, 200, 400, EnumRarity.common, false); //500 uses
        TIER_3 = new DrillTier(1000000, 1000, 1500, EnumRarity.uncommon, false); //1000 uses
        TIER_4 = new DrillTier(4000000, 2000, 5000, EnumRarity.rare, false); //2000 uses */
    }
}
