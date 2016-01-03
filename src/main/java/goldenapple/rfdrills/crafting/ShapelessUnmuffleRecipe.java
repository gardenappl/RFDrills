package goldenapple.rfdrills.crafting;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class ShapelessUnmuffleRecipe extends ShapelessOreRecipe {
    public ShapelessUnmuffleRecipe(ItemStack result) {
        super(result, result, Blocks.noteblock);
    }

    public ShapelessUnmuffleRecipe(Item result){
        this(new ItemStack(result));
    }

    public ShapelessUnmuffleRecipe(Block result){
        this(new ItemStack(result));
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inventory) {
        ItemStack result = getRecipeOutput();

        if(result.stackTagCompound == null)
            result.stackTagCompound = new NBTTagCompound();

        result.stackTagCompound.setBoolean("isSilent", false);
        return result;
    }

    @Override
    public boolean matches(InventoryCrafting inventory, World world) {
        for(int slot = 0; slot < inventory.getSizeInventory(); slot++){
            if(inventory.getStackInSlot(slot) != null) {
                ItemStack stack = inventory.getStackInSlot(slot);

                if(stack.getItem() == getRecipeOutput().getItem()){
                    if(stack.stackTagCompound != null) {
                        if (!stack.stackTagCompound.getBoolean("isSilent")) //If the tool is already not silent
                            return false;
                    }
                }
            }
        }
        return super.matches(inventory, world);
    }
}
