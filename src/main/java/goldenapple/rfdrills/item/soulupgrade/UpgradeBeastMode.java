package goldenapple.rfdrills.item.soulupgrade;

import goldenapple.rfdrills.init.ModItems;
import goldenapple.rfdrills.reference.Metadata;
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
    public void addDescription(ItemStack itemStack, List<String> list) {
        if(SoulUpgradeHelper.getUpgradeLevel(itemStack, SoulUpgrades.upgradeFork) > 0){
            list.add(StatCollector.translateToLocal("rfdrills.upgrade.beast_mode.desc" + SoulUpgradeHelper.getUpgradeLevel(itemStack, this) + ".fork"));
        }else{
            list.add(StatCollector.translateToLocal("rfdrills.upgrade.beast_mode.desc" + SoulUpgradeHelper.getUpgradeLevel(itemStack, this)));
        }
    }

    @Override
    public boolean isRecipeValid(int level, ItemStack itemStack) {
        switch (level){
            case 1: return itemStack.getItem() == ModItems.componentEIO && itemStack.getItemDamage() == Metadata.DESTRUCTIVE_CRYSTAL;
            case 2: return itemStack.getItem() == ModItems.componentEIO && itemStack.getItemDamage() == Metadata.EARTHSHAKING_CRYSTAL;
            default: return false;
        }
    }

    @Override
    public int getLevelCost(int level) {
        return 10;
    }

    @Override
    public boolean isUpgradeAvailable(ItemStack itemStack) {
        switch (SoulUpgradeHelper.getUpgradeLevel(itemStack, this)){
            case 0: return SoulUpgradeHelper.getUpgradeLevel(itemStack, SoulUpgrades.upgradeEmpowered) >= 1;
            case 1: return SoulUpgradeHelper.getUpgradeLevel(itemStack, SoulUpgrades.upgradeEmpowered) >= 2;
            default: return super.isUpgradeAvailable(itemStack);
        }
    }
}
