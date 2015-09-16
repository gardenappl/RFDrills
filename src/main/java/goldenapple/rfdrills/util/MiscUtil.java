package goldenapple.rfdrills.util;

import goldenapple.rfdrills.config.ConfigHandler;
import goldenapple.rfdrills.item.EnumModIntegration;
import goldenapple.rfdrills.item.IEnergyTool;
import net.minecraft.item.ItemStack;
import org.lwjgl.input.Keyboard;

public class MiscUtil {
    public static boolean isShiftPressed(){
        return Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT);
    }

    public static boolean isCtrlPressed(){
        return Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL);
    }

    public static ItemStack setStackSize(ItemStack itemStack, int amount){
        itemStack.stackSize = amount;
        return itemStack;
    }

    public static boolean shouldMakeModeSwitchSound(IEnergyTool tool){
        if(tool.getModType() == EnumModIntegration.TE && ConfigHandler.modeSoundTE)
            return true;
        else if(tool.getModType() == EnumModIntegration.EIO && ConfigHandler.modeSoundEIO)
            return true;

        return false;
    }

    public static boolean shouldModeShiftClick(IEnergyTool tool){
        if(tool.getModType() == EnumModIntegration.TE && ConfigHandler.modeShiftClickTE)
            return true;
        else if(tool.getModType() == EnumModIntegration.EIO && ConfigHandler.modeShiftClickEIO)
            return true;

        return false;
    }
}
