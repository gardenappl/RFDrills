package goldenapple.rfdrills.util;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class OreHelper {
    public static boolean isItemThisOre(ItemStack item, String ore){
        for (int o : OreDictionary.getOreIDs(item)) {
            if (OreDictionary.getOreName(o).equals(ore)) {
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