package goldenapple.rfdrills;

import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;

public class DrillTier {
    public static DrillTier DRILL1;
    public static DrillTier DRILL2;
    public static DrillTier DRILL3;
    public static DrillTier DRILL4;

    public static DrillTier CHAINSAW1;
    public static DrillTier CHAINSAW2;
    public static DrillTier CHAINSAW3;
    public static DrillTier CHAINSAW4;

    public static Item.ToolMaterial DRILL1_MATERIAL;
    public static Item.ToolMaterial DRILL2_MATERIAL;
    public static Item.ToolMaterial DRILL3_MATERIAL;
    public static Item.ToolMaterial DRILL4_MATERIAL;

    public static Item.ToolMaterial CHAINSAW1_MATERIAL;
    public static Item.ToolMaterial CHAINSAW2_MATERIAL;
    public static Item.ToolMaterial CHAINSAW3_MATERIAL;
    public static Item.ToolMaterial CHAINSAW4_MATERIAL;

    public Item.ToolMaterial material;
    public int maxEnergy;
    public int rechargeRate;
    public int energyPerBlock;
    public EnumRarity rarity;
    public boolean canBreak;
    public boolean hasModes;
    public DrillTier(Item.ToolMaterial material, int maxEnergy, int rechargeRate, int energyPerBlock, EnumRarity rarity, boolean canBreak, boolean hasModes){
        this.material = material;
        this.maxEnergy = maxEnergy;
        this.rechargeRate = rechargeRate;
        this.energyPerBlock = energyPerBlock;
        this.rarity = rarity;
        this.canBreak = canBreak;
        this.hasModes = hasModes;
    }
}
