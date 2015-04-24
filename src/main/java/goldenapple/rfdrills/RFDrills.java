package goldenapple.rfdrills;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import goldenapple.rfdrills.config.ConfigHandler;
import goldenapple.rfdrills.handler.DrillMiningHandler;
import goldenapple.rfdrills.init.ModItems;
import goldenapple.rfdrills.init.ModRecipes;
import goldenapple.rfdrills.reference.Reference;
import net.minecraftforge.common.MinecraftForge;

@Mod(modid = Reference.MOD_ID, version = Reference.VERSION, name = Reference.MOD_NAME)
public class RFDrills {
    @Mod.Instance
    public static RFDrills instance;

    public static ConfigHandler configHandler;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        /* configHandler = new ConfigHandler(event.getSuggestedConfigurationFile());
        FMLCommonHandler.instance().bus().register(configHandler); */ //I don't want energy unit conversion
        MinecraftForge.EVENT_BUS.register(new DrillMiningHandler());

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
