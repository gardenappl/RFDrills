package goldenapple.omnidrills;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.EventBus;
import goldenapple.omnidrills.config.ConfigHandler;
import goldenapple.omnidrills.handler.TooltipHandler;
import goldenapple.omnidrills.init.ModItems;
import goldenapple.omnidrills.init.ModRecipes;
import goldenapple.omnidrills.reference.Reference;
import net.minecraftforge.common.MinecraftForge;

@Mod(modid = Reference.MOD_ID, version = Reference.VERSION, name = Reference.MOD_NAME, guiFactory = Reference.GUI_FACTORY)
public class OmniDrills {
    @Mod.Instance
    public static OmniDrills instance;

    public static ConfigHandler configHandler;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        //configHandler = new ConfigHandler(event.getSuggestedConfigurationFile());
        //FMLCommonHandler.instance().bus().register(configHandler); TODO: move this to another mod
        MinecraftForge.EVENT_BUS.register(new TooltipHandler());

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