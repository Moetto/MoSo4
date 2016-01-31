package moetto.moso4;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
 * Created by moetto on 1/29/16.
 */
public class CallMonitor extends BroadcastReceiver {
    private final static String TAG = "CallMonitor";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "Reveived broadcast");

        if (intent.hasExtra(TelephonyManager.EXTRA_INCOMING_NUMBER)) {
            Log.d(TAG, "Incoming call from number " + intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER));
        }

        if (intent.hasExtra(TelephonyManager.EXTRA_STATE)) {
            Log.d(TAG, "Status: " + intent.getStringExtra(TelephonyManager.EXTRA_STATE));
        }
    }
}
