package goldenapple.rfdrills.util;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class OreHelper {
    public static boolean isItemThisOre(ItemStack stack, String ore){
        for (int oreID : OreDictionary.getOreIDs(stack)) {
            if (OreDictionary.getOreName(oreID).equals(ore)) {
                return true;
            }
        }
        return false;
    }

    public static void dumpAllOres(){
        for (String l : OreDictionary.getOreNames()){
            LogHelper.info(l);
        }
    }
}