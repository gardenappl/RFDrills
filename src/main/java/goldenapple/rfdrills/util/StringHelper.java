package goldenapple.rfdrills.util;

import goldenapple.rfdrills.config.ConfigHandler;
import net.minecraft.util.StatCollector;

public class StringHelper {

    public static String formatEnergy(int energy){
        if(energy >= 10000000){ //at least 10 million
            return (energy / 1000000) + "M";
        }else if(energy >= 10000){ //at least 10 thousand
            return (energy / 1000) + "k";
        }else return Integer.toString(energy);
    }

    public static String writeEnergyPerBlockInfo(int energyPerBlock){
        String energy = formatEnergy(energyPerBlock);
        return StatCollector.translateToLocalFormatted("rfdrills.tooltip.energy_per_block", energy);
    }

    public static String writeEnergyInfo(int energyLevel, int maxEnergy){
        String energy1 = formatEnergy(energyLevel);
        String energy2 = formatEnergy(maxEnergy);
        return StatCollector.translateToLocal("info.cofh.charge") + String.format(": %s / %s RF", energy1, energy2);
    }

    public static String writeShiftInfo(){
        return StatCollector.translateToLocal("info.cofh.hold") + " §e§o" + StatCollector.translateToLocal("info.cofh.shift") + " §r§7" + StatCollector.translateToLocal("info.cofh.forDetails");
    }
}
