package goldenapple.rfdrills.util;

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
}
