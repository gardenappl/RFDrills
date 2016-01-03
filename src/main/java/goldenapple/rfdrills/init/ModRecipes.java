package goldenapple.rfdrills.init;

import cofh.api.modhelpers.ThermalExpansionHelper;
import cofh.lib.util.helpers.ItemHelper;
import cpw.mods.fml.common.registry.GameRegistry;
import goldenapple.rfdrills.RFDrills;
import goldenapple.rfdrills.compat.enderio.EnderIOCompat;
import goldenapple.rfdrills.compat.simplyjetpacks.SimplyJetpacksCompat;
import goldenapple.rfdrills.config.ConfigHandler;
import goldenapple.rfdrills.crafting.ShapedUpgradeRecipe;
import goldenapple.rfdrills.crafting.ShapelessMuffleRecipe;
import goldenapple.rfdrills.crafting.ShapelessUnmuffleRecipe;
import goldenapple.rfdrills.reference.Metadata;
import goldenapple.rfdrills.reference.Reference;
import goldenapple.rfdrills.util.LogHelper;
import goldenapple.rfdrills.util.OreHelper;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.oredict.RecipeSorter;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class ModRecipes {
    public static void init(){
        RecipeSorter.register(Reference.MOD_ID + ":upgrading", ShapedUpgradeRecipe.class, RecipeSorter.Category.SHAPED, "after:minecraft:shaped before:minecraft:shapeless");
        RecipeSorter.register(Reference.MOD_ID + ":muffle", ShapelessMuffleRecipe.class, RecipeSorter.Category.SHAPELESS, "after:minecraft:shapeless");
        RecipeSorter.register(Reference.MOD_ID + ":unmuffle", ShapelessUnmuffleRecipe.class, RecipeSorter.Category.SHAPELESS, "after:minecraft:shapeless");

        if(RFDrills.isTELoaded && ConfigHandler.integrateTE) initTE();
        if(RFDrills.isEIOLoaded && ConfigHandler.integrateEIO) initEIO();
        if((RFDrills.isRArsLoaded || SimplyJetpacksCompat.integratesTE()) && ConfigHandler.integrateRArs) initRArs();
    }

    private static void initTE(){
        ItemStack motorLeadstone = new ItemStack(ModItems.componentTE, 1, Metadata.MOTOR_LEASDSTONE);
        ItemStack motorHardened = new ItemStack(ModItems.componentTE, 1, Metadata.MOTOR_HARDENED);
        ItemStack motorRedstoneFrame = new ItemStack(ModItems.componentTE, 1, Metadata.MOTOR_REDSTONE_FRAME);
        ItemStack motorRedstone = new ItemStack(ModItems.componentTE, 1, Metadata.MOTOR_REDSTONE);
        ItemStack motorResonantFrame = new ItemStack(ModItems.componentTE, 1, Metadata.MOTOR_RESONANT_FRAME);
        ItemStack motorResonant = new ItemStack(ModItems.componentTE, 1, Metadata.MOTOR_RESONANT);

        ItemStack receptionCoil = new ItemStack(GameRegistry.findItem("ThermalExpansion", "material"), 1, 1);
        ItemStack capacitorLeadstone = new ItemStack(GameRegistry.findItem("ThermalExpansion", "capacitor"), 1, 2);
        ItemStack capacitorHardened = new ItemStack(GameRegistry.findItem("ThermalExpansion", "capacitor"), 1, 3);
        ItemStack capacitorRedstone = new ItemStack(GameRegistry.findItem("ThermalExpansion", "capacitor"), 1, 4);
        ItemStack capacitorResonant = new ItemStack(GameRegistry.findItem("ThermalExpansion", "capacitor"), 1, 5);


        //Leadstone motor
        GameRegistry.addRecipe(new ShapedOreRecipe(motorLeadstone,
                "DGD",
                "iPi",
                "DCD", 'i', "ingotLead", 'D', "dustRedstone", 'G', "gearTin", 'P', new ItemStack(Blocks.piston), 'C', receptionCoil));

        //Hardened motor
        GameRegistry.addRecipe(new ShapedOreRecipe(motorHardened,
                "DGD",
                "iPi",
                "DCD", 'i', "ingotInvar", 'D', "dustRedstone", 'G', "gearElectrum", 'P', new ItemStack(Blocks.piston), 'C', receptionCoil));

        //Redstone motor frame
        GameRegistry.addRecipe(new ShapedOreRecipe(motorRedstoneFrame,
                "HGH",
                "iPi",
                "HCH", 'i', "ingotElectrum", 'H', "blockGlassHardened", 'G', "gearSignalum", 'P', new ItemStack(Blocks.piston), 'C', receptionCoil));

        //Redstone motor
        ThermalExpansionHelper.addTransposerFill(16000, motorRedstoneFrame, motorRedstone, FluidRegistry.getFluidStack("redstone", 4000), false);

        //Resonant motor frame
        GameRegistry.addRecipe(new ShapedOreRecipe(motorResonantFrame,
                "HGH",
                "iPi",
                "HCH", 'i', "ingotSilver", 'H', "blockGlassHardened", 'G', "gearEnderium", 'P', new ItemStack(Blocks.piston), 'C', receptionCoil));

        //Resonant motor
        ThermalExpansionHelper.addTransposerFill(16000, motorResonantFrame, motorResonant, FluidRegistry.getFluidStack("cryotheum", 4000), false);

        //Leadstone drill
        GameRegistry.addRecipe(new ShapedUpgradeRecipe(ModItems.leadstoneDrill,
                " i ",
                "iMi",
                "ICI", 'I', "ingotLead", 'i', "ingotTin", 'C', capacitorLeadstone, 'M', motorLeadstone));
        GameRegistry.addRecipe(new ShapelessMuffleRecipe(ModItems.leadstoneDrill));
        GameRegistry.addRecipe(new ShapelessUnmuffleRecipe(ModItems.leadstoneDrill));

        //Leadstone chainsaw
        GameRegistry.addRecipe(new ShapedUpgradeRecipe(ModItems.leadstoneChainsaw,
                " ii",
                "IMi",
                "CI ", 'I', "ingotLead", 'i', "ingotTin", 'C', capacitorLeadstone, 'M', motorLeadstone).setMirrored(true));
        GameRegistry.addRecipe(new ShapelessMuffleRecipe(ModItems.leadstoneChainsaw));
        GameRegistry.addRecipe(new ShapelessUnmuffleRecipe(ModItems.leadstoneChainsaw));

        //Hardened drill
        GameRegistry.addRecipe(new ShapedUpgradeRecipe(ModItems.hardenedDrill,
                " D ",
                "iMi",
                "ICI", 'I', "ingotInvar", 'i', "ingotElectrum", 'C', capacitorHardened, 'M', motorHardened, 'D', new ItemStack(ModItems.leadstoneDrill)));
        GameRegistry.addRecipe(new ShapelessMuffleRecipe(ModItems.hardenedDrill));
        GameRegistry.addRecipe(new ShapelessUnmuffleRecipe(ModItems.hardenedDrill));

        //Hardened chainsaw
        GameRegistry.addRecipe(new ShapedUpgradeRecipe(ModItems.hardenedChainsaw,
                " iS",
                "IMi",
                "CI ", 'I', "ingotInvar", 'i', "ingotElectrum", 'C', capacitorHardened, 'M', motorHardened, 'S', new ItemStack(ModItems.leadstoneChainsaw)).setMirrored(true));
        GameRegistry.addRecipe(new ShapelessMuffleRecipe(ModItems.hardenedChainsaw));
        GameRegistry.addRecipe(new ShapelessUnmuffleRecipe(ModItems.hardenedChainsaw));

        //Redstone drill
        GameRegistry.addRecipe(new ShapedUpgradeRecipe(ModItems.redstoneDrill,
                " D ",
                "iMi",
                "ICI", 'I', "ingotElectrum", 'i', "ingotSignalum", 'C', capacitorRedstone, 'M', motorRedstone, 'D', new ItemStack(ModItems.hardenedDrill)));
        GameRegistry.addRecipe(new ShapelessMuffleRecipe(ModItems.redstoneDrill));
        GameRegistry.addRecipe(new ShapelessUnmuffleRecipe(ModItems.redstoneDrill));

        //Redstone chainsaw
        GameRegistry.addRecipe(new ShapedUpgradeRecipe(ModItems.redstoneChainsaw,
                " iS",
                "IMi",
                "CI ", 'I', "ingotElectrum", 'i', "ingotSignalum", 'C', capacitorRedstone, 'M', motorRedstone, 'S', new ItemStack(ModItems.hardenedChainsaw)).setMirrored(true));
        GameRegistry.addRecipe(new ShapelessMuffleRecipe(ModItems.redstoneChainsaw));
        GameRegistry.addRecipe(new ShapelessUnmuffleRecipe(ModItems.redstoneChainsaw));

        //Resonant drill
        GameRegistry.addRecipe(new ShapedUpgradeRecipe(ModItems.resonantDrill,
                " D ",
                "iMi",
                "ICI", 'I', "ingotEnderium", 'i', "ingotSilver", 'C', capacitorResonant, 'M', motorResonant, 'D', new ItemStack(ModItems.redstoneDrill)));
        GameRegistry.addRecipe(new ShapelessMuffleRecipe(ModItems.resonantDrill));
        GameRegistry.addRecipe(new ShapelessUnmuffleRecipe(ModItems.resonantDrill));

        //Resonant chainsaw
        GameRegistry.addRecipe(new ShapedUpgradeRecipe(ModItems.resonantChainsaw,
                " iS",
                "IMi",
                "CI ", 'I', "ingotEnderium", 'i', "ingotSilver", 'C', capacitorResonant, 'M', motorResonant, 'S', new ItemStack(ModItems.redstoneChainsaw)).setMirrored(true));
        GameRegistry.addRecipe(new ShapelessMuffleRecipe(ModItems.resonantChainsaw));
        GameRegistry.addRecipe(new ShapelessUnmuffleRecipe(ModItems.resonantChainsaw));

        //Flux hoe
        GameRegistry.addRecipe(new ShapedUpgradeRecipe(ModItems.fluxHoe,
                "iC",
                " I",
                " I", 'I', "ingotIron", 'C', capacitorLeadstone, 'i', "ingotInvar").setMirrored(true));
    }

    private static void initEIO(){
        ItemStack motorBasic = new ItemStack(ModItems.componentEIO, 1, Metadata.MOTOR_BASIC);
        ItemStack motorAdvanced = new ItemStack(ModItems.componentEIO, 1, Metadata.MOTOR_ADVANCED);
        ItemStack machineChassis = new ItemStack(GameRegistry.findItem("EnderIO", "itemMachinePart"), 1, 0);
        ItemStack pulsatingCrystal = new ItemStack(GameRegistry.findItem("EnderIO", "itemMaterial"), 1, 5);
        ItemStack capacitorBasic = new ItemStack(GameRegistry.findItem("EnderIO", "itemBasicCapacitor"), 1, 0);
        ItemStack capacitorAdvanced = new ItemStack(GameRegistry.findItem("EnderIO", "itemBasicCapacitor"), 1, 1);

        ItemStack resonatingCrystal = new ItemStack(ModItems.componentEIO, 1, Metadata.RESONATING_CRYSTAL);
        ItemStack destructiveCrystal = new ItemStack(ModItems.componentEIO, 1, Metadata.DESTRUCTIVE_CRYSTAL);
        ItemStack earthshakingCrystal = new ItemStack(ModItems.componentEIO, 1, Metadata.EARTHSHAKING_CRYSTAL);
        ItemStack ingotDarkSoularium = OreHelper.findFirstOre("ingotDarkSoularium");
        ItemStack nugggetDarkSoularium = OreHelper.findFirstOre("nuggetDarkSoularium");
        ItemStack ingotDarkSteel = OreHelper.findFirstOre("ingotDarkSteel");
        ItemStack ingotSoularium = OreHelper.findFirstOre("ingotSoularium");
        ItemStack nuggetSoularium = OreHelper.findFirstOre("nuggetSoularium");

        //Basic motor
        GameRegistry.addRecipe(new ShapedOreRecipe(motorBasic,
                "RGR",
                "iFi",
                "RCR", 'i', "itemSilicon", 'F', machineChassis, 'G', "gearStone", 'R', "dustRedstone", 'C', capacitorBasic));

        //Advanced motor
        GameRegistry.addRecipe(new ShapedOreRecipe(motorAdvanced,
                "RPR",
                "iFi",
                "RCR", 'i', "ingotEnergeticAlloy", 'F', machineChassis, 'P', pulsatingCrystal, 'R', "ingotRedstoneAlloy", 'C', capacitorAdvanced));

        //Basic drill
        GameRegistry.addRecipe(new ShapedUpgradeRecipe(ModItems.basicDrill,
                " i ",
                "iMi",
                "ICI", 'I', "itemSilicon", 'i', "ingotConductiveIron", 'C', capacitorBasic, 'M', motorBasic));
        GameRegistry.addRecipe(new ShapelessMuffleRecipe(ModItems.basicDrill));
        GameRegistry.addRecipe(new ShapelessUnmuffleRecipe(ModItems.basicDrill));

        //Basic chainsaw
        GameRegistry.addRecipe(new ShapedUpgradeRecipe(ModItems.basicChainsaw,
                " ii",
                "IMi",
                "CI ", 'I', "itemSilicon", 'i', "ingotConductiveIron", 'C', capacitorBasic, 'M', motorBasic).setMirrored(true));
        GameRegistry.addRecipe(new ShapelessMuffleRecipe(ModItems.basicChainsaw));
        GameRegistry.addRecipe(new ShapelessUnmuffleRecipe(ModItems.basicChainsaw));

        //Advanced drill
        GameRegistry.addRecipe(new ShapedUpgradeRecipe(ModItems.advancedDrill,
                " D ",
                "iMi",
                "ICI", 'I', "ingotEnergeticAlloy", 'i', "ingotElectricalSteel", 'C', capacitorAdvanced, 'M', motorAdvanced, 'D', ModItems.basicDrill));
        GameRegistry.addRecipe(new ShapelessMuffleRecipe(ModItems.advancedDrill));
        GameRegistry.addRecipe(new ShapelessUnmuffleRecipe(ModItems.advancedDrill));

        //Advanced chainsaw
        GameRegistry.addRecipe(new ShapedUpgradeRecipe(ModItems.advancedChainsaw,
                " iS",
                "IMi",
                "CI ", 'I', "ingotEnergeticAlloy", 'i', "ingotElectricalSteel", 'C', capacitorAdvanced, 'M', motorAdvanced, 'S', ModItems.basicChainsaw).setMirrored(true));
        GameRegistry.addRecipe(new ShapelessMuffleRecipe(ModItems.advancedChainsaw));
        GameRegistry.addRecipe(new ShapelessUnmuffleRecipe(ModItems.advancedChainsaw));

        //Soularium ingot <-> nuggets
        ItemHelper.addTwoWayStorageRecipe(ingotSoularium, "ingotSoularium", nuggetSoularium, "nuggetSoularium");

        //Dark Soularium ingot <-> nuggets
        ItemHelper.addTwoWayStorageRecipe(ingotDarkSoularium, "ingotDarkSoularium", nugggetDarkSoularium, "nuggetDarkSoularium");

        if(!RFDrills.isSJLoaded){
            LogHelper.info("Simply Jetpacks not found! Using replacement recipes for EnderIO...");
            EnderIOCompat.addAlloySmelterRecipe("Dark Soularium", 50000, ingotDarkSteel, ingotSoularium, pulsatingCrystal, ingotDarkSoularium);
        }

        //Soul Crusher
        GameRegistry.addRecipe(new ShapedUpgradeRecipe(ModItems.soulCrusher,
                "iGi",
                "DSC",
                " S ", 'i', "ingotDarkSoularium", 'G', resonatingCrystal, 'S', "ingotDarkSteel", 'D', ModItems.advancedDrill, 'C', ModItems.advancedChainsaw).setMirrored(true));
        GameRegistry.addRecipe(new ShapelessMuffleRecipe(ModItems.soulCrusher));
        GameRegistry.addRecipe(new ShapelessUnmuffleRecipe(ModItems.soulCrusher));

        //Resonating Crystal
        GameRegistry.addRecipe(new ShapedOreRecipe(resonatingCrystal,
                "nnn",
                "nGn",
                "nnn", 'n', "nuggetDarkSoularium", 'G', "gemDiamond"));

        //Destructive Crystal
        EnderIOCompat.addSoulBinderRecipe("Destructive Crystal", 100000, 10, "Creeper", resonatingCrystal, destructiveCrystal);

        //Earthshaking Crystal
        EnderIOCompat.addSoulBinderRecipe("Earthshaking Crystal", 200000, 15, "Ghast", destructiveCrystal, earthshakingCrystal);

        //Flux Hoe
        GameRegistry.addRecipe(new ShapedUpgradeRecipe(ModItems.fluxHoe,
                "iC",
                " s",
                " s", 'i', "ingotElectricalSteel", 'C', capacitorBasic, 's', "itemSilicon").setMirrored(true));
    }

    private static void initRArs(){
        boolean useSJReplacement = !RFDrills.isRArsLoaded && SimplyJetpacksCompat.integratesTE();
        if(useSJReplacement)
            LogHelper.info("Redstone Arsenal not found but Simply Jetpacks and TE are installed! Adding alternative items...");

        ItemStack obsidianRod = useSJReplacement ? new ItemStack(ModItems.replacementRA1, 1, Metadata.OBSIDIAN_ROD) : new ItemStack(GameRegistry.findItem("RedstoneArsenal", "material"), 1, 192);
        ItemStack fluxRod = useSJReplacement ? new ItemStack(ModItems.replacementRA1, 1, Metadata.FLUX_OBSIDIAN_ROD) : new ItemStack(GameRegistry.findItem("RedstoneArsenal", "material"), 1, 193);
        ItemStack fluxArmorPlate = useSJReplacement ? new ItemStack(GameRegistry.findItem("simplyjetpacks", "components"), 1, 68) : new ItemStack(GameRegistry.findItem("RedstoneArsenal", "material"), 1, 128);

        ItemStack coolantUnit = new ItemStack(GameRegistry.findItem("simplyjetpacks", "components"), 1, 63);
        ItemStack superConductanceCoil = new ItemStack(ModItems.componentTE, 1, Metadata.SUPERCONDUCTANCE_COIL);
        ItemStack fluctuatingCoreFrame = new ItemStack(ModItems.componentTE, 1, Metadata.FLUCTUATING_CORE_FRAME);
        ItemStack fluctuatingCore = new ItemStack(ModItems.componentTE, 1, Metadata.FLUCTUATING_CORE);

        //Superconductance Coil
        GameRegistry.addRecipe(new ShapedOreRecipe(superConductanceCoil,
                "sc ",
                "cFc",
                " cs", 's', "ingotSignalum", 'F', "ingotElectrumFlux", 'c', "dustCryotheum").setMirrored(true));

        //Fluctuating Core Frame
        GameRegistry.addRecipe(new ShapedOreRecipe(fluctuatingCoreFrame,
                "eFe",
                "FGF",
                "eCe", 'e', "ingotEnderium", 'F', fluxArmorPlate, 'G', "gemCrystalFlux", 'C', superConductanceCoil));

        //Fluctuatung Core
        ThermalExpansionHelper.addTransposerFill(16000, fluctuatingCoreFrame, fluctuatingCore, FluidRegistry.getFluidStack("cryotheum", 8000), false);
        if(RFDrills.isSJLoaded){
            GameRegistry.addRecipe(new ShapedOreRecipe(fluctuatingCore,
                "ece",
                "FGF",
                "eCe", 'e', "ingotEnderium", 'F', fluxArmorPlate, 'G', "gemCrystalFlux", 'C', superConductanceCoil, 'c', coolantUnit));
        }
        if(RFDrills.isRArmLoaded){
            GameRegistry.addRecipe(new ShapedOreRecipe(fluctuatingCore,
                "eFe",
                "FGF",
                "eCe", 'e', "ingotGelidEnderium", 'F', fluxArmorPlate, 'G', "gemCrystalFlux", 'C', superConductanceCoil));
        }

        if(useSJReplacement){
            //Obsidian Rod
            GameRegistry.addRecipe(new ShapedOreRecipe(obsidianRod,
                "  o",
                " b ",
                "o  ", 'o', "dustObsidian", 'b', Items.blaze_powder).setMirrored(true));

            //Infused Obsidian Rod
            GameRegistry.addRecipe(new ShapedOreRecipe(fluxRod,
                "  G",
                " R ",
                "G  ", 'R', obsidianRod, 'G', "gemCrystalFlux").setMirrored(true));
        }

        //Flux Crusher
        GameRegistry.addRecipe(new ShapedUpgradeRecipe(ModItems.fluxCrusher,
                "iFi",
                "DRC",
                " R ", 'i', "ingotElectrumFlux", 'R', fluxRod, 'D', ModItems.resonantDrill, 'C', ModItems.resonantChainsaw, 'F', fluctuatingCore).setMirrored(true));
        GameRegistry.addRecipe(new ShapelessMuffleRecipe(ModItems.fluxCrusher));
        GameRegistry.addRecipe(new ShapelessUnmuffleRecipe(ModItems.fluxCrusher));
    }
}
