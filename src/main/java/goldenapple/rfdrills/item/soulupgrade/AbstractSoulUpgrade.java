package goldenapple.rfdrills.item.soulupgrade;

import net.minecraft.item.ItemStack;

import java.util.List;

public abstract class AbstractSoulUpgrade {
    public abstract String getUnlocalizedName();

    public abstract byte getMaxLevel();

    public abstract void addRecipeDescription(ItemStack itemStack, List<String> list);

    public abstract void addDescription(ItemStack itemStack, List<String> list);

    public abstract boolean isRecipeValid(ItemStack itemStack);

    public abstract int getLevelCost(ItemStack itemStack);

    public boolean isUpgradeAvailable(ItemStack itemStack){
        return SoulUpgradeHelper.getUpgradeLevel(itemStack, this) < getMaxLevel(); //maybe make certain upgrades require others?
    }
}
