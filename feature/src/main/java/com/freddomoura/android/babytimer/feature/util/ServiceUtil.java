/**
 *
 */
package com.freddomoura.android.babytimer.feature.util;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * @author ricardo
 */
public abstract class ServiceUtil extends Service {

    /* (non-Javadoc)
     * @see android.app.Service#onBind(android.content.Intent)
     */
    @Override
    public IBinder onBind(Intent pArg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int onStartCommand(Intent pIntent, int pFlags, int pStartId) {
        return executeCommand(pIntent, pFlags, pStartId);
    }

    @Override
    public void onStart(Intent pIntent, int pStartId) {
        executeCommand(pIntent, -1, pStartId);
    }

    public abstract int executeCommand(Intent intent, int flags, int startId);

}
