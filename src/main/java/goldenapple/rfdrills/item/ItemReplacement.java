package goldenapple.rfdrills.item;

import cofh.lib.util.helpers.StringHelper;
import goldenapple.rfdrills.util.MiscUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import java.util.List;

public class ItemReplacement extends ItemMultiMetadata {
    private String modName;

    public ItemReplacement(String[] names, String defaultName, String modName) {
        super(names, defaultName);
        this.modName = modName;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean iHaveNoIdea) {
        if(MiscUtil.isShiftPressed()){
            list.add(StatCollector.translateToLocalFormatted("rfdrills.replacement.tooltip1", modName));
            list.add(StatCollector.translateToLocal("rfdrills.replacement.tooltip2"));
            list.add(StatCollector.translateToLocal("rfdrills.replacement.tooltip3"));
        }
        else
            list.add(StringHelper.shiftForDetails());
    }
}
