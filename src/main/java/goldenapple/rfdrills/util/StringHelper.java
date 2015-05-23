package goldenapple.rfdrills.util;

import net.minecraft.util.StatCollector;

public class StringHelper {

    public static String formatEnergy(int energy){
        if(energy >= 10000000){
            return String.format("%.1fM", (float)energy / 1000000);
        }else if(energy >= 10000){
            return String.format("%.1fk", (float)energy / 1000);
        }else return Integer.toString(energy);
    }

    public static String writeEnergyPerBlockInfo(int energy){
        String energyFormatted = formatEnergy(energy);
        return StatCollector.translateToLocalFormatted("rfdrills.energy_per_block.tooltip", energyFormatted);
    }

    public static String writeEnergyInfo(int energyLevel, int maxEnergy){
        String energy1 = formatEnergy(energyLevel);
        String energy2 = formatEnergy(maxEnergy);
        return StatCollector.translateToLocal("info.cofh.charge") + String.format(": %s / %s RF", energy1, energy2);
    }
}
