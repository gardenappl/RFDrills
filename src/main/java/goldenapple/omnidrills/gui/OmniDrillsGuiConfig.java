package goldenapple.omnidrills.gui;

import cpw.mods.fml.client.config.GuiConfig;
import cpw.mods.fml.client.config.IConfigElement;
import goldenapple.omnidrills.OmniDrills;
import goldenapple.omnidrills.reference.Reference;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;

public class OmniDrillsGuiConfig extends GuiConfig{

@SuppressWarnings({"unchecked"})
    public OmniDrillsGuiConfig(GuiScreen parentScreen){
        super(parentScreen,
              new ConfigElement(OmniDrills.configHandler.config.getCategory(Configuration.CATEGORY_GENERAL)).getChildElements(),
              Reference.MOD_ID, false, false, GuiConfig.getAbridgedConfigPath(OmniDrills.configHandler.config.toString()));
    }
}
