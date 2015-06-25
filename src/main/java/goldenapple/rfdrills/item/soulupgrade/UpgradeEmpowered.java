package goldenapple.rfdrills.item.soulupgrade;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import java.util.List;

public class UpgradeEmpowered extends AbstractSoulUpgrade {
    @Override
    public String getUnlocalizedName() {
        return "empowered";
    }

    @Override
    public byte getMaxLevel() {
        return 2;
    }

    @Override
    public void addDescription(ItemStack itemStack, List<String> list) {
        list.add(StatCollector.translateToLocal("rfdrills.upgrade.empowered.desc1"));
        list.add(StatCollector.translateToLocal("rfdrills.upgrade.empowered.desc2"));
    }

    @Override
    public boolean isRecipeValid(int level, ItemStack itemStack) {
        return itemStack.getItem() == GameRegistry.findItem("EnderIO", "itemBasicCapacitor") && itemStack.getItemDamage() + 1 == level;
    }

    @Override
    public int getLevelCost(int level) {
        return 10;
    }
}
