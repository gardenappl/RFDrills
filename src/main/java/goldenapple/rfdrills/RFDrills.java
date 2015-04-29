package goldenapple.rfdrills;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import goldenapple.rfdrills.config.ConfigHandler;
import goldenapple.rfdrills.init.ModItems;
import goldenapple.rfdrills.init.ModRecipes;
import goldenapple.rfdrills.reference.Reference;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagByte;

@Mod(modid = Reference.MOD_ID, version = Reference.VERSION, name = Reference.MOD_NAME, guiFactory = Reference.GUI_FACTORY, dependencies = Reference.DEPENDECIES)
public class RFDrills {
    @Mod.Instance
    public static RFDrills instance;

    public static ConfigHandler configHandler;

    public static CreativeTabs OmniDrillsTab;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        configHandler = new ConfigHandler(event.getSuggestedConfigurationFile());
        FMLCommonHandler.instance().bus().register(configHandler);

        if(ConfigHandler.integrateTE || ConfigHandler.integrateEIO) {
            OmniDrillsTab = new CreativeTabs(Reference.MOD_ID) {
                @Override
                public ItemStack getIconItemStack() {
                    ItemStack itemStack = new ItemStack(getTabIconItem());
                    itemStack.setTagInfo("isCreativeTabIcon", new NBTTagByte((byte) 1));
                    return itemStack;
                }

                @Override
                public Item getTabIconItem() {
                    if (ConfigHandler.integrateTE) {
                        return ModItems.redstoneDrill;
                    } else {
                        return ModItems.basicDrill;
                    }
                }
            };
        }

        ModItems.init();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        ModRecipes.init();
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
    }
}
