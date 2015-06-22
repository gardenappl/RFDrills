package goldenapple.rfdrills.item.soulupgrade;

import goldenapple.rfdrills.util.MiscUtil;
import goldenapple.rfdrills.util.StringHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import java.util.List;

public class UpgradeEmpowered extends AbstractSoulUpgrade { //TODO
    @Override
    public String getUnlocalizedName() {
        return "empowered";
    }

    @Override
    public byte getMaxLevel() {
        return 3;
    }

    @Override
    public void addRecipeDescription(ItemStack itemStack, List<String> list) {

    }

    @Override
    public void addDescription(ItemStack itemStack, List<String> list) {
        list.add(EnumChatFormatting.BLUE.toString() + StringHelper.writeUpgradeInfo(itemStack, this));
        if(MiscUtil.isShiftPressed()){
            list.add(StatCollector.translateToLocal("rfdrills.upgrade.empowered.desc" + SoulUpgradeHelper.getUpgradeLevel(itemStack, this)));
        }
    }

    @Override
    public boolean isRecipeValid(ItemStack itemStack) {
        return false;
    }

    @Override
    public int getLevelCost(ItemStack itemStack) {
        return 10;
    }
}
