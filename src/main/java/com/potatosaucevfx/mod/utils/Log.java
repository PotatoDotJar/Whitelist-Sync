package com.potatosaucevfx.mod.utils;

import com.potatosaucevfx.mod.core.Core;

/**
 * Created by PotatoSauceVFX on 7/27/2017.
 */
public class Log {

    /*
    public static void logPrint(String log) {
        System.out.print(log);
    }
    */

    public static void logln(String log) {
        System.out.println(Core.LOG_PREFIX + " " + log);
    }




}
