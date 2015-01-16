package goldenapple.omnidrills.util;

import goldenapple.omnidrills.config.ConfigHandler;
import goldenapple.omnidrills.config.EnumEnergyUnit;
import net.minecraft.util.StatCollector;

import java.text.DecimalFormat;

public class StringHelper {
    private static DecimalFormat seperator = new DecimalFormat("### ###");

    public static float convertRF(float energy){
        return EnumEnergyUnit.convert(energy, EnumEnergyUnit.RF, ConfigHandler.energyUnit);
    }

    public static String writeEnergyPerBlockInfo(int energyPerBlock){
        String energy = seperator.format((int)convertRF(energyPerBlock));
        String energyUnit = StatCollector.translateToLocal("omnidrills.energyunit." + ConfigHandler.energyUnitName);
        return StatCollector.translateToLocalFormatted("omnidrills.tooltip.energy_per_block", energy, energyUnit);
    }

    public static String writeEnergyInfo(int energyLevel, int maxEnergy){
        String energy1 = seperator.format((int)convertRF(energyLevel));
        String energy2 = seperator.format((int)convertRF(maxEnergy));
        String energyUnit = StatCollector.translateToLocal("omnidrills.energyunit." + ConfigHandler.energyUnitName);
        return StatCollector.translateToLocalFormatted("omnidrills.tooltip.energy", energy1, energy2, energyUnit);
    }
}
