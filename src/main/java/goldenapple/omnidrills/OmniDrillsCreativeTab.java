package goldenapple.omnidrills;

import cpw.mods.fml.common.Loader;
import goldenapple.omnidrills.init.ModItems;
import goldenapple.omnidrills.reference.Reference;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;

public class OmniDrillsCreativeTab {
    public static final CreativeTabs OmniDrillsTab = new CreativeTabs(Reference.MOD_ID) {
        @Override
        public Item getTabIconItem() {
            if(ModItems.basicDrill != null) {
                return ModItems.basicDrill;
            }else if(ModItems.leadstoneDrill != null){
                return ModItems.leadstoneDrill;
            }else{
                return Items.iron_pickaxe;
            }
        }
    };
}
