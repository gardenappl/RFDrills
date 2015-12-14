package goldenapple.rfdrills.compat.simplyjetpacks;

import goldenapple.rfdrills.RFDrills;
import goldenapple.rfdrills.util.LogHelper;

import java.lang.reflect.Field;

public class SimplyJetpacksCompat {
    private static Class configClass;

    public static boolean integratesEIO(){
        if(!RFDrills.isSJLoaded)
            return false;
        try{
            Field field = configClass.getDeclaredField("enableIntegrationEIO");
            return field.getBoolean(null);
        }catch(Exception e){
            LogHelper.error("Failed to reflect enableIntegrationEIO in tonius.simplyjetpacks.config.Config!");
            e.printStackTrace();
            return false;
        }
    }

    public static boolean integratesTE(){
        if(!RFDrills.isSJLoaded)
            return false;
        try{
            Field field = configClass.getDeclaredField("enableIntegrationTE");
            return field.getBoolean(null);
        }catch(Exception e){
            LogHelper.error("Failed to reflect enableIntegrationTE in tonius.simplyjetpacks.config.Config!");
            e.printStackTrace();
            return false;
        }
    }

    static{
        if(RFDrills.isSJLoaded) {
            try {
                configClass = Class.forName("tonius.simplyjetpacks.config.Config");
            }catch (Exception e) {
                LogHelper.error("Failed to find class tonius.simplyjetpacks.config.Config");
            }
        }
    }
}
