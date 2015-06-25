package goldenapple.rfdrills.util;

import cofh.core.key.IKeyBinding;
import goldenapple.rfdrills.item.soulupgrade.AbstractSoulUpgrade;
import goldenapple.rfdrills.item.soulupgrade.SoulUpgradeHelper;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import org.lwjgl.input.Keyboard;

public class StringHelper {

    public static String formatEnergy(int energy) {
        if (energy >= 10000000) {
            return String.format("%.1fM", (float) energy / 1000000);
        } else if (energy >= 10000) {
            return String.format("%.1fk", (float) energy / 1000);
        } else return Integer.toString(energy);
    }

    public static String writeUpgradeInfo(ItemStack itemStack, AbstractSoulUpgrade upgrade){
        return writeUpgradeInfo(SoulUpgradeHelper.getUpgradeLevel(itemStack, upgrade), upgrade);
    }

    public static String writeUpgradeInfo(int level, AbstractSoulUpgrade upgrade){
        String upgradeName = StatCollector.translateToLocal("rfdrills.upgrade." + upgrade.getUnlocalizedName());
        if(level == 1) {
            return upgradeName;
        }else{
            String levelName = StatCollector.translateToLocal("rfdrills.level." + level);
            return upgradeName + " " + levelName;
        }
    }

    public static String writeEnergyPerBlockInfo(int energy) {
        String energyFormatted = formatEnergy(energy);
        return StatCollector.translateToLocalFormatted("rfdrills.energy_per_block.tooltip", energyFormatted);
    }

    public static String writeEnergyInfo(int energyLevel, int maxEnergy) {
        String energy1 = formatEnergy(energyLevel);
        String energy2 = formatEnergy(maxEnergy);
        return StatCollector.translateToLocal("info.cofh.charge") + String.format(": %s / %s RF", energy1, energy2);
    }

    private static String writeModeSwitchInfo(String unlocalizedName, int key){
        String keyName = key < 0 ? StatCollector.translateToLocalFormatted("key.mouseButton", key + 101) : Keyboard.getKeyName(key); //Stolen from CoFHLib https://github.com/CoFH/CoFHLib/blob/master/src/main/java/cofh/lib/util/helpers/StringHelper.java
        return StatCollector.translateToLocalFormatted(unlocalizedName, keyName);
    }

    public static String writeModeSwitchInfo(String unlocalizedName, KeyBinding keyBinding) { //vanilla bindings
        return writeModeSwitchInfo(unlocalizedName, keyBinding.getKeyCode());
    }

    public static String writeModeSwitchInfo(String unlocalizedName, IKeyBinding keyBinding){ //CoFH bindings
        return writeModeSwitchInfo(unlocalizedName, keyBinding.getKey());
    }
}
