package goldenapple.rfdrills.crafting;

import cofh.api.energy.IEnergyContainerItem;
import cofh.lib.util.helpers.EnergyHelper;
import goldenapple.rfdrills.item.IEnergyTool;
import goldenapple.rfdrills.item.ItemDrill;
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
        IEnergyTool energyTool = (IEnergyTool) getRecipeOutput().getItem();

        for(int slot = 0; slot < inventoryCrafting.getSizeInventory(); slot++){
            if(inventoryCrafting.getStackInSlot(slot) != null) {
                ItemStack stack = inventoryCrafting.getStackInSlot(slot);
                if (stack.getItem() instanceof IEnergyContainerItem) {
                    energy += ((IEnergyContainerItem) stack.getItem()).getEnergyStored(stack);
                }
            }
        }

        return energyTool.setEnergy(getRecipeOutput(), energy);
    }
}
