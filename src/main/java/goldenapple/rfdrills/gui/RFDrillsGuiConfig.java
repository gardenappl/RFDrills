package goldenapple.rfdrills.gui;

import cpw.mods.fml.client.config.GuiConfig;
import goldenapple.rfdrills.RFDrills;
import goldenapple.rfdrills.reference.Reference;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;

public class RFDrillsGuiConfig extends GuiConfig{

@SuppressWarnings({"unchecked"})
    public RFDrillsGuiConfig(GuiScreen parentScreen){
        super(parentScreen,
              new ConfigElement(RFDrills.configHandler.config.getCategory(Configuration.CATEGORY_GENERAL)).getChildElements(),
              Reference.MOD_ID, false, true, GuiConfig.getAbridgedConfigPath(RFDrills.configHandler.config.toString()));
    }
}
