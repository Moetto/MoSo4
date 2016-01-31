package moetto.moso4;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.PhoneStateListener;
import android.util.Log;

/**
 * Created by moetto on 1/29/16.
 */
public class CallMonitor extends Service {

    

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}


