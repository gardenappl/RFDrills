package goldenapple.rfdrills.compat.enderio;

import cpw.mods.fml.common.event.FMLInterModComms;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class EnderIOHelper { //shamelessly stolen from Simply Jetpacks https://github.com/Tonius/SimplyJetpacks/blob/master/src/main/java/tonius/simplyjetpacks/integration/EIORecipes.java
    public static void addSoulBinderRecipe(String recipeID, int energy, int xp, String soulTypes, ItemStack input, ItemStack output) {
        NBTTagCompound toSend = new NBTTagCompound();

        toSend.setString("recipeUID", recipeID);
        toSend.setInteger("requiredEnergyRF", energy);
        toSend.setInteger("requiredXP", xp);
        toSend.setString("entityTypes", soulTypes);
        writeItemStack(toSend, "inputStack", input);
        writeItemStack(toSend, "outputStack", output);

        FMLInterModComms.sendMessage("EnderIO", "recipe:soulbinder", toSend);
    }

    private static void writeItemStack(NBTTagCompound nbt, String tagName, ItemStack stack) {
        if (stack != null) {
            NBTTagCompound stackTag = new NBTTagCompound();
            stack.writeToNBT(stackTag);
            nbt.setTag(tagName, stackTag);
        }
    }
}
