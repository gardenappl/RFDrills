package goldenapple.rfdrills.crafting;

import cofh.api.energy.IEnergyContainerItem;
import goldenapple.rfdrills.item.IEnergyTool;
import net.minecraft.block.Block;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class ShapelessUpgradeRecipe extends ShapelessOreRecipe {
    public ShapelessUpgradeRecipe(ItemStack result, Object... recipe) {
        super(result, recipe);
    }

    public ShapelessUpgradeRecipe(Item result, Object... recipe) {
        super(result, recipe);
    }

    public ShapelessUpgradeRecipe(Block result, Object... recipe) {
        super(result, recipe);
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inventory) {
        if(!(getRecipeOutput().getItem() instanceof IEnergyTool))
            return super.getCraftingResult(inventory);
        IEnergyTool energyTool = (IEnergyTool) getRecipeOutput().getItem();

        int energy = 0;

        for(int slot = 0; slot < inventory.getSizeInventory(); slot++){
            if(inventory.getStackInSlot(slot) != null) {
                ItemStack stack = inventory.getStackInSlot(slot);
                if (stack.getItem() instanceof IEnergyContainerItem)
                    energy += ((IEnergyContainerItem) stack.getItem()).getEnergyStored(stack);
            }
        }

        return energyTool.setEnergy(super.getCraftingResult(inventory), energy).copy();
    }
}
