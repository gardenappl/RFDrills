package goldenapple.rfdrills.gui;

import cpw.mods.fml.client.config.DummyConfigElement;
import cpw.mods.fml.client.config.GuiConfig;
import cpw.mods.fml.client.config.IConfigElement;
import goldenapple.rfdrills.config.ConfigHandler;
import goldenapple.rfdrills.reference.Reference;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;

import java.util.ArrayList;
import java.util.List;

public class RFDrillsGuiConfig extends GuiConfig{ //@author ljfa

@SuppressWarnings({"unchecked"})
    public RFDrillsGuiConfig(GuiScreen parentScreen){
        super(parentScreen, getConfigElements(), Reference.MOD_ID, false, true, GuiConfig.getAbridgedConfigPath(ConfigHandler.config.toString()));
    }

    //Compiles a list of config elements
    private static List<IConfigElement> getConfigElements() {
        List<IConfigElement> list = new ArrayList<IConfigElement>();

        //Add categories to config GUI
        list.add(categoryElement(Configuration.CATEGORY_GENERAL, StatCollector.translateToLocal("config.general"), "config.general"));
        list.add(categoryElement("drill_tier1", StatCollector.translateToLocal("config.drill_tier1"), "config.drill_tier1"));
        list.add(categoryElement("drill_tier2", StatCollector.translateToLocal("config.drill_tier2"), "config.drill_tier2"));
        list.add(categoryElement("drill_tier3", StatCollector.translateToLocal("config.drill_tier3"), "config.drill_tier3"));
        list.add(categoryElement("drill_tier4", StatCollector.translateToLocal("config.drill_tier4"), "config.drill_tier4"));

        list.add(categoryElement("drill_tier5", StatCollector.translateToLocal("config.flux_crusher"), "config.flux_crusher"));
        list.add(categoryElement("soulcrusher", StatCollector.translateToLocal("config.soul_crusher"), "config.soul_crusher"));
        list.add(categoryElement("hoe_tier1", StatCollector.translateToLocal("config.hoe"), "config.hoe"));

        list.add(categoryElement("chainsaw_tier1", StatCollector.translateToLocal("config.chainsaw_tier1"), "config.chainsaw_tier1"));
        list.add(categoryElement("chainsaw_tier2", StatCollector.translateToLocal("config.chainsaw_tier2"), "config.chainsaw_tier2"));
        list.add(categoryElement("chainsaw_tier3", StatCollector.translateToLocal("config.chainsaw_tier3"), "config.chainsaw_tier3"));
        list.add(categoryElement("chainsaw_tier4", StatCollector.translateToLocal("config.chainsaw_tier4"), "config.chainsaw_tier4"));
        return list;
    }

    /** Creates a button linking to another screen where all options of the category are available */
    @SuppressWarnings("unchecked")
    private static IConfigElement categoryElement(String category, String name, String tooltip_key) {
        return new DummyConfigElement.DummyCategoryElement(name, tooltip_key,
                new ConfigElement(ConfigHandler.config.getCategory(category)).getChildElements());
    }
}
