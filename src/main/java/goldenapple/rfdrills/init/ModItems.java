package goldenapple.rfdrills.init;

import cpw.mods.fml.common.registry.GameRegistry;
import goldenapple.rfdrills.compat.simplyjetpacks.SimplyJetpacksCompat;
import goldenapple.rfdrills.item.ToolTier;
import goldenapple.rfdrills.RFDrills;
import goldenapple.rfdrills.config.ConfigHandler;
import goldenapple.rfdrills.item.*;
import goldenapple.rfdrills.reference.LibMetadata;
import goldenapple.rfdrills.reference.Names;
import goldenapple.rfdrills.util.LogHelper;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class ModItems {
    //Thermal Expansion
    public static ItemMultiMetadata componentTE = new ItemMultiMetadata(Names.COMPONENTS_TE, Names.COMPONENT_TE);
    public static ItemMultiMetadata replacementRA1 = new ItemMultiMetadata(Names.RA_REPLACEMENTS, Names.REPLACEMENT_RA1);

    public static Item leadstoneDrill = new ItemDrill(Names.LEADSTONE_DRILL, ToolTier.DRILL1).setModType(EnumModType.TE);
    public static Item hardenedDrill = new ItemDrill(Names.HARDENED_DRILL, ToolTier.DRILL2).setModType(EnumModType.TE);
    public static Item redstoneDrill = new ItemDrill(Names.REDSTONE_DRILL, ToolTier.DRILL3).setModType(EnumModType.TE);
    public static Item resonantDrill = new ItemDrill(Names.RESONANT_DRILL, ToolTier.DRILL4).setModType(EnumModType.TE);

    public static Item fluxCrusher = new ItemFluxCrusher();
    public static Item fluxHoe = new ItemFluxHoe();

    public static Item leadstoneChainsaw = new ItemChainsaw(Names.LEADSTONE_CHAINSAW, ToolTier.CHAINSAW1);
    public static Item hardenedChainsaw = new ItemChainsaw(Names.HARDENED_CHAINSAW, ToolTier.CHAINSAW2);
    public static Item redstoneChainsaw = new ItemChainsaw(Names.REDSTONE_CHAINSAW, ToolTier.CHAINSAW3);
    public static Item resonantChainsaw = new ItemChainsaw(Names.RESONANT_CHAINSAW, ToolTier.CHAINSAW4);

    //EnderIO
    public static ItemMultiMetadata componentEIO = new ItemMultiMetadata(Names.COMPONENTS_EIO, Names.COMPONENT_EIO);
    public static ItemMultiMetadata replacementSJ = new ItemMultiMetadata(Names.SJ_REPLACEMENTS, Names.REPLACEMENT_SJ);

    public static Item basicDrill = new ItemDrill(Names.BASIC_DRILL, ToolTier.DRILL1);
    public static Item advancedDrill = new ItemDrill(Names.ADVANCED_DRILL, ToolTier.DRILL3);

    public static Item soulCrusher = new ItemSoulCrusher();

    public static Item basicChainsaw = new ItemChainsaw(Names.BASIC_CHAINSAW, ToolTier.CHAINSAW1);
    public static Item advancedChainsaw = new ItemChainsaw(Names.ADVANCED_CHAINSAW, ToolTier.CHAINSAW3);

    public static void init(){
        if(RFDrills.isTELoaded && ConfigHandler.integrateTE) initTE();
        if(RFDrills.isEIOLoaded && ConfigHandler.integrateEIO) initEIO();
        if((RFDrills.isTELoaded && ConfigHandler.integrateTE) || (RFDrills.isEIOLoaded && ConfigHandler.integrateEIO))
            GameRegistry.registerItem(fluxHoe, Names.FLUX_HOE);
    }

    private static void initTE(){
        componentTE.setRarities(new EnumRarity[]{EnumRarity.common, EnumRarity.common, EnumRarity.uncommon, EnumRarity.common, EnumRarity.rare, EnumRarity.common, EnumRarity.rare, EnumRarity.rare, EnumRarity.uncommon});

        replacementRA1.setRarities(new EnumRarity[]{EnumRarity.uncommon, EnumRarity.uncommon});

        GameRegistry.registerItem(componentTE, Names.COMPONENT_TE);

        if((!RFDrills.isRArsLoaded && SimplyJetpacksCompat.integratesTE()) && ConfigHandler.integrateRArs)
            GameRegistry.registerItem(replacementRA1, Names.REPLACEMENT_RA1);

        GameRegistry.registerItem(leadstoneDrill, Names.LEADSTONE_DRILL);
        GameRegistry.registerItem(hardenedDrill, Names.HARDENED_DRILL);
        GameRegistry.registerItem(redstoneDrill, Names.REDSTONE_DRILL);
        GameRegistry.registerItem(resonantDrill, Names.RESONANT_DRILL);

        GameRegistry.registerItem(leadstoneChainsaw, Names.LEADSTONE_CHAINSAW);
        GameRegistry.registerItem(hardenedChainsaw, Names.HARDENED_CHAINSAW);
        GameRegistry.registerItem(redstoneChainsaw, Names.REDSTONE_CHAINSAW);
        GameRegistry.registerItem(resonantChainsaw, Names.RESONANT_CHAINSAW);

        if(ConfigHandler.integrateRArs)
            GameRegistry.registerItem(fluxCrusher, Names.FLUX_CRUSHER);
    }

    private static void initEIO(){
        componentEIO.setEffects(new boolean[]{false, false, true, false, true, true, true});
        componentEIO.setRarities(new EnumRarity[]{EnumRarity.common, EnumRarity.uncommon, EnumRarity.uncommon, EnumRarity.common, EnumRarity.uncommon, EnumRarity.uncommon, EnumRarity.rare});
        componentEIO.setTooltips(new String[][]{null, null, null, null, null, new String[]{"rfdrills.soul_upgrade.tooltip"}, new String[]{"rfdrills.soul_upgrade.tooltip"}});

        replacementSJ.setEffects(new boolean[]{true});
        replacementSJ.setRarities(new EnumRarity[]{EnumRarity.uncommon});

        GameRegistry.registerItem(componentEIO, Names.COMPONENT_EIO);
        OreDictionary.registerOre("nuggetSoularium", new ItemStack(componentEIO, 1, LibMetadata.SOULARIUM_NUGGET));
        OreDictionary.registerOre("nuggetDarkSoularium", new ItemStack(componentEIO, 1, LibMetadata.RICH_SOULARIUM_NUGGET));

        if (SimplyJetpacksCompat.integratesEIO()) {
            LogHelper.info("Registering Simply Jetpacks Enriched Soularium Alloy in the OreDictionary as ingotDarkSoularium...");
            OreDictionary.registerOre("ingotDarkSoularium", new ItemStack(GameRegistry.findItem("simplyjetpacks", "components"), 1, 70));
        } else {
            LogHelper.info("Simply Jetpacks EIO items not found! Using replacement items...");
            GameRegistry.registerItem(replacementSJ, Names.REPLACEMENT_SJ);
            OreDictionary.registerOre("ingotDarkSoularium", new ItemStack(replacementSJ, 1, LibMetadata.DARK_SOULARIUM));
        }

        GameRegistry.registerItem(basicDrill, Names.BASIC_DRILL);
        GameRegistry.registerItem(advancedDrill, Names.ADVANCED_DRILL);

        GameRegistry.registerItem(basicChainsaw, Names.BASIC_CHAINSAW);
        GameRegistry.registerItem(advancedChainsaw, Names.ADVANCED_CHAINSAW);

        GameRegistry.registerItem(soulCrusher, Names.SOUL_CRUSHER);
    }
}
