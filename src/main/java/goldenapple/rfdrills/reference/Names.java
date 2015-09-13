package goldenapple.rfdrills.reference;

import goldenapple.rfdrills.compat.simplyjetpacks.SimplyJetpacksCompat;

public class Names {
    public static final String LEADSTONE_DRILL = "leadstone_drill";
    public static final String HARDENED_DRILL = "hardened_drill";
    public static final String REDSTONE_DRILL = "redstone_drill";
    public static final String RESONANT_DRILL = "resonant_drill";
    public static final String BASIC_DRILL = "basic_drill";
    public static final String ADVANCED_DRILL = "advanced_drill";

    public static final String LEADSTONE_CHAINSAW = "leadstone_chainsaw";
    public static final String HARDENED_CHAINSAW = "hardened_chainsaw";
    public static final String REDSTONE_CHAINSAW = "redstone_chainsaw";
    public static final String RESONANT_CHAINSAW = "resonant_chainsaw";
    public static final String BASIC_CHAINSAW = "basic_chainsaw";
    public static final String ADVANCED_CHAINSAW = "advanced_chainsaw";

    public static final String SOUL_CRUSHER = "soul_crusher";
    public static final String FLUX_CRUSHER = "flux_infused_crusher";
    public static final String FLUX_HOE = "flux_hoe";

    public static final String COMPONENT_TE = "motor_te";
    public static final String[] COMPONENTS_TE = {"leadstone_motor",
                                              "hardened_motor",
                                              "redstone_motor",
                                              "redstone_motor_frame",
                                              "resonant_motor",
                                              "resonant_motor_frame",
                                              "fluctuating_core",
                                              "fluctuating_core_frame",
                                              "superconductance_coil"};
    public static final String COMPONENT_EIO = "motor_eio";
    public static       String[] COMPONENTS_EIO = {"basic_motor",
                                               "advanced_motor",
                                               "resonating_crystal",
                                               "soularium_nugget",
                                               "rich_soularium_nugget",
                                               "destructive_crystal",
                                               "earthshaking_crystal"};
    public static final String REPLACEMENT_SJ = "sj_replacement";
    public static final String[] SJ_REPLACEMENTS = {"dark_soularium"};

    public static final String REPLACEMENT_RA1 = "ra_replacement";
    public static final String[] RA_REPLACEMENTS = {"obsidian_rod",
                                                    "flux_obsidian_rod"};

    static{
        if(!SimplyJetpacksCompat.integratesEIO()) COMPONENTS_EIO[LibMetadata.RICH_SOULARIUM_NUGGET] = "dark_soularium_nugget";
    }
}
