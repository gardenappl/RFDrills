package goldenapple.rfdrills.util;

import goldenapple.rfdrills.config.ConfigHandler;
import goldenapple.rfdrills.item.EnumModType;
import goldenapple.rfdrills.item.IEnergyTool;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import org.lwjgl.input.Keyboard;

public class MiscUtil {
    public static boolean isShiftPressed(){
        return Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT);
    }

    public static boolean isCtrlPressed(){
        return Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL);
    }

    public static boolean isItemSilent(ItemStack stack){
        if(!stack.hasTagCompound())
            stack.stackTagCompound = new NBTTagCompound();
        return stack.stackTagCompound.getBoolean("isSilent");
    }

    public static boolean shouldModeShiftClick(IEnergyTool tool){
        if(tool.getModType() == EnumModType.TE && ConfigHandler.modeShiftClickTE)
            return true;
        else if(tool.getModType() == EnumModType.EIO && ConfigHandler.modeShiftClickEIO)
            return true;

        return false;
    }
}
