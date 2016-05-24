package org.brainkiller.xposed.denylocationconsent;

import android.content.Context;
import android.location.LocationManager;
import android.os.Build;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class DenyLocationConsent implements IXposedHookLoadPackage {
    @Override
    public void handleLoadPackage(LoadPackageParam lpparam) throws Throwable {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (lpparam.packageName.equals("com.google.android.gms")) {
                final Class<?> NetworkLocationChimeraService = XposedHelpers.findClass("com.google.android.location.network.NetworkLocationChimeraService", lpparam.classLoader);

                XposedHelpers.findAndHookMethod("com.google.android.location.network.NetworkLocationChimeraService", lpparam.classLoader, "a", Context.class, new XC_MethodReplacement() {
                    @Override
                    protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
                        Context context = (Context) param.args[0];

                        XposedHelpers.callStaticMethod(NetworkLocationChimeraService, "b", context, false);

                        return null;
                    }
                });
            }
        } 
    }
}
