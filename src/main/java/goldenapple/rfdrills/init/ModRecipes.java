package goldenapple.rfdrills.init;

import cofh.api.modhelpers.ThermalExpansionHelper;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import goldenapple.rfdrills.crafting.ShapedUpgradeRecipe;
import goldenapple.rfdrills.reference.LibMetadata;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class ModRecipes {

    public static void init(){
        if(Loader.isModLoaded("ThermalExpansion")) initTE();
        if(Loader.isModLoaded("EnderIO")) initEIO();
    }

    private static void initTE(){
        ItemStack motorLeadstone = new ItemStack(ModItems.motor, 1, LibMetadata.MOTOR_LEASDSTONE);
        ItemStack motorHardened = new ItemStack(ModItems.motor, 1, LibMetadata.MOTOR_HARDENED);
        ItemStack motorRedstoneFrameEmpty = new ItemStack(ModItems.motor, 1, LibMetadata.MOTOR_REDSTONE_EMPTY);
        ItemStack motorRedstoneFrame = new ItemStack(ModItems.motor, 1, LibMetadata.MOTOR_REDSTONE_FULL);
        ItemStack motorRedstone = new ItemStack(ModItems.motor, 1, LibMetadata.MOTOR_REDSTONE);
        ItemStack motorResonant = new ItemStack(ModItems.motor, 1, LibMetadata.MOTOR_RESONANT);

        ItemStack pneumaticServo = new ItemStack(GameRegistry.findItem("ThermalExpansion", "material"), 1, 0);
        ItemStack receptionCoil = new ItemStack(GameRegistry.findItem("ThermalExpansion", "material"), 1, 1);
        ItemStack cryocoilAugment = new ItemStack(GameRegistry.findItem("ThermalExpansion", "augment"), 1, 82);
        ItemStack capacitorLeadstone = new ItemStack(GameRegistry.findItem("ThermalExpansion", "capacitor"), 1, 2);
        ItemStack capacitorHardened = new ItemStack(GameRegistry.findItem("ThermalExpansion", "capacitor"), 1, 3);
        ItemStack capacitorRedstone = new ItemStack(GameRegistry.findItem("ThermalExpansion", "capacitor"), 1, 4);
        ItemStack capacitorResonant = new ItemStack(GameRegistry.findItem("ThermalExpansion", "capacitor"), 1, 5);

        //Leadstone motor
        GameRegistry.addRecipe(new ShapedOreRecipe(motorLeadstone.copy(),
                "iDi",
                "GPG",
                "iCi", 'i', "ingotLead", 'D', "dustRedstone", 'G', "gearTin", 'P', pneumaticServo.copy(), 'C', receptionCoil.copy()));

        //Hardened motor
        GameRegistry.addRecipe(new ShapedOreRecipe(motorHardened.copy(),
                "iDi",
                "GPG",
                "iCi", 'i', "ingotInvar", 'D', "dustGlowstone", 'G', "gearElectrum", 'P', pneumaticServo.copy(), 'C', receptionCoil.copy()));

        //Redstone motor frame
        GameRegistry.addRecipe(new ShapedOreRecipe(motorRedstoneFrameEmpty.copy(),
                "iGi",
                "GBG",
                "iGi", 'i', "ingotElectrum", 'B', "blockRedstone", 'G', "blockGlassHardened"));

        //Redstone motor frame filling
        ThermalExpansionHelper.addTransposerFill(16000, motorRedstoneFrameEmpty.copy(), motorRedstoneFrame.copy(), FluidRegistry.getFluidStack("redstone", 4000), false);

        //Redstone motor
        GameRegistry.addRecipe(new ShapedOreRecipe(motorRedstone.copy(),
                "iMi",
                "GPG",
                "iCi", 'i', "ingotElectrum", 'M', new ItemStack(ModItems.motor, 1, LibMetadata.MOTOR_REDSTONE_FULL), 'G', "gearSignalum", 'P', pneumaticServo.copy(), 'C', receptionCoil.copy()));

        //Resonant motor
        GameRegistry.addRecipe(new ShapedOreRecipe(motorResonant.copy(),
                "iAi",
                "GMG",
                "iCi", 'i', "ingotSilver", 'A', cryocoilAugment.copy(), 'G', "gearEnderium", 'M', new ItemStack(ModItems.motor, 1, LibMetadata.MOTOR_REDSTONE), 'C', receptionCoil.copy()));


        //Leadstone drill
        GameRegistry.addRecipe(new ShapedUpgradeRecipe(ModItems.leadstoneDrill,
                " i ",
                "iMi",
                "ICI", 'I', "ingotLead", 'i', "ingotBronze", 'C', capacitorLeadstone.copy(), 'M', motorLeadstone.copy()));

        //Hardened drill
        GameRegistry.addRecipe(new ShapedUpgradeRecipe(ModItems.hardenedDrill,
                " D ",
                "iMi",
                "ICI", 'I', "ingotInvar", 'i', "ingotIron", 'C', capacitorHardened.copy(), 'M', motorHardened.copy(), 'D', new ItemStack(ModItems.leadstoneDrill)));

        //Redstone drill
        GameRegistry.addRecipe(new ShapedUpgradeRecipe(ModItems.redstoneDrill,
                " D ",
                "iMi",
                "ICI", 'I', "ingotElectrum", 'i', "ingotInvar", 'C', capacitorRedstone.copy(), 'M', motorRedstone.copy(), 'D', new ItemStack(ModItems.hardenedDrill)));

        //Resonant drill
        GameRegistry.addRecipe(new ShapedUpgradeRecipe(ModItems.resonantDrill,
                " D ",
                "iMi",
                "ICI", 'I', "ingotEnderium", 'i', "ingotSilver", 'C', capacitorResonant.copy(), 'M', motorResonant.copy(), 'D', new ItemStack(ModItems.redstoneDrill)));
    }

    private static void initEIO(){
        ItemStack motorBasic = new ItemStack(ModItems.motor, 1, LibMetadata.MOTOR_BASIC);
        ItemStack machineChassis = new ItemStack(GameRegistry.findItem("EnderIO", "itemMachinePart"), 1, 0);
        ItemStack capacitorBasic = new ItemStack(GameRegistry.findItem("EnderIO", "itemBasicCapacitor"), 1, 0);

        //Basic motor
        GameRegistry.addRecipe(new ShapedOreRecipe(motorBasic.copy(),
                "iCi",
                "GFG",
                "iRi", 'i', "itemSilicon", 'F', machineChassis.copy(), 'G', "gearStone", 'R', "dustRedstone", 'C', capacitorBasic.copy()));


        //Basic drill
        GameRegistry.addRecipe(new ShapedOreRecipe(ModItems.basicDrill,
                " i ",
                "iMi",
                "ICI", 'I', "ingotIron", 'i', "ingotElectricalSteel", 'C', capacitorBasic.copy(), 'M', motorBasic.copy()));
    }
}
