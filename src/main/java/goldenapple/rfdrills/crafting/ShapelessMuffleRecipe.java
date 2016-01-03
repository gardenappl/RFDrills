package goldenapple.rfdrills.crafting;

import cpw.mods.fml.common.registry.GameRegistry;
import goldenapple.rfdrills.RFDrills;
import net.minecraft.block.Block;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class ShapelessMuffleRecipe extends ShapelessOreRecipe {
    public ShapelessMuffleRecipe(ItemStack result) {
        super(result, result, RFDrills.isXULoaded ? new ItemStack(GameRegistry.findItem("ExtraUtilities", "sound_muffler")) : "blockCloth");
    }

    public ShapelessMuffleRecipe(Item result){
        this(new ItemStack(result));
    }

    public ShapelessMuffleRecipe(Block result){
        this(new ItemStack(result));
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

        result.stackTagCompound.setBoolean("isSilent", true);
        return result;
    }

    @Override
    public boolean matches(InventoryCrafting inventory, World world) {
        for(int slot = 0; slot < inventory.getSizeInventory(); slot++){
            if(inventory.getStackInSlot(slot) != null) {
                ItemStack stack = inventory.getStackInSlot(slot);

                if(stack.getItem() == getRecipeOutput().getItem()){
                    if(stack.stackTagCompound != null) {
                        if (stack.stackTagCompound.getBoolean("isSilent")) //If the tool is already silent
                            return false;
                    }
                }
            }
        }
        return super.matches(inventory, world);
    }
}
