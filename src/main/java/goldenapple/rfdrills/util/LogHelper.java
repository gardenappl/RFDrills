package goldenapple.rfdrills.util;

import cpw.mods.fml.common.FMLLog;
import goldenapple.rfdrills.reference.Reference;
import org.apache.logging.log4j.Level;

public class LogHelper
{
    public static void log(Level logLevel, Object object, Object... formatData)
    {
        FMLLog.log(Reference.MOD_NAME, logLevel, String.valueOf(object), formatData);
    }

    public static void off(Object object, Object... formatData)
    {
        log(Level.OFF, object, formatData);
    }

    public static void fatal(Object object, Object... formatData)
    {
        log(Level.FATAL, object, formatData);
    }

    public static void error(Object object, Object... formatData)
    {
        log(Level.ERROR, object, formatData);
    }

    public static void warn(Object object, Object... formatData)
    {
        log(Level.WARN, object, formatData);
    }

    public static void info(Object object, Object... formatData)
    {
        log(Level.INFO, object, formatData);
    }

    public static void debug(Object object, Object... formatData)
    {
        log(Level.DEBUG, object, formatData);
    }

    public static void trace(Object object, Object... formatData)
    {
        log(Level.TRACE, object, formatData);
    }

    public static void all(Object object, Object... formatData)
    {
        log(Level.ALL, object, formatData);
    }


}