package goldenapple.rfdrills.item.soulupgrade;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class UpgradeFork extends AbstractSoulUpgrade {
    @Override
    public String getUnlocalizedName() {
        return "fork";
    }

    @Override
    public byte getMaxLevel() {
        return 1;
    }

    @Override
    public boolean isRecipeValid(int level, ItemStack itemStack) {
        return itemStack.getItem() == Items.diamond_hoe;
    }

    @Override
    public int getLevelCost(int level) {
        return 10;
    }

    @Override
    public boolean isUpgradeAvailable(ItemStack itemStack) {
        return super.isUpgradeAvailable(itemStack) && SoulUpgradeHelper.getUpgradeLevel(itemStack, SoulUpgrades.upgradeEmpowered) >= 1;
    }
}
