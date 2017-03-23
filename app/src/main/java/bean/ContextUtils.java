/*
package bean;


import android.content.Context;
public class ContextUtils {
    private static volatile Context sMyContext;

    public ContextUtils() {
    }

    public static Context getApplicationContext() {
        Context result = sMyContext;
        if(result == null) {
            Class var1 = ContextUtils.class;
            synchronized(ContextUtils.class) {
                result = sMyContext;
                if(result == null) {
                    try {
                        Object obj = JavaCalls.callStaticMethod("android.app.ActivityThread", "currentActivityThread", new Object[0]);
                        result = (Context)JavaCalls.callMethod(obj, "getApplication", new Object[0]);
                    } catch (Exception var4) {
                        ;
                    }

                    if(result == null) {
                        throw new RuntimeException("My Application havn\'t be call onCreate by Framework.");
                    }

                    sMyContext = result;
                }
            }
        }

        return result;
    }
}
*/
