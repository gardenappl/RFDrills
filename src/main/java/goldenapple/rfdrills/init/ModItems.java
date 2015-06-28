package goldenapple.rfdrills.init;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import goldenapple.rfdrills.DrillTier;
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
    public static ItemMultiMetadata RAreplacement = new ItemMultiMetadata(Names.RA_REPLACEMENTS, Names.RA_REPLACEMENT);

    public static Item leadstoneDrill = new ItemDrill(Names.LEADSTONE_DRILL, DrillTier.DRILL1);
    public static Item hardenedDrill = new ItemDrill(Names.HARDENED_DRILL, DrillTier.DRILL2);
    public static Item redstoneDrill = new ItemDrill(Names.REDSTONE_DRILL, DrillTier.DRILL3);
    public static Item resonantDrill = new ItemDrill(Names.RESONANT_DRILL, DrillTier.DRILL4);

    public static Item fluxCrusher = new ItemFluxCrusher();
    public static Item fluxHoe = new ItemFluxHoe();

    public static Item leadstoneChainsaw = new ItemChainsaw(Names.LEADSTONE_CHAINSAW, DrillTier.CHAINSAW1);
    public static Item hardenedChainsaw = new ItemChainsaw(Names.HARDENED_CHAINSAW, DrillTier.CHAINSAW2);
    public static Item redstoneChainsaw = new ItemChainsaw(Names.REDSTONE_CHAINSAW, DrillTier.CHAINSAW3);
    public static Item resonantChainsaw = new ItemChainsaw(Names.RESONANT_CHAINSAW, DrillTier.CHAINSAW4);

    //EnderIO
    public static ItemMultiMetadata componentEIO = new ItemMultiMetadata(Names.COMPONENTS_EIO, Names.COMPONENT_EIO);
    public static ItemMultiMetadata SJreplacement = new ItemMultiMetadata(Names.SJ_REPLACEMENTS, Names.SJ_REPLACEMENT);

    public static Item basicDrill = new ItemDrill(Names.BASIC_DRILL, DrillTier.DRILL1);
    public static Item advancedDrill = new ItemDrill(Names.ADVANCED_DRILL, DrillTier.DRILL3);

    public static Item soulCrusher = new ItemSoulCrusher();

    public static Item basicChainsaw = new ItemChainsaw(Names.BASIC_CHAINSAW, DrillTier.CHAINSAW1);
    public static Item advancedChainsaw = new ItemChainsaw(Names.ADVANCED_CHAINSAW, DrillTier.CHAINSAW3);


    public static void init(){
        GameRegistry.registerItem(fluxHoe, Names.FLUX_HOE);
        if(Loader.isModLoaded("ThermalExpansion") && ConfigHandler.integrateTE) initTE();
        if(Loader.isModLoaded("EnderIO") && ConfigHandler.integrateEIO) initEIO();
    }

    private static void initTE(){
        componentTE.setRarities(new EnumRarity[]{EnumRarity.common, EnumRarity.common, EnumRarity.uncommon, EnumRarity.common, EnumRarity.rare, EnumRarity.common, EnumRarity.rare, EnumRarity.rare, EnumRarity.uncommon});

        RAreplacement.setRarities(new EnumRarity[]{EnumRarity.uncommon, EnumRarity.uncommon});

        GameRegistry.registerItem(componentTE, Names.COMPONENT_TE);

        if((!RFDrills.isRArsLoaded && RFDrills.isSJLoaded) && ConfigHandler.integrateRArs)
            GameRegistry.registerItem(RAreplacement, Names.RA_REPLACEMENT);

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

        SJreplacement.setEffects(new boolean[]{true});
        SJreplacement.setRarities(new EnumRarity[]{EnumRarity.uncommon});

        GameRegistry.registerItem(componentEIO, Names.COMPONENT_EIO);
        OreDictionary.registerOre("nuggetSoularium", new ItemStack(componentEIO, 1, LibMetadata.SOULARIUM_NUGGET));
        OreDictionary.registerOre("nuggetDarkSoularium", new ItemStack(componentEIO, 1, LibMetadata.RICH_SOULARIUM_NUGGET));

        if (RFDrills.isSJLoaded) {
            LogHelper.info("Registering Simply Jetpacks Enriched Soularium Alloy in the OreDictionary as ingotDarkSoularium...");
            OreDictionary.registerOre("ingotDarkSoularium", new ItemStack(GameRegistry.findItem("simplyjetpacks", "components"), 1, 70));
        } else {
            LogHelper.info("Simply Jetpacks not found! Using replacement items...");

            GameRegistry.registerItem(SJreplacement, Names.SJ_REPLACEMENT);
            OreDictionary.registerOre("ingotDarkSoularium", new ItemStack(SJreplacement, 1, LibMetadata.DARK_SOULARIUM));
        }

        GameRegistry.registerItem(basicDrill, Names.BASIC_DRILL);
        GameRegistry.registerItem(advancedDrill, Names.ADVANCED_DRILL);

        GameRegistry.registerItem(basicChainsaw, Names.BASIC_CHAINSAW);
        GameRegistry.registerItem(advancedChainsaw, Names.ADVANCED_CHAINSAW);

        GameRegistry.registerItem(soulCrusher, Names.SOUL_CRUSHER);
    }
}
