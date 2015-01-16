package goldenapple.omnidrills.util;

import org.lwjgl.input.Keyboard;

public class MiscUtil {
    public static boolean isShiftPressed(){
        return Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT);
    }
}
