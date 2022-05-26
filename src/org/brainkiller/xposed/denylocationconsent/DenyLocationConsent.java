package org.brainkiller.xposed.denylocationconsent;

import android.content.Context;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings.Secure;
import android.util.Log;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class DenyLocationConsent implements IXposedHookLoadPackage {
    @Override
    public void handleLoadPackage(LoadPackageParam lpparam) throws Throwable {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (lpparam.packageName.equals("com.google.android.gms")) {
                final Class<?> NetworkLocationProvider = XposedHelpers.findClass("com.google.android.location.network.NetworkLocationService", lpparam.classLoader);

                XposedHelpers.findAndHookMethod("com.google.android.location.network.NetworkLocationService", lpparam.classLoader, "a", new XC_MethodReplacement() {
                    @Override
                    protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
                        Context context = (Context) XposedHelpers.getObjectField(param.thisObject, "a");
                        Secure.setLocationProviderEnabled(context.getContentResolver(), "network", false);
                        Log.wtf("GmsNetworkLocationProvi", "Don't be evil");
                        
                        return null;
                    }
                });
            }
        } 
    }
}
