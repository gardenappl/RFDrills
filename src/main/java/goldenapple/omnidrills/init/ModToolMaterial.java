package goldenapple.omnidrills.init;

import net.minecraft.item.Item;
import net.minecraftforge.common.util.EnumHelper;

public class ModToolMaterial {
    public static final Item.ToolMaterial LEADSTONE = EnumHelper.addToolMaterial("LEADSTONE_DRILL", 2, 0, 5.0F, 2.0F, 0);
    public static final Item.ToolMaterial HARDENED = EnumHelper.addToolMaterial("HARDENED_DRILL", 2, 0, 6.0F, 2.5F, 0);
    public static final Item.ToolMaterial REDSTONE = EnumHelper.addToolMaterial("REDSTONE_DRILL", 3, 0, 7.0F, 3.0F, 0);
    public static final Item.ToolMaterial RESONANT = EnumHelper.addToolMaterial("RESONANT_DRILL", 4, 0, 10.0F, 3.5F, 10);

    public static final Item.ToolMaterial BASIC = EnumHelper.addToolMaterial("BASIC_DRILL", 2, 0, 5.0F, 2.0F, 0);
}
