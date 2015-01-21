package goldenapple.omnidrills.init;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.oredict.ShapedOreRecipe;
import sun.awt.SunToolkit;

public class ModRecipes {

    public static void init(){
        if(Loader.isModLoaded("ThermalExpansion")) initTE();
        if(Loader.isModLoaded("EnderIO")) initEIO();
    }

    private static void initTE(){
        ItemStack pneumaticServo = new ItemStack(GameRegistry.findItem("ThermalExpansion", "material"), 1, 0);
        ItemStack receptionCoil = new ItemStack(GameRegistry.findItem("ThermalExpansion", "material"), 1, 1);
        ItemStack dustCryotheum = new ItemStack(GameRegistry.findItem("ThermalFoundation", "material"), 1, 513);
        ItemStack dustBlizz = new ItemStack(GameRegistry.findItem("ThermalFoundation", "material"), 1, 1025);
        ItemStack capacitorLeadstone = new ItemStack(GameRegistry.findItem("ThermalExpansion", "capacitor"), 1, 2);
        ItemStack capacitorHardened = new ItemStack(GameRegistry.findItem("ThermalExpansion", "capacitor"), 1, 3);
        ItemStack capacitorRedstone = new ItemStack(GameRegistry.findItem("ThermalExpansion", "capacitor"), 1, 4);
        ItemStack capacitorResonant = new ItemStack(GameRegistry.findItem("ThermalExpansion", "capacitor"), 1, 5);

        //Leadstone motor
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.motor, 1, 0),
                "iDi",
                "GPG",
                "iCi", 'i', "ingotLead", 'D', "dustRedstone", 'G', "gearTin", 'P', pneumaticServo, 'C', receptionCoil));

        //Hardened motor
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.motor, 1, 1),
                "iDi",
                "GPG",
                "iCi", 'i', "ingotInvar", 'D', "dustGlowstone", 'G', "gearElectrum", 'P', pneumaticServo, 'C', receptionCoil));

        //Reinforced motor
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.motor, 1, 2),
                "iDi",
                "GPG",
                "iCi", 'i', "blockGlassHardened", 'D', dustBlizz, 'G', "gearSignalum", 'P', pneumaticServo, 'C', receptionCoil));

        //Resonant motor
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.motor, 1, 3),
                "iDi",
                "GPG",
                "iCi", 'i', "ingotSilver", 'D', dustCryotheum, 'G', "gearEnderium", 'P', pneumaticServo, 'C', receptionCoil));


        //Leadstone drill
        GameRegistry.addRecipe(new ShapedOreRecipe(ModItems.leadstoneDrill, //TODO: Special recipe handler
                " i ",
                "iMi",
                "ICI", 'I', "ingotLead", 'i', "ingotBronze", 'C', capacitorLeadstone, 'M', new ItemStack(ModItems.motor, 1, 0)));

        //Hardened drill
        GameRegistry.addRecipe(new ShapedOreRecipe(ModItems.hardenedDrill,
                " D ",
                "iMi",
                "ICI", 'I', "ingotInvar", 'i', "ingotIron", 'C', capacitorHardened, 'M', new ItemStack(ModItems.motor, 1, 1), 'D', new ItemStack(ModItems.leadstoneDrill)));

        //Redstone drill
        GameRegistry.addRecipe(new ShapedOreRecipe(ModItems.redstoneDrill,
                " D ",
                "iMi",
                "ICI", 'I', "ingotElectrum", 'i', "ingotInvar", 'C', capacitorRedstone, 'M', new ItemStack(ModItems.motor, 1, 2), 'D', new ItemStack(ModItems.hardenedDrill)));

        //Resonant drill
        GameRegistry.addRecipe(new ShapedOreRecipe(ModItems.resonantDrill,
                " D ",
                "iMi",
                "ICI", 'I', "ingotEnderium", 'i', "ingotSilver", 'C', capacitorResonant, 'M', new ItemStack(ModItems.motor, 1, 3), 'D', new ItemStack(ModItems.redstoneDrill)));
    }

    private static void initEIO(){
        ItemStack machineChassis = new ItemStack(GameRegistry.findItem("EnderIO", "itemMachinePart"), 1, 0);
        ItemStack capacitorBasic = new ItemStack(GameRegistry.findItem("EnderIO", "itemBasicCapacitor"), 1, 0);

        //Basic motor
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.motor, 1, 4),
                "iCi",
                "GFG",
                "iRi", 'i', "itemSilicon", 'F', machineChassis, 'G', "gearStone", 'R', "dustRedstone", 'C', capacitorBasic));


        //Basic drill
        GameRegistry.addRecipe(new ShapedOreRecipe(ModItems.basicDrill,
                " i ",
                "iGi",
                "IMI", 'I', "ingotIron", 'i', "ingotElectricalSteel", 'G', "gearStone", 'M', new ItemStack(ModItems.motor, 1, 4)));
    }
}
