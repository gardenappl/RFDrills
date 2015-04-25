package goldenapple.rfdrills.init;

import net.minecraft.item.Item;
import net.minecraftforge.common.util.EnumHelper;

public class ModToolMaterial {
    public static final Item.ToolMaterial TIER1 = EnumHelper.addToolMaterial("TIER1_DRILL", 2, 9001, 5.0F, 2.0F, 0);
    public static final Item.ToolMaterial TIER2 = EnumHelper.addToolMaterial("TIER2_DRILL", 3, 9001, 6.0F, 2.5F, 0);
    public static final Item.ToolMaterial TIER3 = EnumHelper.addToolMaterial("TIER3_DRILL", 3, 9001, 7.0F, 3.0F, 0);
    public static final Item.ToolMaterial TIER4 = EnumHelper.addToolMaterial("TIER4_DRILL", 4, 9001, 10.0F, 3.5F, 10);
}
