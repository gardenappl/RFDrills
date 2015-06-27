package goldenapple.rfdrills.compat.enderio;

import cpw.mods.fml.common.event.FMLInterModComms;
import goldenapple.rfdrills.reference.Reference;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class EnderIOHelper { //shamelessly stolen from Simply Jetpacks https://github.com/Tonius/SimplyJetpacks/blob/master/src/main/java/tonius/simplyjetpacks/integration/EIORecipes.java
    public static void addAlloySmelterRecipe(String name, int energy, ItemStack primaryInput, ItemStack secondaryInput, ItemStack tertiaryInput, ItemStack output) {
        StringBuilder toSend = new StringBuilder();

        toSend.append("<recipeGroup name=\"" + Reference.MOD_ID + "\">");
            toSend.append("<recipe name=\"" + name + "\" energyCost=\"" + energy + "\">");
                toSend.append("<input>");
                    writeItemStack(toSend, primaryInput);
                    writeItemStack(toSend, secondaryInput);
                    writeItemStack(toSend, tertiaryInput);
                toSend.append("</input>");
                toSend.append("<output>");
                    writeItemStack(toSend, output);
                toSend.append("</output>");
            toSend.append("</recipe>");
        toSend.append("</recipeGroup>");

        FMLInterModComms.sendMessage("EnderIO", "recipe:alloysmelter", toSend.toString());
    }

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

    private static void writeItemStack(StringBuilder sb, ItemStack stack) {
        if (stack != null) {
            String[] itemName = Item.itemRegistry.getNameForObject(stack.getItem()).split(":");
            sb.append("<itemStack modID=\"" + itemName[0] + "\" itemName=\"" + itemName[1] + "\" itemMeta=\"" + stack.getItemDamage() + "\" number=\"" + stack.stackSize + "\" />");
        }
    }
}
