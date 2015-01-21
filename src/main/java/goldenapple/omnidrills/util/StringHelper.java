package goldenapple.omnidrills.util;

import goldenapple.omnidrills.config.ConfigHandler;
import goldenapple.omnidrills.config.EnumEnergyUnit;
import net.minecraft.util.StatCollector;

public class StringHelper {
    public static float convertRF(float energy){
        return EnumEnergyUnit.convert(energy, EnumEnergyUnit.RF, ConfigHandler.energyUnit);
    }

    public static String formatEnergy(int energy){
        if(energy >= 10000000){ //at least 10 million
            return (energy / 1000000) + "M";
        }else if(energy >= 10000){ //at least 10 thousand
            return (energy / 1000) + "k";
        }else return Integer.toString(energy); //don't know other ways to convert ints to strings
    }

    public static String writeEnergyPerBlockInfo(int energyPerBlock){
        String energy = formatEnergy((int)convertRF(energyPerBlock));
        String energyUnit = StatCollector.translateToLocal("omnidrills.energyunit." + ConfigHandler.energyUnitName);
        return StatCollector.translateToLocalFormatted("omnidrills.tooltip.energy_per_block", energy, energyUnit);
    }

    public static String writeEnergyInfo(int energyLevel, int maxEnergy){
        String energy1 = formatEnergy((int)convertRF(energyLevel));
        String energy2 = formatEnergy((int)convertRF(maxEnergy));
        String energyUnit = StatCollector.translateToLocal("omnidrills.energyunit." + ConfigHandler.energyUnitName);
        return StatCollector.translateToLocalFormatted("omnidrills.tooltip.energy", energy1, energy2, energyUnit);
    }
}
