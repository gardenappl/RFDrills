package goldenapple.rfdrills.item;

import cofh.api.energy.IEnergyContainerItem;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public interface IEnergyTool extends IEnergyContainerItem{
    public ToolTier getTier(ItemStack stack);

    public ItemStack setEnergy(ItemStack stack, int energy);

    public ItemStack drainEnergy(ItemStack stack, int energy);

    public int getEnergyPerUse(ItemStack stack, Block block, int meta);

    public String writeModeInfo(ItemStack stack);

    public EnumModIntegration getModType();
}
