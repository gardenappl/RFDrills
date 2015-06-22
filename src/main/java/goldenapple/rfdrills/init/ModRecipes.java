package goldenapple.rfdrills.init;

import cofh.api.modhelpers.ThermalExpansionHelper;
import cpw.mods.fml.common.registry.GameRegistry;
import goldenapple.rfdrills.RFDrills;
import goldenapple.rfdrills.config.ConfigHandler;
import goldenapple.rfdrills.crafting.ShapedUpgradeRecipe;
import goldenapple.rfdrills.reference.LibMetadata;
import goldenapple.rfdrills.reference.Reference;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.oredict.RecipeSorter;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class ModRecipes {

    public static void init(){
        RecipeSorter.register(Reference.MOD_ID + ":upgrading", ShapedUpgradeRecipe.class, RecipeSorter.Category.SHAPED, "after:minecraft:shaped before:minecraft:shapeless");
        if(RFDrills.isTELoaded && ConfigHandler.integrateTE){
            initTE();
            if(RFDrills.isRArsLoaded && ConfigHandler.integrateRArs) initRArs();
        }
        if(RFDrills.isEIOLoaded && ConfigHandler.integrateEIO) initEIO();
    }

    private static void initTE(){
        ItemStack motorLeadstone = new ItemStack(ModItems.componentTE, 1, LibMetadata.MOTOR_LEASDSTONE);
        ItemStack motorHardened = new ItemStack(ModItems.componentTE, 1, LibMetadata.MOTOR_HARDENED);
        ItemStack motorRedstoneFrame = new ItemStack(ModItems.componentTE, 1, LibMetadata.MOTOR_REDSTONE_FRAME);
        ItemStack motorRedstone = new ItemStack(ModItems.componentTE, 1, LibMetadata.MOTOR_REDSTONE);
        ItemStack motorResonantFrame = new ItemStack(ModItems.componentTE, 1, LibMetadata.MOTOR_RESONANT_FRAME);
        ItemStack motorResonant = new ItemStack(ModItems.componentTE, 1, LibMetadata.MOTOR_RESONANT);

        ItemStack pneumaticServo = new ItemStack(GameRegistry.findItem("ThermalExpansion", "material"), 1, 0);
        ItemStack receptionCoil = new ItemStack(GameRegistry.findItem("ThermalExpansion", "material"), 1, 1);
        ItemStack cryocoilAugment = new ItemStack(GameRegistry.findItem("ThermalExpansion", "augment"), 1, 82);
        ItemStack capacitorLeadstone = new ItemStack(GameRegistry.findItem("ThermalExpansion", "capacitor"), 1, 2);
        ItemStack capacitorHardened = new ItemStack(GameRegistry.findItem("ThermalExpansion", "capacitor"), 1, 3);
        ItemStack capacitorRedstone = new ItemStack(GameRegistry.findItem("ThermalExpansion", "capacitor"), 1, 4);
        ItemStack capacitorResonant = new ItemStack(GameRegistry.findItem("ThermalExpansion", "capacitor"), 1, 5);

        //Leadstone motor
        GameRegistry.addRecipe(new ShapedOreRecipe(motorLeadstone.copy(),
                "DGD",
                "iPi",
                "DCD", 'i', "ingotLead", 'D', "dustRedstone", 'G', "gearTin", 'P', new ItemStack(Blocks.piston), 'C', receptionCoil.copy()));

        //Hardened motor
        GameRegistry.addRecipe(new ShapedOreRecipe(motorHardened.copy(),
                "DGD",
                "iPi",
                "DCD", 'i', "ingotInvar", 'D', "dustRedstone", 'G', "gearElectrum", 'P', new ItemStack(Blocks.piston), 'C', receptionCoil.copy()));

        //Redstone motor frame
        GameRegistry.addRecipe(new ShapedOreRecipe(motorRedstoneFrame.copy(),
                "HGH",
                "iPi",
                "HCH", 'i', "ingotElectrum", 'H', "blockGlassHardened", 'G', "gearSignalum", 'P', new ItemStack(Blocks.piston), 'C', receptionCoil.copy()));

        //Redstone motor
        ThermalExpansionHelper.addTransposerFill(16000, motorRedstoneFrame.copy(), motorRedstone.copy(), FluidRegistry.getFluidStack("redstone", 4000), false);

        //Resonant motor frame
        GameRegistry.addRecipe(new ShapedOreRecipe(motorResonantFrame.copy(),
                "HGH",
                "iPi",
                "HCH", 'i', "ingotSilver", 'H', "blockGlassHardened", 'G', "gearEnderium", 'P', new ItemStack(Blocks.piston), 'C', receptionCoil.copy()));

        //Resonant motor
        ThermalExpansionHelper.addTransposerFill(16000, motorResonantFrame.copy(), motorResonant.copy(), FluidRegistry.getFluidStack("cryotheum", 4000), false);


        //Leadstone drill
        GameRegistry.addRecipe(new ShapedUpgradeRecipe(ModItems.leadstoneDrill,
                " i ",
                "iMi",
                "ICI", 'I', "ingotLead", 'i', "ingotTin", 'C', capacitorLeadstone.copy(), 'M', motorLeadstone.copy()));

        //Leadstone chainsaw
        GameRegistry.addRecipe(new ShapedUpgradeRecipe(ModItems.leadstoneChainsaw,
                " ii",
                "IMi",
                "CI ", 'I', "ingotLead", 'i', "ingotTin", 'C', capacitorLeadstone.copy(), 'M', motorLeadstone.copy()).setMirrored(true));

        //Hardened drill
        GameRegistry.addRecipe(new ShapedUpgradeRecipe(ModItems.hardenedDrill,
                " D ",
                "iMi",
                "ICI", 'I', "ingotInvar", 'i', "ingotElectrum", 'C', capacitorHardened.copy(), 'M', motorHardened.copy(), 'D', new ItemStack(ModItems.leadstoneDrill)));

        //Hardened chainsaw
        GameRegistry.addRecipe(new ShapedUpgradeRecipe(ModItems.hardenedChainsaw,
                " iS",
                "IMi",
                "CI ", 'I', "ingotInvar", 'i', "ingotElectrum", 'C', capacitorHardened.copy(), 'M', motorHardened.copy(), 'S', new ItemStack(ModItems.leadstoneChainsaw)).setMirrored(true));

        //Redstone drill
        GameRegistry.addRecipe(new ShapedUpgradeRecipe(ModItems.redstoneDrill,
                " D ",
                "iMi",
                "ICI", 'I', "ingotElectrum", 'i', "ingotSignalum", 'C', capacitorRedstone.copy(), 'M', motorRedstone.copy(), 'D', new ItemStack(ModItems.hardenedDrill)));

        //Redstone chainsaw
        GameRegistry.addRecipe(new ShapedUpgradeRecipe(ModItems.redstoneChainsaw,
                " iS",
                "IMi",
                "CI ", 'I', "ingotElectrum", 'i', "ingotSignalum", 'C', capacitorRedstone.copy(), 'M', motorRedstone.copy(), 'S', new ItemStack(ModItems.hardenedChainsaw)).setMirrored(true));

        //Resonant drill
        GameRegistry.addRecipe(new ShapedUpgradeRecipe(ModItems.resonantDrill,
                " D ",
                "iMi",
                "ICI", 'I', "ingotEnderium", 'i', "ingotSilver", 'C', capacitorResonant.copy(), 'M', motorResonant.copy(), 'D', new ItemStack(ModItems.redstoneDrill)));

        //Resonant chainsaw
        GameRegistry.addRecipe(new ShapedUpgradeRecipe(ModItems.resonantChainsaw,
                " iS",
                "IMi",
                "CI ", 'I', "ingotEnderium", 'i', "ingotSilver", 'C', capacitorResonant.copy(), 'M', motorResonant.copy(), 'S', new ItemStack(ModItems.redstoneChainsaw)).setMirrored(true));
    }

    private static void initEIO(){
        ItemStack motorBasic = new ItemStack(ModItems.componentEIO, 1, LibMetadata.MOTOR_BASIC);
        ItemStack motorAdvanced = new ItemStack(ModItems.componentEIO, 1, LibMetadata.MOTOR_ADVANCED);
        ItemStack machineChassis = new ItemStack(GameRegistry.findItem("EnderIO", "itemMachinePart"), 1, 0);
        ItemStack pulsatingCrystal = new ItemStack(GameRegistry.findItem("EnderIO", "itemMaterial"), 1, 5);
        ItemStack capacitorBasic = new ItemStack(GameRegistry.findItem("EnderIO", "itemBasicCapacitor"), 1, 0);
        ItemStack capacitorAdvanced = new ItemStack(GameRegistry.findItem("EnderIO", "itemBasicCapacitor"), 1, 1);

        ItemStack resonatingCrystal = new ItemStack(ModItems.componentEIO, 1, LibMetadata.RESONATING_CRYSTAL);
        ItemStack destructiveCrystal = new ItemStack(ModItems.componentEIO, 1, LibMetadata.DESTRUCTIVE_CRYSTAL);
        ItemStack earthshakingCrystal = new ItemStack(ModItems.componentEIO, 1, LibMetadata.EARTHSHAKING_CRYSTAL);
        ItemStack darkSoulariumIngot = new ItemStack(GameRegistry.findItem("simplyjetpacks", "components"), 1, 70);
        ItemStack soulariumIngot = new ItemStack(GameRegistry.findItem("EnderIO", "itemAlloy"), 1, 7);

        //Basic motor
        GameRegistry.addRecipe(new ShapedOreRecipe(motorBasic.copy(),
                "RGR",
                "iFi",
                "RCR", 'i', "itemSilicon", 'F', machineChassis.copy(), 'G', "gearStone", 'R', "dustRedstone", 'C', capacitorBasic.copy()));

        //Advanced motor
        GameRegistry.addRecipe(new ShapedOreRecipe(motorAdvanced.copy(),
                "RPR",
                "iFi",
                "RCR", 'i', "ingotEnergeticAlloy", 'F', machineChassis.copy(), 'P', pulsatingCrystal.copy(), 'R', "ingotRedstoneAlloy", 'C', capacitorAdvanced.copy()));

        //Basic drill
        GameRegistry.addRecipe(new ShapedUpgradeRecipe(ModItems.basicDrill,
                " i ",
                "iMi",
                "ICI", 'I', "itemSilicon", 'i', "ingotConductiveIron", 'C', capacitorBasic.copy(), 'M', motorBasic.copy()));

        //Basic chainsaw
        GameRegistry.addRecipe(new ShapedUpgradeRecipe(ModItems.basicChainsaw,
                " ii",
                "IMi",
                "CI ", 'I', "itemSilicon", 'i', "ingotConductiveIron", 'C', capacitorBasic.copy(), 'M', motorBasic.copy()).setMirrored(true));

        //Advanced drill
        GameRegistry.addRecipe(new ShapedUpgradeRecipe(ModItems.advancedDrill,
                " D ",
                "iMi",
                "ICI", 'I', "ingotEnergeticAlloy", 'i', "ingotElectricalSteel", 'C', capacitorAdvanced.copy(), 'M', motorAdvanced.copy(), 'D', new ItemStack(ModItems.basicDrill)));

        //Advanced chainsaw
        GameRegistry.addRecipe(new ShapedUpgradeRecipe(ModItems.advancedChainsaw,
                " iS",
                "IMi",
                "CI ", 'I', "ingotEnergeticAlloy", 'i', "ingotElectricalSteel", 'C', capacitorAdvanced.copy(), 'M', motorAdvanced.copy(), 'S', new ItemStack(ModItems.basicChainsaw)).setMirrored(true));

        //Dark Soularium nuggets -> ingot
        GameRegistry.addRecipe(new ShapedOreRecipe(darkSoulariumIngot.copy(),
                "nnn",
                "nnn",
                "nnn", 'n', "nuggetDarkSoularium"));

        //Dark Soularium ingot -> nuggets
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ModItems.componentEIO, 9, LibMetadata.DARK_SOULARIUM_NUGGET),
                darkSoulariumIngot.copy()));

        //Soularium nuggets -> ingot
        GameRegistry.addRecipe(new ShapedOreRecipe(soulariumIngot.copy(),
                "nnn",
                "nnn",
                "nnn", 'n', "nuggetSoularium"));

        //Soularium ingot -> nuggets
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ModItems.componentEIO, 9, LibMetadata.SOULARIUM_NUGGET),
                soulariumIngot.copy()));

        if(RFDrills.isSJLoaded && ConfigHandler.integrateSJ){
            //Resonating Crystal
            GameRegistry.addRecipe(new ShapedOreRecipe(resonatingCrystal.copy(),
                "nnn",
                "nDn",
                "nnn", 'n', "nuggetDarkSoularium", 'D', "gemDiamond"));

            //Soul Crusher
            GameRegistry.addRecipe(new ShapedOreRecipe(ModItems.soulCrusher,
                "iCi",
                "iSi",
                " S ", 'i', darkSoulariumIngot.copy(), 'C', resonatingCrystal.copy(), 'S', "ingotDarkSteel"));
        }else{
            //Resonating Crystal
            GameRegistry.addRecipe(new ShapedOreRecipe(resonatingCrystal.copy(),
                "nnn",
                "nDn",
                "nnn", 'n', "nuggetSoularium", 'D', "gemDiamond"));

            //Soul Crusher
            GameRegistry.addRecipe(new ShapedOreRecipe(ModItems.soulCrusher,
                "iCi",
                "iSi",
                " S ", 'i', "ingotSoularium", 'C', resonatingCrystal.copy(), 'S', "ingotDarkSteel"));
        }
    }

    public static void initRArs(){
        ItemStack fluxRod = new ItemStack(GameRegistry.findItem("RedstoneArsenal", "material"), 1, 193);
        ItemStack fluxPickaxe = new ItemStack(GameRegistry.findItem("RedstoneArsenal", "tool.pickaxeFlux"), 1, 0);
        ItemStack fluxShovel = new ItemStack(GameRegistry.findItem("RedstoneArsenal", "tool.shovelFlux"), 1, 0);
        ItemStack fluxAxe = new ItemStack(GameRegistry.findItem("RedstoneArsenal", "tool.axeFlux"), 1, 0);

        GameRegistry.addRecipe(new ShapedUpgradeRecipe(ModItems.fluxBreaker,
                "PSA",
                "DBC",
                " R ", 'B', "blockElectrumFlux", 'R', fluxRod, 'P', fluxPickaxe, 'S', fluxShovel, 'A', fluxAxe, 'D', new ItemStack(ModItems.resonantDrill), 'C', new ItemStack(ModItems.resonantChainsaw)));
    }
}
