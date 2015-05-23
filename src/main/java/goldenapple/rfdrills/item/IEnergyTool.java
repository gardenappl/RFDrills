package goldenapple.rfdrills.item;

import cofh.api.energy.IEnergyContainerItem;
import net.minecraft.item.ItemStack;

public interface IEnergyTool extends IEnergyContainerItem{//used for the ShapedUpgradeRecipe

    public ItemStack setEnergy(ItemStack itemStack, int energy);

    public ItemStack drainEnergy(ItemStack itemStack, int energy);
}
