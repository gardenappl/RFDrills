package goldenapple.rfdrills.item.soulupgrade;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import java.util.List;

public abstract class AbstractSoulUpgrade {
    public abstract String getUnlocalizedName();

    public abstract void addTooltip(ItemStack itemStack, EntityPlayer player, List<String> list);

    public abstract boolean isRecipeValid(ItemStack itemStack);

    public abstract boolean isUpgradeAvailable(ItemStack itemStack);
}
