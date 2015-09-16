package goldenapple.rfdrills.item.soulupgrade;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.HashMap;
import java.util.Map;

public class SoulUpgradeHelper {
    public static ItemStack applyUpgrade(ItemStack stack, AbstractSoulUpgrade upgrade, byte level){
        ItemStack upgradedStack = stack.copy();

        if(upgradedStack.stackTagCompound == null)
            upgradedStack.stackTagCompound = new NBTTagCompound();

        NBTTagCompound upgrades = upgradedStack.stackTagCompound.getCompoundTag("Upgrades");

        upgrades.setByte(upgrade.getUnlocalizedName(), level);
        upgradedStack.stackTagCompound.setTag("Upgrades", upgrades);
        return upgradedStack;
    }

    public static byte getUpgradeLevel(ItemStack stack, AbstractSoulUpgrade upgrade){
        if(stack.stackTagCompound == null)
            return 0;

        NBTTagCompound upgrades = stack.stackTagCompound.getCompoundTag("Upgrades");
        return upgrades.getByte(upgrade.getUnlocalizedName());
    }

    public static Map<AbstractSoulUpgrade, Byte> getUpgrades(ItemStack stack){
        HashMap<AbstractSoulUpgrade, Byte> map = new HashMap<AbstractSoulUpgrade, Byte>();

        for(AbstractSoulUpgrade upgrade : SoulUpgrades.registry){
            byte level = getUpgradeLevel(stack, upgrade);
            if(level != 0)
                map.put(upgrade, level);
        }

        return map;
    }
}
