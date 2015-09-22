package goldenapple.rfdrills.crafting;

import net.minecraft.block.Block;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class ShapelessToolSoundRecipe extends ShapelessOreRecipe {
    private boolean isSilent;

    public ShapelessToolSoundRecipe(ItemStack result, boolean isSilent, Object... recipe) {
        super(result, recipe);
        this.isSilent = isSilent;
    }

    public ShapelessToolSoundRecipe(Item result, boolean isSilent, Object... recipe){
        this(new ItemStack(result), isSilent, recipe);
    }

    public ShapelessToolSoundRecipe(Block result, boolean isSilent, Object... recipe){
        this(new ItemStack(result), isSilent, recipe);
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inventory) {
        ItemStack result = getRecipeOutput();

        for(int slot = 0; slot < inventory.getSizeInventory(); slot++){
            if(inventory.getStackInSlot(slot) != null && inventory.getStackInSlot(slot).getItem() == getRecipeOutput().getItem())
                result = inventory.getStackInSlot(slot).copy();
        }

        if(result.stackTagCompound == null)
            result.stackTagCompound = new NBTTagCompound();

        result.stackTagCompound.setBoolean("isSilent", isSilent);
        return result;
    }

    @Override
    public boolean matches(InventoryCrafting inventory, World world) {
        for(int slot = 0; slot < inventory.getSizeInventory(); slot++){
            if(inventory.getStackInSlot(slot) != null) {
                ItemStack stack = inventory.getStackInSlot(slot);

                if(stack.getItem() == getRecipeOutput().getItem()){
                    if(stack.stackTagCompound != null) {
                        if (stack.stackTagCompound.getBoolean("isSilent") == isSilent)
                            return false;
                    }
                }
            }
        }
        return super.matches(inventory, world);
    }
}
