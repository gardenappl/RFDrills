package goldenapple.rfdrills.item.soulupgrade;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import java.util.List;

public abstract class AbstractSoulUpgrade {
    public abstract String getUnlocalizedName();

    public abstract byte getMaxLevel();

    public void addRecipeDescription(ItemStack itemStack, List<String> list){
        list.add("  " + EnumChatFormatting.DARK_AQUA + EnumChatFormatting.ITALIC + StatCollector.translateToLocal("rfdrills.upgrade." + getUnlocalizedName() + ".recipe" + Integer.toString(SoulUpgradeHelper.getUpgradeLevel(itemStack, this) + 1)));
    }

    public void addDescription(ItemStack itemStack, List<String> list){
        int i = 1;
        while(StatCollector.canTranslate("rfdrills.upgrade." + getUnlocalizedName() + ".desc" + SoulUpgradeHelper.getUpgradeLevel(itemStack, this) + "." + i)){
            list.add(StatCollector.translateToLocal("rfdrills.upgrade." + getUnlocalizedName() + ".desc" + SoulUpgradeHelper.getUpgradeLevel(itemStack, this) + "." + i));
            i++;
        }
    }

    public abstract boolean isRecipeValid(int level, ItemStack itemStack);

    public abstract int getLevelCost(int level);

    public int getItemCost(int level){
        return 1;
    }

    public boolean isUpgradeAvailable(ItemStack itemStack){
        return SoulUpgradeHelper.getUpgradeLevel(itemStack, this) < getMaxLevel(); //certain upgrades require others
    }
}
