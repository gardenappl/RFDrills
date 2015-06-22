package goldenapple.rfdrills.item.soulupgrade;

import goldenapple.rfdrills.init.ModItems;
import goldenapple.rfdrills.reference.LibMetadata;
import goldenapple.rfdrills.util.MiscUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import java.util.List;

public class UpgradeBeastMode extends AbstractSoulUpgrade {
    @Override
    public String getUnlocalizedName() {
        return "beast_mode";
    }

    @Override
    public byte getMaxLevel() {
        return 2;
    }

    @Override
    public void addRecipeDescription(ItemStack itemStack, List<String> list) {

    }

    @Override
    public void addDescription(ItemStack itemStack, List<String> list) {
        if(MiscUtil.isShiftPressed()){
            list.add(StatCollector.translateToLocal("rfdrills.upgrade.beast_mode." + SoulUpgradeHelper.getUpgradeLevel(itemStack, this)));
        }
    }

    @Override
    public boolean isRecipeValid(ItemStack itemStack) {
        switch (SoulUpgradeHelper.getUpgradeLevel(itemStack, this)){
            case 0: return itemStack.getItem() == ModItems.componentEIO && itemStack.getItemDamage() == LibMetadata.DESTRUCTIVE_CRYSTAL;
            case 1: return itemStack.getItem() == ModItems.componentEIO && itemStack.getItemDamage() == LibMetadata.EARTHSHAKING_CRYSTAL;
            default: return false;
        }
    }

    @Override
    public int getLevelCost(ItemStack itemStack) {
        return 10;
    }
}
