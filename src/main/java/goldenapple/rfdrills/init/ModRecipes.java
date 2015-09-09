package goldenapple.rfdrills.init;

import cofh.api.modhelpers.ThermalExpansionHelper;
import cofh.lib.util.helpers.ItemHelper;
import cpw.mods.fml.common.registry.GameRegistry;
import goldenapple.rfdrills.RFDrills;
import goldenapple.rfdrills.util.modhelpers.EnderIOHelper;
import goldenapple.rfdrills.config.ConfigHandler;
import goldenapple.rfdrills.crafting.ShapedUpgradeRecipe;
import goldenapple.rfdrills.reference.LibMetadata;
import goldenapple.rfdrills.reference.Reference;
import goldenapple.rfdrills.util.LogHelper;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.oredict.RecipeSorter;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class ModRecipes {

    public static void init(){
        RecipeSorter.register(Reference.MOD_ID + ":upgrading", ShapedUpgradeRecipe.class, RecipeSorter.Category.SHAPED, "after:minecraft:shaped before:minecraft:shapeless");
        if(RFDrills.isTELoaded && ConfigHandler.integrateTE) initTE();
        if(RFDrills.isEIOLoaded && ConfigHandler.integrateEIO) initEIO();
        if((RFDrills.isRArsLoaded || RFDrills.isSJLoaded) && ConfigHandler.integrateRArs) initRArs();
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

        //Flux hoe
        GameRegistry.addRecipe(new ShapedUpgradeRecipe(ModItems.fluxHoe,
                "iC",
                " I",
                " I", 'I', "ingotIron", 'C', capacitorLeadstone.copy(), 'i', "ingotInvar").setMirrored(true));
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
        ItemStack ingotDarkSoularium = RFDrills.isSJLoaded ? new ItemStack(GameRegistry.findItem("simplyjetpacks", "components"), 1, 70) : new ItemStack(ModItems.replacementSJ, 1, LibMetadata.DARK_SOULARIUM);
        ItemStack nugggetDarkSoularium = new ItemStack(ModItems.componentEIO, 1, LibMetadata.RICH_SOULARIUM_NUGGET);
        ItemStack ingotDarkSteel = new ItemStack(GameRegistry.findItem("EnderIO", "itemAlloy"), 1, 6);
        ItemStack ingotSoularium = new ItemStack(GameRegistry.findItem("EnderIO", "itemAlloy"), 1, 7);
        ItemStack nuggetSoularium = new ItemStack(ModItems.componentEIO, 1, LibMetadata.SOULARIUM_NUGGET);

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
                "ICI", 'I', "ingotEnergeticAlloy", 'i', "ingotElectricalSteel", 'C', capacitorAdvanced.copy(), 'M', motorAdvanced.copy(), 'D', ModItems.basicDrill));

        //Advanced chainsaw
        GameRegistry.addRecipe(new ShapedUpgradeRecipe(ModItems.advancedChainsaw,
                " iS",
                "IMi",
                "CI ", 'I', "ingotEnergeticAlloy", 'i', "ingotElectricalSteel", 'C', capacitorAdvanced.copy(), 'M', motorAdvanced.copy(), 'S', ModItems.basicChainsaw).setMirrored(true));

        //Soularium ingot <-> nuggets
        ItemHelper.addTwoWayStorageRecipe(ingotSoularium, "ingotSoularium", nuggetSoularium, "nuggetSoularium");

        //Dark Soularium ingot <-> nuggets
        ItemHelper.addTwoWayStorageRecipe(ingotDarkSoularium, "ingotDarkSoularium", nugggetDarkSoularium, "nuggetDarkSoularium");

        if(!RFDrills.isSJLoaded){
            LogHelper.info("Simply Jetpacks not found! Using replacement recipes...");
            EnderIOHelper.addAlloySmelterRecipe("Dark Soularium", 50000, ingotDarkSteel.copy(), ingotSoularium.copy(), pulsatingCrystal.copy(), ingotDarkSoularium.copy());
        }

        //Soul Crusher
        GameRegistry.addRecipe(new ShapedUpgradeRecipe(ModItems.soulCrusher,
                "iGi",
                "DSC",
                " S ", 'i', ingotDarkSoularium.copy(), 'G', resonatingCrystal.copy(), 'S', "ingotDarkSteel", 'D', ModItems.advancedDrill, 'C', ModItems.advancedChainsaw).setMirrored(true));

        //Resonating Crystal
        GameRegistry.addRecipe(new ShapedOreRecipe(resonatingCrystal.copy(),
                "nnn",
                "nGn",
                "nnn", 'n', "nuggetDarkSoularium", 'G', "gemDiamond"));

        //Destructive Crystal
        EnderIOHelper.addSoulBinderRecipe("Destructive Crystal", 100000, 10, "Creeper", resonatingCrystal.copy(), destructiveCrystal.copy());

        //Earthshaking Crystal
        EnderIOHelper.addSoulBinderRecipe("Earthshaking Crystal", 200000, 15, "Ghast", destructiveCrystal.copy(), earthshakingCrystal.copy());

        GameRegistry.addRecipe(new ShapedUpgradeRecipe(ModItems.fluxHoe,
                "iC",
                " s",
                " s", 'i', "ingotElectricalSteel", 'C', capacitorBasic.copy(), 's', "itemSilicon").setMirrored(true));
    }

    private static void initRArs(){
        boolean useReplacement = RFDrills.isSJLoaded && !RFDrills.isRArsLoaded;

        ItemStack obsidianRod = useReplacement ? new ItemStack(ModItems.replacementRA, 1, LibMetadata.OBSIDIAN_ROD) : new ItemStack(GameRegistry.findItem("RedstoneArsenal", "material"), 1, 192);
        ItemStack fluxRod = useReplacement ? new ItemStack(ModItems.replacementRA, 1, LibMetadata.FLUX_OBSIDIAN_ROD) : new ItemStack(GameRegistry.findItem("RedstoneArsenal", "material"), 1, 193);
        ItemStack fluxArmorPlate = useReplacement ? new ItemStack(GameRegistry.findItem("simplyjetpacks", "components"), 1, 68) : new ItemStack(GameRegistry.findItem("RedstoneArsenal", "material"), 1, 128);

        ItemStack coolantUnit = new ItemStack(GameRegistry.findItem("simplyjetpacks", "components"), 1, 63);
        ItemStack superConductanceCoil = new ItemStack(ModItems.componentTE, 1, LibMetadata.SUPERCONDUCTANCE_COIL);
        ItemStack fluctuatingCoreFrame = new ItemStack(ModItems.componentTE, 1, LibMetadata.FLUCTUATING_CORE_FRAME);
        ItemStack fluctuatingCore = new ItemStack(ModItems.componentTE, 1, LibMetadata.FLUCTUATING_CORE);

        //Superconductance coil
        GameRegistry.addRecipe(new ShapedOreRecipe(superConductanceCoil.copy(),
                "sc ",
                "cFc",
                " cs", 's', "ingotSignalum", 'F', "ingotElectrumFlux", 'c', "dustCryotheum").setMirrored(true));

        //Fluctuating core frame
        GameRegistry.addRecipe(new ShapedOreRecipe(fluctuatingCoreFrame.copy(),
                "eFe",
                "FGF",
                "eCe", 'e', "ingotEnderium", 'F', fluxArmorPlate.copy(), 'G', "gemCrystalFlux", 'C', superConductanceCoil.copy()));

        //Fluctuatung core
        ThermalExpansionHelper.addTransposerFill(16000, fluctuatingCoreFrame.copy(), fluctuatingCore.copy(), FluidRegistry.getFluidStack("cryotheum", 8000), false);

        if(RFDrills.isSJLoaded){
            GameRegistry.addRecipe(new ShapedOreRecipe(fluctuatingCore.copy(),
                "ece",
                "FGF",
                "eCe", 'e', "ingotEnderium", 'F', fluxArmorPlate.copy(), 'G', "gemCrystalFlux", 'C', superConductanceCoil.copy(), 'c', coolantUnit.copy()));
        }

        if(RFDrills.isRArmLoaded){
            GameRegistry.addRecipe(new ShapedOreRecipe(fluctuatingCore.copy(),
                "eFe",
                "FGF",
                "eCe", 'e', "ingotGelidEnderium", 'F', fluxArmorPlate.copy(), 'G', "gemCrystalFlux", 'C', superConductanceCoil.copy()));
        }

        if(!RFDrills.isRArsLoaded && RFDrills.isSJLoaded){
            LogHelper.info("Redstone Arsenal not found but Simply Jetpacks is available! Using SJ's replacement items...");

            GameRegistry.addRecipe(new ShapedOreRecipe(obsidianRod.copy(),
                "  o",
                " b ",
                "o  ", 'o', "dustObsidian", 'b', "itemBlazePowder"));

            GameRegistry.addRecipe(new ShapedOreRecipe(fluxRod.copy(),
                "  G",
                " R ",
                "G  ", 'R', obsidianRod.copy(), 'G', "gemCrystalFlux"));
        }

        //Flux Crusher
        GameRegistry.addRecipe(new ShapedUpgradeRecipe(ModItems.fluxCrusher,
                "iFi",
                "DRC",
                " R ", 'i', "ingotElectrumFlux", 'R', fluxRod, 'D', ModItems.resonantDrill, 'C', ModItems.resonantChainsaw, 'F', fluctuatingCore.copy()).setMirrored(true));
    }
}
