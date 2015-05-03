package goldenapple.rfdrills.init;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import goldenapple.rfdrills.DrillTier;
import goldenapple.rfdrills.config.ConfigHandler;
import goldenapple.rfdrills.item.ItemChainsaw;
import goldenapple.rfdrills.item.ItemDrill;
import goldenapple.rfdrills.item.ItemMultiMetadata;
import goldenapple.rfdrills.reference.Names;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;

public class ModItems {
    //Thermal Expansion
    public static Item motorTE = new ItemMultiMetadata(Names.MOTORS_TE, Names.MOTOR_TE, new EnumRarity[]{EnumRarity.common, EnumRarity.common, EnumRarity.uncommon, EnumRarity.common, EnumRarity.rare, EnumRarity.common});

    public static Item leadstoneDrill = new ItemDrill(Names.LEADSTONE_DRILL, DrillTier.DRILL1);
    public static Item hardenedDrill = new ItemDrill(Names.HARDENED_DRILL, DrillTier.DRILL2);
    public static Item redstoneDrill = new ItemDrill(Names.REDSTONE_DRILL, DrillTier.DRILL3);
    public static Item resonantDrill = new ItemDrill(Names.RESONANT_DRILL, DrillTier.DRILL4);

    public static Item leadstoneChainsaw = new ItemChainsaw(Names.LEADSTONE_CHAINSAW, DrillTier.CHAINSAW1);
    public static Item hardenedChainsaw = new ItemChainsaw(Names.HARDENED_CHAINSAW, DrillTier.CHAINSAW2);
    public static Item redstoneChainsaw = new ItemChainsaw(Names.REDSTONE_CHAINSAW, DrillTier.CHAINSAW3);
    public static Item resonantChainsaw = new ItemChainsaw(Names.RESONANT_CHAINSAW, DrillTier.CHAINSAW4);

    //EnderIO
    public static Item motorEIO = new ItemMultiMetadata(Names.MOTORS_EIO, Names.MOTOR_EIO, new EnumRarity[]{EnumRarity.common, EnumRarity.common});
    public static Item basicDrill = new ItemDrill(Names.BASIC_DRILL, DrillTier.DRILL1);
    public static Item advancedDrill = new ItemDrill(Names.ADVANCED_DRILL, DrillTier.DRILL3);

    public static Item basicChainsaw = new ItemChainsaw(Names.BASIC_CHAINSAW, DrillTier.CHAINSAW1);
    public static Item advancedChainsaw = new ItemChainsaw(Names.ADVANCED_CHAINSAW, DrillTier.CHAINSAW3);


    public static void init(){
        if(Loader.isModLoaded("ThermalExpansion") && ConfigHandler.integrateTE) initTE();
        if(Loader.isModLoaded("EnderIO") && ConfigHandler.integrateEIO) initEIO();
    }

    private static void initTE(){
        GameRegistry.registerItem(motorTE, Names.MOTOR_TE);

        GameRegistry.registerItem(leadstoneDrill, Names.LEADSTONE_DRILL);
        GameRegistry.registerItem(hardenedDrill, Names.HARDENED_DRILL);
        GameRegistry.registerItem(redstoneDrill, Names.REDSTONE_DRILL);
        GameRegistry.registerItem(resonantDrill, Names.RESONANT_DRILL);

        GameRegistry.registerItem(leadstoneChainsaw, Names.LEADSTONE_CHAINSAW);
        GameRegistry.registerItem(hardenedChainsaw, Names.HARDENED_CHAINSAW);
        GameRegistry.registerItem(redstoneChainsaw, Names.REDSTONE_CHAINSAW);
        GameRegistry.registerItem(resonantChainsaw, Names.RESONANT_CHAINSAW);
    }

    private static void initEIO(){
        GameRegistry.registerItem(motorEIO, Names.MOTOR_EIO);

        GameRegistry.registerItem(basicDrill, Names.BASIC_DRILL);
        GameRegistry.registerItem(advancedDrill, Names.ADVANCED_DRILL);

        GameRegistry.registerItem(basicChainsaw, Names.BASIC_CHAINSAW);
        GameRegistry.registerItem(advancedChainsaw, Names.ADVANCED_CHAINSAW);
    }
}
