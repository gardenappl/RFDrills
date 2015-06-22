package goldenapple.rfdrills.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import java.util.List;

public class ItemSoulUpgrade extends ItemMultiMetadata {

    public ItemSoulUpgrade(String[] names, String defaultName) {
        super(names, defaultName);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean whatIsThisIDontEven) {
        list.add(StatCollector.translateToLocal("rfdrills.soul_upgrade.tooltip"));
    }
}
