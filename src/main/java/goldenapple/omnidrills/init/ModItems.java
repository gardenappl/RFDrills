package goldenapple.omnidrills.init;

import cpw.mods.fml.common.registry.GameRegistry;
import goldenapple.omnidrills.item.ItemDrill;
import goldenapple.omnidrills.reference.Names;
import net.minecraft.item.EnumRarity;

public class ModItems {
    public static ItemDrill leadstoneDrill = new ItemDrill(Names.LEADSTONE_DRILL, ModToolMaterial.LEADSTONE, 20000, 40, 10, EnumRarity.common);

    public static void init(){
        GameRegistry.registerItem(leadstoneDrill, Names.LEADSTONE_DRILL);
    }
}
