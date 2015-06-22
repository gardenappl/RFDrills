package goldenapple.rfdrills.item.soulupgrade;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class SoulUpgradeHelper {
    public static ItemStack applyUpgrade(ItemStack itemStack, AbstractSoulUpgrade upgrade, byte level){
        if(itemStack.stackTagCompound == null){
            itemStack.stackTagCompound = new NBTTagCompound();
        }

        NBTTagCompound upgrades = itemStack.stackTagCompound.getCompoundTag("Upgrades");

        upgrades.setByte(upgrade.getUnlocalizedName(), level);
        itemStack.stackTagCompound.setTag("Upgrades", upgrades);
        return itemStack;
    }

    public static byte getUpgradeLevel(ItemStack itemStack, AbstractSoulUpgrade upgrade){
        if(itemStack.stackTagCompound == null) return 0;

        NBTTagCompound upgrades = itemStack.stackTagCompound.getCompoundTag("Upgrades");
        return upgrades.getByte(upgrade.getUnlocalizedName());
    }
}
