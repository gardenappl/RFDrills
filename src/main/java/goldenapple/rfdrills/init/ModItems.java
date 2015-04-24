package goldenapple.rfdrills.init;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import goldenapple.rfdrills.item.ItemDrill;
import goldenapple.rfdrills.item.ItemMultiMetadata;
import goldenapple.rfdrills.reference.Names;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;

public class ModItems {
    //Thermal Expansion
    public static Item leadstoneDrill = new ItemDrill(Names.LEADSTONE_DRILL, ModToolMaterial.LEADSTONE, 10000, 80, 80, EnumRarity.common, true); //125 uses
    public static Item hardenedDrill = new ItemDrill(Names.HARDENED_DRILL, ModToolMaterial.HARDENED, 100000, 400, 400, EnumRarity.common, false); //250 uses
    public static Item redstoneDrill = new ItemDrill(Names.REDSTONE_DRILL, ModToolMaterial.REDSTONE, 1000000, 1000, 1500, EnumRarity.uncommon, false); //500 uses
    public static Item resonantDrill = new ItemDrill(Names.RESONANT_DRILL, ModToolMaterial.RESONANT, 7500000, 10000, 5000, EnumRarity.rare, false); //1500 uses

    //EnderIO
    public static Item basicDrill = new ItemDrill(Names.BASIC_DRILL, ModToolMaterial.BASIC, 10000, 80, 80, EnumRarity.common, true);

    public static Item motor = new ItemMultiMetadata(Names.MOTORS, Names.MOTOR, new EnumRarity[]{EnumRarity.common, EnumRarity.common, EnumRarity.uncommon, EnumRarity.common, EnumRarity.uncommon, EnumRarity.rare});

    public static void init(){
        if(Loader.isModLoaded("ThermalExpansion")) initTE();
        if(Loader.isModLoaded("EnderIO")) initEIO();
        GameRegistry.registerItem(motor, Names.MOTOR);
    }

    private static void initTE(){
        GameRegistry.registerItem(leadstoneDrill, Names.LEADSTONE_DRILL);
        GameRegistry.registerItem(hardenedDrill, Names.HARDENED_DRILL);
        GameRegistry.registerItem(redstoneDrill, Names.REDSTONE_DRILL);
        GameRegistry.registerItem(resonantDrill, Names.RESONANT_DRILL);
    }

    private static void initEIO(){
        GameRegistry.registerItem(basicDrill, Names.BASIC_DRILL);
    }
}
