package goldenapple.rfdrills.compat.versionchecker;

import cpw.mods.fml.common.event.FMLInterModComms;
import goldenapple.rfdrills.reference.Reference;

public class VersionCheckerCompat {
    public static void init(){
        FMLInterModComms.sendRuntimeMessage(Reference.MOD_ID, "VersionChecker", "addVersionCheck", "https://raw.githubusercontent.com/goldenapple3/RFDrills/master/version.json");
    }
}
