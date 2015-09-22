package goldenapple.rfdrills.client;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import goldenapple.rfdrills.config.ConfigHandler;
import goldenapple.rfdrills.item.IEnergyTool;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderPlayerEvent;

public class PlayerRenderHandler {
    @SubscribeEvent
    public void onPlayerRender(RenderPlayerEvent.Pre event){ //handles 3rd person rendering
        if(ConfigHandler.animationType == 1) {
            if (event.entityPlayer == null || event.entityPlayer.getCurrentEquippedItem() == null)
                return;
            ItemStack equippedStack = event.entityPlayer.getCurrentEquippedItem();

            if (equippedStack.getItem() instanceof IEnergyTool && equippedStack.getItem().getItemUseAction(equippedStack) == EnumAction.bow) {
                if (event.entityPlayer.isSwingInProgress) {
                    event.renderer.modelArmor.aimedBow = true;
                    event.renderer.modelArmorChestplate.aimedBow = true;
                    event.renderer.modelBipedMain.aimedBow = true;
                }
            }
        }
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.PlayerTickEvent event){ //handles both 1st and 3rd person rendering, but in a cheatier way
        if(ConfigHandler.animationType == 2 && event.phase == TickEvent.Phase.END) {
            if (event.side.isServer() || event.player == null || event.player.getCurrentEquippedItem() == null)
                return;
            ItemStack equippedStack = event.player.getCurrentEquippedItem();

            if (equippedStack.getItem() instanceof IEnergyTool && equippedStack.getItem().getItemUseAction(equippedStack) == EnumAction.bow) {
                if(event.player.isSwingInProgress) {
                    event.player.setItemInUse(equippedStack, event.player.ticksExisted); //a bit of a hack here. I need a variable that would increase every tick, otherwise the "bow shaking" effect won't happen.
                    event.player.swingProgress = 0; //disable the normal mining swing animation
                }
            }
        }
    }
}
