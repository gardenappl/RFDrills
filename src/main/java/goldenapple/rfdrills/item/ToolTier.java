package goldenapple.rfdrills.item;

import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;

public class ToolTier { //look into ConfigHandler for more info
    public static ToolTier DRILL1;
    public static ToolTier DRILL2;
    public static ToolTier DRILL3;
    public static ToolTier DRILL4;

    public static ToolTier HOE;
    public static ToolTier FLUX_CRUSHER;
    public static ToolTier SOUL_CRUSHER;

    public static ToolTier CHAINSAW1;
    public static ToolTier CHAINSAW2;
    public static ToolTier CHAINSAW3;
    public static ToolTier CHAINSAW4;

    public Item.ToolMaterial material;
    public int maxEnergy;
    public int rechargeRate;
    public int energyPerBlock;
    public EnumRarity rarity;
    public boolean canBreak;
    public boolean hasModes;
    public ToolTier(Item.ToolMaterial material, int maxEnergy, int rechargeRate, int energyPerBlock, EnumRarity rarity, boolean canBreak, boolean hasModes){
        this.material = material;
        this.maxEnergy = maxEnergy;
        this.rechargeRate = rechargeRate;
        this.energyPerBlock = energyPerBlock;
        this.rarity = rarity;
        this.canBreak = canBreak;
        this.hasModes = hasModes;
    }
}
