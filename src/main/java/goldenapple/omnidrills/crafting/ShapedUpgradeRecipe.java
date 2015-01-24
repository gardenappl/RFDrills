package goldenapple.omnidrills.crafting;

import cofh.api.energy.IEnergyContainerItem;
import cofh.api.energy.ItemEnergyContainer;
import goldenapple.omnidrills.item.ItemDrill;
import net.minecraft.block.Block;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class ShapedUpgradeRecipe extends ShapedOreRecipe {
    public ShapedUpgradeRecipe(Item result, Object... recipe){
        this(new ItemStack(result), recipe);
    }

    public ShapedUpgradeRecipe(ItemStack result, Object... recipe) {
        super(result, recipe);
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inventoryCrafting) {
        int energy = 0;
        ItemDrill drillItem = (ItemDrill) getRecipeOutput().getItem();

        for(int slot = 0; slot < inventoryCrafting.getSizeInventory(); slot++){
            if(inventoryCrafting.getStackInSlot(slot) != null) {
                ItemStack stack = inventoryCrafting.getStackInSlot(slot);
                if (stack.getItem() instanceof IEnergyContainerItem) {
                    energy += ((IEnergyContainerItem) stack.getItem()).getEnergyStored(stack);
                }
            }
        }

        return drillItem.setEnergy(getRecipeOutput(), Math.min(energy, drillItem.getMaxEnergyStored(getRecipeOutput())));
    }
}
