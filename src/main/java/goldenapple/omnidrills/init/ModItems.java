package goldenapple.omnidrills.init;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import goldenapple.omnidrills.item.ItemDrill;
import goldenapple.omnidrills.item.ItemMultiMetadata;
import goldenapple.omnidrills.reference.Names;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import javax.lang.model.element.Name;

public class ModItems {
    //Thermal Expansion
    public static Item leadstoneDrill = new ItemDrill(Names.LEADSTONE_DRILL, ModToolMaterial.LEADSTONE, 10000, 80, 80, EnumRarity.common, true); //125 uses
    public static Item hardenedDrill = new ItemDrill(Names.HARDENED_DRILL, ModToolMaterial.HARDENED, 100000, 400, 400, EnumRarity.common, false); //250 uses
    public static Item redstoneDrill = new ItemDrill(Names.REDSTONE_DRILL, ModToolMaterial.REDSTONE, 1000000, 1000, 2000, EnumRarity.uncommon, false); //500 uses
    public static Item resonantDrill = new ItemDrill(Names.RESONANT_DRILL, ModToolMaterial.RESONANT, 7500000, 10000, 5000, EnumRarity.rare, false); //1500 uses

    //EnderIO
    public static Item basicDrill = new ItemDrill(Names.BASIC_DRILL, ModToolMaterial.BASIC, 10000, 80, 80, EnumRarity.common, true);

    public static Item motor = new ItemMultiMetadata(Names.MOTORS, Names.MOTOR);

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