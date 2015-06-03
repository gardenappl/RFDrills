package goldenapple.rfdrills.item;

import cofh.api.energy.IEnergyContainerItem;
import goldenapple.rfdrills.DrillTier;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public interface IEnergyTool extends IEnergyContainerItem{//used for the ShapedUpgradeRecipe and Waila stuff
    public DrillTier getTier(ItemStack itemStack);

    public ItemStack setEnergy(ItemStack itemStack, int energy);

    public ItemStack drainEnergy(ItemStack itemStack, int energy);

    public int getEnergyPerUse(ItemStack itemStack, Block block, int meta);
}
