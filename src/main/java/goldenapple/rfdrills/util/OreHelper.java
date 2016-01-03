package goldenapple.rfdrills.util;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class OreHelper {
    public static boolean isItemThisOre(ItemStack stack, String ore){
        for (int oreID : OreDictionary.getOreIDs(stack))
            if (OreDictionary.getOreName(oreID).equals(ore))
                return true;
        return false;
    }

    public static void dumpAllOres(){
        for (String oreName : OreDictionary.getOreNames())
            if (!OreDictionary.getOres(oreName).isEmpty())
                LogHelper.info(oreName);
    }

    public static ItemStack findFirstOre(String ore){
        if (OreDictionary.getOres(ore).isEmpty())
            return null;
        else
            return OreDictionary.getOres(ore).get(0);
    }
}